package com.anem.character_app.race;

import com.anem.character_app.race.dto.RaceCreateRequest;
import com.anem.character_app.race.dto.RaceResponse;
import com.anem.character_app.race.dto.RaceUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RaceService {
    RaceResponse createRace(RaceCreateRequest request);

    RaceResponse updateRace(String id, RaceUpdateRequest request);

    Optional<RaceResponse> getRaceById(String id);

    boolean exists(String id);

    void delete(String id);

    Page<RaceResponse> searchRaces(String name, String size, String speed, String specialTraits, Pageable pageable);
}
