package com.kuznetsov.linoleumShopRest.exception;

public class ImageNotFoundException extends RuntimeException{

    public ImageNotFoundException() {
        super("Image is null, not found");
    }

    public ImageNotFoundException(String message) {
        super(message);
    }
}
