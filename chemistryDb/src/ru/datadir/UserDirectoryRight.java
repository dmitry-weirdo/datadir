/**
 * User: 1
 * Date: 04.02.2011
 * Time: 21:03:21
 */
package ru.datadir;

/**
 * ����� ������������ �� ����������.
 * ������ ������� ������������ ������������� ���� ������ � ������� ���� ������������.
 */
public class UserDirectoryRight
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
	public Directory getDirectory() {
		return directory;
	}
	public void setDirectory(Directory directory) {
		this.directory = directory;
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
	 * ����������.
	 */
	private Directory directory;

	/**
	 * ����� ������������.
	 */
	private Right right;
}