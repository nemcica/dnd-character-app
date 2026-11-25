alter table armor_def add column min_str_required int;
alter table armor_def add column stealth_disadvantage boolean  not null default(false);

update armor_def
set min_str_required = 13, stealth_disadvantage = true
where id = 'chain-mail';

update armor_def
set min_str_required = 15, stealth_disadvantage = true
where id = 'plate';

insert into armor_def(id, name, category, base_ac, dex_cap, min_str_required, stealth_disadvantage, source)
values
-- Light Armor
    ('padded', 'Padded Armor', 'LIGHT', 11, null, null, true, 'PHB2024'),
    ('studded-leather', 'Studded Leather Armor', 'LIGHT', 12, null, null, false, 'PHB2024'),

    -- Medium Armor
    ('hide', 'Hide Armor', 'MEDIUM', 12, 2, null, false, 'PHB2024'),
    ('scale-mail', 'Scale Mail', 'MEDIUM', 14, 2, null, true, 'PHB2024'),
    ('breastplate', 'Breastplate', 'MEDIUM', 14, 2, null, false, 'PHB2024'),
    ('half-plate', 'Half Plate Armor', 'MEDIUM', 15, 2, null, true, 'PHB2024'),

    -- Heavy Armor
    ('ring-mail', 'Ring Mail', 'HEAVY', 14, 0, null, true, 'PHB2024'),
    ('splint', 'Splint Armor', 'HEAVY', 17, 0, 15, true, 'PHB2024'),

    -- Shield
    ('shield', 'Shield', 'SHIELD', 2, null, null, false, 'PHB2024');
