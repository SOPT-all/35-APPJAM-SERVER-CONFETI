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

            page.waitForLoadState(LoadState.LOAD);

            // 콘솔 메시지 대기를 위한 플래그
            final boolean[] isContentReady = {false};
            final Object lock = new Object();

            page.onConsoleMessage(consoleMessage -> {
                String messageText = consoleMessage.text();
                log.info("[PNG] 콘솔 메시지: {}", messageText);

                // 특정 메시지 패턴 확인 (사이트에 맞게 수정)
                if (messageText.contains("페이지 로딩 완료") ||
                        messageText.contains("content-loaded") ||
                        messageText.contains("render-complete")) {

                    synchronized (lock) {
                        isContentReady[0] = true;
                        lock.notify();
                    }
                    log.info("[PNG] 페이지 준비 완료 메시지 감지");
                }
            });

            long waitStart = System.nanoTime();
            synchronized (lock) {
                try {
                    if (!isContentReady[0]) {
                        lock.wait(10000); // 10초 타임아웃
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("[PNG] 콘솔 메시지 대기 중 인터럽트 발생");
                }
            }

            if (isContentReady[0]) {
                log.info("[PNG] 콘솔 메시지 대기 완료. ({} ms)", elapsed(waitStart));
            } else {
                log.warn("[PNG] 콘솔 메시지 타임아웃, 기본 대기로 진행. ({} ms)", elapsed(waitStart));
                page.waitForTimeout(2000); // 대체 대기
            }

            long pngGenStart = System.nanoTime();
            byte[] pngBytes = page.screenshot(
                    new Page.ScreenshotOptions()
                            .setType(ScreenshotType.PNG)
                            .setFullPage(false)
                            .setOmitBackground(false)
                            .setClip(command.getX(), command.getY(), command.getWidth(), command.getHeight())
            );

            log.info("[PNG] PNG 생성 완료. ({} ms)", elapsed(pngGenStart));

            return pngBytes;
        }
    }

    private long elapsed(long startTime) {
        return (System.nanoTime() - startTime) / 1_000_000;
    }
}
