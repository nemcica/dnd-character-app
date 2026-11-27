package com.an.character_app;

import com.an.character_app.character.repository.PlayerCharacterRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "app.demo-data.enabled=false")
@AutoConfigureMockMvc
@Testcontainers
public class CharacterApiIntegrationTest {

    private static final String VALID_BODY = """
            {
              "name": "Tom",
              "level": 5,
              "baseAbilityScores": {
                "strength": 15, "dexterity": 14, "constitution": 13,
                "intelligence": 12, "wisdom": 10, "charisma": 8
              },
              "classId": "fighter",
              "speciesId": "human",
              "backgroundId": "soldier",
              "backgroundAbilityBonuses": { "STR": 2, "CON": 1 },
              "armorId": "chain-mail",
              "classSkills": ["ANIMAL_HANDLING", "PERCEPTION"],
              "backgroundTool": "DICE"
            }
            """;

    private static final String ROGUE_CRIMINAL_BODY = """
            {
              "name": "Sly",
              "level": 3,
              "baseAbilityScores": {
                "strength": 10, "dexterity": 15, "constitution": 14,
                "intelligence": 12, "wisdom": 10, "charisma": 8
              },
              "classId": "rogue",
              "speciesId": "human",
              "backgroundId": "criminal",
              "backgroundAbilityBonuses": { "DEX": 2, "CON": 1 },
              "classSkills": ["ACROBATICS", "ATHLETICS", "DECEPTION", "INSIGHT"]
            }
            """;

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16");
    @Autowired
    MockMvc mockMvc;
    @Autowired
    PlayerCharacterRepository playerCharacterRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @AfterEach
    void cleanUp() {
        playerCharacterRepository.deleteAll();
    }

    @Test
    void createsCharacterThenFetchesItWithCorrectSheet() throws Exception {
        MvcResult created = mockMvc.perform(post("/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        String location = created.getResponse().getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tom"))
                .andExpect(jsonPath("$.className").value("Fighter"))
                .andExpect(jsonPath("$.backgroundName").value("Soldier"))
                .andExpect(jsonPath("$.calculatedStats.proficiencyBonus").value(3))
                .andExpect(jsonPath("$.calculatedStats.abilities.STR.total").value(17))
                .andExpect(jsonPath("$.calculatedStats.abilities.STR.modifier").value(3))
                .andExpect(jsonPath("$.calculatedStats.abilities.CON.total").value(14))
                .andExpect(jsonPath("$.calculatedStats.maxHp").value(44))
                .andExpect(jsonPath("$.calculatedStats.armorClass").value(16))
                .andExpect(jsonPath("$.calculatedStats.skills.ATHLETICS").value(6))
                .andExpect(jsonPath("$.calculatedStats.skills.PERCEPTION").value(3))
                .andExpect(jsonPath("$.calculatedStats.skills.STEALTH").value(2))
                .andExpect(jsonPath("$.calculatedStats.savingThrows.STR").value(6))
                .andExpect(jsonPath("$.calculatedStats.savingThrows.CON").value(5))
                .andExpect(jsonPath("$.calculatedStats.savingThrows.DEX").value(2));
    }

    @Test
    void unknownClassReturns400AndSavesNothing() throws Exception {
        String body = VALID_BODY.replace("\"fighter\"", "\"wizardz\"");

        mockMvc.perform(post("/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(containsString("wizardz")));

        assertThat(playerCharacterRepository.count()).isZero();
    }

    @Test
    void invalidBodyReturns400WithFieldErrors() throws Exception {
        String body = VALID_BODY.replace("\"level\": 5", "\"level\": 21");

        mockMvc.perform(post("/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.level").exists());

        assertThat(playerCharacterRepository.count()).isZero();
    }

    @Test
    void missingCharacterReturns404() throws Exception {
        mockMvc.perform(get("/characters/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listIsPaginatedAndDeleteRemovesCharacter() throws Exception {
        for (int i = 0; i < 2; i++) {
            mockMvc.perform(post("/characters")
                            .contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(get("/characters?size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.page.totalElements").value(2))
                .andExpect(jsonPath("$.page.totalPages").value(2));

        Long id = playerCharacterRepository.findAll().getFirst().getId();
        mockMvc.perform(delete("/characters/" + id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/characters/" + id)).andExpect(status().isNotFound());
        assertThat(playerCharacterRepository.count()).isEqualTo(1);
    }

    @Test
    void soldierWithChosenGamingSetShowsToolOnSheet() throws Exception {
        MvcResult created = mockMvc.perform(post("/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(get(created.getResponse().getHeader("Location")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.toolProficiencies.length()").value(1))
                .andExpect(jsonPath("$.toolProficiencies[0]").value("DICE"));
    }

    @Test
    void soldierWithoutToolIsRejectedAndSavesNothing() throws Exception {
        String body = VALID_BODY.replace(",\n" +
                "  \"backgroundTool\": \"DICE\"", "");

        mockMvc.perform(post("/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(containsString("requires you to select")));

        assertThat(playerCharacterRepository.count()).isZero();
    }

    @Test
    void acolyteGetsFixedToolWithoutSendingOne() throws Exception {
        String body = VALID_BODY
                .replace("\"backgroundId\": \"soldier\"", "\"backgroundId\": \"acolyte\"")
                .replace("{ \"STR\": 2, \"CON\": 1 }", "{ \"WIS\": 2, \"INT\": 1 }")
                .replace("\"backgroundTool\": \"DICE\"", "\"backgroundTool\": null");

        MvcResult created = mockMvc.perform(post("/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(get(created.getResponse().getHeader("Location")))
                .andExpect(jsonPath("$.toolProficiencies[0]").value("CALLIGRAPHERS_SUPPLIES"));
    }

    @Test
    void rogueWithCriminalBackgroundStoresThievesToolsTwiceButListsItOnce() throws Exception {
        MvcResult created = mockMvc.perform(post("/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ROGUE_CRIMINAL_BODY))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(get(created.getResponse().getHeader("Location")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.toolProficiencies.length()").value(1))
                .andExpect(jsonPath("$.toolProficiencies[0]").value("THIEVES_TOOLS"));

        Integer rows = jdbcTemplate.queryForObject(
                "select count(*) from tool_proficiency_grant where tool = 'THIEVES_TOOLS'", Integer.class);
        assertThat(rows).isEqualTo(2);
    }
}
