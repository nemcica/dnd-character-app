package com.an.character_app.character.controller;

import com.an.character_app.character.dto.CharacterSheetResponse;
import com.an.character_app.character.dto.CharacterSummary;
import com.an.character_app.character.dto.CreateCharacterRequest;
import com.an.character_app.character.service.PlayerCharacterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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

    @PostMapping
    public ResponseEntity<CharacterSheetResponse> createCharacter(@Valid @RequestBody CreateCharacterRequest request) {
        CharacterSheetResponse createdCharacter = playerCharacterService.createCharacter(request);
        URI location = URI.create("/characters/" + createdCharacter.id());
        return ResponseEntity.created(location).body(createdCharacter);
    }

    @GetMapping
    public PagedModel<CharacterSummary> getCharacters(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return new PagedModel<>(playerCharacterService.getCharacters(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id) {
        playerCharacterService.deleteCharacter(id);
        return ResponseEntity.noContent().build();
    }
}
