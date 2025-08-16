package org.sopt.confeti.api.playwright;

import io.jsonwebtoken.lang.Assert;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaywrightCommand {
    private String url;
    private String accessToken;
    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;

    @Builder
    public PlaywrightCommand(String url, String accessToken, Integer x, Integer y, Integer width, Integer height) {
        Assert.notNull(url, "url is required.");
        Assert.notNull(accessToken, "accessToken is required.");
        Assert.notNull(x, "x is required.");
        Assert.notNull(y, "y is required.");
        Assert.notNull(width, "width is required.");
        Assert.notNull(height, "height is required.");

        this.url = url;
        this.accessToken = accessToken;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
