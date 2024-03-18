package com.kuznetsov.linoleumShopRest.integration.controller;

import com.kuznetsov.linoleumShopRest.annotation.IntegrationTest;
import com.kuznetsov.linoleumShopRest.dto.ReadLinoleumDto;
import com.kuznetsov.linoleumShopRest.service.LinoleumService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



import static com.kuznetsov.linoleumShopRest.testData.LinoleumTestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@AutoConfigureMockMvc
@Transactional
@Sql(value = {"classpath:sql/populateDB.sql"})
@ActiveProfiles(value = {"test","noCache"})
public class LinoleumControllerRevIT {

    private final MockMvc mockMvc;

    private final LinoleumService linoleumService;

    public LinoleumControllerRevIT(MockMvc mockMvc, LinoleumService linoleumService) {
        this.mockMvc = mockMvc;
        this.linoleumService = linoleumService;
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void findRevisionByLinoleumIdAndRevNum() throws Exception {
        ReadLinoleumDto readLinoleumDto = linoleumService.save(CREATE_EDIT_LINOLEUM_DTO);
        mockMvc.perform(get(BASE_URI+"/{id}/revision",readLinoleumDto.getId())
                        .param("revNum","1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$.revisionNumber").value(1),
                        jsonPath("$.metadata.revisionType").value("INSERT"),
                        jsonPath("$.entity.id").value(NEWID),
                        jsonPath("$.entity.protect").value(0.4f),
                        jsonPath("$.entity.thickness").value(2f),
                        jsonPath("$.entity.price").value(940),
                        jsonPath("$.entity.lName").value("saratoga 4"),
                        jsonPath("$.entity.imagePath").value("test.png"));

    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void findAllRevisionsByLinoleumId() throws Exception {
        ReadLinoleumDto readLinoleumDto = linoleumService.save(CREATE_EDIT_LINOLEUM_DTO);
        mockMvc.perform(get(BASE_URI+"/{id}/revisions",readLinoleumDto.getId())
                        .param("page","0")
                        .param("size","1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$.content[0].metadata.revisionNumber").value(1),
                        jsonPath("$.content[0].metadata.revisionType").value("INSERT"),
                        jsonPath("$.content[0].entity.protect").value(0.4f),
                        jsonPath("$.content[0].entity.thickness").value(2f),
                        jsonPath("$.content[0].entity.price").value(940),
                        jsonPath("$.content[0].entity.lName").value("saratoga 4"),
                        jsonPath("$.content[0].entity.imagePath").value("test.png"));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void findAllRevisions() throws Exception {
        //1st revision
        ReadLinoleumDto readLinoleumDto = linoleumService.save(CREATE_EDIT_LINOLEUM_DTO);
        CREATE_EDIT_LINOLEUM_DTO.setPrice(730);
        //2nd revision
        linoleumService.update(readLinoleumDto.getId(),CREATE_EDIT_LINOLEUM_DTO);
        //3rd revision
        linoleumService.delete(readLinoleumDto.getId());
        mockMvc.perform(get(BASE_URI+"/revisions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$[0].linoleum.id").value(readLinoleumDto.getId()),
                        jsonPath("$[0].linoleum.protect").value(0.4f),
                        jsonPath("$[0].linoleum.thickness").value(2f),
                        jsonPath("$[0].linoleum.price").value(940),
                        jsonPath("$[0].linoleum.lName").value("saratoga 4"),
                        jsonPath("$[0].linoleum.imagePath").value("test.png"),
                        jsonPath("$[0].revisionType").value("ADD"),
                        jsonPath("$[0].revision.id").value(1),
                        jsonPath("$[1].revisionType").value("MOD"),
                        jsonPath("$[1].revision.id").value(2),
                        jsonPath("$[1].linoleum.price").value(730),
                        jsonPath("$[2].revisionType").value("DEL"),
                        jsonPath("$[2].revision.id").value(3));
        flushCreateEditLinoleumDto(CREATE_EDIT_LINOLEUM_DTO);
    }
}
