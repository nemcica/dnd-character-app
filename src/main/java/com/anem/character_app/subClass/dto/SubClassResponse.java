package com.anem.character_app.subClass.dto;

import com.anem.character_app.baseClass.BaseClass;

public record SubClassResponse(
        String id,
        String name,
        String description,
        BaseClass baseClass
) {
}
