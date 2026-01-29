package org.sopt.confeti.api.user.facade.dto.request;

import org.sopt.confeti.api.user.dto.request.PatchTimetableListRequest;

public record PatchTimeBlockListDTO(
        long timeBlockId,
        boolean isSelected
) {
    public static PatchTimeBlockListDTO from(final PatchTimetableListRequest patchTimetableListRequest) {
        return new PatchTimeBlockListDTO(
                patchTimetableListRequest.userTimetableId(),
                patchTimetableListRequest.isSelected()
        );
    }
}

