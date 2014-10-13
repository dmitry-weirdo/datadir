package ru.datadir.model.user;

import ru.datadir.model.Operation;

import javax.ejb.Local;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
@Local
public interface UserService
{
	boolean userHasRight(String userLogin, Long entityId, Operation operation);
}