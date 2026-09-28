package com.anem.character_app.baseClass;

import com.anem.character_app.baseClass.dto.BaseClassCreateRequest;
import com.anem.character_app.baseClass.dto.BaseClassResponse;
import com.anem.character_app.baseClass.dto.BaseClassUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BaseClassMapper {

    BaseClass toEntity(BaseClassCreateRequest request);

    BaseClassResponse toResponse(BaseClass baseClass);
    
    void updateEntityFromRequest(BaseClassUpdateRequest request, @MappingTarget BaseClass existingBaseClass);
}
