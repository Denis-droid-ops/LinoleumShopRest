package com.kuznetsov.linoleumShopRest.controller;

import com.kuznetsov.linoleumShopRest.dto.CreateEditLinoleumDto;
import com.kuznetsov.linoleumShopRest.dto.LinoleumFilter;
import com.kuznetsov.linoleumShopRest.dto.PageResponse;
import com.kuznetsov.linoleumShopRest.dto.ReadLinoleumDto;
import com.kuznetsov.linoleumShopRest.exception.ImageNotFoundException;
import com.kuznetsov.linoleumShopRest.exception.LinoleumNotFoundException;
import com.kuznetsov.linoleumShopRest.service.LinoleumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;



@RestController
@RequestMapping("/api/v1/linoleums")
public class LinoleumController {

    private final LinoleumService linoleumService;

    @Autowired
    public LinoleumController(LinoleumService linoleumService) {
        this.linoleumService = linoleumService;
    }

    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReadLinoleumDto> create(@RequestPart CreateEditLinoleumDto createEditLinoleumDto, @RequestPart MultipartFile image){
        createEditLinoleumDto.setImage(image);
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .build()
                .toUri())
                .body(linoleumService.save(createEditLinoleumDto));
    }

    @PutMapping(value = "/{id}",consumes = "multipart/form-data")
    public ResponseEntity<ReadLinoleumDto> update(@PathVariable("id") Integer id,
                                             @RequestPart CreateEditLinoleumDto createEditLinoleumDto,
                                             @RequestPart MultipartFile image){
        createEditLinoleumDto.setImage(image);
        return linoleumService.update(id,createEditLinoleumDto)
                .map(ResponseEntity::ok)
                .orElseThrow(LinoleumNotFoundException::new);
    }

    @GetMapping()
    public PageResponse<ReadLinoleumDto> findAll(@RequestBody(required = false) LinoleumFilter filter,Pageable pageable){
        Page<ReadLinoleumDto> page = linoleumService.findAll(filter,pageable);
        return PageResponse.of(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadLinoleumDto> findById(@PathVariable("id") Integer id){
        return linoleumService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(LinoleumNotFoundException::new);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") Integer id) {
        return linoleumService.getImage(id)
                .map(img->ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .contentLength(img.length)
                        .body(img))
                .orElseThrow(ImageNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        if(!linoleumService.delete(id)){
            throw new LinoleumNotFoundException();
        }
        return ResponseEntity.noContent().build();
    }



}
