package org.sopt.confeti.global.exception;

import org.sopt.confeti.global.message.ErrorMessage;

public class ConfetiException extends RuntimeException {
    private final ErrorMessage errorMessage;

    public ConfetiException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage=errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
