package com.anem.character_app.background.dto;

import com.anem.character_app.feat.Feat;
import com.anem.character_app.feat.FeatTag;
import com.anem.character_app.generic.AbilityScore;
import com.anem.character_app.generic.Skill;
import jakarta.validation.constraints.*;

import java.util.Set;

public record BackgroundCreateRequest(
        @NotBlank
        @Pattern(
                regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "ID must be kebab-case (e.g., 'haunted-one')")
        String id,
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
