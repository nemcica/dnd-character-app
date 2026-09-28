package com.anem.character_app.character;

import com.anem.character_app.background.Background;
import com.anem.character_app.baseClass.BaseClass;
import com.anem.character_app.character.misc.CharacterHp;
import com.anem.character_app.character.misc.CharacterSkill;
import com.anem.character_app.generic.AbilityScore;
import com.anem.character_app.race.Race;

import java.util.Map;
import java.util.Set;

public class Character {

    Long id;

    String name;

    Integer characterLevel;

    Map<BaseClass, Integer> classLevel;

    Race race;

    Background background;

    Map<AbilityScore, Integer> abilityScores;

    Map<AbilityScore, Boolean> savingThrowProficiencies;

    Set<CharacterSkill> skills;

    Integer proficiencyBonus;

    Integer armorClass;

    Integer initiative;

    Boolean heroicInspiration;

    CharacterHp characterHp;

    Map<Integer, Integer> totalHitDice;

    Map<Integer, Integer> currentHitDice;

    Map<String, Boolean> deathSaves;
}
