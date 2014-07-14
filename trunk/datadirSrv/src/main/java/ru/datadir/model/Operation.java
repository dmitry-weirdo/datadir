package ru.datadir.model;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */

/**
 * Возможные операции с&nbsp;сущностями и&nbsp;разделами.
 */
public enum Operation
{
	/**
	 * Создание.
 	 */
	  create

	/**
	 * Чтение. Как списка сущностей, так и отдельной сущности.
	 */
	, read

	/**
	 * Изменение.
	 */
	, update

	/**
	 * Удаление.
	 */
	, delete
}