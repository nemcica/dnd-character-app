package com.anem.character_app.race;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RaceSpecifications {

    public static Specification<Race> withFilters(String name, String size, String speed, String specialTraits) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(name != null && !name.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if(size != null && !size.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("size")), "%" + size.toLowerCase() + "%"));
            }

            if(speed != null && !speed.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("speed")), "%" + speed.toLowerCase() + "%"));
            }

            if(specialTraits != null && !specialTraits.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("specialTraits")), "%" + specialTraits.toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
