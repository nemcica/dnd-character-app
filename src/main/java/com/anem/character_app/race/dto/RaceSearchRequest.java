package com.anem.character_app.race.dto;

import com.anem.character_app.generic.CreatureSize;

public record RaceSearchRequest(
        String name,
        CreatureSize size,
        String speed,
        String specialTraits
) {}
