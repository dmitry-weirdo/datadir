package ru.pda.chemistry.entities;

/**
 * User: 1
 * Date: 01.05.2010
 * Time: 11:38:47
 * Одно из значений атрибута, имеющего тип "множество".
 */
public class EnumValue
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
	public Attribute getAttribute() {
		return attribute;
	}
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	
	private Integer id;
	private String name;
	private Attribute attribute;
}