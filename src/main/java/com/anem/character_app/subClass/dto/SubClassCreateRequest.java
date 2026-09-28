package com.anem.character_app.subClass.dto;

import com.anem.character_app.baseClass.BaseClass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SubClassCreateRequest(
        @NotBlank
        @Pattern(
                regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "ID must be kebab-case (e.g., 'light-domain')"
        )
        String id,

        @NotBlank(message = "Name is required.")
        String name,

        @NotBlank(message = "Description is required.")
        String description,

        @NotNull(message = "Base class is required.")
        BaseClass baseClass
) {
}
