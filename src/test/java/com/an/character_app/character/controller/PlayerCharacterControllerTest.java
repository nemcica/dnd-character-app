package com.an.character_app.character.controller;

import com.an.character_app.calc.CalculatedStats;
import com.an.character_app.calc.CharacterCalculator;
import com.an.character_app.calc.CharacterInput;
import com.an.character_app.character.dto.CharacterSheetResponse;
import com.an.character_app.character.service.PlayerCharacterService;
import com.an.character_app.rules.model.Ability;
import com.an.character_app.rules.model.ArmorCategory;
import com.an.character_app.rules.model.ProficiencyLevel;
import com.an.character_app.rules.model.Skill;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerCharacterController.class)
class PlayerCharacterControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PlayerCharacterService playerCharacterService;

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
}