package com.anem.character_app.background;

import com.anem.character_app.background.dto.BackgroundCreateRequest;
import com.anem.character_app.background.dto.BackgroundResponse;
import com.anem.character_app.background.dto.BackgroundUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BackgroundMapper {

    Background toEntity(BackgroundCreateRequest request);

    BackgroundResponse toResponse(Background background);

    void updateEntityFromRequest(BackgroundUpdateRequest request, @MappingTarget Background existingBackground);
}
