package com.kuznetsov.linoleumShopRest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@Schema(description = "Linoleum data for filtering")
public class LinoleumFilter {

    @JsonProperty("name")
    @Schema(description = "Linoleum name")
    String name;

    @JsonProperty("protect")
    @Schema(description = "Linoleum protective layer's thickness")
    Float protect;

    @JsonProperty("thickness")
    @Schema(description = "Linoleum thickness")
    Float thickness;

    @JsonProperty("minPrice")
    @Schema(description = "Linoleum minimum price")
    Integer minPrice;

    @JsonProperty("maxPrice")
    @Schema(description = "Linoleum maximum price")
    Integer maxPrice;
}
