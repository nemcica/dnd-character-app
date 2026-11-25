package com.an.character_app.rules.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "armor_def")
@Getter
@NoArgsConstructor
public class ArmorDef {
    @Id
    private String id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ArmorCategory category;

    private int baseAc;

    private Integer dexCap;

    private Integer minStrRequired;

    private boolean stealthDisadvantage;

    private String source;
}
