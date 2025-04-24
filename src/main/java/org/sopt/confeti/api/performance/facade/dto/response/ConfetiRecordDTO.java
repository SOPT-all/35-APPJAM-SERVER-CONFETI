package org.sopt.confeti.api.performance.facade.dto.response;

public record ConfetiRecordDTO(
        long totalCount,
        long timetableCount,
        long setlistCount
) {
    public static ConfetiRecordDTO of(final long totalCount, final long timetableCount, final long setlistCount) {
        return new ConfetiRecordDTO(
                totalCount, timetableCount, setlistCount
        );
    }
}