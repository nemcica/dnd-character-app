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

}
