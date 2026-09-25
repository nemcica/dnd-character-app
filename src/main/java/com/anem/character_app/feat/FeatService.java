package com.anem.character_app.feat;

import com.anem.character_app.feat.dto.FeatCreateRequest;
import com.anem.character_app.feat.dto.FeatResponse;
import com.anem.character_app.feat.dto.FeatUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FeatService {
    FeatResponse createFeat(FeatCreateRequest request);

    FeatResponse updateFeat(String id, FeatUpdateRequest request);

    Optional<FeatResponse> getFeatById(String id);

    boolean exists(String id);

    void delete(String id);

    Page<FeatResponse> searchFeats(String name, FeatTag featTag, Pageable pageable);
}
