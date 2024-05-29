package com.kuznetsov.linoleumShopRest.errorResponse;

public class RevisionErrorResponse extends ErrorResponse{
    public RevisionErrorResponse(String message, Long timestamp) {
        super(message, timestamp);
    }
}
