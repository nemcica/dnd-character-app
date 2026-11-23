create table player_character (
    id            bigint       generated always as identity primary key,
    name          varchar(100) not null,
    level         int          not null,
    strength      int          not null,
    dexterity     int          not null,
    constitution  int          not null,
    intelligence  int          not null,
    wisdom        int          not null,
    charisma      int          not null,
    class_id      varchar(50)  not null references class_def(id),
    species_id    varchar(50)  not null references species_def(id),
    background_id varchar(50)  not null references background_def(id),
    armor_id      varchar(50)  references armor_def(id),
    shield_id     varchar(50)  references armor_def(id)
);