package com.anem.character_app.race;

import com.anem.character_app.race.dto.RaceCreateRequest;
import com.anem.character_app.race.dto.RaceResponse;
import com.anem.character_app.race.dto.RaceUpdateRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RaceServiceImpl implements RaceService {

    private final RaceMapper raceMapper;
    private final RaceRepository raceRepository;

    @Override
    @Transactional
    public RaceResponse createRace(RaceCreateRequest request) {
        if (raceRepository.existsById(request.id())) {
            throw new EntityExistsException("Id: " + request.id() + " already exists.");
        }

        Race race = raceMapper.toEntity(request);
        Race savedRace = raceRepository.save(race);

        return raceMapper.toResponse(savedRace);
    }

    @Override
    @Transactional
    public RaceResponse updateRace(String id, RaceUpdateRequest request) {
        Race existingRace = raceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id: " + id + " does not exist."));

        raceMapper.updateEntityFromRequest(request, existingRace);

        Race updatedRace = raceRepository.save(existingRace);

        return raceMapper.toResponse(updatedRace);
    }

    @Override
    public Optional<RaceResponse> getRaceById(String id) {
        return raceRepository.findById(id).map(raceMapper::toResponse);
    }

    @Override
    public boolean exists(String id) {
        return raceRepository.existsById(id);
    }

    @Override
    @Transactional
    public void delete(String id) {
        if(!raceRepository.existsById(id)) {
            throw new EntityNotFoundException("Id: " + id + " does not exist.");
        }
        raceRepository.deleteById(id);
    }

    @Override
    public Page<RaceResponse> searchRaces(String name, String size, String speed, String specialTraits, Pageable pageable) {
        Specification<Race> specification = RaceSpecifications.withFilters(name, size, speed, specialTraits);

        return raceRepository.findAll(specification, pageable).map(raceMapper::toResponse);
    }
}
