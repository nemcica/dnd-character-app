create table class_def_armor_training (
    class_def_id    varchar(50)     not null references class_def(id),
    armor_category  varchar(10)     not null,
    primary key(class_def_id, armor_category)
);

insert into class_def_armor_training(class_def_id, armor_category)
values
    ('fighter', 'LIGHT'), ('fighter', 'MEDIUM'),
    ('fighter', 'HEAVY'), ('fighter', 'SHIELD'),
    ('rogue', 'LIGHT');

create table armor_training_grant (
    id                  bigint          generated always as identity primary key,
    player_character_id bigint          not null references player_character(id) on delete cascade,
    armor_category      varchar(10)     not null,
    source              varchar(100)    not null
);

create index idx_armor_training_grant_character on armor_training_grant(player_character_id);
