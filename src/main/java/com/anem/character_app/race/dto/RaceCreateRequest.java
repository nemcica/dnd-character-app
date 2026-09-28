package com.anem.character_app.race.dto;

import com.anem.character_app.generic.CreatureSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RaceCreateRequest(
        @NotBlank(message = "Race ID is required")
        @Pattern(
                regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "ID must be kebab-case (e.g., 'high-elf')"
        )
        String id,

        @NotBlank(message = "Race name is required")
        String name,

        @NotBlank(message = "Creature type is required.")
        String creatureType,

        @NotNull(message = "Size is required.")
        CreatureSize size,

        @NotBlank(message = "Speed is required.")
        String speed,

        @NotBlank(message = "Special traits is required.")
        String specialTraits
) {}
