package com.anem.character_app.spell.dto;

import com.anem.character_app.generic.SpellSchool;

public record SpellSearchRequest(
        String name,
        SpellSchool spellSchool,
        Integer spellLevel,
        String castingTime,
        String duration,
        String components,
        String range,
        String description
) {}
