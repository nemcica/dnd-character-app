package com.an.character_app.calc;
import com.an.character_app.rules.model.ArmorCategory;
import com.an.character_app.rules.model.ProficiencyLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

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
    void testSavingThrowModifierCalculations(int abilityModifier, int proficiencyBonus, boolean isProficient, int expected){
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

}