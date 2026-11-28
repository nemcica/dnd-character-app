package com.an.character_app.calc;

import com.an.character_app.rules.model.Ability;
import com.an.character_app.rules.model.ArmorCategory;
import com.an.character_app.rules.model.ProficiencyLevel;
import com.an.character_app.rules.model.Skill;

import java.util.Map;
import java.util.Set;

public record CharacterInput(
        int level,
        int hitDie,
        Map<Ability, Integer> baseAbilityScores,
        Map<Ability, Integer> bonusAbilityScores,
        Map<Skill, ProficiencyLevel> skillProficiencies,
        Set<Ability> savingThrowProficiencies,
        ArmorFacts armor,
        Set<ArmorCategory> armorTraining
) {
}
