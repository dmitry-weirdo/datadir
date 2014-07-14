package ru.datadir.database;

import ru.datadir.model.attribute.Attribute;
import ru.datadir.model.attribute.AttributeType;
import ru.datadir.model.attribute.AttributeTypeMnemonic;
import ru.datadir.model.entity.Entity;

import java.util.ArrayList;
import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class FirebirdDatabaseTableCreator implements DatabaseTableCreator
{
	@Override
	public String getCreateTableScript(Entity entity) {
		StringBuilder sb = new StringBuilder();
		DatabaseTypeMapper typeMapper = new FirebirdDatabaseTypeMapper();

		List<Attribute> attributes = entity.getAttributes();
		int maxAttributeNameLength = -1;
		for (Attribute attribute : attributes)
		{
			int length = attribute.getName().length();
			if (length > maxAttributeNameLength)
				maxAttributeNameLength = length;
		}

		int lengthBeforeFieldType = maxAttributeNameLength + 2;

		String tableName = DatabaseEntityUtils.getDatabaseTableName(entity);

		sb.append("create table ").append(tableName).append(" (");

		int spacesBeforeFieldTypeCount;

		// append id
		String primaryKeyColumnName = DatabaseEntityUtils.getPrimaryKeyColumnName(entity);
		sb.append("\n");
		sb.append("\t  ").append(primaryKeyColumnName);
		spacesBeforeFieldTypeCount = lengthBeforeFieldType - primaryKeyColumnName.length();
		for (int j = 0; j < spacesBeforeFieldTypeCount; j++)
			sb.append(" ");

		sb.append("bigint not null primary key");

		// append fields
		for (int i = 0; i < attributes.size(); i++)
		{
			Attribute attribute = attributes.get(i);

			sb.append("\n");
/*
			if (i == 0)
			{
				sb.append("\t  ");
			}
			else
			{
				sb.append("\t, ");
			}
*/
			sb.append("\t, ");

			sb.append( attribute.getName() );

			spacesBeforeFieldTypeCount = lengthBeforeFieldType - attribute.getName().length();
			for (int j = 0; j < spacesBeforeFieldTypeCount; j++)
				sb.append(" ");

			sb.append( typeMapper.getDatabaseFieldType(attribute.getType().getMnemonic()) );
		}

		sb.append("\n");
		sb.append(");"); // end of create table

		// create sequence
		String sequenceName = DatabaseEntityUtils.getDatabaseSequenceName(entity);
		sb.append("\n");
		sb.append("create sequence ").append(sequenceName).append(";");

		return sb.toString();
	}

	@Override
	public String getDropTableScript(Entity entity) {
		StringBuilder sb = new StringBuilder();

		String tableName = DatabaseEntityUtils.getDatabaseTableName(entity);
		sb.append("drop table ").append(tableName).append(";");

		String sequenceName = DatabaseEntityUtils.getDatabaseSequenceName(entity);
		sb.append("\n");
		sb.append("drop sequence ").append(sequenceName).append(";");

		return sb.toString();
	}

	public static void main(String[] args) {
		List<Attribute> attributes = new ArrayList<>();
		attributes.add( new Attribute("string_field", new AttributeType(AttributeTypeMnemonic.STRING)) );
		attributes.add( new Attribute("long_field", new AttributeType(AttributeTypeMnemonic.LONG)) );
		attributes.add( new Attribute("double_field", new AttributeType(AttributeTypeMnemonic.DOUBLE)) );
		attributes.add( new Attribute("boolean_field", new AttributeType(AttributeTypeMnemonic.BOOLEAN)) );
		attributes.add( new Attribute("character_field", new AttributeType(AttributeTypeMnemonic.CHARACTER)) );
//		attributes.add( new Attribute("file_field", new AttributeType(AttributeTypeMnemonic.FILE)) );
		attributes.add( new Attribute("blob_field", new AttributeType(AttributeTypeMnemonic.BLOB)) );
		attributes.add( new Attribute("date_field", new AttributeType(AttributeTypeMnemonic.DATE)) );
		attributes.add( new Attribute("datetime_field", new AttributeType(AttributeTypeMnemonic.DATETIME)) );
//		attributes.add( new Attribute("link_field", new AttributeType(AttributeTypeMnemonic.LINK)) );

		Entity entity = new Entity();
		entity.setName("Firebird_test_table");
		entity.setAttributes(attributes);

		DatabaseTableCreator tableCreator = new FirebirdDatabaseTableCreator();
		String createTableScript = tableCreator.getCreateTableScript(entity);
		String dropTableScript = tableCreator.getDropTableScript(entity);

		System.out.println( concat("Table create script:\n", createTableScript) );

		System.out.println( concat("\n\nDrop create script:\n", dropTableScript) );
	}
}