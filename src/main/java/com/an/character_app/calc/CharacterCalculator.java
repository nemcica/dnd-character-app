package com.an.character_app.calc;

import com.an.character_app.rules.model.ArmorCategory;
import com.an.character_app.rules.model.ProficiencyLevel;

import java.util.List;

public final class CharacterCalculator {

    public static int totalAbilityScore(int base, int bonus) {
        return Math.min(base + bonus, 20);
    }

    public static int abilityModifier(int score) {
        return Math.floorDiv(score - 10, 2);
    }

    public static int proficiencyBonus(int level) {
        return 2 + (level - 1) / 4;
    }

    public static int skillModifier(int abilityModifier, int proficiencyBonus, ProficiencyLevel proficiencyLevel) {
        return switch (proficiencyLevel) {
            case NONE -> abilityModifier;
            case PROFICIENT -> abilityModifier + proficiencyBonus;
            case EXPERTISE -> abilityModifier + proficiencyBonus * 2;
        };
    }

    public static int savingThrowModifier(int abilityModifier, int proficiencyBonus, boolean isProficient) {
        return (isProficient) ? abilityModifier + proficiencyBonus : abilityModifier;
    }

    public static int maxHp(int firstLevelHitDie, List<Integer> laterLevelHitDice, int conModifier) {
        int hp = firstLevelHitDie + conModifier;
        for (int die : laterLevelHitDice) {
            hp += Math.max(1, die / 2 + 1 + conModifier);
        }
        return hp;
    }

    public static int armorClass(int dexModifier, ArmorCategory category, Integer baseAc, Integer dexCap, int shieldBonus) {
        int total;
        if (category == null || baseAc == null) {
            total = 10 + dexModifier;
        } else {
            total = baseAc + switch (category) {
                case LIGHT  -> dexModifier;
                case MEDIUM -> Math.min(dexModifier, dexCap == null ? 2 : dexCap);
                case HEAVY, SHIELD -> 0;
            };
        }
        return total + shieldBonus;
    }

    public static int initiative(int dexModifier, int bonus) {
        return dexModifier + bonus;
    }

    public static int passivePerception(int wisModifier) {
        return 10 + wisModifier;
    }

    public static int passiveInsight(int wisModifier) {
        return 10 + wisModifier;
    }

    public static int passiveInvestigation(int intModifier) {
        return 10 + intModifier;
    }
}
