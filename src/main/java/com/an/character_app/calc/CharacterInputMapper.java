package com.an.character_app.calc;

import com.an.character_app.character.model.AbilityScoreModifier;
import com.an.character_app.character.model.ArmorTrainingGrant;
import com.an.character_app.character.model.PlayerCharacter;
import com.an.character_app.character.model.SkillProficiencyGrant;
import com.an.character_app.rules.model.*;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        for (SkillProficiencyGrant sp : playerCharacter.getSkillProficiencyGrants()) {
            skillProficiencies.merge(sp.getSkill(), sp.getProficiencyLevel(), (a, b) -> a.compareTo(b) >= 0 ? a : b);
        }

        Set<Ability> savingThrowProficiencies = EnumSet.noneOf(Ability.class);
        savingThrowProficiencies.addAll(playerCharacter.getCharacterClass().getSavingThrows());

        ArmorDef armor = playerCharacter.getArmor();
        ArmorDef shield = playerCharacter.getShield();

        ArmorFacts armorFacts = new ArmorFacts(
                armor == null ? null : armor.getCategory(),
                armor == null ? null : armor.getBaseAc(),
                armor == null ? null : armor.getDexCap(),
                armor == null ? null : armor.getMinStrRequired(),
                armor != null && armor.isStealthDisadvantage(),
                shield != null,
                shield == null ? 0 : shield.getBaseAc());

        Set<ArmorCategory> armorTraining = playerCharacter.getArmorTrainingGrants().stream()
                .map(ArmorTrainingGrant::getArmorCategory)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(ArmorCategory.class)));

        return new CharacterInput(
                playerCharacter.getLevel(),
                playerCharacter.getCharacterClass().getHitDie(),
                baseScores,
                bonusScores,
                skillProficiencies,
                savingThrowProficiencies,
                armorFacts,
                armorTraining
        );
    }

}