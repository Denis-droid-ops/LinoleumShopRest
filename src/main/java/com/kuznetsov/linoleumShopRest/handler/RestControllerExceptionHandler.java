package com.kuznetsov.linoleumShopRest.handler;


import com.kuznetsov.linoleumShopRest.errorResponse.LinoleumErrorResponse;
import com.kuznetsov.linoleumShopRest.exception.LinoleumNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "com.kuznetsov.linoleumShopRest.controller")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<LinoleumErrorResponse> handleException(LinoleumNotFoundException ex){
        LinoleumErrorResponse linoleumErrorResponse =
                new LinoleumErrorResponse(ex.getMessage(),System.currentTimeMillis());
        return new ResponseEntity<>(linoleumErrorResponse, HttpStatus.NOT_FOUND);
    }
}
