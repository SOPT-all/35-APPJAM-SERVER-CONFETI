package org.sopt.confeti.api.user.dto.request;

public record PatchTimetableListRequest (
        long userTimetableId,
        boolean isSelected
){
    public static PatchTimetableListRequest from(PatchTimetableListRequest request) {
        return new PatchTimetableListRequest(request.userTimetableId(), request.isSelected());
    }
}
