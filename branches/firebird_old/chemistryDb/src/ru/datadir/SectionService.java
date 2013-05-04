/**
 * User: 1
 * Date: 30.12.2010
 * Time: 23:31:18
 */
package ru.datadir;

import java.util.List;

/**
 * Сервис БД, работающий с разделами.
 * // todo: везде выбрасывать экспешны, если нет прав
 * // todo: методы работы со справочниками
 */
public interface SectionService
{
	/**
	 * @return список корневых разделов, упорядоченный по названию.
	 */
	List<Section> getRootSections();

	/**
	 * Создает корневой раздел. ParentId (код родительского раздела) для корневого раздела устанавливается в <code>null</code>.
	 * @param name название создаваемого корневого раздела
	 * @return код созданного раздела
	 * @throws ru.datadir.ConstraintException если корневой раздел с таким названием (независимо от регистра) уже существует. 
	 */
	Integer createRootSection(String name) throws ConstraintException;

	/**
	 * Удаляет корневой раздел. Вместе с разделом удаляются все подразделы со всеми справочниками.<br/>
	 * Если указан код несуществующего раздела, ничего не происходит.
	 * // todo: think about выбрасываться, если в разделе есть справочники на любой глубине
	 * // todo: Неясно, чем этот метод отличается от deleteSection, наверно, этот метод надо будет убрать.
	 * @param id код удаляемого раздела.
	 * @throws ru.datadir.ConstraintException если текущий пользователь не имеет права на удаление указанного раздела
	 */
	void deleteRootSection(Integer id) throws ConstraintException;


	/**
	 * @param parentId код раздела
	 * @return список непосредственных подразделов указанного раздела, упорядоченыный по назнванию. (То есть тех, у которых parentId равен указанному)
	 */
	List<Section> getSections(Integer parentId);

	/**
	 * Создает подраздел в указанном разделе. ParentId созданного подразедела устанавливается в parentId (код раздела). 
	 * @param parentId код раздела
	 * @param name название создаваемого раздела
	 * @return код созданного подраздела
	 * @throws ConstraintException если раздел не существует (не найден по parentId),
	 * либо в разделе уже существует подраздел с именем, равным name (независимо от регистра) 
	 */
	Integer createSection(Integer parentId, String name) throws ConstraintException;

	/**
	 * Удаляет подраздел. Вместе с ним удаляются все его подразделы вместе со справочниками.
	 * // todo: think about выбрасываться, если в разделе есть справочники на любой глубине
	 * @param id код подраздела.
	 */
	void deleteSection(Integer id);
}