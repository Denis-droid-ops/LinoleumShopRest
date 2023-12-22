package com.kuznetsov.linoleumShopRest.controller;

import com.kuznetsov.linoleumShopRest.dto.CreateEditLinoleumDto;
import com.kuznetsov.linoleumShopRest.dto.ReadLinoleumDto;
import com.kuznetsov.linoleumShopRest.exception.LinoleumNotFoundException;
import com.kuznetsov.linoleumShopRest.service.LinoleumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;



import java.util.List;


@RestController
@RequestMapping("/api/v1/linoleums")
public class LinoleumController {

    private final LinoleumService linoleumService;

    @Autowired
    public LinoleumController(LinoleumService linoleumService) {
        this.linoleumService = linoleumService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ReadLinoleumDto create(@RequestBody CreateEditLinoleumDto createEditLinoleumDto){
        return linoleumService.save(createEditLinoleumDto);
    }

    @PutMapping("/{id}")
    public ReadLinoleumDto update(@PathVariable("id") Integer id,
                                             @RequestBody CreateEditLinoleumDto createEditLinoleumDto){
        return linoleumService.update(id,createEditLinoleumDto)
                .orElseThrow(()->new LinoleumNotFoundException());
    }

    @GetMapping()
    public List<ReadLinoleumDto> findAll(){
        return linoleumService.findAll();
    }

    @GetMapping("/{id}")
    public ReadLinoleumDto findById(@PathVariable("id") Integer id){
        return linoleumService.findById(id)
                .orElseThrow(()->new LinoleumNotFoundException());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Integer id){
        if(!linoleumService.delete(id)){
            throw new LinoleumNotFoundException();
        }
    }



}
