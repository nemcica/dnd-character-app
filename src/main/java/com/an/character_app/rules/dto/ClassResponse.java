package com.an.character_app.rules.dto;

import com.an.character_app.rules.model.Ability;
import com.an.character_app.rules.model.Skill;
import com.an.character_app.rules.model.Tool;
import com.an.character_app.rules.model.ToolCategory;

import java.util.Set;

public record ClassResponse(
        String id,
        String name,
        int hitDie,
        Set<Ability> savingThrows,
        Set<Skill> skillOptions,
        int skillChoiceCount,
        Set<Tool> fixedToolProficiencies,
        Integer toolChoiceCount,
        Set<ToolCategory> toolChoiceCategories
) {
}
