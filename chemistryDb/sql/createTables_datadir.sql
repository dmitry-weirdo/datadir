create table Package
(
	id        integer not null,
	name      varchar(255) not null,
	parent_id integer, -- null for root packages

	primary key(id)
);

create generator package_gen;
set generator package_gen to 0;