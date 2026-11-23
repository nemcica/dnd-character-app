package com.an.character_app.character.controller;

import com.an.character_app.character.dto.CharacterSheetResponse;
import com.an.character_app.character.service.PlayerCharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
public class PlayerCharacterController {

    private final PlayerCharacterService playerCharacterService;

    @GetMapping("/{id}")
    public ResponseEntity<CharacterSheetResponse> getCharacterById(@PathVariable Long id) {
        CharacterSheetResponse characterSheet = playerCharacterService.getCharacterSheet(id);
        return ResponseEntity.ok(characterSheet);
    }
}
