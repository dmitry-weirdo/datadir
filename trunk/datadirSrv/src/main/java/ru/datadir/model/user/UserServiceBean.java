package ru.datadir.model.user;

import org.apache.log4j.Logger;
import ru.datadir.model.Operation;
import su.opencode.kefir.util.ObjectUtils;
import su.opencode.kefir.util.StringUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
@Stateless
public class UserServiceBean implements UserService
{
	@Override
	public boolean userHasRight(String userLogin, Long entityId, Operation operation) {
		if ( StringUtils.empty(userLogin) )
			throw new IllegalArgumentException("userLogin cannot be null or empty");

		if ( entityId == null )
			throw new IllegalArgumentException("entityId cannot be null");

		if ( operation == null )
			throw new IllegalArgumentException("operation cannot be null");

		Query query = em.createQuery("select ueo from UserEntityOperation ueo where (ueo.user.login = :login) and (ueo.entity.id = :entityId) and (ueo.operation = :operation)")
			.setParameter("login", userLogin)
			.setParameter("entityId", entityId)
			.setParameter("operation", operation);

		List resultList = query.getResultList();
		if ( ObjectUtils.empty(resultList) )
			return false;

		if (resultList.size() > 1)
			throw new IllegalStateException( concat(sb, "More than one UserEntityOperation for User with login = \"", userLogin, "\", Entity with id = ", entityId, ", operation = ", operation, ".") );

		return true;
	}

	@PersistenceContext
	private EntityManager em;

	private StringBuffer sb = new StringBuffer();

	private static final Logger logger = Logger.getLogger(UserServiceBean.class);
}