package com.anem.character_app.baseClass.dto;

import com.anem.character_app.generic.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record BaseClassUpdateRequest(
        @NotBlank(message = "Name is required.")
        String name,

        String description,

        @Positive(message = "Hit Die is required.")
        Integer hitDie,

        @NotEmpty
        Set<@NotNull ArmorCategory> armorProficiency,

        @NotEmpty
        Set<@NotNull WeaponCategory> weaponProficiency,

        @NotNull
        SpellcasterType spellcasterType,

        @NotEmpty
        Set<@NotNull Skill> availableSkillProficiencies,

        @Positive
        Integer skillProfSelection,

        @NotEmpty
        Set<@NotNull AbilityScore> savingThrowProficiency,

        String equipment
) {}
