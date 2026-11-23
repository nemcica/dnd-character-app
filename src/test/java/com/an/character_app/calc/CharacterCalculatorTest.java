package com.an.character_app.calc;

import com.an.character_app.rules.model.Ability;
import com.an.character_app.rules.model.ArmorCategory;
import com.an.character_app.rules.model.ProficiencyLevel;
import com.an.character_app.rules.model.Skill;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.an.character_app.calc.CharacterCalculator.*;
import static org.assertj.core.api.Assertions.assertThat;

class CharacterCalculatorTest {
    @ParameterizedTest
    @CsvSource({
            "1, 2", "4, 2", "5, 3", "8, 3", "9, 4",
            "12, 4", "13, 5", "16, 5", "17, 6", "20, 6"
    })
    void proficiencyBonusByLevel(int level, int expected) {
        assertThat(proficiencyBonus(level)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "1, -5", "8, -1", "9, -1", "10, 0",
            "11, 0", "12, 1", "15, 2", "20, 5"
    })
    void abilityModifierFromScore(int score, int expected) {
        assertThat(abilityModifier(score)).isEqualTo(expected);
    }

    @Test
    void totalAbilityScoreIsCappedAtTwenty() {
        assertThat(totalAbilityScore(19, 3)).isEqualTo(20);
    }

    @Test
    void totalAbilityScoreAddsBonusBelowCap() {
        assertThat(totalAbilityScore(15, 2)).isEqualTo(17);
    }

    @ParameterizedTest
    @CsvSource({
            "3,  2, NONE,       3",
            "3,  2, PROFICIENT, 5",
            "3,  2, EXPERTISE,  7",
            "-1, 2, NONE,      -1",
            "-1, 2, PROFICIENT, 1",
            "-1, 3, EXPERTISE,  5",
            "4,  0, EXPERTISE,  4"
    })
    void testSkillModifierCalculations(int abilityMod, int profBonus, ProficiencyLevel level, int expected) {
        assertThat(skillModifier(abilityMod, profBonus, level)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "3,  2, false, 3",
            "3,  2, true,  5",
            "-1, 2, false, -1",
            "-1, 2, true,  1",
            "0,  0, false, 0"
    })
    void testSavingThrowModifierCalculations(int abilityModifier, int proficiencyBonus, boolean isProficient, int expected) {
        assertThat(savingThrowModifier(abilityModifier, proficiencyBonus, isProficient)).isEqualTo(expected);
    }

    @Test
    void testMaxHp() {
        //assertThat(maxHp(10, List.of(6, 6, 10), -1)).isEqualTo(20);
        assertThat(maxHp(8, List.of(6), -4)).isEqualTo(5);
    }

    @ParameterizedTest
    @CsvSource({
            "3,      ,    ,  , 0, 13",
            "3, LIGHT,  11,  , 0, 14",
            "3, MEDIUM, 14, 2, 0, 16",
            "1, MEDIUM, 14, 2, 0, 15",
            "3, HEAVY,  16,  , 0, 16",
            "2, LIGHT,  11,  , 2, 15"
    })
    void testArmorClassCalculations(int dexModifier, ArmorCategory category, Integer baseAc, Integer dexCap, int shieldBonus, int expected) {
        assertThat(armorClass(dexModifier, category, baseAc, dexCap, shieldBonus)).isEqualTo(expected);
    }

    @Test
    void calculatesFullSheetForLevelFiveFighter() {
        CharacterInput characterInput = new CharacterInput(
                5,
                10,
                Map.of(Ability.STR, 15, Ability.DEX, 14, Ability.CON, 13,
                        Ability.INT, 12, Ability.WIS, 10, Ability.CHA, 8),
                Map.of(Ability.STR, 2, Ability.CON, 1),
                Map.of(Skill.ATHLETICS, ProficiencyLevel.PROFICIENT,
                        Skill.PERCEPTION, ProficiencyLevel.EXPERTISE),
                Set.of(Ability.STR, Ability.CON),
                ArmorCategory.HEAVY, 16, 0, 2);

        CalculatedStats stats = calculate(characterInput);

        assertThat(stats.proficiencyBonus()).isEqualTo(3);
        assertThat(stats.abilities().get(Ability.STR).total()).isEqualTo(17);
        assertThat(stats.abilities().get(Ability.STR).modifier()).isEqualTo(3);
        assertThat(stats.skills().get(Skill.ATHLETICS)).isEqualTo(6);
        assertThat(stats.skills().get(Skill.PERCEPTION)).isEqualTo(6);
        assertThat(stats.skills().get(Skill.STEALTH)).isEqualTo(2);
        assertThat(stats.savingThrows().get(Ability.STR)).isEqualTo(6);
        assertThat(stats.savingThrows().get(Ability.CON)).isEqualTo(5);
        assertThat(stats.savingThrows().get(Ability.DEX)).isEqualTo(2);
        assertThat(stats.maxHp()).isEqualTo(44);
        assertThat(stats.armorClass()).isEqualTo(18);
        assertThat(stats.initiative()).isEqualTo(2);
        assertThat(stats.passivePerception()).isEqualTo(16);
        assertThat(stats.passiveInsight()).isEqualTo(10);
        assertThat(stats.passiveInvestigation()).isEqualTo(11);
    }

    @Test
    void calculatesUnarmoredLevelOneCharacterWithNoProficiencies() {
        CharacterInput input = new CharacterInput(
                1, 8,
                Map.of(Ability.STR, 10, Ability.DEX, 16, Ability.CON, 14,
                        Ability.INT, 10, Ability.WIS, 10, Ability.CHA, 10),
                Map.of(),
                Map.of(),
                Set.of(),
                null, null, null, 0);

        CalculatedStats stats = CharacterCalculator.calculate(input);

        assertThat(stats.proficiencyBonus()).isEqualTo(2);
        assertThat(stats.maxHp()).isEqualTo(10);
        assertThat(stats.armorClass()).isEqualTo(13);
        assertThat(stats.skills()).hasSize(Skill.values().length);
        assertThat(stats.skills().get(Skill.STEALTH)).isEqualTo(3);
        assertThat(stats.passivePerception()).isEqualTo(10);
    }

}