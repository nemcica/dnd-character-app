package com.an.character_app.character.repository;

import com.an.character_app.character.model.AbilityScores;
import com.an.character_app.character.model.PlayerCharacter;
import com.an.character_app.rules.model.Ability;
import com.an.character_app.rules.repository.ArmorDefRepository;
import com.an.character_app.rules.repository.BackgroundDefRepository;
import com.an.character_app.rules.repository.ClassDefRepository;
import com.an.character_app.rules.repository.SpeciesDefRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PlayerCharacterRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16");

    @Autowired
    PlayerCharacterRepository playerCharacterRepository;

    @Autowired
    ClassDefRepository classDefRepository;

    @Autowired
    BackgroundDefRepository backgroundDefRepository;

    @Autowired
    SpeciesDefRepository speciesDefRepository;

    @Autowired
    ArmorDefRepository armorDefRepository;

    @Test
    void savesAndLoadsCharacter() {
        PlayerCharacter playerCharacter = new PlayerCharacter();
        playerCharacter.setName("Test fighter");
        playerCharacter.setLevel(1);
        playerCharacter.setBaseAbilityScores(new AbilityScores(15, 14, 13, 12, 10, 8));
        playerCharacter.setCharacterClass(classDefRepository.findById("fighter").orElseThrow());
        playerCharacter.setSpecies(speciesDefRepository.findById("human").orElseThrow());
        playerCharacter.setBackground(backgroundDefRepository.findById("soldier").orElseThrow());
        playerCharacter.setArmor(armorDefRepository.findById("chain-mail").orElseThrow());

        PlayerCharacter savedCharacter = playerCharacterRepository.saveAndFlush(playerCharacter);

        PlayerCharacter loadedCharacter = playerCharacterRepository.findById(savedCharacter.getId()).orElseThrow();

        assertThat(loadedCharacter.getName()).isEqualTo("Test fighter");
        assertThat(loadedCharacter.getBaseAbilityScores().getAbility(Ability.STR)).isEqualTo(15);
        assertThat(loadedCharacter.getCharacterClass().getId()).isEqualTo("fighter");
        assertThat(loadedCharacter.getShield()).isNull();
    }
}