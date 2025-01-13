package org.sopt.confeti.global.exception;

import org.sopt.confeti.global.message.ErrorMessage;

public class MethodArgumentTypeMismatchException extends ConfetiException{
    public MethodArgumentTypeMismatchException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
