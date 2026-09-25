package com.anem.character_app.race.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RaceUpdateRequest(
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
