package ru.pda.chemistry.service;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ru.pda.chemistry.entities.*;
import ru.pda.chemistry.entities.attributeValue.*;
import ru.pda.chemistry.sql.SqlConnector;
import static ru.pda.chemistry.util.StringUtils.getConcatenation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: 1
 * Date: 03.06.2010
 * Time: 22:16:45
 */
public class GeneratorServiceTest
{
	@Before
	public void init() {
		constructorService = ServiceFactory.getConstructorService();
		service = ServiceFactory.getGeneratorService();
		sb = new StringBuffer();
	}

	@Test
	public void testGenerateTable() throws SQLException, NotUniqueException, ConstraintException {
		Section section = new Section();
		section.setName("test section");
		Integer sectionId = constructorService.createSection(section);

		Entity entity = new Entity();
		entity.setName("test entity");
		Integer entityId = constructorService.createEntity(sectionId, entity);

    Attribute booleanAttribute = new Attribute();
		booleanAttribute.setType(AttributeType.BOOLEAN);
		booleanAttribute.setName("boolean attr");
		booleanAttribute.setMeasureUnit("boolean measure unit");
		Integer booleanAttributeId = constructorService.createAttribute(entityId, booleanAttribute);

		Attribute doubleAttribute = new Attribute();
		doubleAttribute.setType(AttributeType.DOUBLE);
		doubleAttribute.setName("double attr");
		doubleAttribute.setMeasureUnit("double measure unit");
		Integer doubleAttributeId = constructorService.createAttribute(entityId, doubleAttribute);

		Attribute intAttribute = new Attribute();
		intAttribute.setType(AttributeType.INTEGER);
		intAttribute.setName("int attr");
		intAttribute.setMeasureUnit("int measure unit");
		Integer intAttributeId = constructorService.createAttribute(entityId, intAttribute);

		Attribute stringAttribute = new Attribute();
		stringAttribute.setType(AttributeType.STRING);
		stringAttribute.setName("string attr");
		stringAttribute.setMeasureUnit("string measure unit");
		Integer stringAttributeId = constructorService.createAttribute(entityId, stringAttribute);

		Attribute enumAttribute = new Attribute();
		enumAttribute.setType(AttributeType.ENUM);
		enumAttribute.setName("enum attr");
		enumAttribute.setMeasureUnit("enum measure unit");
		Integer enumAttributeId = constructorService.createAttribute(entityId, enumAttribute);

		// values for enumAttribute
		EnumValue enumValue;

		enumValue = new EnumValue();
		enumValue.setName("enum value 1");
		Integer enumValueId1 = constructorService.createEnumValue(enumAttributeId, enumValue);

		enumValue = new EnumValue();
		enumValue.setName("enum value 2");
		Integer enumValueId2 = constructorService.createEnumValue(enumAttributeId, enumValue);


		entity = constructorService.getEntity(entityId); // for filling attributes and enum values

		try
		{
			service.generateTable(entity);
			fail("generateTable() didn't fail on entity with no attributes");
		}
		catch (IllegalArgumentException e)
		{
			// ok
		}


		entity.setAttributes(constructorService.getAttributes(entityId));

		service.generateTable(entity);


		// check that table_name and generator_name is not null
		entity = constructorService.getEntity(entityId);
		assertNotNull("table name is null, not set after generateTable()", entity.getTableName());
		assertNotNull("generator name is null, not set after generateTable()", entity.getGeneratorName());

		// check that attribute names are not null
		List<Attribute> attributes = constructorService.getAttributes(entityId);
		for (Attribute attribute : attributes)
			assertNotNull("attribute column name is null, not set after generateTable(): " + attribute.getColumnName());


		// check that column names are set for each attribute of the entity
		booleanAttribute = constructorService.getAttribute(booleanAttributeId);
		assertNotNull("boolean attribute column name is null", booleanAttribute.getColumnName());

		doubleAttribute = constructorService.getAttribute(doubleAttributeId);
		assertNotNull("double attribute column name is null", doubleAttribute.getColumnName());

		intAttribute = constructorService.getAttribute(intAttributeId);
		assertNotNull("int attribute column name is null", intAttribute.getColumnName());

		stringAttribute = constructorService.getAttribute(stringAttributeId);
		assertNotNull("string attribute column name is null", stringAttribute.getColumnName());

		enumAttribute = constructorService.getAttribute(enumAttributeId);
		assertNotNull("enum attribute column name is null", enumAttribute.getColumnName());


		// try to insert entity values
		SqlConnector connector = SqlConnector.create();
		getConcatenation(sb,
			"insert into ", entity.getTableName(),
			"(",
				"id, ",
				"source, ",
				booleanAttribute.getColumnName(), ", ",
				doubleAttribute.getColumnName(), ", ",
				intAttribute.getColumnName(), ", ",
				stringAttribute.getColumnName(), ", ",
				enumAttribute.getColumnName(),
			") ",
			"values",
			"(",
				"gen_id(", entity.getGeneratorName(), ", 1), ", // id
				"'some mega source'", ", ", // source
				"1", ", ", // bool
				"123.33", ", ", // double
				"8760", ", ", // int
				"'some string value'", ", ", // string
				enumValueId1.toString(), // enum value (foreign key of enum table
			")"
		);

		connector.executeUpdate(sb.toString());

		getConcatenation(sb,
			"insert into ", entity.getTableName(),
			"(",
				"id, ",
				"source, ",
				booleanAttribute.getColumnName(), ", ",
				doubleAttribute.getColumnName(), ", ",
				intAttribute.getColumnName(), ", ",
				stringAttribute.getColumnName(), ", ",
				enumAttribute.getColumnName(),
			") ",
			"values",
			"(",
				"gen_id(", entity.getGeneratorName(), ", 1), ", // id
				"'super great source'", ", ", // source
				"0", ", ", // bool
				"-345.51", ", ", // double
				"-9623", ", ", // int
				"'some other string value'", ", ", // string
				enumValueId2.toString(), // enum value (foreign key of enum table
			")"
		);

		connector.executeUpdate(sb.toString());

		// try to install incorrect enumValueId (of inexisting value) - check that foreign key constraint has been added
		try
		{
			getConcatenation(sb,
			"insert into ", entity.getTableName(),
				"(",
					"id, ",
					"source, ",
					booleanAttribute.getColumnName(), ", ",
					doubleAttribute.getColumnName(), ", ",
					intAttribute.getColumnName(), ", ",
					stringAttribute.getColumnName(), ", ",
					enumAttribute.getColumnName(),
				") ",
				"values",
				"(",
					"gen_id(", entity.getGeneratorName(), ", 1), ", // id
					"'super mega source'", ", ", // source
					"0", ", ", // bool
					"-345.51", ", ", // double
					"-9623", ", ", // int
					"'some other string value'", ", ", // string
					"-1", // enum value (foreign key of enum table
				") "
			);

			connector.executeUpdate(sb.toString());

			fail("Adding link from entity table to inexisting enum value did not throw an exception");
		}
		catch (SQLException e)
		{
			// ok, this must have failed
		}


		constructorService.deleteEntity(entityId); // attributes are deleted cascadely
		constructorService.deleteSection(sectionId);
	}

	@Test
	public void testDeleteTable() throws SQLException, NotUniqueException, ConstraintException {
		Section section = new Section();
		section.setName("test section");
		Integer sectionId = constructorService.createSection(section);

		Entity entity = new Entity();
		entity.setName("test entity");
		Integer entityId = constructorService.createEntity(sectionId, entity);

    Attribute booleanAttribute = new Attribute();
		booleanAttribute.setType(AttributeType.BOOLEAN);
		booleanAttribute.setName("boolean attr");
		booleanAttribute.setMeasureUnit("boolean measure unit");
		Integer booleanAttributeId = constructorService.createAttribute(entityId, booleanAttribute);


		entity = constructorService.getEntity(entityId); // for filling attributes and enum values
		entity.setAttributes(constructorService.getAttributes(entityId));

		// check that table_name and generator_name is not null
		service.generateTable(entity);
		entity = constructorService.getEntity(entityId);
		String tableName = entity.getTableName(); // save for usage after deletion
		assertNotNull("table name is null, not set after generateTable()", entity.getTableName());
		assertNotNull("generator name is null, not set after generateTable()", entity.getGeneratorName());
		assertFalse("entity is editable, although entity's table was generated", entity.isEditable());

		// check that column names are set for each attribute of the entity
		booleanAttribute = constructorService.getAttribute(booleanAttributeId);
		assertNotNull("boolean attribute column name is null", booleanAttribute.getColumnName());

		SqlConnector connector = SqlConnector.create();
		getConcatenation(sb,
			"insert into ", entity.getTableName(),
			"(",
				"id, ",
				"source, ",
				booleanAttribute.getColumnName(),
			") ",
			"values",
			"(",
				"gen_id(", entity.getGeneratorName(), ", 1), ", // id
				"'some mega source'", ", ", // source
				"1",// bool
			")"
		);

		try
		{
			connector.executeUpdate(sb.toString());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			fail("insertion into created entity table failed");
		}

		// check that table_name is null and generator_name is null
		service.deleteTable(entity);
		entity = constructorService.getEntity(entityId);
		assertNull("table name is not null, not deleted after generateTable()", entity.getTableName());
		assertNull("generator name is not null, not deleted after generateTable()", entity.getGeneratorName());
		assertTrue("entity is not editable, although entity's table was dropped", entity.isEditable());

		// try to insert into old entity table
		getConcatenation(sb,
			"insert into ", tableName,
			"(",
				"id, ",
				"source, ",
				booleanAttribute.getColumnName(),
			") ",
			"values",
			"(",
				"gen_id(", entity.getGeneratorName(), ", 1), ", // id
				"'some mega source'", ", ", // source
				"1",// bool
			")"
		);

		try
		{
			connector.executeUpdate(sb.toString());
			fail("insertion into deleted entity table did not fail");
		}
		catch (SQLException e)
		{
			// ok, this must have failed
		}

		constructorService.deleteEntity(entityId); // attributes are deleted cascadely
		constructorService.deleteSection(sectionId);
	}

	@Test
	public void testSetSource() throws SQLException, NotUniqueException, ConstraintException {
		Section section = new Section();
		section.setName("test section");
		Integer sectionId = constructorService.createSection(section);

		Entity entity = new Entity();
		entity.setName("test entity");
		Integer entityId = constructorService.createEntity(sectionId, entity);

    Attribute booleanAttribute = new Attribute();
		booleanAttribute.setType(AttributeType.BOOLEAN);
		booleanAttribute.setName("boolean attr");
		booleanAttribute.setMeasureUnit("boolean measure unit");
		Integer booleanAttributeId = constructorService.createAttribute(entityId, booleanAttribute);


		entity = constructorService.getEntity(entityId); // for filling attributes and enum values
		entity.setAttributes(constructorService.getAttributes(entityId));

		// check that table_name is not null and generator name is not null
		service.generateTable(entity);
		entity = constructorService.getEntity(entityId);
		entity.setAttributes(constructorService.getAttributes(entityId));

		assertNotNull("table name is null, not set after generateTable()", entity.getTableName());
		assertNotNull("generator name is null, not set after generateTable()", entity.getGeneratorName());
		assertFalse("entity is editable, although entity's table was generated", entity.isEditable());

		// check that column names are set for each attribute of the entity
		booleanAttribute = constructorService.getAttribute(booleanAttributeId);
		assertNotNull("boolean attribute column name is null", booleanAttribute.getColumnName());


		String sourceValue1 = "some mega source";
		Integer entityValueId = service.createEntityValue(entity, sourceValue1);
		EntityValue entityValue = service.getEntityValue(entity, entityValueId);
		assertEquals("source value was not set correctly on creation of entity table", sourceValue1, entityValue.getSource());

		String sourceValue2 = "some super hyper source!";
		service.setSource(entity, entityValue.getId(), sourceValue2);
		entityValue = service.getEntityValue(entity, entityValueId);
		assertEquals("source value was not set correctly on setSource", sourceValue2, entityValue.getSource());

		constructorService.deleteEntity(entityId); // attributes are deleted cascadely
		constructorService.deleteSection(sectionId);
	}

	@Test
	public void testDoubleAttributeValue() {
		AttributeValue value = new DoubleAttributeValue();
		value.setValue(12398.456D);
		assertEquals("12398.456", value.getSqlAttributeValue());

		value.setValue(-12398.456D);
		assertEquals("-12398.456", value.getSqlAttributeValue());
	}

	@Test
	public void testSetGetAttributeValue() throws SQLException, NotUniqueException, ConstraintException {
		Section section = new Section();
		section.setName("test section");
		Integer sectionId = constructorService.createSection(section);

		Entity entity = new Entity();
		entity.setName("test entity");
		Integer entityId = constructorService.createEntity(sectionId, entity);

		String booleanAttributeName = "boolean attr";
		Attribute booleanAttribute = new Attribute();
		booleanAttribute.setType(AttributeType.BOOLEAN);
		booleanAttribute.setName(booleanAttributeName);
		booleanAttribute.setMeasureUnit("boolean measure unit");
		Integer booleanAttributeId = constructorService.createAttribute(entityId, booleanAttribute);

		String doubleAttributeName = "double attr";
		Attribute doubleAttribute = new Attribute();
		doubleAttribute.setType(AttributeType.DOUBLE);
		doubleAttribute.setName(doubleAttributeName);
		doubleAttribute.setMeasureUnit("double measure unit");
		Integer doubleAttributeId = constructorService.createAttribute(entityId, doubleAttribute);

		String intAttributeName = "int attr";
		Attribute intAttribute = new Attribute();
		intAttribute.setType(AttributeType.INTEGER);
		intAttribute.setName(intAttributeName);
		intAttribute.setMeasureUnit("int measure unit");
		Integer intAttributeId = constructorService.createAttribute(entityId, intAttribute);

		String stringAttributeName = "string attr";
		Attribute stringAttribute = new Attribute();
		stringAttribute.setType(AttributeType.STRING);
		stringAttribute.setName(stringAttributeName);
		stringAttribute.setMeasureUnit("string measure unit");
		Integer stringAttributeId = constructorService.createAttribute(entityId, stringAttribute);

		String enumAttributeName = "enum attr";
		Attribute enumAttribute = new Attribute();
		enumAttribute.setType(AttributeType.ENUM);
		enumAttribute.setName(enumAttributeName);
		enumAttribute.setMeasureUnit("enum measure unit");
		Integer enumAttributeId = constructorService.createAttribute(entityId, enumAttribute);

		// values for enumAttribute
		EnumValue enumValue;

		enumValue = new EnumValue();
		enumValue.setName("enum value 1");
		Integer enumValueId1 = constructorService.createEnumValue(enumAttributeId, enumValue);

		enumValue = new EnumValue();
		enumValue.setName("enum value 2");
		Integer enumValueId2 = constructorService.createEnumValue(enumAttributeId, enumValue);


		entity = constructorService.getEntity(entityId); // for filling attributes and enum values
		entity.setAttributes(constructorService.getAttributes(entityId));

		service.generateTable(entity); // checked by testGenerateTable();

		// get attributes with column names filled
		booleanAttribute = constructorService.getAttribute(booleanAttributeId);
		doubleAttribute = constructorService.getAttribute(doubleAttributeId);
		intAttribute = constructorService.getAttribute(intAttributeId);
		stringAttribute = constructorService.getAttribute(stringAttributeId);
		enumAttribute = constructorService.getAttribute(enumAttributeId);


		String source = "source value";
		Integer entityValueId = service.createEntityValue(entity, source);

		// check that all get attributes return null values
		EntityValue entityValue = service.getEntityValue(entity, entityValueId);
		assertEquals(source, entityValue.getSource());
		
		BooleanAttributeValue gotBooleanValue = (BooleanAttributeValue) entityValue.getAttributeValue(booleanAttributeName);
		assertNotNull(gotBooleanValue);
		assertEquals(gotBooleanValue.getAttribute().getId(), booleanAttributeId);
		assertNull(gotBooleanValue.getValue());

		DoubleAttributeValue gotDoubleValue = (DoubleAttributeValue) entityValue.getAttributeValue(doubleAttributeName);
		assertNotNull(gotDoubleValue);
		assertEquals(gotDoubleValue.getAttribute().getId(), doubleAttributeId);
		assertNull(gotDoubleValue.getValue());

		IntegerAttributeValue gotIntValue = (IntegerAttributeValue) entityValue.getAttributeValue(intAttributeName);
		assertNotNull(gotIntValue);
		assertEquals(gotIntValue.getAttribute().getId(), intAttributeId);
		assertNull(gotIntValue.getValue());

		StringAttributeValue gotStringValue = (StringAttributeValue) entityValue.getAttributeValue(stringAttributeName);
		assertNotNull(gotStringValue);
		assertEquals(gotStringValue.getAttribute().getId(), stringAttributeId);
		assertNull(gotStringValue.getValue());

		EnumAttributeValue gotEnumValue = (EnumAttributeValue) entityValue.getAttributeValue(enumAttributeName);
		assertNotNull(gotEnumValue);
		assertEquals(gotEnumValue.getAttribute().getId(), enumAttributeId);
		assertNull(gotEnumValue.getValue());



		boolean booleanValue = true;
		AttributeValue booleanAttributeValue = new BooleanAttributeValue();
		booleanAttributeValue.setAttribute(booleanAttribute);
		booleanAttributeValue.setValue(booleanValue);
		service.setAttributeValue(entityValueId, booleanAttributeValue);

		double doubleValue = 123.456D;
		AttributeValue doubleAttributeValue = new DoubleAttributeValue();
		doubleAttributeValue.setAttribute(doubleAttribute);
		doubleAttributeValue.setValue(doubleValue);
		service.setAttributeValue(entityValueId, doubleAttributeValue);

		Long intValue = 9999999999L;
		AttributeValue intAttributeValue = new IntegerAttributeValue();
		intAttributeValue.setAttribute(intAttribute);
		intAttributeValue.setValue(intValue);
		service.setAttributeValue(entityValueId, intAttributeValue);

		String stringValue = "some string attribute value";
		AttributeValue stringAttributeValue = new StringAttributeValue();
		stringAttributeValue.setAttribute(stringAttribute);
		stringAttributeValue.setValue(stringValue);
		service.setAttributeValue(entityValueId, stringAttributeValue);

		Integer enumValue1 = enumValueId1;
		AttributeValue enumAttributeValue = new EnumAttributeValue();
		enumAttributeValue.setAttribute(enumAttribute);
		enumAttributeValue.setValue(enumValue1);
		service.setAttributeValue(entityValueId, enumAttributeValue);

		Integer enumValue2 = enumValueId2;
		AttributeValue enumAttributeValue2 = new EnumAttributeValue();
		enumAttributeValue2.setAttribute(enumAttribute);
		enumAttributeValue2.setValue(enumValue2);
		service.setAttributeValue(entityValueId, enumAttributeValue2);

		// check fail on incorrect enum value
		AttributeValue incorrectEnumValue = new EnumAttributeValue();
		incorrectEnumValue.setAttribute(enumAttribute);
		incorrectEnumValue.setValue(-1);
		try
		{
			service.setAttributeValue(entityValueId, incorrectEnumValue);
			fail("Setting incorrect enum value did not fail");
		}
		catch (SQLException e)
		{
			// ok
		}


		entityValue = service.getEntityValue(entity, entityValueId);
		assertEquals(source, entityValue.getSource());

		gotBooleanValue = (BooleanAttributeValue) entityValue.getAttributeValue(booleanAttributeName);
		assertNotNull(gotBooleanValue);
		assertEquals(gotBooleanValue.getAttribute().getId(), booleanAttributeId);
		assertEquals((Boolean) gotBooleanValue.getValue(), booleanValue);

		gotDoubleValue = (DoubleAttributeValue) entityValue.getAttributeValue(doubleAttributeName);
		assertNotNull(gotDoubleValue);
		assertEquals(gotDoubleValue.getAttribute().getId(), doubleAttributeId);
		assertEquals((Double) gotDoubleValue.getValue(), doubleValue, 0.0001);

		gotIntValue = (IntegerAttributeValue) entityValue.getAttributeValue(intAttributeName);
		assertNotNull(gotIntValue);
		assertEquals(gotIntValue.getAttribute().getId(), intAttributeId);
		assertEquals((Long) gotIntValue.getValue(), intValue);

		gotStringValue = (StringAttributeValue) entityValue.getAttributeValue(stringAttributeName);
		assertNotNull(gotStringValue);
		assertEquals(gotStringValue.getAttribute().getId(), stringAttributeId);
		assertEquals((String) gotStringValue.getValue(), stringValue);

		gotEnumValue = (EnumAttributeValue) entityValue.getAttributeValue(enumAttributeName);
		assertNotNull(gotEnumValue);
		assertEquals(gotEnumValue.getAttribute().getId(), enumAttributeId);
		assertEquals((Integer) gotEnumValue.getValue(), enumValue2);


		// test that getting entity value by inexisting id returns null
		entityValue = service.getEntityValue(entity, -1);
		assertNull(entityValue);

		
		constructorService.deleteEntity(entityId); // attributes are deleted cascadely
		constructorService.deleteSection(sectionId);
	}

	@Test
	public void testSetAttributeValues() throws SQLException, NotUniqueException, ConstraintException {
		Section section = new Section();
		section.setName("test section");
		Integer sectionId = constructorService.createSection(section);

		Entity entity = new Entity();
		entity.setName("test entity");
		Integer entityId = constructorService.createEntity(sectionId, entity);

    Attribute booleanAttribute = new Attribute();
		booleanAttribute.setType(AttributeType.BOOLEAN);
		booleanAttribute.setName("boolean attr");
		booleanAttribute.setMeasureUnit("boolean measure unit");
		Integer booleanAttributeId = constructorService.createAttribute(entityId, booleanAttribute);

		Attribute doubleAttribute = new Attribute();
		doubleAttribute.setType(AttributeType.DOUBLE);
		doubleAttribute.setName("double attr");
		doubleAttribute.setMeasureUnit("double measure unit");
		Integer doubleAttributeId = constructorService.createAttribute(entityId, doubleAttribute);

		Attribute intAttribute = new Attribute();
		intAttribute.setType(AttributeType.INTEGER);
		intAttribute.setName("int attr");
		intAttribute.setMeasureUnit("int measure unit");
		Integer intAttributeId = constructorService.createAttribute(entityId, intAttribute);

		Attribute stringAttribute = new Attribute();
		stringAttribute.setType(AttributeType.STRING);
		stringAttribute.setName("string attr");
		stringAttribute.setMeasureUnit("string measure unit");
		Integer stringAttributeId = constructorService.createAttribute(entityId, stringAttribute);

		Attribute enumAttribute = new Attribute();
		enumAttribute.setType(AttributeType.ENUM);
		enumAttribute.setName("enum attr");
		enumAttribute.setMeasureUnit("enum measure unit");
		Integer enumAttributeId = constructorService.createAttribute(entityId, enumAttribute);

		// values for enumAttribute
		EnumValue enumValue;

		enumValue = new EnumValue();
		enumValue.setName("enum value 1");
		Integer enumValueId1 = constructorService.createEnumValue(enumAttributeId, enumValue);

		enumValue = new EnumValue();
		enumValue.setName("enum value 2");
		Integer enumValueId2 = constructorService.createEnumValue(enumAttributeId, enumValue);


		entity = constructorService.getEntity(entityId); // for filling attributes and enum values
		entity.setAttributes(constructorService.getAttributes(entityId));

		service.generateTable(entity); // checked by testGenerateTable();

		// get attributes with column names filled
		booleanAttribute = constructorService.getAttribute(booleanAttributeId);
		doubleAttribute = constructorService.getAttribute(doubleAttributeId);
		intAttribute = constructorService.getAttribute(intAttributeId);
		stringAttribute = constructorService.getAttribute(stringAttributeId);
		enumAttribute = constructorService.getAttribute(enumAttributeId);


		String source = "source value";
		Integer entityValueId = service.createEntityValue(entity, source);

		List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();

		AttributeValue booleanAttributeValue = new BooleanAttributeValue();
		booleanAttributeValue.setAttribute(booleanAttribute);
		booleanAttributeValue.setValue(true);
		attributeValues.add(booleanAttributeValue);

		AttributeValue doubleAttributeValue = new DoubleAttributeValue();
		doubleAttributeValue.setAttribute(doubleAttribute);
		doubleAttributeValue.setValue(123.456D);
		attributeValues.add(doubleAttributeValue);

		AttributeValue intAttributeValue = new IntegerAttributeValue();
		intAttributeValue.setAttribute(intAttribute);
		intAttributeValue.setValue(987654L);
		attributeValues.add(intAttributeValue);
		
		AttributeValue stringAttributeValue = new StringAttributeValue();
		stringAttributeValue.setAttribute(stringAttribute);
		stringAttributeValue.setValue("some string attribute value");
		attributeValues.add(stringAttributeValue);

		AttributeValue enumAttributeValue = new EnumAttributeValue();
		enumAttributeValue.setAttribute(enumAttribute);
		enumAttributeValue.setValue(enumValueId1);
		attributeValues.add(enumAttributeValue);

		service.setAttributeValues(entityValueId, attributeValues);

		// test setting null falues
		attributeValues.clear();

		booleanAttributeValue = new BooleanAttributeValue();
		booleanAttributeValue.setAttribute(booleanAttribute);
		booleanAttributeValue.setValue(null);
		attributeValues.add(booleanAttributeValue);

		doubleAttributeValue = new DoubleAttributeValue();
		doubleAttributeValue.setAttribute(doubleAttribute);
		doubleAttributeValue.setValue(null);
		attributeValues.add(doubleAttributeValue);

		intAttributeValue = new IntegerAttributeValue();
		intAttributeValue.setAttribute(intAttribute);
		intAttributeValue.setValue(null);
		attributeValues.add(intAttributeValue);

		stringAttributeValue = new StringAttributeValue();
		stringAttributeValue.setAttribute(stringAttribute);
		stringAttributeValue.setValue(null);
		attributeValues.add(stringAttributeValue);

		enumAttributeValue = new EnumAttributeValue();
		enumAttributeValue.setAttribute(enumAttribute);
		enumAttributeValue.setValue(null);
		attributeValues.add(enumAttributeValue);

		assertEquals(5, attributeValues.size());
		service.setAttributeValues(entityValueId, attributeValues);


		// check fail on incorrect enum value
		AttributeValue incorrectEnumValue = new EnumAttributeValue();
		incorrectEnumValue.setAttribute(enumAttribute);
		incorrectEnumValue.setValue(-1);
		attributeValues.add(incorrectEnumValue);

		try
		{
			service.setAttributeValues(entityValueId, attributeValues);
			fail("Setting incorrect enum value (one of multiple set values) did not fail");
		}
		catch (SQLException e)
		{
			// ok
		}


		constructorService.deleteEntity(entityId); // attributes are deleted cascadely
		constructorService.deleteSection(sectionId);
	}

	@Test
	public void testGetEntityValues() throws SQLException, NotUniqueException, ConstraintException {
		Section section = new Section();
		section.setName("test section");
		Integer sectionId = constructorService.createSection(section);

		Entity entity = new Entity();
		entity.setName("test entity");
		Integer entityId = constructorService.createEntity(sectionId, entity);

		String booleanAttributeName = "boolean attr";
		Attribute booleanAttribute = new Attribute();
		booleanAttribute.setType(AttributeType.BOOLEAN);
		booleanAttribute.setName(booleanAttributeName);
		booleanAttribute.setMeasureUnit("boolean measure unit");
		Integer booleanAttributeId = constructorService.createAttribute(entityId, booleanAttribute);

		String doubleAttributeName = "double attr";
		Attribute doubleAttribute = new Attribute();
		doubleAttribute.setType(AttributeType.DOUBLE);
		doubleAttribute.setName(doubleAttributeName);
		doubleAttribute.setMeasureUnit("double measure unit");
		Integer doubleAttributeId = constructorService.createAttribute(entityId, doubleAttribute);

		String intAttributeName = "int attr";
		Attribute intAttribute = new Attribute();
		intAttribute.setType(AttributeType.INTEGER);
		intAttribute.setName(intAttributeName);
		intAttribute.setMeasureUnit("int measure unit");
		Integer intAttributeId = constructorService.createAttribute(entityId, intAttribute);

		String stringAttributeName = "string attr";
		Attribute stringAttribute = new Attribute();
		stringAttribute.setType(AttributeType.STRING);
		stringAttribute.setName(stringAttributeName);
		stringAttribute.setMeasureUnit("string measure unit");
		Integer stringAttributeId = constructorService.createAttribute(entityId, stringAttribute);

		String enumAttributeName = "enum attr";
		Attribute enumAttribute = new Attribute();
		enumAttribute.setType(AttributeType.ENUM);
		enumAttribute.setName(enumAttributeName);
		enumAttribute.setMeasureUnit("enum measure unit");
		Integer enumAttributeId = constructorService.createAttribute(entityId, enumAttribute);

		// values for enumAttribute
		EnumValue enumValue;

		enumValue = new EnumValue();
		enumValue.setName("enum value 1");
		Integer enumValueId1 = constructorService.createEnumValue(enumAttributeId, enumValue);

		enumValue = new EnumValue();
		enumValue.setName("enum value 2");
		Integer enumValueId2 = constructorService.createEnumValue(enumAttributeId, enumValue);


		entity = constructorService.getEntity(entityId); // for filling attributes and enum values
		entity.setAttributes(constructorService.getAttributes(entityId));



		service.generateTable(entity); // checked by testGenerateTable();

		// get attributes with column names filled
		booleanAttribute = constructorService.getAttribute(booleanAttributeId);
		doubleAttribute = constructorService.getAttribute(doubleAttributeId);
		intAttribute = constructorService.getAttribute(intAttributeId);
		stringAttribute = constructorService.getAttribute(stringAttributeId);
		enumAttribute = constructorService.getAttribute(enumAttributeId);


		// check that list of entity values is empty after creation
		List<EntityValue> entityValues = service.getEntityValues(entity);
		assertEquals(0, entityValues.size());


		// create 1st entity value
		String source1 = "source value 1";
		Integer entityValueId1 = service.createEntityValue(entity, source1);

		boolean booleanValue1 = true;
		AttributeValue booleanAttributeValue = new BooleanAttributeValue();
		booleanAttributeValue.setAttribute(booleanAttribute);
		booleanAttributeValue.setValue(booleanValue1);
		service.setAttributeValue(entityValueId1, booleanAttributeValue);

		double doubleValue1 = 123.456D;
		AttributeValue doubleAttributeValue = new DoubleAttributeValue();
		doubleAttributeValue.setAttribute(doubleAttribute);
		doubleAttributeValue.setValue(doubleValue1);
		service.setAttributeValue(entityValueId1, doubleAttributeValue);

		Long intValue1 = 9999999999L;
		AttributeValue intAttributeValue = new IntegerAttributeValue();
		intAttributeValue.setAttribute(intAttribute);
		intAttributeValue.setValue(intValue1);
		service.setAttributeValue(entityValueId1, intAttributeValue);

		String stringValue1 = "some string attribute value";
		AttributeValue stringAttributeValue = new StringAttributeValue();
		stringAttributeValue.setAttribute(stringAttribute);
		stringAttributeValue.setValue(stringValue1);
		service.setAttributeValue(entityValueId1, stringAttributeValue);

		Integer enumValue1 = enumValueId1;
		AttributeValue enumAttributeValue = new EnumAttributeValue();
		enumAttributeValue.setAttribute(enumAttribute);
		enumAttributeValue.setValue(enumValue1);
		service.setAttributeValue(entityValueId1, enumAttributeValue);

		// create 2nd entity value
		String source2 = "source value 2";
		Integer entityValueId2 = service.createEntityValue(entity, source2);

		boolean booleanValue2 = false;
		booleanAttributeValue = new BooleanAttributeValue();
		booleanAttributeValue.setAttribute(booleanAttribute);
		booleanAttributeValue.setValue(booleanValue2);
		service.setAttributeValue(entityValueId2, booleanAttributeValue);

		double doubleValue2 = -456.321D;
		doubleAttributeValue = new DoubleAttributeValue();
		doubleAttributeValue.setAttribute(doubleAttribute);
		doubleAttributeValue.setValue(doubleValue2);
		service.setAttributeValue(entityValueId2, doubleAttributeValue);

		Long intValue2 = -9999999999L;
		intAttributeValue = new IntegerAttributeValue();
		intAttributeValue.setAttribute(intAttribute);
		intAttributeValue.setValue(intValue2);
		service.setAttributeValue(entityValueId2, intAttributeValue);

		String stringValue2 = "some other string attribute value 2";
		stringAttributeValue = new StringAttributeValue();
		stringAttributeValue.setAttribute(stringAttribute);
		stringAttributeValue.setValue(stringValue2);
		service.setAttributeValue(entityValueId2, stringAttributeValue);

		Integer enumValue2 = enumValueId2;
		enumAttributeValue = new EnumAttributeValue();
		enumAttributeValue.setAttribute(enumAttribute);
		enumAttributeValue.setValue(enumValue2);
		service.setAttributeValue(entityValueId2, enumAttributeValue);

		// create 3nd entity value with all attribute values set to null
		String source3 = "source value 3";
		Integer entityValueId3 = service.createEntityValue(entity, source3);

		Boolean booleanValue3 = null;
		booleanAttributeValue = new BooleanAttributeValue();
		booleanAttributeValue.setAttribute(booleanAttribute);
		booleanAttributeValue.setValue(booleanValue3);
		service.setAttributeValue(entityValueId3, booleanAttributeValue);

		Double doubleValue3 = null;
		doubleAttributeValue = new DoubleAttributeValue();
		doubleAttributeValue.setAttribute(doubleAttribute);
		doubleAttributeValue.setValue(doubleValue3);
		service.setAttributeValue(entityValueId3, doubleAttributeValue);

		Long intValue3 = null;
		intAttributeValue = new IntegerAttributeValue();
		intAttributeValue.setAttribute(intAttribute);
		intAttributeValue.setValue(intValue3);
		service.setAttributeValue(entityValueId3, intAttributeValue);

		String stringValue3 = null;
		stringAttributeValue = new StringAttributeValue();
		stringAttributeValue.setAttribute(stringAttribute);
		stringAttributeValue.setValue(stringValue3);
		service.setAttributeValue(entityValueId3, stringAttributeValue);

		Integer enumValue3 = null;
		enumAttributeValue = new EnumAttributeValue();
		enumAttributeValue.setAttribute(enumAttribute);
		enumAttributeValue.setValue(enumValue3);
		service.setAttributeValue(entityValueId3, enumAttributeValue);


		entityValues = service.getEntityValues(entity);
		assertEquals(3, entityValues.size());

		// check 1st entity value
		EntityValue entityValue1 = entityValues.get(0);
		assertEquals(entityValueId1, entityValue1.getId());
		assertEquals(source1, entityValue1.getSource());

		BooleanAttributeValue gotBooleanValue = (BooleanAttributeValue) entityValue1.getAttributeValue(booleanAttributeName);
		assertNotNull(gotBooleanValue);
		assertEquals(gotBooleanValue.getAttribute().getId(), booleanAttributeId);
		assertEquals((Boolean) gotBooleanValue.getValue(), booleanValue1);

		DoubleAttributeValue gotDoubleValue = (DoubleAttributeValue) entityValue1.getAttributeValue(doubleAttributeName);
		assertNotNull(gotDoubleValue);
		assertEquals(gotDoubleValue.getAttribute().getId(), doubleAttributeId);
		assertEquals((Double) gotDoubleValue.getValue(), doubleValue1, 0.0001);

		IntegerAttributeValue gotIntValue = (IntegerAttributeValue) entityValue1.getAttributeValue(intAttributeName);
		assertNotNull(gotIntValue);
		assertEquals(gotIntValue.getAttribute().getId(), intAttributeId);
		assertEquals((Long) gotIntValue.getValue(), intValue1);

		StringAttributeValue gotStringValue = (StringAttributeValue) entityValue1.getAttributeValue(stringAttributeName);
		assertNotNull(gotStringValue);
		assertEquals(gotStringValue.getAttribute().getId(), stringAttributeId);
		assertEquals((String) gotStringValue.getValue(), stringValue1);

		EnumAttributeValue gotEnumValue = (EnumAttributeValue) entityValue1.getAttributeValue(enumAttributeName);
		assertNotNull(gotEnumValue);
		assertEquals(gotEnumValue.getAttribute().getId(), enumAttributeId);
		assertEquals((Integer) gotEnumValue.getValue(), enumValue1);

		// check 2nd entity value
		EntityValue entityValue2 = entityValues.get(1);
		assertEquals(entityValueId2, entityValue2.getId());
		assertEquals(source2, entityValue2.getSource());

		gotBooleanValue = (BooleanAttributeValue) entityValue2.getAttributeValue(booleanAttributeName);
		assertNotNull(gotBooleanValue);
		assertEquals(gotBooleanValue.getAttribute().getId(), booleanAttributeId);
		assertEquals((Boolean) gotBooleanValue.getValue(), booleanValue2);

		gotDoubleValue = (DoubleAttributeValue) entityValue2.getAttributeValue(doubleAttributeName);
		assertNotNull(gotDoubleValue);
		assertEquals(gotDoubleValue.getAttribute().getId(), doubleAttributeId);
		assertEquals((Double) gotDoubleValue.getValue(), doubleValue2, 0.0001);

		gotIntValue = (IntegerAttributeValue) entityValue2.getAttributeValue(intAttributeName);
		assertNotNull(gotIntValue);
		assertEquals(gotIntValue.getAttribute().getId(), intAttributeId);
		assertEquals((Long) gotIntValue.getValue(), intValue2);

		gotStringValue = (StringAttributeValue) entityValue2.getAttributeValue(stringAttributeName);
		assertNotNull(gotStringValue);
		assertEquals(gotStringValue.getAttribute().getId(), stringAttributeId);
		assertEquals((String) gotStringValue.getValue(), stringValue2);

		gotEnumValue = (EnumAttributeValue) entityValue2.getAttributeValue(enumAttributeName);
		assertNotNull(gotEnumValue);
		assertEquals(gotEnumValue.getAttribute().getId(), enumAttributeId);
		assertEquals((Integer) gotEnumValue.getValue(), enumValue2);

		// check 3rd entity value (all attribute values must be null
		EntityValue entityValue3 = entityValues.get(2);
		assertEquals(entityValueId3, entityValue3.getId());
		assertEquals(source3, entityValue3.getSource());

		gotBooleanValue = (BooleanAttributeValue) entityValue3.getAttributeValue(booleanAttributeName);
		assertNotNull(gotBooleanValue);
		assertEquals(gotBooleanValue.getAttribute().getId(), booleanAttributeId);
		assertNull(gotBooleanValue.getValue());

		gotDoubleValue = (DoubleAttributeValue) entityValue3.getAttributeValue(doubleAttributeName);
		assertNotNull(gotDoubleValue);
		assertEquals(gotDoubleValue.getAttribute().getId(), doubleAttributeId);
		assertNull(gotDoubleValue.getValue());

		gotIntValue = (IntegerAttributeValue) entityValue3.getAttributeValue(intAttributeName);
		assertNotNull(gotIntValue);
		assertEquals(gotIntValue.getAttribute().getId(), intAttributeId);
		assertNull(gotIntValue.getValue());

		gotStringValue = (StringAttributeValue) entityValue3.getAttributeValue(stringAttributeName);
		assertNotNull(gotStringValue);
		assertEquals(gotStringValue.getAttribute().getId(), stringAttributeId);
		assertNull(gotStringValue.getValue());

		gotEnumValue = (EnumAttributeValue) entityValue3.getAttributeValue(enumAttributeName);
		assertNotNull(gotEnumValue);
		assertEquals(gotEnumValue.getAttribute().getId(), enumAttributeId);
		assertNull(gotEnumValue.getValue());


		constructorService.deleteEntity(entityId); // attributes are deleted cascadely
		constructorService.deleteSection(sectionId);
	}

	@Test
	public void testSetGetDeleteEntityValues() throws SQLException, NotUniqueException, ConstraintException {
		Section section = new Section();
		section.setName("test section");
		Integer sectionId = constructorService.createSection(section);

		Entity entity = new Entity();
		entity.setName("test entity");
		Integer entityId = constructorService.createEntity(sectionId, entity);

		String booleanAttributeName = "boolean attr";
		Attribute booleanAttribute = new Attribute();
		booleanAttribute.setType(AttributeType.BOOLEAN);
		booleanAttribute.setName(booleanAttributeName);
		booleanAttribute.setMeasureUnit("boolean measure unit");
		Integer booleanAttributeId = constructorService.createAttribute(entityId, booleanAttribute);

		String doubleAttributeName = "double attr";
		Attribute doubleAttribute = new Attribute();
		doubleAttribute.setType(AttributeType.DOUBLE);
		doubleAttribute.setName(doubleAttributeName);
		doubleAttribute.setMeasureUnit("double measure unit");
		Integer doubleAttributeId = constructorService.createAttribute(entityId, doubleAttribute);

		String intAttributeName = "int attr";
		Attribute intAttribute = new Attribute();
		intAttribute.setType(AttributeType.INTEGER);
		intAttribute.setName(intAttributeName);
		intAttribute.setMeasureUnit("int measure unit");
		Integer intAttributeId = constructorService.createAttribute(entityId, intAttribute);

		String stringAttributeName = "string attr";
		Attribute stringAttribute = new Attribute();
		stringAttribute.setType(AttributeType.STRING);
		stringAttribute.setName(stringAttributeName);
		stringAttribute.setMeasureUnit("string measure unit");
		Integer stringAttributeId = constructorService.createAttribute(entityId, stringAttribute);

		String enumAttributeName = "enum attr";
		Attribute enumAttribute = new Attribute();
		enumAttribute.setType(AttributeType.ENUM);
		enumAttribute.setName(enumAttributeName);
		enumAttribute.setMeasureUnit("enum measure unit");
		Integer enumAttributeId = constructorService.createAttribute(entityId, enumAttribute);

		// values for enumAttribute
		EnumValue enumValue;

		enumValue = new EnumValue();
		enumValue.setName("enum value 1");
		Integer enumValueId1 = constructorService.createEnumValue(enumAttributeId, enumValue);

		enumValue = new EnumValue();
		enumValue.setName("enum value 2");
		Integer enumValueId2 = constructorService.createEnumValue(enumAttributeId, enumValue);


		entity = constructorService.getEntity(entityId); // for filling attributes and enum values
		entity.setAttributes(constructorService.getAttributes(entityId));



		service.generateTable(entity); // checked by testGenerateTable();

		// get attributes with column names filled
		booleanAttribute = constructorService.getAttribute(booleanAttributeId);
		doubleAttribute = constructorService.getAttribute(doubleAttributeId);
		intAttribute = constructorService.getAttribute(intAttributeId);
		stringAttribute = constructorService.getAttribute(stringAttributeId);
		enumAttribute = constructorService.getAttribute(enumAttributeId);


		// check that list of entity values is empty after creation
		List<EntityValue> entityValues = service.getEntityValues(entity);
		assertEquals(0, entityValues.size());


		List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();

		// create 1st entity value
		attributeValues.clear();
		String source1 = "source value 1";
		Integer entityValueId1 = service.createEntityValue(entity, source1);

		boolean booleanValue1 = true;
		AttributeValue booleanAttributeValue = new BooleanAttributeValue();
		booleanAttributeValue.setAttribute(booleanAttribute);
		booleanAttributeValue.setValue(booleanValue1);
		attributeValues.add(booleanAttributeValue);

		double doubleValue1 = 123.456D;
		AttributeValue doubleAttributeValue = new DoubleAttributeValue();
		doubleAttributeValue.setAttribute(doubleAttribute);
		doubleAttributeValue.setValue(doubleValue1);
		attributeValues.add(doubleAttributeValue);

		Long intValue1 = 9999999999L;
		AttributeValue intAttributeValue = new IntegerAttributeValue();
		intAttributeValue.setAttribute(intAttribute);
		intAttributeValue.setValue(intValue1);
		attributeValues.add(intAttributeValue);

		String stringValue1 = "some string attribute value";
		AttributeValue stringAttributeValue = new StringAttributeValue();
		stringAttributeValue.setAttribute(stringAttribute);
		stringAttributeValue.setValue(stringValue1);
		attributeValues.add(stringAttributeValue);

		Integer enumValue1 = enumValueId1;
		AttributeValue enumAttributeValue = new EnumAttributeValue();
		enumAttributeValue.setAttribute(enumAttribute);
		enumAttributeValue.setValue(enumValue1);
		attributeValues.add(enumAttributeValue);

		assertEquals(5, attributeValues.size());
		service.setAttributeValues(entityValueId1, attributeValues);
		
		// create 2nd entity value
		attributeValues.clear();
		String source2 = "source value 2";
		Integer entityValueId2 = service.createEntityValue(entity, source2);

		boolean booleanValue2 = false;
		booleanAttributeValue = new BooleanAttributeValue();
		booleanAttributeValue.setAttribute(booleanAttribute);
		booleanAttributeValue.setValue(booleanValue2);
		attributeValues.add(booleanAttributeValue);

		double doubleValue2 = -456.321D;
		doubleAttributeValue = new DoubleAttributeValue();
		doubleAttributeValue.setAttribute(doubleAttribute);
		doubleAttributeValue.setValue(doubleValue2);
		attributeValues.add(doubleAttributeValue);

		Long intValue2 = -9999999999L;
		intAttributeValue = new IntegerAttributeValue();
		intAttributeValue.setAttribute(intAttribute);
		intAttributeValue.setValue(intValue2);
		attributeValues.add(intAttributeValue);
		
		String stringValue2 = "some other string attribute value 2";
		stringAttributeValue = new StringAttributeValue();
		stringAttributeValue.setAttribute(stringAttribute);
		stringAttributeValue.setValue(stringValue2);
		attributeValues.add(stringAttributeValue);

		Integer enumValue2 = enumValueId2;
		enumAttributeValue = new EnumAttributeValue();
		enumAttributeValue.setAttribute(enumAttribute);
		enumAttributeValue.setValue(enumValue2);
		attributeValues.add(enumAttributeValue);
		
		assertEquals(5, attributeValues.size());
		service.setAttributeValues(entityValueId2, attributeValues);


		// create 3nd entity value with all attribute values set to null
		attributeValues.clear();
		String source3 = "source value 3";
		Integer entityValueId3 = service.createEntityValue(entity, source3);

		Boolean booleanValue3 = null;
		booleanAttributeValue = new BooleanAttributeValue();
		booleanAttributeValue.setAttribute(booleanAttribute);
		booleanAttributeValue.setValue(booleanValue3);
		attributeValues.add(booleanAttributeValue);

		Double doubleValue3 = null;
		doubleAttributeValue = new DoubleAttributeValue();
		doubleAttributeValue.setAttribute(doubleAttribute);
		doubleAttributeValue.setValue(doubleValue3);
		attributeValues.add(doubleAttributeValue);

		Long intValue3 = null;
		intAttributeValue = new IntegerAttributeValue();
		intAttributeValue.setAttribute(intAttribute);
		intAttributeValue.setValue(intValue3);
		attributeValues.add(intAttributeValue);

		String stringValue3 = null;
		stringAttributeValue = new StringAttributeValue();
		stringAttributeValue.setAttribute(stringAttribute);
		stringAttributeValue.setValue(stringValue3);
		attributeValues.add(stringAttributeValue);

		Integer enumValue3 = null;
		enumAttributeValue = new EnumAttributeValue();
		enumAttributeValue.setAttribute(enumAttribute);
		enumAttributeValue.setValue(enumValue3);
		attributeValues.add(enumAttributeValue);

		assertEquals(5, attributeValues.size());
		service.setAttributeValues(entityValueId3, attributeValues);


		// check got values
		entityValues = service.getEntityValues(entity);
		assertEquals(3, entityValues.size());

		// check 1st entity value
		EntityValue entityValue1 = entityValues.get(0);
		assertEquals(entityValueId1, entityValue1.getId());
		assertEquals(source1, entityValue1.getSource());

		BooleanAttributeValue gotBooleanValue = (BooleanAttributeValue) entityValue1.getAttributeValue(booleanAttributeName);
		assertNotNull(gotBooleanValue);
		assertEquals(gotBooleanValue.getAttribute().getId(), booleanAttributeId);
		assertEquals((Boolean) gotBooleanValue.getValue(), booleanValue1);

		DoubleAttributeValue gotDoubleValue = (DoubleAttributeValue) entityValue1.getAttributeValue(doubleAttributeName);
		assertNotNull(gotDoubleValue);
		assertEquals(gotDoubleValue.getAttribute().getId(), doubleAttributeId);
		assertEquals((Double) gotDoubleValue.getValue(), doubleValue1, 0.0001);

		IntegerAttributeValue gotIntValue = (IntegerAttributeValue) entityValue1.getAttributeValue(intAttributeName);
		assertNotNull(gotIntValue);
		assertEquals(gotIntValue.getAttribute().getId(), intAttributeId);
		assertEquals((Long) gotIntValue.getValue(), intValue1);

		StringAttributeValue gotStringValue = (StringAttributeValue) entityValue1.getAttributeValue(stringAttributeName);
		assertNotNull(gotStringValue);
		assertEquals(gotStringValue.getAttribute().getId(), stringAttributeId);
		assertEquals((String) gotStringValue.getValue(), stringValue1);

		EnumAttributeValue gotEnumValue = (EnumAttributeValue) entityValue1.getAttributeValue(enumAttributeName);
		assertNotNull(gotEnumValue);
		assertEquals(gotEnumValue.getAttribute().getId(), enumAttributeId);
		assertEquals((Integer) gotEnumValue.getValue(), enumValue1);

		// check 2nd entity value
		EntityValue entityValue2 = entityValues.get(1);
		assertEquals(entityValueId2, entityValue2.getId());
		assertEquals(source2, entityValue2.getSource());

		gotBooleanValue = (BooleanAttributeValue) entityValue2.getAttributeValue(booleanAttributeName);
		assertNotNull(gotBooleanValue);
		assertEquals(gotBooleanValue.getAttribute().getId(), booleanAttributeId);
		assertEquals((Boolean) gotBooleanValue.getValue(), booleanValue2);

		gotDoubleValue = (DoubleAttributeValue) entityValue2.getAttributeValue(doubleAttributeName);
		assertNotNull(gotDoubleValue);
		assertEquals(gotDoubleValue.getAttribute().getId(), doubleAttributeId);
		assertEquals((Double) gotDoubleValue.getValue(), doubleValue2, 0.0001);

		gotIntValue = (IntegerAttributeValue) entityValue2.getAttributeValue(intAttributeName);
		assertNotNull(gotIntValue);
		assertEquals(gotIntValue.getAttribute().getId(), intAttributeId);
		assertEquals((Long) gotIntValue.getValue(), intValue2);

		gotStringValue = (StringAttributeValue) entityValue2.getAttributeValue(stringAttributeName);
		assertNotNull(gotStringValue);
		assertEquals(gotStringValue.getAttribute().getId(), stringAttributeId);
		assertEquals((String) gotStringValue.getValue(), stringValue2);

		gotEnumValue = (EnumAttributeValue) entityValue2.getAttributeValue(enumAttributeName);
		assertNotNull(gotEnumValue);
		assertEquals(gotEnumValue.getAttribute().getId(), enumAttributeId);
		assertEquals((Integer) gotEnumValue.getValue(), enumValue2);

		// check 3rd entity value (all attribute values must be null)
		EntityValue entityValue3 = entityValues.get(2);
		assertEquals(entityValueId3, entityValue3.getId());
		assertEquals(source3, entityValue3.getSource());

		gotBooleanValue = (BooleanAttributeValue) entityValue3.getAttributeValue(booleanAttributeName);
		assertNotNull(gotBooleanValue);
		assertEquals(gotBooleanValue.getAttribute().getId(), booleanAttributeId);
		assertNull(gotBooleanValue.getValue());

		gotDoubleValue = (DoubleAttributeValue) entityValue3.getAttributeValue(doubleAttributeName);
		assertNotNull(gotDoubleValue);
		assertEquals(gotDoubleValue.getAttribute().getId(), doubleAttributeId);
		assertNull(gotDoubleValue.getValue());

		gotIntValue = (IntegerAttributeValue) entityValue3.getAttributeValue(intAttributeName);
		assertNotNull(gotIntValue);
		assertEquals(gotIntValue.getAttribute().getId(), intAttributeId);
		assertNull(gotIntValue.getValue());

		gotStringValue = (StringAttributeValue) entityValue3.getAttributeValue(stringAttributeName);
		assertNotNull(gotStringValue);
		assertEquals(gotStringValue.getAttribute().getId(), stringAttributeId);
		assertNull(gotStringValue.getValue());

		gotEnumValue = (EnumAttributeValue) entityValue3.getAttributeValue(enumAttributeName);
		assertNotNull(gotEnumValue);
		assertEquals(gotEnumValue.getAttribute().getId(), enumAttributeId);
		assertNull(gotEnumValue.getValue());


		// check delete entity values
		service.deleteEntityValue(entity, entityValueId1);

		entityValues = service.getEntityValues(entity);
		assertEquals(2, entityValues.size());

		// check 2nd entity value - 1st value must have been deleted
		entityValue2 = entityValues.get(0);
		assertEquals(entityValueId2, entityValue2.getId());
		assertEquals(source2, entityValue2.getSource());

		gotBooleanValue = (BooleanAttributeValue) entityValue2.getAttributeValue(booleanAttributeName);
		assertNotNull(gotBooleanValue);
		assertEquals(gotBooleanValue.getAttribute().getId(), booleanAttributeId);
		assertEquals((Boolean) gotBooleanValue.getValue(), booleanValue2);

		gotDoubleValue = (DoubleAttributeValue) entityValue2.getAttributeValue(doubleAttributeName);
		assertNotNull(gotDoubleValue);
		assertEquals(gotDoubleValue.getAttribute().getId(), doubleAttributeId);
		assertEquals((Double) gotDoubleValue.getValue(), doubleValue2, 0.0001);

		gotIntValue = (IntegerAttributeValue) entityValue2.getAttributeValue(intAttributeName);
		assertNotNull(gotIntValue);
		assertEquals(gotIntValue.getAttribute().getId(), intAttributeId);
		assertEquals((Long) gotIntValue.getValue(), intValue2);

		gotStringValue = (StringAttributeValue) entityValue2.getAttributeValue(stringAttributeName);
		assertNotNull(gotStringValue);
		assertEquals(gotStringValue.getAttribute().getId(), stringAttributeId);
		assertEquals((String) gotStringValue.getValue(), stringValue2);

		gotEnumValue = (EnumAttributeValue) entityValue2.getAttributeValue(enumAttributeName);
		assertNotNull(gotEnumValue);
		assertEquals(gotEnumValue.getAttribute().getId(), enumAttributeId);
		assertEquals((Integer) gotEnumValue.getValue(), enumValue2);

		// check 3rd entity value (all attribute values must be null) - 1st value must have been deleted
		entityValue3 = entityValues.get(1);
		assertEquals(entityValueId3, entityValue3.getId());
		assertEquals(source3, entityValue3.getSource());

		gotBooleanValue = (BooleanAttributeValue) entityValue3.getAttributeValue(booleanAttributeName);
		assertNotNull(gotBooleanValue);
		assertEquals(gotBooleanValue.getAttribute().getId(), booleanAttributeId);
		assertNull(gotBooleanValue.getValue());

		gotDoubleValue = (DoubleAttributeValue) entityValue3.getAttributeValue(doubleAttributeName);
		assertNotNull(gotDoubleValue);
		assertEquals(gotDoubleValue.getAttribute().getId(), doubleAttributeId);
		assertNull(gotDoubleValue.getValue());

		gotIntValue = (IntegerAttributeValue) entityValue3.getAttributeValue(intAttributeName);
		assertNotNull(gotIntValue);
		assertEquals(gotIntValue.getAttribute().getId(), intAttributeId);
		assertNull(gotIntValue.getValue());

		gotStringValue = (StringAttributeValue) entityValue3.getAttributeValue(stringAttributeName);
		assertNotNull(gotStringValue);
		assertEquals(gotStringValue.getAttribute().getId(), stringAttributeId);
		assertNull(gotStringValue.getValue());

		gotEnumValue = (EnumAttributeValue) entityValue3.getAttributeValue(enumAttributeName);
		assertNotNull(gotEnumValue);
		assertEquals(gotEnumValue.getAttribute().getId(), enumAttributeId);
		assertNull(gotEnumValue.getValue());


		// try to delete the same value twice - check that nothing changes
		service.deleteEntityValue(entity, entityValueId1);

		entityValues = service.getEntityValues(entity);
		assertEquals(2, entityValues.size());

		// check 2nd entity value - 1st value must have been deleted
		entityValue2 = entityValues.get(0);
		assertEquals(entityValueId2, entityValue2.getId());
		assertEquals(source2, entityValue2.getSource());

		gotBooleanValue = (BooleanAttributeValue) entityValue2.getAttributeValue(booleanAttributeName);
		assertNotNull(gotBooleanValue);
		assertEquals(gotBooleanValue.getAttribute().getId(), booleanAttributeId);
		assertEquals((Boolean) gotBooleanValue.getValue(), booleanValue2);

		gotDoubleValue = (DoubleAttributeValue) entityValue2.getAttributeValue(doubleAttributeName);
		assertNotNull(gotDoubleValue);
		assertEquals(gotDoubleValue.getAttribute().getId(), doubleAttributeId);
		assertEquals((Double) gotDoubleValue.getValue(), doubleValue2, 0.0001);

		gotIntValue = (IntegerAttributeValue) entityValue2.getAttributeValue(intAttributeName);
		assertNotNull(gotIntValue);
		assertEquals(gotIntValue.getAttribute().getId(), intAttributeId);
		assertEquals((Long) gotIntValue.getValue(), intValue2);

		gotStringValue = (StringAttributeValue) entityValue2.getAttributeValue(stringAttributeName);
		assertNotNull(gotStringValue);
		assertEquals(gotStringValue.getAttribute().getId(), stringAttributeId);
		assertEquals((String) gotStringValue.getValue(), stringValue2);

		gotEnumValue = (EnumAttributeValue) entityValue2.getAttributeValue(enumAttributeName);
		assertNotNull(gotEnumValue);
		assertEquals(gotEnumValue.getAttribute().getId(), enumAttributeId);
		assertEquals((Integer) gotEnumValue.getValue(), enumValue2);

		// check 3rd entity value (all attribute values must be null) - 1st value must have been deleted
		entityValue3 = entityValues.get(1);
		assertEquals(entityValueId3, entityValue3.getId());
		assertEquals(source3, entityValue3.getSource());

		gotBooleanValue = (BooleanAttributeValue) entityValue3.getAttributeValue(booleanAttributeName);
		assertNotNull(gotBooleanValue);
		assertEquals(gotBooleanValue.getAttribute().getId(), booleanAttributeId);
		assertNull(gotBooleanValue.getValue());

		gotDoubleValue = (DoubleAttributeValue) entityValue3.getAttributeValue(doubleAttributeName);
		assertNotNull(gotDoubleValue);
		assertEquals(gotDoubleValue.getAttribute().getId(), doubleAttributeId);
		assertNull(gotDoubleValue.getValue());

		gotIntValue = (IntegerAttributeValue) entityValue3.getAttributeValue(intAttributeName);
		assertNotNull(gotIntValue);
		assertEquals(gotIntValue.getAttribute().getId(), intAttributeId);
		assertNull(gotIntValue.getValue());

		gotStringValue = (StringAttributeValue) entityValue3.getAttributeValue(stringAttributeName);
		assertNotNull(gotStringValue);
		assertEquals(gotStringValue.getAttribute().getId(), stringAttributeId);
		assertNull(gotStringValue.getValue());

		gotEnumValue = (EnumAttributeValue) entityValue3.getAttributeValue(enumAttributeName);
		assertNotNull(gotEnumValue);
		assertEquals(gotEnumValue.getAttribute().getId(), enumAttributeId);
		assertNull(gotEnumValue.getValue());


		service.deleteEntityValue(entity, entityValueId2);

		entityValues = service.getEntityValues(entity);
		assertEquals(1, entityValues.size());

		// check 3rd entity value (all attribute values must be null) - 1st and 2nd values must have been deleted
		entityValue3 = entityValues.get(0);
		assertEquals(entityValueId3, entityValue3.getId());
		assertEquals(source3, entityValue3.getSource());

		gotBooleanValue = (BooleanAttributeValue) entityValue3.getAttributeValue(booleanAttributeName);
		assertNotNull(gotBooleanValue);
		assertEquals(gotBooleanValue.getAttribute().getId(), booleanAttributeId);
		assertNull(gotBooleanValue.getValue());

		gotDoubleValue = (DoubleAttributeValue) entityValue3.getAttributeValue(doubleAttributeName);
		assertNotNull(gotDoubleValue);
		assertEquals(gotDoubleValue.getAttribute().getId(), doubleAttributeId);
		assertNull(gotDoubleValue.getValue());

		gotIntValue = (IntegerAttributeValue) entityValue3.getAttributeValue(intAttributeName);
		assertNotNull(gotIntValue);
		assertEquals(gotIntValue.getAttribute().getId(), intAttributeId);
		assertNull(gotIntValue.getValue());

		gotStringValue = (StringAttributeValue) entityValue3.getAttributeValue(stringAttributeName);
		assertNotNull(gotStringValue);
		assertEquals(gotStringValue.getAttribute().getId(), stringAttributeId);
		assertNull(gotStringValue.getValue());

		gotEnumValue = (EnumAttributeValue) entityValue3.getAttributeValue(enumAttributeName);
		assertNotNull(gotEnumValue);
		assertEquals(gotEnumValue.getAttribute().getId(), enumAttributeId);
		assertNull(gotEnumValue.getValue());


		service.deleteEntityValue(entity, entityValueId3);

		entityValues = service.getEntityValues(entity);
		assertEquals(0, entityValues.size());


		// try to delete on empty table - check that nothing changes
		service.deleteEntityValue(entity, entityValueId3);

		entityValues = service.getEntityValues(entity);
		assertEquals(0, entityValues.size());


		constructorService.deleteEntity(entityId); // attributes are deleted cascadely
		constructorService.deleteSection(sectionId);
	}


	private ConstructorService constructorService;
	private GeneratorService service;
	private StringBuffer sb;
}