package com.anem.character_app.subClass;

import com.anem.character_app.subClass.dto.SubClassCreateRequest;
import com.anem.character_app.subClass.dto.SubClassResponse;
import com.anem.character_app.subClass.dto.SubClassSearchRequest;
import com.anem.character_app.subClass.dto.SubClassUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SubClassService {

    SubClassResponse createSubClass(SubClassCreateRequest request);

    SubClassResponse updateSubClass(String id, SubClassUpdateRequest request);

    Optional<SubClassResponse> getSubClassById(String id);

    void delete(String id);

    Page<SubClassResponse> searchSubClasses(SubClassSearchRequest request, Pageable pageable);
}
