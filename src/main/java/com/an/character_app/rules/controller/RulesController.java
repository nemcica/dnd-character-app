package com.an.character_app.rules.controller;

import com.an.character_app.rules.dto.ArmorResponse;
import com.an.character_app.rules.dto.BackgroundResponse;
import com.an.character_app.rules.dto.ClassResponse;
import com.an.character_app.rules.dto.SpeciesResponse;
import com.an.character_app.rules.mapper.RulesMapper;
import com.an.character_app.rules.repository.ArmorDefRepository;
import com.an.character_app.rules.repository.BackgroundDefRepository;
import com.an.character_app.rules.repository.ClassDefRepository;
import com.an.character_app.rules.repository.SpeciesDefRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rules")
@RequiredArgsConstructor
public class RulesController {

    private final ArmorDefRepository armorDefRepository;
    private final SpeciesDefRepository speciesDefRepository;
    private final BackgroundDefRepository backgroundDefRepository;
    private final ClassDefRepository classDefRepository;
    private final RulesMapper rulesMapper;


    @GetMapping("/classes")
    public List<ClassResponse> getAllClasses() {
        return classDefRepository.findAll().stream().map(rulesMapper::toResponse).toList();
    }

    @GetMapping("/species")
    public List<SpeciesResponse> getAllSpecies() {
        return speciesDefRepository.findAll().stream().map(rulesMapper::toResponse).toList();
    }

    @GetMapping("/backgrounds")
    public List<BackgroundResponse> getAllBackgrounds() {
        return backgroundDefRepository.findAll().stream().map(rulesMapper::toResponse).toList();
    }

    @GetMapping("/armor")
    public List<ArmorResponse> getAllArmor() {
        return armorDefRepository.findAll().stream().map(rulesMapper::toResponse).toList();
    }
}
