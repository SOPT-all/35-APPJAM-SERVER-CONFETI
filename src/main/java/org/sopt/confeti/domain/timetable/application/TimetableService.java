package org.sopt.confeti.domain.timetable.application;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.timetable.Timetable;
import org.sopt.confeti.domain.timetable.TimetableCursor.CursorData;
import org.sopt.confeti.domain.timetable.infra.repository.TimetableRepository;
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
public class TimetableService {


    private static final int INIT_PAGE = 0;
    private static final int GET_TIMETABLES_SIZE_WITH_CURSOR = 10 + 1;
    private static final String START_AT_COLUMN = "startAt";

    private final TimetableRepository timetableRepository;

    @Transactional(readOnly = true)
    public Timetable getWithFestival(Long timetableId) {
        return timetableRepository.findByIdWithFestival(timetableId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<Timetable> getFetivalList(long userId) {
        return timetableRepository.findByUserIdWhereEndAtLENow(userId);
    }

    @Transactional(readOnly = true)
    public boolean existsByUserIdAndFestivalId(final long userId, final long festivalId) {
        return timetableRepository.existsByUserIdAndFestivalId(userId, festivalId);
    }

    @Transactional
    public void removeTimetable(final long userId, final long festivalId) {
        timetableRepository.deleteByUserIdAndFestivalId(userId, festivalId);
    }

    @Transactional
    public void removeTimetables(
        final long userId,
        final Collection<Long> deleteTimeTableIds
    ) {
        timetableRepository.deleteAllByUserIdAndIdIn(userId, deleteTimeTableIds);
    }

    @Transactional
    public void addTimetables(final User user, final List<Festival> festivals) {
        timetableRepository.saveAll(
            festivals.stream()
                .map(festival -> Timetable.create(user, festival))
                .toList()
        );
    }

    @Deprecated
    @Transactional(readOnly = true)
    public List<Timetable> getTimetables(final long userId, final String sortBy) {
        List<Timetable> timetables = timetableRepository.findByUserId(userId);

        if ("createdAt".equalsIgnoreCase(sortBy)) {
            timetables.sort(Comparator.comparing(Timetable::getCreatedAt).reversed());
        } else if ("oldestFirst".equalsIgnoreCase(sortBy)) {
            timetables.sort(Comparator.comparing(Timetable::getCreatedAt));
        }

        return timetables;
    }

    @Transactional(readOnly = true)
    public CursorPage<Timetable> getTimetablesEarliest(long userId, CursorData cursor,
        PerformanceStatus performanceStatus) {
        List<Timetable> timetables = Optional.ofNullable(cursor)
            .map(cursorData ->
                timetableRepository.findAllUsingCursorOrderByStartAtAsc(
                    userId, cursor, performanceStatus, GET_TIMETABLES_SIZE_WITH_CURSOR
                )
            ).orElseGet(() ->
                timetableRepository.findAllOrderByStartAtAsc(
                    userId, performanceStatus, GET_TIMETABLES_SIZE_WITH_CURSOR
                )
            );

        return CursorPage.of(
            timetables,
            GET_TIMETABLES_SIZE_WITH_CURSOR
        );
    }

    @Transactional(readOnly = true)
    public CursorPage<Timetable> getTimetablesLatest(long userId, CursorData cursor,
        PerformanceStatus performanceStatus) {
        List<Timetable> timetables = Optional.ofNullable(cursor)
            .map(cursorData ->
                timetableRepository.findAllUsingCursorOrderByStartAtDesc(
                    userId, cursor, performanceStatus, GET_TIMETABLES_SIZE_WITH_CURSOR
                )
            ).orElseGet(() ->
                timetableRepository.findAllOrderByStartAtDesc(
                    userId, performanceStatus, GET_TIMETABLES_SIZE_WITH_CURSOR
                )
            );

        return CursorPage.of(
            timetables,
            GET_TIMETABLES_SIZE_WITH_CURSOR
        );
    }

    @Transactional(readOnly = true)
    public List<Timetable> getTimetablesPreview(final long userId) {
        return timetableRepository.findTop4ByUserIdOrderByCreatedAt(userId);
    }

    @Transactional(readOnly = true)
    public List<Long> findFestivalIdsByUserId(final Long userId) {
        return timetableRepository.findFestivalIdsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Timetable getEntireFestivalInfo(final long userId, final long festivalId) {
        return timetableRepository.findByUserIdAndFestivalId(userId, festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
    }

    private PageRequest getPageRequestWithSort(final int size, final Sort sort) {
        return PageRequest.of(INIT_PAGE, size, sort);
    }
}
