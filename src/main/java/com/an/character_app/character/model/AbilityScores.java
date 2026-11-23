package com.an.character_app.character.model;

import com.an.character_app.rules.model.Ability;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AbilityScores {
    private int strength;
    private int dexterity;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int charisma;

    public int getAbility(Ability a) {
        return switch (a) {
            case STR -> strength;
            case DEX -> dexterity;
            case CON -> constitution;
            case INT -> intelligence;
            case WIS -> wisdom;
            case CHA -> charisma;
        };
    }
}
