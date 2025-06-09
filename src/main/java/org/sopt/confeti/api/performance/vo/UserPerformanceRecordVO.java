package org.sopt.confeti.api.performance.vo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record UserPerformanceRecordVO(
        List<Long> timetableFestivalIds,
        List<Long> setListFestivalIds,
        List<Long> setListConcertIds
) {
    public int getTotalUniquePerformanceCount() {
        Set<Long> uniqueFestivalIds = new HashSet<>(timetableFestivalIds);
        uniqueFestivalIds.addAll(setListFestivalIds);
        return uniqueFestivalIds.size() + setListConcertIds.size();
    }

    public int getTimetableFestivalCount() {
        return timetableFestivalIds.size();
    }

    public int getSetListPerformanceCount() {
        return setListFestivalIds.size() + setListConcertIds.size();
    }
}