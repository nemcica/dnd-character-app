package com.an.character_app.mapper;

import com.an.character_app.character.dto.AbilityScoresRequest;
import com.an.character_app.character.model.AbilityScores;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AbilityScoresMapper {

    AbilityScores toEntity(AbilityScoresRequest request);
}
