package com.an.character_app.character.dto;

public record CharacterSummary(
        Long id,
        String name,
        int level,
        String className,
        String speciesName,
        String backgroundName
) {
}
