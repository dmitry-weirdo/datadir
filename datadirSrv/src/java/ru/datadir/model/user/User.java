package ru.datadir.model.user;

/**
 * Created with IntelliJ IDEA.
 * User: nosferatum
 * Date: 04.05.13
 * Time: 17:36
 * To change this template use File | Settings | File Templates.
 * <p/>
 * Пользователь системы.
 * Может логиниться в систему.
 * Имеет набор прав на CRUD-операции с пакетами и справочниками.
 */
public class User
{
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Идентификатор.
	 */
	private Long id;

	/**
	 * Логин.
	 */
	private String login;

	/**
	 * Пароль.
	 */
	private String password;

	/**
	 * Имя пользователя (например, ФИО).
	 */
	private String name;

	// todo: другие параметры (например, displayName, nickname etc.)
}