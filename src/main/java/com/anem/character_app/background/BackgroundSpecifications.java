package com.anem.character_app.background;

import com.anem.character_app.background.dto.BackgroundSearchRequest;
import com.anem.character_app.generic.AbilityScore;
import com.anem.character_app.generic.Skill;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BackgroundSpecifications {

    public static Specification<Background> withFilters(BackgroundSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.name() != null && !request.name().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.name() + "%"));
            }
            if (request.description() != null && !request.description().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + request.description() + "%"));
            }
            if (request.abilityScores() != null && !request.abilityScores().isEmpty()) {
                query.distinct(true);
                Join<Background, AbilityScore> abilityScoreJoin = root.join("abilityScores");
                predicates.add(abilityScoreJoin.in(request.abilityScores()));
            }
            if (request.feat() != null) {
                predicates.add(criteriaBuilder.equal(root.get("feat"), request.feat()));
            }
            if (request.featTag() != null) {
                predicates.add(criteriaBuilder.equal(root.get("featTag"), request.featTag()));
            }
            if (request.skillProficiencies() != null && !request.skillProficiencies().isEmpty()) {
                query.distinct(true);
                Join<Background, Skill> skillJoin = root.join("skillProficiencies");
                predicates.add(skillJoin.in(request.skillProficiencies()));
            }
            if (request.toolProficiencies() != null && !request.toolProficiencies().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("toolProficiencies")), "%" + request.toolProficiencies() + "%"));
            }
            if (request.equipment() != null && !request.equipment().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("equipment")), "%" + request.equipment() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
