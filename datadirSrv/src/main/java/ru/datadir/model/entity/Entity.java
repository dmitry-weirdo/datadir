package ru.datadir.model.entity;

import ru.datadir.model.attribute.Attribute;
import ru.datadir.model.section.Section;
import su.opencode.kefir.srv.json.JsonObject;

import javax.persistence.Transient;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nosferatum
 * Date: 16.05.13
 * Time: 22:29
 * To change this template use File | Settings | File Templates.
 * <p/>
 * Сущность.
 * Метаданные, описывающие сущность.
 * На их основе генерируется таблица базы данных, хранящая экземпляры сущностей.
 */
public class Entity extends JsonObject
{
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Section getSection() {
		return section;
	}
	public void setSection(Section section) {
		this.section = section;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public List<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	@Transient
	public int getMaxAttributeNameLength() {
		int maxAttributeNameLength = -1;
		for (Attribute attribute : attributes)
		{
			int length = attribute.getName().length();
			if (length > maxAttributeNameLength)
				maxAttributeNameLength = length;
		}

		return maxAttributeNameLength;
	}


	/**
	 * Идентификатор.
	 */
	private Long id;

	/**
	 * Раздел (пакет), к&nbsp;которому относится сущность.
	 */
	private Section section;

	/**
	 * Имя сущности. Валидный идентификатор для названия таблицы базы данных.
	 */
	private String name;

	/**
	 * Лейбл для сущности.
	 */
	private String label;

	/**
	 * Комментарий к сущности.
	 */
	private String comment;

	/**
	 * Список атрибутов сущности.
	 */
	private List<Attribute> attributes;

	// todo: валидаторы на сущность целиком
}