package com.ubitricity.challenge.carparkubi.exception;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends ServiceResponseException {

    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getHttpStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
