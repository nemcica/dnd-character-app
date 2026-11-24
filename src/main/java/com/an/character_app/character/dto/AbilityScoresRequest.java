package com.an.character_app.character.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record AbilityScoresRequest(
        @Min(3)
        @Max(18)
        int strength,

        @Min(3)
        @Max(18)
        int dexterity,

        @Min(3)
        @Max(18)
        int constitution,

        @Min(3)
        @Max(18)
        int intelligence,

        @Min(3)
        @Max(18)
        int wisdom,

        @Min(3)
        @Max(18)
        int charisma
) {
}
