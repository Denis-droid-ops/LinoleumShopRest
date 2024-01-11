package com.kuznetsov.linoleumShopRest.dto;

import lombok.Value;

@Value
public class LinoleumFilter {
    String name;
    Float protect;
    Float thickness;
    Integer minPrice;
    Integer maxPrice;
}
