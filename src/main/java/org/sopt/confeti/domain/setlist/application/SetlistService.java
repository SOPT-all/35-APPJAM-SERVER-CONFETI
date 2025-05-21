package org.sopt.confeti.domain.setlist.application;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.infra.repository.ConcertRepository;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.infra.repository.FestivalRepository;
import org.sopt.confeti.domain.setlist.Setlist;
import org.sopt.confeti.domain.setlist.SetlistMusic;
import org.sopt.confeti.domain.setlist.SetlistSortType;
import org.sopt.confeti.domain.setlist.SetlistType;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistAddMusicDTO;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistCreateRequestDTO;
import org.sopt.confeti.api.setlist.dto.response.GetAllSetlistsResponse;
import org.sopt.confeti.api.setlist.dto.response.GetSetlistDetailResponse;
import org.sopt.confeti.api.setlist.dto.response.SetlistMusicResponse;
import org.sopt.confeti.api.setlist.dto.response.SetlistSummaryResponse;
import org.sopt.confeti.domain.setlist.infra.repository.SetlistMusicRepository;
import org.sopt.confeti.domain.setlist.infra.repository.SetlistRepository;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.infra.repository.UserRepository;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.domain.view.performance.application.PerformanceService;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SetlistService {

    private final SetlistRepository setlistRepository;
    private final ConcertRepository concertRepository;
    private final FestivalRepository festivalRepository;
    private final UserRepository userRepository;
    private final SetlistMusicRepository setlistMusicRepository;
    private final S3FileHandler s3FileHandler;
    private final PerformanceService performanceService;

    private static final int MAX_SETLIST_PREVIEW_COUNT = 3;

    @Transactional(readOnly = true)
    public GetAllSetlistsResponse getAllMySetlists(Long userId, SetlistSortType sortType) {
        List<Setlist> setlists = setlistRepository.findAllByUserId(userId);

        List<SetlistSummaryResponse> dtoList = setlists.stream()
                .map(setlist -> {
                    Performance performance = performanceService.getPerformanceByTypeAndTypeId(
                            PerformanceType.valueOf(setlist.getType().name()), setlist.getTypeId());
                    return SetlistSummaryResponse.of(setlist, performance, s3FileHandler);
                })
                .sorted((a , b) -> sortType == SetlistSortType.OLDEST
                        ? a.endAt().compareTo(b.endAt()) : b.endAt().compareTo(a.endAt()))
                .toList();

        return new GetAllSetlistsResponse(dtoList.size(), dtoList);
    }

    @Transactional(readOnly = true)
    public List<SetlistSummaryResponse> getPreviewMySetlists(Long userId) {
        List<Setlist> setlists = setlistRepository.findAllByUserId(userId);

        return setlists.stream()
                .map(setlist -> {
                    Performance performance = performanceService.getPerformanceByTypeAndTypeId(
                            PerformanceType.valueOf(setlist.getType().name()), setlist.getTypeId());
                    return SetlistSummaryResponse.of(setlist, performance, s3FileHandler);
                })
                .sorted(Comparator.comparing(SetlistSummaryResponse::endAt))
                .limit(MAX_SETLIST_PREVIEW_COUNT)
                .toList();
    }

    @Transactional
    public List<Long> createSetLists(User user, List<SetlistCreateRequestDTO> requests) {
        return requests.stream()
                .filter(req -> !setlistRepository.existsByUserIdAndTypeAndTypeId(user.getId(), req.type(), req.typeId()))
                .map(req -> Setlist.builder()
                        .user(user)
                        .type(req.type())
                        .typeId(req.typeId())
                        .build())
                .map(setlistRepository::save)
                .map(Setlist::getId)
                .toList();
    }

    @Transactional
    public int addMusics(Long userId, Long setlistId, List<SetlistAddMusicDTO> requests) {
        Setlist setlist = setlistRepository.findByIdAndUserId(userId, setlistId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        List<String> existingIds = setlist.getMusics().stream()
                .map(SetlistMusic::getTrackId)
                .toList();

        int startOrder = setlist.getMusics().size() + 1;
        int addedCount = 0;

        for (SetlistAddMusicDTO req : requests) {
            if(existingIds.contains(req.trackId())) continue;

            SetlistMusic music = SetlistMusic.builder()
                    .trackId(req.trackId())
                    .artistName(req.artistName())
                    .trackName(req.trackName())
                    .artworkUrl(req.artworkUrl())
                    .previewUrl(req.previewUrl())
                    .orders(startOrder++)
                    .build();

            setlist.addMusics(music);
            addedCount++;
        }

        return addedCount;
    }

    public GetSetlistDetailResponse getSetlistDetail(Long userId, Long setlistId) {
        Setlist setlist = setlistRepository.findByIdAndUserId(setlistId, userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        List<SetlistMusicResponse> musics = setlistMusicRepository.findBySetlist(setlist).stream()
                .sorted(Comparator.comparing(SetlistMusic::getOrders))
                .map(m -> new SetlistMusicResponse(
                        m.getId(), m.getTrackId(), m.getArtistName(),
                        m.getTrackName(), m.getArtworkUrl(), m.getPreviewUrl(), m.getOrders()
                ))
                .toList();

        if (setlist.getType() == SetlistType.CONCERT) {
            Concert concert = concertRepository.findById(setlist.getTypeId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
            return GetSetlistDetailResponse.of(
                    setlist, concert.getTitle(), concert.getSubtitle(),
                    concert.getPosterPath(),
                    concert.getStartAt(), concert.getEndAt(),
                    musics, setlist.getType(), s3FileHandler
            );
        } else {
            Festival festival = festivalRepository.findById(setlist.getTypeId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
            return GetSetlistDetailResponse.of(
                    setlist, festival.getTitle(), festival.getSubtitle(),
                    festival.getPosterPath(),
                    festival.getStartAt(), festival.getEndAt(),
                    musics, setlist.getType(), s3FileHandler
            );
        }
    }

    @Transactional(readOnly = true)
    public List<Long> findFestivalIdsByUserId(final Long userId) {
        return setlistRepository.findFestivalIdsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Long> findConcertIdsByUserId(final Long userId) {
        return setlistRepository.findConcertIdsByUserId(userId);
    }
}
