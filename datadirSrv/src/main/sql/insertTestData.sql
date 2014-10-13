-- Attribute type
insert into Attribute_type (id, "type", database_type, title)
values(gen_id(gen_attribute_type_id, 1), 'java.lang.Integer', 'Integer', 'Целое число');

insert into Attribute_type (id, "type", database_type, title)
values(gen_id(gen_attribute_type_id, 1), 'java.lang.String', 'varchar(255)', 'Строка');

insert into Attribute_type (id, "type", database_type, title)
values(gen_id(gen_attribute_type_id, 1), 'java.lang.Boolean', 'smallint', 'Булево значение');

insert into Attribute_type (id, "type", database_type, title)
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
, ( select att.id from Attribute_type att where att."type" = 'java.lang.String')
);

insert into Attribute(id, entity_id, display_order, name, label, attribute_type_id)
values(
  gen_id(gen_attribute_id, 1)
, ( select e.id from Entity e where e.name = 'Car')
, 2
, 'power'
, 'Мощность двигателя'
, ( select att.id from Attribute_type att where att."type" = 'java.lang.Double')
);

insert into Attribute(id, entity_id, display_order, name, label, attribute_type_id)
values(
  gen_id(gen_attribute_id, 1)
, ( select e.id from Entity e where e.name = 'Car')
, 3
, 'doorsCount'
, 'Количество дверей'
, ( select att.id from Attribute_type att where att."type" = 'java.lang.Integer')
);

insert into Attribute(id, entity_id, display_order, name, label, attribute_type_id)
values(
  gen_id(gen_attribute_id, 1)
, ( select e.id from Entity e where e.name = 'Car')
, 4
, 'colour'
, 'Цвет'
, ( select att.id from Attribute_type att where att."type" = 'java.lang.String')
);

insert into Attribute(id, entity_id, display_order, name, label, attribute_type_id)
values(
  gen_id(gen_attribute_id, 1)
, ( select e.id from Entity e where e.name = 'Car')
, 5
, 'comment'
, 'Комментарий'
, ( select att.id from Attribute_type att where att."type" = 'java.lang.String')
);

insert into "User"(id, login, password, name)
values(
  gen_id(user_gen, 0)
, 'testUserLogin'
, 'testUserPassword'
, 'Test User Name'
);

insert into User_entity_operation(id, user_id, entity_id, operation)
values(
  gen_id(user_entity_operation_gen, 0)
, ( select u.id from "User" u where u.login = 'testUserLogin')
, ( select e.id from Entity e where e.name = 'Car')
, 'create'
);

commit;