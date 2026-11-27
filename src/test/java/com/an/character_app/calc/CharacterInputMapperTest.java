package com.an.character_app.calc;

import com.an.character_app.character.TestCharacters;
import com.an.character_app.character.model.PlayerCharacter;
import com.an.character_app.rules.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CharacterInputMapperTest {

    TestCharacters testCharacters = new TestCharacters();

    private ArmorDef armor(ArmorCategory category, int baseAc, Integer dexCap) {
        ArmorDef a = mock(ArmorDef.class);
        when(a.getCategory()).thenReturn(category);
        when(a.getBaseAc()).thenReturn(baseAc);
        when(a.getDexCap()).thenReturn(dexCap);
        return a;
    }


    @Test
    void unarmoredCharacterHasNullArmorFieldsAndNoShieldBonus() {
        PlayerCharacter pc = testCharacters.baseCharacter();

        CharacterInput input = CharacterInputMapper.toInput(pc);

        assertThat(input.armorCategory()).isNull();
        assertThat(input.baseAc()).isNull();
        assertThat(input.dexCap()).isNull();
        assertThat(input.shieldBonus()).isZero();
    }

    @Test
    void armorAndShieldAreMapped() {
        PlayerCharacter pc = testCharacters.baseCharacter();
        pc.setArmor(armor(ArmorCategory.HEAVY, 16, 0));
        pc.setShield(armor(ArmorCategory.SHIELD, 2, null));

        CharacterInput input = CharacterInputMapper.toInput(pc);

        assertThat(input.armorCategory()).isEqualTo(ArmorCategory.HEAVY);
        assertThat(input.baseAc()).isEqualTo(16);
        assertThat(input.dexCap()).isZero();
        assertThat(input.shieldBonus()).isEqualTo(2);
    }

    @Test
    void lightArmorKeepsNullDexCap() {
        PlayerCharacter pc = testCharacters.baseCharacter();
        pc.setArmor(armor(ArmorCategory.LIGHT, 11, null));

        assertThat(CharacterInputMapper.toInput(pc).dexCap()).isNull();
    }

    @Test
    void baseScoresAndClassDataAreMapped() {
        CharacterInput input = CharacterInputMapper.toInput(testCharacters.baseCharacter());

        assertThat(input.level()).isEqualTo(5);
        assertThat(input.hitDie()).isEqualTo(10);
        assertThat(input.baseAbilityScores())
                .containsEntry(Ability.STR, 15)
                .containsEntry(Ability.CHA, 8);
        assertThat(input.savingThrowProficiencies())
                .containsExactlyInAnyOrder(Ability.STR, Ability.CON);
    }

    @Test
    void modifiersForTheSameAbilityAreSummed() {
        PlayerCharacter pc = testCharacters.baseCharacter();
        pc.addAbilityScoreModifier(Ability.STR, 2, "BACKGROUND:soldier");
        pc.addAbilityScoreModifier(Ability.STR, 1, "FEAT:test");

        assertThat(CharacterInputMapper.toInput(pc).bonusAbilityScores().get(Ability.STR))
                .isEqualTo(3);
    }

    @Test
    void bonusMapContainsEveryAbilityEvenWithoutModifiers() {
        Map<Ability, Integer> bonus =
                CharacterInputMapper.toInput(testCharacters.baseCharacter()).bonusAbilityScores();

        assertThat(bonus).hasSize(Ability.values().length);
        assertThat(bonus.values()).allMatch(v -> v == 0);
    }


    @Test
    void grantsAreMapped() {
        PlayerCharacter pc = testCharacters.baseCharacter();
        pc.addSkillProficiencyGrant(Skill.STEALTH, ProficiencyLevel.PROFICIENT, "CLASS:rogue");

        assertThat(CharacterInputMapper.toInput(pc).skillProficiencies())
                .containsOnly(entry(Skill.STEALTH, ProficiencyLevel.PROFICIENT));
    }

    @Test
    void sameSkillFromTwoGrantsStaysSingleProficiency() {
        PlayerCharacter pc = testCharacters.baseCharacter();
        pc.addSkillProficiencyGrant(Skill.STEALTH, ProficiencyLevel.PROFICIENT, "BACKGROUND:criminal");
        pc.addSkillProficiencyGrant(Skill.STEALTH, ProficiencyLevel.PROFICIENT, "FEAT:skilled");

        assertThat(CharacterInputMapper.toInput(pc).skillProficiencies())
                .containsOnly(entry(Skill.STEALTH, ProficiencyLevel.PROFICIENT));
    }

    @Test
    void expertiseUpgradesProficiency() {
        PlayerCharacter pc = testCharacters.baseCharacter();
        pc.addSkillProficiencyGrant(Skill.STEALTH, ProficiencyLevel.PROFICIENT, "BACKGROUND:criminal");
        pc.addSkillProficiencyGrant(Skill.STEALTH, ProficiencyLevel.EXPERTISE, "CLASS:rogue");

        assertThat(CharacterInputMapper.toInput(pc).skillProficiencies())
                .containsOnly(entry(Skill.STEALTH, ProficiencyLevel.EXPERTISE));
    }

    @Test
    void proficiencyNeverDowngradesExpertiseWhateverTheOrder() {
        PlayerCharacter expertiseFirst = testCharacters.baseCharacter();
        expertiseFirst.addSkillProficiencyGrant(Skill.STEALTH, ProficiencyLevel.EXPERTISE, "CLASS:rogue");
        expertiseFirst.addSkillProficiencyGrant(Skill.STEALTH, ProficiencyLevel.PROFICIENT, "BACKGROUND:criminal");

        PlayerCharacter proficiencyFirst = testCharacters.baseCharacter();
        proficiencyFirst.addSkillProficiencyGrant(Skill.STEALTH, ProficiencyLevel.PROFICIENT, "BACKGROUND:criminal");
        proficiencyFirst.addSkillProficiencyGrant(Skill.STEALTH, ProficiencyLevel.EXPERTISE, "CLASS:rogue");

        assertThat(CharacterInputMapper.toInput(expertiseFirst).skillProficiencies())
                .containsOnly(entry(Skill.STEALTH, ProficiencyLevel.EXPERTISE));
        assertThat(CharacterInputMapper.toInput(proficiencyFirst).skillProficiencies())
                .containsOnly(entry(Skill.STEALTH, ProficiencyLevel.EXPERTISE));
    }


}