package com.kuznetsov.linoleumShopRest.dto;

public record CreateEditLinoleumDto(String lName,
                                Float protect,
                                Float thickness,
                                Integer price,
                                String imagePath) {
}
