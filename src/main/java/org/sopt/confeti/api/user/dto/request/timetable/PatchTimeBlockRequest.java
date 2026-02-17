package org.sopt.confeti.api.user.dto.request.timetable;

public record PatchTimeBlockRequest(
    long timeBlockId,
    boolean isSelected
) {

}
