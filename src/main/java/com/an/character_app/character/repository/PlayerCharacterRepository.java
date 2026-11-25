package com.an.character_app.character.repository;

import com.an.character_app.character.model.PlayerCharacter;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacter, Long> {

    @Override
    @EntityGraph(attributePaths = {"characterClass", "species", "background"})
    @NullMarked
    Page<PlayerCharacter> findAll(Pageable pageable);
}
