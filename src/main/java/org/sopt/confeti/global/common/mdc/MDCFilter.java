package org.sopt.confeti.global.common.mdc;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class MDCFilter implements Filter {

    private static final String TRACE_ID_KEY = "TRACE_ID";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        MDC.put(TRACE_ID_KEY, UUID.randomUUID().toString());
        chain.doFilter(request, response);
    }
}
