/**
 * User: 1
 * Date: 04.02.2011
 * Time: 21:16:38
 */
package ru.datadir;

import java.util.List;

/**
 * Сервис БД, работающий с пользователями.
 */
public interface UserService
{
	/**
	 * @param login логин пользователя
	 * @return пользователь с указанным логином, с прогруженными правами 
	 */
	User getUser(String login);

	/**
	 * Создает пользователя с указанными логином и паролем.
	 * @param user пользователь
	 * @return логин созданного пользователя
	 * @throws ConstraintException если пользователь с указанным логином уже существует
	 */
	String createUser(User user) throws ConstraintException;

	
	/**
	 * @param login логин пользователя
	 * @param sectionId код раздела
	 * @return список прав пользователя на указанный раздел. Список может быть пустым.
	 */
	List<Right> getSectionRights(String login, Integer sectionId);

	/**
	 * Добавляет пользователю с указанным логином указанное право на раздел с указанным кодом.
	 * Если пользователь уже имеет это право, то ничего не происходит.
	 * @param login логин пользователя
	 * @param sectionId код раздела
	 * @param right право
	 * @return код созданного права или <code>null</code>, если право уже существовало
	 * @throws ConstraintException если пользователю не может быть назначено право на указанный раздел (либо на раздел вообще не может быть назначено такое право)
	 * // todo: описать случаи, когда такое добавление невозможно
	 */
	Integer createSectionRight(String login, Integer sectionId, Right right) throws ConstraintException;

	/**
	 * Удаляет право на раздел.
	 * @param id код права на раздел
	 * @throws ConstraintException если право не может быть удалено (например, пользователь является последним имеющим право на раздел (?) )
	 * // todo: описать случаи, когда такое удаление невозможно
	 */
	void deleteSectionRight(Integer id) throws ConstraintException;


	/**
	 * @param login логин пользователя
	 * @param directoryId код справочника
	 * @return список прав пользователя на указанный справочник. Список может быть пустым.
	 */
	List<Right> getDirectoryRights(String login, Integer directoryId);

	/**
	 * Добавляет пользователю с указанным логином указанное право на справочник с указанным кодом.
	 * Если пользователь уже имеет это право, то ничего не происходит.
	 * @param login логин пользователя
	 * @param directoryId код справочника
	 * @param right право
	 * @return код созданного права или <code>null</code>, если право уже существовало
	 * @throws ConstraintException если пользователю не может быть назначено право на указанный справочник (либо на справочник вообще не может быть назначено такое право)
	 * // todo: описать случаи, когда такое добавление невозможно
	 */
	Integer createDirectoryRight(String login, Integer directoryId, Right right) throws ConstraintException;

	/**
	 * Удаляет право на справочник.
	 * @param id код права на справочник
	 * @throws ConstraintException если право не может быть удалено (например, пользователь является последним имеющим право на справочник (?) )
	 * // todo: описать случаи, когда такое удаление невозможно
	 */
	void deleteDirectoryRight(Integer id) throws ConstraintException;
}
