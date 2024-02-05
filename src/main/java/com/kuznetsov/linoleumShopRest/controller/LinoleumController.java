package com.kuznetsov.linoleumShopRest.controller;

import com.kuznetsov.linoleumShopRest.dto.*;
import com.kuznetsov.linoleumShopRest.entity.Linoleum;
import com.kuznetsov.linoleumShopRest.exception.ImageNotFoundException;
import com.kuznetsov.linoleumShopRest.exception.LinoleumNotFoundException;
import com.kuznetsov.linoleumShopRest.exception.LinoleumValidationException;
import com.kuznetsov.linoleumShopRest.exception.RevisionNotFoundException;
import com.kuznetsov.linoleumShopRest.service.LinoleumService;
import com.kuznetsov.linoleumShopRest.validator.LinoleumValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/v1/linoleums")
public class LinoleumController {

    private final LinoleumService linoleumService;
    private final LinoleumValidator linoleumValidator;

    @Autowired
    public LinoleumController(LinoleumService linoleumService, LinoleumValidator linoleumValidator) {
        this.linoleumService = linoleumService;
        this.linoleumValidator = linoleumValidator;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ReadLinoleumDto> create(@RequestPart @Valid CreateEditLinoleumDto createEditLinoleumDto,
                                                  BindingResult bindingResult,
                                                  @RequestPart MultipartFile image){
        createEditLinoleumDto.setImage(image);
        linoleumValidator.validate(createEditLinoleumDto,bindingResult);
        if(bindingResult.hasErrors()){
            throw new LinoleumValidationException(bindingResult);
        }
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .build()
                .toUri())
                .body(linoleumService.save(createEditLinoleumDto));
    }

    @PutMapping(value = "/{id}",consumes = "multipart/form-data")
    public ResponseEntity<ReadLinoleumDto> update(@PathVariable("id") Integer id,
                                             @RequestPart @Valid CreateEditLinoleumDto createEditLinoleumDto,
                                             BindingResult bindingResult,
                                             @RequestPart MultipartFile image){
        createEditLinoleumDto.setImage(image);
        return linoleumService.update(id,createEditLinoleumDto)
                .map(ResponseEntity::ok)
                .orElseThrow(LinoleumNotFoundException::new);
    }

    @GetMapping()
    public PageResponse<ReadLinoleumDto> findAll(@RequestBody(required = false) LinoleumFilter filter,
                                                 Pageable pageable){
        return PageResponse.of(linoleumService.findAll(filter,pageable));
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

    @GetMapping("/{id}/revision")
    public ResponseEntity<Revision<Long, Linoleum>> findRevisionByLinoleumIdAndRevNum(@PathVariable("id") Integer linoleumId,
                                                                     @RequestParam("revNum") Long revisionNumber){
        return linoleumService.findRevisionByLinoleumIdAndRevNum(linoleumId,revisionNumber)
                .map(ResponseEntity::ok)
                .orElseThrow(RevisionNotFoundException::new);
    }

    @GetMapping("/{id}/revisions")
    public PageResponse<Revision<Long, Linoleum>> findAllRevisionsByLinoleumId(@PathVariable("id") Integer linoleumId,
                                                                  Pageable pageable){
        return PageResponse.of(linoleumService.findAllRevisionsByLinoleumId(linoleumId,pageable));
    }

    @GetMapping("/revisions")
    public List<RevisionDto> findAllRevisions(){
        return linoleumService.findAllRevisions();
    }


}
