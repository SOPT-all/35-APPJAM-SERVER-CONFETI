package org.sopt.confeti.global.common.mdc;

import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

/**
 * &#064;Async와  같은 비동기 요청 시 해당 스레드에 trace id를 전달한다.
 *
 * @see MDCFilter
 */
public class MDCTaskDecorator implements TaskDecorator {

    @NotNull
    @Override
    public Runnable decorate(@NotNull Runnable runnable) {
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();

        return () -> {
            MDC.setContextMap(copyOfContextMap);
            runnable.run();
        };
    }
}
