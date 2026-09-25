package com.anem.character_app.feat.dto;

import com.anem.character_app.feat.FeatTag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FeatCreateRequest(
        @NotBlank(message = "Feat ID is required")
        @Pattern(
                regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "ID must be kebab-case (e.g., 'great-weapon-fighting')"
        )
        @Size(max = 80, message = "ID cannot exceed 80 characters")
        String id,

        @NotBlank(message = "Feat name is required")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Feat type is required")
        FeatTag featTag
) {}
