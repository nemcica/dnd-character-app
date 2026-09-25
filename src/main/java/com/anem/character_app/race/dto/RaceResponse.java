package com.anem.character_app.race.dto;

public record RaceResponse(
        String id,
        String name,
        String creatureType,
        String size,
        String speed,
        String specialTraits
) {}
