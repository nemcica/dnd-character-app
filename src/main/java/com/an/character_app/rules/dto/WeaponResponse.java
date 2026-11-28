package com.an.character_app.rules.dto;

import com.an.character_app.rules.model.*;

import java.util.Set;

public record WeaponResponse(
        String id,
        String name,
        WeaponCategory weaponCategory,
        MeleeOrRanged meleeOrRanged,
        String damageDice,
        DamageType damageType,
        Set<WeaponProperty> weaponProperties,
        String versatileDamageDice,
        String weaponRange,
        WeaponMastery weaponMastery,
        String source
) {
}
