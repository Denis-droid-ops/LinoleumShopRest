package com.kuznetsov.linoleumShopRest.mapper;


import com.kuznetsov.linoleumShopRest.dto.ReadLinoleumDto;
import com.kuznetsov.linoleumShopRest.entity.Linoleum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.util.List;


import static com.kuznetsov.linoleumShopRest.testData.LinoleumTestData.*;
import static org.assertj.core.api.Assertions.assertThat;

class LinoleumMapperTest {
/*
    private static LinoleumMapper linoleumMapper;

    @BeforeAll
    static void init() {
        linoleumMapper = Mappers.getMapper(LinoleumMapper.class);
    }

    @Test
    void mapToLinoleum() throws IOException {
        Linoleum actual = linoleumMapper.mapToLinoleum(CREATE_EDIT_LINOLEUM_DTO);
        assertThat(actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(LINOLEUM);
    }

    @Test
    void mapToReadLinoleumDto() {
        LINOLEUM.setId(READ_LINOLEUM_DTO.getId()); //Линолеум из тестовых данных изначально без id
        ReadLinoleumDto actual = linoleumMapper.mapToReadLinoleumDto(LINOLEUM);
        assertThat(actual).usingRecursiveComparison().isEqualTo(READ_LINOLEUM_DTO);
        flushLinoleum(LINOLEUM);
    }

    @Test
    void updateLinoleumFromDto() throws IOException {
        Linoleum actual = linoleumMapper.updateLinoleumFromDto(CREATE_EDIT_LINOLEUM_DTO,LINOLEUM);
        assertThat(actual).usingRecursiveComparison().isEqualTo(LINOLEUM);
    }

    @Test
    void entitiesToDtos() {
        List<ReadLinoleumDto> actual = linoleumMapper.entitiesToDtos(List.of(LINOLEUM));
        assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(READ_LINOLEUM_DTO));
        flushLinoleum(LINOLEUM);
    }

 */

}