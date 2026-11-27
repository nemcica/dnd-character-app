package com.an.character_app.character;

import com.an.character_app.character.model.AbilityScores;
import com.an.character_app.character.model.PlayerCharacter;
import com.an.character_app.rules.model.*;

import java.util.Set;

import static org.mockito.Mockito.*;

public class TestCharacters {

    public ClassDef fighterClass() {
        ClassDef c = mock(ClassDef.class);
        when(c.getHitDie()).thenReturn(10);
        when(c.getName()).thenReturn("Fighter");
        when(c.getSavingThrows()).thenReturn(Set.of(Ability.STR, Ability.CON));
        return c;
    }

    public SpeciesDef species() {
        SpeciesDef s = mock(SpeciesDef.class);
        when(s.getName()).thenReturn("Human");
        return s;
    }

    public BackgroundDef background(Skill... skills) {
        BackgroundDef b = mock(BackgroundDef.class);
        lenient().when(b.getSkills()).thenReturn(Set.of(skills));
        return b;
    }

    public PlayerCharacter baseCharacter() {
        PlayerCharacter pc = new PlayerCharacter();
        pc.setLevel(5);
        pc.setBaseAbilityScores(new AbilityScores(15, 14, 13, 12, 10, 8));
        pc.setCharacterClass(fighterClass());
        pc.setBackground(background());
        pc.setSpecies(species());
        return pc;
    }
}
