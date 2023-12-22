package com.kuznetsov.linoleumShopRest.mapper;


import com.kuznetsov.linoleumShopRest.dto.CreateEditLinoleumDto;
import com.kuznetsov.linoleumShopRest.dto.ReadLinoleumDto;
import com.kuznetsov.linoleumShopRest.entity.Linoleum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LinoleumMapper {
    Linoleum mapToLinoleum(CreateEditLinoleumDto createEditLinoleumDto);

    //Ссылаемся на LName, так как mapstruct ищет поля по геттерам, а Lombok в свою очередь
    // делает getLName вместо getlName(правильный по JAVA BEAN спецификации)
    @Mapping(source = "linoleum.LName",target = "lName")
    ReadLinoleumDto mapToReadLinoleumDto(Linoleum linoleum);

    @Mapping(source = "createEditLinoleumDto.lName",target = "LName")
    Linoleum updateLinoleumFromDto(CreateEditLinoleumDto createEditLinoleumDto,
                               @MappingTarget Linoleum linoleum);

    List<ReadLinoleumDto> entitiesToDtos(List<Linoleum> linoleums);
}
