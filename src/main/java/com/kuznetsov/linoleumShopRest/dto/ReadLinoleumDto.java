package com.kuznetsov.linoleumShopRest.dto;

public record ReadLinoleumDto(Integer id,
                              String lName,
                              Float protect,
                              Float thickness,
                              Integer price,
                              String imagePath) {
}
