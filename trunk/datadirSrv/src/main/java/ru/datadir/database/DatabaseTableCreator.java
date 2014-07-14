package ru.datadir.database;

import ru.datadir.model.entity.Entity;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public interface DatabaseTableCreator
{
	String getCreateTableScript(Entity entity);

	String getDropTableScript(Entity entity);
}