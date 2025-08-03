package org.sopt.confeti.global.annotation.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.annotation.Interceptor;
import org.sopt.confeti.global.notification.SlackNotificationAgent;
import org.sopt.confeti.global.notification.SlackNotificationType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@Interceptor
@RequiredArgsConstructor
public class ErrorNotificationInterceptor implements HandlerInterceptor {

    private static final int MAX_STACK_TRACE_LENGTH = 3000;
    private final SlackNotificationAgent slackNotificationAgent;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        if (handler instanceof ResourceHttpRequestHandler) {
            return;
        }


        boolean shouldNotify = ex != null || response.getStatus() >= 400;

        if (shouldNotify) {

            if (ex != null) {
                String errorDetails = buildErrorDetails(request, ex, response.getStatus());
                slackNotificationAgent.notify(SlackNotificationType.CRITICAL_ERROR, errorDetails);
                return;
            }

            Exception exception = (Exception) request.getAttribute("exception");

            int status = response.getStatus();
            if (status >= 500) {
                String errorDetails = buildErrorDetails(request, exception, status);
                slackNotificationAgent.notify(SlackNotificationType.CRITICAL_ERROR, errorDetails);
            } else if (status >= 400) {
                String errorDetails = buildErrorDetails(request, exception, status);
                slackNotificationAgent.notify(SlackNotificationType.HIGH_ERROR, errorDetails);
            }
        }
    }

    private String makeRequestInfo(HttpServletRequest request, Object handler) {
        StringBuilder info = new StringBuilder();
        info.append("🔍 요청 정보\n");
        info.append("• URL: ").append(request.getMethod()).append(" ").append(request.getRequestURI()).append("\n");
        info.append("• Query: ").append(request.getQueryString()).append("\n");
        info.append("• Remote IP: ").append(getClientIpAddress(request)).append("\n");

        if (handler instanceof HandlerMethod handlerMethod) {
            info.append("• Controller: ").append(handlerMethod.getBeanType().getSimpleName()).append("\n");
            info.append("• Method: ").append(handlerMethod.getMethod().getName()).append("\n");
        }

        return info.toString();
    }

    private String buildErrorDetails(HttpServletRequest request, Exception ex, int status) {
        StringBuilder details = new StringBuilder();
        details.append("🚨 **에러 발생**\n");
        details.append(makeRequestInfo(request, null));
        details.append("• Status Code: ").append(status).append("\n");
        details.append("• Exception Type: ").append(ex.getClass().getSimpleName()).append("\n");
        details.append("• Error Message: ").append(ex.getMessage()).append("\n");
        details.append("• Stack Trace:\n```\n").append(getStackTrace(ex)).append("\n```");

        return details.toString();
    }

    private String getStackTrace(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);

        // 스택트레이스가 너무 길면 앞 부분만 잘라서 반환
        String fullStackTrace = sw.toString();
        if (fullStackTrace.length() > MAX_STACK_TRACE_LENGTH) {
            return fullStackTrace.substring(0, MAX_STACK_TRACE_LENGTH) + "\n... (truncated)";
        }
        return fullStackTrace;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String[] headerNames = {
                "X-Forwarded-For",
                "X-Real-IP",
                "X-Original-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        for (String headerName : headerNames) {
            String ip = request.getHeader(headerName);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // 첫 번째 IP 주소만 반환 (여러 개가 있을 경우)
                return ip.split(",")[0].trim();
            }
        }

        return request.getRemoteAddr();
    }
}
