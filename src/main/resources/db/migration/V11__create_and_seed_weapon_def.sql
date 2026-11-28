create table weapon_def(
    id                      varchar(50)     primary key,
    name                    varchar(100)    not null,
    category                varchar(30)     not null,
    melee_or_ranged         varchar(10)     not null,
    damage_dice             varchar(10)     not null,
    damage_type             varchar(20)     not null,
    versatile_damage_dice   varchar(10),
    weapon_range            varchar(30),
    weapon_mastery          varchar(30)     not null,
    source                  varchar(100)    not null
);

alter table weapon_def
    add constraint chk_damage_dice_format
        check (damage_dice ~ '^(\d+d\d+|1)$'),
    add constraint chk_versatile_dice_format
        check (versatile_damage_dice ~ '^\d+d\d+$'),
    add constraint chk_weapon_range_format
        check (weapon_range ~ '^\d+/\d+$');

create table weapon_property(
    weapon_id   varchar(50)     not null references weapon_def(id),
    property    varchar(30)     not null,
    primary key(weapon_id, property)
);

-- ==========================================
-- SIMPLE MELEE WEAPONS
-- ==========================================

insert into weapon_def (id, name, category, melee_or_ranged, damage_dice, damage_type, versatile_damage_dice, weapon_range, weapon_mastery, source) values
('club', 'Club', 'SIMPLE', 'MELEE', '1d4', 'BLUDGEONING', null, null, 'SLOW', 'PHB2024'),
('dagger', 'Dagger', 'SIMPLE', 'MELEE', '1d4', 'PIERCING', null, '20/60', 'NICK', 'PHB2024'),
('greatclub', 'Greatclub', 'SIMPLE', 'MELEE', '1d8', 'BLUDGEONING', null, null, 'PUSH', 'PHB2024'),
('handaxe', 'Handaxe', 'SIMPLE', 'MELEE', '1d6', 'SLASHING', null, '20/60', 'VEX', 'PHB2024'),
('javelin', 'Javelin', 'SIMPLE', 'MELEE', '1d6', 'PIERCING', null, '30/120', 'SLOW', 'PHB2024'),
('light_hammer', 'Light Hammer', 'SIMPLE', 'MELEE', '1d4', 'BLUDGEONING', null, '20/60', 'NICK', 'PHB2024'),
('mace', 'Mace', 'SIMPLE', 'MELEE', '1d6', 'BLUDGEONING', null, null, 'SAP', 'PHB2024'),
('pitchfork', 'Pitchfork', 'SIMPLE', 'MELEE', '1d6', 'PIERCING', null, null, 'PUSH', 'PHB2024'),
('quarterstaff', 'Quarterstaff', 'SIMPLE', 'MELEE', '1d6', 'BLUDGEONING', '1d8', null, 'TOPPLE', 'PHB2024'),
('sickle', 'Sickle', 'SIMPLE', 'MELEE', '1d4', 'SLASHING', null, null, 'NICK', 'PHB2024'),
('spear', 'Spear', 'SIMPLE', 'MELEE', '1d6', 'PIERCING', '1d8', '20/60', 'SAP', 'PHB2024');

insert into weapon_property (weapon_id, property) values
('club', 'LIGHT'),
('dagger', 'FINESSE'),
('dagger', 'LIGHT'),
('dagger', 'THROWN'),
('greatclub', 'TWO_HANDED'),
('handaxe', 'LIGHT'),
('handaxe', 'THROWN'),
('javelin', 'THROWN'),
('light_hammer', 'LIGHT'),
('light_hammer', 'THROWN'),
('pitchfork', 'REACH'),
('pitchfork', 'TWO_HANDED'),
('quarterstaff', 'VERSATILE'),
('sickle', 'LIGHT'),
('spear', 'THROWN'),
('spear', 'VERSATILE');


-- ==========================================
-- SIMPLE RANGED WEAPONS
-- ==========================================

insert into weapon_def (id, name, category, melee_or_ranged, damage_dice, damage_type, versatile_damage_dice, weapon_range, weapon_mastery, source) values
('dart', 'Dart', 'SIMPLE', 'RANGED', '1d4', 'PIERCING', null, '20/60', 'VEX', 'PHB2024'),
('light_crossbow', 'Light Crossbow', 'SIMPLE', 'RANGED', '1d8', 'PIERCING', null, '80/320', 'SLOW', 'PHB2024'),
('shortbow', 'Shortbow', 'SIMPLE', 'RANGED', '1d6', 'PIERCING', null, '80/320', 'VEX', 'PHB2024'),
('sling', 'Sling', 'SIMPLE', 'RANGED', '1d4', 'BLUDGEONING', null, '30/120', 'SLOW', 'PHB2024');

insert into weapon_property (weapon_id, property) values
('dart', 'FINESSE'),
('dart', 'THROWN'),
('light_crossbow', 'AMMUNITION'),
('light_crossbow', 'LOADING'),
('light_crossbow', 'TWO_HANDED'),
('shortbow', 'AMMUNITION'),
('shortbow', 'TWO_HANDED'),
('sling', 'AMMUNITION');


-- ==========================================
-- MARTIAL MELEE WEAPONS
-- ==========================================

insert into weapon_def (id, name, category, melee_or_ranged, damage_dice, damage_type, versatile_damage_dice, weapon_range, weapon_mastery, source) values
('battleaxe', 'Battleaxe', 'MARTIAL', 'MELEE', '1d8', 'SLASHING', '1d10', null, 'TOPPLE', 'PHB2024'),
('flail', 'Flail', 'MARTIAL', 'MELEE', '1d8', 'BLUDGEONING', null, null, 'SAP', 'PHB2024'),
('glaive', 'Glaive', 'MARTIAL', 'MELEE', '1d10', 'SLASHING', null, null, 'GRAZE', 'PHB2024'),
('greataxe', 'Greataxe', 'MARTIAL', 'MELEE', '1d12', 'SLASHING', null, null, 'CLEAVE', 'PHB2024'),
('greatsword', 'Greatsword', 'MARTIAL', 'MELEE', '2d6', 'SLASHING', null, null, 'GRAZE', 'PHB2024'),
('halberd', 'Halberd', 'MARTIAL', 'MELEE', '1d10', 'SLASHING', null, null, 'CLEAVE', 'PHB2024'),
('lance', 'Lance', 'MARTIAL', 'MELEE', '1d10', 'PIERCING', null, null, 'TOPPLE', 'PHB2024'),
('longsword', 'Longsword', 'MARTIAL', 'MELEE', '1d8', 'SLASHING', '1d10', null, 'SAP', 'PHB2024'),
('maul', 'Maul', 'MARTIAL', 'MELEE', '2d6', 'BLUDGEONING', null, null, 'TOPPLE', 'PHB2024'),
('morningstar', 'Morningstar', 'MARTIAL', 'MELEE', '1d8', 'PIERCING', null, null, 'SAP', 'PHB2024'),
('pike', 'Pike', 'MARTIAL', 'MELEE', '1d10', 'PIERCING', null, null, 'PUSH', 'PHB2024'),
('rapier', 'Rapier', 'MARTIAL', 'MELEE', '1d8', 'PIERCING', null, null, 'VEX', 'PHB2024'),
('scimitar', 'Scimitar', 'MARTIAL', 'MELEE', '1d6', 'SLASHING', null, null, 'NICK', 'PHB2024'),
('shortsword', 'Shortsword', 'MARTIAL', 'MELEE', '1d6', 'PIERCING', null, null, 'VEX', 'PHB2024'),
('trident', 'Trident', 'MARTIAL', 'MELEE', '1d8', 'PIERCING', '1d10', '20/60', 'TOPPLE', 'PHB2024'),
('warhammer', 'Warhammer', 'MARTIAL', 'MELEE', '1d8', 'BLUDGEONING', '1d10', null, 'PUSH', 'PHB2024'),
('war_pick', 'War Pick', 'MARTIAL', 'MELEE', '1d8', 'PIERCING', '1d10', null, 'SAP', 'PHB2024'),
('weighted_chain', 'Weighted Chain', 'MARTIAL', 'MELEE', '1d8', 'BLUDGEONING', null, null, 'GRAZE', 'PHB2024'),
('whip', 'Whip', 'MARTIAL', 'MELEE', '1d4', 'SLASHING', null, null, 'SLOW', 'PHB2024');

insert into weapon_property (weapon_id, property) values
('battleaxe', 'VERSATILE'),
('glaive', 'HEAVY'),
('glaive', 'REACH'),
('glaive', 'TWO_HANDED'),
('greataxe', 'HEAVY'),
('greataxe', 'TWO_HANDED'),
('greatsword', 'HEAVY'),
('greatsword', 'TWO_HANDED'),
('halberd', 'HEAVY'),
('halberd', 'REACH'),
('halberd', 'TWO_HANDED'),
('lance', 'HEAVY'),
('lance', 'REACH'),
('lance', 'TWO_HANDED'),
('longsword', 'VERSATILE'),
('maul', 'HEAVY'),
('maul', 'TWO_HANDED'),
('pike', 'HEAVY'),
('pike', 'REACH'),
('pike', 'TWO_HANDED'),
('rapier', 'FINESSE'),
('scimitar', 'FINESSE'),
('scimitar', 'LIGHT'),
('shortsword', 'FINESSE'),
('shortsword', 'LIGHT'),
('trident', 'THROWN'),
('trident', 'VERSATILE'),
('warhammer', 'VERSATILE'),
('war_pick', 'VERSATILE'),
('weighted_chain', 'FINESSE'),
('weighted_chain', 'HEAVY'),
('weighted_chain', 'REACH'),
('weighted_chain', 'TWO_HANDED'),
('whip', 'FINESSE'),
('whip', 'REACH');


-- ==========================================
-- MARTIAL RANGED WEAPONS
-- ==========================================

insert into weapon_def (id, name, category, melee_or_ranged, damage_dice, damage_type, versatile_damage_dice, weapon_range, weapon_mastery, source) values
('blowgun', 'Blowgun', 'MARTIAL', 'RANGED', '1', 'PIERCING', null, '25/100', 'VEX', 'PHB2024'),
('bola', 'Bola', 'MARTIAL', 'RANGED', '1', 'BLUDGEONING', null, '20/40', 'TOPPLE', 'PHB2024'),
('hand_crossbow', 'Hand Crossbow', 'MARTIAL', 'RANGED', '1d6', 'PIERCING', null, '30/120', 'VEX', 'PHB2024'),
('heavy_crossbow', 'Heavy Crossbow', 'MARTIAL', 'RANGED', '1d10', 'PIERCING', null, '100/400', 'PUSH', 'PHB2024'),
('longbow', 'Longbow', 'MARTIAL', 'RANGED', '1d8', 'PIERCING', null, '150/600', 'SLOW', 'PHB2024'),
('musket', 'Musket', 'MARTIAL', 'RANGED', '1d12', 'PIERCING', null, '40/120', 'SLOW', 'PHB2024'),
('pistol', 'Pistol', 'MARTIAL', 'RANGED', '1d10', 'PIERCING', null, '30/90', 'VEX', 'PHB2024');

insert into weapon_property (weapon_id, property) values
('blowgun', 'AMMUNITION'),
('blowgun', 'LOADING'),
('bola', 'FINESSE'),
('bola', 'THROWN'),
('hand_crossbow', 'AMMUNITION'),
('hand_crossbow', 'LIGHT'),
('hand_crossbow', 'LOADING'),
('heavy_crossbow', 'AMMUNITION'),
('heavy_crossbow', 'HEAVY'),
('heavy_crossbow', 'LOADING'),
('heavy_crossbow', 'TWO_HANDED'),
('longbow', 'AMMUNITION'),
('longbow', 'HEAVY'),
('longbow', 'TWO_HANDED'),
('musket', 'AMMUNITION'),
('musket', 'LOADING'),
('musket', 'TWO_HANDED'),
('pistol', 'AMMUNITION'),
('pistol', 'LOADING');