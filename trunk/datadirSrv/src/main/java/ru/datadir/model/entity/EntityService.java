package ru.datadir.model.entity;

import javax.ejb.Local;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
@Local
public interface EntityService
{
	public void executeCreateTable(String tableName);
}