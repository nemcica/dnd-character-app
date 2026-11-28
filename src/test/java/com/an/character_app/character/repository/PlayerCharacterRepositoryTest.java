package com.an.character_app.character.repository;

import com.an.character_app.character.model.*;
import com.an.character_app.rules.model.*;
import com.an.character_app.rules.repository.ArmorDefRepository;
import com.an.character_app.rules.repository.BackgroundDefRepository;
import com.an.character_app.rules.repository.ClassDefRepository;
import com.an.character_app.rules.repository.SpeciesDefRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

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

    @Autowired
    EntityManager entityManager;

    private PlayerCharacter newTestCharacter() {
        PlayerCharacter playerCharacter = new PlayerCharacter();
        playerCharacter.setName("Test fighter");
        playerCharacter.setLevel(1);
        playerCharacter.setBaseAbilityScores(new AbilityScores(15, 14, 13, 12, 10, 8));
        playerCharacter.setCharacterClass(classDefRepository.findById("fighter").orElseThrow());
        playerCharacter.setSpecies(speciesDefRepository.findById("human").orElseThrow());
        playerCharacter.setBackground(backgroundDefRepository.findById("soldier").orElseThrow());
        playerCharacter.setArmor(armorDefRepository.findById("chain-mail").orElseThrow());

        return playerCharacter;
    }

    @Test
    void savesAndLoadsCharacter() {
        PlayerCharacter playerCharacter = newTestCharacter();

        PlayerCharacter savedCharacter = playerCharacterRepository.saveAndFlush(playerCharacter);

        PlayerCharacter loadedCharacter = playerCharacterRepository.findById(savedCharacter.getId()).orElseThrow();

        assertThat(loadedCharacter.getName()).isEqualTo("Test fighter");
        assertThat(loadedCharacter.getBaseAbilityScores().getAbility(Ability.STR)).isEqualTo(15);
        assertThat(loadedCharacter.getCharacterClass().getId()).isEqualTo("fighter");
        assertThat(loadedCharacter.getShield()).isNull();
    }

    @Test
    void savesAndLoadsSkillProficiencyGrants() {
        PlayerCharacter playerCharacter = newTestCharacter();
        playerCharacter.addSkillProficiencyGrant(Skill.SLEIGHT_OF_HAND, ProficiencyLevel.PROFICIENT, "CLASS:rogue");
        playerCharacter.addSkillProficiencyGrant(Skill.ACROBATICS, ProficiencyLevel.EXPERTISE, "CLASS:rogue");
        Long id = playerCharacterRepository.saveAndFlush(playerCharacter).getId();
        entityManager.clear();

        PlayerCharacter loadedCharacter = playerCharacterRepository.findById(id).orElseThrow();
        assertThat(loadedCharacter.getSkillProficiencyGrants())
                .extracting(SkillProficiencyGrant::getSkill, SkillProficiencyGrant::getProficiencyLevel)
                .containsExactlyInAnyOrder(
                        tuple(Skill.SLEIGHT_OF_HAND, ProficiencyLevel.PROFICIENT),
                        tuple(Skill.ACROBATICS, ProficiencyLevel.EXPERTISE)
                );


    }

    @Test
    void savesAndLoadsToolProficiencyGrants() {
        PlayerCharacter c = newTestCharacter();
        c.addToolProficiencyGrant(Tool.CALLIGRAPHERS_SUPPLIES, "BACKGROUND:acolyte");
        c.addToolProficiencyGrant(Tool.THIEVES_TOOLS, "CLASS:rogue");

        Long id = playerCharacterRepository.saveAndFlush(c).getId();
        entityManager.clear();

        PlayerCharacter loaded = playerCharacterRepository.findById(id).orElseThrow();

        assertThat(loaded.getToolProficiencyGrants())
                .extracting(ToolProficiencyGrant::getTool, ToolProficiencyGrant::getSource)
                .containsExactlyInAnyOrder(
                        tuple(Tool.CALLIGRAPHERS_SUPPLIES, "BACKGROUND:acolyte"),
                        tuple(Tool.THIEVES_TOOLS, "CLASS:rogue"));
    }

    @Test
    void sameToolFromTwoSourcesIsStoredTwice() {
        PlayerCharacter c = newTestCharacter();
        c.addToolProficiencyGrant(Tool.THIEVES_TOOLS, "CLASS:rogue");
        c.addToolProficiencyGrant(Tool.THIEVES_TOOLS, "BACKGROUND:criminal");

        Long id = playerCharacterRepository.saveAndFlush(c).getId();
        entityManager.clear();

        assertThat(playerCharacterRepository.findById(id).orElseThrow().getToolProficiencyGrants())
                .extracting(ToolProficiencyGrant::getSource)
                .containsExactlyInAnyOrder("CLASS:rogue", "BACKGROUND:criminal");
    }

    @Test
    void deletingCharacterDeletesItsToolGrants() {
        PlayerCharacter c = newTestCharacter();
        c.addToolProficiencyGrant(Tool.THIEVES_TOOLS, "CLASS:rogue");
        Long id = playerCharacterRepository.saveAndFlush(c).getId();
        entityManager.clear();

        playerCharacterRepository.deleteById(id);
        playerCharacterRepository.flush();
        entityManager.clear();

        Number remaining = (Number) entityManager
                .createNativeQuery("select count(*) from tool_proficiency_grant where player_character_id = :id")
                .setParameter("id", id)
                .getSingleResult();
        assertThat(remaining.longValue()).isZero();
    }

    @Test
    void savesAndLoadsArmorTrainingGrants() {
        PlayerCharacter c = newTestCharacter();
        c.addArmorTrainingGrant(ArmorCategory.LIGHT, "CLASS:rogue");
        c.addArmorTrainingGrant(ArmorCategory.SHIELD, "FEAT:test");

        Long id = playerCharacterRepository.saveAndFlush(c).getId();
        entityManager.clear();

        assertThat(playerCharacterRepository.findById(id).orElseThrow().getArmorTrainingGrants())
                .extracting(ArmorTrainingGrant::getArmorCategory, ArmorTrainingGrant::getSource)
                .containsExactlyInAnyOrder(
                        tuple(ArmorCategory.LIGHT, "CLASS:rogue"),
                        tuple(ArmorCategory.SHIELD, "FEAT:test"));
    }

    @Test
    void deletingCharacterDeletesItsArmorTrainingGrants() {
        PlayerCharacter c = newTestCharacter();
        c.addArmorTrainingGrant(ArmorCategory.LIGHT, "CLASS:rogue");
        Long id = playerCharacterRepository.saveAndFlush(c).getId();
        entityManager.clear();

        playerCharacterRepository.deleteById(id);
        playerCharacterRepository.flush();
        entityManager.clear();

        Number remaining = (Number) entityManager
                .createNativeQuery("select count(*) from armor_training_grant where player_character_id = :id")
                .setParameter("id", id)
                .getSingleResult();
        assertThat(remaining.longValue()).isZero();
    }
}