package com.anem.character_app.feat.dto;

import com.anem.character_app.feat.FeatTag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record FeatCreateRequest(
        @NotBlank(message = "Feat ID is required")
        @Pattern(
                regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "ID must be kebab-case (e.g., 'great-weapon-fighting')"
        )
        String id,

        @NotBlank(message = "Feat name is required")
        String name,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Feat type is required")
        FeatTag featTag
) {}
