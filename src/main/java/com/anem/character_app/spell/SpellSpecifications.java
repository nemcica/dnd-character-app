package com.anem.character_app.spell;

import com.anem.character_app.spell.dto.SpellSearchRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpellSpecifications {
    public static Specification<Spell> withFilters(SpellSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(request.name() != null && !request.name().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.name() + "%"));
            }

            if(request.spellSchool() != null) {
                predicates.add(criteriaBuilder.equal(root.get("spellSchool"), request.spellSchool()));
            }

            if(request.spellLevel() != null && request.spellLevel() >= 0) {
                predicates.add(criteriaBuilder.equal(root.get("spellLevel"), request.spellLevel()));
            }

            if(request.castingTime() != null && !request.castingTime().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("castingTime")), "%" + request.castingTime() + "%"));
            }

            if(request.duration() != null && !request.duration().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("duration")), "%" + request.duration() + "%"));
            }

            if(request.components() != null && !request.components().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("components")), "%" + request.components() + "%"));
            }

            if(request.range() != null && !request.range().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("range")), "%" + request.range() + "%"));
            }

            if(request.description() != null && !request.description().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + request.description() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
