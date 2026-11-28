package com.an.character_app.character.model;

import com.an.character_app.rules.model.ArmorCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "armor_training_grant")
@Getter
@NoArgsConstructor
public class ArmorTrainingGrant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private PlayerCharacter playerCharacter;

    @Enumerated(EnumType.STRING)
    private ArmorCategory armorCategory;

    @Column(nullable = false)
    private String source;

    public ArmorTrainingGrant(PlayerCharacter playerCharacter, ArmorCategory armorCategory, String source) {
        this.playerCharacter = playerCharacter;
        this.armorCategory = armorCategory;
        this.source = source;
    }
}
