package org.sopt.confeti.global.exception;

import org.sopt.confeti.global.message.ErrorMessage;

public class UnauthorizedException extends ConfetiException {
    public UnauthorizedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }


    public static UnauthorizedException wrong() {
        return new UnauthorizedException(ErrorMessage.WRONG_TOKEN);
    }

    public static UnauthorizedException expired() {
        return new UnauthorizedException(ErrorMessage.EXPIRED_TOKEN);
    }

    public static UnauthorizedException empty() {
        return new UnauthorizedException(ErrorMessage.EMPTY_TOKEN);
    }
}
