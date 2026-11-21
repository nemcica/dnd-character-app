package com.an.character_app.rules.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "species_def")
@Getter
@NoArgsConstructor
public class SpeciesDef {

    @Id
    private String id;

    private String name;

    private int walkSpeed;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "species_def_sizes",
            joinColumns = @JoinColumn(name = "species_def_id"))
    @Column(name = "creature_size")
    @Enumerated(EnumType.STRING)
    private Set<CreatureSize> creatureSizes;

    private String creatureSizeDescription;

}
