package com.kuznetsov.linoleumShopRest.mapper;


import com.kuznetsov.linoleumShopRest.dto.CreateEditLinoleumDto;
import com.kuznetsov.linoleumShopRest.dto.ReadLinoleumDto;
import com.kuznetsov.linoleumShopRest.entity.Linoleum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface LinoleumMapper {


    @Mapping(source = "createEditLinoleumDto.image", target = "imagePath",qualifiedByName = "castToImagePath")
    @Mapping(source = "createEditLinoleumDto.LName", target = "lName")
    Linoleum mapToLinoleum(CreateEditLinoleumDto createEditLinoleumDto);

    //Ссылаемся на LName, так как mapstruct ищет поля по геттерам, а Lombok в свою очередь
    // делает getLName вместо getlName(правильный по JAVA BEAN спецификации)
    @Mapping(source = "linoleum.LName",target = "lName")
    ReadLinoleumDto mapToReadLinoleumDto(Linoleum linoleum);

    @Mapping(source = "createEditLinoleumDto.image", target = "imagePath",qualifiedByName = "castToImagePath")
    @Mapping(source = "createEditLinoleumDto.LName",target = "LName")
    Linoleum updateLinoleumFromDto(CreateEditLinoleumDto createEditLinoleumDto,
                               @MappingTarget Linoleum linoleum);

    List<ReadLinoleumDto> entitiesToDtos(List<Linoleum> linoleums);

    @Named("castToImagePath")
    static String imageToImagePath(MultipartFile image){
        return image.getOriginalFilename();
    }
}
