package com.an.character_app.character.service;

import com.an.character_app.character.dto.CharacterSheetResponse;
import com.an.character_app.character.dto.CreateCharacterRequest;

public interface PlayerCharacterService {
    CharacterSheetResponse getCharacterSheet(Long id);

    CharacterSheetResponse createCharacter(CreateCharacterRequest request);
}
