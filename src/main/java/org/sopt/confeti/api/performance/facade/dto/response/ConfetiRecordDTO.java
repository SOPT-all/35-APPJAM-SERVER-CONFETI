package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.api.performance.vo.UserPerformanceRecordVO;

public record ConfetiRecordDTO(
        long totalCount,
        long timetableCount,
        long setlistCount
) {
    public static ConfetiRecordDTO from(UserPerformanceRecordVO record) {
        return new ConfetiRecordDTO(
                record.getTotalUniquePerformanceCount(),
                record.getTimetableFestivalCount(),
                record.getSetListPerformanceCount()
        );
    }
}