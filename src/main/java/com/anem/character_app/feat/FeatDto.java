package com.anem.character_app.feat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FeatDto(
        @NotBlank String id,
        @NotBlank String name,
        @NotBlank String description,
        @NotNull FeatTag featTag
) {}
