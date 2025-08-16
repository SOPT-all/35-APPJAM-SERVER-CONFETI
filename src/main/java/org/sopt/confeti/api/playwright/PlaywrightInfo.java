package org.sopt.confeti.api.playwright;

public record PlaywrightInfo(
        String url,
        Integer x,
        Integer y,
        Integer width,
        Integer height
) {}
