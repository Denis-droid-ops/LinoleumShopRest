package com.kuznetsov.linoleumShopRest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
//обьекты класса изменяемы, поскольку логика в контроллере требует сеттинг полей уже после создания обьекта
public class CreateEditLinoleumDto {
    private String lName;
    private Float protect;
    private Float thickness;
    private Integer price;
    private MultipartFile image;
}
