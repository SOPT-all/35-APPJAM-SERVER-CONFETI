package org.sopt.confeti.global.annotation;

import java.lang.reflect.Method;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Verify that the version value is correctly set when @ApiVersion is specified.
 */
@Component
public class ApiVersionValidator implements ApplicationRunner {

    private static final String VERSION_REGEX = "^v[1-9]\\d*$";
    private static final Pattern VERSION_PATTERN = Pattern.compile(VERSION_REGEX);

    private final RequestMappingHandlerMapping handlerMapping;

    public ApiVersionValidator(
        @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @Override
    public void run(ApplicationArguments args) {
        handlerMapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
            validateApiVersion(handlerMethod);
        });
    }

    private void validateApiVersion(HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        ApiVersion apiVersion = method.getAnnotation(ApiVersion.class);

        if (apiVersion != null) {
            validateVersion(apiVersion.value(), method.getDeclaringClass().getSimpleName(),
                method.getName());
        }
    }

    private void validateVersion(String version, String className, String methodName) {
        try {
            validateNotBlank(version);
            validateFormat(version);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(
                String.format("%s [%s.%s]", e.getMessage(), className, methodName));
        }
    }

    private void validateNotBlank(String version) {
        if (!StringUtils.hasText(version)) {
            throw new IllegalStateException("@ApiVersion의 version 값은 필수입니다.");
        }
    }

    private void validateFormat(String version) {
        if (!VERSION_PATTERN.matcher(version).matches()) {
            throw new IllegalStateException(
                "@ApiVersion의 version 값이 올바르지 않습니다. 정규식 (" + VERSION_REGEX + ")를 따라야 합니다.");
        }
    }
}
