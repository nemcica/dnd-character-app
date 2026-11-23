package com.an.character_app.character.service;

import com.an.character_app.character.dto.CharacterSheetResponse;

public interface PlayerCharacterService {
    CharacterSheetResponse getCharacterSheet(Long id);
}
