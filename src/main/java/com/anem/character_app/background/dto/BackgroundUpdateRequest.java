package com.anem.character_app.background.dto;

import com.anem.character_app.feat.Feat;
import com.anem.character_app.feat.FeatTag;
import com.anem.character_app.generic.AbilityScore;
import com.anem.character_app.generic.Skill;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record BackgroundUpdateRequest(
        @NotBlank(message = "Name is required.")
        String name,
        @NotBlank(message = "Description is required.")
        String description,
        @NotEmpty
        Set<@NotNull AbilityScore> abilityScores,
        Feat feat,
        FeatTag featTag,
        @NotEmpty
        Set<@NotNull Skill> skillProficiencies,
        @NotBlank(message = "Tool proficiency is required.")
        String toolProficiencies,
        @NotBlank(message = "Starting equipment is required.")
        String equipment
) {}
