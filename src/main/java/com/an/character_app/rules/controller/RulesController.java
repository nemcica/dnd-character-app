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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Rules", description = "View static dnd rules (classes, species, etc.)")
@RestController
@RequestMapping("/rules")
@RequiredArgsConstructor
public class RulesController {

    private final ArmorDefRepository armorDefRepository;
    private final SpeciesDefRepository speciesDefRepository;
    private final BackgroundDefRepository backgroundDefRepository;
    private final ClassDefRepository classDefRepository;
    private final RulesMapper rulesMapper;


    @Operation(summary = "Get a list of classes.")
    @GetMapping("/classes")
    public List<ClassResponse> getAllClasses() {
        return classDefRepository.findAll().stream().map(rulesMapper::toResponse).toList();
    }

    @Operation(summary = "Get a list of species.")
    @GetMapping("/species")
    public List<SpeciesResponse> getAllSpecies() {
        return speciesDefRepository.findAll().stream().map(rulesMapper::toResponse).toList();
    }

    @Operation(summary = "Get a list of backgrounds.")
    @GetMapping("/backgrounds")
    public List<BackgroundResponse> getAllBackgrounds() {
        return backgroundDefRepository.findAll().stream().map(rulesMapper::toResponse).toList();
    }

    @Operation(summary = "Get a list of armor.")
    @GetMapping("/armor")
    public List<ArmorResponse> getAllArmor() {
        return armorDefRepository.findAll().stream().map(rulesMapper::toResponse).toList();
    }
}
