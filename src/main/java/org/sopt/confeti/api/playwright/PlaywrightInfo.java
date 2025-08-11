package org.sopt.confeti.api.playwright;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaywrightInfo {

    private String url;

    private Integer width;

    private Integer height;

}
