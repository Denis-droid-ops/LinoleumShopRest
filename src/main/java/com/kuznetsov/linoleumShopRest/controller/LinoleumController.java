package com.kuznetsov.linoleumShopRest.controller;

import com.kuznetsov.linoleumShopRest.dto.*;
import com.kuznetsov.linoleumShopRest.entity.Linoleum;
import com.kuznetsov.linoleumShopRest.errorResponse.LinoleumErrorResponse;
import com.kuznetsov.linoleumShopRest.errorResponse.LinoleumValidationErrorResponse;
import com.kuznetsov.linoleumShopRest.errorResponse.RevisionErrorResponse;
import com.kuznetsov.linoleumShopRest.exception.ImageNotFoundException;
import com.kuznetsov.linoleumShopRest.exception.LinoleumNotFoundException;
import com.kuznetsov.linoleumShopRest.exception.LinoleumValidationException;
import com.kuznetsov.linoleumShopRest.exception.RevisionNotFoundException;
import com.kuznetsov.linoleumShopRest.service.LinoleumService;
import com.kuznetsov.linoleumShopRest.validator.LinoleumValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping(value = "/api/v1/linoleums", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Linoleum controller",description = "Methods for work with linoleum")
public class LinoleumController {

    private final LinoleumService linoleumService;
    private final LinoleumValidator linoleumValidator;

    @Autowired
    public LinoleumController(LinoleumService linoleumService, LinoleumValidator linoleumValidator) {
        this.linoleumService = linoleumService;
        this.linoleumValidator = linoleumValidator;
    }

    @Operation(summary = "Create new linoleum")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Linoleum creating success"),
            @ApiResponse(responseCode = "400", description = "Linoleum validation exception",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LinoleumValidationErrorResponse.class))})
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Linoleum updating success"),
            @ApiResponse(responseCode = "400", description = "Linoleum validation exception",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LinoleumValidationErrorResponse.class))})
    })
    @Operation(summary = "Update linoleum by its identifier")
    @PutMapping(value = "/{id}",consumes = "multipart/form-data")
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Linoleums finding success")
    })
    @Operation(summary = "Find all linoleums by filter(or not), sort them(or not), generate pages")
    @GetMapping
    public PageResponse<ReadLinoleumDto> findAll(@Parameter(description = "Filter by name or string")
                                                     @RequestParam(required = false) String name,
                                                 @Parameter(description = "Filter by protective layer")
                                                 @RequestParam(required = false) Float protect,
                                                 @Parameter(description = "Filter by total thickness")
                                                 @RequestParam(required = false) Float thickness,
                                                 @Parameter(description = "Filter by minimum price")
                                                 @RequestParam(required = false) Integer minPrice,
                                                 @Parameter(description = "Filter by maximum price")
                                                 @RequestParam(required = false) Integer maxPrice,
                                                 @Parameter(description = "Page and sort parameters")
                                                 @ParameterObject Pageable pageable){
        return PageResponse.of(linoleumService.findAll(
                new LinoleumFilter(name, protect, thickness, minPrice, maxPrice),
                pageable));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Linoleum finding success"),
            @ApiResponse(responseCode = "404", description = "Linoleum not found exception",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LinoleumErrorResponse.class))})
    })
    @Operation(summary = "Information about linoleum by its identifier")
    @GetMapping("/{id}")
    public ResponseEntity<ReadLinoleumDto> findById(@Parameter(description = "Linoleum identifier")
                                                        @PathVariable("id") Integer id){
        return linoleumService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(LinoleumNotFoundException::new);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image getting success"),
            @ApiResponse(responseCode = "404", description = "Image not found exception",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ImageNotFoundException.class))})
    })
    @Operation(summary = "Get image of linoleum by linoleum identifier")
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@Parameter(description = "Linoleum identifier")
                                               @PathVariable("id") Integer id) {
        return linoleumService.getImage(id)
                .map(img->ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .contentLength(img.length)
                        .body(img))
                .orElseThrow(ImageNotFoundException::new);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Linoleum deleting success"),
            @ApiResponse(responseCode = "404", description = "Linoleum not found exception",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LinoleumErrorResponse.class))})
    })
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete linoleum by its identifier")
    public ResponseEntity<?> delete(@Parameter(description = "Linoleum identifier")
                                        @PathVariable("id") Integer id){
        if(!linoleumService.delete(id)){
            throw new LinoleumNotFoundException();
        }
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Revision finding success"),
            @ApiResponse(responseCode = "404", description = "Revision not found exception",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RevisionErrorResponse.class))})
    })
    @Operation(summary = "Find revision(auditing information) by revision and linoleum identifiers")
    @GetMapping("/{id}/revision")
    public ResponseEntity<Revision<Long, Linoleum>> findRevisionByLinoleumIdAndRevNum(
                                                    @Parameter(description = "Linoleum identifier")
                                                    @PathVariable("id") Integer linoleumId,
                                                    @Parameter(description = "Revision identifier")
                                                    @RequestParam(value = "revNum") Long revisionNumber){
        return linoleumService.findRevisionByLinoleumIdAndRevNum(linoleumId,revisionNumber)
                .map(ResponseEntity::ok)
                .orElseThrow(RevisionNotFoundException::new);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Revisions finding success")
    })
    @Operation(summary = "Find all revisions(auditing information) by linoleum identifier, generate pages")
    @GetMapping("/{id}/revisions")
    public PageResponse<Revision<Long, Linoleum>> findAllRevisionsByLinoleumId(
                                                  @Parameter(description = "Linoleum identifier")
                                                  @PathVariable("id") Integer linoleumId,
                                                  @ParameterObject Pageable pageable){
        return PageResponse.of(linoleumService.findAllRevisionsByLinoleumId(linoleumId,pageable));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Revisions finding success")
    })
    @Operation(summary = "Find all revisions(auditing information)")
    @GetMapping("/revisions")
    public List<RevisionDto> findAllRevisions(){
        return linoleumService.findAllRevisions();
    }


}
