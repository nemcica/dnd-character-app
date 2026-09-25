package com.anem.character_app.race;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Race {
    @Id
    String id;

    @Column(nullable = false, unique = true)
    String name;

    @Column(nullable = false)
    String creatureType;

    @Column(nullable = false)
    String size;

    @Column(nullable = false)
    String speed;

    @Column(columnDefinition = "TEXT")
    String specialTraits;
}
