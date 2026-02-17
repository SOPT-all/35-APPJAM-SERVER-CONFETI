package org.sopt.confeti.api.user.dto.request.timetable;

import java.util.List;

public record PatchTimeBlocksRequest(
    List<PatchTimeBlockRequest> timeBlocks
) {

}
