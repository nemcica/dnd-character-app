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

    @OneToMany(mappedBy = "playerCharacter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SkillProficiencyGrant> skillProficiencyGrants = new ArrayList<>();

    @OneToMany(mappedBy = "playerCharacter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ToolProficiencyGrant> toolProficiencyGrants = new ArrayList<>();

    @OneToMany(mappedBy = "playerCharacter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArmorTrainingGrant> armorTrainingGrants = new ArrayList<>();

    public void addAbilityScoreModifier(Ability ability, int amount, String source) {
        abilityScoreModifiers.add(new AbilityScoreModifier(this, ability, amount, source));
    }

    public void addSkillProficiencyGrant(Skill skill, ProficiencyLevel proficiencyLevel, String source) {
        skillProficiencyGrants.add(new SkillProficiencyGrant(this, skill, proficiencyLevel, source));
    }

    public void addToolProficiencyGrant(Tool tool, String source) {
        toolProficiencyGrants.add(new ToolProficiencyGrant(this, tool, source));
    }

    public void addArmorTrainingGrant(ArmorCategory armorCategory, String source) {
        armorTrainingGrants.add(new ArmorTrainingGrant(this, armorCategory, source));
    }
}