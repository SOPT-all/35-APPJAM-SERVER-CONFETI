package org.sopt.confeti.global.common.constant;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.domain.timetable_festival.TimetableFestival;
import org.sopt.confeti.domain.timetable_festival.TimetableFestivalCursor.CursorData;
import org.sopt.confeti.domain.timetable_festival.application.TimetableFestivalService;
import org.sopt.confeti.global.common.CursorPage;

/**
 * @see org.sopt.confeti.global.converter.TimetableSortTypeConverter
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public enum TimetableSortType {
    EARLIEST("earliest") {
        @Override
        public CursorPage<TimetableFestival> getTimetableCursorPage(
            TimetableFestivalService timetableFestivalService, long userId, CursorData cursor,
            PerformanceStatus performanceStatus) {
            return timetableFestivalService.getTimetablesEarliest(userId, cursor,
                performanceStatus);
        }
    },
    LATEST("latest") {
        @Override
        public CursorPage<TimetableFestival> getTimetableCursorPage(
            TimetableFestivalService timetableFestivalService, long userId, CursorData cursor,
            PerformanceStatus performanceStatus) {
            return timetableFestivalService.getTimetablesLatest(userId, cursor, performanceStatus);
        }
    };

    private final String name;

    public static TimetableSortType getDefault() {
        return EARLIEST;
    }

    public static Optional<TimetableSortType> from(String name) {
        return Arrays.stream(TimetableSortType.values())
            .filter(sortType -> sortType.name.equalsIgnoreCase(name))
            .findFirst();
    }

    public abstract CursorPage<TimetableFestival> getTimetableCursorPage(
        TimetableFestivalService timetableFestivalService, long userId, CursorData cursor,
        PerformanceStatus performanceStatus);
}
