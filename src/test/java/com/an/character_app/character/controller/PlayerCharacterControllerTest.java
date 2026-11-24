package com.an.character_app.character.controller;

import com.an.character_app.calc.AbilityBreakdown;
import com.an.character_app.calc.CalculatedStats;
import com.an.character_app.calc.CharacterCalculator;
import com.an.character_app.calc.CharacterInput;
import com.an.character_app.character.dto.AbilityScoresRequest;
import com.an.character_app.character.dto.CharacterSheetResponse;
import com.an.character_app.character.dto.CreateCharacterRequest;
import com.an.character_app.character.service.PlayerCharacterService;
import com.an.character_app.exception.InvalidCharacterException;
import com.an.character_app.rules.model.Ability;
import com.an.character_app.rules.model.ArmorCategory;
import com.an.character_app.rules.model.ProficiencyLevel;
import com.an.character_app.rules.model.Skill;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlayerCharacterController.class)
class PlayerCharacterControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    PlayerCharacterService playerCharacterService;

    private CreateCharacterRequest validRequest() {
        return new CreateCharacterRequest(
                "Tom",
                1,
                new AbilityScoresRequest(15, 12, 14, 8, 10, 10),
                "fighter",
                "human",
                "soldier",
                Map.of(Ability.STR, 2, Ability.CON, 1),
                "plate",
                "shield",
                Set.of(Skill.ATHLETICS, Skill.INTIMIDATION));
    }

    private CharacterSheetResponse validResponse() {
        Map<Ability, AbilityBreakdown> abilities = Map.of(
                Ability.STR, new AbilityBreakdown(15, 2, 17, 3),
                Ability.DEX, new AbilityBreakdown(12, 0, 12, 1),
                Ability.CON, new AbilityBreakdown(14, 1, 15, 2),
                Ability.INT, new AbilityBreakdown(8, 0, 8, -1),
                Ability.WIS, new AbilityBreakdown(10, 0, 10, 0),
                Ability.CHA, new AbilityBreakdown(10, 0, 10, 0)
        );

        Map<Skill, Integer> skills = Map.ofEntries(
                Map.entry(Skill.ACROBATICS, 1),
                Map.entry(Skill.ANIMAL_HANDLING, 0),
                Map.entry(Skill.ARCANA, -1),
                Map.entry(Skill.ATHLETICS, 5),
                Map.entry(Skill.DECEPTION, 0),
                Map.entry(Skill.HISTORY, -1),
                Map.entry(Skill.INSIGHT, 0),
                Map.entry(Skill.INTIMIDATION, 2),
                Map.entry(Skill.INVESTIGATION, -1),
                Map.entry(Skill.MEDICINE, 0),
                Map.entry(Skill.NATURE, -1),
                Map.entry(Skill.PERCEPTION, 0),
                Map.entry(Skill.PERFORMANCE, 0),
                Map.entry(Skill.PERSUASION, 0),
                Map.entry(Skill.RELIGION, -1),
                Map.entry(Skill.SLEIGHT_OF_HAND, 1),
                Map.entry(Skill.STEALTH, 1),
                Map.entry(Skill.SURVIVAL, 0)
        );

        Map<Ability, Integer> savingThrows = Map.of(
                Ability.STR, 5,
                Ability.DEX, 1,
                Ability.CON, 4,
                Ability.INT, -1,
                Ability.WIS, 0,
                Ability.CHA, 0
        );

        CalculatedStats calculatedStats = new CalculatedStats(
                2,
                abilities,
                skills,
                savingThrows,
                12,
                20,
                1,
                10,
                10,
                9
        );

        return new CharacterSheetResponse(
                1L,
                "Tom",
                1,
                "Human",
                "Fighter",
                "Soldier",
                calculatedStats
        );
    }

    @Test
    void returnsSheet() throws Exception {
        CharacterInput characterInput = new CharacterInput(
                5,
                10,
                Map.of(Ability.STR, 15, Ability.DEX, 14, Ability.CON, 13,
                        Ability.INT, 12, Ability.WIS, 10, Ability.CHA, 8),
                Map.of(Ability.STR, 2, Ability.CON, 1),
                Map.of(Skill.ATHLETICS, ProficiencyLevel.PROFICIENT,
                        Skill.PERCEPTION, ProficiencyLevel.EXPERTISE),
                Set.of(Ability.STR, Ability.CON),
                ArmorCategory.HEAVY, 16, 0, 2);

        CalculatedStats stats = CharacterCalculator.calculate(characterInput);
        when(playerCharacterService.getCharacterSheet(1L)).thenReturn(
                new CharacterSheetResponse(
                        1L,
                        "Test",
                        5,
                        "Human",
                        "Fighter",
                        "Soldier",
                        stats));

        mockMvc.perform(get("/characters/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.calculatedStats.proficiencyBonus").value(3));

    }

    @Test
    void returns404WhenMissing() throws Exception {
        when(playerCharacterService.getCharacterSheet(99L)).thenThrow(new EntityNotFoundException("Character with id 99 does not exist."));

        mockMvc.perform(get("/characters/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Character with id 99 does not exist."));
    }

    @Test
    void createsCharacterValidRequestReturns201WithLocationAndBody() throws Exception {
        CreateCharacterRequest request = validRequest();
        CharacterSheetResponse response = validResponse();

        when(playerCharacterService.createCharacter(request)).thenReturn(response);

        mockMvc.perform(post("/characters")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/characters/1"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Tom"));
    }

    @Test
    void blankNameReturns400WithFieldError() throws Exception {
        CreateCharacterRequest badRequest = new CreateCharacterRequest(
                "",
                1,
                validRequest().baseAbilityScores(),
                "fighter",
                "human",
                "soldier",
                validRequest().backgroundAbilityBonuses(),
                null,
                null,
                null);

        mockMvc.perform(post("/characters")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists());

        verifyNoInteractions(playerCharacterService);
    }

    @Test
    void levelAboveTwentyReturns400() throws Exception {
        CreateCharacterRequest bad = new CreateCharacterRequest(
                "Tom", 21, validRequest().baseAbilityScores(),
                "fighter", "human", "soldier",
                validRequest().backgroundAbilityBonuses(), null, null, null);

        mockMvc.perform(post("/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.level").exists());
    }

    @Test
    void outOfRangeScoreReturns400WithNestedPath() throws Exception {
        CreateCharacterRequest bad = new CreateCharacterRequest(
                "Tom", 1, new AbilityScoresRequest(19, 12, 14, 8, 10, 10),
                "fighter", "human", "soldier",
                validRequest().backgroundAbilityBonuses(), null, null, null);

        mockMvc.perform(post("/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors['baseAbilityScores.strength']").exists());
    }

    @Test
    void bonusValueOfThreeReturns400() throws Exception {
        CreateCharacterRequest bad = new CreateCharacterRequest(
                "Tom", 1, validRequest().baseAbilityScores(),
                "fighter", "human", "soldier",
                Map.of(Ability.STR, 3), null, null, null);

        mockMvc.perform(post("/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void businessRuleViolationReturns400WithMessage() throws Exception {
        when(playerCharacterService.createCharacter(any()))
                .thenThrow(new InvalidCharacterException("Class with id: wizardz does not exist."));

        mockMvc.perform(post("/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Class with id: wizardz does not exist."));
    }

    @Test
    void malformedJsonReturns400() throws Exception {
        mockMvc.perform(post("/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ not json"))
                .andExpect(status().isBadRequest());
    }
}