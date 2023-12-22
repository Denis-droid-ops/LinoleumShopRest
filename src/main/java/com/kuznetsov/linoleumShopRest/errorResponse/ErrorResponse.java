package com.kuznetsov.linoleumShopRest.errorResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public abstract class ErrorResponse {
    private String message;
    private Long timestamp;
}
