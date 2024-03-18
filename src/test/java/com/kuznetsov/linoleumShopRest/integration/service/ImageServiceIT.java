package com.kuznetsov.linoleumShopRest.integration.service;

import com.kuznetsov.linoleumShopRest.annotation.IntegrationTest;
import com.kuznetsov.linoleumShopRest.service.ImageService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import static com.kuznetsov.linoleumShopRest.testData.LinoleumTestData.*;
import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@Transactional
class ImageServiceIT {

    @Autowired
    private ImageService imageService;

    @Test
    void upload() throws IOException {
        imageService.upload(CREATE_EDIT_LINOLEUM_DTO.getImage().getOriginalFilename(),
                CREATE_EDIT_LINOLEUM_DTO.getImage().getInputStream());
        assertTrue(Files.exists(IMAGE_FULL_PATH));
        assertArrayEquals(Files.readAllBytes(IMAGE_FULL_PATH), CREATE_EDIT_LINOLEUM_DTO
                .getImage()
                .getBytes());
    }

    @Test
    void get() throws IOException {
        Files.write(IMAGE_FULL_PATH,CREATE_EDIT_LINOLEUM_DTO.getImage().getBytes());
        Optional<byte[]> actual = imageService.get(CREATE_EDIT_LINOLEUM_DTO.getImage().getOriginalFilename());
        assertTrue(actual.isPresent());
        assertArrayEquals(actual.get(),Files.readAllBytes(IMAGE_FULL_PATH));
    }

    @Test
    void delete() throws IOException {
        Files.write(IMAGE_FULL_PATH,CREATE_EDIT_LINOLEUM_DTO.getImage().getBytes());
        imageService.delete(CREATE_EDIT_LINOLEUM_DTO.getImage().getOriginalFilename());
        assertFalse(Files.exists(IMAGE_FULL_PATH));
    }

    @AfterAll
    static void after() throws IOException {
        flushImage();
    }
}