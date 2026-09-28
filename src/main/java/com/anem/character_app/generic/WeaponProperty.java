package com.anem.character_app.generic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WeaponProperty {
    AMMUNITION("Ammunition", ""),
    FINESSE("Finesse", ""),
    HEAVY("Heavy", ""),
    LIGHT("Light", ""),
    LOADING("Loading", ""),
    RANGE("Range", ""),
    REACH("Reach", ""),
    THROWN("Thrown", ""),
    TWO_HANDED("Two-Handed", ""),
    VERSATILE("Versatile", "");

    private final String name;
    private final String description;
}
