package com.anem.character_app.feat;

import com.anem.character_app.feat.dto.FeatCreateRequest;
import com.anem.character_app.feat.dto.FeatResponse;
import com.anem.character_app.feat.dto.FeatUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feats")
public class FeatController {

    private final FeatService featService;

    @PostMapping
    public ResponseEntity<FeatResponse> createFeat(@Valid @RequestBody FeatCreateRequest featCreateRequest) {
        FeatResponse savedFeat = featService.createFeat(featCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFeat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeatResponse> updateFeat(@PathVariable String id, @Valid @RequestBody FeatUpdateRequest featUpdateRequest) {
        FeatResponse updatedFeat = featService.updateFeat(id, featUpdateRequest);
        return ResponseEntity.ok(updatedFeat);
    }

    @GetMapping
    public ResponseEntity<Page<FeatResponse>> searchFeats(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) FeatTag featTag,
            Pageable pageable) {

        Page<FeatResponse> feats = featService.searchFeats(name, featTag, pageable);
        return ResponseEntity.ok(feats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeatResponse> getFeatById(@PathVariable String id) {
        return featService.getFeatById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        featService.delete(id);
        return ResponseEntity.noContent().build();
    }
}