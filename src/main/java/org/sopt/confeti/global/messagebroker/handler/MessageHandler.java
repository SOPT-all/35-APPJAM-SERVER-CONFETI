package org.sopt.confeti.global.messagebroker.handler;

import org.sopt.confeti.global.messagebroker.message.Message;

public interface MessageHandler<T extends Message> {

    void handle(T message);

    Class<T> getSupportedType();

    default String getSupportedTypeId() {
        return getSupportedType().getName();
    }

    default boolean isSupported(Class<?> clazz) {
        return getSupportedType().equals(clazz);
    }

}
