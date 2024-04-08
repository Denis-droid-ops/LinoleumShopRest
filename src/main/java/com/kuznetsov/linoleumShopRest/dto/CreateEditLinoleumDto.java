package com.kuznetsov.linoleumShopRest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.constraints.*;

@Data
@AllArgsConstructor
//обьекты класса изменяемы, поскольку логика в контроллере требует сеттинг полей уже после создания обьекта
@Schema(description = "Linoleum data for creating or updating")
public class CreateEditLinoleumDto {

    @NotBlank(message = "Linoleum name is empty!")
    @Size(min = 2,max = 50,message = "Numbers of chars must be in range from 2 to 50!")
    @Schema(description = "Linoleum name")
    private String lName;

    @NotNull(message = "Protect is empty!")
    @Max(value = 1,message = "Maximum protect is 1!")
    @Schema(description = "Linoleum protective layer's thickness")
    private Float protect;

    @NotNull(message = "Thickness is empty!")
    @Min(value = 1,message = "Minimum thickness is 1!")
    @Max(value = 5,message = "Maximum thickness is 5!")
    @Schema(description = "Linoleum total thickness")
    private Float thickness;

    @NotNull(message = "Price is empty!")
    @Min(value = 400,message = "Minimum price is 400!")
    @Max(value = 3000,message = "Maximum price is 3000!")
    @Schema(description = "Linoleum price")
    private Integer price;

    @Schema(description = "Linoleum image")
    private MultipartFile image;
}
