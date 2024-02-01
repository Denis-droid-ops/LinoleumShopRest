package com.kuznetsov.linoleumShopRest.validator;

import com.kuznetsov.linoleumShopRest.dto.CreateEditLinoleumDto;
import com.kuznetsov.linoleumShopRest.service.LinoleumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LinoleumValidator implements Validator {

    private final LinoleumService linoleumService;

    @Autowired
    public LinoleumValidator(LinoleumService linoleumService) {
        this.linoleumService = linoleumService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz==CreateEditLinoleumDto.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateEditLinoleumDto createEditLinoleumDto = (CreateEditLinoleumDto) target;
        linoleumService.findByLName(createEditLinoleumDto.getLName())
                .ifPresent(readLinoleumDto ->
                        errors.rejectValue("lName","","Linoleum name is already taken! Must be unique!"));
        linoleumService.findByImageName(createEditLinoleumDto.getImage().getOriginalFilename())
                .ifPresent(readLinoleumDto ->
                        errors.rejectValue("image","","Image path is already use! Must be unique!"));
    }
}
