package ru.pda.chemistry.service;

import junit.framework.TestCase;
import ru.pda.chemistry.entities.*;
import static ru.pda.chemistry.util.StringUtils.getConcatenation;

import java.sql.SQLException;
import java.util.List;

/**
 * User: 1
 * Date: 01.05.2010
 * Time: 7:10:39
 */
public class ConstructorServiceTest extends TestCase
{
	public ConstructorServiceTest() {
		service = ServiceFactory.getConstructorService();
		sb = new StringBuffer();
	}

	public void testSection() throws SQLException {
		Section section;
		Integer id;
		String name = getInexistingSectionName();

		try
		{
			// test createSection()
			section = new Section();
			section.setName(name);
			id = service.createSection(section);
			assertNotNull("Created section id is null", id);
		}
		catch (NotUniqueException e)
		{
			throw new IllegalStateException(getConcatenation(sb, "Section with name \"", name, "\" already exists."));
		}

		// test getSection() is null for inexisting section
		section = service.getSection(-666);
		assertNull("Getting section with negative id returned not null", section);

		// test getSection() for created section
		section = service.getSection(id);
		assertNotNull("Getting created section returned null", section);
		assertEquals("Name of created section gotten by does not match", name, section.getName());

		// testing created section present in sections list returned by getSections()
		List<Section> sections = service.getSections();
		assertNotSame("List of sections is empty, while at least one section was created", 0, sections.size()); // list must contain at least one created section

		boolean foundInList = false;
		int foundCountById = 0;
		int foundCountByName = 0;
		for (Section existingSection : sections)
		{
			if (existingSection.getId().equals(id))
			{
				foundInList = true;
				foundCountById++;
				assertEquals("Section found by id, but it's name does not match", name, existingSection.getName());
			}
			if (existingSection.getName().equals(name))
			{
				foundInList = true;
				foundCountByName++;
				assertEquals("Section found by name, but it's id does not match", id, existingSection.getId());
			}
		}

		assertEquals("Created section was not found in sections list", true, foundInList);
		assertEquals("Created section's id found not once in sections list", 1, foundCountById);
		assertEquals("Created section's name found not once in sections list", 1, foundCountByName);

		// test failing on creating section with name equal to created section
		boolean failedDuplicate = false;

		try
		{
			section.setName(name);
			service.createSection(section);
		}
		catch (NotUniqueException e)
		{
			failedDuplicate = true;
		}

		assertEquals("Creating section with duplicate name does not fail", true, failedDuplicate);

		// testing deleteSection() for created section
		service.deleteSection(id);

		section = service.getSection(id);
		assertNull("Deleted section is returned by id", section); // deleted section returned from service

		for (Section existingSection : service.getSections())
		{
			assertNotSame("Deleted section present in section list by id", id, existingSection.getId());
			assertNotSame("Deleted section present in section list by name", name, existingSection.getName());
		}
	}
	private String getInexistingSectionName() throws SQLException {
		String name = "test section тест";
		boolean exists = true;

		while (exists)
		{
			exists = false;

			for (Section section : service.getSections())
			{
				if (section.getName().equals(name))
				{
					exists = true;
					name = getConcatenation(sb, name, " 666");
					break;
				}
			}
		}

		return name;
	}

	public void testEntity() throws SQLException {
		Entity entity;
		Integer id = null;
		String name = getInexistingEntityName();

		Section section = new Section();
		Integer sectionId = null;
		String sectionName = getInexistingSectionName();
		List<Entity> sectionEntities;
		
		try
		{
			section.setName(sectionName);
			sectionId = service.createSection(section);
			section.setId(sectionId);
			assertNotNull("Created section id is null", sectionId);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}

		// test createSection fail on inexisting section
		boolean failedInexisingSection = false;

		try
		{
			entity = new Entity();
			id = service.createEntity(-666, entity);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch (ConstraintException e)
		{
			failedInexisingSection = true;
		}

		assertEquals("Creating entity does not fail on inexisting section", true, failedInexisingSection);

		// test createSection()
		try
		{
			entity = new Entity();
			entity.setName(name);
			id = service.createEntity(sectionId, entity);
			assertNotNull("Created entity's id is null", id);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch(ConstraintException	e)
		{
			fail(e.getMessage());
		}

		// test getEntity() is null for inexisting entity
		entity = service.getEntity(-666);
		assertNull("Getting entity with negative id returned not null", entity);

		// test getEntity() for created entity
		entity = service.getEntity(id);
		assertNotNull("Getting created entity returned null", entity);
		assertEquals("Name of created entity does not match", name, entity.getName());
		assertEquals("Section id of created entity does not match", sectionId, entity.getSection().getId());
		assertEquals("Section name of created entity does not match", sectionName, entity.getSection().getName());

		// test entity is present in section's entities
		sectionEntities = service.getEntities(sectionId);
		assertEquals("Size of list of section's entities is not 1, while entity for this section was created", 1, sectionEntities.size());

		boolean foundInList = false;
		int foundCountById = 0;
		int foundCountByName = 0;
		for (Entity sectionEntity : sectionEntities)
		{
			if (sectionEntity.getId().equals(id))
			{
				foundInList = true;
				foundCountById++;
				assertEquals("Entity found by id, but it's name does not match", name, sectionEntity.getName());
				assertEquals("Entity found by id, but it's section id does not match", sectionId, sectionEntity.getSection().getId());
				assertEquals("Entity found by id, but it's section name does not match", sectionName, sectionEntity.getSection().getName());
			}
			if (sectionEntity.getName().equals(name))
			{
				foundInList = true;
				foundCountByName++;
				assertEquals("Entity found by name, but it's id does not match", id, sectionEntity.getId());
				assertEquals("Entity found by name, but it's section id does not match", sectionId, sectionEntity.getSection().getId());
				assertEquals("Entity found by name, but it's section name does not match", sectionName, sectionEntity.getSection().getName());
			}
		}

		assertEquals("Created entity was not found in sections list", true, foundInList);
		assertEquals("Created entity's id found not once in sections list", 1, foundCountById);
		assertEquals("Created entity's name found not once in sections list", 1, foundCountByName);

		// test failing on creating entity with name equal to created entity
		boolean failedDuplicate = false;
		try
		{
			entity.setName(name);
			service.createEntity(sectionId, entity);
		}
		catch (NotUniqueException e)
		{
			failedDuplicate = true;
		}
		catch (ConstraintException e)
		{
			fail(e.getMessage());
		}

		assertEquals("Creating entity with duplicate name does not fail", true, failedDuplicate);

		// testing deleteEntity() for created entity
		service.deleteEntity(id);

		entity = service.getEntity(id);
		assertNull("Deleted entity is returned by id", entity); // deleted entity returned from service

		sectionEntities = service.getEntities(sectionId);
		assertEquals("List of section's entities is not empty, while the only entity for this section was deleted", 0, sectionEntities.size());

		// delete test section
		service.deleteSection(sectionId);

		section = service.getSection(sectionId);
		assertNull("Deleted section returned by id ", section);
	}
	private String getInexistingEntityName() throws SQLException {
		String name = "test entity тест";
		boolean exists = true;

		while (exists)
		{
			exists = false;

			for (Section section : service.getSections())
			{
				for (Entity entity : service.getEntities(section.getId()))
				{
					if (entity.getName().equals(name))
					{
						exists = true;
						name = getConcatenation(sb, name, " 666");
						break;
					}
				}
			}
		}

		return name;
	}

	public void testAttribute() throws SQLException {
		Attribute attribute;
		Integer id = null;
		String name = "test attribute тест";
		AttributeType type = AttributeType.BOOLEAN;
		String measureUnit = "√радусы цельси€";

		Entity entity = new Entity();
		Integer entityId = null;
		String entityName;
		List<Attribute> entityAttributes;

		Section section = new Section();
		Integer sectionId = null;
		String sectionName = getInexistingSectionName();


		try
		{
			section.setName(sectionName);
			sectionId = service.createSection(section);
			section.setId(sectionId);
			assertNotNull("Created section id is null", sectionId);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}

		entityName = getInexistingEntityName();

		try
		{
			entity.setName(entityName);
			entityId = service.createEntity(sectionId, entity);
			assertNotNull("Created entity's id is null", entityId);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch (ConstraintException e)
		{
			fail(e.getMessage());
		}

		// test createAttribute() fail on inexisting entity
		boolean failedInexistingEntity = false;

		try
		{
			attribute = new Attribute();
			id = service.createAttribute(-666, attribute);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch (ConstraintException e)
		{
			failedInexistingEntity = true;
		}

		assertEquals("Creating attribute does not fail on inexisting entity", true, failedInexistingEntity);
		
		// test createAttribute()
		try
		{
			attribute = new Attribute();
			attribute.setName(name);
			attribute.setType(type);
			attribute.setMeasureUnit(measureUnit);

			id = service.createAttribute(entityId, attribute);
			assertNotNull("Created attribute's id is null", id);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch (ConstraintException e)
		{
			fail(e.getMessage());
		}

		// test getAttribute() is null for inexising attribute
		entity = service.getEntity(-666);
		assertNull("Getting attribute with negative id returned not null", entity);

		// test getAttribute() for created attribute
		attribute = service.getAttribute(id);
		assertNotNull("Getting created attribute returned null", attribute);
		assertEquals("Name of created attribute does not match", name, attribute.getName());
		assertEquals("Type of created attribute does not match", type, attribute.getType());
		assertEquals("Measure unit of created attribute does not match", measureUnit, attribute.getMeasureUnit());
		assertEquals("Entity id of created attribute does not match", entityId, attribute.getEntity().getId());
		assertEquals("Entity name of created attribute does not match", entityName, attribute.getEntity().getName());
		assertEquals("Section id of created attribute does not match", sectionId, attribute.getEntity().getSection().getId());
		assertEquals("Section name of created attribute does not match", sectionName, attribute.getEntity().getSection().getName());

		// test attribute is present in entity's attributes
		entityAttributes = service.getAttributes(entityId);
		assertEquals("Size of list of entity's attributes is not 1, while attribute for this entity was created", 1, entityAttributes.size());

		boolean foundInList = false;
		int foundCountById = 0;
		int foundCountByName = 0;
		for (Attribute entityAttribute : entityAttributes)
		{
			if (entityAttribute.getId().equals(id))
			{
				foundInList = true;
				foundCountById++;
				assertEquals("Attribute found by id, but it's name does not match", name, entityAttribute.getName());
				assertEquals("Attribute found by id, but it's type does not match", type, entityAttribute.getType());
				assertEquals("Attribute found by id, but it's measure unit does not match", measureUnit, entityAttribute.getMeasureUnit());
				assertEquals("Attribute found by id, but it's entity id does not match", entityId, entityAttribute.getEntity().getId());
				assertEquals("Attribute found by id, but it's entity name does not match", entityName, entityAttribute.getEntity().getName());
				assertEquals("Attribute found by id, but it's section id does not match", sectionId, entityAttribute.getEntity().getSection().getId());
				assertEquals("Attribute found by id, but it's section name does not match", sectionName, entityAttribute.getEntity().getSection().getName());
			}
			if (entityAttribute.getName().equals(name))
			{
				foundInList = true;
				foundCountByName++;
				assertEquals("Attribute found by name, but it's id does not match", id, entityAttribute.getId());
				assertEquals("Attribute found by name, but it's type does not match", type, entityAttribute.getType());
				assertEquals("Attribute found by name, but it's measure unit does not match", measureUnit, entityAttribute.getMeasureUnit());
				assertEquals("Attribute found by name, but it's entity id does not match", entityId, entityAttribute.getEntity().getId());
				assertEquals("Attribute found by name, but it's entity name does not match", entityName, entityAttribute.getEntity().getName());
				assertEquals("Attribute found by name, but it's section id does not match", sectionId, entityAttribute.getEntity().getSection().getId());
				assertEquals("Attribute found by name, but it's section name does not match", sectionName, entityAttribute.getEntity().getSection().getName());
			}
		}

		assertEquals("Created attribute was not found in sections list", true, foundInList);
		assertEquals("Created attribute's id found not once in sections list", 1, foundCountById);
		assertEquals("Created attribute's name found not once in sections list", 1, foundCountByName);

		// test failing on create attribute with name equal to created attribute in the same entity
		boolean failedDuplicate = false;
		try
		{
			attribute.setName(name);
			service.createAttribute(entityId, attribute);
		}
		catch (NotUniqueException e)
		{
			failedDuplicate = true;
		}
		catch (ConstraintException e)
		{
			fail(e.getMessage());
		}

		assertEquals("Creating attribute with duplicate name for same entity does not fail", true, failedDuplicate);

		// testing deleteAttribute() for created attribute
		service.deleteAttribute(id);

		attribute = service.getAttribute(id);
		assertNull("Deleted attribute is returned by id", attribute); // deleted attribute returned from service

		entityAttributes = service.getAttributes(entityId);
		assertEquals("List of entity's attributes is not empty, while the only attribute for this entity was deleted", 0, entityAttributes.size());

		// delete test entity
		service.deleteEntity(entityId);

		entity = service.getEntity(entityId);
		assertNull("Deleted entity returned by id ", entity);

		// delete test section
		service.deleteSection(sectionId);

		section = service.getSection(sectionId);
		assertNull("Deleted section returned by id ", section);
	}

	public void testAttributeWithNullMeasureUnit() throws SQLException {
		Attribute attribute;
		Integer id = null;
		String name = "test attribute тест";
		AttributeType type = AttributeType.BOOLEAN;
		String measureUnit = null;

		Entity entity = new Entity();
		Integer entityId = null;
		String entityName;
		List<Attribute> entityAttributes;

		Section section = new Section();
		Integer sectionId = null;
		String sectionName = getInexistingSectionName();


		try
		{
			section.setName(sectionName);
			sectionId = service.createSection(section);
			section.setId(sectionId);
			assertNotNull("Created section id is null", sectionId);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}

		entityName = getInexistingEntityName();

		try
		{
			entity.setName(entityName);
			entityId = service.createEntity(sectionId, entity);
			assertNotNull("Created entity's id is null", entityId);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch (ConstraintException e)
		{
			fail(e.getMessage());
		}

		// test createAttribute fail on inexisting entity
		boolean failedInexistingEntity = false;

		try
		{
			attribute = new Attribute();
			id = service.createAttribute(-666, attribute);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch (ConstraintException e)
		{
			failedInexistingEntity = true;
		}

		assertEquals("Creating attribute does not fail on inexisting entity", true, failedInexistingEntity);

		// test createAttribute()
		try
		{
			attribute = new Attribute();
			attribute.setName(name);
			attribute.setType(type);
			attribute.setMeasureUnit(measureUnit);

			id = service.createAttribute(entityId, attribute);
			assertNotNull("Created attribute's id is null", id);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch (ConstraintException e)
		{
			fail(e.getMessage());
		}

		// test getAttribute() is null for inexising attribute
		entity = service.getEntity(-666);
		assertNull("Getting attribute with negative id returned not null", entity);

		// test getAttribute() for created attribute
		attribute = service.getAttribute(id);
		assertNotNull("Getting created attribute returned null", attribute);
		assertEquals("Name of created attribute does not match", name, attribute.getName());
		assertEquals("Type of created attribute does not match", type, attribute.getType());
		assertNull("Measure unit of created attribute does not match", attribute.getMeasureUnit());
		assertEquals("Entity id of created attribute does not match", entityId, attribute.getEntity().getId());
		assertEquals("Entity name of created attribute does not match", entityName, attribute.getEntity().getName());
		assertEquals("Section id of created attribute does not match", sectionId, attribute.getEntity().getSection().getId());
		assertEquals("Section name of created attribute does not match", sectionName, attribute.getEntity().getSection().getName());

		// test attribute is present in entity's attributes
		entityAttributes = service.getAttributes(entityId);
		assertEquals("Size of list of entity's attributes is not 1, while attribute for this entity was created", 1, entityAttributes.size());

		boolean foundInList = false;
		int foundCountById = 0;
		int foundCountByName = 0;
		for (Attribute entityAttribute : entityAttributes)
		{
			if (entityAttribute.getId().equals(id))
			{
				foundInList = true;
				foundCountById++;
				assertEquals("Attribute found by id, but it's name does not match", name, entityAttribute.getName());
				assertEquals("Attribute found by id, but it's type does not match", type, entityAttribute.getType());
				assertNull("Attribute found by id, but it's measure unit does not match", entityAttribute.getMeasureUnit());
				assertEquals("Attribute found by id, but it's entity id does not match", entityId, entityAttribute.getEntity().getId());
				assertEquals("Attribute found by id, but it's entity name does not match", entityName, entityAttribute.getEntity().getName());
				assertEquals("Attribute found by id, but it's section id does not match", sectionId, entityAttribute.getEntity().getSection().getId());
				assertEquals("Attribute found by id, but it's section name does not match", sectionName, entityAttribute.getEntity().getSection().getName());
			}
			if (entityAttribute.getName().equals(name))
			{
				foundInList = true;
				foundCountByName++;
				assertEquals("Attribute found by name, but it's id does not match", id, entityAttribute.getId());
				assertEquals("Attribute found by name, but it's type does not match", type, entityAttribute.getType());
				assertEquals("Attribute found by name, but it's measure unit does not match", measureUnit, entityAttribute.getMeasureUnit());
				assertEquals("Attribute found by name, but it's entity id does not match", entityId, entityAttribute.getEntity().getId());
				assertEquals("Attribute found by name, but it's entity name does not match", entityName, entityAttribute.getEntity().getName());
				assertEquals("Attribute found by name, but it's section id does not match", sectionId, entityAttribute.getEntity().getSection().getId());
				assertEquals("Attribute found by name, but it's section name does not match", sectionName, entityAttribute.getEntity().getSection().getName());
			}
		}

		assertEquals("Created attribute was not found in sections list", true, foundInList);
		assertEquals("Created attribute's id found not once in sections list", 1, foundCountById);
		assertEquals("Created attribute's name found not once in sections list", 1, foundCountByName);

		// test failing on create attribute with name equal to created attribute in the same entity
		boolean failedDuplicate = false;
		try
		{
			attribute.setName(name);
			service.createAttribute(entityId, attribute);
		}
		catch (NotUniqueException e)
		{
			failedDuplicate = true;
		}
		catch (ConstraintException e)
		{
			fail(e.getMessage());
		}

		assertEquals("Creating attribute with duplicate name for same entity does not fail", true, failedDuplicate);

		// testing deleteAttribute() for created attribute
		service.deleteAttribute(id);

		attribute = service.getAttribute(id);
		assertNull("Deleted attribute is returned by id", attribute); // deleted attribute returned from service

		entityAttributes = service.getAttributes(entityId);
		assertEquals("List of entity's attributes is not empty, while the only attribute for this entity was deleted", 0, entityAttributes.size());

		// delete test entity
		service.deleteEntity(entityId);

		entity = service.getEntity(entityId);
		assertNull("Deleted entity returned by id ", entity);

		// delete test section
		service.deleteSection(sectionId);

		section = service.getSection(sectionId);
		assertNull("Deleted section returned by id ", section);
	}

	public void testEnumValue() throws SQLException {
		EnumValue enumValue;
		Integer id = null;
		String name = "test enum value тест";

		Attribute attribute = new Attribute();
		Integer attributeId = null;
		String attributeName = "test attribute тест";
		AttributeType attributeType = AttributeType.ENUM;
		String attributeMeasureUnit = "√радусы цельси€";
		List<EnumValue> attributeEnumValues;

		Entity entity = new Entity();
		Integer entityId = null;
		String entityName = getInexistingEntityName();

		Section section = new Section();
		Integer sectionId = null;
		String sectionName = getInexistingSectionName();

		try
		{
			section.setName(sectionName);
			sectionId = service.createSection(section);
			section.setId(sectionId);
			assertNotNull("Created section id is null", sectionId);

			entity.setName(entityName);
			entityId = service.createEntity(sectionId, entity);
			assertNotNull("Created entity's id is null", entityId);

			attribute.setName(attributeName);
			attribute.setType(attributeType);
			attribute.setMeasureUnit(attributeMeasureUnit);
			attributeId = service.createAttribute(entityId, attribute);
			assertNotNull("Created attribute's id is null", attributeId);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch (ConstraintException e)
		{
			fail(e.getMessage());
		}

		// test createEnumValue() fail on inexisting attribute
		boolean failedInexistingAttribute = false;

		try
		{
			enumValue = new EnumValue();
			id = service.createEnumValue(-666, enumValue);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch (ConstraintException e)
		{
			failedInexistingAttribute = true;
		}

		assertEquals("Creating enum value does not fail on inexisting entity", true, failedInexistingAttribute);

		// test createEnumValue()
		try
		{
			enumValue = new EnumValue();
			enumValue.setName(name);

			id = service.createEnumValue(attributeId, enumValue);
			assertNotNull("Created enum value's id is null", id);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch (ConstraintException e)
		{
			fail(e.getMessage());
		}

		// test getEnumValue() is null for inexising enum value
		enumValue = service.getEnumValue(-666);
		assertNull("Getting enum value with negative id returned not null", enumValue);

		// test getEnumValue() for created attribute
		enumValue = service.getEnumValue(id);
		assertNotNull("Getting created enum value returned null", enumValue);
		assertEquals("Name of created enum value does not match", name, enumValue.getName());
		assertEquals("Attribute id of created enum value does not match", attributeId, enumValue.getAttribute().getId());
		assertEquals("Attribute name of created enum value does not match", attributeName, enumValue.getAttribute().getName());
		assertEquals("Entity id of created enum value does not match", entityId, enumValue.getAttribute().getEntity().getId());
		assertEquals("Entity name of created enum value does not match", entityName, enumValue.getAttribute().getEntity().getName());
		assertEquals("Section id of created enum value does not match", sectionId, enumValue.getAttribute().getEntity().getSection().getId());
		assertEquals("Section name of created enum value does not match", sectionName, enumValue.getAttribute().getEntity().getSection().getName());

		// test enum value is present in attribute's enum values
		attributeEnumValues = service.getEnumValues(attributeId);
		assertEquals("Size of list of attribute's enum values is not 1, while enum value for this attribute was created", 1, attributeEnumValues.size());

		boolean foundInList = false;
		int foundCountById = 0;
		int foundCountByName = 0;
		for (EnumValue attributeEnumValue : attributeEnumValues)
		{
			if (attributeEnumValue.getId().equals(id))
			{
				foundInList = true;
				foundCountById++;
				assertEquals("Enum value found by id, but it's name does not match", name, attributeEnumValue.getName());
				assertEquals("Enum value found by id, but it's attribute id does not match", attributeId, attributeEnumValue.getAttribute().getId());
				assertEquals("Enum value found by id, but it's attribute name does not match", attributeName, attributeEnumValue.getAttribute().getName());
				assertEquals("Enum value found by id, but it's entity id does not match", entityId, attributeEnumValue.getAttribute().getEntity().getId());
				assertEquals("Enum value found by id, but it's entity name does not match", entityName, attributeEnumValue.getAttribute().getEntity().getName());
				assertEquals("Enum value found by id, but it's section id does not match", sectionId, attributeEnumValue.getAttribute().getEntity().getSection().getId());
				assertEquals("Enum value found by id, but it's section name does not match", sectionName, attributeEnumValue.getAttribute().getEntity().getSection().getName());
			}
			if (attributeEnumValue.getName().equals(name))
			{
				foundInList = true;
				foundCountByName++;
				assertEquals("Enum value found by name, but it's id does not match", id, attributeEnumValue.getId());
				assertEquals("Enum value found by name, but it's attribute id does not match", attributeId, attributeEnumValue.getAttribute().getId());
				assertEquals("Enum value found by name, but it's attribute name does not match", attributeName, attributeEnumValue.getAttribute().getName());
				assertEquals("Enum value found by name, but it's entity id does not match", entityId, attributeEnumValue.getAttribute().getEntity().getId());
				assertEquals("Enum value found by name, but it's entity name does not match", entityName, attributeEnumValue.getAttribute().getEntity().getName());
				assertEquals("Enum value found by name, but it's section id does not match", sectionId, attributeEnumValue.getAttribute().getEntity().getSection().getId());
				assertEquals("Enum value found by name, but it's section name does not match", sectionName, attributeEnumValue.getAttribute().getEntity().getSection().getName());
			}
		}

		assertEquals("Created enum value was not found in attribute's list", true, foundInList);
		assertEquals("Created enum value's id found not once in attribute's list", 1, foundCountById);
		assertEquals("Created enum value's name found not once in attribute's list", 1, foundCountByName);

		// test failing on create enum value with name equal to created enum value for the same attribute
		boolean failedDuplicate = false;
		try
		{
			enumValue.setName(name);
			service.createEnumValue(attributeId, enumValue);
		}
		catch (NotUniqueException e)
		{
			failedDuplicate = true;
		}
		catch (ConstraintException e)
		{
			fail(e.getMessage());
		}

		assertEquals("Creating enum value with duplicate name for same attribute does not fail", true, failedDuplicate);

		// testing deleteEnumValue() for created enum value
		service.deleteEnumValue(id);

		enumValue = service.getEnumValue(id);
		assertNull("Deleted enum value is returned by id", enumValue); // deleted enum value returned from service

		attributeEnumValues = service.getEnumValues(attributeId);
		assertEquals("List of attribute's enum values is not empty, while the only enum value for this attribute was deleted", 0, attributeEnumValues.size());

		// delete test attribute
		service.deleteAttribute(attributeId);

		attribute = service.getAttribute(attributeId);
		assertNull("Deleted attribute returned by id ", attribute);
		
		// delete test entity
		service.deleteEntity(entityId);

		entity = service.getEntity(entityId);
		assertNull("Deleted entity returned by id ", entity);

		// delete test section
		service.deleteSection(sectionId);

		section = service.getSection(sectionId);
		assertNull("Deleted section returned by id ", section);
	}

	public void testEnumValueForNonEnumAttribute() throws SQLException {
		EnumValue enumValue;
		Integer id = null;
		String name = "test enum тест";

		Attribute attribute;
		Integer attributeId = null;
		String attributeName = "test attribute тест";
		AttributeType attributeType = AttributeType.DOUBLE;
		String attributeMeasureUnit = "√радусы цельси€";
		List<EnumValue> attributeEnumValues;

		Entity entity = new Entity();
		Integer entityId;
		String entityName = getInexistingEntityName();

		Section section = new Section();
		Integer sectionId = null;
		String sectionName = getInexistingSectionName();

		try
		{
			section.setName(sectionName);
			sectionId = service.createSection(section);
			section.setId(sectionId);
			assertNotNull("Created section id is null", sectionId);

			entity.setName(entityName);
			entityId = service.createEntity(sectionId, entity);
			assertNotNull("Created entity's id is null", entityId);

			attribute = new Attribute();
			attribute.setName(attributeName);
			attribute.setType(attributeType);
			attribute.setMeasureUnit(attributeMeasureUnit);
			attributeId = service.createAttribute(entityId, attribute);
			assertNotNull("Created attribute's id is null", entityId);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch (ConstraintException e)
		{
			fail(e.getMessage());
		}

		// test creation of enum value will fail for non-enum attribute type
		enumValue = new EnumValue();
		enumValue.setName(name);

		boolean failedForNonEnumAttribute = false;

		try
		{
			id = service.createEnumValue(attributeId, enumValue);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch (ConstraintException e)
		{
			failedForNonEnumAttribute = true;
		}

		assertEquals("Creating enum value for non-enum attribute does not fail", true, failedForNonEnumAttribute);
		assertNull("Created enum value id is not null for attribute with non-enum type", id); // must be null because type of attribute is not ENUM

		attributeEnumValues = service.getEnumValues(attributeId);
		assertEquals("List of attribute's enum values is not empty, while no enum values must have been were added", 0, attributeEnumValues.size());

		enumValue = service.getEnumValue(id);
		assertNull("Getting created enum value1 returned not null", enumValue);

		// delete test section (it's entities with their attributes will also be deleted)
		service.deleteSection(sectionId);
	}

	public void testCascadeEnumValueDelete() throws SQLException {
		EnumValue enumValue1;
		Integer id1 = null;
		String name1 = "test enum тест 1";

		EnumValue enumValue2;
		Integer id2 = null;
		String name2 = "test enum тест 666 -2";

		Attribute attribute;
		Integer attributeId = null;
		String attributeName = "test attribute тест";
		AttributeType attributeType = AttributeType.ENUM;
		String attributeMeasureUnit = "√радусы цельси€";
		List<EnumValue> attributeEnumValues;

		Entity entity = new Entity();
		Integer entityId;
		String entityName = getInexistingEntityName();

		Section section = new Section();
		Integer sectionId = null;
		String sectionName = getInexistingSectionName();

		try
		{
			section.setName(sectionName);
			sectionId = service.createSection(section);
			section.setId(sectionId);
			assertNotNull("Created section id is null", sectionId);

			entity.setName(entityName);
			entityId = service.createEntity(sectionId, entity);
			assertNotNull("Created entity's id is null", entityId);

			attribute = new Attribute();
			attribute.setName(attributeName);
			attribute.setType(attributeType);
			attribute.setMeasureUnit(attributeMeasureUnit);
			attributeId = service.createAttribute(entityId, attribute);
			assertNotNull("Created attribute's id is null", entityId);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch (ConstraintException e)
		{
			fail(e.getMessage());
		}

		enumValue1 = new EnumValue();
		enumValue1.setName(name1);

		enumValue2 = new EnumValue();
		enumValue2.setName(name2);

		try
		{
			id1 = service.createEnumValue(attributeId, enumValue1);
			assertNotNull("Created enum value1's id is null", id1);

			id2 = service.createEnumValue(attributeId, enumValue2);
			assertNotNull("Created enum value1's id is null", id2);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch (ConstraintException e)
		{
			fail(e.getMessage());
		}

		attributeEnumValues = service.getEnumValues(attributeId);
		assertEquals("Size of list of attribute's enum values is not 2, while 2 enum values for this attribute were added", 2, attributeEnumValues.size());

		enumValue1 = service.getEnumValue(id1);
		assertNotNull("Getting created enum value1 returned null", enumValue1);

		enumValue2 = service.getEnumValue(id2);
		assertNotNull("Getting created enum value2 returned null", enumValue2);

		// test deleteAttribute() will also cascadely delete attribute's enum values
		service.deleteAttribute(attributeId);
		attribute = service.getAttribute(attributeId);
		assertNull("Deleted attribute is returned by id ", attribute);

		enumValue1 = service.getEnumValue(id1);
		assertNull("Deleted attribute's enum value is returned by id", enumValue1); // deleted attribute's enum value returned from service

		enumValue2 = service.getEnumValue(id2);
		assertNull("Deleted attribute's enum value is returned by id", enumValue2); // deleted attribute's enum value returned from service

		// delete test section (it's entities with their attributes will also be deleted)
		service.deleteSection(sectionId);

		section = service.getSection(sectionId);
		assertNull("Deleted section returned by id ", section);
	}

	public void testCascadeAttributeDelete() throws SQLException {
		EnumValue enumValue1;
		String enumValueName1 = "test enum value тест 1";
		Integer id1 = null;

		EnumValue enumValue2;
		String enumValueName2 = "test enum value тест 2 абыр";
		Integer id2 = null;

		Attribute attribute1;
		Integer attributeId1 = null;
		String attributeName1 = "test attribute тест";
		AttributeType attributeType1 = AttributeType.ENUM;
		String attributeMeasureUnit1 = "√радусы цельси€";
		List<EnumValue> attributeEnumValues1;

		Attribute attribute2;
		Integer attributeId2 = null;
		String attributeName2 = "test attribute тест 666 абыр¬алг";
		AttributeType attributeType12 = AttributeType.INTEGER;
		String attributeMeasureUnit2 = null;
		List<EnumValue> attributeEnumValues2;

		Entity entity = new Entity();
		Integer entityId = null;
		String entityName = getInexistingEntityName();
		List<Attribute> entityAttributes;

		Section section = new Section();
		Integer sectionId = null;
		String sectionName = getInexistingSectionName();

		try
		{
			section.setName(sectionName);
			sectionId = service.createSection(section);
			section.setId(sectionId);
			assertNotNull("Created section id is null", sectionId);

			entity.setName(entityName);
			entityId = service.createEntity(sectionId, entity);
			assertNotNull("Created entity's id is null", entityId);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch (ConstraintException e)
		{
			fail(e.getMessage());
		}


		try
		{
			attribute1 = new Attribute();
			attribute1.setName(attributeName1);
			attribute1.setType(attributeType1);
			attribute1.setMeasureUnit(attributeMeasureUnit1);
			attributeId1 = service.createAttribute(entityId, attribute1);
			assertNotNull("Created attribute1's id is null", attributeId1);

			attribute2 = new Attribute();
			attribute2.setName(attributeName2);
			attribute2.setType(attributeType12);
			attribute2.setMeasureUnit(attributeMeasureUnit2);
			attributeId2 = service.createAttribute(entityId, attribute2);
			assertNotNull("Created attribute2's id is null", attributeId2);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch (ConstraintException e)
		{
			fail(e.getMessage());
		}

		entityAttributes = service.getAttributes(entityId);
		assertEquals("Size of list of entity's attributes is not 2, while 2 attributes for this entity were added", 2, entityAttributes.size());

		attribute1 = service.getAttribute(attributeId1);
		assertNotNull("Getting created attribute1 returned null", attribute1);

		attribute2 = service.getAttribute(attributeId2);
		assertNotNull("Getting created attribute2 returned null", attribute2);


		try
		{
			enumValue1 = new EnumValue();
			enumValue1.setName(enumValueName1);
			id1 = service.createEnumValue(attributeId1, enumValue1);

			enumValue2 = new EnumValue();
			enumValue2.setName(enumValueName2);
			id2 = service.createEnumValue(attributeId1, enumValue2);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch (ConstraintException e)
		{
			fail(e.getMessage());
		}

		attributeEnumValues1 = service.getEnumValues(attributeId1);
		assertEquals("Size of list of attribute1's enum values is not 2, while 2 enum values for this attribute were added", 2, attributeEnumValues1.size());

		attributeEnumValues2 = service.getEnumValues(attributeId2);
		assertEquals("List of attribute2's enum values is not empty, while no enum values for this attribute were added", 0, attributeEnumValues2.size());

		enumValue1 = service.getEnumValue(id1);
		assertNotNull("Getting created enum value1 returned null", enumValue1);

		enumValue2 = service.getEnumValue(id2);
		assertNotNull("Getting created enum value2 returned null", enumValue2);

		// test deleteEntity() will also cascadely delete entity's attribute
		service.deleteEntity(entityId);
		entity = service.getEntity(entityId);
		assertNull("Deleted entity is returned by id ", entity);
		
		attribute1 = service.getAttribute(attributeId1);
		assertNull("Deleted entity's attribute1 is returned by id", attribute1); // deleted entity's attribute returned from service

		attribute2 = service.getAttribute(attributeId2);
		assertNull("Deleted entity's attribute2 is returned by id", attribute2); // deleted entity's attribute returned from service

		enumValue1 = service.getEnumValue(id1);
		assertNull("Deleted entity's attribute's enum value1 is returned by id", enumValue1); // deleted entity's attribute's enum value returned from service

		enumValue2 = service.getEnumValue(id2);
		assertNull("Deleted entity's attribute's enum value2 is returned by id", enumValue2); // deleted entity's attribute's enum value returned from service

		// delete test section
		service.deleteSection(sectionId);

		section = service.getSection(sectionId);
		assertNull("Deleted section returned by id ", section);
	}

	public void testCascadeEntityDelete() throws SQLException {
		EnumValue enumValue1;
		String enumValueName1 = "test enum value тест 1";
		Integer id1 = null;

		EnumValue enumValue2;
		String enumValueName2 = "test enum value тест 2 абыр";
		Integer id2 = null;

		Attribute attribute1;
		Integer attributeId1 = null;
		String attributeName1 = "test attribute тест";
		AttributeType attributeType1 = AttributeType.ENUM;
		String attributeMeasureUnit1 = "√радусы цельси€";
		List<EnumValue> attributeEnumValues1;

		Attribute attribute2;
		Integer attributeId2 = null;
		String attributeName2 = "test attribute тест 666 абыр¬алг";
		AttributeType attributeType2 = AttributeType.INTEGER;
		String attributeMeasureUnit2 = null;
		List<EnumValue> attributeEnumValues2;

		Entity entity1 = new Entity();
		Integer entityId1 = null;
		String entityName1;
		List<Attribute> entityAttributes1;

		Entity entity2 = new Entity();
		Integer entityId2 = null;
		String entityName2;
		List<Attribute> entityAttributes2;

		Section section = new Section();
		Integer sectionId = null;
		String sectionName = getInexistingSectionName();

		try
		{
			section.setName(sectionName);
			sectionId = service.createSection(section);
			section.setId(sectionId);
			assertNotNull("Created section's id is null", sectionId);

			entityName1 = getInexistingEntityName();
			entity1.setName(entityName1);
			entityId1 = service.createEntity(sectionId, entity1);
			assertNotNull("Created entity1's id is null", entityId1);

			entityName2 = getInexistingEntityName();
			entity2.setName(entityName2);
			entityId2 = service.createEntity(sectionId, entity2);
			assertNotNull("Created entity2's id is null", entityId2);

			attribute1 = new Attribute();
			attribute1.setName(attributeName1);
			attribute1.setType(attributeType1);
			attribute1.setMeasureUnit(attributeMeasureUnit1);
			attributeId1 = service.createAttribute(entityId1, attribute1);
			assertNotNull("Created attribute1's id is null", attributeId1);

			attribute2 = new Attribute();
			attribute2.setName(attributeName2);
			attribute2.setType(attributeType2);
			attribute2.setMeasureUnit(attributeMeasureUnit2);
			attributeId2 = service.createAttribute(entityId1, attribute2);
			assertNotNull("Created attribute2's id is null", attributeId2);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch (ConstraintException e)
		{
			fail(e.getMessage());
		}

		entityAttributes1 = service.getAttributes(entityId1);
		assertEquals("Size of list of entity1's attributes is not 2, while 2 attributes for this entity were added", 2, entityAttributes1.size());

		entityAttributes2 = service.getAttributes(entityId2);
		assertEquals("List of entity2's attributes is not empty, while ", 0, entityAttributes2.size());

		attribute1 = service.getAttribute(attributeId1);
		assertNotNull("Getting created attribute1 returned null", attribute1);

		attribute2 = service.getAttribute(attributeId2);
		assertNotNull("Getting created attribute1 returned null", attribute2);

		try
		{
			enumValue1 = new EnumValue();
			enumValue1.setName(enumValueName1);
			id1 = service.createEnumValue(attributeId1, enumValue1);

			enumValue2 = new EnumValue();
			enumValue2.setName(enumValueName2);
			id2 = service.createEnumValue(attributeId1, enumValue2);
		}
		catch (NotUniqueException e)
		{
			fail(e.getMessage());
		}
		catch (ConstraintException e)
		{
			fail(e.getMessage());
		}

		attributeEnumValues1 = service.getEnumValues(attributeId1);
		assertEquals("Size of list of attribute1's enum values is not 2, while 2 enum values for this attribute were added", 2, attributeEnumValues1.size());

		attributeEnumValues2 = service.getEnumValues(attributeId2);
		assertEquals("List of attribute2's enum values is not empty, while no enum values for this attribute were added", 0, attributeEnumValues2.size());

		enumValue1 = service.getEnumValue(id1);
		assertNotNull("Getting created enum value1 returned null", enumValue1);

		enumValue2 = service.getEnumValue(id2);
		assertNotNull("Getting created enum value2 returned null", enumValue2);

		// test deleteSection will also cascadely delete section's entities and entity's attributes  
		service.deleteSection(sectionId);
		section = service.getSection(sectionId);
		assertNull("Deleted section returned by id ", section);

		entity1 = service.getEntity(entityId1);
		assertNull("Deleted section's entity returned by id ", entity1);

		entity2 = service.getEntity(entityId2);
		assertNull("Deleted section's entity returned by id ", entity2);

		attribute1 = service.getAttribute(attributeId1);
		assertNull("Deleted section's entity1's attribute1 is returned by id", attribute1); // deleted section's entity's attribute returned from service

		attribute2 = service.getAttribute(attributeId2);
		assertNull("Deleted section's entity1's attribute2 is returned by id", attribute2); // deleted section's entity's attribute returned from service

		enumValue1 = service.getEnumValue(id1);
		assertNull("Deleted section's entity1's attribute1's enum value1 is returned by id", enumValue1); // deleted section's entity's attribute's enum value returned from service

		enumValue2 = service.getEnumValue(id2);
		assertNull("Deleted entity1's attribute1's enum value2 is returned by id", enumValue2); // deleted section's entity's attribute's enum value returned from service
	}

	public void testDeleteInexisting() throws SQLException {
		int sectionsCount = getTotalSectionsCount();
		int entitiesCount = getTotalEntitiesCount();
		int attributesCount = getTotalAttributesCount();
		int enumValuesCount = getTotalEnumValuesCount();

		// test deleting with null values
		service.deleteSection(null);
		service.deleteEntity(null);
		service.deleteAttribute(null);

		assertEquals("Sections count changed after calling deleteSection with null id", sectionsCount, getTotalSectionsCount());
		assertEquals("Entities count changed after calling deleteEntity with null id", entitiesCount, getTotalEntitiesCount());
		assertEquals("Attributes count changed after calling deleteAttribute with null id", attributesCount, getTotalAttributesCount());
		assertEquals("Enum values count changed after calling deleteEnumValue with null id", enumValuesCount, getTotalEnumValuesCount());

		service.deleteSection(-666);
		service.deleteEntity(-666);
		service.deleteAttribute(-666);
		service.deleteEnumValue(-666);

		assertEquals("Sections count changed after calling deleteSection with negative id", sectionsCount, getTotalSectionsCount());
		assertEquals("Entities count changed after calling deleteEntity with negative id", entitiesCount, getTotalEntitiesCount());
		assertEquals("Attributes count changed after calling deleteAttribute with negative id", attributesCount, getTotalAttributesCount());
		assertEquals("Enum values count changed after calling deleteEnumValue with negative id", enumValuesCount, getTotalEnumValuesCount());
	}
	private int getTotalSectionsCount() throws SQLException {
		return service.getSections().size();
	}
	private int getTotalEntitiesCount() throws SQLException {
		int count = 0;

		List<Section> sections = service.getSections();
		for (Section section : sections)
			count += service.getEntities(section.getId()).size();

		return count;
	}
	private int getTotalAttributesCount() throws SQLException {
		int count = 0;

		List<Section> sections = service.getSections();
		for (Section section : sections)
			for (Entity entity : service.getEntities(section.getId()))
				count += service.getAttributes(entity.getId()).size();

		return count;
	}
	private int getTotalEnumValuesCount() throws SQLException {
		int count = 0;

		List<Section> sections = service.getSections();
		for (Section section : sections)
			for (Entity entity : service.getEntities(section.getId()))
				for (Attribute attribute : service.getAttributes(entity.getId()))
					count += service.getEnumValues(attribute.getId()).size();

		return count;
	}

	public void testNullIdCalls() throws SQLException {
		// test getting by id with null ids
		assertNull("getSection by null id returned not null", service.getSection(null));
		assertNull("getEntity by null id returned not null", service.getEntity(null));
		assertNull("getAttribute by null id returned not null", service.getAttribute(null));
		assertNull("getEnumValue by null id returned not null", service.getEnumValue(null));

		// test deleting with null values
		service.deleteSection(null);
		service.deleteEntity(null);
		service.deleteAttribute(null);
		service.deleteEnumValue(null);

		// test getting entities by section's null id
		assertEquals("getEntities by null section id returned not empty collection", 0, service.getEntities(null).size());
		
		// test getting attributes by entity's null id
		assertEquals("getAttributes by null entity id returned not empty collection", 0, service.getAttributes(null).size());

		// test getting enum values by attributes's null id
		assertEquals("getEnumValues by null attribute id returned not empty collection", 0, service.getEnumValues(null).size());
	}


	private ConstructorService service;
	private StringBuffer sb;
}