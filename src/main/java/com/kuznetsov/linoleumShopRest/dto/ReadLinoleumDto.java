package com.kuznetsov.linoleumShopRest.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@Schema(description = "Information about linoleum")
public class ReadLinoleumDto{

    @Schema(description = "Linoleum identifier")
    Integer id;

    @Schema(description = "Linoleum name")
    String lName;

    @Schema(description = "Linoleum protective layer's thickness")
    Float protect;

    @Schema(description = "Linoleum total thickness")
    Float thickness;

    @Schema(description = "Linoleum price")
    Integer price;

    @Schema(description = "Linoleum image path")
    String imagePath;


}
