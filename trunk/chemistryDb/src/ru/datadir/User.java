/**
 * User: 1
 * Date: 04.02.2011
 * Time: 20:57:54
 */
package ru.datadir;

import java.util.List;

/**
 * ������������.
 * ������������ ������������ � ������� � ������� ������ � ������.
 * ������������ ����� ����� ���� �� ��������\�������������� �����������, �������.
 * <br/>
 * � <b>�������������� �����������</b> �������� ����������, ��������� � �������� ������� � �����������.
 * ����� �������������� ������������� �������� ��������� ����������� ��� �������.
 * <br/>
 * � <b>�������������� ��������� �����������</b> �������� ����������, ��������� � �������� ��������� �����������, � �����
 * �������� ������ �����������.
 * <br/>
 * � <b>�������������� ��������� �������</b> �������� ����������, ��������� � �������� �������� �������� � ������������ �������, � �����
 * �������� ������ ������� �������.
 * <br/>
 * <b>���� ������������ ����� ����� �� ������</b>, �� �� ������������� ����� ����� ����� �� ��� �������� ������� � ��� �����������
 * ����� ����������� ������ �������.
 * <br/>
 * // todo: think about ������ �� �������������� ��� �������� ����������� ������ �������, �� ������� ��� ����� ����
 */
public class User
{
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<UserSectionRight> getSectionRights() {
		return sectionRights;
	}
	public void setSectionRights(List<UserSectionRight> sectionRights) {
		this.sectionRights = sectionRights;
	}
	public List<UserDirectoryRight> getDirectoryRights() {
		return directoryRights;
	}
	public void setDirectoryRights(List<UserDirectoryRight> directoryRights) {
		this.directoryRights = directoryRights;
	}

	/**
	 * �����.
	 */
	private String login;

	/**
	 * ������.
	 */
	private String password;

	/**
	 * ����� ������������ �� �������.
	 */
	private List<UserSectionRight> sectionRights;

	/**
	 * ����� ������������ �� �����������.
	 */
	private List<UserDirectoryRight> directoryRights;
}