package org.sopt.confeti.global.exception;

import org.sopt.confeti.global.message.ErrorMessage;

public class UnauthorizedException extends ConfetiException {
    public UnauthorizedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
