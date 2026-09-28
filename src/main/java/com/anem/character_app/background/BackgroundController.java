package com.anem.character_app.background;

import com.anem.character_app.background.dto.BackgroundCreateRequest;
import com.anem.character_app.background.dto.BackgroundResponse;
import com.anem.character_app.background.dto.BackgroundSearchRequest;
import com.anem.character_app.background.dto.BackgroundUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/backgrounds")
public class BackgroundController {

    private final BackgroundService backgroundService;

    @PostMapping
    public ResponseEntity<BackgroundResponse> createBackground(@Valid @RequestBody BackgroundCreateRequest backgroundCreateRequest) {
        BackgroundResponse savedBackground = backgroundService.createBackground(backgroundCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBackground);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BackgroundResponse> updateBackground(@PathVariable String id, @Valid @RequestBody BackgroundUpdateRequest backgroundUpdateRequest) {
        BackgroundResponse updatedBackground = backgroundService.updateBackground(id, backgroundUpdateRequest);

        return ResponseEntity.ok(updatedBackground);
    }

    @GetMapping
    public ResponseEntity<Page<BackgroundResponse>> searchBackgrounds(
            @RequestBody BackgroundSearchRequest backgroundSearchRequest,
            Pageable pageable) {
        Page<BackgroundResponse> backgrounds = backgroundService.searchBackgrounds(backgroundSearchRequest, pageable);
        return ResponseEntity.ok(backgrounds);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BackgroundResponse> getBackgroundById(@PathVariable String id) {
        return backgroundService.getBackgroundById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBackground(@PathVariable String id) {
        backgroundService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
