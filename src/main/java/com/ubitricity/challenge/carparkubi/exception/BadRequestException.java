package com.ubitricity.challenge.carparkubi.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ServiceResponseException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getHttpStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
