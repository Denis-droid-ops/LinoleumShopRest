package com.kuznetsov.linoleumShopRest.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@Value
@Schema(description = "Custom response with content and page metadata")
public class PageResponse<T> {

    @Schema(description = "Response main content")
    List<T> content;

    @Schema(description = "Information about page, size, total elements")
    Metadata metadata;

    public static <T> PageResponse<T> of(Page<T> page){
        Metadata metadata = new Metadata(page.getNumber(),page.getSize(),page.getTotalElements());
        return new PageResponse<>(page.getContent(),metadata);
    }

    @Value
    public static class Metadata{
       int page;
       int size;
       long totalElements;
    }
}
