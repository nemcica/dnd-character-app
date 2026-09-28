package com.anem.character_app.subClass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubClassRepository extends JpaRepository<SubClass, String>, JpaSpecificationExecutor<SubClass> {
}
