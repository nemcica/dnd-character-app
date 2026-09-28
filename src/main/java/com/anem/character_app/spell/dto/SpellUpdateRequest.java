package com.anem.character_app.spell.dto;

import com.anem.character_app.generic.SpellSchool;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SpellUpdateRequest(
        @NotBlank(message = "Name is required.")
        String name,

        @NotNull(message = "School of magic is required.")
        SpellSchool spellSchool,

        @Positive
        Integer spellLevel,

        @NotBlank(message = "Casting time is required.")
        String castingTime,

        @NotBlank(message = "Duration is required.")
        String duration,

        @NotBlank(message = "Components are required.")
        String components,

        @NotBlank(message = "Range is required.")
        String range,

        @NotBlank(message = "Description is required.")
        String description
        ) {
}
