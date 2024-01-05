package com.kuznetsov.linoleumShopRest.service;

import com.kuznetsov.linoleumShopRest.dto.CreateEditLinoleumDto;
import com.kuznetsov.linoleumShopRest.dto.ReadLinoleumDto;

import com.kuznetsov.linoleumShopRest.mapper.LinoleumMapper;
import com.kuznetsov.linoleumShopRest.repository.LinoleumRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class LinoleumService {
    
    private final LinoleumRepository linoleumRepository;
    private final LinoleumMapper linoleumMapper;
    private final ImageService imageService;

    @Autowired
    public LinoleumService(LinoleumRepository linoleumRepository, LinoleumMapper linoleumMapper, ImageService imageService) {
        this.linoleumRepository = linoleumRepository;
        this.linoleumMapper = linoleumMapper;
        this.imageService = imageService;
    }

    @Transactional
    public ReadLinoleumDto save(CreateEditLinoleumDto createEditLinoleumDto){
        return Optional.of(createEditLinoleumDto)
                .map(dto->{
                    uploadImage(dto.getImage());
                    return linoleumMapper.mapToLinoleum(createEditLinoleumDto);
                })
                .map(linoleum -> linoleumRepository.save(linoleum))
                .map(saved->linoleumMapper.mapToReadLinoleumDto(saved))
                .orElseThrow();

    }

    @SneakyThrows
    private void uploadImage(MultipartFile image){
        if (!image.isEmpty()){
            imageService.upload(image.getOriginalFilename(),image.getInputStream());
        }
    }

    @SneakyThrows
    public Optional<byte[]> getImage(Integer id) {
       return linoleumRepository.findById(id)
               .map(linoleum -> linoleum.getImagePath())
               .filter(imagePath->StringUtils.hasText(imagePath))
               .flatMap(imagePath->imageService.get(imagePath));
    }

    @SneakyThrows
    private void deleteImage(String imagePath){
        if (!imagePath.isEmpty()){
            imageService.delete(imagePath);
        }
    }

    @Transactional
    public Optional<ReadLinoleumDto> update(Integer id,CreateEditLinoleumDto createEditLinoleumDto){
        return linoleumRepository.findById(id)
                .map(linoleum->{
                    deleteImage(linoleum.getImagePath());
                    uploadImage(createEditLinoleumDto.getImage());
                    return linoleumMapper.updateLinoleumFromDto(createEditLinoleumDto,linoleum);
                })
                .map(linoleum -> linoleumRepository.saveAndFlush(linoleum))
                //saveAndFlush используется чтобы сразу отследить исключения
                .map(saved->linoleumMapper.mapToReadLinoleumDto(saved));
    }

    public Optional<ReadLinoleumDto> findById(Integer id){
        return linoleumRepository.findById(id)
                .map(linoleum -> linoleumMapper.mapToReadLinoleumDto(linoleum));
    }

    public List<ReadLinoleumDto> findAll(){
        return linoleumMapper.entitiesToDtos(linoleumRepository.findAll());
    }

    @Transactional
    public boolean delete(Integer id){
        return linoleumRepository.findById(id)
                .map(linoleum -> {
                    linoleumRepository.delete(linoleum);
                    deleteImage(linoleum.getImagePath());
                    return true;
                }).orElse(false);
    }
}
