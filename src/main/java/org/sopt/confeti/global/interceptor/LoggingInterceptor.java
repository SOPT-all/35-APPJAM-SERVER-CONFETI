package org.sopt.confeti.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.annotation.Interceptor;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Interceptor
public class LoggingInterceptor implements HandlerInterceptor, CustomInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        log.info("request: {} {}", request.getMethod(), request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex) throws Exception {
        if (ex != null) {
            log.error("response: {} - error: {}", response.getStatus(), ex.getMessage());
        } else {
            log.info("response: {}", response.getStatus());
        }
    }
}
