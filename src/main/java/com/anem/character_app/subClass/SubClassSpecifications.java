package com.anem.character_app.subClass;

import com.anem.character_app.subClass.dto.SubClassSearchRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SubClassSpecifications {
    public static Specification<SubClass> withFilters(SubClassSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.name() != null && !request.name().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.name() + "%"));
            }

            if (request.description() != null && !request.description().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + request.description() + "%"));
            }

            if (request.baseClass() != null) {
                predicates.add(criteriaBuilder.equal(root.get("baseClass"), request.baseClass()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
