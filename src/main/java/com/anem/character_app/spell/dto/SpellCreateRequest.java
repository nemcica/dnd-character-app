package com.anem.character_app.spell.dto;

import com.anem.character_app.generic.SpellSchool;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record SpellCreateRequest(
        @NotBlank
        @Pattern(
                regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "ID must be kebab-case (e.g., 'haunted-one')")
        String id,

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
