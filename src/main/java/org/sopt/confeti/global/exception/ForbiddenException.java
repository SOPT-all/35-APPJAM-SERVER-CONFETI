package org.sopt.confeti.global.exception;

import org.sopt.confeti.global.message.ErrorMessage;

public class ForbiddenException extends ConfetiException {

    public ForbiddenException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
