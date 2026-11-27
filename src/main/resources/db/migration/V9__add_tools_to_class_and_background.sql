alter table background_def add column tool varchar(40);
alter table background_def add column tool_category varchar(40);

update background_def
set tool = 'CALLIGRAPHERS_SUPPLIES'
where id = 'acolyte';
update background_def
set tool = 'THIEVES_TOOLS'
where id = 'criminal';
update background_def
set tool_category = 'GAMING_SET'
where id = 'soldier';

alter table background_def add constraint ch_background_def_tool_exclusive
check((tool is not null) <> (tool_category is not null));

create table tool_proficiency_grant (
    id                  bigint          generated always as identity primary key,
    player_character_id bigint          not null references player_character(id) on delete cascade,
    tool                varchar(40)     not null,
    source              varchar(100)    not null
);

create index idx_tool_tool_proficiency_grant_character on tool_proficiency_grant(player_character_id);
