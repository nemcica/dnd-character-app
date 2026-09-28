package com.anem.character_app.feat.dto;

import com.anem.character_app.feat.FeatTag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FeatUpdateRequest(
        @NotBlank(message = "Feat name is required")
        String name,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Feat type is required")
        FeatTag featTag
) {}
