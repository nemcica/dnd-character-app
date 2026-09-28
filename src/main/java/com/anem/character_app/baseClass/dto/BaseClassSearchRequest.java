package com.anem.character_app.baseClass.dto;

import com.anem.character_app.generic.*;

import java.util.Set;

public record BaseClassSearchRequest(
        String name,

        String description,

        Integer hitDie,

        Set<ArmorCategory> armorProficiency,

        Set<WeaponCategory> weaponProficiency,

        SpellcasterType spellcasterType,

        Set<Skill> availableSkillProficiencies,

        Integer skillProfSelection,

        Set<AbilityScore> savingThrowProficiency,

        String equipment
) {}
