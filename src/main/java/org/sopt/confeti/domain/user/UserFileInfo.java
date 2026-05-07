package org.sopt.confeti.domain.user;

import lombok.Builder;

@Builder
public record UserFileInfo(
    String profileUrl
) {

}
