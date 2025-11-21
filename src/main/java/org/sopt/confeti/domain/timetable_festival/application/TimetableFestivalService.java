package org.sopt.confeti.domain.timetable_festival.application;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.timetable_festival.TimetableFestival;
import org.sopt.confeti.domain.timetable_festival.TimetableFestivalCursor.CursorData;
import org.sopt.confeti.domain.timetable_festival.infra.repository.TimetableFestivalRepository;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.global.common.CursorPage;
import org.sopt.confeti.global.common.constant.PerformanceStatus;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TimetableFestivalService {


    private static final int INIT_PAGE = 0;
    private static final int GET_TIMETABLES_SIZE_WITH_CURSOR = 10 + 1;
    private static final String START_AT_COLUMN = "startAt";

    private final TimetableFestivalRepository timetableFestivalRepository;

    @Transactional(readOnly = true)
    public TimetableFestival getWithFestival(Long timetableFestivalId) {
        return timetableFestivalRepository.findByIdWithFestival(timetableFestivalId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<TimetableFestival> getFetivalList(long userId) {
        return timetableFestivalRepository.findByUserIdWhereEndAtLENow(userId);
    }

    @Transactional(readOnly = true)
    public boolean existsByUserIdAndFestivalId(final long userId, final long festivalId) {
        return timetableFestivalRepository.existsByUserIdAndFestivalId(userId, festivalId);
    }

    @Transactional
    public void removeTimetableFestival(final long userId, final long festivalId) {
        timetableFestivalRepository.deleteByUserIdAndFestivalId(userId, festivalId);
    }

    @Transactional
    public void removeTimetableFestivals(
        final long userId,
        final Collection<Long> deleteFestivalId
    ) {
        timetableFestivalRepository.deleteAllByUserIdAndFestivalIdIn(userId, deleteFestivalId);
    }

    @Transactional
    public void addTimetableFestivals(final User user, final List<Festival> festivals) {
        timetableFestivalRepository.saveAll(
            festivals.stream()
                .map(festival -> TimetableFestival.create(user, festival))
                .toList()
        );
    }

    @Deprecated
    @Transactional(readOnly = true)
    public List<TimetableFestival> getTimetables(final long userId, final String sortBy) {
        List<TimetableFestival> festivals = timetableFestivalRepository.findByUserId(userId);

        if ("createdAt".equalsIgnoreCase(sortBy)) {
            festivals.sort(Comparator.comparing(TimetableFestival::getCreatedAt).reversed());
        } else if ("oldestFirst".equalsIgnoreCase(sortBy)) {
            festivals.sort(Comparator.comparing(TimetableFestival::getCreatedAt));
        }

        return festivals;
    }

    @Transactional(readOnly = true)
    public CursorPage<TimetableFestival> getTimetablesEarliest(long userId, CursorData cursor,
        PerformanceStatus performanceStatus) {
        List<TimetableFestival> timetables = Optional.ofNullable(cursor)
            .map(cursorData ->
                timetableFestivalRepository.findAllUsingCursorOrderByStartAtAsc(
                    userId, cursor, performanceStatus, GET_TIMETABLES_SIZE_WITH_CURSOR
                )
            ).orElseGet(() ->
                timetableFestivalRepository.findAllOrderByStartAtAsc(
                    userId, performanceStatus, GET_TIMETABLES_SIZE_WITH_CURSOR
                )
            );

        return CursorPage.of(
            timetables,
            GET_TIMETABLES_SIZE_WITH_CURSOR
        );
    }

    @Transactional(readOnly = true)
    public CursorPage<TimetableFestival> getTimetablesLatest(long userId, CursorData cursor,
        PerformanceStatus performanceStatus) {
        List<TimetableFestival> timetables = Optional.ofNullable(cursor)
            .map(cursorData ->
                timetableFestivalRepository.findAllUsingCursorOrderByStartAtDesc(
                    userId, cursor, performanceStatus, GET_TIMETABLES_SIZE_WITH_CURSOR
                )
            ).orElseGet(() ->
                timetableFestivalRepository.findAllOrderByStartAtDesc(
                    userId, performanceStatus, GET_TIMETABLES_SIZE_WITH_CURSOR
                )
            );

        return CursorPage.of(
            timetables,
            GET_TIMETABLES_SIZE_WITH_CURSOR
        );
    }

    @Transactional(readOnly = true)
    public List<TimetableFestival> getTimetablesPreview(final long userId) {
        return timetableFestivalRepository.findTop4ByUserIdOrderByCreatedAt(userId);
    }

    @Transactional(readOnly = true)
    public List<Long> findFestivalIdsByUserId(final Long userId) {
        return timetableFestivalRepository.findFestivalIdsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public TimetableFestival getEntireFestivalInfo(final long userId, final long festivalId) {
        return timetableFestivalRepository.findByUserIdAndFestivalId(userId, festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
    }

    private PageRequest getPageRequestWithSort(final int size, final Sort sort) {
        return PageRequest.of(INIT_PAGE, size, sort);
    }
}
