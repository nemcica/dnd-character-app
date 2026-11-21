package com.an.character_app.rules.dto;

import com.an.character_app.rules.model.CreatureSize;

import java.util.Set;

public record SpeciesResponse(
        String id,
        String name,
        int walkSpeed,
        Set<CreatureSize> creatureSizes,
        String creatureSizeDescription
) {}
