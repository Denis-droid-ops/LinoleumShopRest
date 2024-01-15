package com.kuznetsov.linoleumShopRest.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class LinoleumValidationException extends RuntimeException{

    private BindingResult bindingResult;

    public LinoleumValidationException() {
        super("Linoleum validation exception");
    }

    public LinoleumValidationException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}
