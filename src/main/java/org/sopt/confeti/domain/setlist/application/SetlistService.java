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
import org.sopt.confeti.domain.setlist.application.dto.request.AddSetListMusicRequest;
import org.sopt.confeti.domain.setlist.application.dto.request.SetlistCreateRequest;
import org.sopt.confeti.domain.setlist.application.dto.response.GetAllSetlistsResponse;
import org.sopt.confeti.domain.setlist.application.dto.response.SetlistSummaryDto;
import org.sopt.confeti.domain.setlist.infra.repository.SetlistRepository;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.infra.repository.UserRepository;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
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

    @Transactional(readOnly = true)
    public GetAllSetlistsResponse getAllMySetlists(Long userId, SetlistSortType sortType) {
        List<Setlist> setlists = setlistRepository.findAllByUserId(userId);

        List<SetlistSummaryDto> dtoList = setlists.stream()
                .map(setlist -> {
                    if (setlist.getType() == SetlistType.CONCERT) {
                        Concert concert = concertRepository.findById(setlist.getTypeId())
                                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
                        return SetlistSummaryDto.of(
                                setlist, concert.getTitle(), concert.getPosterPath(), concert.getEndAt());
                    } else {
                        Festival festival = festivalRepository.findById(setlist.getTypeId())
                                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
                        return SetlistSummaryDto.of(
                                setlist, festival.getTitle(), festival.getPosterPath(), festival.getEndAt());
                    }
                })
                .sorted((a, b) -> {
                    if (sortType == SetlistSortType.OLDEST) {
                        return a.endAt().compareTo(b.endAt());
                    } else {
                        return b.endAt().compareTo(a.endAt());
                    }
                })
                .toList();

        return new GetAllSetlistsResponse(dtoList.size(), dtoList);
    }

    @Transactional(readOnly = true)
    public List<SetlistSummaryDto> getPreviewMySetlists(Long userId) {
        List<Setlist> setlists = setlistRepository.findAllByUserId(userId);

        return setlists.stream()
                .map(setlist -> {
                    if (setlist.getType() == SetlistType.CONCERT) {
                        Concert concert = concertRepository.findById(setlist.getTypeId())
                                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
                        return SetlistSummaryDto.of(
                                setlist, concert.getTitle(), concert.getPosterPath(), concert.getEndAt());
                    } else {
                        Festival festival = festivalRepository.findById(setlist.getTypeId())
                                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
                        return SetlistSummaryDto.of(
                                setlist, festival.getTitle(), festival.getPosterPath(), festival.getEndAt());
                    }
                })
                .sorted(Comparator.comparing(SetlistSummaryDto::endAt))
                .limit(3)
                .toList();
    }

    @Transactional
    public List<Long> createSetLists(Long userId, List<SetlistCreateRequest> requests) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        return requests.stream()
                .filter(req -> !setlistRepository.existsByUserIdAndTypeAndTypeId(userId, req.type(), req.typeId()))
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
    public int addMusics(Long userId, Long setlistId, List<AddSetListMusicRequest> requests) {
        Setlist setlist = setlistRepository.findById(setlistId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        if (!setlist.getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED);
        }

        int startOrder = setlist.getMusics().size() + 1;

        for (int i = 0; i < requests.size(); i++) {
            AddSetListMusicRequest req = requests.get(i);
            SetlistMusic music = SetlistMusic.builder()
                    .trackId(req.trackId())
                    .artistName(req.artistName())
                    .trackName(req.trackName())
                    .artworkUrl(req.artworkUrl())
                    .previewUrl(req.previewUrl())
                    .orders(startOrder + i)
                    .build();

            setlist.addMusics(music);
        }

        return requests.size();
    }
}
