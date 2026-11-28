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

create table class_def_fixed_tool (
    class_def_id varchar(50) not null references class_def(id),
    tool varchar(40) not null,
    primary key(class_def_id, tool)
);

create table class_def_tool_category (
    class_def_id varchar(50) not null references class_def(id),
    tool_category varchar(40) not null,
    primary key(class_def_id, tool_category)
);

alter table class_def add column tool_choice_count int not null default(0);

insert into class_def_fixed_tool(class_def_id, tool)
values ('rogue', 'THIEVES_TOOLS');

