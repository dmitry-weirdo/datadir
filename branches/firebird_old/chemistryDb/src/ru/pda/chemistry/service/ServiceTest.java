package ru.pda.chemistry.service;

import ru.pda.chemistry.entities.Section;
import ru.pda.chemistry.entities.Entity;
import static ru.pda.chemistry.util.StringUtils.getConcatenation;

import java.sql.SQLException;
import java.util.List;

/**
 * User: 1
 * Date: 27.04.2010
 * Time: 0:46:24
 */
public class ServiceTest
{
	public static void main(String[] args) throws SQLException {
		testConstuctorService();
	}

	private static void testConstuctorService() throws SQLException {
		// todo: write good test for related create, get and remove of one section

		ConstructorService service = ServiceFactory.getConstructorService();

		Section section;
		Integer id;


		// test getSections()
		System.out.println("\nTesting ConstructorService.getSections()");
		for (Section existingSection : service.getSections())
			System.out.println(existingSection.toString());


	 	// test getSection(Integer id)
		System.out.println("\nTesting ConstructorService.getSection(Integer id)");

		id = 2;
		section	= service.getSection(id);
		if (section != null)
			System.out.println(section);
		else
			System.out.println(getConcatenation(sb, "section with id = ", id.toString(), " not found"));

		id = -666;
		section = service.getSection(id);
		if (section != null)
			System.out.println(section);
		else
			System.out.println(getConcatenation(sb, "section with id = ", id.toString(), " not found"));


		// createSection(String name)
		System.out.println("\nTesting ConstructorService.createSection(String name)");
		section = new Section();
		section.setName("Коллоидная химия");

		try
		{
			id = service.createSection(section);
			System.out.println(getConcatenation(sb, "section with name \"", section.getName(), "\" created successfully"));
			System.out.println(getConcatenation(sb, "created section id: ", id.toString()));
		}
		catch (NotUniqueException e)
		{
			System.out.println(e.getMessage());
		}


		// deleteSection(Integer id)
		System.out.println("\nTesting ConstructorService.deleteSection(Integer id)");
		service.deleteSection(id);
		System.out.println(getConcatenation(sb, "Section with id = ", id.toString(), " deleted successfully"));

		// check it was deleted
		System.out.println(getConcatenation(sb, "Trying to find session with id = ", id.toString()));
		section = service.getSection(id);
		if (section != null)
			System.out.println(section);
		else
			System.out.println(getConcatenation(sb, "Section with id = ", id.toString(), " not found"));


		// getEntities(Integer sectionId)
		List<Entity> entities;

		System.out.println("\nTesting ConstructorService.getEntities(Integer sectionId)");
		id = 1; // todo: use gotten from base id of created section

		System.out.println(getConcatenation(sb, "Entities for section with id = ", id.toString(), ": ")); // todo: write section name too
		entities = service.getEntities(id);

		if (!entities.isEmpty())
		{
			for (Entity sectionEntity : entities)
				System.out.println(sectionEntity);
		}
		else
		{
			System.out.println(getConcatenation(sb, "No entities found for section with id = ", id.toString())); // todo: write section name too
		}


		id = -666; // todo: use gotten from base id of created section

		System.out.println(getConcatenation(sb, "Entities for section with id = ", id.toString(), ": ")); // todo: write section name too
		entities = service.getEntities(id);

		if (!entities.isEmpty())
		{
			for (Entity sectionEntity : entities)
				System.out.println(sectionEntity);
		}
		else
		{
			System.out.println(getConcatenation(sb, "No entities found for section with id = ", id.toString())); // todo: write section name too
		}


		// getEntity(Integer id)
		Entity entity;

		System.out.println("\nTesting ConstructorService.getEntity(Integer id)");

		id = 1;
		entity	= service.getEntity(id);
		if (entity != null)
			System.out.println(entity);
		else
			System.out.println(getConcatenation(sb, "entity with id = ", id.toString(), " not found"));

		id = -666;
		entity = service.getEntity(id);
		if (entity != null)
			System.out.println(entity);
		else
			System.out.println(getConcatenation(sb, "entity with id = ", id.toString(), " not found"));


		// createEntityValue(Integer sectionId, Entity entity)
		Integer sectionId = 1; // todo: get from existing created section

		System.out.println("\nTesting ConstructorService.createEntityValue(Integer sectionId, Entity entity)");
		entity = new Entity();
		entity.setName("Четырехкомпонентная взаимная система");

		try
		{
			id = service.createEntity(sectionId, entity);
			System.out.println(getConcatenation(sb, "entity with name \"", entity.getName(), "\" and sectionId = ", sectionId.toString(), " created successfully"));
			System.out.println(getConcatenation(sb, "created entity id: ", id.toString()));
		}
		catch (NotUniqueException e)
		{
			System.out.println(e.getMessage());
		}
		catch (ConstraintException e)
		{
			System.out.println(e.getMessage());
		}

		// deleteEntity(Integer id)
		System.out.println("\nTesting ConstructorService.deleteEntity(Integer id)");
		service.deleteEntity(id);
		System.out.println(getConcatenation(sb, "Entity with id = ", id.toString(), " deleted successfully"));

		// check it was deleted
		System.out.println(getConcatenation(sb, "Trying to find entity with id = ", id.toString()));
		entity = service.getEntity(id);
		if (entity != null)
			System.out.println(entity);
		else
			System.out.println(getConcatenation(sb, "Entity with id = ", id.toString(), " not found"));


	}

	private static StringBuffer sb = new StringBuffer();
}