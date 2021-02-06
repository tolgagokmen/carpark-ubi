package com.ubitricity.challenge.carparkubi.exception;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ErrorModel> handle(HttpServletRequest request, ServiceResponseException e) {
        ErrorModel errorModel = new ErrorModel(e.getMessage(), e.getHttpStatusCode(), getRequestUrl(request), e.getData());
        return new ResponseEntity<>(errorModel, errorModel.getHttpStatusCode());
    }

    private String getRequestUrl(HttpServletRequest request) {
        if (request == null) {
            return "No HTTP Request";
        } else {
            return request.getRequestURL().toString();
        }
    }
}
