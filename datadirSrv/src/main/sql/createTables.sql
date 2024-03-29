create table Attribute_type (
	  id             bigint not null
	, "type"         varchar(255)
	, database_type  varchar(255)
	, title          varchar(255)
);
alter table Attribute_type add constraint pk_attribute_type primary key(id);
create sequence gen_attribute_type_id;
alter sequence gen_attribute_type_id restart with 0;

create table Section (
	  id                 bigint not null primary key
	, parent_section_id  bigint
	, name               varchar(255) not null
	, comment            varchar(255)

	, foreign key(parent_section_id) references Section(id)
);
create sequence section_gen;
alter sequence section_gen restart with 0;

create table Entity (
	  id          bigint not null primary key
	, section_id  bigint not null
	, name        varchar(255)
	, label       varchar(255)
	, comment     varchar(255)

	, foreign key(section_id) references Section(id)
);
create sequence entity_gen;
alter sequence entity_gen restart with 0;

create table Attribute (
	  id                 bigint not null
	, entity_id          bigint not null
	, display_order      integer
	, attribute_type_id  bigint not null
	, name               varchar(255) not null
	, label              varchar(255)
	, allow_blank        smallint
	, max_length         integer
	, comment            varchar(255)
);
alter table Attribute add constraint pk_attribute primary key(id);
alter table Attribute add constraint fk_attribute_1 foreign key(entity_id) references Entity(id);
alter table Attribute add constraint fk_attribute_2 foreign key(attribute_type_id) references Attribute_type(id);
create sequence gen_attribute_id;
alter sequence gen_attribute_id restart with 0;

create table "User" (
	  id        bigint not null primary key
	, login     varchar(255) not null
	, password  varchar(255) not null
	, name      varchar(255) not null
);
create sequence user_gen;
alter sequence user_gen restart with 0;

create table User_entity_operation (
	  id        bigint not null primary key
	, user_id   bigint not null
	, entity_id bigint not null
	, operation varchar(20) not null

	, foreign key(user_id) references "User"(id)
	, foreign key(entity_id) references Entity(id)
);
create sequence user_entity_operation_gen;
alter sequence user_entity_operation_gen restart with 0;

commit;