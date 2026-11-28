package com.an.character_app.calc;

import com.an.character_app.rules.model.Ability;
import com.an.character_app.rules.model.ArmorCategory;
import com.an.character_app.rules.model.ProficiencyLevel;
import com.an.character_app.rules.model.Skill;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.EnumMap;
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
                new ArmorFacts(ArmorCategory.HEAVY, 16, 0, 13, true, true, 2),
                Set.of(ArmorCategory.LIGHT, ArmorCategory.MEDIUM, ArmorCategory.HEAVY, ArmorCategory.SHIELD));

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
                ArmorFacts.none(),
                Set.of());

        CalculatedStats stats = CharacterCalculator.calculate(input);

        assertThat(stats.proficiencyBonus()).isEqualTo(2);
        assertThat(stats.maxHp()).isEqualTo(10);
        assertThat(stats.armorClass()).isEqualTo(13);
        assertThat(stats.skills()).hasSize(Skill.values().length);
        assertThat(stats.skills().get(Skill.STEALTH)).isEqualTo(3);
        assertThat(stats.passivePerception()).isEqualTo(10);
    }

    private static CharacterInput input(ArmorFacts armor, Set<ArmorCategory> training, int str, int dex) {
        return input(armor, training, str, dex, Map.of());
    }

    private static CharacterInput input(ArmorFacts armor, Set<ArmorCategory> training, int str, int dex,
                                        Map<Ability, Integer> bonuses) {
        Map<Ability, Integer> base = new EnumMap<>(Ability.class);
        for (Ability a : Ability.values()) {
            base.put(a, 10);
        }
        base.put(Ability.STR, str);
        base.put(Ability.DEX, dex);
        return new CharacterInput(1, 8, base, bonuses, Map.of(), Set.of(), armor, training);
    }

    private static final ArmorFacts SHIELD_ONLY = new ArmorFacts(null, null, null, null, false, true, 2);
    private static final ArmorFacts LEATHER = new ArmorFacts(ArmorCategory.LIGHT, 11, null, null, false, false, 0);
    private static final ArmorFacts CHAIN_MAIL = new ArmorFacts(ArmorCategory.HEAVY, 16, 0, 13, true, false, 0);

    @Test
    void shieldCountsWhenTrained() {
        assertThat(effectiveShieldBonus(Set.of(ArmorCategory.SHIELD), 2)).isEqualTo(2);
    }

    @Test
    void shieldDoesNotCountWhenUntrained() {
        assertThat(effectiveShieldBonus(Set.of(ArmorCategory.LIGHT, ArmorCategory.HEAVY), 2)).isZero();
        assertThat(effectiveShieldBonus(Set.of(), 2)).isZero();
    }

    @Test
    void noShieldGivesNoBonusEvenWhenTrained() {
        assertThat(effectiveShieldBonus(Set.of(ArmorCategory.SHIELD), 0)).isZero();
    }

    @Test
    void unarmoredCharacterWithNoShieldHasNoWarnings() {
        assertThat(armorWarnings(ArmorFacts.none(), Set.of(), 8)).isEmpty();
    }

    @Test
    void trainedArmorWithNoIssuesHasNoWarnings() {
        assertThat(armorWarnings(LEATHER, Set.of(ArmorCategory.LIGHT), 10)).isEmpty();
    }

    @ParameterizedTest
    @EnumSource(value = ArmorCategory.class, names = {"LIGHT", "MEDIUM", "HEAVY"})
    void untrainedArmorCategoryIsFlagged(ArmorCategory category) {
        ArmorFacts armor = new ArmorFacts(category, 12, null, null, false, false, 0);

        assertThat(armorWarnings(armor, Set.of(), 10)).containsExactly(ArmorWarning.UNTRAINED_ARMOR);
    }

    @Test
    void trainingInAnotherCategoryDoesNotCount() {
        ArmorFacts heavy = new ArmorFacts(ArmorCategory.HEAVY, 18, 0, null, false, false, 0);

        assertThat(armorWarnings(heavy, Set.of(ArmorCategory.LIGHT, ArmorCategory.MEDIUM), 10))
                .containsExactly(ArmorWarning.UNTRAINED_ARMOR);
    }

    @Test
    void strengthExactlyAtRequirementIsNotFlagged() {
        assertThat(armorWarnings(CHAIN_MAIL, Set.of(ArmorCategory.HEAVY), 13))
                .doesNotContain(ArmorWarning.STR_REQUIREMENT);
    }

    @Test
    void strengthOneBelowRequirementIsFlagged() {
        assertThat(armorWarnings(CHAIN_MAIL, Set.of(ArmorCategory.HEAVY), 12))
                .contains(ArmorWarning.STR_REQUIREMENT);
    }

    @Test
    void noStrengthRequirementIsNeverFlagged() {
        assertThat(armorWarnings(LEATHER, Set.of(ArmorCategory.LIGHT), 3)).isEmpty();
    }

    @Test
    void stealthDisadvantageIsIndependentOfTrainingAndStrength() {
        ArmorFacts stealthy = new ArmorFacts(ArmorCategory.MEDIUM, 14, 2, null, true, false, 0);

        assertThat(armorWarnings(stealthy, Set.of(ArmorCategory.MEDIUM), 10))
                .containsExactly(ArmorWarning.STEALTH_DISADVANTAGE);
    }

    @Test
    void untrainedShieldWithoutArmorIsFlagged() {
        assertThat(armorWarnings(SHIELD_ONLY, Set.of(), 10))
                .containsExactly(ArmorWarning.UNTRAINED_SHIELD);
    }

    @Test
    void trainedShieldIsNotFlagged() {
        assertThat(armorWarnings(SHIELD_ONLY, Set.of(ArmorCategory.SHIELD), 10)).isEmpty();
    }

    @Test
    void trainedArmorWithUntrainedShieldFlagsOnlyTheShield() {
        ArmorFacts both = new ArmorFacts(ArmorCategory.LIGHT, 11, null, null, false, true, 2);

        assertThat(armorWarnings(both, Set.of(ArmorCategory.LIGHT), 10))
                .containsExactly(ArmorWarning.UNTRAINED_SHIELD);
    }

    @Test
    void everythingWrongGivesAllFourWarnings() {
        ArmorFacts plateAndShield = new ArmorFacts(ArmorCategory.HEAVY, 18, 0, 15, true, true, 2);

        assertThat(armorWarnings(plateAndShield, Set.of(), 8))
                .containsExactlyInAnyOrder(ArmorWarning.UNTRAINED_ARMOR, ArmorWarning.UNTRAINED_SHIELD,
                        ArmorWarning.STR_REQUIREMENT, ArmorWarning.STEALTH_DISADVANTAGE);
    }

    @Test
    void untrainedShieldGivesNoAcBonusAndAWarning() {
        CalculatedStats stats = CharacterCalculator.calculate(input(SHIELD_ONLY, Set.of(), 10, 14));

        assertThat(stats.armorClass()).isEqualTo(12);
        assertThat(stats.armorWarnings()).containsExactly(ArmorWarning.UNTRAINED_SHIELD);
    }

    @Test
    void trainedFighterInChainMailWithShieldGetsFullAc() {
        ArmorFacts chainAndShield = new ArmorFacts(ArmorCategory.HEAVY, 16, 0, 13, true, true, 2);
        Set<ArmorCategory> training = Set.of(ArmorCategory.LIGHT, ArmorCategory.MEDIUM,
                ArmorCategory.HEAVY, ArmorCategory.SHIELD);

        CalculatedStats stats = CharacterCalculator.calculate(input(chainAndShield, training, 15, 14));

        assertThat(stats.armorClass()).isEqualTo(18);
        assertThat(stats.armorWarnings()).containsExactly(ArmorWarning.STEALTH_DISADVANTAGE);
    }

    @Test
    void untrainedArmorButTrainedShieldStillCountsTheShield() {
        ArmorFacts leatherAndShield = new ArmorFacts(ArmorCategory.LIGHT, 11, null, null, false, true, 2);

        CalculatedStats stats = CharacterCalculator.calculate(
                input(leatherAndShield, Set.of(ArmorCategory.SHIELD), 10, 14));

        assertThat(stats.armorClass()).isEqualTo(15);
        assertThat(stats.armorWarnings()).containsExactly(ArmorWarning.UNTRAINED_ARMOR);
    }

    @Test
    void strengthWarningUsesTheTotalScoreIncludingBonuses() {
        Set<ArmorCategory> heavy = Set.of(ArmorCategory.HEAVY);

        assertThat(CharacterCalculator.calculate(input(CHAIN_MAIL, heavy, 12, 10)).armorWarnings())
                .contains(ArmorWarning.STR_REQUIREMENT);
        assertThat(CharacterCalculator.calculate(input(CHAIN_MAIL, heavy, 12, 10, Map.of(Ability.STR, 1))).armorWarnings())
                .doesNotContain(ArmorWarning.STR_REQUIREMENT);
    }
}