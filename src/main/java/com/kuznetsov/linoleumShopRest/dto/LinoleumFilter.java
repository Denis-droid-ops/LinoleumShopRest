package com.kuznetsov.linoleumShopRest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class LinoleumFilter {
    @JsonProperty("name")
    String name;
    @JsonProperty("protect")
    Float protect;
    @JsonProperty("thickness")
    Float thickness;
    @JsonProperty("minPrice")
    Integer minPrice;
    @JsonProperty("maxPrice")
    Integer maxPrice;
}
