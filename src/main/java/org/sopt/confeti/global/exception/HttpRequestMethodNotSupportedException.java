package org.sopt.confeti.global.exception;

import org.sopt.confeti.global.message.ErrorMessage;

public class HttpRequestMethodNotSupportedException extends ConfetiException{
    public HttpRequestMethodNotSupportedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
