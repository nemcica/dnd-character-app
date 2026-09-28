package com.anem.character_app.baseClass;

import com.anem.character_app.baseClass.dto.BaseClassCreateRequest;
import com.anem.character_app.baseClass.dto.BaseClassResponse;
import com.anem.character_app.baseClass.dto.BaseClassSearchRequest;
import com.anem.character_app.baseClass.dto.BaseClassUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/classes")
public class BaseClassController {

    BaseClassService baseClassService;

    @PostMapping
    public ResponseEntity<BaseClassResponse> createBaseClass(@Valid @RequestBody BaseClassCreateRequest baseClassCreateRequest) {
        BaseClassResponse savedBaseClass = baseClassService.createBaseClass(baseClassCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedBaseClass);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseClassResponse> updateClass(@PathVariable String id, @Valid @RequestBody BaseClassUpdateRequest baseClassUpdateRequest) {
        BaseClassResponse updatedBaseClass = baseClassService.updateBaseClass(id, baseClassUpdateRequest);

        return ResponseEntity.ok(updatedBaseClass);
    }

    @GetMapping
    public ResponseEntity<Page<BaseClassResponse>> searchClasses(@RequestBody BaseClassSearchRequest baseClassSearchRequest, Pageable pageable) {
        Page<BaseClassResponse> classes = baseClassService.searchBaseClasses(baseClassSearchRequest, pageable);

        return ResponseEntity.ok(classes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseClassResponse> getClassById(@PathVariable String id) {
        return baseClassService.getBaseClassById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable String id) {
        baseClassService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
