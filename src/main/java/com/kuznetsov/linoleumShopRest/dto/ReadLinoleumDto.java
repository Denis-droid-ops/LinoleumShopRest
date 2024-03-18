package com.kuznetsov.linoleumShopRest.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Value;

@Value
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class ReadLinoleumDto{
    Integer id;
    String lName;
    Float protect;
    Float thickness;
    Integer price;
    String imagePath;


}
