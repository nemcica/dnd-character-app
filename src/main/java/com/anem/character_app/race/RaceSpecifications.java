package com.anem.character_app.race;

import com.anem.character_app.race.dto.RaceSearchRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RaceSpecifications {

    public static Specification<Race> withFilters(RaceSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(request.name() != null && !request.name().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.name().toLowerCase() + "%"));
            }

            if(request.size() != null) {
                predicates.add(criteriaBuilder.equal(root.get("creatureSize"), request.size()));
            }

            if(request.speed() != null && !request.speed().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("speed")), "%" + request.speed().toLowerCase() + "%"));
            }

            if(request.specialTraits() != null && !request.specialTraits().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("specialTraits")), "%" + request.specialTraits().toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
