package com.an.character_app.rules.dto;

import com.an.character_app.rules.model.Ability;

import java.util.Set;

public record ClassResponse(
        String id,
        String name,
        int hitDie,
        Set<Ability> savingThrows
) {
}
