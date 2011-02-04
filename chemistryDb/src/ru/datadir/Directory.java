/**
 * User: 1
 * Date: 04.02.2011
 * Time: 20:54:09
 */
package ru.datadir;

/**
 * ����������.
 * �������� � �������, � ������� ���������.
 * ���������� ����� ������������ ����� ����� ��������� �����.
 * // todo: ��������, ��� �������� �������� � ����������� � ����
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
	 * ��� �����������.
	 */
	private Integer id;

	/**
	 * ������, � ������� ��������� ����������.
	 */
	private Section section;

	/**
	 * ����� {@linkplain User ������������}, ���������� ����������.
	 */
	private String createdBy;

	/**
	 * �������� �����������. �������� ��������� �� ������� ������ (������ ������ ������������� �������).
	 */
	private String name;

	// todo: ������ ���������
}