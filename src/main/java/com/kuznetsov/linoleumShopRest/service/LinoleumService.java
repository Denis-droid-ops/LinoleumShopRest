package com.kuznetsov.linoleumShopRest.service;

import com.kuznetsov.linoleumShopRest.dto.CreateEditLinoleumDto;
import com.kuznetsov.linoleumShopRest.dto.ReadLinoleumDto;

import com.kuznetsov.linoleumShopRest.mapper.LinoleumMapper;
import com.kuznetsov.linoleumShopRest.repository.LinoleumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class LinoleumService {

    private final LinoleumRepository linoleumRepository;
    private final LinoleumMapper linoleumMapper;

    @Autowired
    public LinoleumService(LinoleumRepository linoleumRepository, LinoleumMapper linoleumMapper) {
        this.linoleumRepository = linoleumRepository;
        this.linoleumMapper = linoleumMapper;
    }

    @Transactional
    public ReadLinoleumDto save(CreateEditLinoleumDto createEditLinoleumDto){
        return Optional.of(createEditLinoleumDto)
                .map(dto->linoleumMapper.mapToLinoleum(createEditLinoleumDto))
                .map(linoleum -> linoleumRepository.save(linoleum))
                .map(saved->linoleumMapper.mapToReadLinoleumDto(saved))
                .orElseThrow();

    }

    @Transactional
    public Optional<ReadLinoleumDto> update(Integer id,CreateEditLinoleumDto createEditLinoleumDto){
        return linoleumRepository.findById(id)
                .map(linoleum -> linoleumMapper.updateLinoleumFromDto(createEditLinoleumDto,linoleum))
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
                    return true;
                })
                .orElse(false);
    }
}
