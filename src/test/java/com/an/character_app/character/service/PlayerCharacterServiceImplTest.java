package com.an.character_app.character.service;

import com.an.character_app.character.TestCharacters;
import com.an.character_app.character.dto.CharacterSheetResponse;
import com.an.character_app.character.model.AbilityScores;
import com.an.character_app.character.model.PlayerCharacter;
import com.an.character_app.character.repository.PlayerCharacterRepository;
import com.an.character_app.rules.model.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerCharacterServiceImplTest {

    @Mock
    PlayerCharacterRepository repository;
    @InjectMocks
    PlayerCharacterServiceImpl service;

    TestCharacters testCharacters = new TestCharacters();

    @Test
    void returnsSheetWithCalculatedStats() {
        PlayerCharacter pc = testCharacters.baseCharacter();
        when(repository.findById(1L)).thenReturn(Optional.of(pc));

        CharacterSheetResponse sheet = service.getCharacterSheet(1L);

        assertThat(sheet.className()).isEqualTo("Fighter");
        assertThat(sheet.speciesName()).isEqualTo("Human");
        assertThat(sheet.calculatedStats().proficiencyBonus()).isEqualTo(3);
    }

    @Test
    void throwsWhenCharacterDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCharacterSheet(99L)).isInstanceOf(EntityNotFoundException.class);
    }
}