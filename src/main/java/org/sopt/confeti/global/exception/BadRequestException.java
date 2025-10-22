package org.sopt.confeti.global.exception;

import org.sopt.confeti.global.message.ErrorMessage;

public class BadRequestException extends ConfetiException {

    public BadRequestException(ErrorMessage message) {
        super(message);
    }
}
