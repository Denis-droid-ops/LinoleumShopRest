package com.kuznetsov.linoleumShopRest.controller;

import com.kuznetsov.linoleumShopRest.dto.*;
import com.kuznetsov.linoleumShopRest.entity.Linoleum;
import com.kuznetsov.linoleumShopRest.exception.ImageNotFoundException;
import com.kuznetsov.linoleumShopRest.exception.LinoleumNotFoundException;
import com.kuznetsov.linoleumShopRest.exception.LinoleumValidationException;
import com.kuznetsov.linoleumShopRest.exception.RevisionNotFoundException;
import com.kuznetsov.linoleumShopRest.service.LinoleumService;
import com.kuznetsov.linoleumShopRest.validator.LinoleumValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
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
@Tag(name = "Linoleum controller",description = "Methods for work with linoleum")
public class LinoleumController {

    private final LinoleumService linoleumService;
    private final LinoleumValidator linoleumValidator;

    @Autowired
    public LinoleumController(LinoleumService linoleumService, LinoleumValidator linoleumValidator) {
        this.linoleumService = linoleumService;
        this.linoleumValidator = linoleumValidator;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create new linoleum")
    public ResponseEntity<ReadLinoleumDto> create(@RequestPart
                                                  @Parameter(description = "Linoleum data except image in json file format",
                                                          schema = @Schema(type = "string", format = "binary"))
                                                  @Valid CreateEditLinoleumDto createEditLinoleumDto,
                                                  BindingResult bindingResult,
                                                  @Parameter(description = "Linoleum image file")
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
    @Operation(summary = "Update linoleum by its identifier")
    public ResponseEntity<ReadLinoleumDto> update(@PathVariable("id") Integer id,
                                             @RequestPart
                                             @Parameter(description = "Linoleum data except image in json file format",
                                                     schema = @Schema(type = "string", format = "binary"))
                                             @Valid CreateEditLinoleumDto createEditLinoleumDto,
                                             BindingResult bindingResult,
                                             @Parameter(description = "Linoleum image file")
                                             @RequestPart MultipartFile image){
        createEditLinoleumDto.setImage(image);
        if(bindingResult.hasErrors()){
            throw new LinoleumValidationException(bindingResult);
        }
        return linoleumService.update(id,createEditLinoleumDto)
                .map(ResponseEntity::ok)
                .orElseThrow(LinoleumNotFoundException::new);
    }

    @GetMapping()
    @Operation(summary = "Find all linoleums by filter(or not), sort them(or not), generate pages")
    public PageResponse<ReadLinoleumDto> findAll(@RequestBody(required = false) LinoleumFilter filter,
                                                 @Parameter(description = "Page and sort parameters")
                                                 @ParameterObject Pageable pageable){
        return PageResponse.of(linoleumService.findAll(filter,pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Information about linoleum by its identifier")
    public ResponseEntity<ReadLinoleumDto> findById(@Parameter(description = "Linoleum identifier")
                                                        @PathVariable("id") Integer id){
        return linoleumService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(LinoleumNotFoundException::new);
    }

    @GetMapping("/{id}/image")
    @Operation(summary = "Get image of linoleum by linoleum identifier")
    public ResponseEntity<byte[]> getImage(@Parameter(description = "Linoleum identifier")
                                               @PathVariable("id") Integer id) {
        return linoleumService.getImage(id)
                .map(img->ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .contentLength(img.length)
                        .body(img))
                .orElseThrow(ImageNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete linoleum by its identifier")
    public ResponseEntity<?> delete(@Parameter(description = "Linoleum identifier")
                                        @PathVariable("id") Integer id){
        if(!linoleumService.delete(id)){
            throw new LinoleumNotFoundException();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/revision")
    @Operation(summary = "Find revision(auditing information) by revision and linoleum identifiers")
    public ResponseEntity<Revision<Long, Linoleum>> findRevisionByLinoleumIdAndRevNum(
                                                    @Parameter(description = "Linoleum identifier")
                                                    @PathVariable("id") Integer linoleumId,
                                                    @Parameter(description = "Revision identifier")
                                                    @RequestParam(value = "revNum") Long revisionNumber){
        return linoleumService.findRevisionByLinoleumIdAndRevNum(linoleumId,revisionNumber)
                .map(ResponseEntity::ok)
                .orElseThrow(RevisionNotFoundException::new);
    }

    @GetMapping("/{id}/revisions")
    @Operation(summary = "Find all revisions(auditing information) by linoleum identifier, generate pages")
    public PageResponse<Revision<Long, Linoleum>> findAllRevisionsByLinoleumId(
                                                  @Parameter(description = "Linoleum identifier")
                                                  @PathVariable("id") Integer linoleumId,
                                                  @ParameterObject Pageable pageable){
        return PageResponse.of(linoleumService.findAllRevisionsByLinoleumId(linoleumId,pageable));
    }

    @GetMapping("/revisions")
    @Operation(summary = "Find all revisions(auditing information)")
    public List<RevisionDto> findAllRevisions(){
        return linoleumService.findAllRevisions();
    }


}
