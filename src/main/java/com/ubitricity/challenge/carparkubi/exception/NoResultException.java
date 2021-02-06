package com.ubitricity.challenge.carparkubi.exception;

import org.springframework.http.HttpStatus;

public class NoResultException extends ServiceResponseException {

    public NoResultException(String message) {
        super(message);
    }

    public NoResultException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getHttpStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}
