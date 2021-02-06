package com.ubitricity.challenge.carparkubi.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;

public abstract class ServiceResponseException extends Exception {

    private ObjectNode data;

    public ServiceResponseException(String message) {
        super(message);
    }

    public ServiceResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract HttpStatus getHttpStatusCode();


    public JsonNode getData() {
        return data;
    }


}

