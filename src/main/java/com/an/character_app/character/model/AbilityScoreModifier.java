package com.an.character_app.character.model;

import com.an.character_app.rules.model.Ability;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ability_score_modifier")
@Getter
@NoArgsConstructor
public class AbilityScoreModifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_character_id")
    private PlayerCharacter playerCharacter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ability ability;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String source;

    public AbilityScoreModifier(PlayerCharacter playerCharacter, Ability ability, int amount, String source) {
        this.playerCharacter =playerCharacter;
        this.ability = ability;
        this.amount = amount;
        this.source = source;
    }
}
