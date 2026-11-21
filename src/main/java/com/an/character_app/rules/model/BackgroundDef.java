package com.an.character_app.rules.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "background_def")
@Getter
@NoArgsConstructor
public class BackgroundDef {
    @Id
    private String id;

    private String name;

    private String source;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "background_def_ability_options",
            joinColumns = @JoinColumn(name = "background_def_id"))
    @Column(name = "ability")
    @Enumerated(EnumType.STRING)
    private Set<Ability> abilityOptions;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "background_def_skills",
            joinColumns = @JoinColumn(name = "background_def_id"))
    @Column(name = "skill")
    @Enumerated(EnumType.STRING)
    private Set<Skill> skills;
}

