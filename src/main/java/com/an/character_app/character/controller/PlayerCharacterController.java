package com.an.character_app.character.controller;

import com.an.character_app.character.dto.CharacterSheetResponse;
import com.an.character_app.character.dto.CharacterSummary;
import com.an.character_app.character.dto.CreateCharacterRequest;
import com.an.character_app.character.service.PlayerCharacterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Characters", description = "Create, view and delete characters")
@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
public class PlayerCharacterController {

    private final PlayerCharacterService playerCharacterService;

    @Operation(summary = "Get a character sheet with calculated stats")
    @GetMapping("/{id}")
    public ResponseEntity<CharacterSheetResponse> getCharacterById(@PathVariable Long id) {
        CharacterSheetResponse characterSheet = playerCharacterService.getCharacterSheet(id);
        return ResponseEntity.ok(characterSheet);
    }

    @Operation(summary = "Create a character")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = @ExampleObject(name = "Level 10 Rogue", value = """
                    {
                      "name": "Michael",
                      "level": 10,
                      "baseAbilityScores": {
                        "strength": 13, "dexterity": 15, "constitution": 14,
                        "intelligence": 12, "wisdom": 10, "charisma": 8
                      },
                      "classId": "rogue",
                      "speciesId": "dwarf",
                      "backgroundId": "criminal",
                      "backgroundAbilityBonuses": { "DEX": 2, "INT": 1 },
                      "armorId": "leather",
                      "classSkills": ["ATHLETICS"]
                    }
                    """)))
    @PostMapping
    public ResponseEntity<CharacterSheetResponse> createCharacter(@Valid @RequestBody CreateCharacterRequest request) {
        CharacterSheetResponse createdCharacter = playerCharacterService.createCharacter(request);
        URI location = URI.create("/characters/" + createdCharacter.id());
        return ResponseEntity.created(location).body(createdCharacter);
    }

    @Operation(summary = "List characters (paginated)")
    @GetMapping
    public PagedModel<CharacterSummary> getCharacters(@ParameterObject @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return new PagedModel<>(playerCharacterService.getCharacters(pageable));
    }

    @Operation(summary = "Delete a character.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id) {
        playerCharacterService.deleteCharacter(id);
        return ResponseEntity.noContent().build();
    }
}
