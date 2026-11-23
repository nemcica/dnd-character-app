package com.an.character_app.character.model;

import com.an.character_app.rules.model.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "player_character")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int level;

    @Embedded
    private AbilityScores baseAbilityScores;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "class_id")
    private ClassDef characterClass;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private SpeciesDef species;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private BackgroundDef background;

    @ManyToOne(fetch = FetchType.LAZY)
    private ArmorDef armor;

    @ManyToOne(fetch = FetchType.LAZY)
    private ArmorDef shield;

    @OneToMany(mappedBy = "playerCharacter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AbilityScoreModifier> abilityScoreModifiers = new ArrayList<>();

    public void addAbilityScoreModifier(Ability ability, int amount, String source) {
        abilityScoreModifiers.add(new AbilityScoreModifier(this, ability, amount, source));
    }
}
