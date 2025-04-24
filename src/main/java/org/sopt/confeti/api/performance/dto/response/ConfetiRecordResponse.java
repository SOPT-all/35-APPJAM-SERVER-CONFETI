package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.ConfetiRecordDTO;

public record ConfetiRecordResponse(
        long totalCount,
        long timetableCount,
        long setlistCount
) {
    public static ConfetiRecordResponse from(ConfetiRecordDTO recordDTO){
        return new ConfetiRecordResponse(
                recordDTO.totalCount(),
                recordDTO.timetableCount(),
                recordDTO.setlistCount()
        );
    }
}
