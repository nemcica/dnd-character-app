package com.an.character_app.rules.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "weapon_def")
@Getter
@NoArgsConstructor
public class WeaponDef {

    @Id
    private String id;

    private String  name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private WeaponCategory weaponCategory;

    @Enumerated(EnumType.STRING)
    private MeleeOrRanged meleeOrRanged;

    private String damageDice;

    @Enumerated(EnumType.STRING)
    private DamageType damageType;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "weapon_property", joinColumns = @JoinColumn(name = "weapon_id"))
    @Column(name = "property")
    @Enumerated(EnumType.STRING)
    private Set<WeaponProperty> weaponProperties;

    private String versatileDamageDice;

    private String weaponRange;

    @Enumerated(EnumType.STRING)
    private WeaponMastery weaponMastery;

    private String source;
}
