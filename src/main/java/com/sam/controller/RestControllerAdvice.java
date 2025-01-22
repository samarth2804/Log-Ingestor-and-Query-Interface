package com.sam.controller;

import com.sam.exception.ApiException;
import com.sam.exception.ErrorCode;
import com.sam.exception.ErrorData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestControllerAdvice {

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ErrorData handleUnknownException(HttpServletRequest req, Throwable t) {
        t.printStackTrace();
        return ErrorData.builder()
                .code(ErrorCode.INTERNAL_SERVER_ERROR)
                .message("Internal Error")
                .build();
    }

    @ExceptionHandler(ApiException.class)
    @ResponseBody
    public ErrorData handleApiException(HttpServletRequest req, ApiException e, HttpServletResponse response) {
        e.printStackTrace();
        setResponseStatus(response, e.getErrorCode());
        return ErrorData.builder()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorData handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletResponse response) {
        FieldError fieldError = e.getBindingResult().getFieldErrors().stream().findAny().get();
        setResponseStatus(response, ErrorCode.BAD_REQUEST);
        return ErrorData.builder()
                .code(ErrorCode.BAD_REQUEST)
                .message(fieldError.getDefaultMessage())
                .build();

    }

    private void setResponseStatus(HttpServletResponse response, ErrorCode errorCode) {
        switch (errorCode) {
            case BAD_REQUEST:
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                break;
            case INTERNAL_SERVER_ERROR:
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
