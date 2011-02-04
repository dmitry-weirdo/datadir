/**
 * User: 1
 * Date: 04.02.2011
 * Time: 20:54:09
 */
package ru.datadir;

/**
 * —правочник.
 * ѕрив€зан к разделу, в котором находитс€.
 * —правочник имеет произвольный набор полей различных типов.
 * // todo: описание, как хран€тс€ атрибуты и справочники в базе
 */
public class Directory
{
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Section getSection() {
		return section;
	}
	public void setSection(Section section) {
		this.section = section;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 *  од справочника.
	 */
	private Integer id;

	/**
	 * –аздел, в котором находитс€ справочник.
	 */
	private Section section;

	/**
	 * Ћогин {@linkplain User пользовател€}, создавшего справочник.
	 */
	private String createdBy;

	/**
	 * Ќазвание справочника. Ќазвание уникально на текущем уровне (внутри одного родительского раздела).
	 */
	private String name;

	// todo: список атрибутов
}