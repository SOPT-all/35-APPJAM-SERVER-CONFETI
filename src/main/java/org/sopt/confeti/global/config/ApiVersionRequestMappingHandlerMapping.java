package org.sopt.confeti.global.config;

import java.lang.reflect.Method;
import org.jetbrains.annotations.NotNull;
import org.sopt.confeti.global.annotation.ApiVersion;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class ApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestMappingInfo getMappingForMethod(@NotNull Method method,
        @NotNull Class<?> handlerType) {
        RequestMappingInfo mappingInfo = super.getMappingForMethod(method, handlerType);

        if (mappingInfo != null) {
            ApiVersion apiVersion = AnnotatedElementUtils.findMergedAnnotation(method,
                ApiVersion.class);
            if (apiVersion != null) {
                RequestMappingInfo versionMappingInfo = RequestMappingInfo
                    .paths("/" + apiVersion.value())
                    .build();
                mappingInfo = versionMappingInfo.combine(mappingInfo);
            }
        }

        return mappingInfo;
    }
}
