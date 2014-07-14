package ru.datadir.database;

import ru.datadir.model.attribute.AttributeTypeMnemonic;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class FirebirdDatabaseTypeMapper implements DatabaseTypeMapper
{
	@Override
	public String getDatabaseFieldType(AttributeTypeMnemonic type) {
		switch (type)
		{
			case STRING: return "varchar(8191)"; // max varchar for UTF-8 encoding
			case LONG: return "bigint"; // int64 does not work in 2.5
			case DOUBLE: return "double precision"; // maybe float fits better
			case BOOLEAN: return "smallint";
			case CHARACTER: return "char";
			case BLOB: return "blob";
			case DATE: return "timestamp";
			case DATETIME:  return "timestamp";

			case LINK:
				throw new IllegalStateException( concat("Type: \"", type, "\" is not supported.") );

			case FILE:
				throw new IllegalStateException( concat("Type: \"", type, "\" is not supported.") );

			default:
				throw new IllegalArgumentException( concat("Unknown type: \"", type, "\".") );
		}
	}
}