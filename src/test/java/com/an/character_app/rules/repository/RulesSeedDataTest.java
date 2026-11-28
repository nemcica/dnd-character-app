package com.an.character_app.rules.repository;

import com.an.character_app.rules.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
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
    @Autowired
    EntityManager entityManager;

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

    @Test
    void seededClassArmorTrainingMatchesExpectedValues() {
        assertThat(classRepo.findById("fighter").orElseThrow().getArmorTraining())
                .containsExactlyInAnyOrder(ArmorCategory.LIGHT, ArmorCategory.MEDIUM,
                        ArmorCategory.HEAVY, ArmorCategory.SHIELD);
        assertThat(classRepo.findById("wizard").orElseThrow().getArmorTraining()).isEmpty();
        assertThat(classRepo.findById("rogue").orElseThrow().getArmorTraining())
                .containsExactly(ArmorCategory.LIGHT);
    }

    // --- weapons ---------------------------------------------------------
    @Autowired
    WeaponDefRepository weaponDefRepository;

    @Test
    void everyWeaponLoadsWithRequiredFields() {
        List<WeaponDef> weapons = weaponDefRepository.findAll();

        assertThat(weapons).isNotEmpty();
        for (WeaponDef w : weapons) {
            assertThat(w.getId()).matches("[a-z]+(-[a-z]+)*");
            assertThat(w.getName()).as(w.getId() + " name").isNotBlank();
            assertThat(w.getWeaponCategory()).as(w.getId() + " category").isNotNull();
            assertThat(w.getMeleeOrRanged()).as(w.getId() + " melee/ranged").isNotNull();
            assertThat(w.getDamageType()).as(w.getId() + " damage type").isNotNull();
            assertThat(w.getWeaponMastery()).as(w.getId() + " mastery").isNotNull();
            assertThat(w.getSource()).as(w.getId() + " source").isNotBlank();
        }
    }

    @Test
    void weaponDiceAndRangeStringsFollowTheExpectedPatterns() {
        for (WeaponDef w : weaponDefRepository.findAll()) {
            assertThat(w.getDamageDice()).as(w.getId() + " damage dice").matches("\\d+d\\d+|\\d+");
            if (w.getVersatileDamageDice() != null) {
                assertThat(w.getVersatileDamageDice()).as(w.getId() + " versatile dice").matches("\\d+d\\d+");
            }
            if (w.getWeaponRange() != null) {
                assertThat(w.getWeaponRange()).as(w.getId() + " range").matches("\\d+/\\d+");
            }
        }
    }

    @Test
    void versatileDiceArePresentExactlyWhenTheVersatilePropertyIs() {
        for (WeaponDef w : weaponDefRepository.findAll()) {
            boolean hasProperty = w.getWeaponProperties().contains(WeaponProperty.VERSATILE);
            boolean hasDice = w.getVersatileDamageDice() != null;
            assertThat(hasDice).as("%s: versatile property and versatile dice must agree", w.getId())
                    .isEqualTo(hasProperty);
        }
    }

    @Test
    void rangeIsPresentExactlyWhenTheWeaponIsThrownOrUsesAmmunition() {
        for (WeaponDef w : weaponDefRepository.findAll()) {
            boolean needsRange = w.getWeaponProperties().contains(WeaponProperty.THROWN)
                    || w.getWeaponProperties().contains(WeaponProperty.AMMUNITION);
            assertThat(w.getWeaponRange() != null).as("%s: range and thrown/ammunition must agree", w.getId())
                    .isEqualTo(needsRange);
        }
    }

    @Test
    void everyRangedWeaponIsThrownOrUsesAmmunition() {
        for (WeaponDef w : weaponDefRepository.findAll()) {
            if (w.getMeleeOrRanged() == MeleeOrRanged.RANGED) {
                assertThat(w.getWeaponProperties())
                        .as(w.getId() + " is ranged, so it should be thrown or use ammunition")
                        .containsAnyOf(WeaponProperty.THROWN, WeaponProperty.AMMUNITION);
            }
        }
    }

    @Test
    void catalogCoversBothCategoriesAndBothKindsOfWeapon() {
        List<WeaponDef> weapons = weaponDefRepository.findAll();

        assertThat(weapons).anyMatch(w -> w.getWeaponCategory() == WeaponCategory.SIMPLE);
        assertThat(weapons).anyMatch(w -> w.getWeaponCategory() == WeaponCategory.MARTIAL);
        assertThat(weapons).anyMatch(w -> w.getMeleeOrRanged() == MeleeOrRanged.MELEE);
        assertThat(weapons).anyMatch(w -> w.getMeleeOrRanged() == MeleeOrRanged.RANGED);
    }

    @Test
    void catalogSupportsTheRogueStyleProficiencyRule() {
        List<WeaponDef> martial = weaponDefRepository.findAll().stream()
                .filter(w -> w.getWeaponCategory() == WeaponCategory.MARTIAL)
                .toList();

        assertThat(martial).anyMatch(w -> w.getWeaponProperties().contains(WeaponProperty.FINESSE)
                || w.getWeaponProperties().contains(WeaponProperty.LIGHT));
        assertThat(martial).anyMatch(w -> !w.getWeaponProperties().contains(WeaponProperty.FINESSE)
                && !w.getWeaponProperties().contains(WeaponProperty.LIGHT));
    }

    @Test
    void weaponPropertiesAreLoadedEagerly() {
        entityManager.clear();

        List<WeaponDef> weapons = weaponDefRepository.findAll();

        PersistenceUnitUtil util = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        for (WeaponDef w : weapons) {
            assertThat(util.isLoaded(w, "weaponProperties")).as(w.getId()).isTrue();
        }
    }

    @Test
    void spotCheckedWeaponsMatchTheBook() {
        WeaponDef dagger = weaponDefRepository.findById("dagger").orElseThrow();
        assertThat(dagger.getDamageDice()).isEqualTo("1d4");
        assertThat(dagger.getDamageType()).isEqualTo(DamageType.PIERCING);
        assertThat(dagger.getWeaponProperties())
                .contains(WeaponProperty.FINESSE, WeaponProperty.LIGHT, WeaponProperty.THROWN);

        WeaponDef longsword = weaponDefRepository.findById("longsword").orElseThrow();
        assertThat(longsword.getDamageDice()).isEqualTo("1d8");
        assertThat(longsword.getVersatileDamageDice()).isEqualTo("1d10");
        assertThat(longsword.getWeaponCategory()).isEqualTo(WeaponCategory.MARTIAL);

        WeaponDef shortbow = weaponDefRepository.findById("shortbow").orElseThrow();
        assertThat(shortbow.getMeleeOrRanged()).isEqualTo(MeleeOrRanged.RANGED);
        assertThat(shortbow.getWeaponProperties()).contains(WeaponProperty.AMMUNITION);
        assertThat(shortbow.getWeaponRange()).isNotNull();
    }

    @Test
    void everyWeaponMasteryHasADescription() {
        for (WeaponMastery m : WeaponMastery.values()) {
            assertThat(m.getDescription()).as(m.name() + " description").isNotBlank();
        }
    }
}
