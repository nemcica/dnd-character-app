package com.an.character_app.character.model;

import com.an.character_app.rules.model.ProficiencyLevel;
import com.an.character_app.rules.model.Skill;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "skill_proficiency_grant")
@Getter
@NoArgsConstructor
public class SkillProficiencyGrant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_character_id")
    private PlayerCharacter playerCharacter;

    @Enumerated(EnumType.STRING)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    private ProficiencyLevel proficiencyLevel;

    @Column(nullable = false)
    private String source;

    public SkillProficiencyGrant(PlayerCharacter playerCharacter, Skill skill, ProficiencyLevel proficiencyLevel, String source) {
        this.playerCharacter = playerCharacter;
        this.skill = skill;
        this.proficiencyLevel = proficiencyLevel;
        this.source = source;
    }
}
