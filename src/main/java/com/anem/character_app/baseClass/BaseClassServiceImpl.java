package com.anem.character_app.baseClass;

import com.anem.character_app.baseClass.dto.BaseClassCreateRequest;
import com.anem.character_app.baseClass.dto.BaseClassResponse;
import com.anem.character_app.baseClass.dto.BaseClassSearchRequest;
import com.anem.character_app.baseClass.dto.BaseClassUpdateRequest;
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
public class BaseClassServiceImpl implements BaseClassService {

    private final BaseClassRepository baseClassRepository;
    private final BaseClassMapper baseClassMapper;

    @Override
    @Transactional
    public BaseClassResponse createBaseClass(BaseClassCreateRequest request) {
        if (baseClassRepository.existsById(request.id())) {
            throw new EntityExistsException("Id: " + request.id() + " already exists.");
        }
        BaseClass savedBaseClass = baseClassMapper.toEntity(request);
        return baseClassMapper.toResponse(baseClassRepository.save(savedBaseClass));
    }

    @Override
    @Transactional
    public BaseClassResponse updateBaseClass(String id, BaseClassUpdateRequest request) {
        BaseClass existingBaseClass = baseClassRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id: " + id + " does not exist."));

        baseClassMapper.updateEntityFromRequest(request, existingBaseClass);
        BaseClass updatedBaseClass = baseClassRepository.save(existingBaseClass);
        return baseClassMapper.toResponse(updatedBaseClass);
    }

    @Override
    public Optional<BaseClassResponse> getBaseClassById(String id) {
        return baseClassRepository.findById(id).map(baseClassMapper::toResponse);
    }

    @Override
    public boolean exists(String id) {
        return baseClassRepository.existsById(id);
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!baseClassRepository.existsById(id)) {
            throw new EntityNotFoundException("Id: " + id + " does not exist.");
        }
        baseClassRepository.deleteById(id);
    }

    @Override
    public Page<BaseClassResponse> searchBaseClasses(BaseClassSearchRequest baseClassSearchRequest, Pageable pageable) {
        Specification<BaseClass> specification = BaseClassSpecifications.withFilters(baseClassSearchRequest);

        return baseClassRepository.findAll(specification, pageable).map(baseClassMapper::toResponse);
    }
}
