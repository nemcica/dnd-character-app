# D&D 2024 Character Manager API
![CI](https://github.com/nemcica/dnd-character-app/actions/workflows/ci.yml/badge.svg)

A REST API for creating and viewing Dungeons & Dragons (2024 rules) characters.
You choose a class, species, background and ability scores; the API validates the
choices against the rules data and returns a computed character sheet: ability
modifiers, skills, saving throws, proficiency bonus, HP, AC and passive scores.

**Stack:** Java 21, Spring Boot 4, Spring Data JPA, PostgreSQL, Flyway,
MapStruct, springdoc (Swagger UI), JUnit 5, Mockito, AssertJ, Testcontainers.

## Running it

Prerequisites: Java 21, Maven, and Docker running.

```bash
mvn spring-boot:run
```

PostgreSQL starts automatically through Spring Boot's Docker Compose support.
Open **http://localhost:2072/swagger-ui.html** to try the endpoints.
Two demo characters are created on first launch (disable with `app.demo-data.enabled: false`).

Run the tests (Docker must be running, the integration tests start a real PostgreSQL):

```bash
mvn verify
```

## Endpoints

| Method | Path                 | Description                                              |
|--------|----------------------|----------------------------------------------------------|
| GET    | `/rules/classes`     | Classes (hit die, saving throw proficiencies)            |
| GET    | `/rules/species`     | Species (speed, sizes)                                   |
| GET    | `/rules/backgrounds` | Backgrounds (ability options, skills)                    |
| GET    | `/rules/armor`       | Armor and shields                                        |
| POST   | `/characters`        | Create a character, returns `201` and the computed sheet |
| GET    | `/characters/{id}`   | Character sheet                                          |
| GET    | `/characters`        | Paginated list (`?page=0&size=20&sort=name`)             |
| DELETE | `/characters/{id}`   | Delete a character                                       |

### Example

`POST /characters`

```json
{
  "name": "Tom",
  "level": 5,
  "baseAbilityScores": {
    "strength": 15,
    "dexterity": 14,
    "constitution": 13,
    "intelligence": 12,
    "wisdom": 10,
    "charisma": 8
  },
  "classId": "fighter",
  "speciesId": "human",
  "backgroundId": "soldier",
  "backgroundAbilityBonuses": {
    "STR": 2,
    "CON": 1
  },
  "armorId": "chain-mail",
  "classSkills": [
    "PERCEPTION"
  ]
}
```

Response (abbreviated):

```json
{
  "id": 2,
  "name": "Tom",
  "level": 5,
  "speciesName": "Human",
  "className": "Fighter",
  "backgroundName": "Soldier",
  "calculatedStats": {
    "proficiencyBonus": 3,
    "abilities": {
      "STR": {
        "base": 15,
        "bonus": 2,
        "total": 17,
        "modifier": 3
      }
    },
    "skills": {
      "ATHLETICS": 6,
      "ACROBATICS": 2,
      "SLEIGHT_OF_HAND": 2
    },
    "savingThrows": {
      "STR": 6,
      "DEX": 2
    },
    "maxHp": 44,
    "armorClass": 16,
    "initiative": 2,
    "passivePerception": 13
  }
}
```

Errors use the standard `ProblemDetail` format. Invalid input returns `400` with
per-field messages; unknown ids return `404`.

## Architecture

```
com.an.character_app
├── rules/        reference data (classes, species, backgrounds, armor): read-only entities, DTOs, controller
├── character/    player data: entities, repository, service, controller, input mapper
├── calc/         pure Java calculations: no Spring, no JPA
└── exception/    custom exceptions and the global handler
```

Dependency rule: `rules` depends on nothing, `calc` depends on nothing, and
`character` depends on both. `calc` never sees a JPA entity; the `character` layer
converts an entity into a plain `CharacterInput` record first.

## Design decisions

- **Totals are computed, never stored.** The database holds base ability scores and a
  list of bonus rows (each with a source such as `BACKGROUND:soldier`). Totals,
  modifiers, skills, HP and AC are calculated on every read, so changing a
  background or removing a bonus can't leave stale numbers behind.
- **A pure, unit-tested calculator.** All formulas are static functions in `calc`,
  tested with edge cases (floor division for negative modifiers, DEX caps by armor
  type, level thresholds for the proficiency bonus, HP with a negative CON modifier).
- **Entities and DTOs are separate.** The API shape can change without touching the
  schema, and lazy relations never leak into JSON.
- **Rules data lives in the database with stable slug ids** (`fighter`, `soldier`),
  so adding a book means adding rows, not changing code. Each row has a `source` column.
- **Sparse proficiency storage.** Only proficiencies a character actually has are stored.
  Background skills are derived from the background; player-chosen skills are stored
  as grants. When two sources overlap, the highest level wins.
- **Schema is versioned with Flyway**, and Hibernate only validates it.
- **Validation in two layers.** Bean Validation checks request shape (ranges, blanks);
  the service checks rules that need the database (ids exist, armor and shield are in
  the right slots, background bonuses are legal).
- **Tests at every layer.** Unit tests for formulas and mappers, Mockito tests for
  the service, `MockMvc` tests for the controller, repository tests and an end-to-end
  test against a real PostgreSQL via Testcontainers.

## Not implemented yet

- Validation of class skill choices (the class's skill list and pick count)
- Multiclassing (the HP calculation is already written to support it)
- Feats, spells, equipment and inventory, features and class abilities
- Updating a character (`PUT`)
- Authentication and per-user ownership of characters
- [Add anything else you've deliberately left out]

## Roadmap

1. Full 2024 PHB classes, species and backgrounds as seed data
2. Class skill options and validation
3. Origin feats from backgrounds
4. Equipment and inventory
5. Spell lists
