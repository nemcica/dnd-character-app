package com.anem.character_app.subClass;

import com.anem.character_app.baseClass.BaseClass;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubClass {

    @Id
    String id;

    @Column(nullable = false)
    String name;

    @Column(columnDefinition = "TEXT")
    String description;

    @ManyToOne
    @JoinColumn(name = "baseClass_id", nullable = false)
    BaseClass baseClass;

}
