package ru.datadir.entity;

import ru.datadir.model.attribute.AttributeType;
import ru.datadir.model.attribute.AttributeTypeService;
import ru.datadir.model.entity.Entity;
import su.opencode.kefir.web.Action;
import su.opencode.kefir.web.InitiableAction;
import su.opencode.kefir.web.JsonServlet;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nosferatum
 * Date: 28.05.13
 * Time: 22:09
 * To change this template use File | Settings | File Templates.
 */
public class EntityGetServlet extends JsonServlet
{
	protected Action getAction() {
		return new InitiableAction()
		{
			public void doAction() throws Exception {
				AttributeTypeService service = getService(AttributeTypeService.class);

				Long entityId = getLongParam("entityId");
				// todo: check entityId

				Entity entity = service.getEntity(entityId);

				// todo: modify JsonServlet.writeJson
				if (entity == null)
					writeSuccess();
				else
					writeJson(entity);
			}
		};
	}
}