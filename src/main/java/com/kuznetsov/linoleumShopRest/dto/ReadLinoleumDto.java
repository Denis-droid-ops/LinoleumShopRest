package com.kuznetsov.linoleumShopRest.dto;

import lombok.Value;

@Value
public class ReadLinoleumDto{
    Integer id;
    String lName;
    Float protect;
    Float thickness;
    Integer price;
    String imagePath;
}
