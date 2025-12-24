package org.sopt.confeti.global.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.converter.StringToPerformanceTypeConverter;
import org.sopt.confeti.global.interceptor.CustomInterceptor;
import org.sopt.confeti.global.interceptor.InterceptorOrder;
import org.sopt.confeti.global.resolver.auth.TokenArgumentResolver;
import org.sopt.confeti.global.resolver.auth.UserIdArgumentResolver;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer, WebMvcRegistrations {

    private final UserIdArgumentResolver userIdArgumentResolver;
    private final TokenArgumentResolver tokenArgumentResolver;
    private final List<CustomInterceptor> customInterceptors;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdArgumentResolver);
        resolvers.add(tokenArgumentResolver);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToPerformanceTypeConverter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        customInterceptors.forEach(interceptor -> {
            int order = InterceptorOrder.getOrder(interceptor.getClass());
            registry.addInterceptor((HandlerInterceptor) interceptor).order(order);
        });
    }

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new ApiVersionRequestMappingHandlerMapping();
    }
}
