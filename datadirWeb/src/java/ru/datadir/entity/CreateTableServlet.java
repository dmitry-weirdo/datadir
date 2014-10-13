package ru.datadir.entity;

import ru.datadir.model.entity.EntityService;
import su.opencode.kefir.web.Action;
import su.opencode.kefir.web.InitiableAction;
import su.opencode.kefir.web.JsonServlet;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
public class CreateTableServlet extends JsonServlet
{
	protected Action getAction() {
		return new InitiableAction()
		{
			public void doAction() throws Exception {
				String tableName = getStringParam("tableName", false);

				EntityService service = getService(EntityService.class);

				service.executeCreateTable(tableName);
				writeSuccess();
			}
		};
	}
}