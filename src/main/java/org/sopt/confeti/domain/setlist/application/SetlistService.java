package org.sopt.confeti.domain.setlist.application;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.infra.repository.ConcertRepository;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.infra.repository.FestivalRepository;
import org.sopt.confeti.domain.setlist.Setlist;
import org.sopt.confeti.domain.setlist.SetlistSortType;
import org.sopt.confeti.domain.setlist.SetlistType;
import org.sopt.confeti.domain.setlist.application.dto.request.SetlistCreateRequest;
import org.sopt.confeti.domain.setlist.application.dto.response.GetAllSetlistsResponse;
import org.sopt.confeti.domain.setlist.application.dto.response.SetlistSummaryDto;
import org.sopt.confeti.domain.setlist.infra.repository.SetlistRepository;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.infra.repository.UserRepository;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
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
                .map(req -> Setlist.builder()
                        .user(user)
                        .type(req.type())
                        .typeId(req.typeId())
                        .build())
                .map(setlistRepository::save)
                .map(Setlist::getId)
                .toList();
    }
}
