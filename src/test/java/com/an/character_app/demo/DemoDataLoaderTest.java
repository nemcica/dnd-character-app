package com.an.character_app.demo;

import com.an.character_app.character.repository.PlayerCharacterRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "app.demo-data.enabled=true")
@Testcontainers
class DemoDataLoaderTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16");

    @Autowired
    PlayerCharacterRepository characterRepository;
    @Autowired
    DemoDataLoader loader;

    @Test
    void createsDemoCharactersOnStartupAndDoesNotDuplicateThem() {
        assertThat(characterRepository.count()).isEqualTo(2);

        loader.run(null);

        assertThat(characterRepository.count()).isEqualTo(2);
    }
}