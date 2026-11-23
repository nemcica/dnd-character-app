package com.an.character_app.calc;

import com.an.character_app.rules.model.Ability;
import com.an.character_app.rules.model.Skill;

import java.util.Map;

public record CalculatedStats(
        int proficiencyBonus,
        Map<Ability, AbilityBreakdown> abilities,
        Map<Skill, Integer> skills,
        Map<Ability, Integer> savingThrows,
        int maxHp,
        int armorClass,
        int initiative,
        int passivePerception,
        int passiveInsight,
        int passiveInvestigation
) {
}
