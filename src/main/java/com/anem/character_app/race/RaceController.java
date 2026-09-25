package com.anem.character_app.race;

import com.anem.character_app.race.dto.RaceCreateRequest;
import com.anem.character_app.race.dto.RaceResponse;
import com.anem.character_app.race.dto.RaceUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/races")
public class RaceController {

    private final RaceService raceService;

    @PostMapping
    public ResponseEntity<RaceResponse> createRace(@Valid @RequestBody RaceCreateRequest raceCreateRequest) {
        RaceResponse savedRace = raceService.createRace(raceCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRace);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RaceResponse> updateRace(@PathVariable String id, @Valid RaceUpdateRequest raceUpdateRequest) {
        RaceResponse updatedRace = raceService.updateRace(id, raceUpdateRequest);
        return ResponseEntity.ok(updatedRace);
    }

    @GetMapping
    public ResponseEntity<Page<RaceResponse>> searchRaces(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String speed,
            @RequestParam(required = false) String specialTraits,
            Pageable pageable) {

        Page<RaceResponse> races = raceService.searchRaces(name, size, speed, specialTraits, pageable);
        return ResponseEntity.ok(races);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RaceResponse> getRaceById(@PathVariable String id) {
        return raceService.getRaceById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRace(@PathVariable String id) {
        raceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
