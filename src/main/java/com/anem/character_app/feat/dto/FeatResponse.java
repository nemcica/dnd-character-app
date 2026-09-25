package com.anem.character_app.feat.dto;

import com.anem.character_app.feat.FeatTag;

public record FeatResponse(
        String id,
        String name,
        String description,
        FeatTag featTag
) {}
