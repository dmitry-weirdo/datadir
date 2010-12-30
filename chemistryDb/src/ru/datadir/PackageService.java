/**
 * User: 1
 * Date: 30.12.2010
 * Time: 23:31:18
 */
package ru.datadir;

import java.util.List;

/**
 * Сервис БД, работающий с разделами.
 */
public interface PackageService
{
	/**
	 * @return список корневых разделов, упорядоченный по названию.
	 */
	List<Package> getRootPackages();

	/**
	 * Создает корневой раздел. ParentId (код родительского раздела) для корневого раздела устанавливается в <code>null</code>.
	 * @param name название создаваемого корневого раздела
	 * @return код созданного раздела
	 * @throws ru.datadir.ConstraintException если корневой раздел с таким названием (независимо от регистра) уже существует. 
	 */
	Integer createRootPackage(String name) throws ConstraintException;

	/**
	 * Удаляет корневой раздел. Вместе с разделом удаляются все подразделы со всеми справочниками.<br/>
	 * Если указан код несуществующего раздела, ничего не происходит.
	 * // todo: think about выбрасываться, если в разделе есть справочники на любой глубине
	 * @param id код удаляемого раздела.
	 */
	void deleteRootPackage(Integer id);


	/**
	 * @param parentId код раздела
	 * @return список непосредственных подразделов указанного раздела, упорядоченыный по назнванию. (То есть тех, у которых parentId равен указанному)
	 */
	List<Package> getPackages(Integer parentId);

	/**
	 * Создает подраздел в указанном разделе. ParentId созданного подразедела устанавливается в parentId (код раздела). 
	 * @param parentId код раздела
	 * @param name название создаваемого раздела
	 * @return код созданного подраздела
	 * @throws ConstraintException если раздел не существует (не найден по parentId),
	 * либо в разделе уже существует подраздел с именем, равным name (независимо от регистра) 
	 */
	Integer createPackage(Integer parentId, String name) throws ConstraintException;

	/**
	 * Удаляет подраздел. Вместе с ним удаляются все его подразделы вместе со справочниками.
	 * // todo: think about выбрасываться, если в разделе есть справочники на любой глубине
	 * @param id код подраздела.
	 */
	void deletePackage(Integer id);
}