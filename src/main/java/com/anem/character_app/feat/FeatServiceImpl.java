package com.anem.character_app.feat;

import com.anem.character_app.feat.dto.FeatCreateRequest;
import com.anem.character_app.feat.dto.FeatResponse;
import com.anem.character_app.feat.dto.FeatUpdateRequest;
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
public class FeatServiceImpl implements FeatService {

    private final FeatMapper featMapper;
    private final FeatRepository featRepository;

    @Override
    @Transactional
    public FeatResponse createFeat(FeatCreateRequest request) {
        if (featRepository.existsById(request.id())) {
            throw new EntityExistsException("Id: " + request.id() + " already exists.");
        }

        Feat feat = featMapper.toEntity(request);
        Feat savedFeat = featRepository.save(feat);

        return featMapper.toResponse(savedFeat);
    }

    @Override
    @Transactional
    public FeatResponse updateFeat(String id, FeatUpdateRequest request) {
        Feat existingFeat = featRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id: " + id + " does not exist."));

        featMapper.updateEntityFromRequest(request, existingFeat);

        Feat updatedFeat = featRepository.save(existingFeat);

        return featMapper.toResponse(updatedFeat);
    }

    @Override
    public Optional<FeatResponse> getFeatById(String id) {
        return featRepository.findById(id).map(featMapper::toResponse);
    }

    @Override
    public boolean exists(String id) {
        return featRepository.existsById(id);
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!featRepository.existsById(id)) {
            throw new EntityNotFoundException("Id: " + id + " does not exist.");
        }
        featRepository.deleteById(id);
    }

    @Override
    public Page<FeatResponse> searchFeats(String name, FeatTag featTag, Pageable pageable) {
        Specification<Feat> specification = FeatSpecifications.withFilters(name, featTag);

        return featRepository.findAll(specification, pageable).map(featMapper::toResponse);
    }

}