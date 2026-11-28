package com.an.character_app.calc;

import com.an.character_app.rules.model.ArmorCategory;

public record ArmorFacts(
        ArmorCategory category,
        Integer baseAc,
        Integer dexCap,
        Integer minStrRequired,
        boolean stealthDisadvantage,
        boolean shieldEquipped,
        int shieldBonus
) {
    public static ArmorFacts none() {
        return new ArmorFacts(null, null, null, null, false, false, 0);
    }
}
