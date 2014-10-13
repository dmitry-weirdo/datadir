/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 */
package ru.datadir.user;

import org.json.JSONObject;
import ru.datadir.model.Operation;
import ru.datadir.model.user.UserService;
import su.opencode.kefir.web.Action;
import su.opencode.kefir.web.InitiableAction;
import su.opencode.kefir.web.JsonServlet;

/**
 * Example usage: <a href="http://localhost:8080/datadir/userRightsCheck?userLogin=testUserLogin&entityId=1&operation=create">link</a>
 */
public class UserRightsCheckServlet extends JsonServlet
{
	protected Action getAction() {
		return new InitiableAction()
		{
			public void doAction() throws Exception {
				String userLogin = getStringParam("userLogin", false);
				Long entityId = getLongParam("entityId");

				String operationStr = getStringParam("operation", false);
				Operation operation = Enum.valueOf(Operation.class, operationStr);

				UserService service = getService(UserService.class);
				boolean userHasRights = service.userHasRight(userLogin, entityId, operation);

				JSONObject result = new JSONObject();
				result.put("userHasRights", userHasRights);

				writeSuccess(result);
			}
		};
	}
}