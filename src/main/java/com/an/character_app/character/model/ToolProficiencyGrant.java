package com.an.character_app.character.model;

import com.an.character_app.rules.model.Tool;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tool_proficiency_grant")
@Getter
@NoArgsConstructor
public class ToolProficiencyGrant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_character_id")
    private PlayerCharacter playerCharacter;

    @Enumerated(EnumType.STRING)
    private Tool tool;

    @Column(nullable = false)
    private String source;

    public ToolProficiencyGrant(PlayerCharacter playerCharacter, Tool tool, String source) {
        this.playerCharacter = playerCharacter;
        this.tool = tool;
        this.source = source;
    }
}
