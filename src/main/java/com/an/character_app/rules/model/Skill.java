package com.an.character_app.rules.model;

public enum Skill {

    ATHLETICS(Ability.STR),
    ACROBATICS(Ability.DEX),
    SLEIGHT_OF_HAND(Ability.DEX),
    STEALTH(Ability.DEX),
    ARCANA(Ability.INT),
    HISTORY(Ability.INT),
    INVESTIGATION(Ability.INT),
    NATURE(Ability.INT),
    RELIGION(Ability.INT),
    ANIMAL_HANDLING(Ability.WIS),
    INSIGHT(Ability.WIS),
    MEDICINE(Ability.WIS),
    PERCEPTION(Ability.WIS),
    SURVIVAL(Ability.WIS),
    DECEPTION(Ability.CHA),
    INTIMIDATION(Ability.CHA),
    PERFORMANCE(Ability.CHA),
    PERSUASION(Ability.CHA);

    private final Ability ability;
    Skill(Ability ability) {
        this.ability = ability;
    }
    public Ability getAbility() {
        return ability;
    }
}

