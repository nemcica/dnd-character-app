package com.anem.character_app.spell;

import com.anem.character_app.spell.dto.SpellCreateRequest;
import com.anem.character_app.spell.dto.SpellResponse;
import com.anem.character_app.spell.dto.SpellUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SpellMapper {

    Spell toEntity(SpellCreateRequest request);

    SpellResponse toResponse(Spell spell);

    void updateEntityFromRequest(SpellUpdateRequest request, @MappingTarget Spell existingSpell);
}
