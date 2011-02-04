/**
 * User: 1
 * Date: 04.02.2011
 * Time: 20:57:54
 */
package ru.datadir;

import java.util.List;

/**
 * Пользователь.
 * Пользователь авторизуется в системе с помощью логина и пароля.
 * Пользователь имеет набор прав на просмотр\редактирование справочника, раздела.
 * <br/>
 * В <b>редактирование справочника</b> включено добавление, изменение и удаление записей в справочнике.
 * Право редактирования автоматически получает создатель справочника или раздела.
 * <br/>
 * В <b>редактирование структуры справочника</b> включено добавление, изменение и удаление атрибутов справочника, а также
 * удаление самого справочника.
 * <br/>
 * В <b>редактирование структуры раздела</b> включено добавление, изменение и удаление дочерних разделов и справочников раздела, а также
 * удаление самого раздела целиком.
 * <br/>
 * <b>Если пользователь имеет право на раздел</b>, то он автоматически имеет такое право на все дочерние разделы и все справочники
 * любой вложенности внутри раздела.
 * <br/>
 * // todo: think about запрет на редактирование или просмотр справочника внутри раздела, на который эти права есть
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
	 * Логин.
	 */
	private String login;

	/**
	 * Пароль.
	 */
	private String password;

	/**
	 * Права пользователя на разделы.
	 */
	private List<UserSectionRight> sectionRights;

	/**
	 * Права пользователя на справочники.
	 */
	private List<UserDirectoryRight> directoryRights;
}