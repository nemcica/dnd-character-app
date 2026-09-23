package com.anem.character_app.feat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FeatService {
    FeatDto createFeat(FeatDto featDto);

    FeatDto updateFeat(String id, FeatDto featDto);

    Page<FeatDto> getAllFeats(Pageable pageable);

    Optional<FeatDto> getFeatById(String id);

    boolean exists(String id);

    void delete(String id);
}
