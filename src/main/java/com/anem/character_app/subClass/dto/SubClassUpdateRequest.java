package com.anem.character_app.subClass.dto;

import com.anem.character_app.baseClass.BaseClass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubClassUpdateRequest(
        @NotBlank(message = "Name is required.")
        String name,

        @NotBlank(message = "Description is required.")
        String description,

        @NotNull(message = "Base class is required.")
        BaseClass baseClass
) {
}
