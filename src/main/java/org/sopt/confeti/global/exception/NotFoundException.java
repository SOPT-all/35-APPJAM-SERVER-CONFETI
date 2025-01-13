package org.sopt.confeti.global.exception;

import org.sopt.confeti.global.message.ErrorMessage;

public class NotFoundException extends ConfetiException {
    public NotFoundException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
