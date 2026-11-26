package com.an.character_app.rules.dto;

import com.an.character_app.rules.model.Ability;
import com.an.character_app.rules.model.Skill;

import java.util.Set;

public record ClassResponse(
        String id,
        String name,
        int hitDie,
        Set<Ability> savingThrows,
        Set<Skill> skillOptions,
        int skillChoiceCount
) {
}
