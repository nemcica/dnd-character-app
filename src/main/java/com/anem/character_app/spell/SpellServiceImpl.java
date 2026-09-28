package com.anem.character_app.spell;

import com.anem.character_app.spell.dto.SpellCreateRequest;
import com.anem.character_app.spell.dto.SpellResponse;
import com.anem.character_app.spell.dto.SpellSearchRequest;
import com.anem.character_app.spell.dto.SpellUpdateRequest;
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
public class SpellServiceImpl implements SpellService{

    private final SpellRepository spellRepository;
    private final SpellMapper spellMapper;

    @Override
    public SpellResponse createSpell(SpellCreateRequest request) {
        if(spellRepository.existsById(request.id())) {
            throw new EntityExistsException("Id: " + request.id() + " already exists.");
        }
        Spell savedSpell = spellMapper.toEntity(request);
        return spellMapper.toResponse(spellRepository.save(savedSpell));
    }

    @Override
    public SpellResponse updateSpell(String id, SpellUpdateRequest request) {
        Spell existingSpell = spellRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id: " + id + " does not exist."));
        spellMapper.updateEntityFromRequest(request, existingSpell);
        Spell updatedSpell = spellRepository.save(existingSpell);

        return spellMapper.toResponse(updatedSpell);
    }

    @Override
    public Optional<SpellResponse> getSpellById(String id) {
        return spellRepository.findById(id).map(spellMapper::toResponse);
    }

    @Override
    public void deleteSpell(String id) {
        if(!spellRepository.existsById(id)) {
            throw new EntityNotFoundException("Id: " + id + " does not exist.");
        }
        spellRepository.deleteById(id);
    }

    @Override
    public Page<SpellResponse> searchSpells(SpellSearchRequest request, Pageable pageable) {
        Specification<Spell> specification = SpellSpecifications.withFilters(request);

        return spellRepository.findAll(specification, pageable).map(spellMapper::toResponse);
    }
}
