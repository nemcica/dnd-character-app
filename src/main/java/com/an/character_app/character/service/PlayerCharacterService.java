package com.an.character_app.character.service;

import com.an.character_app.character.dto.CharacterSheetResponse;
import com.an.character_app.character.dto.CharacterSummary;
import com.an.character_app.character.dto.CreateCharacterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlayerCharacterService {
    CharacterSheetResponse getCharacterSheet(Long id);

    CharacterSheetResponse createCharacter(CreateCharacterRequest request);

    Page<CharacterSummary> getCharacters(Pageable pageable);

    void deleteCharacter(Long id);
}
