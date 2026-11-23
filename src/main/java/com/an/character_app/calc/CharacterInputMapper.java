package com.an.character_app.calc;

import com.an.character_app.character.model.AbilityScoreModifier;
import com.an.character_app.character.model.PlayerCharacter;
import com.an.character_app.character.model.SkillProficiencyGrant;
import com.an.character_app.rules.model.Ability;
import com.an.character_app.rules.model.ArmorDef;
import com.an.character_app.rules.model.ProficiencyLevel;
import com.an.character_app.rules.model.Skill;

import java.util.*;

public class CharacterInputMapper {

    public static CharacterInput toInput(PlayerCharacter playerCharacter) {
        Map<Ability, Integer> baseScores = new EnumMap<>(Ability.class);
        for (Ability a : Ability.values()) {
            baseScores.put(a, playerCharacter.getBaseAbilityScores().getAbility(a));
        }

        Map<Ability, Integer> bonusScores = new EnumMap<>(Ability.class);
        for (Ability a : Ability.values()) {
            bonusScores.put(a, 0);
        }
        for (AbilityScoreModifier asm : playerCharacter.getAbilityScoreModifiers()) {
            bonusScores.merge(asm.getAbility(), asm.getAmount(), Integer::sum);
        }

        Map<Skill, ProficiencyLevel> skillProficiencies = new EnumMap<>(Skill.class);
        for (Skill s : playerCharacter.getBackground().getSkills()) {
            skillProficiencies.put(s, ProficiencyLevel.PROFICIENT);
        }
        for (SkillProficiencyGrant g : playerCharacter.getSkillProficiencyGrants()) {
            skillProficiencies.merge(g.getSkill(), g.getProficiencyLevel(), (a, b) -> a.compareTo(b) >= 0 ? a : b);
        }

        Set<Ability> savingThrowProficiencies = EnumSet.noneOf(Ability.class);
        savingThrowProficiencies.addAll(playerCharacter.getCharacterClass().getSavingThrows());

        ArmorDef armor = playerCharacter.getArmor();
        ArmorDef shield = playerCharacter.getShield();

        return new CharacterInput(
                playerCharacter.getLevel(),
                playerCharacter.getCharacterClass().getHitDie(),
                baseScores,
                bonusScores,
                skillProficiencies,
                savingThrowProficiencies,
                armor == null ? null : armor.getCategory(),
                armor == null ? null : armor.getBaseAc(),
                armor == null ? null : armor.getDexCap(),
                shield == null ? 0 : shield.getBaseAc()
        );
    }

}