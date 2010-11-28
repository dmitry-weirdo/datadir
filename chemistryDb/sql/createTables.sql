create table Section
(
    id        integer not null,
    name      varchar(255) not null,

    primary key(id),
    unique(name)
);
create generator section_gen;
set generator section_gen to 0;

create table Entity
(
    id        integer not null,
    name      varchar(255) not null,
    section_id integer not null,
    table_name varchar(255),

    primary key(id),
    unique(name),
    foreign key(section_id) references Section(id)
);
create generator entity_gen;
set generator entity_gen to 0;

create table Attribute
(
    id        integer not null,
    name      varchar(255) not null,
    type      smallint not null,
    measure_unit varchar(255),
    entity_id integer, -- todo: why null ?
    column_name varchar(255),

    primary key(id),
    foreign key(entity_id) references Entity(id)
);
create generator attribute_gen;
set generator attribute_gen to 0;

create table Enum
(
    id        integer not null,
    name      varchar(255) not null,
    attribute_id integer not null,

    primary key(id),
    foreign key(attribute_id) references Attribute(id)
);
create generator enum_gen;
set generator enum_gen to 0;

commit;