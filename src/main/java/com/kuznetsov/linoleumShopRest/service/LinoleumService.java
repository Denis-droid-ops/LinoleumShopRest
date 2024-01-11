package com.kuznetsov.linoleumShopRest.service;

import com.kuznetsov.linoleumShopRest.dto.CreateEditLinoleumDto;
import com.kuznetsov.linoleumShopRest.dto.LinoleumFilter;
import com.kuznetsov.linoleumShopRest.dto.ReadLinoleumDto;

import com.kuznetsov.linoleumShopRest.entity.Linoleum;

import com.kuznetsov.linoleumShopRest.mapper.LinoleumMapper;
import com.kuznetsov.linoleumShopRest.repository.LinoleumRepository;

import com.kuznetsov.linoleumShopRest.util.QPredicates;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.util.Optional;

import static com.kuznetsov.linoleumShopRest.entity.QLinoleum.linoleum;


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
                .map(linoleumRepository::save)
                .map(linoleumMapper::mapToReadLinoleumDto)
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
               .map(Linoleum::getImagePath)
               .filter(StringUtils::hasText)
               .flatMap(imageService::get);
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
                .map(linoleumRepository::saveAndFlush)
                //saveAndFlush используется чтобы сразу отследить исключения
                .map(linoleumMapper::mapToReadLinoleumDto);
    }

    public Optional<ReadLinoleumDto> findById(Integer id){
        return linoleumRepository.findById(id)
                .map(linoleumMapper::mapToReadLinoleumDto);
    }

    public Page<ReadLinoleumDto> findAll(LinoleumFilter filter,Pageable pageable){
        if(filter==null) {
            return linoleumRepository.findAll(pageable).map(linoleumMapper::mapToReadLinoleumDto);
        }
        return linoleumRepository.findAll(QPredicates.builder()
                        .add(filter.getName(), linoleum.lName::containsIgnoreCase)
                        .add(filter.getProtect(), linoleum.protect::eq)
                        .add(filter.getThickness(),linoleum.thickness::eq)
                        .add(Optional.ofNullable(filter.getMinPrice()).orElse(0),
                                Optional.ofNullable(filter.getMaxPrice()).orElse(Integer.MAX_VALUE),
                                linoleum.price::between)
                        .build(),pageable)
                        .map(linoleumMapper::mapToReadLinoleumDto);
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
