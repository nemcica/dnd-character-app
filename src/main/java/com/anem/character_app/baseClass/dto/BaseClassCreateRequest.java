package com.anem.character_app.baseClass.dto;

import com.anem.character_app.generic.*;
import jakarta.validation.constraints.*;

import java.util.Set;

public record BaseClassCreateRequest(
        @NotBlank
        @Pattern(
                regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "ID must be kebab-case"
        )
        String id,

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
