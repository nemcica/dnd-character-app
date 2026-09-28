package com.anem.character_app.subClass;

import com.anem.character_app.subClass.dto.SubClassCreateRequest;
import com.anem.character_app.subClass.dto.SubClassResponse;
import com.anem.character_app.subClass.dto.SubClassSearchRequest;
import com.anem.character_app.subClass.dto.SubClassUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subclasses")
public class SubClassController {

    SubClassService subClassService;

    @PostMapping
    public ResponseEntity<SubClassResponse> createSubClass(@Valid @RequestBody SubClassCreateRequest subClassCreateRequest) {
        SubClassResponse savedSubClass = subClassService.createSubClass(subClassCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSubClass);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubClassResponse> updateSubClass(@PathVariable String id, @Valid @RequestBody SubClassUpdateRequest subClassUpdateRequest) {
        SubClassResponse updatedSubClass = subClassService.updateSubClass(id, subClassUpdateRequest);
        return ResponseEntity.ok(updatedSubClass);
    }

    @GetMapping
    public ResponseEntity<Page<SubClassResponse>> searchSubClasses(@RequestBody SubClassSearchRequest subClassSearchRequest, Pageable pageable) {
        Page<SubClassResponse> subclasses = subClassService.searchSubClasses(subClassSearchRequest, pageable);
        return ResponseEntity.ok(subclasses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubClassResponse> getSubClassById(@PathVariable String id) {
        return subClassService.getSubClassById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubClass(@PathVariable String id) {
        subClassService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
