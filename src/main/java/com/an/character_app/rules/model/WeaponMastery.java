package com.an.character_app.rules.model;

import lombok.Getter;

@Getter
public enum WeaponMastery {

    CLEAVE("Once per turn on a melee hit, make an extra attack against another creature within 5 feet of the target and within your reach (no positive ability modifier added to damage)."),
    GRAZE("On a miss, deal damage equal to the ability modifier used for the attack roll (same damage type as the weapon)."),
    NICK("Make the extra attack of the Light property as part of the Attack action instead of a Bonus Action (once per turn)."),
    PUSH("On a hit, push a Large or smaller target up to 10 feet straight away from you."),
    SAP("On a hit, the target has Disadvantage on its next attack roll before the start of your next turn."),
    SLOW("On a hit that deals damage, reduce the target's Speed by 10 feet until the start of your next turn (does not stack)."),
    TOPPLE("On a hit, force a Constitution saving throw (DC 8 + attack ability modifier + Proficiency Bonus) or knock the target Prone."),
    VEX("On a hit that deals damage, gain Advantage on your next attack roll against that target before the end of your next turn.");

    private final String description;

    WeaponMastery(String description) {
        this.description = description;
    }
}
