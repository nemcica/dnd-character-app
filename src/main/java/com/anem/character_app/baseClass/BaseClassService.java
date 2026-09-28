package com.anem.character_app.baseClass;

import com.anem.character_app.baseClass.dto.BaseClassCreateRequest;
import com.anem.character_app.baseClass.dto.BaseClassResponse;
import com.anem.character_app.baseClass.dto.BaseClassSearchRequest;
import com.anem.character_app.baseClass.dto.BaseClassUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BaseClassService {
    BaseClassResponse createBaseClass(BaseClassCreateRequest request);

    BaseClassResponse updateBaseClass(String id, BaseClassUpdateRequest request);

    Optional<BaseClassResponse> getBaseClassById(String id);

    boolean exists(String id);

    void delete(String id);

    Page<BaseClassResponse> searchBaseClasses(BaseClassSearchRequest baseClassSearchRequest, Pageable pageable);
}
