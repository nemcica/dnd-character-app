package com.anem.character_app.util;

//Implement MapStruct Eventually
public interface Mapper<Entity, Dto> {

    Entity toEntity(Dto dto);

    Dto toDto(Entity entity);
}

