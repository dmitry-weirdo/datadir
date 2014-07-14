package ru.datadir.database;

import ru.datadir.model.entity.Entity;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class DatabaseEntityUtils
{
	public static String getDatabaseTableName(Entity entity) {
		return entity.getName(); // todo: use entity id and package id instead (or also append package name for uniqueness or use id-dependent table name)
	}
	public static String getPrimaryKeyColumnName(Entity entity) {
		return "id"; // todo: use name which does not coincide with any of entity attribute's name
	}
	public static String getDatabaseSequenceName(Entity entity) {
		return concat( getDatabaseTableName(entity), "_seq" );
	}
}