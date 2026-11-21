package com.an.character_app.rules.mapper;

import com.an.character_app.rules.dto.ArmorResponse;
import com.an.character_app.rules.dto.BackgroundResponse;
import com.an.character_app.rules.dto.ClassResponse;
import com.an.character_app.rules.dto.SpeciesResponse;
import com.an.character_app.rules.model.ArmorDef;
import com.an.character_app.rules.model.BackgroundDef;
import com.an.character_app.rules.model.ClassDef;
import com.an.character_app.rules.model.SpeciesDef;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RulesMapper {
    ClassResponse toResponse(ClassDef classDef);

    ArmorResponse toResponse(ArmorDef armorDef);

    BackgroundResponse toResponse(BackgroundDef backgroundDef);

    SpeciesResponse toResponse(SpeciesDef speciesDef);
}
