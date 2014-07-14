package ru.datadir.model.user;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */

import ru.datadir.model.Operation;
import ru.datadir.model.entity.Entity;

/**
 * Разрешенная операция пользователю с сущностями.
 */
public class UserEntityOperation
{
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	public Operation getOperation() {
		return operation;
	}
	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	/**
	 * Пользователь.
	 */
	private User user;

	/**
	 * Сущность.
	 */
	private Entity entity;

	/**
	 * Операция, разрешенная пользователю по отношению к сущности.
	 */
	private Operation operation;
}