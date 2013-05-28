package ru.datadir.attribute;

import ru.datadir.model.attribute.AttributeType;
import ru.datadir.model.attribute.AttributeTypeService;
import su.opencode.kefir.web.Action;
import su.opencode.kefir.web.InitiableAction;
import su.opencode.kefir.web.JsonServlet;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nosferatum
 * Date: 28.05.13
 * Time: 0:11
 * To change this template use File | Settings | File Templates.
 */
public class AttributeTypesListServlet extends JsonServlet
{
	protected Action getAction() {
		return new InitiableAction()
		{
			public void doAction() throws Exception {
				AttributeTypeService service = getService(AttributeTypeService.class);

				List<AttributeType> attributeTypes = service.getAttributeTypes();
				writeJson(attributeTypes);
			}
		};
	}
}