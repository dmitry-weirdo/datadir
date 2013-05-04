/**
 * User: 1
 * Date: 04.02.2011
 * Time: 21:16:38
 */
package ru.datadir;

import java.util.List;

/**
 * ������ ��, ���������� � ��������������.
 */
public interface UserService
{
	/**
	 * @param login ����� ������������
	 * @return ������������ � ��������� �������, � ������������� ������� 
	 */
	User getUser(String login);

	/**
	 * ������� ������������ � ���������� ������� � �������.
	 * @param user ������������
	 * @return ����� ���������� ������������
	 * @throws ConstraintException ���� ������������ � ��������� ������� ��� ����������
	 */
	String createUser(User user) throws ConstraintException;

	
	/**
	 * @param login ����� ������������
	 * @param sectionId ��� �������
	 * @return ������ ���� ������������ �� ��������� ������. ������ ����� ���� ������.
	 */
	List<Right> getSectionRights(String login, Integer sectionId);

	/**
	 * ��������� ������������ � ��������� ������� ��������� ����� �� ������ � ��������� �����.
	 * ���� ������������ ��� ����� ��� �����, �� ������ �� ����������.
	 * @param login ����� ������������
	 * @param sectionId ��� �������
	 * @param right �����
	 * @return ��� ���������� ����� ��� <code>null</code>, ���� ����� ��� ������������
	 * @throws ConstraintException ���� ������������ �� ����� ���� ��������� ����� �� ��������� ������ (���� �� ������ ������ �� ����� ���� ��������� ����� �����)
	 * // todo: ������� ������, ����� ����� ���������� ����������
	 */
	Integer createSectionRight(String login, Integer sectionId, Right right) throws ConstraintException;

	/**
	 * ������� ����� �� ������.
	 * @param id ��� ����� �� ������
	 * @throws ConstraintException ���� ����� �� ����� ���� ������� (��������, ������������ �������� ��������� ������� ����� �� ������ (?) )
	 * // todo: ������� ������, ����� ����� �������� ����������
	 */
	void deleteSectionRight(Integer id) throws ConstraintException;


	/**
	 * @param login ����� ������������
	 * @param directoryId ��� �����������
	 * @return ������ ���� ������������ �� ��������� ����������. ������ ����� ���� ������.
	 */
	List<Right> getDirectoryRights(String login, Integer directoryId);

	/**
	 * ��������� ������������ � ��������� ������� ��������� ����� �� ���������� � ��������� �����.
	 * ���� ������������ ��� ����� ��� �����, �� ������ �� ����������.
	 * @param login ����� ������������
	 * @param directoryId ��� �����������
	 * @param right �����
	 * @return ��� ���������� ����� ��� <code>null</code>, ���� ����� ��� ������������
	 * @throws ConstraintException ���� ������������ �� ����� ���� ��������� ����� �� ��������� ���������� (���� �� ���������� ������ �� ����� ���� ��������� ����� �����)
	 * // todo: ������� ������, ����� ����� ���������� ����������
	 */
	Integer createDirectoryRight(String login, Integer directoryId, Right right) throws ConstraintException;

	/**
	 * ������� ����� �� ����������.
	 * @param id ��� ����� �� ����������
	 * @throws ConstraintException ���� ����� �� ����� ���� ������� (��������, ������������ �������� ��������� ������� ����� �� ���������� (?) )
	 * // todo: ������� ������, ����� ����� �������� ����������
	 */
	void deleteDirectoryRight(Integer id) throws ConstraintException;
}
