package com.an.character_app.calc;

import lombok.Getter;

@Getter
public enum ArmorWarning {

    UNTRAINED_ARMOR("Untrained in the worn armor. You get disadvantage on STR and DEX tests and can't cast spells."),
    UNTRAINED_SHIELD("Untrained in worn shield. You get no shield AC bonuses."),
    STR_REQUIREMENT("Strength below armor's requirement. Your speed reduces by 10 feet."),
    STEALTH_DISADVANTAGE("Armor imposes stealth disadvantage");

    private final String warning;

    ArmorWarning(String warning) {
        this.warning = warning;
    }
}
