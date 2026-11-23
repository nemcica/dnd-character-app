package com.an.character_app.calc;

import com.an.character_app.rules.model.Ability;
import com.an.character_app.rules.model.ArmorCategory;
import com.an.character_app.rules.model.ProficiencyLevel;
import com.an.character_app.rules.model.Skill;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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
                case LIGHT -> dexModifier;
                case MEDIUM -> Math.min(dexModifier, dexCap == null ? 2 : dexCap);
                case HEAVY, SHIELD -> 0;
            };
        }
        return total + shieldBonus;
    }

    public static int initiative(int dexModifier, int bonus) {
        return dexModifier + bonus;
    }

    public static int passiveScore(int skillModifier) {
        return 10 + skillModifier;
    }

    public static CalculatedStats calculate(CharacterInput characterInput) {
        int proficiencyBonus = proficiencyBonus(characterInput.level());

        Map<Ability, AbilityBreakdown> abilities = new EnumMap<>(Ability.class);
        for (Map.Entry<Ability, Integer> entry : characterInput.baseAbilityScores().entrySet()) {
            Ability ability = entry.getKey();
            int baseValue = entry.getValue();
            int bonusValue = characterInput.bonusAbilityScores().getOrDefault(ability, 0);
            int total = totalAbilityScore(baseValue, bonusValue);
            int modifier = abilityModifier(total);
            abilities.put(ability, new AbilityBreakdown(baseValue, bonusValue, total, modifier));
        }

        Map<Skill, Integer> skills = new EnumMap<>(Skill.class);
        for (Skill s : Skill.values()) {
            int value = skillModifier(abilities.get(s.getAbility()).modifier(), proficiencyBonus, characterInput.skillProficiencies().getOrDefault(s, ProficiencyLevel.NONE));
            skills.put(s, value);
        }

        Map<Ability, Integer> savingThrows = new EnumMap<>(Ability.class);
        for (Ability a : Ability.values()) {
            int value = savingThrowModifier(abilities.get(a).modifier(), proficiencyBonus, characterInput.savingThrowProficiencies().contains(a));
            savingThrows.put(a, value);
        }

        int maxHp = maxHp(characterInput.hitDie(), Collections.nCopies(characterInput.level() - 1, characterInput.hitDie()), abilities.get(Ability.CON).modifier());

        int armorClass = armorClass(abilities.get(Ability.DEX).modifier(), characterInput.armorCategory(), characterInput.baseAc(), characterInput.dexCap(), characterInput.shieldBonus());

        int initiative = initiative(abilities.get(Ability.DEX).modifier(), 0); //TO-DO: add initiative bonuses to char input

        int passivePerception = passiveScore(skills.get(Skill.PERCEPTION));
        int passiveInsight = passiveScore(skills.get(Skill.INSIGHT));
        int passiveInvestigation = passiveScore(skills.get(Skill.INVESTIGATION));

        return new CalculatedStats(
                proficiencyBonus,
                abilities,
                skills,
                savingThrows,
                maxHp,
                armorClass,
                initiative,
                passivePerception,
                passiveInsight,
                passiveInvestigation
        );
    }

    private CharacterCalculator() {
    }
}
