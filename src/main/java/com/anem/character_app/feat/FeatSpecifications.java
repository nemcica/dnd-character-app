package com.anem.character_app.feat;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class FeatSpecifications {

    public static Specification<Feat> withFilters(String name, FeatTag featTag) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(name != null && !name.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if(featTag != null) {
                predicates.add(criteriaBuilder.equal(root.get("featTag"), featTag));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
