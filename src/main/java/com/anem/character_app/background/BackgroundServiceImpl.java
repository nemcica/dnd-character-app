package com.anem.character_app.background;

import com.anem.character_app.background.dto.BackgroundCreateRequest;
import com.anem.character_app.background.dto.BackgroundResponse;
import com.anem.character_app.background.dto.BackgroundSearchRequest;
import com.anem.character_app.background.dto.BackgroundUpdateRequest;
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
public class BackgroundServiceImpl implements BackgroundService {

    private final BackgroundRepository backgroundRepository;
    private final BackgroundMapper backgroundMapper;

    @Override
    @Transactional
    public BackgroundResponse createBackground(BackgroundCreateRequest request) {
        if (backgroundRepository.existsById(request.id())) {
            throw new EntityExistsException("Id: " + request.id() + " already exists.");
        }

        Background background = backgroundMapper.toEntity(request);
        Background savedBackground = backgroundRepository.save(background);
        return backgroundMapper.toResponse(savedBackground);
    }

    @Override
    @Transactional
    public BackgroundResponse updateBackground(String id, BackgroundUpdateRequest request) {
        Background existingBackground = backgroundRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Id: " + id + " does not exist."));

        backgroundMapper.updateEntityFromRequest(request, existingBackground);

        Background updatedBackground = backgroundRepository.save(existingBackground);

        return backgroundMapper.toResponse(updatedBackground);
    }

    @Override
    public Optional<BackgroundResponse> getBackgroundById(String id) {
        return backgroundRepository.findById(id).map(backgroundMapper::toResponse);
    }

    @Override
    public boolean exists(String id) {
        return backgroundRepository.existsById(id);
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!backgroundRepository.existsById(id)) {
            throw new EntityNotFoundException("Id: " + id + " does not exist.");
        }
        backgroundRepository.deleteById(id);
    }

    @Override
    public Page<BackgroundResponse> searchBackgrounds(BackgroundSearchRequest backgroundSearchRequest, Pageable pageable) {
        Specification<Background> specification = BackgroundSpecifications.withFilters(backgroundSearchRequest);

        return backgroundRepository.findAll(specification, pageable).map(backgroundMapper::toResponse);
    }
}
