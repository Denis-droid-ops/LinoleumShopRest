package com.kuznetsov.linoleumShopRest.integration.service;

import com.kuznetsov.linoleumShopRest.annotation.IntegrationTest;
import com.kuznetsov.linoleumShopRest.dto.LinoleumFilter;
import com.kuznetsov.linoleumShopRest.dto.ReadLinoleumDto;
import com.kuznetsov.linoleumShopRest.dto.RevisionDto;
import com.kuznetsov.linoleumShopRest.entity.Linoleum;
import com.kuznetsov.linoleumShopRest.integration.IntegrationTestConfig;
import com.kuznetsov.linoleumShopRest.service.ImageService;
import com.kuznetsov.linoleumShopRest.service.LinoleumService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.history.Revision;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;


import static com.kuznetsov.linoleumShopRest.testData.LinoleumTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@Transactional
@ActiveProfiles(value = {"test","noCache"})
class LinoleumServiceIT extends IntegrationTestConfig {
/*
    private final LinoleumService linoleumService;
    
    private final ImageService imageService;

    public LinoleumServiceIT(LinoleumService linoleumService, ImageService imageService) {
        this.linoleumService = linoleumService;
        this.imageService = imageService;
    }

    @Test
    void saveAndImageUpload() throws IOException {
        ReadLinoleumDto actual = linoleumService.save(CREATE_EDIT_LINOLEUM_DTO);
        assertThat(actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(READ_LINOLEUM_DTO);
        assertThat(actual.getId()).isEqualTo(NEWID);
        Optional<byte[]> actualImage = imageService.get(actual.getImagePath());
        assertTrue(actualImage.isPresent());
        assertArrayEquals(actualImage.get(), Files.readAllBytes(IMAGE_FULL_PATH));

        flushImage();
    }

    @Test
    void getImage() throws IOException {
        imageService.upload(getMockImage().getOriginalFilename(),
               getMockImage().getInputStream());
        //DBEXISTEDLINOLEUM содержит путь к MockImage
        Optional<byte[]> actual = linoleumService.getImage(DB_EXISTED_LINOLEUM.getId());
        assertTrue(actual.isPresent());
        assertArrayEquals(actual.get(), Files.readAllBytes(IMAGE_FULL_PATH));

        flushImage();
    }

    @Test
    @Transactional
    void update() throws IOException {
        //меняем изображение на updated.png, так как test.png уже есть в БД
        CREATE_EDIT_LINOLEUM_DTO.setImage(getMockImageForUpdate());
        //меняем существующую запись с id 1, данными из ДТО
        Optional<ReadLinoleumDto> actual =
                linoleumService.update(DB_EXISTED_LINOLEUM.getId(),CREATE_EDIT_LINOLEUM_DTO);
        assertTrue(actual.isPresent());
        assertThat(actual.get().getId()).isEqualTo(DB_EXISTED_LINOLEUM.getId());
        assertThat(actual.get())
                .usingRecursiveComparison()
                .isEqualTo(new ReadLinoleumDto(DB_EXISTED_LINOLEUM.getId(),
                        CREATE_EDIT_LINOLEUM_DTO.getLName(),
                        CREATE_EDIT_LINOLEUM_DTO.getProtect(),
                        CREATE_EDIT_LINOLEUM_DTO.getThickness(),
                        CREATE_EDIT_LINOLEUM_DTO.getPrice(),
                        CREATE_EDIT_LINOLEUM_DTO.getImage().getOriginalFilename()));
        //проверяем, загрузилась ли картинка
        assertArrayEquals(imageService.get(actual.get().getImagePath()).get(),
                Files.readAllBytes(UPDATED_IMAGE_FULL_PATH));

        flushImage();
        flushCreateEditLinoleumDto(CREATE_EDIT_LINOLEUM_DTO);
    }

    @Test
    void findById() {
        Optional<ReadLinoleumDto> actual =
                linoleumService.findById(DB_EXISTED_LINOLEUM.getId());
        assertTrue(actual.isPresent());
        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(EXISTED_READ_LINOLEUM_DTO);
    }

    @Test
    void findByLName() {
        Optional<ReadLinoleumDto> actual =
                linoleumService.findByLName(DB_EXISTED_LINOLEUM.getLName());
        assertTrue(actual.isPresent());
        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(EXISTED_READ_LINOLEUM_DTO);
    }

    @Test
    void findByImageName() {
        Optional<ReadLinoleumDto> actual =
                linoleumService.findByImageName(DB_EXISTED_LINOLEUM.getImagePath());
        assertTrue(actual.isPresent());
        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(EXISTED_READ_LINOLEUM_DTO);
    }

    @Test
    void findAll() {
        Page<ReadLinoleumDto> actual =
                linoleumService.findAll(null, Pageable.unpaged());
        assertThat(actual.toList()).isEqualTo(ALL_LINOLEUM_READ_DTO);
    }

    @Test
    void findAllFiltered() {
        Page<ReadLinoleumDto> actual =
                linoleumService.findAll(
                        new LinoleumFilter(null,null,null,400,711),
                        PageRequest.of(0,2, Sort.by("lName")));
        assertThat(actual.toList()).isEqualTo(FILTERED_LINOLEUM_READ_DTO);
    }

    @Test
    void delete() throws IOException {
        imageService.upload(getMockImage().getOriginalFilename(),
                getMockImage().getInputStream());
       assertTrue(linoleumService
               .findById(EXISTED_READ_LINOLEUM_DTO.getId())
               .isPresent());
       assertTrue(linoleumService.delete(EXISTED_READ_LINOLEUM_DTO.getId()));
       assertFalse(linoleumService
               .findById(EXISTED_READ_LINOLEUM_DTO.getId())
               .isPresent());
       assertFalse(imageService.get(getMockImage().getOriginalFilename()).isPresent());

       flushImage();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void findRevisionByLinoleumIdAndRevNum() throws IOException {
        ReadLinoleumDto readLinoleumDto = linoleumService.save(CREATE_EDIT_LINOLEUM_DTO);
        Optional<Revision<Long, Linoleum>> actual =
                linoleumService.findRevisionByLinoleumIdAndRevNum(readLinoleumDto.getId(), 1L);

        LINOLEUM.setId(readLinoleumDto.getId());
        LINOLEUM.setLName(readLinoleumDto.getLName());
        LINOLEUM.setProtect(readLinoleumDto.getProtect());
        LINOLEUM.setThickness(readLinoleumDto.getThickness());
        LINOLEUM.setPrice(readLinoleumDto.getPrice());
        LINOLEUM.setImagePath(readLinoleumDto.getImagePath());

        assertTrue(actual.isPresent());
        assertTrue(actual.get().getMetadata().getRevisionType().ordinal()==1);
        assertThat(actual.get().getEntity().getId()).isEqualTo(LINOLEUM.getId());
        assertThat(actual.get().getEntity()).usingRecursiveComparison()
                .ignoringFields("createdAt",
                        "createdBy",
                        "modifiedBy",
                        "modifiedAt")
                .isEqualTo(LINOLEUM);

        flushImage();
        flushLinoleum(LINOLEUM);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void findAllRevisionsByLinoleumId() throws IOException {
        ReadLinoleumDto readLinoleumDto = linoleumService.save(CREATE_EDIT_LINOLEUM_DTO);
        CREATE_EDIT_LINOLEUM_DTO.setPrice(730);
        linoleumService.update(readLinoleumDto.getId(),CREATE_EDIT_LINOLEUM_DTO);

        LINOLEUM.setId(readLinoleumDto.getId());
        LINOLEUM.setLName(readLinoleumDto.getLName());
        LINOLEUM.setProtect(readLinoleumDto.getProtect());
        LINOLEUM.setThickness(readLinoleumDto.getThickness());
        LINOLEUM.setPrice(readLinoleumDto.getPrice());
        LINOLEUM.setImagePath(readLinoleumDto.getImagePath());

        Page<Revision<Long, Linoleum>> actual =
                linoleumService.findAllRevisionsByLinoleumId(readLinoleumDto.getId(),PageRequest.of(0,2));
        assertFalse(actual.toList().isEmpty());
        assertThat(actual.toList().get(0).getEntity()).isEqualTo(LINOLEUM);
        LINOLEUM.setPrice(730);
        assertThat(actual.toList().get(1).getEntity()).isEqualTo(LINOLEUM);
        assertTrue(actual.toList().get(0).getRevisionNumber().get()==1L);
        assertTrue(actual.toList().get(1).getRevisionNumber().get()==2L);
        assertThat(actual.toList().get(0).getMetadata().getRevisionType().ordinal())
                .isEqualTo(1);
        assertThat(actual.toList().get(1).getMetadata().getRevisionType().ordinal())
                .isEqualTo(2);

        flushLinoleum(LINOLEUM);
        flushCreateEditLinoleumDto(CREATE_EDIT_LINOLEUM_DTO);
        flushImage();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void findAllRevisions() throws IOException {
        List<RevisionDto> actual = linoleumService.findAllRevisions();
        assertTrue(actual.isEmpty());
        //1st revision
        ReadLinoleumDto readLinoleumDto = linoleumService.save(CREATE_EDIT_LINOLEUM_DTO);
        CREATE_EDIT_LINOLEUM_DTO.setPrice(730);
        //2nd revision
        linoleumService.update(readLinoleumDto.getId(),CREATE_EDIT_LINOLEUM_DTO);
        //3rd revision
        linoleumService.delete(readLinoleumDto.getId());
        actual = linoleumService.findAllRevisions();
        assertThat(actual).usingRecursiveComparison().ignoringFields("linoleum.createdAt",
                        "linoleum.createdBy",
                        "linoleum.modifiedBy",
                        "linoleum.modifiedAt",
                        "linoleum.revision",
                        "revision.timestamp")
                .isEqualTo(ALL_REVISIONS);
        List<Long> actualRevIds = actual.stream().map(rev -> rev.getRevision().getId()).toList();
        List<Long> expectedRevIds = ALL_REVISIONS.stream().map(rev -> rev.getRevision().getId()).toList();
        assertThat(actualRevIds).usingRecursiveComparison().isEqualTo(expectedRevIds);

        flushCreateEditLinoleumDto(CREATE_EDIT_LINOLEUM_DTO);
        flushImage();

    }

 */
}