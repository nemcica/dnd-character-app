package com.anem.character_app.baseClass;

import com.anem.character_app.baseClass.dto.BaseClassSearchRequest;
import com.anem.character_app.generic.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BaseClassSpecifications {
    public static Specification<BaseClass> withFilters(BaseClassSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.name() != null && !request.name().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.name() + "%"));
            }

            if (request.description() != null && !request.description().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + request.description() + "%"));
            }

            if (request.hitDie() != null && request.hitDie() > 0) {
                predicates.add(criteriaBuilder.equal(root.get("hitDie"), request.hitDie()));
            }

            if (request.armorProficiency() != null && !request.armorProficiency().isEmpty()) {
                query.distinct(true);
                Join<BaseClass, ArmorCategory> armorJoin = root.join("armorProficiency");
                predicates.add(armorJoin.in(request.armorProficiency()));
            }

            if (request.weaponProficiency() != null && !request.weaponProficiency().isEmpty()) {
                query.distinct(true);
                Join<BaseClass, WeaponCategory> weaponJoin = root.join("weaponProficiency");
                predicates.add(weaponJoin.in(request.weaponProficiency()));
            }

            if (request.spellcasterType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("spellcasterType"), request.spellcasterType()));
            }

            if (request.availableSkillProficiencies() != null && !request.availableSkillProficiencies().isEmpty()) {
                query.distinct(true);
                Join<BaseClass, Skill> skillJoin = root.join("availableSkillProficiencies");
                predicates.add(skillJoin.in(request.availableSkillProficiencies()));
            }

            if (request.availableSkillProficiencies() != null && !request.availableSkillProficiencies().isEmpty()) {
                query.distinct(true);
                Join<BaseClass, AbilityScore> savingThrowJoin = root.join("savingThrowProficiency");
                predicates.add(savingThrowJoin.in(request.availableSkillProficiencies()));
            }

            if (request.equipment() != null && !request.equipment().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("equipment")), "%" + request.equipment() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
