package org.sopt.confeti.global.messagebroker.handler;

import org.sopt.confeti.global.messagebroker.message.CreateEvent;

public interface CreateEventHandler<T extends CreateEvent> extends EventHandler<T> {

}

