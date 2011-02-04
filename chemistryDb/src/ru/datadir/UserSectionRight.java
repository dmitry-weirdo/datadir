/**
 * User: 1
 * Date: 04.02.2011
 * Time: 21:03:21
 */
package ru.datadir;

/**
 * Право пользователя на раздел.
 * Одному правилу пользователя соответствует одна запись в таблице прав пользователя.
 *
 * // todo: надо наверно отдельные права на редактирование корневых разделов
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
	 * Код права.
	 */
	private Integer id;

	/**
	 * Пользователь.
	 */
	private User user;

	/**
	 * Раздел.
	 */
	private Section section;

	/**
	 * Право пользователя.
	 */
	private Right right;
}