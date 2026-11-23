create table skill_proficiency_grant(
    id                  bigint          generated always as identity primary key,
    player_character_id bigint          not null references player_character(id) on delete cascade,
    skill               varchar(30)     not null,
    proficiency_level   varchar(20)     not null,
    source              varchar(100)    not null
);

create index idx_skill_proficiency_grant_character on skill_proficiency_grant(player_character_id);