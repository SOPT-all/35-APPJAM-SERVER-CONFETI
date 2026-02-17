package org.sopt.confeti.api.user.facade.dto.request.timetable;

import org.sopt.confeti.api.user.dto.request.timetable.PatchTimeBlockRequest;

public record PatchTimeBlockDTO(
    long timeBlockId,
    boolean isSelected
) {

    public static PatchTimeBlockDTO from(final PatchTimeBlockRequest patchTimeBlockRequest) {
        return new PatchTimeBlockDTO(
            patchTimeBlockRequest.timeBlockId(),
            patchTimeBlockRequest.isSelected()
        );
    }
}
