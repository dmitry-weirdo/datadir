/**
 * User: 1
 * Date: 30.12.2010
 * Time: 23:31:03
 */
package ru.datadir;

/**
 * Раздел.
 */
public class Package
{
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
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
	 * Код родительского раздела. <br/>
	 * Для корневого раздела равен <code>null</code>.
	 */
	private Integer parentId;

	/**
	 * Название раздела. Название уникально на текущем уровне (внутри одного родительского раздела либо среди корневых разделов).
	 */
	private String name;
}