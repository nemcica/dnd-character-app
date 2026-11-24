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
import com.an.character_app.mapper.AbilityScoresMapper;
import com.an.character_app.rules.model.*;
import com.an.character_app.rules.repository.ArmorDefRepository;
import com.an.character_app.rules.repository.BackgroundDefRepository;
import com.an.character_app.rules.repository.ClassDefRepository;
import com.an.character_app.rules.repository.SpeciesDefRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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
                null, null, null);
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
                armorId, shieldId, null);
    }

    private void stubLookupsWithBackgroundOptions(Ability... options) {
        BackgroundDef background = mock(BackgroundDef.class);
        when(background.getAbilityOptions()).thenReturn(Set.of(options));
        when(classDefRepository.findById("fighter")).thenReturn(Optional.of(mock(ClassDef.class)));
        when(speciesDefRepository.findById("human")).thenReturn(Optional.of(mock(SpeciesDef.class)));
        when(backgroundDefRepository.findById("soldier")).thenReturn(Optional.of(background));
    }

    private CreateCharacterRequest requestWithBonuses(Map<Ability, Integer> bonuses) {
        return new CreateCharacterRequest(
                "Test", 1,
                new AbilityScoresRequest(15, 14, 13, 12, 10, 8),
                "fighter", "human", "soldier",
                bonuses, null, null, null);
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
                null, null, Set.of(Skill.STEALTH));

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
                .containsExactly(tuple(Skill.STEALTH, "CLASS:fighter"));

        assertThat(sheet.name()).isEqualTo("Test");
        assertThat(sheet.className()).isEqualTo("Fighter");
        assertThat(sheet.calculatedStats().abilities().get(Ability.STR).total()).isEqualTo(17);
        assertThat(sheet.calculatedStats().skills().get(Skill.ATHLETICS)).isEqualTo(6);
        assertThat(sheet.calculatedStats().skills().get(Skill.STEALTH)).isEqualTo(5);
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

}