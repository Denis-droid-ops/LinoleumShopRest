package com.kuznetsov.linoleumShopRest.integration.controller;

import com.kuznetsov.linoleumShopRest.annotation.IntegrationTest;
import com.kuznetsov.linoleumShopRest.dto.LinoleumFilter;
import com.kuznetsov.linoleumShopRest.exception.LinoleumNotFoundException;
import com.kuznetsov.linoleumShopRest.exception.LinoleumValidationException;
import com.kuznetsov.linoleumShopRest.service.LinoleumService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.Optional;

import static com.kuznetsov.linoleumShopRest.testData.LinoleumTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:spring.properties",
        properties = "spring.jpa.properties.hibernate.hbm2ddl.auto=none")
@ActiveProfiles(value = {"test","noCache"})
class LinoleumControllerTestIT {

    private final MockMvc mockMvc;

    @MockBean
    private LinoleumService linoleumService;

    LinoleumControllerTestIT(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }


    @Test
    void create() throws Exception {
        Mockito.when(linoleumService.save(CREATE_EDIT_LINOLEUM_DTO)).thenReturn(READ_LINOLEUM_DTO);

        //Моки для валидации
        Mockito.when(linoleumService.findByLName(CREATE_EDIT_LINOLEUM_DTO.getLName()))
                .thenReturn(Optional.empty());
        Mockito.when(linoleumService.findByImageName(
                CREATE_EDIT_LINOLEUM_DTO.getImage().getOriginalFilename()))
                .thenReturn(Optional.empty());

        mockMvc.perform(multipart(BASE_URI)
                                .file(new MockMultipartFile("createEditLinoleumDto",
                                        "",
                                        MediaType.APPLICATION_JSON_VALUE,
                                        JSON.toString().getBytes()))
                                .file((MockMultipartFile) CREATE_EDIT_LINOLEUM_DTO.getImage())
                        )
                .andExpect(status().isCreated())
                        .andExpectAll(jsonPath("$.id").value(6),
                                jsonPath("$.lName").value("saratoga 4"),
                                jsonPath("$.protect").value(0.4f),
                                jsonPath("$.thickness").value(2f),
                                jsonPath("$.price").value(940),
                                jsonPath("$.imagePath").value("test.png"));
        Mockito.verify(linoleumService).save(CREATE_EDIT_LINOLEUM_DTO);
    }

    @Test
    void createWithInvalidData() throws Exception {
        //Моки для валидации
        Mockito.when(linoleumService.findByLName(CREATE_EDIT_LINOLEUM_DTO.getLName()))
                .thenReturn(Optional.of(READ_LINOLEUM_DTO));
        Mockito.when(linoleumService.findByImageName(
                        CREATE_EDIT_LINOLEUM_DTO.getImage().getOriginalFilename()))
                .thenReturn(Optional.of(READ_LINOLEUM_DTO));

        mockMvc.perform(multipart(BASE_URI)
                        .file(new MockMultipartFile("createEditLinoleumDto",
                                "",
                                MediaType.APPLICATION_JSON_VALUE,
                                JSON.toString().getBytes()))
                        .file((MockMultipartFile) CREATE_EDIT_LINOLEUM_DTO.getImage())
                )
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof LinoleumValidationException))
                .andExpect(jsonPath("$.message")
                        .value("Linoleum name is already taken! Must be unique!   " +
                                "Image path is already use! Must be unique!   "));
    }

    @Test
    void update() throws Exception {
        Mockito.when(linoleumService.update(READ_LINOLEUM_DTO.getId(),
                CREATE_EDIT_LINOLEUM_DTO)).thenReturn(Optional.of(READ_LINOLEUM_DTO));

        mockMvc.perform(multipart(BASE_URI+"/{id}",
                        READ_LINOLEUM_DTO.getId())
                        .file(new MockMultipartFile("createEditLinoleumDto",
                                "",
                                MediaType.APPLICATION_JSON_VALUE,
                                JSON.toString().getBytes()))
                        .file((MockMultipartFile) CREATE_EDIT_LINOLEUM_DTO.getImage())
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                )
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$.id").value(6),
                        jsonPath("$.lName").value("saratoga 4"),
                        jsonPath("$.protect").value(0.4f),
                        jsonPath("$.thickness").value(2f),
                        jsonPath("$.price").value(940),
                        jsonPath("$.imagePath").value("test.png"));
        Mockito.verify(linoleumService).update(READ_LINOLEUM_DTO.getId(),CREATE_EDIT_LINOLEUM_DTO);
    }

    @Test
    void findAll() throws Exception {
        Mockito.when(linoleumService.findAll(
                new LinoleumFilter("",null,null,599,711),
                PageRequest.of(0,2, Sort.by("lName"))))
                .thenReturn(new PageImpl<>(FILTERED_LINOLEUM_READ_DTO));
        mockMvc.perform(get(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(LINOLEUM_FILTER_JSON.toString())
                .param("page","0")
                .param("size","2")
                .param("sort","lName"))
                .andExpect(status().isOk())
                .andExpect(content().json(FILTERED_LINOLEUM_CONTENT_JSON.toString()));
    }

    @Test
    void findById() throws Exception {
        Mockito.when(linoleumService.findById(DB_EXISTED_LINOLEUM.getId()))
                .thenReturn(Optional.of(EXISTED_READ_LINOLEUM_DTO));
        mockMvc.perform(get(BASE_URI+"/{id}",
                DB_EXISTED_LINOLEUM.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(content().json(EXISTED_JSON.toString()));
    }

    @Test
    void isNotFindedById() throws Exception {
        Mockito.when(linoleumService.findById(NEWID))
                .thenReturn(Optional.empty());
        mockMvc.perform(get(BASE_URI+"/{id}",
                        NEWID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof LinoleumNotFoundException))
                .andExpect(result -> assertEquals("Linoleum is null, not found",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void getImage() throws Exception {
        Mockito.when(linoleumService.getImage(EXISTED_READ_LINOLEUM_DTO.getId()))
                .thenReturn(Optional.of(getMockImage().getBytes()));
        mockMvc.perform(get(BASE_URI+"/{id}/image",
                EXISTED_READ_LINOLEUM_DTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(getMockImage().getBytes()));
        Mockito.verify(linoleumService).getImage(EXISTED_READ_LINOLEUM_DTO.getId());
    }

    @Test
    void isNotDeleted() throws Exception {
        Mockito.when(linoleumService.delete(NEWID))
                .thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders
                .delete(BASE_URI+"/{id}",
                        NEWID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof LinoleumNotFoundException))
                .andExpect(result -> assertEquals("Linoleum is null, not found",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void delete() throws Exception {
        Mockito.when(linoleumService.delete(DB_EXISTED_LINOLEUM.getId()))
                .thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(BASE_URI+"/{id}",
                                DB_EXISTED_LINOLEUM.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


}