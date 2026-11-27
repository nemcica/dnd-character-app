package com.an.character_app.character.dto;

import com.an.character_app.rules.model.Ability;
import com.an.character_app.rules.model.Skill;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.Map;
import java.util.Set;

public record CreateCharacterRequest(
        @NotBlank
        @Size(max = 100)
        String name,

        @Min(1)
        @Max(20)
        int level,

        @NotNull
        @Valid
        AbilityScoresRequest baseAbilityScores,

        @NotBlank
        String classId,

        @NotBlank
        String speciesId,

        @NotBlank
        String backgroundId,

        @NotEmpty
        Map<Ability, @NotNull @Min(1) @Max(2) Integer> backgroundAbilityBonuses,

        String armorId,

        String shieldId,

        @NotEmpty
        Set<@NotNull Skill> classSkills,

        Set<Skill> freeSkillChoices
) {
}
