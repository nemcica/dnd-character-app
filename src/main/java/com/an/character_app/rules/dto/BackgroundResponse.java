package com.an.character_app.rules.dto;

import com.an.character_app.rules.model.Ability;
import com.an.character_app.rules.model.Skill;
import com.an.character_app.rules.model.Tool;
import com.an.character_app.rules.model.ToolCategory;

import java.util.Set;

public record BackgroundResponse(
        String id,
        String name,
        String source,
        Set<Ability> abilityOptions,
        Set<Skill> skills,
        Tool tool,
        ToolCategory toolCategory
) {
}
