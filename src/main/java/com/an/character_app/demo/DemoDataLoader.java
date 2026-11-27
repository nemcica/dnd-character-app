package com.an.character_app.demo;

import com.an.character_app.character.dto.AbilityScoresRequest;
import com.an.character_app.character.dto.CreateCharacterRequest;
import com.an.character_app.character.repository.PlayerCharacterRepository;
import com.an.character_app.character.service.PlayerCharacterService;
import com.an.character_app.exception.InvalidCharacterException;
import com.an.character_app.rules.model.Ability;
import com.an.character_app.rules.model.Skill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@ConditionalOnProperty(name = "app.demo-data.enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class DemoDataLoader implements ApplicationRunner {

    private final PlayerCharacterRepository characterRepository;
    private final PlayerCharacterService characterService;

    @Override
    @NullMarked
    public void run(ApplicationArguments args) {
        if (characterRepository.count() > 0) {
            return;
        }
        try {
            characterService.createCharacter(demoFighter());
            characterService.createCharacter(demoWizard());
            log.info("Created demo characters");
        } catch (InvalidCharacterException e) {
            log.error("Could not create demo data: {}", e.getMessage());
        }
    }

    private CreateCharacterRequest demoFighter() {
        return new CreateCharacterRequest(
                "Demo Fighter", 5,
                new AbilityScoresRequest(15, 14, 13, 12, 10, 8),
                "fighter", "human", "soldier",
                Map.of(Ability.STR, 2, Ability.CON, 1),
                "chain-mail", null,
                Set.of(Skill.PERCEPTION, Skill.ANIMAL_HANDLING));
    }

    private CreateCharacterRequest demoWizard() {
        return new CreateCharacterRequest(
                "Demo Wizard", 3,
                new AbilityScoresRequest(8, 14, 13, 15, 12, 10),
                "wizard", "elf", "acolyte",
                Map.of(Ability.INT, 2, Ability.WIS, 1),
                null, null,
                Set.of(Skill.ARCANA, Skill.INVESTIGATION));
    }
}