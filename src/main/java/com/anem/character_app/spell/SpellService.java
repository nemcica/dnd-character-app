package com.anem.character_app.spell;

import com.anem.character_app.spell.dto.SpellCreateRequest;
import com.anem.character_app.spell.dto.SpellResponse;
import com.anem.character_app.spell.dto.SpellSearchRequest;
import com.anem.character_app.spell.dto.SpellUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SpellService {

    SpellResponse createSpell(SpellCreateRequest request);

    SpellResponse updateSpell(String id, SpellUpdateRequest request);

    Optional<SpellResponse> getSpellById(String id);

    void deleteSpell(String id);

    Page<SpellResponse> searchSpells(SpellSearchRequest request, Pageable pageable);
}
