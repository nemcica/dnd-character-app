create table ability_score_modifier (
    id                  bigint       generated always as identity primary key,
    player_character_id bigint       not null references player_character(id) on delete cascade,
    ability             varchar(10)  not null,
    amount              int          not null,
    source              varchar(100) not null
);

create index idx_ability_modifier_character on ability_score_modifier(player_character_id);