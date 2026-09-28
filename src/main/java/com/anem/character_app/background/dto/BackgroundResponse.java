package com.anem.character_app.background.dto;

import com.anem.character_app.feat.Feat;
import com.anem.character_app.feat.FeatTag;
import com.anem.character_app.generic.AbilityScore;
import com.anem.character_app.generic.Skill;

import java.util.Set;

public record BackgroundResponse(
        String id,
        String name,
        String description,
        Set<AbilityScore> abilityScores,
        Feat feat,
        FeatTag featTag,
        Set<Skill> skillProficiencies,
        String toolProficiencies,
        String equipment
) {}
