package com.anem.character_app.background;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BackgroundRepository extends JpaRepository<Background, String>, JpaSpecificationExecutor<Background> {

}
