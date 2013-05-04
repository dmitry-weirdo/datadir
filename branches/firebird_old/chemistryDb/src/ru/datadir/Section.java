/**
 * User: 1
 * Date: 30.12.2010
 * Time: 23:31:03
 */
package ru.datadir;

/**
 * Раздел.
 */
public class Section
{
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Section getParent() {
		return parent;
	}
	public void setParent(Section parent) {
		this.parent = parent;
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
	 * Код раздела.
	 */
	private Integer id;

	/**
	 * Родительский раздел. <br/>
	 * Для корневого раздела равен <code>null</code>.
	 */
	private Section parent;

	/**
	 * Логин {@linkplain User пользователя}, создавшего раздел.
	 */
	private String createdBy;

	/**
	 * Название раздела. Название уникально на текущем уровне (внутри одного родительского раздела либо среди корневых разделов).
	 */
	private String name;
}