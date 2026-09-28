package com.anem.character_app.race.dto;

import com.anem.character_app.generic.CreatureSize;

public record RaceResponse(
        String id,
        String name,
        String creatureType,
        CreatureSize size,
        String speed,
        String specialTraits
) {}
