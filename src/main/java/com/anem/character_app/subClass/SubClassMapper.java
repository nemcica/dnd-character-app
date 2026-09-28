package com.anem.character_app.subClass;

import com.anem.character_app.subClass.dto.SubClassCreateRequest;
import com.anem.character_app.subClass.dto.SubClassResponse;
import com.anem.character_app.subClass.dto.SubClassUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SubClassMapper {

    SubClass toEntity(SubClassCreateRequest request);

    SubClassResponse toResponse(SubClass subClass);

    void updateFromRequest(SubClassUpdateRequest subClassUpdateRequest, @MappingTarget SubClass existingSubclass);
}
