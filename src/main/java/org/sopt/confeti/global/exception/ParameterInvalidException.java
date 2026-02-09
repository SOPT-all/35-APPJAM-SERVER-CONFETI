package org.sopt.confeti.global.exception;

import lombok.Getter;
import org.sopt.confeti.global.message.ErrorMessage;

@Getter
public class ParameterInvalidException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public ParameterInvalidException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}
