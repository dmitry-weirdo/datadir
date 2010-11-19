package ru.pda.chemistry.entities;

import static ru.pda.chemistry.util.StringUtils.getConcatenation;

import java.util.List;

/**
 * User: 1
 * Date: 27.04.2010
 * Time: 1:47:33
 * Сущность (определяет формат справочник).
 * Привязана к определенному разделу химии.
 * Имеет набор атрибутов.
 */
public class Entity
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
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Section getSection() {
		return section;
	}
	public void setSection(Section section) {
		this.section = section;
	}
	public List<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public String toString() {
		return getConcatenation("Entity", "\n\tid: ", id.toString(), "\n\tname: ", name, "\n\tsection: ", section.getName());
	}

	// transient
	public boolean isEditable() {
		return tableName == null;
	}
	
	private Integer id;
	private String name;
	private String tableName;
	private Section section;
	private List<Attribute> attributes;
}