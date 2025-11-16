package org.sopt.confeti.global.messagebroker;

import org.sopt.confeti.global.messagebroker.message.Message;

public interface MessageBroker {

    void sendMessage(Message message);

}
