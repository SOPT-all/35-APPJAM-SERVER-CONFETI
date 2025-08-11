package org.sopt.confeti.api.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Cookie;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SameSiteAttribute;
import com.microsoft.playwright.options.ScreenshotType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaywrightService implements PlaywrightUseCase{


    @Override
    public byte[] generatePng(PlaywrightCommand command) {
        long startTime = System.nanoTime();
        try (
                Playwright playwright = Playwright.create();
                Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chromium"));
                BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                        .setViewportSize(command.getWidth(), 1080)
                        .setJavaScriptEnabled(true));
                Page page = context.newPage();
        ) {
            Cookie cookie = new Cookie("accessToken", command.getAccessToken())
                    .setDomain("www.confeti.co.kr")
                    .setPath("/")
                    .setHttpOnly(false)
                    .setSecure(true)
                    .setSameSite(SameSiteAttribute.NONE);
            context.addCookies(List.of(cookie));

            log.info("[PNG] Playwright 초기화 완료. ({} ms)", elapsed(startTime));

            long navStart = System.nanoTime();
            page.navigate(command.getUrl());
            log.info("[PNG] 페이지 네비게이션 완료. ({} ms)", elapsed(navStart));

            long loadStart = System.nanoTime();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            log.info("[PNG] 페이지 로드 완료. ({} ms)", elapsed(loadStart));

            page.waitForTimeout(4000);
            log.info("[PNG] waitForTimeout(2000) 완료");

            long pngGenStart = System.nanoTime();
            byte[] pngBytes = page.screenshot(
                    new Page.ScreenshotOptions()
                            .setType(ScreenshotType.PNG)
                            .setFullPage(false)
                            .setOmitBackground(false)
                            .setClip(0, 0, command.getWidth(), command.getHeight())
            );

            log.info("[PNG] PNG 생성 완료. ({} ms)", elapsed(pngGenStart));

            return pngBytes;
        }
    }

    private long elapsed(long startTime) {
        return (System.nanoTime() - startTime) / 1_000_000;
    }
}
