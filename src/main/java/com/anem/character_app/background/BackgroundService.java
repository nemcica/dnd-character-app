package com.anem.character_app.background;

import com.anem.character_app.background.dto.BackgroundCreateRequest;
import com.anem.character_app.background.dto.BackgroundResponse;
import com.anem.character_app.background.dto.BackgroundSearchRequest;
import com.anem.character_app.background.dto.BackgroundUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BackgroundService {

    BackgroundResponse createBackground(BackgroundCreateRequest request);

    BackgroundResponse updateBackground(String id, BackgroundUpdateRequest request);

    Optional<BackgroundResponse> getBackgroundById(String id);

    boolean exists(String id);

    void delete(String id);

    Page<BackgroundResponse> searchBackgrounds(BackgroundSearchRequest backgroundSearchRequest, Pageable pageable);
}
