package com.kuznetsov.linoleumShopRest.integration.service;

import com.kuznetsov.linoleumShopRest.annotation.IntegrationTest;
import com.kuznetsov.linoleumShopRest.dto.ReadLinoleumDto;
import com.kuznetsov.linoleumShopRest.service.LinoleumService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

import static com.kuznetsov.linoleumShopRest.testData.LinoleumTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@IntegrationTest
@Transactional
@Sql(value = {"classpath:sql/populateDB.sql"})
public class LinoleumCacheIT {

    private final LinoleumService linoleumService;
    private final CacheManager cacheManager;

    public LinoleumCacheIT(LinoleumService linoleumService, CacheManager cacheManager) {
        this.linoleumService = linoleumService;
        this.cacheManager = cacheManager;
    }

    @Test
    public void save() throws IOException {
        Optional<ReadLinoleumDto> readLinoleumDtoCache = Optional.ofNullable(cacheManager
                .getCache("readLinoleumDto")
                .get(NEWID,ReadLinoleumDto.class));

        assertFalse(readLinoleumDtoCache.isPresent());

        linoleumService.save(CREATE_EDIT_LINOLEUM_DTO);
        readLinoleumDtoCache = Optional.ofNullable(cacheManager
                .getCache("readLinoleumDto")
                .get(NEWID,ReadLinoleumDto.class));

        assertTrue(readLinoleumDtoCache.isPresent());
        assertThat(readLinoleumDtoCache.get()).usingRecursiveComparison().isEqualTo(READ_LINOLEUM_DTO);

        flushImage();
    }

    @Test
    public void update() throws IOException {
        //добавляем в пустой кэш значение
        cacheManager.getCache("readLinoleumDto")
                .put(DB_EXISTED_LINOLEUM.getId(),EXISTED_READ_LINOLEUM_DTO);

        //обновляем значение того же линолеума, что ранее добавили в кэш, ожидаем обновления кэша
        linoleumService.update(DB_EXISTED_LINOLEUM.getId(),CREATE_EDIT_LINOLEUM_DTO);
        ReadLinoleumDto readLinoleumDtoCache = cacheManager.getCache("readLinoleumDto")
                .get(DB_EXISTED_LINOLEUM.getId(),ReadLinoleumDto.class);

        assertThat(readLinoleumDtoCache.getId()).isEqualTo(DB_EXISTED_LINOLEUM.getId());
        assertThat(readLinoleumDtoCache)
                .usingRecursiveComparison()
                .ignoringFields("id")//игнорируем, так как актуальный id равен 1
                .isEqualTo(READ_LINOLEUM_DTO);

        flushImage();
    }

    @Test
    public void findById(){
        Optional<ReadLinoleumDto> readLinoleumDtoCache = Optional.ofNullable(cacheManager
                .getCache("readLinoleumDto")
                .get(DB_EXISTED_LINOLEUM.getId(),ReadLinoleumDto.class));

        assertFalse(readLinoleumDtoCache.isPresent());

        linoleumService.findById(DB_EXISTED_LINOLEUM.getId());
        readLinoleumDtoCache = Optional.ofNullable(cacheManager
                .getCache("readLinoleumDto")
                .get(DB_EXISTED_LINOLEUM.getId(),ReadLinoleumDto.class));

        assertTrue(readLinoleumDtoCache.isPresent());
        assertThat(readLinoleumDtoCache.get())
                .usingRecursiveComparison()
                .isEqualTo(EXISTED_READ_LINOLEUM_DTO);

    }

    @Test
    public void delete(){
        cacheManager.getCache("readLinoleumDto")
                .put(DB_EXISTED_LINOLEUM.getId(),EXISTED_READ_LINOLEUM_DTO);
        Optional<ReadLinoleumDto> readLinoleumDtoCache = Optional.ofNullable(cacheManager
                .getCache("readLinoleumDto")
                .get(DB_EXISTED_LINOLEUM.getId(),ReadLinoleumDto.class));

        assertTrue(readLinoleumDtoCache.isPresent());

        linoleumService.delete(DB_EXISTED_LINOLEUM.getId());
        readLinoleumDtoCache = Optional.ofNullable(cacheManager
                .getCache("readLinoleumDto")
                .get(DB_EXISTED_LINOLEUM.getId(),ReadLinoleumDto.class));

        assertFalse(readLinoleumDtoCache.isPresent());
    }

    @AfterEach
    public void evictAllCache(){
        cacheManager.getCache("readLinoleumDto").invalidate();
    }

}
