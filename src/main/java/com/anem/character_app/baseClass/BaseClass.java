package com.anem.character_app.baseClass;

import com.anem.character_app.generic.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseClass {
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Positive
    private Integer hitDie;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<ArmorCategory> armorProficiency;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<WeaponCategory> weaponProficiency;

    @Enumerated(EnumType.STRING)
    private SpellcasterType spellcasterType;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<Skill> availableSkillProficiencies;

    @Positive
    private Integer skillProfSelection;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<AbilityScore> savingThrowProficiency;

    private String equipment;

}
