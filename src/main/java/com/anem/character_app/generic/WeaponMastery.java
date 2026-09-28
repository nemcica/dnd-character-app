package com.anem.character_app.generic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WeaponMastery {
    CLEAVE("Cleave", ""),
    GRAZE("Graze", ""),
    NICK("Nick", ""),
    PUSH("Push", ""),
    SAP("Sap", ""),
    SLOW("Slow", ""),
    TOPPLE("Topple", ""),
    VEX("Vex", "");

    private final String name;
    private final String description;
}
