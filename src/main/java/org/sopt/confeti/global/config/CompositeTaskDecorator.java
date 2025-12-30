package org.sopt.confeti.global.config;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.task.TaskDecorator;

/**
 * Composite class for applying multiple TaskDecorators
 */
public final class CompositeTaskDecorator implements TaskDecorator {

    private final List<TaskDecorator> decorators;

    private CompositeTaskDecorator(List<TaskDecorator> decorators) {
        this.decorators = decorators.reversed();
    }

    public static CompositeTaskDecorator with(TaskDecorator... taskDecorators) {
        return new CompositeTaskDecorator(List.of(taskDecorators));
    }

    @NotNull
    @Override
    public Runnable decorate(@NotNull Runnable runnable) {
        Runnable decorated = runnable;

        for (TaskDecorator decorator : decorators) {
            decorated = decorator.decorate(decorated);
        }

        return decorated;
    }
}
