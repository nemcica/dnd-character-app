package com.an.character_app.rules.dto;

import com.an.character_app.rules.model.ArmorCategory;

public record ArmorResponse(
        String id,
        String name,
        ArmorCategory category,
        int baseAc,
        Integer dexCap,
        String source
) {
}
