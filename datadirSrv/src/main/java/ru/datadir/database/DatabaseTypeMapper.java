package ru.datadir.database;

import ru.datadir.model.attribute.AttributeTypeMnemonic;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public interface DatabaseTypeMapper
{
	String getDatabaseFieldType(AttributeTypeMnemonic type);
}