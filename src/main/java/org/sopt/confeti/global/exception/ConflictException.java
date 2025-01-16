package org.sopt.confeti.global.exception;

import org.sopt.confeti.global.message.ErrorMessage;

public class ConflictException extends ConfetiException {
    public ConflictException(ErrorMessage errorMessage) { super(errorMessage); }
}