package org.sopt.confeti.global.messagebroker.handler;

import org.sopt.confeti.global.messagebroker.message.Event;

public interface EventHandler<T extends Event> {

    void handle(T event);

    Class<T> getSupportedType();

    default String getSupportedTypeId() {
        return getSupportedType().getName();
    }

    default boolean isSupported(Class<?> clazz) {
        return getSupportedType().equals(clazz);
    }

}
