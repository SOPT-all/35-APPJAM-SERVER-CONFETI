package org.sopt.confeti.global.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.auth.jwt.JwtTokenExtractor;
import org.sopt.confeti.auth.jwt.TokenParser;
import org.sopt.confeti.global.annotation.interceptor.ErrorNotificationInterceptor;
import org.sopt.confeti.global.annotation.interceptor.PermissionInterceptor;
import org.sopt.confeti.global.converter.StringToPerformanceTypeConverter;
import org.sopt.confeti.global.notification.SlackNotificationAgent;
import org.sopt.confeti.global.resolver.user.UserIdArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserIdArgumentResolver userIdArgumentResolver;
    private final SlackNotificationAgent slackNotificationAgent;
    private final JwtTokenExtractor jwtTokenExtractor;
    private final TokenParser tokenParser;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdArgumentResolver);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToPerformanceTypeConverter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ErrorNotificationInterceptor(slackNotificationAgent));
        registry.addInterceptor(new PermissionInterceptor(jwtTokenExtractor, tokenParser));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/static/swagger-ui/");
    }
}
