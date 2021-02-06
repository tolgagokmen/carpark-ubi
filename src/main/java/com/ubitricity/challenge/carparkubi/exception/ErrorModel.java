package com.ubitricity.challenge.carparkubi.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

class ErrorModel {

    private final String error;

    private final String errorMessage;

    private final HttpStatus statusCode;

    private final Long timestamp;

    private final String requestUrl;

    private final Object data;

    ErrorModel(String errorMessage, HttpStatus statusCode, String requestUrl, Object data) {
        this.error = statusCode.getReasonPhrase();
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.timestamp = System.currentTimeMillis();
        this.requestUrl = requestUrl;
        this.data = data;
    }

    @JsonIgnore
    HttpStatus getHttpStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getPath() {
        return requestUrl;
    }

    public int getStatusCode() {
        return statusCode.value();
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Object getData() {
        return data;
    }
}
