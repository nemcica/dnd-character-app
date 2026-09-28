package com.anem.character_app.baseClass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseClassRepository extends JpaRepository<BaseClass, String>, JpaSpecificationExecutor<BaseClass> {
}
