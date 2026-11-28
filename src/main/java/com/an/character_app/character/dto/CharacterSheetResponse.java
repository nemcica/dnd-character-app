package com.an.character_app.character.dto;

import com.an.character_app.calc.CalculatedStats;
import com.an.character_app.rules.model.ArmorCategory;
import com.an.character_app.rules.model.Tool;

import java.util.Set;

public record CharacterSheetResponse(
        Long id,
        String name,
        int level,
        String speciesName,
        String className,
        String backgroundName,
        CalculatedStats calculatedStats,
        Set<Tool> toolProficiencies,
        Set<ArmorCategory> armorTraining
) {
}
