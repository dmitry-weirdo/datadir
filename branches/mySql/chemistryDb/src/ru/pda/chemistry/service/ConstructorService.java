package ru.pda.chemistry.service;

import ru.pda.chemistry.entities.*;

import java.util.List;
import java.sql.SQLException;

/**
 * User: 1
 * Date: 27.04.2010
 * Time: 0:24:23
 */
public interface ConstructorService
{
	List<Section> getSections() throws SQLException;

	Section getSection(Integer id) throws SQLException;

	Integer createSection(Section section) throws SQLException, NotUniqueException;

	void deleteSection(Integer id) throws SQLException;


	List<Entity> getEntities(Integer sectionId) throws SQLException;

	Entity getEntity(Integer id) throws SQLException;

	Integer createEntity(Integer sectionId, Entity entity) throws SQLException, NotUniqueException, ConstraintException;

	void deleteEntity(Integer id) throws SQLException;


	List<Attribute> getAttributes(Integer entityId) throws SQLException;

	Attribute getAttribute(Integer id) throws SQLException;

	Integer createAttribute(Integer entityId, Attribute attribute) throws SQLException, NotUniqueException, ConstraintException;

	void deleteAttribute(Integer id) throws SQLException;


	List<EnumValue> getEnumValues(Integer attributeId) throws SQLException;

	EnumValue getEnumValue(Integer id) throws SQLException;

	Integer createEnumValue(Integer attributeId, EnumValue enumValue) throws SQLException, NotUniqueException, ConstraintException;

	void deleteEnumValue(Integer id) throws SQLException;
}