package ru.pda.chemistry.service;

import ru.pda.chemistry.entities.*;
import ru.pda.chemistry.entities.EnumValue;
import ru.pda.chemistry.sql.SqlConnector;
import static ru.pda.chemistry.util.StringUtils.getConcatenation;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * User: 1
 * Date: 26.04.2010
 * Time: 23:41:24
 * Сервис для данных конструктора сущностей.
 */
public class ConstructorServiceImpl implements ConstructorService
{
	public List<Section> getSections() throws SQLException {
		SqlConnector connector = SqlConnector.create();
		ResultSet resultSet = null;

		try
		{
			List<Section> sections = new ArrayList<Section>();

			resultSet = connector.getResultSet("select id, name from section order by name");
			while (resultSet.next())
			{
				Section section = new Section();

				section.setId(resultSet.getInt(1));
				section.setName(resultSet.getString(2));

				sections.add(section);
			}

			return sections;
		}
		finally
		{
			connector.closeResultSet(resultSet);
			connector.closeConnection();
		}
	}

	public Section getSection(Integer id) throws SQLException {
		if (id == null)
			return null;

		SqlConnector connector = SqlConnector.create();
		ResultSet resultSet = null;

		try
		{
			resultSet = connector.getResultSet(getConcatenation(sb, "select id, name from section where id = ", id.toString()));
			if (!resultSet.next())
				return null;

			Section section = new Section();
			section.setId(resultSet.getInt(1));
			section.setName(resultSet.getString(2));
			// todo: fill entities (may be on boolean method parameter)

			return section;
		}
		finally
		{
			connector.closeResultSet(resultSet);
			connector.closeConnection();
		}
	}

	public Integer createSection(Section section) throws SQLException, NotUniqueException {
		SqlConnector connector = SqlConnector.create();
		ResultSet resultSet = null;

		try
		{
			resultSet = connector.getResultSet(getConcatenation(sb, "select id from section where name = '", section.getName(), "'"));
			if (resultSet.next())
				throw new NotUniqueException(getConcatenation(sb, "Section with name \"", section.getName(), "\" already exists"));

			connector.executeUpdate(getConcatenation(sb, "insert into section(id, name) values (gen_id(section_gen, 1), '", section.getName(), "')"));

			resultSet = connector.getResultSetNext(getConcatenation(sb, "select id from section where name = '", section.getName(), "'"));
			return resultSet.getInt(1);
		}
		finally
		{
			connector.closeResultSet(resultSet);
			connector.closeConnection();
		}
	}

	public void deleteSection(Integer id) throws SQLException {
		if (id == null)
			return;
		
		SqlConnector connector = SqlConnector.create();

		try
		{
			// cascade delete section's entities
			for (Entity entity : getEntities(id))
				deleteEntity(entity.getId());

			// no need to check whether it exists
			connector.executeUpdate(getConcatenation(sb, "delete from section where id = ", id.toString()));
		}
		finally
		{
			connector.closeConnection();
		}
	}


	public List<Entity> getEntities(Integer sectionId) throws SQLException {
		Section section = getSection(sectionId);
		if (section == null)
			return Collections.emptyList(); // section not found by id


		SqlConnector connector = SqlConnector.create();
		ResultSet resultSet = null;
		
		try
		{
			List<Entity> entities = new ArrayList<Entity>();

			resultSet = connector.getResultSet(getConcatenation(sb, "select id, name from entity where section_id = ", sectionId.toString(), " order by name"));
			while (resultSet.next())
			{
				Entity entity = new Entity();
				entity.setId(resultSet.getInt(1));
				entity.setName(resultSet.getString(2));
				entity.setSection(section);
				// todo: fill attributes (may be on boolean method parameter)

				entities.add(entity);
			}

			return entities;
		}
		finally
		{
			connector.closeResultSet(resultSet);
			connector.closeConnection();
		}
	}

	public Entity getEntity(Integer id) throws SQLException {
		if (id == null)
			return null;

		SqlConnector connector = SqlConnector.create();
		ResultSet resultSet = null;

		try
		{
			resultSet = connector.getResultSet(getConcatenation(sb, "select id, name, table_name, generator_name, section_id from entity where id = ", id.toString()));
			if (!resultSet.next())
				return null;

			Entity entity = new Entity();
			entity.setId(resultSet.getInt(1));
			entity.setName(resultSet.getString(2));
			entity.setTableName(resultSet.getString(3));
			entity.setGeneratorName(resultSet.getString(4));
			entity.setSection(getSection(resultSet.getInt(5)));
			// todo: fill attributes (may be on boolean method parameter)

			return entity;
		}
		finally
		{
			connector.closeResultSet(resultSet);
			connector.closeConnection();
		}
	}

	public Integer createEntity(Integer sectionId, Entity entity) throws SQLException, NotUniqueException, ConstraintException {
		if (getSection(sectionId) == null)
			throw new ConstraintException(getConcatenation(sb, "Section with id = ", sectionId.toString(), " does not exist"));

		SqlConnector connector = SqlConnector.create();
		ResultSet resultSet = null;

		try
		{
			// todo: think about name uniquness only within section (name + section_id) -> then database unique constraint will have to be change appropriately 
			resultSet = connector.getResultSet(getConcatenation(sb, "select id from entity where name = '", entity.getName(), "'"));
			if (resultSet.next())
				throw new NotUniqueException(getConcatenation(sb, "Entity with name \"", entity.getName(), "\" already exists"));

			connector.executeUpdate(getConcatenation(sb, "insert into entity(id, name, section_id) values (",
				"gen_id(entity_gen, 1) ,", // id
				"'", entity.getName(), "', ", // name
				sectionId.toString(), // section_id 
				")"
			));

			resultSet = connector.getResultSetNext(getConcatenation(sb, "select id from entity where name = '", entity.getName(), "' and section_id = ", sectionId.toString()));
			return resultSet.getInt(1);
		}
		finally
		{
			connector.closeResultSet(resultSet);
			connector.closeConnection();
		}
	}

	public void deleteEntity(Integer id) throws SQLException {
		if (id == null)
			return;

		SqlConnector connector = SqlConnector.create();

		try
		{
			// delete entity's table, if it exists
			Entity entity = getEntity(id);
			if (entity == null)
				return;

			String tableName = entity.getTableName();
			if (tableName != null)
				connector.executeUpdate(getConcatenation(sb, "drop table ", tableName));

			// cascade delete entity's attributes
			for (Attribute attribute : getAttributes(id))
				deleteAttribute(attribute.getId());

			// no need to check whether it exists
			connector.executeUpdate(getConcatenation(sb, "delete from entity where id = ", id.toString()));
		}
		finally
		{
			connector.closeConnection();
		}
	}


	public List<Attribute> getAttributes(Integer entityId) throws SQLException {
		Entity entity = getEntity(entityId);
		if (entity == null)
			return Collections.emptyList(); // entity not found by id
		
		SqlConnector connector = SqlConnector.create();
		ResultSet resultSet = null;

		try
		{
			List<Attribute> attributes = new ArrayList<Attribute>();

			// todo: order by given order number
			resultSet = connector.getResultSet(getConcatenation(sb, "select id, name, type, measure_unit, column_name from attribute where entity_id = ", entityId.toString(), " order by name"));
			while (resultSet.next())
			{
				Attribute attribute = new Attribute();
				attribute.setId(resultSet.getInt(1));
				attribute.setName(resultSet.getString(2));
				attribute.setType(AttributeType.values()[resultSet.getInt(3)]);
				attribute.setMeasureUnit(resultSet.getString(4));
				attribute.setColumnName(resultSet.getString(5));
				attribute.setEntity(entity);
				
				attributes.add(attribute);
			}

			return attributes;
		}
		finally
		{
			connector.closeResultSet(resultSet);
			connector.closeConnection();
		}
	}

	public Attribute getAttribute(Integer id) throws SQLException {
		if (id == null)
			return null;

		SqlConnector connector = SqlConnector.create();
		ResultSet resultSet = null;

		try
		{
			resultSet = connector.getResultSet(getConcatenation(sb, "select id, name, type, measure_unit, column_name, entity_id from attribute where id = ", id.toString()));
			if (!resultSet.next())
				return null;

			Attribute attribute = new Attribute();
			attribute.setId(resultSet.getInt(1));
			attribute.setName(resultSet.getString(2));
			attribute.setType(AttributeType.values()[resultSet.getInt(3)]);
			attribute.setMeasureUnit(resultSet.getString(4));
			attribute.setColumnName(resultSet.getString(5));
			attribute.setEntity(getEntity(resultSet.getInt(6)));

			return attribute;
		}
		finally
		{
			connector.closeResultSet(resultSet);
			connector.closeConnection();
		}
	}

	public Integer createAttribute(Integer entityId, Attribute attribute) throws SQLException, NotUniqueException, ConstraintException {
		if (getEntity(entityId) == null)
			throw new ConstraintException(getConcatenation(sb, "Entity with id = ", entityId.toString(), " does not exist"));

		SqlConnector connector = SqlConnector.create();
		ResultSet resultSet = null;

		try
		{
			// check name uniqueness of the attribute within the entity
			resultSet = connector.getResultSet(getConcatenation(sb, "select id from attribute where name = '", attribute.getName(), "' and entity_id = ", entityId.toString()));
			if (resultSet.next())
				throw new NotUniqueException(getConcatenation(sb, "Attribute with name \"", attribute.getName(), "\" for entity with id = ", entityId.toString()," already exists"));

			connector.executeUpdate(getConcatenation(sb, "insert into attribute(id, name, type, measure_unit, entity_id) values (",
				"gen_id(attribute_gen, 1), ", // id
				"'", attribute.getName(), "', ", // name
				Integer.toString(attribute.getType().ordinal()), ", ", // type
				attribute.getMeasureUnit() == null ? "NULL": getConcatenation(sb, "'", attribute.getMeasureUnit(), "'"), ", ", // measure_unit
				entityId.toString(), // entity_id
				")"
			));

			resultSet = connector.getResultSetNext(getConcatenation(sb, "select id from attribute where name = '", attribute.getName(), "' and entity_id = ", entityId.toString()));
			return resultSet.getInt(1);
		}
		finally
		{
			connector.closeResultSet(resultSet);
			connector.closeConnection();
		}
	}

	public void deleteAttribute(Integer id) throws SQLException {
		if (id == null)
			return;

		SqlConnector connector = SqlConnector.create();

		try
		{
			// cascade delete attribute's enum values
			for (EnumValue enumValue : getEnumValues(id))
				deleteEnumValue(enumValue.getId());

			// no need to check whether it exists
			connector.executeUpdate(getConcatenation(sb, "delete from attribute where id = ", id.toString()));
		}
		finally
		{
			connector.closeConnection();
		}
	}


	public List<EnumValue> getEnumValues(Integer attributeId) throws SQLException {
		Attribute attribute = getAttribute(attributeId);
		if (attribute == null || !attribute.isEnum())
			return Collections.emptyList(); // attribute not found by id

		SqlConnector connector = SqlConnector.create();
		ResultSet resultSet = null;

		try
		{
			List<EnumValue> enums = new ArrayList<EnumValue>();

			// todo: order by given order number if needed
			resultSet = connector.getResultSet(getConcatenation(sb, "select id, name from enum where attribute_id = ", attributeId.toString(), " order by name"));
			while (resultSet.next())
			{
				EnumValue enumValue = new EnumValue();
				enumValue.setId(resultSet.getInt(1));
				enumValue.setName(resultSet.getString(2));
				enumValue.setAttribute(attribute);

				enums.add(enumValue);
			}

			return enums;
		}
		finally
		{
			connector.closeResultSet(resultSet);
			connector.closeConnection();
		}
	}

	public EnumValue getEnumValue(Integer id) throws SQLException {
		if (id == null)
			return null;

		SqlConnector connector = SqlConnector.create();
		ResultSet resultSet = null;

		try
		{
			resultSet = connector.getResultSet(getConcatenation(sb, "select id, name, attribute_id from enum where id = ", id.toString()));
			if (!resultSet.next())
				return null;

			EnumValue enumValue = new EnumValue();
			enumValue.setId(resultSet.getInt(1));
			enumValue.setName(resultSet.getString(2));
			enumValue.setAttribute(getAttribute(resultSet.getInt(3)));

			return enumValue;
		}
		finally
		{
			connector.closeResultSet(resultSet);
			connector.closeConnection();
		}
	}

	public Integer createEnumValue(Integer attributeId, EnumValue enumValue) throws SQLException, NotUniqueException, ConstraintException  {
		Attribute attribute = getAttribute(attributeId);
		if (attribute == null)
			throw new ConstraintException(getConcatenation(sb, "Attribute with id = ", attributeId.toString(), " does not exist"));

		if (!attribute.isEnum())
			throw new ConstraintException(getConcatenation(sb, "Attribute with id = ", attributeId.toString(), " has non-enum type"));

		SqlConnector connector = SqlConnector.create();
		ResultSet resultSet = null;

		try
		{
			// check name uniqueness of the enumValue within the attribute
			resultSet = connector.getResultSet(getConcatenation(sb, "select id from enum where name = '", enumValue.getName(), "' and attribute_id = ", attributeId.toString()));
			if (resultSet.next())
				throw new NotUniqueException(getConcatenation(sb, "EnumValue with name \"", enumValue.getName(), "\" for attribute with id = ", attributeId.toString()," already exists"));

			connector.executeUpdate(getConcatenation(sb, "insert into enum(id, name, attribute_id) values (",
				"gen_id(enum_gen, 1), ", // id
				"'", enumValue.getName(), "', ", // name
				attributeId.toString(), // attribute_id
				")"
			));

			resultSet = connector.getResultSetNext(getConcatenation(sb, "select id from enum where name = '", enumValue.getName(), "' and attribute_id = ", attributeId.toString()));
			return resultSet.getInt(1);
		}
		finally
		{
			connector.closeResultSet(resultSet);
			connector.closeConnection();
		}

	}

	public void deleteEnumValue(Integer id) throws SQLException {
		if (id == null)
			return;

		SqlConnector connector = SqlConnector.create();

		try
		{
			// no need to check whether it exists
			connector.executeUpdate(getConcatenation(sb, "delete from enum where id = ", id.toString()));
		}
		finally
		{
			connector.closeConnection();
		}
	}

	
	private StringBuffer sb = new StringBuffer();
}