package com.an.character_app.character.service;


import com.an.character_app.calc.CalculatedStats;
import com.an.character_app.calc.CharacterCalculator;
import com.an.character_app.calc.CharacterInput;
import com.an.character_app.calc.CharacterInputMapper;
import com.an.character_app.character.dto.CharacterSheetResponse;
import com.an.character_app.character.model.PlayerCharacter;
import com.an.character_app.character.repository.PlayerCharacterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlayerCharacterServiceImpl implements PlayerCharacterService{

    private final PlayerCharacterRepository playerCharacterRepository;

    @Override
    public CharacterSheetResponse getCharacterSheet(Long id) {
        PlayerCharacter foundCharacter = playerCharacterRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Character with id: " + id + " does not exist."));
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
}
