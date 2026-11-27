package com.an.character_app.character.service;

import com.an.character_app.character.TestCharacters;
import com.an.character_app.character.dto.AbilityScoresRequest;
import com.an.character_app.character.dto.CharacterSheetResponse;
import com.an.character_app.character.dto.CreateCharacterRequest;
import com.an.character_app.character.model.AbilityScoreModifier;
import com.an.character_app.character.model.PlayerCharacter;
import com.an.character_app.character.model.SkillProficiencyGrant;
import com.an.character_app.character.repository.PlayerCharacterRepository;
import com.an.character_app.exception.InvalidCharacterException;
import com.an.character_app.character.mapper.AbilityScoresMapper;
import com.an.character_app.rules.model.*;
import com.an.character_app.rules.repository.ArmorDefRepository;
import com.an.character_app.rules.repository.BackgroundDefRepository;
import com.an.character_app.rules.repository.ClassDefRepository;
import com.an.character_app.rules.repository.SpeciesDefRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerCharacterServiceImplTest {

    @Mock
    PlayerCharacterRepository playerCharacterRepository;
    @Mock
    ClassDefRepository classDefRepository;
    @Mock
    SpeciesDefRepository speciesDefRepository;
    @Mock
    BackgroundDefRepository backgroundDefRepository;
    @Mock
    ArmorDefRepository armorDefRepository;
    @Spy
    AbilityScoresMapper abilityScoresMapper = Mappers.getMapper(AbilityScoresMapper.class);
    @InjectMocks
    PlayerCharacterServiceImpl service;

    TestCharacters testCharacters = new TestCharacters();

    private CreateCharacterRequest request(String classId, String speciesId, String backgroundId) {
        return new CreateCharacterRequest(
                "Test", 1,
                new AbilityScoresRequest(15, 14, 13, 12, 10, 8),
                classId, speciesId, backgroundId,
                Map.of(Ability.STR, 2, Ability.CON, 1),
                null, null, null, null);
    }

    private void stubValidClassSpeciesBackground() {
        when(classDefRepository.findById("fighter"))
                .thenReturn(Optional.of(mock(ClassDef.class)));
        when(speciesDefRepository.findById("human"))
                .thenReturn(Optional.of(mock(SpeciesDef.class)));
        when(backgroundDefRepository.findById("soldier"))
                .thenReturn(Optional.of(mock(BackgroundDef.class)));
    }

    private CreateCharacterRequest requestWithArmor(String armorId, String shieldId) {
        return new CreateCharacterRequest(
                "Test", 1,
                new AbilityScoresRequest(15, 14, 13, 12, 10, 8),
                "fighter", "human", "soldier",
                Map.of(Ability.STR, 2, Ability.CON, 1),
                armorId, shieldId, null, null);
    }

    private void stubLookupsWithBackgroundOptions(Ability... options) {
        BackgroundDef background = mock(BackgroundDef.class);
        when(background.getAbilityOptions()).thenReturn(Set.of(options));
        when(classDefRepository.findById("fighter")).thenReturn(Optional.of(mock(ClassDef.class)));
        when(speciesDefRepository.findById("human")).thenReturn(Optional.of(mock(SpeciesDef.class)));
        when(backgroundDefRepository.findById("soldier")).thenReturn(Optional.of(background));
    }

    private void stubLookupsWithClassSkillOptions(int numOfSkills, Skill... options) {
        ClassDef dndClass = mock(ClassDef.class);

        BackgroundDef soldier = mock(BackgroundDef.class);
        when(soldier.getAbilityOptions()).thenReturn(Set.of(Ability.STR, Ability.DEX, Ability.CON));
        lenient().when(soldier.getSkills()).thenReturn(Set.of(Skill.ATHLETICS, Skill.INTIMIDATION));

        lenient().when(dndClass.getSkillOptions()).thenReturn(Set.of(options));
        when(dndClass.getSkillChoiceCount()).thenReturn(numOfSkills);
        when(backgroundDefRepository.findById("soldier")).thenReturn(Optional.of(soldier));
        when(speciesDefRepository.findById("human")).thenReturn(Optional.of(mock(SpeciesDef.class)));
        when(classDefRepository.findById("fighter")).thenReturn(Optional.of(dndClass));
    }

    private CreateCharacterRequest requestWithClassSkills(Set<Skill> classSKills) {
        return new CreateCharacterRequest(
                "Test", 1,
                new AbilityScoresRequest(15, 14, 13, 12, 10, 8),
                "fighter", "human", "soldier",
                Map.of(Ability.STR, 2, Ability.CON, 1),
                null, null, classSKills, null);
    }

    private CreateCharacterRequest requestWithBonuses(Map<Ability, Integer> bonuses) {
        return new CreateCharacterRequest(
                "Test", 1,
                new AbilityScoresRequest(15, 14, 13, 12, 10, 8),
                "fighter", "human", "soldier",
                bonuses, null, null, null, null);
    }

    private CreateCharacterRequest requestWithSkills(Set<Skill> classSkills, Set<Skill> freeSkillChoices) {
        return new CreateCharacterRequest(
                "Test", 5,
                new AbilityScoresRequest(15, 14, 13, 12, 10, 8),
                "fighter", "human", "soldier",
                Map.of(Ability.STR, 2, Ability.CON, 1),
                null, null,
                classSkills, freeSkillChoices);
    }

    private void stubSavePersistsWithId(Long id) {
        when(playerCharacterRepository.save(any(PlayerCharacter.class))).thenAnswer(inv -> {
            PlayerCharacter pc = inv.getArgument(0);
            ReflectionTestUtils.setField(pc, "id", id);
            when(playerCharacterRepository.findById(id)).thenReturn(Optional.of(pc));
            return pc;
        });
    }

    @Test
    void returnsSheetWithCalculatedStats() {
        PlayerCharacter pc = testCharacters.baseCharacter();
        when(playerCharacterRepository.findById(1L)).thenReturn(Optional.of(pc));

        CharacterSheetResponse sheet = service.getCharacterSheet(1L);

        assertThat(sheet.className()).isEqualTo("Fighter");
        assertThat(sheet.speciesName()).isEqualTo("Human");
        assertThat(sheet.calculatedStats().proficiencyBonus()).isEqualTo(3);
    }

    @Test
    void throwsWhenCharacterDoesNotExist() {
        when(playerCharacterRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCharacterSheet(99L)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void rejectsUnknownClass() {
        when(classDefRepository.findById("nope")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createCharacter(request("nope", "human", "soldier")))
                .isInstanceOf(InvalidCharacterException.class)
                .hasMessageContaining("nope");
    }

    @Test
    void rejectsUnknownSpecies() {
        when(classDefRepository.findById("fighter")).thenReturn(Optional.of(mock(ClassDef.class)));
        when(speciesDefRepository.findById("nope")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createCharacter(request("fighter", "nope", "soldier")))
                .isInstanceOf(InvalidCharacterException.class)
                .hasMessageContaining("nope");
    }

    @Test
    void rejectsUnknownBackground() {
        when(classDefRepository.findById("fighter")).thenReturn(Optional.of(mock(ClassDef.class)));
        when(speciesDefRepository.findById("human")).thenReturn(Optional.of(mock(SpeciesDef.class)));
        when(backgroundDefRepository.findById("nope")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createCharacter(request("fighter", "human", "nope")))
                .isInstanceOf(InvalidCharacterException.class)
                .hasMessageContaining("nope");
    }

    @Test
    void rejectUnknownArmorId() {
        stubValidClassSpeciesBackground();
        when(armorDefRepository.findById("banana")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createCharacter(requestWithArmor("banana", null)))
                .isInstanceOf(InvalidCharacterException.class)
                .hasMessageContaining("banana");
    }

    @Test
    void rejectShieldInArmorSlot() {
        stubValidClassSpeciesBackground();
        ArmorDef shield = mock(ArmorDef.class);
        when(shield.getCategory()).thenReturn(ArmorCategory.SHIELD);
        when(armorDefRepository.findById("shield")).thenReturn(Optional.of(shield));

        assertThatThrownBy(() -> service.createCharacter(requestWithArmor("shield", null)))
                .isInstanceOf(InvalidCharacterException.class)
                .hasMessageContaining("shield as armor");

    }

    @Test
    void rejectArmorInShieldSlot() {
        stubValidClassSpeciesBackground();
        ArmorDef armor = mock(ArmorDef.class);
        when(armor.getCategory()).thenReturn(ArmorCategory.MEDIUM);
        when(armorDefRepository.findById("studded")).thenReturn(Optional.of(armor));

        assertThatThrownBy(() -> service.createCharacter(requestWithArmor(null, "studded")))
                .isInstanceOf(InvalidCharacterException.class)
                .hasMessageContaining("armor as a shield");
    }

    @Test
    void rejectsAbilityNotOfferedByBackground() {
        stubLookupsWithBackgroundOptions(Ability.STR, Ability.DEX, Ability.CON);

        assertThatThrownBy(() -> service.createCharacter(
                requestWithBonuses(Map.of(Ability.INT, 2, Ability.STR, 1))))
                .isInstanceOf(InvalidCharacterException.class)
                .hasMessageContaining("INT");
    }

    @Test
    void rejectsBonusesThatTotalMoreThanThree() {
        stubLookupsWithBackgroundOptions(Ability.STR, Ability.DEX, Ability.CON);

        assertThatThrownBy(() -> service.createCharacter(
                requestWithBonuses(Map.of(Ability.STR, 2, Ability.DEX, 2))))
                .isInstanceOf(InvalidCharacterException.class)
                .hasMessageContaining("4");
    }

    @Test
    void rejectsBonusesThatTotalLessThanThree() {
        stubLookupsWithBackgroundOptions(Ability.STR, Ability.DEX, Ability.CON);

        assertThatThrownBy(() -> service.createCharacter(
                requestWithBonuses(Map.of(Ability.STR, 1, Ability.DEX, 1))))
                .isInstanceOf(InvalidCharacterException.class)
                .hasMessageContaining("2");
    }

    @Test
    void createsCharacterWithBackgroundBonusesAndClassSkills() {
        ClassDef fighter = mock(ClassDef.class);
        when(fighter.getId()).thenReturn("fighter");
        when(fighter.getName()).thenReturn("Fighter");
        when(fighter.getHitDie()).thenReturn(10);
        when(fighter.getSavingThrows()).thenReturn(Set.of(Ability.STR, Ability.CON));
        when(fighter.getSkillOptions()).thenReturn(Set.of(Skill.ATHLETICS, Skill.ACROBATICS, Skill.ANIMAL_HANDLING));
        when(fighter.getSkillChoiceCount()).thenReturn(2);

        SpeciesDef human = mock(SpeciesDef.class);
        when(human.getName()).thenReturn("Human");

        BackgroundDef soldier = mock(BackgroundDef.class);
        when(soldier.getId()).thenReturn("soldier");
        when(soldier.getName()).thenReturn("Soldier");
        when(soldier.getSkills()).thenReturn(Set.of(Skill.ATHLETICS));
        when(soldier.getAbilityOptions()).thenReturn(Set.of(Ability.STR, Ability.DEX, Ability.CON));

        when(classDefRepository.findById("fighter")).thenReturn(Optional.of(fighter));
        when(speciesDefRepository.findById("human")).thenReturn(Optional.of(human));
        when(backgroundDefRepository.findById("soldier")).thenReturn(Optional.of(soldier));
        stubSavePersistsWithId(1L);

        CreateCharacterRequest request = new CreateCharacterRequest(
                "Test", 5,
                new AbilityScoresRequest(15, 14, 13, 12, 10, 8),
                "fighter", "human", "soldier",
                Map.of(Ability.STR, 2, Ability.CON, 1),
                null, null, Set.of(Skill.ANIMAL_HANDLING, Skill.ACROBATICS), null);

        CharacterSheetResponse sheet = service.createCharacter(request);

        ArgumentCaptor<PlayerCharacter> captor = ArgumentCaptor.forClass(PlayerCharacter.class);
        verify(playerCharacterRepository).save(captor.capture());
        PlayerCharacter saved = captor.getValue();

        assertThat(saved.getAbilityScoreModifiers())
                .extracting(AbilityScoreModifier::getAbility, AbilityScoreModifier::getAmount,
                        AbilityScoreModifier::getSource)
                .containsExactlyInAnyOrder(
                        tuple(Ability.STR, 2, "BACKGROUND:soldier"),
                        tuple(Ability.CON, 1, "BACKGROUND:soldier"));
        assertThat(saved.getSkillProficiencyGrants())
                .extracting(SkillProficiencyGrant::getSkill, SkillProficiencyGrant::getSource)
                .containsExactlyInAnyOrder(tuple(Skill.ANIMAL_HANDLING, "CLASS:fighter"), tuple(Skill.ACROBATICS, "CLASS:fighter"), tuple(Skill.ATHLETICS, "BACKGROUND:soldier"));

        assertThat(sheet.name()).isEqualTo("Test");
        assertThat(sheet.className()).isEqualTo("Fighter");
        assertThat(sheet.calculatedStats().abilities().get(Ability.STR).total()).isEqualTo(17);
        assertThat(sheet.calculatedStats().skills().get(Skill.ATHLETICS)).isEqualTo(6);
        assertThat(sheet.calculatedStats().skills().get(Skill.ACROBATICS)).isEqualTo(5);
    }

    @ParameterizedTest(name = "allowed={0}, selected={1}")
    @CsvSource({
            "2, 1",
            "2, 3"
    })
    void rejectsInvalidNumberOfClassSkills(int skillChoiceCount, int selected) {
        List<Skill> availableOptions = List.of(Skill.ACROBATICS, Skill.ATHLETICS, Skill.ANIMAL_HANDLING);
        stubLookupsWithClassSkillOptions(skillChoiceCount, availableOptions.toArray(Skill[]::new));

        Set<Skill> chosenSkills = Set.copyOf(availableOptions.subList(0, selected));

        assertThatThrownBy(() -> service.createCharacter(requestWithClassSkills(chosenSkills)))
                .isInstanceOf(InvalidCharacterException.class)
                .hasMessageContaining("number of skills you can select: " + skillChoiceCount);
    }

    @Test
    void rejectsInvalidClassSkill() {
        stubLookupsWithClassSkillOptions(2, Skill.ACROBATICS, Skill.ATHLETICS, Skill.ANIMAL_HANDLING);

        assertThatThrownBy(()-> service.createCharacter(requestWithClassSkills(Set.of(Skill.ARCANA, Skill.ACROBATICS))))
                .isInstanceOf(InvalidCharacterException.class)
                .hasMessageContaining("ARCANA");
    }

    @Test
    void rejectsOverlapWithoutReplacementSkills() {
        stubLookupsWithClassSkillOptions(2, Skill.ATHLETICS, Skill.PERCEPTION, Skill.ACROBATICS);

        assertThatThrownBy(() -> service.createCharacter(
                requestWithSkills(Set.of(Skill.ATHLETICS, Skill.ACROBATICS), Set.of())))
                .isInstanceOf(InvalidCharacterException.class)
                .hasMessageContaining("exactly 1")
                .hasMessageContaining("0 were provided");
    }

    @Test
    void rejectsOverlapWhenReplacementsAreMissingEntirely() {
        stubLookupsWithClassSkillOptions(2, Skill.ATHLETICS, Skill.PERCEPTION, Skill.ACROBATICS);

        assertThatThrownBy(() -> service.createCharacter(
                requestWithSkills(Set.of(Skill.ATHLETICS, Skill.ACROBATICS), null)))
                .isInstanceOf(InvalidCharacterException.class)
                .hasMessageContaining("exactly 1");
    }

    @Test
    void rejectsTooManyReplacementSkills() {
        stubLookupsWithClassSkillOptions(2, Skill.ATHLETICS, Skill.PERCEPTION, Skill.ACROBATICS);

        assertThatThrownBy(() -> service.createCharacter(
                requestWithSkills(Set.of(Skill.ATHLETICS, Skill.ACROBATICS),
                        Set.of(Skill.STEALTH, Skill.ARCANA))))
                .isInstanceOf(InvalidCharacterException.class)
                .hasMessageContaining("2 were provided");
    }

    @Test
    void rejectsReplacementSkillsWhenThereIsNoOverlap() {
        stubLookupsWithClassSkillOptions(2, Skill.ATHLETICS, Skill.PERCEPTION, Skill.ACROBATICS);

        assertThatThrownBy(() -> service.createCharacter(
                requestWithSkills(Set.of(Skill.PERCEPTION, Skill.ACROBATICS),
                        Set.of(Skill.STEALTH))))
                .isInstanceOf(InvalidCharacterException.class)
                .hasMessageContaining("exactly 0");
    }

    @Test
    void rejectsReplacementAlreadyGrantedByBackground() {
        stubLookupsWithClassSkillOptions(2, Skill.ATHLETICS, Skill.PERCEPTION, Skill.ACROBATICS);

        assertThatThrownBy(() -> service.createCharacter(
                requestWithSkills(Set.of(Skill.ATHLETICS, Skill.ACROBATICS),
                        Set.of(Skill.INTIMIDATION))))
                .isInstanceOf(InvalidCharacterException.class)
                .hasMessageContaining("INTIMIDATION");
    }

    @Test
    void rejectsReplacementEqualToRemainingClassPick() {
        stubLookupsWithClassSkillOptions(2, Skill.ATHLETICS, Skill.PERCEPTION, Skill.ACROBATICS);

        assertThatThrownBy(() -> service.createCharacter(
                requestWithSkills(Set.of(Skill.ATHLETICS, Skill.ACROBATICS),
                        Set.of(Skill.ACROBATICS))))
                .isInstanceOf(InvalidCharacterException.class)
                .hasMessageContaining("ACROBATICS");
    }

    @Test
    void replacesOverlappingClassSkillWithFreeChoice() {
        ClassDef fighter = mock(ClassDef.class);
        when(fighter.getId()).thenReturn("fighter");
        when(fighter.getName()).thenReturn("Fighter");
        when(fighter.getHitDie()).thenReturn(10);
        when(fighter.getSavingThrows()).thenReturn(Set.of(Ability.STR, Ability.CON));
        when(fighter.getSkillChoiceCount()).thenReturn(2);
        when(fighter.getSkillOptions()).thenReturn(
                Set.of(Skill.ATHLETICS, Skill.PERCEPTION, Skill.ACROBATICS));

        SpeciesDef human = mock(SpeciesDef.class);
        when(human.getName()).thenReturn("Human");

        BackgroundDef soldier = mock(BackgroundDef.class);
        when(soldier.getId()).thenReturn("soldier");
        when(soldier.getName()).thenReturn("Soldier");
        when(soldier.getSkills()).thenReturn(Set.of(Skill.ATHLETICS, Skill.INTIMIDATION));
        when(soldier.getAbilityOptions()).thenReturn(Set.of(Ability.STR, Ability.DEX, Ability.CON));

        when(classDefRepository.findById("fighter")).thenReturn(Optional.of(fighter));
        when(speciesDefRepository.findById("human")).thenReturn(Optional.of(human));
        when(backgroundDefRepository.findById("soldier")).thenReturn(Optional.of(soldier));
        stubSavePersistsWithId(1L);

        service.createCharacter(requestWithSkills(
                Set.of(Skill.ATHLETICS, Skill.ACROBATICS),
                Set.of(Skill.STEALTH)));

        ArgumentCaptor<PlayerCharacter> captor = ArgumentCaptor.forClass(PlayerCharacter.class);
        verify(playerCharacterRepository).save(captor.capture());

        assertThat(captor.getValue().getSkillProficiencyGrants())
                .extracting(SkillProficiencyGrant::getSkill,
                        SkillProficiencyGrant::getProficiencyLevel,
                        SkillProficiencyGrant::getSource)
                .containsExactlyInAnyOrder(
                        tuple(Skill.ATHLETICS, ProficiencyLevel.PROFICIENT, "BACKGROUND:soldier"),
                        tuple(Skill.INTIMIDATION, ProficiencyLevel.PROFICIENT, "BACKGROUND:soldier"),
                        tuple(Skill.ACROBATICS, ProficiencyLevel.PROFICIENT, "CLASS:fighter"),
                        tuple(Skill.STEALTH, ProficiencyLevel.PROFICIENT, "REPLACEMENT:overlap"));
    }

    @Test
    void deleteThrowsWhenCharacterMissing() {
        when(playerCharacterRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteCharacter(99L))
                .isInstanceOf(EntityNotFoundException.class);
        verify(playerCharacterRepository, never()).deleteById(any());
    }

    @Test
    void deleteRemovesExistingCharacter() {
        when(playerCharacterRepository.existsById(1L)).thenReturn(true);

        service.deleteCharacter(1L);

        verify(playerCharacterRepository).deleteById(1L);
    }

    @Test
    void createsGrantsForBackgroundSkillsAndClassSkills() {
        ClassDef fighter = mock(ClassDef.class);
        when(fighter.getId()).thenReturn("fighter");
        when(fighter.getName()).thenReturn("Fighter");
        when(fighter.getHitDie()).thenReturn(10);
        when(fighter.getSavingThrows()).thenReturn(Set.of(Ability.STR, Ability.CON));
        when(fighter.getSkillChoiceCount()).thenReturn(2);
        when(fighter.getSkillOptions()).thenReturn(
                Set.of(Skill.ACROBATICS, Skill.ATHLETICS, Skill.PERCEPTION));

        SpeciesDef human = mock(SpeciesDef.class);
        when(human.getName()).thenReturn("Human");

        BackgroundDef soldier = mock(BackgroundDef.class);
        when(soldier.getId()).thenReturn("soldier");
        when(soldier.getName()).thenReturn("Soldier");
        when(soldier.getSkills()).thenReturn(Set.of(Skill.ATHLETICS, Skill.INTIMIDATION));
        when(soldier.getAbilityOptions()).thenReturn(Set.of(Ability.STR, Ability.DEX, Ability.CON));

        when(classDefRepository.findById("fighter")).thenReturn(Optional.of(fighter));
        when(speciesDefRepository.findById("human")).thenReturn(Optional.of(human));
        when(backgroundDefRepository.findById("soldier")).thenReturn(Optional.of(soldier));
        stubSavePersistsWithId(1L);

        CreateCharacterRequest request = new CreateCharacterRequest(
                "Test", 5,
                new AbilityScoresRequest(15, 14, 13, 12, 10, 8),
                "fighter", "human", "soldier",
                Map.of(Ability.STR, 2, Ability.CON, 1),
                null, null,
                Set.of(Skill.PERCEPTION, Skill.ACROBATICS), null);

        service.createCharacter(request);

        ArgumentCaptor<PlayerCharacter> captor = ArgumentCaptor.forClass(PlayerCharacter.class);
        verify(playerCharacterRepository).save(captor.capture());

        assertThat(captor.getValue().getSkillProficiencyGrants())
                .extracting(SkillProficiencyGrant::getSkill,
                        SkillProficiencyGrant::getProficiencyLevel,
                        SkillProficiencyGrant::getSource)
                .containsExactlyInAnyOrder(
                        tuple(Skill.ATHLETICS, ProficiencyLevel.PROFICIENT, "BACKGROUND:soldier"),
                        tuple(Skill.INTIMIDATION, ProficiencyLevel.PROFICIENT, "BACKGROUND:soldier"),
                        tuple(Skill.PERCEPTION, ProficiencyLevel.PROFICIENT, "CLASS:fighter"),
                        tuple(Skill.ACROBATICS, ProficiencyLevel.PROFICIENT, "CLASS:fighter"));
    }

}