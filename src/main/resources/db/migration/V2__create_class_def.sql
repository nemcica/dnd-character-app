create table class_def (
    id        varchar(50)  primary key,
    name      varchar(100) not null,
    hit_die   int          not null,
    source    varchar(20)  not null
);

create table class_def_saving_throws (
    class_def_id varchar(50) not null references class_def(id),
    saving_throw varchar(10) not null,
    primary key (class_def_id, saving_throw)
);

insert into class_def (id, name, hit_die, source) values
    ('fighter', 'Fighter', 10, 'PHB2024'),
    ('wizard',  'Wizard',    6, 'PHB2024'),
    ('rogue',   'Rogue',     8, 'PHB2024');

insert into class_def_saving_throws (class_def_id, saving_throw) values
    ('fighter', 'STR'), ('fighter', 'CON'),
    ('wizard',  'INT'), ('wizard',  'WIS'),
    ('rogue',   'DEX'), ('rogue',   'INT');