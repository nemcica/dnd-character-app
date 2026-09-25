package com.anem.character_app.race;

import com.anem.character_app.race.dto.RaceCreateRequest;
import com.anem.character_app.race.dto.RaceResponse;
import com.anem.character_app.race.dto.RaceUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RaceMapper {

    Race toEntity(RaceCreateRequest raceCreateRequest);

    RaceResponse toResponse(Race race);

    void updateEntityFromRequest(RaceUpdateRequest raceUpdateRequest, @MappingTarget Race existingRace);
}
