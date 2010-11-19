package ru.pda.chemistry.service;

import ru.pda.chemistry.entities.Attribute;
import ru.pda.chemistry.entities.Entity;
import ru.pda.chemistry.entities.EntityValue;
import ru.pda.chemistry.entities.attributeValue.AttributeValue;
import ru.pda.chemistry.entities.attributeValue.AttributeValueFactory;
import ru.pda.chemistry.sql.SqlConnector;
import static ru.pda.chemistry.util.StringUtils.getConcatenation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 * User: 1
 * Date: 03.06.2010
 * Time: 22:08:17
 */
public class GeneratorServiceImpl implements GeneratorService
{
	public void generateTable(Entity entity) throws SQLException {
		if (entity.getAttributes() == null || entity.getAttributes().isEmpty())
			throw new IllegalArgumentException(getConcatenation(sb, "Entity with id = ", entity.getId().toString(), " has no attributes"));

		String tableName = getConcatenation(this.sb, "entity_table_", entity.getId().toString());
		entity.setTableName(tableName);

		StringBuffer sb = new StringBuffer();
		sb.append("create table ");
		sb.append("`").append(tableName).append("`");
		sb.append("(");
		sb.append("`id` int(11) NOT NULL AUTO_INCREMENT,");
		sb.append("`source` varchar(255) NOT NULL,");

		fillEntityAttributesScript(sb, entity);

		sb.append("PRIMARY KEY (`id`)");
		sb.append(") ENGINE=InnoDB DEFAULT CHARSET=cp1251;");


		SqlConnector connector = SqlConnector.create();

		try
		{
			// create table
			connector.executeUpdate(sb.toString());

			// fill entity's table name
			getConcatenation(sb, "update entity set table_name = '", entity.getTableName(), "' where id = ", entity.getId().toString());
			connector.executeUpdate(sb.toString());

			// fill attributes' column names
			for (Attribute attribute : entity.getAttributes())
			{
				getConcatenation(sb, "update attribute set column_name = '", attribute.getColumnName(), "' where id = ", attribute.getId().toString());
				connector.executeUpdate(sb.toString());
			}
		}
		finally
		{
			connector.closeConnection();
		}
	}
	private void fillEntityAttributesScript(StringBuffer sb, Entity entity) {
		for (Attribute attribute : entity.getAttributes())
			fillAttributeScript(sb, attribute);
	}
	private void fillAttributeScript(StringBuffer sb, Attribute attribute) {
		// todo: maybe this may be refactored more polymorphic
		String columnName = null;

		switch (attribute.getType())
		{
			case BOOLEAN:
				columnName = getConcatenation(this.sb, "bool_", attribute.getId().toString());
				sb.append("`").append(columnName).append("` ").append("tinyint(1) DEFAULT NULL,");
				break;

			case INTEGER:
				columnName = getConcatenation(this.sb, "int_", attribute.getId().toString());
				sb.append("`").append(columnName).append("` ").append("bigint(11) DEFAULT NULL,"); 
				break;

			case DOUBLE:
				columnName = getConcatenation(this.sb, "double_", attribute.getId().toString());
				sb.append("`").append(columnName).append("` ").append("double(15,3) DEFAULT NULL,"); // todo: somewhen use set precision
				break;

			case STRING:
				columnName = getConcatenation(this.sb, "string_", attribute.getId().toString());
				sb.append("`").append(columnName).append("` ").append("varchar(255) DEFAULT NULL,"); 
				break;

			case ENUM:
				columnName = getConcatenation(this.sb, "enum_", attribute.getId().toString());
				String columnFkName = getConcatenation(this.sb, columnName, "_fk");

				sb.append("`").append(columnName).append("` ").append("int(11) DEFAULT NULL,");
				sb.append("KEY `").append(columnFkName).append("` (`").append(columnName).append("`),");
				sb.append("CONSTRAINT `").append(columnFkName).append("` FOREIGN KEY (`").append(columnName).append("`) REFERENCES `enum` (`id`),");
				break;
		}

		attribute.setColumnName(columnName);
	}

	public void deleteTable(Entity entity) throws SQLException {
		if (entity == null || entity.getTableName() == null)
			return;

		SqlConnector connector = SqlConnector.create();
		
		try
		{
			sb.delete(0, sb.length());
			sb.append("drop table ").append(entity.getTableName());
			connector.executeUpdate(sb.toString());

			sb.delete(0, sb.length());
			sb.append("update entity set table_name = null where id = ").append(entity.getId());
			connector.executeUpdate(sb.toString());
		}
		finally
		{
			connector.closeConnection();
		}
	}

	public List<EntityValue> getEntityValues(Entity entity) throws SQLException {
		if (entity == null)
			return null;

		SqlConnector connector = SqlConnector.create();
		ResultSet resultSet = null;

		try
		{
			sb.delete(0, sb.length());
			sb.append("select id, source, ");

			List<Attribute> attributeList = entity.getAttributes();
			for (Attribute attribute : attributeList)
				sb.append(attribute.getColumnName()).append(", ");

			sb.delete(sb.length() - 2, sb.length() - 1); // remove redundant comma
			sb.append("from ").append(entity.getTableName()).append(" ");
			sb.append("order by id"); // todo: order by set attribute


			List<EntityValue> values = new ArrayList<EntityValue>();

			resultSet = connector.getResultSet(sb.toString());
			while (resultSet.next())
			{
				EntityValue entityValue = new EntityValue();
				entityValue.setId(resultSet.getInt(1));
				entityValue.setSource(resultSet.getString(2));

				for (int i = 0; i < attributeList.size(); i++)
				{
					Attribute attribute = attributeList.get(i);
					AttributeValue attributeValue = AttributeValueFactory.createAttributeValue(attribute);
					attributeValue.setAttribute(attribute);
					AttributeValueFactory.setAttributeValue(attributeValue, resultSet, i + 3); // result set values start from 1 + 1 column is for id + 1 column is for source

					entityValue.setAttributeValue(attribute.getName(), attributeValue);
				}

				values.add(entityValue);
			}

			return values;
		}
		finally               
		{
			connector.closeResultSet(resultSet);
			connector.closeConnection();
		}
	}
	public EntityValue getEntityValue(Entity entity, Integer id) throws SQLException {
		if (entity == null || id == null)
			return null;

		SqlConnector connector = SqlConnector.create();
		ResultSet resultSet = null;

		try
		{
			sb.delete(0, sb.length());
			sb.append("select source, ");

			List<Attribute> attributeList = entity.getAttributes();
			for (Attribute attribute : attributeList)
				sb.append(attribute.getColumnName()).append(", ");

			sb.delete(sb.length() - 2, sb.length() - 1); // remove redundant comma
			sb.append("from ").append(entity.getTableName()).append(" ");
			sb.append("where id = ").append(id);

			resultSet = connector.getResultSet(sb.toString());
			if (!resultSet.next())
				return null;

			EntityValue entityValue = new EntityValue();
			entityValue.setId(id);
			entityValue.setSource(resultSet.getString(1));

			for (int i = 0; i < attributeList.size(); i++)
			{
				Attribute attribute = attributeList.get(i);
				AttributeValue attributeValue = AttributeValueFactory.createAttributeValue(attribute);
				attributeValue.setAttribute(attribute);
				AttributeValueFactory.setAttributeValue(attributeValue, resultSet, i + 2); // result set values start from 1 + 1 column is for source

				entityValue.setAttributeValue(attribute.getName(), attributeValue);
			}

			return entityValue;
		}
		finally
		{
			connector.closeResultSet(resultSet);
			connector.closeConnection();
		}
	}
	public void deleteEntityValue(Entity entity, Integer id) throws SQLException {
		if (entity == null || id == null)
			return;

		SqlConnector connector = SqlConnector.create();

		try
		{
			sb.delete(0, sb.length());
			sb.append("delete from ").append(entity.getTableName()).append(" ");
			sb.append("where id = ").append(id);

			connector.executeUpdate(sb.toString());
		}
		finally
		{
			connector.closeConnection();
		}
	}
	
	public Integer createEntityValue(Entity entity, String source) throws SQLException {
		SqlConnector connector = SqlConnector.create();
		ResultSet resultSet = null;

		try
		{
			getConcatenation(sb, "insert into ", entity.getTableName(), " ",	"(source) values('", source, "')");
			connector.executeUpdate(sb.toString());

			resultSet = connector.getResultSetNext(getConcatenation(sb, "select max(id) from ", entity.getTableName()));
			return resultSet.getInt(1);
		}
		finally
		{
			connector.closeResultSet(resultSet);
			connector.closeConnection();
		}
	}
	public void setAttributeValue(Integer entityId, AttributeValue value) throws SQLException {
		SqlConnector connector = SqlConnector.create();

		try
		{
			getConcatenation(sb,
				"update ", value.getAttribute().getEntity().getTableName(), " ",
				"set ", value.getAttribute().getColumnName(), " = ", value.getSqlAttributeValue(), " ",
				"where id = ", entityId.toString()
			);

			connector.executeUpdate(sb.toString());
		}
		finally
		{
			connector.closeConnection();
		}
	}
	
	public void setAttributeValues(Integer entityId, List<AttributeValue> values) throws SQLException {
		if (values.isEmpty())
			return;

		// todo: may be check that attributes belong to one entity

		SqlConnector connector = SqlConnector.create();

		try
		{
			sb.delete(0, sb.length());
			sb.append("update ").append(values.get(0).getAttribute().getEntity().getTableName()).append(" ");
			sb.append("set ");

			for (AttributeValue value : values)
				sb.append(value.getAttribute().getColumnName()).append(" = ").append(value.getSqlAttributeValue()).append(", ");

			sb.delete(sb.length() - 2, sb.length() - 1); // delete redunant comma
			sb.append("where id = ").append(entityId);

			connector.executeUpdate(sb.toString());
		}
		finally
		{
			connector.closeConnection();
		}
	}

	public void setSource(Entity entity, Integer entityValueId, String source) throws SQLException {
		if (source == null || source.isEmpty())
			return;

		SqlConnector connector = SqlConnector.create();

		try
		{
			sb.delete(0, sb.length());
			sb.append("update ").append(entity.getTableName()).append(" ");
			sb.append("set source = '").append(source).append("' ");
			sb.append("where id = ").append(entityValueId);

			connector.executeUpdate(sb.toString());
		}
		finally
		{
			connector.closeConnection();
		}
	}
	
	private StringBuffer sb = new StringBuffer();
}