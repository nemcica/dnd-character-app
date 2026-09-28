package com.anem.character_app.spell;

import com.anem.character_app.generic.SpellSchool;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Spell {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    private SpellSchool spellSchool;

    private Integer spellLevel;

    private String castingTime;

    private String duration;

    private String components;

    private String range;

    @Column(columnDefinition = "TEXT")
    private String description;
}
