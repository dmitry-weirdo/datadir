/**
 * User: 1
 * Date: 04.02.2011
 * Time: 21:03:21
 */
package ru.datadir;

/**
 * ����� ������������ �� ������.
 * ������ ������� ������������ ������������� ���� ������ � ������� ���� ������������.
 *
 * // todo: ���� ������� ��������� ����� �� �������������� �������� ��������
 */
public class UserSectionRight
{
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Section getSection() {
		return section;
	}
	public void setSection(Section section) {
		this.section = section;
	}
	public Right getRight() {
		return right;
	}
	public void setRight(Right right) {
		this.right = right;
	}
	
	/**
	 * ��� �����.
	 */
	private Integer id;

	/**
	 * ������������.
	 */
	private User user;

	/**
	 * ������.
	 */
	private Section section;

	/**
	 * ����� ������������.
	 */
	private Right right;
}