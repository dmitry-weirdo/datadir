package ru.pda.chemistry.entities;

import static ru.pda.chemistry.util.StringUtils.getConcatenation;

import java.util.List;

/**
 * User: 1
 * Date: 26.04.2010
 * Time: 23:52:59
 * Раздел химии.
 * К разделу привязаны сущности-справочники, относящиеся к этому разделу химии.
 */
public class Section
{
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Entity> getEntities() {
		return entities;
	}
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

	public String toString() {
		return getConcatenation("Section", "\n\tid: ", id.toString(), "\n\tname: ", name);
	}

	private Integer id;
	private String name;
	private List<Entity> entities;
}