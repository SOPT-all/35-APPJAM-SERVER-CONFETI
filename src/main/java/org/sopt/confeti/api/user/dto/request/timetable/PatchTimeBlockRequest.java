package org.sopt.confeti.api.user.dto.request.timetable;

public record PatchTimeBlockRequest(
    long timeBlockId,
    boolean isSelected
) {

    public static PatchTimeBlockRequest from(PatchTimeBlockRequest request) {
        return new PatchTimeBlockRequest(request.timeBlockId(), request.isSelected());
    }
}
