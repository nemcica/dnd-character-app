package com.an.character_app.character.dto;

import com.an.character_app.calc.CalculatedStats;

public record CharacterSheetResponse(
        Long id,
        String name,
        int level,
        String speciesName,
        String className,
        String backgroundName,
        CalculatedStats calculatedStats
) {
}
