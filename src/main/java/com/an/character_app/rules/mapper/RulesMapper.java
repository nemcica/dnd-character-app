package com.an.character_app.rules.mapper;

import com.an.character_app.rules.dto.*;
import com.an.character_app.rules.model.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RulesMapper {
    ClassResponse toResponse(ClassDef classDef);

    ArmorResponse toResponse(ArmorDef armorDef);

    BackgroundResponse toResponse(BackgroundDef backgroundDef);

    SpeciesResponse toResponse(SpeciesDef speciesDef);

    WeaponResponse toResponse(WeaponDef weaponDef);
}
