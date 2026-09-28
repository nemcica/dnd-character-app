package com.anem.character_app.character.misc;

import com.anem.character_app.generic.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CharacterSkill {

    private Skill skill;

    private Character proficiency;

    private Integer modifier;
}
