package org.sopt.confeti.api.user.facade.dto.request;

import org.sopt.confeti.api.user.dto.request.PatchTimetableListRequest;

public record PatchTimetableListDTO (
        long userTimetableId,
        boolean isSelected
){
    public static PatchTimetableListDTO from(final PatchTimetableListRequest patchTimetableListRequest) {
        return new PatchTimetableListDTO(
            patchTimetableListRequest.userTimetableId(),
            patchTimetableListRequest.isSelected()
        );
    }
}

