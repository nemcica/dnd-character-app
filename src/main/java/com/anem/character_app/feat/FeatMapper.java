package com.anem.character_app.feat;

import com.anem.character_app.feat.dto.FeatCreateRequest;
import com.anem.character_app.feat.dto.FeatResponse;
import com.anem.character_app.feat.dto.FeatUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FeatMapper {

    Feat toEntity(FeatCreateRequest request);

    FeatResponse toResponse(Feat feat);

    void updateEntityFromRequest(FeatUpdateRequest request, @MappingTarget Feat existingFeat);
}
