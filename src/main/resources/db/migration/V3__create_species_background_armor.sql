create table species_def (
    id                          varchar(50)  primary key,
    name                        varchar(100) not null,
    walk_speed                  int          not null,
    creature_size_description   varchar(200) not null,
    source                      varchar(20)  not null
);

create table species_def_sizes (
    species_def_id varchar(50) not null references species_def(id),
    creature_size  varchar(10) not null,
    primary key (species_def_id, creature_size)
);

create table background_def (
    id     varchar(50)  primary key,
    name   varchar(100) not null,
    source varchar(20)  not null
);

create table background_def_ability_options (
    background_def_id varchar(50) not null references background_def(id),
    ability           varchar(10) not null,
    primary key (background_def_id, ability)
);

create table background_def_skills (
    background_def_id varchar(50) not null references background_def(id),
    skill             varchar(30) not null,
    primary key (background_def_id, skill)
);

create table armor_def (
    id       varchar(50)  primary key,
    name     varchar(100) not null,
    category varchar(10)  not null,
    base_ac  int          not null,
    dex_cap  int,
    source   varchar(20)  not null
);

insert into species_def (id, name, walk_speed, creature_size_description, source) values
    ('human', 'Human', 30, '5 to 7 feet', 'PHB2024'),
    ('elf',   'Elf',   30, '6 to 8 feet', 'PHB2024'),
    ('dwarf', 'Dwarf', 30, '3 to 5 feet', 'PHB2024');

insert into species_def_sizes (species_def_id, creature_size) values
    ('human', 'MEDIUM'), ('human', 'SMALL'),
    ('elf', 'MEDIUM'), ('dwarf', 'MEDIUM');

insert into background_def (id, name, source) values
    ('soldier',  'Soldier',  'PHB2024'),
    ('acolyte',  'Acolyte',  'PHB2024'),
    ('criminal', 'Criminal', 'PHB2024');

insert into background_def_ability_options (background_def_id, ability) values
    ('soldier', 'STR'), ('soldier', 'DEX'), ('soldier', 'CON'),
    ('acolyte', 'INT'), ('acolyte', 'WIS'), ('acolyte', 'CHA'),
    ('criminal', 'DEX'), ('criminal', 'CON'), ('criminal', 'INT');

insert into background_def_skills (background_def_id, skill) values
    ('soldier', 'ATHLETICS'), ('soldier', 'INTIMIDATION'),
    ('acolyte', 'INSIGHT'), ('acolyte', 'RELIGION'),
    ('criminal', 'SLEIGHT_OF_HAND'), ('criminal', 'STEALTH');

insert into armor_def (id, name, category, base_ac, dex_cap, source) values
    ('leather',     'Leather Armor', 'LIGHT',  11, null, 'PHB2024'),
    ('chain-shirt', 'Chain Shirt',   'MEDIUM', 13, 2,    'PHB2024'),
    ('chain-mail',  'Chain Mail',    'HEAVY',  16, 0,    'PHB2024'),
    ('plate',       'Plate Armor',   'HEAVY',  18, 0,    'PHB2024');