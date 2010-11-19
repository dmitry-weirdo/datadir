package ru.pda.chemistry.service;

import ru.pda.chemistry.entities.Entity;
import ru.pda.chemistry.entities.EntityValue;
import ru.pda.chemistry.entities.attributeValue.AttributeValue;

import java.sql.SQLException;
import java.util.List;

/**
 * User: 1
 * Date: 03.06.2010
 * Time: 22:02:35
 */
public interface GeneratorService
{
	void generateTable(Entity entity) throws SQLException;

	void deleteTable(Entity entity) throws SQLException;


	List<EntityValue> getEntityValues(Entity entity) throws SQLException;

	EntityValue getEntityValue(Entity entity, Integer id) throws SQLException;

	void deleteEntityValue(Entity entity, Integer id) throws SQLException;

	
	Integer createEntityValue(Entity entity, String source) throws SQLException;

	void setAttributeValue(Integer entityValueId, AttributeValue value) throws SQLException;

	void setAttributeValues(Integer entityValueId, List<AttributeValue> values) throws SQLException;

	void setSource(Entity entity, Integer entityValueId, String source) throws SQLException;
}