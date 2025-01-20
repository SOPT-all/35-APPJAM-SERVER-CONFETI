package org.sopt.confeti.api.user.dto.request;

import java.util.List;

public record AddTimetableFestivalRequest(
        List<AddTimetableFestivalArtistRequest> festivals
) {
}
