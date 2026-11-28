package com.an.character_app.rules.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "class_def")
@Getter
@NoArgsConstructor
public class ClassDef {

    @Id
    private String id;

    private String name;

    private int hitDie;

    private String source;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "class_def_saving_throws",
            joinColumns = @JoinColumn(name = "class_def_id"))
    @Column(name = "saving_throw")
    @Enumerated(EnumType.STRING)
    private Set<Ability> savingThrows;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "class_def_skill_options", joinColumns = @JoinColumn(name = "class_def_id"))
    @Column(name = "skill")
    @Enumerated(EnumType.STRING)
    private Set<Skill> skillOptions;

    private int skillChoiceCount;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "class_def_fixed_tool", joinColumns = @JoinColumn(name = "class_def_id"))
    @Column(name = "tool")
    @Enumerated(EnumType.STRING)
    private Set<Tool> fixedToolProficiencies;

    private int toolChoiceCount;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "class_def_tool_category", joinColumns = @JoinColumn(name = "class_def_id"))
    @Column(name = "tool_category")
    @Enumerated(EnumType.STRING)
    private Set<ToolCategory> toolChoiceCategories;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "class_def_armor_training", joinColumns = @JoinColumn(name = "class_def_id"))
    @Column(name = "armor_category")
    @Enumerated(EnumType.STRING)
    private Set<ArmorCategory> armorTraining;
}
