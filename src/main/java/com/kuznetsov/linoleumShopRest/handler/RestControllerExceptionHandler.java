package com.kuznetsov.linoleumShopRest.handler;


import com.kuznetsov.linoleumShopRest.errorResponse.ImageErrorResponse;
import com.kuznetsov.linoleumShopRest.errorResponse.LinoleumErrorResponse;
import com.kuznetsov.linoleumShopRest.errorResponse.LinoleumValidationErrorResponse;
import com.kuznetsov.linoleumShopRest.exception.ImageNotFoundException;
import com.kuznetsov.linoleumShopRest.exception.LinoleumNotFoundException;
import com.kuznetsov.linoleumShopRest.exception.LinoleumValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "com.kuznetsov.linoleumShopRest.controller")
@Slf4j
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<LinoleumErrorResponse> handleException(LinoleumNotFoundException ex){
        log.info("Start handling exception, ex is {}",ex);
        LinoleumErrorResponse linoleumErrorResponse =
                new LinoleumErrorResponse(ex.getMessage(),System.currentTimeMillis());
        return new ResponseEntity<>(linoleumErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ImageErrorResponse> handleException(ImageNotFoundException ex){
        log.info("Start handling exception, ex is {}",ex);
        ImageErrorResponse imageErrorResponse =
                new ImageErrorResponse(ex.getMessage(),System.currentTimeMillis());
        return new ResponseEntity<>(imageErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<LinoleumValidationErrorResponse> handleException(LinoleumValidationException ex){
        log.info("Start handling exception, ex is {}",ex);
        StringBuilder sb = new StringBuilder("Error! ");
        ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .forEach(message->sb.append(message).append("   "));
        LinoleumValidationErrorResponse linoleumValidationErrorResponse =
                new LinoleumValidationErrorResponse(sb.toString(),System.currentTimeMillis());
        return new ResponseEntity<>(linoleumValidationErrorResponse, HttpStatus.NOT_FOUND);
    }


}
