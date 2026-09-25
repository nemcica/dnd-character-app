package com.anem.character_app.race.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RaceCreateRequest(
        @NotBlank(message = "Race ID is required")
        @Pattern(
                regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "ID must be kebab-case (e.g., 'high-elf')"
        )
        @Size(max = 80, message = "ID cannot exceed 80 characters")
        String id,

        @NotBlank(message = "Race name is required")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name,

        @NotBlank(message = "Creature type is required.")
        String creatureType,

        @NotBlank(message = "Size is required.")
        String size,

        @NotBlank(message = "Speed is required.")
        String speed,

        @NotBlank(message = "Special traits is required.")
        String specialTraits
) {}
