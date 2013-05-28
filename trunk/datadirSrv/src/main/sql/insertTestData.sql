-- Attribute type
insert into Attribute_type (id, "TYPE", database_type, title)
values(gen_id(gen_attribute_type_id, 1), 'java.lang.Integer', 'Integer', 'Целое число');

insert into Attribute_type (id, "TYPE", database_type, title)
values(gen_id(gen_attribute_type_id, 1), 'java.lang.String', 'varchar(255)', 'Строка');

insert into Attribute_type (id, "TYPE", database_type, title)
values(gen_id(gen_attribute_type_id, 1), 'java.lang.Boolean', 'smallint', 'Булево значение');

insert into Attribute_type (id, "TYPE", database_type, title)
values(gen_id(gen_attribute_type_id, 1), 'java.lang.Double', 'numeric(15, 2)', 'Вещественное число');

-- Entity
insert into Entity(id, name, label, comment)
values(gen_id(gen_entity_id, 1), 'Car', 'Машина', 'Тестовая таблица');

-- Attribute
insert into Attribute(id, entity_id, display_order, name, label, attribute_type_id)
values(
  gen_id(gen_attribute_id, 1)
, ( select e.id from Entity e where e.name = 'Car')
, 1
, 'brand'
, 'Марка'
, ( select att.id from Attribute_type att where att."TYPE" = 'java.lang.String')
);

insert into Attribute(id, entity_id, display_order, name, label, attribute_type_id)
values(
  gen_id(gen_attribute_id, 1)
, ( select e.id from Entity e where e.name = 'Car')
, 2
, 'power'
, 'Мощность двигателя'
, ( select att.id from Attribute_type att where att."TYPE" = 'java.lang.Double')
);

insert into Attribute(id, entity_id, display_order, name, label, attribute_type_id)
values(
  gen_id(gen_attribute_id, 1)
, ( select e.id from Entity e where e.name = 'Car')
, 3
, 'doorsCount'
, 'Количество дверей'
, ( select att.id from Attribute_type att where att."TYPE" = 'java.lang.Integer')
);

insert into Attribute(id, entity_id, display_order, name, label, attribute_type_id)
values(
  gen_id(gen_attribute_id, 1)
, ( select e.id from Entity e where e.name = 'Car')
, 4
, 'colour'
, 'Цвет'
, ( select att.id from Attribute_type att where att."TYPE" = 'java.lang.String')
);

insert into Attribute(id, entity_id, display_order, name, label, attribute_type_id)
values(
  gen_id(gen_attribute_id, 1)
, ( select e.id from Entity e where e.name = 'Car')
, 5
, 'comment'
, 'Комментарий'
, ( select att.id from Attribute_type att where att."TYPE" = 'java.lang.String')
);
