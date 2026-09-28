package com.anem.character_app.background;

import com.anem.character_app.feat.Feat;
import com.anem.character_app.feat.FeatTag;
import com.anem.character_app.generic.AbilityScore;
import com.anem.character_app.generic.Skill;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Background {
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<AbilityScore> abilityScores = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "feat_id")
    private Feat feat;

    @Enumerated(EnumType.STRING)
    private FeatTag featTag;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<Skill> skillProficiencies = new HashSet<>();

    private String toolProficiencies;

    private String equipment;

}
