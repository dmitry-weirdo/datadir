/**
 * User: 1
 * Date: 30.12.2010
 * Time: 23:31:18
 */
package ru.datadir;

import java.util.List;

/**
 * ������ ��, ���������� � ���������.
 * // todo: ����� ����������� ��������, ���� ��� ����
 * // todo: ������ ������ �� �������������
 */
public interface SectionService
{
	/**
	 * @return ������ �������� ��������, ������������� �� ��������.
	 */
	List<Section> getRootSections();

	/**
	 * ������� �������� ������. ParentId (��� ������������� �������) ��� ��������� ������� ��������������� � <code>null</code>.
	 * @param name �������� ������������ ��������� �������
	 * @return ��� ���������� �������
	 * @throws ru.datadir.ConstraintException ���� �������� ������ � ����� ��������� (���������� �� ��������) ��� ����������. 
	 */
	Integer createRootSection(String name) throws ConstraintException;

	/**
	 * ������� �������� ������. ������ � �������� ��������� ��� ���������� �� ����� �������������.<br/>
	 * ���� ������ ��� ��������������� �������, ������ �� ����������.
	 * // todo: think about �������������, ���� � ������� ���� ����������� �� ����� �������
	 * // todo: ������, ��� ���� ����� ���������� �� deleteSection, �������, ���� ����� ���� ����� ������.
	 * @param id ��� ���������� �������.
	 * @throws ru.datadir.ConstraintException ���� ������� ������������ �� ����� ����� �� �������� ���������� �������
	 */
	void deleteRootSection(Integer id) throws ConstraintException;


	/**
	 * @param parentId ��� �������
	 * @return ������ ���������������� ����������� ���������� �������, �������������� �� ���������. (�� ���� ���, � ������� parentId ����� ����������)
	 */
	List<Section> getSections(Integer parentId);

	/**
	 * ������� ��������� � ��������� �������. ParentId ���������� ����������� ��������������� � parentId (��� �������). 
	 * @param parentId ��� �������
	 * @param name �������� ������������ �������
	 * @return ��� ���������� ����������
	 * @throws ConstraintException ���� ������ �� ���������� (�� ������ �� parentId),
	 * ���� � ������� ��� ���������� ��������� � ������, ������ name (���������� �� ��������) 
	 */
	Integer createSection(Integer parentId, String name) throws ConstraintException;

	/**
	 * ������� ���������. ������ � ��� ��������� ��� ��� ���������� ������ �� �������������.
	 * // todo: think about �������������, ���� � ������� ���� ����������� �� ����� �������
	 * @param id ��� ����������.
	 */
	void deleteSection(Integer id);
}