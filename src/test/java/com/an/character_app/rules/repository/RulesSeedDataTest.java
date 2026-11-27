package com.an.character_app.rules.repository;

import com.an.character_app.rules.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RulesSeedDataTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16");

    @Autowired BackgroundDefRepository backgroundRepo;
    @Autowired
    ClassDefRepository classRepo;
    @Autowired ArmorDefRepository armorRepo;
    @Autowired
    JdbcTemplate jdbc;

    // --- backgrounds ---------------------------------------------------

    @Test
    void everyBackgroundHasExactlyOneToolSource() {
        List<BackgroundDef> backgrounds = backgroundRepo.findAll();

        assertThat(backgrounds).isNotEmpty();
        for (BackgroundDef b : backgrounds) {
            boolean hasTool = b.getTool() != null;
            boolean hasCategory = b.getToolCategory() != null;
            assertThat(hasTool ^ hasCategory)
                    .as("%s must have a tool or a tool category, not both and not neither", b.getId())
                    .isTrue();
        }
    }

    @Test
    void seededBackgroundToolsMatchExpectedValues() {
        BackgroundDef soldier = backgroundRepo.findById("soldier").orElseThrow();
        assertThat(soldier.getTool()).isNull();
        assertThat(soldier.getToolCategory()).isEqualTo(ToolCategory.GAMING_SET);

        BackgroundDef acolyte = backgroundRepo.findById("acolyte").orElseThrow();
        assertThat(acolyte.getTool()).isEqualTo(Tool.CALLIGRAPHERS_SUPPLIES);
        assertThat(acolyte.getToolCategory()).isNull();

        BackgroundDef criminal = backgroundRepo.findById("criminal").orElseThrow();
        assertThat(criminal.getTool()).isEqualTo(Tool.THIEVES_TOOLS);
        assertThat(criminal.getToolCategory()).isNull();
    }

    @Test
    void databaseRejectsBackgroundWithBothToolAndCategory() {
        assertThatThrownBy(() -> jdbc.update(
                "insert into background_def (id, name, source, tool, tool_category) values (?, ?, ?, ?, ?)",
                "bad-both", "Bad Both", "TEST", "SOME_TOOL", "SOME_CATEGORY"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void databaseRejectsBackgroundWithNeitherToolNorCategory() {
        assertThatThrownBy(() -> jdbc.update(
                "insert into background_def (id, name, source, tool, tool_category) values (?, ?, ?, ?, ?)",
                "bad-neither", "Bad Neither", "TEST", null, null))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    // --- classes -------------------------------------------------------

    @Test
    void everyClassLoadsWithValidSkillOptions() {
        List<ClassDef> classes = classRepo.findAll();

        assertThat(classes).isNotEmpty();
        for (ClassDef c : classes) {
            assertThat(c.getSavingThrows()).as(c.getId() + " saving throws").hasSize(2);
            assertThat(c.getSkillOptions().size())
                    .as(c.getId() + " needs at least as many options as picks")
                    .isGreaterThanOrEqualTo(c.getSkillChoiceCount());
        }
    }

    @Test
    void seededClassSkillChoiceCountsMatchExpectedValues() {
        assertThat(classRepo.findById("fighter").orElseThrow().getSkillChoiceCount()).isEqualTo(2);
        assertThat(classRepo.findById("wizard").orElseThrow().getSkillChoiceCount()).isEqualTo(2);
        assertThat(classRepo.findById("rogue").orElseThrow().getSkillChoiceCount()).isEqualTo(4);
    }

    // --- armor ---------------------------------------------------------

    @Test
    void armorSeedDataFollowsCalculatorAssumptions() {
        List<ArmorDef> armor = armorRepo.findAll();

        assertThat(armor).isNotEmpty();
        for (ArmorDef a : armor) {
            switch (a.getCategory()) {
                case LIGHT  -> assertThat(a.getDexCap()).as(a.getId()).isNull();
                case MEDIUM -> assertThat(a.getDexCap()).as(a.getId()).isEqualTo(2);
                case HEAVY  -> assertThat(a.getDexCap()).as(a.getId()).isZero();
                case SHIELD -> assertThat(a.getBaseAc()).as(a.getId()).isEqualTo(2);
            }
        }
    }

    @Test
    void updatedArmorRowsHaveStrengthAndStealthValues() {
        ArmorDef chainMail = armorRepo.findById("chain-mail").orElseThrow();
        assertThat(chainMail.getMinStrRequired()).isEqualTo(13);
        assertThat(chainMail.isStealthDisadvantage()).isTrue();

        ArmorDef plate = armorRepo.findById("plate").orElseThrow();
        assertThat(plate.getMinStrRequired()).isEqualTo(15);
        assertThat(plate.isStealthDisadvantage()).isTrue();
    }
}
