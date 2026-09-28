package com.anem.character_app.subClass;

import com.anem.character_app.subClass.dto.SubClassCreateRequest;
import com.anem.character_app.subClass.dto.SubClassResponse;
import com.anem.character_app.subClass.dto.SubClassSearchRequest;
import com.anem.character_app.subClass.dto.SubClassUpdateRequest;
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
public class SubClassServiceImpl implements SubClassService {

    SubClassRepository subClassRepository;
    SubClassMapper subClassMapper;

    @Override
    @Transactional
    public SubClassResponse createSubClass(SubClassCreateRequest request) {
        if (subClassRepository.existsById(request.id())) {
            throw new EntityExistsException("Id: " + request.id() + " already exists.");
        }

        SubClass savedSubClass = subClassMapper.toEntity(request);
        return subClassMapper.toResponse(subClassRepository.save(savedSubClass));
    }

    @Override
    @Transactional
    public SubClassResponse updateSubClass(String id, SubClassUpdateRequest request) {
        SubClass existingSubClass = subClassRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id: " + id + " does not exist."));

        subClassMapper.updateFromRequest(request, existingSubClass);
        SubClass updatedSubClass = subClassRepository.save(existingSubClass);
        return subClassMapper.toResponse(updatedSubClass);
    }

    @Override
    public Optional<SubClassResponse> getSubClassById(String id) {
        return subClassRepository.findById(id).map(subClassMapper::toResponse);
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!subClassRepository.existsById(id)) {
            throw new EntityNotFoundException("Id: " + id + " does not exist.");
        }
        subClassRepository.deleteById(id);
    }

    @Override
    public Page<SubClassResponse> searchSubClasses(SubClassSearchRequest request, Pageable pageable) {
        Specification<SubClass> specification = SubClassSpecifications.withFilters(request);

        return subClassRepository.findAll(specification, pageable).map(subClassMapper::toResponse);
    }
}
