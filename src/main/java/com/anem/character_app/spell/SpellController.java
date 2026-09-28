package com.anem.character_app.spell;

import com.anem.character_app.spell.dto.SpellCreateRequest;
import com.anem.character_app.spell.dto.SpellResponse;
import com.anem.character_app.spell.dto.SpellSearchRequest;
import com.anem.character_app.spell.dto.SpellUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/spells")
public class SpellController {

    SpellService spellService;

    @PostMapping
    public ResponseEntity<SpellResponse> createSpell(@Valid @RequestBody SpellCreateRequest spellCreateRequest) {
        SpellResponse savedSpell = spellService.createSpell(spellCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSpell);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpellResponse> updateSpell(@PathVariable String id, @Valid @RequestBody SpellUpdateRequest spellUpdateRequest) {
        SpellResponse updatedSpell = spellService.updateSpell(id, spellUpdateRequest);
        return ResponseEntity.ok(updatedSpell);
    }

    @GetMapping
    public ResponseEntity<Page<SpellResponse>> searchSpells(@RequestBody SpellSearchRequest spellSearchRequest, Pageable pageable) {
        Page<SpellResponse> spells = spellService.searchSpells(spellSearchRequest, pageable);
        return ResponseEntity.ok(spells);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpellResponse> getSpellById(@PathVariable String id) {
        return spellService.getSpellById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpell(@PathVariable String id) {
        spellService.deleteSpell(id);
        return ResponseEntity.noContent().build();
    }
}
