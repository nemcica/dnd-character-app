package com.an.character_app.calc;

import com.an.character_app.character.model.AbilityScores;
import com.an.character_app.character.model.PlayerCharacter;
import com.an.character_app.rules.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CharacterInputMapperTest {

    private ClassDef fighterClass() {
        ClassDef c = mock(ClassDef.class);
        when(c.getHitDie()).thenReturn(10);
        when(c.getSavingThrows()).thenReturn(Set.of(Ability.STR, Ability.CON));
        return c;
    }

    private ArmorDef armor(ArmorCategory category, int baseAc, Integer dexCap) {
        ArmorDef a = mock(ArmorDef.class);
        when(a.getCategory()).thenReturn(category);
        when(a.getBaseAc()).thenReturn(baseAc);
        when(a.getDexCap()).thenReturn(dexCap);
        return a;
    }

    private BackgroundDef background(Skill... skills) {
        BackgroundDef b = mock(BackgroundDef.class);
        when(b.getSkills()).thenReturn(Set.of(skills));
        return b;
    }

    private PlayerCharacter baseCharacter() {
        PlayerCharacter pc = new PlayerCharacter();
        pc.setLevel(5);
        pc.setBaseAbilityScores(new AbilityScores(15, 14, 13, 12, 10, 8));
        pc.setCharacterClass(fighterClass());
        pc.setBackground(background());
        return pc;
    }

    @Test
    void unarmoredCharacterHasNullArmorFieldsAndNoShieldBonus() {
        PlayerCharacter pc = baseCharacter();       // no armor, no shield

        CharacterInput input = CharacterInputMapper.toInput(pc);

        assertThat(input.armorCategory()).isNull();
        assertThat(input.baseAc()).isNull();
        assertThat(input.dexCap()).isNull();
        assertThat(input.shieldBonus()).isZero();
    }

    @Test
    void armorAndShieldAreMapped() {
        PlayerCharacter pc = baseCharacter();
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
        PlayerCharacter pc = baseCharacter();
        pc.setArmor(armor(ArmorCategory.LIGHT, 11, null));

        assertThat(CharacterInputMapper.toInput(pc).dexCap()).isNull();
    }

    @Test
    void baseScoresAndClassDataAreMapped() {
        CharacterInput input = CharacterInputMapper.toInput(baseCharacter());

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
        PlayerCharacter pc = baseCharacter();
        pc.addAbilityScoreModifier(Ability.STR, 2, "BACKGROUND:soldier");
        pc.addAbilityScoreModifier(Ability.STR, 1, "FEAT:test");

        assertThat(CharacterInputMapper.toInput(pc).bonusAbilityScores().get(Ability.STR))
                .isEqualTo(3);
    }

    @Test
    void bonusMapContainsEveryAbilityEvenWithoutModifiers() {
        Map<Ability, Integer> bonus =
                CharacterInputMapper.toInput(baseCharacter()).bonusAbilityScores();

        assertThat(bonus).hasSize(Ability.values().length);
        assertThat(bonus.values()).allMatch(v -> v == 0);
    }

    @Test
    void backgroundSkillsAreProficient() {
        PlayerCharacter pc = baseCharacter();
        pc.setBackground(background(Skill.ATHLETICS, Skill.INTIMIDATION));

        assertThat(CharacterInputMapper.toInput(pc).skillProficiencies())
                .containsOnly(entry(Skill.ATHLETICS, ProficiencyLevel.PROFICIENT),
                        entry(Skill.INTIMIDATION, ProficiencyLevel.PROFICIENT));
    }

    @Test
    void grantsAreMapped() {
        PlayerCharacter pc = baseCharacter();
        pc.addSkillProficiencyGrant(Skill.STEALTH, ProficiencyLevel.PROFICIENT, "CLASS:rogue");

        assertThat(CharacterInputMapper.toInput(pc).skillProficiencies())
                .containsOnly(entry(Skill.STEALTH, ProficiencyLevel.PROFICIENT));
    }

    @Test
    void sameSkillFromTwoSourcesStaysSingleProficiency() {
        PlayerCharacter pc = baseCharacter();
        pc.setBackground(background(Skill.STEALTH));
        pc.addSkillProficiencyGrant(Skill.STEALTH, ProficiencyLevel.PROFICIENT, "CLASS:rogue");

        assertThat(CharacterInputMapper.toInput(pc).skillProficiencies())
                .containsOnly(entry(Skill.STEALTH, ProficiencyLevel.PROFICIENT));
    }

    @Test
    void expertiseUpgradesProficiency() {
        PlayerCharacter pc = baseCharacter();
        pc.setBackground(background(Skill.STEALTH));
        pc.addSkillProficiencyGrant(Skill.STEALTH, ProficiencyLevel.EXPERTISE, "CLASS:rogue");

        assertThat(CharacterInputMapper.toInput(pc).skillProficiencies())
                .containsOnly(entry(Skill.STEALTH, ProficiencyLevel.EXPERTISE));
    }

    @Test
    void proficiencyGrantNeverDowngradesExpertise() {
        PlayerCharacter pc = baseCharacter();
        pc.addSkillProficiencyGrant(Skill.STEALTH, ProficiencyLevel.EXPERTISE, "CLASS:rogue");
        pc.addSkillProficiencyGrant(Skill.STEALTH, ProficiencyLevel.PROFICIENT, "FEAT:skilled");

        assertThat(CharacterInputMapper.toInput(pc).skillProficiencies())
                .containsOnly(entry(Skill.STEALTH, ProficiencyLevel.EXPERTISE));
    }


}