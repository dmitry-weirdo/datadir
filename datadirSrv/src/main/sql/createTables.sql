create table Attribute_type (
	id             bigint not null,
	"type"         varchar(255),
	database_type  varchar(255),
	title          varchar(255)
);
alter table Attribute_type add constraint pk_attribute_type primary key(id);
create sequence gen_attribute_type_id;
alter sequence gen_attribute_type_id restart with 0;

create table Entity (
	id          bigint not null,
	package_id  bigint,
	name        varchar(255),
	label       varchar(255),
	comment     varchar(255)
);
alter table Entity add constraint pk_entity primary key(id);
create sequence gen_entity_id;
alter sequence gen_entity_id restart with 0;

create table Attribute (
	id                 bigint not null,
	entity_id          bigint not null,
	display_order      integer,
	attribute_type_id  bigint not null,
	name               varchar(255) not null,
	label              varchar(255),
	allow_blank        smallint,
	max_length         integer,
	comment            varchar(255)
);
alter table Attribute add constraint pk_attribute primary key(id);
alter table Attribute add constraint fk_attribute_1 foreign key(entity_id) references Entity(id);
alter table Attribute add constraint fk_attribute_2 foreign key(attribute_type_id) references Attribute_type(id);
create sequence gen_attribute_id;
alter sequence gen_attribute_id restart with 0;

commit;