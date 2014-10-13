package ru.datadir.model.entity;

import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
@Stateless
public class EntityServiceBean implements EntityService
{
	@Override
	public void executeCreateTable(String tableName) {
		sb.delete(0, sb.length());

		sb.append("create table ").append(tableName).append(" (");

		// append fields
		sb.append( " id").append(" bigint").append(" not null");
		sb.append(", name").append(" varchar(255)");

		sb.append(" );");

		String queryStr = sb.toString();
		logger.info( concat(sb, "create table query: ", queryStr) );

		Query query = em.createNativeQuery(queryStr);
		query.executeUpdate();
	}

	@PersistenceContext
	private EntityManager em;

	private StringBuffer sb = new StringBuffer();

	private static final Logger logger = Logger.getLogger(EntityServiceBean.class);
}