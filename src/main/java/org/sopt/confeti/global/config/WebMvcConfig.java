package org.sopt.confeti.global.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.interceptor.CustomInterceptor;
import org.sopt.confeti.global.converter.StringToPerformanceTypeConverter;
import org.sopt.confeti.global.resolver.auth.TokenArgumentResolver;
import org.sopt.confeti.global.resolver.auth.UserIdArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

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
            registry.addInterceptor((HandlerInterceptor) interceptor);
        });
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/static/swagger-ui/");
    }
}
