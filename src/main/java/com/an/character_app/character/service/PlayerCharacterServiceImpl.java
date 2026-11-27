package com.an.character_app.character.service;


import com.an.character_app.calc.CalculatedStats;
import com.an.character_app.calc.CharacterCalculator;
import com.an.character_app.calc.CharacterInput;
import com.an.character_app.calc.CharacterInputMapper;
import com.an.character_app.character.dto.CharacterSheetResponse;
import com.an.character_app.character.dto.CharacterSummary;
import com.an.character_app.character.dto.CreateCharacterRequest;
import com.an.character_app.character.model.AbilityScores;
import com.an.character_app.character.model.PlayerCharacter;
import com.an.character_app.character.repository.PlayerCharacterRepository;
import com.an.character_app.exception.InvalidCharacterException;
import com.an.character_app.character.mapper.AbilityScoresMapper;
import com.an.character_app.rules.model.*;
import com.an.character_app.rules.repository.ArmorDefRepository;
import com.an.character_app.rules.repository.BackgroundDefRepository;
import com.an.character_app.rules.repository.ClassDefRepository;
import com.an.character_app.rules.repository.SpeciesDefRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlayerCharacterServiceImpl implements PlayerCharacterService {

    private final PlayerCharacterRepository playerCharacterRepository;
    private final ClassDefRepository classDefRepository;
    private final SpeciesDefRepository speciesDefRepository;
    private final BackgroundDefRepository backgroundDefRepository;
    private final ArmorDefRepository armorDefRepository;
    private final AbilityScoresMapper abilityScoresMapper;

    @Override
    public CharacterSheetResponse getCharacterSheet(Long id) {
        PlayerCharacter foundCharacter = playerCharacterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Character with id: " + id + " does not exist."));
        CharacterInput input = CharacterInputMapper.toInput(foundCharacter);
        CalculatedStats stats = CharacterCalculator.calculate(input);

        return new CharacterSheetResponse(
                foundCharacter.getId(),
                foundCharacter.getName(),
                foundCharacter.getLevel(),
                foundCharacter.getSpecies().getName(),
                foundCharacter.getCharacterClass().getName(),
                foundCharacter.getBackground().getName(),
                stats
        );
    }

    @Override
    @Transactional
    public CharacterSheetResponse createCharacter(CreateCharacterRequest request) {
        PlayerCharacter createdCharacter = new PlayerCharacter();

        ClassDef playerClass = classDefRepository.findById(request.classId()).orElseThrow(() -> new InvalidCharacterException("Class with id: " + request.classId() + " does not exist."));
        SpeciesDef playerSpecies = speciesDefRepository.findById(request.speciesId()).orElseThrow(() -> new InvalidCharacterException("Species with id: " + request.speciesId() + " does not exist."));
        BackgroundDef playerBackground = backgroundDefRepository.findById(request.backgroundId()).orElseThrow(() -> new InvalidCharacterException("Background with id: " + request.backgroundId() + " does not exist."));

        ArmorDef playerArmor = null;
        if (request.armorId() != null) {
            playerArmor = findArmor(request.armorId(), "Armor");
            if (playerArmor.getCategory() == ArmorCategory.SHIELD) {
                throw new InvalidCharacterException("You cannot equip a shield as armor.");
            }
        }

        ArmorDef playerShield = null;
        if (request.shieldId() != null) {
            playerShield = findArmor(request.shieldId(), "Shield");
            if (playerShield.getCategory() != ArmorCategory.SHIELD) {
                throw new InvalidCharacterException("You cannot equip armor as a shield.");
            }
        }

        AbilityScores baseAbilityScores = abilityScoresMapper.toEntity(request.baseAbilityScores());

        int backgroundBonusValidation = 0;
        for (Ability a : request.backgroundAbilityBonuses().keySet()) {
            if (!playerBackground.getAbilityOptions().contains(a)) {
                throw new InvalidCharacterException("The ability: " + a + " is not a part of the provided background.");
            }
            backgroundBonusValidation += request.backgroundAbilityBonuses().get(a);
        }
        if (backgroundBonusValidation != 3)
            throw new InvalidCharacterException("Total of provided ability bonuses is not correct: " + backgroundBonusValidation);

        if (request.classSkills().size() != playerClass.getSkillChoiceCount())
            throw new InvalidCharacterException("Number of selected class skills: " + request.classSkills().size() + " does not match the number of skills you can select: " + playerClass.getSkillChoiceCount());

        for (Skill skill : request.classSkills()) {
            if (!playerClass.getSkillOptions().contains(skill)) {
                throw new InvalidCharacterException("The skill: " + skill.name() + " is not available in skill options of: " + playerClass.getName());
            }
            if (playerBackground.getSkills().contains(skill)) {
                throw new InvalidCharacterException("The skill: " + skill.name() + " is already provided by background: " + playerBackground.getName());
            }
        }


        createdCharacter.setName(request.name());
        createdCharacter.setLevel(request.level());
        createdCharacter.setCharacterClass(playerClass);
        createdCharacter.setSpecies(playerSpecies);
        createdCharacter.setBackground(playerBackground);
        createdCharacter.setArmor(playerArmor);
        createdCharacter.setShield(playerShield);
        createdCharacter.setBaseAbilityScores(baseAbilityScores);

        for (Map.Entry<Ability, Integer> e : request.backgroundAbilityBonuses().entrySet()) {
            createdCharacter.addAbilityScoreModifier(e.getKey(), e.getValue(), "BACKGROUND:" + playerBackground.getId());
        }

        for(Skill skill : playerBackground.getSkills()) {
            createdCharacter.addSkillProficiencyGrant(skill, ProficiencyLevel.PROFICIENT, "BACKGROUND:" + playerBackground.getId());
        }

        for (Skill skill : request.classSkills()) {
            createdCharacter.addSkillProficiencyGrant(skill, ProficiencyLevel.PROFICIENT, "CLASS:" + playerClass.getId());
        }

        Long savedCharacterId = playerCharacterRepository.save(createdCharacter).getId();

        return getCharacterSheet(savedCharacterId);
    }

    @Override
    public Page<CharacterSummary> getCharacters(Pageable pageable) {
        return playerCharacterRepository.findAll(pageable)
                .map(c -> new CharacterSummary(
                        c.getId(), c.getName(), c.getLevel(),
                        c.getCharacterClass().getName(),
                        c.getSpecies().getName(),
                        c.getBackground().getName()));
    }

    @Override
    @Transactional
    public void deleteCharacter(Long id) {
        if (!playerCharacterRepository.existsById(id)) {
            throw new EntityNotFoundException("Character with id: " + id + " does not exist.");
        }
        playerCharacterRepository.deleteById(id);
    }

    private ArmorDef findArmor(String id, String label) {
        return armorDefRepository.findById(id)
                .orElseThrow(() -> new InvalidCharacterException(
                        label + " with id: " + id + " does not exist."));
    }
}
