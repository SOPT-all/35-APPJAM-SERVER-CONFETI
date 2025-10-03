package org.sopt.confeti.global.messagebroker;

public interface Event {

    default String getTypeId() {
        return this.getClass().getName();
    }
}
