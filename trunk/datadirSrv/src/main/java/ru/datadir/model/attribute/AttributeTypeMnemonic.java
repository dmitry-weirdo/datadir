package ru.datadir.model.attribute;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */

/**
 * Мнемоники типов данных.
 */
public enum AttributeTypeMnemonic
{
	/**
	 * Строка.
	 */
	STRING,

	/**
	 * Целое число.
	 */
	LONG,

	/**
	 * Вещественное число.
	 */
	DOUBLE,

	/**
	 * Булево значение (истина/ложь).
	 */
	BOOLEAN,

	/**
	 * Символ.
	 */
	CHARACTER,

	/**
	 * Файл. Байты файла и метаданные о&nbsp;файле.
	 */
	FILE,

	/**
	 * Набор байт неограниченного размера.
	 */
	BLOB,

	/**
	 * Дата, без&nbsp;времени.
	 */
	DATE,

	/**
	 * Дата и время.
	 */
	DATETIME,

	/**
	 * Ссылка на&nbsp;другую сущность (справочник) или&nbsp;на&nbsp;перечисление.
	 */
	LINK,
}