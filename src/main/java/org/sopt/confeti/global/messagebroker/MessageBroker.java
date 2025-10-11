package org.sopt.confeti.global.messagebroker;

import org.sopt.confeti.global.messagebroker.message.Event;

public interface MessageBroker {

    void sendEvent(Event event);

}
