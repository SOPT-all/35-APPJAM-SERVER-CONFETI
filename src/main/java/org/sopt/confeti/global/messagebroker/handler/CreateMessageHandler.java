package org.sopt.confeti.global.messagebroker.handler;

import org.sopt.confeti.global.messagebroker.message.CreateMessage;

public interface CreateMessageHandler<T extends CreateMessage> extends MessageHandler<T> {

}

