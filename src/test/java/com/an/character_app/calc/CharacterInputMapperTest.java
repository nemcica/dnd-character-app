package com.an.character_app.calc;

import com.an.character_app.character.model.AbilityScores;
import com.an.character_app.character.model.PlayerCharacter;
import com.an.character_app.rules.model.Ability;
import com.an.character_app.rules.model.ArmorCategory;
import com.an.character_app.rules.model.ArmorDef;
import com.an.character_app.rules.model.ClassDef;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
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

    private PlayerCharacter baseCharacter() {
        PlayerCharacter pc = new PlayerCharacter();
        pc.setLevel(5);
        pc.setBaseAbilityScores(new AbilityScores(15, 14, 13, 12, 10, 8));
        pc.setCharacterClass(fighterClass());
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
}