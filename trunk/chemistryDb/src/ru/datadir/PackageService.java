/**
 * User: 1
 * Date: 30.12.2010
 * Time: 23:31:18
 */
package ru.datadir;

import java.util.List;

/**
 * ������ ��, ���������� � ���������.
 */
public interface PackageService
{
	/**
	 * @return ������ �������� ��������, ������������� �� ��������.
	 */
	List<Package> getRootPackages();

	/**
	 * ������� �������� ������. ParentId (��� ������������� �������) ��� ��������� ������� ��������������� � <code>null</code>.
	 * @param name �������� ������������ ��������� �������
	 * @return ��� ���������� �������
	 * @throws ru.datadir.ConstraintException ���� �������� ������ � ����� ��������� (���������� �� ��������) ��� ����������. 
	 */
	Integer createRootPackage(String name) throws ConstraintException;

	/**
	 * ������� �������� ������. ������ � �������� ��������� ��� ���������� �� ����� �������������.<br/>
	 * ���� ������ ��� ��������������� �������, ������ �� ����������.
	 * // todo: think about �������������, ���� � ������� ���� ����������� �� ����� �������
	 * @param id ��� ���������� �������.
	 */
	void deleteRootPackage(Integer id);


	/**
	 * @param parentId ��� �������
	 * @return ������ ���������������� ����������� ���������� �������, �������������� �� ���������. (�� ���� ���, � ������� parentId ����� ����������)
	 */
	List<Package> getPackages(Integer parentId);

	/**
	 * ������� ��������� � ��������� �������. ParentId ���������� ����������� ��������������� � parentId (��� �������). 
	 * @param parentId ��� �������
	 * @param name �������� ������������ �������
	 * @return ��� ���������� ����������
	 * @throws ConstraintException ���� ������ �� ���������� (�� ������ �� parentId),
	 * ���� � ������� ��� ���������� ��������� � ������, ������ name (���������� �� ��������) 
	 */
	Integer createPackage(Integer parentId, String name) throws ConstraintException;

	/**
	 * ������� ���������. ������ � ��� ��������� ��� ��� ���������� ������ �� �������������.
	 * // todo: think about �������������, ���� � ������� ���� ����������� �� ����� �������
	 * @param id ��� ����������.
	 */
	void deletePackage(Integer id);
}