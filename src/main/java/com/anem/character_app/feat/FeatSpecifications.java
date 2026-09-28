package com.anem.character_app.feat;

import com.anem.character_app.feat.dto.FeatSearchRequest;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class FeatSpecifications {

    public static Specification<Feat> withFilters(FeatSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(request.name() != null && !request.name().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.name().toLowerCase() + "%"));
            }

            if(request.description() != null && !request.description().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + request.description() + "%"));
            }

            if(request.featTag() != null) {
                predicates.add(criteriaBuilder.equal(root.get("featTag"), request.featTag()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
