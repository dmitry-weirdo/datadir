/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.datadir.model.section;

import su.opencode.kefir.srv.json.JsonObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Раздел (пакет) сущностей.
 * Может содержать вложенные разделы и&nbsp;сущности.
 */
@Entity
public class Section extends JsonObject
{
	@Id
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne()
	@JoinColumn(name = "parent_section_id")
	public Section getParent() {
		return parent;
	}
	public void setParent(Section parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Идентификатор.
	 */
	private Long id;

	/**
	 * Родительский раздел.
	 */
	private Section parent;

	/**
	 * название раздела для отображения.
	 */
	private String name;

	/**
	 * Примечание.
	 */
	private String comment;
}