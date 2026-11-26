create table class_def_skill_options (
    class_def_id varchar(50) not null references class_def(id),
    skill        varchar(20) not null,
    primary key(class_def_id, skill)
);

alter table class_def add column skill_choice_count int not null default(2);

update class_def
set skill_choice_count = 4
where id = 'rogue';

insert into class_def_skill_options(class_def_id, skill)
values
    ('fighter','ACROBATICS'), ('fighter','ANIMAL_HANDLING'), ('fighter','ATHLETICS'),
    ('fighter','HISTORY'), ('fighter','INSIGHT'), ('fighter','INTIMIDATION'),
    ('fighter','PERSUASION'), ('fighter','PERCEPTION'), ('fighter','SURVIVAL'),

    ('wizard', 'ARCANA'), ('wizard', 'HISTORY'), ('wizard', 'INSIGHT'),
    ('wizard', 'INVESTIGATION'), ('wizard', 'MEDICINE'), ('wizard', 'NATURE'),
    ('wizard', 'RELIGION'),

    ('rogue', 'ACROBATICS'), ('rogue', 'ATHLETICS'), ('rogue', 'DECEPTION'),
    ('rogue', 'INSIGHT'), ('rogue', 'INTIMIDATION'), ('rogue', 'INVESTIGATION'),
    ('rogue', 'PERCEPTION'), ('rogue', 'PERSUASION'), ('rogue', 'SLEIGHT_OF_HAND'),
    ('rogue', 'STEALTH');
