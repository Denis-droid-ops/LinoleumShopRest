package com.kuznetsov.linoleumShopRest.exception;

public class LinoleumNotFoundException extends RuntimeException{

    public LinoleumNotFoundException() {
        super("Linoleum is null, not found");
    }

    public LinoleumNotFoundException(String message) {
        super(message);
    }
}
