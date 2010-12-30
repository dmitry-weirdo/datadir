/**
 * User: 1
 * Date: 30.12.2010
 * Time: 23:31:03
 */
package ru.datadir;

/**
 * ������.
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
	 * ��� �������.
	 */
	private Integer id;

	/**
	 * ��� ������������� �������. <br/>
	 * ��� ��������� ������� ����� <code>null</code>.
	 */
	private Integer parentId;

	/**
	 * �������� �������. �������� ��������� �� ������� ������ (������ ������ ������������� ������� ���� ����� �������� ��������).
	 */
	private String name;
}