package com.kuznetsov.linoleumShopRest.errorResponse;

public class LinoleumValidationErrorResponse extends ErrorResponse{
    public LinoleumValidationErrorResponse(String message, Long timestamp) {
        super(message, timestamp);
    }
}
