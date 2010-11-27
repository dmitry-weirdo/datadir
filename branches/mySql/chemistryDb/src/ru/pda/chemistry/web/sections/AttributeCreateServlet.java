package ru.pda.chemistry.web.sections;

import ru.pda.chemistry.entities.Attribute;
import ru.pda.chemistry.entities.AttributeType;
import ru.pda.chemistry.entities.Entity;
import ru.pda.chemistry.service.ConstraintException;
import ru.pda.chemistry.service.ConstructorService;
import ru.pda.chemistry.service.NotUniqueException;
import ru.pda.chemistry.service.ServiceFactory;
import static ru.pda.chemistry.util.StringUtils.getEncodedString;
import ru.pda.chemistry.util.ValidateUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * User: 1
 * Date: 08.05.2010
 * Time: 23:16:18
 */
public class AttributeCreateServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// todo: if get entityId fails, forward to attributes list
		Integer entityId = Integer.parseInt(request.getParameter("entityId"));

		try
		{
			ConstructorService service = ServiceFactory.getConstructorService();
			request.setAttribute("entity", service.getEntity(entityId));
			request.getRequestDispatcher("constructor/attributeCreate.jsp").forward(request, response);
		}
		catch (SQLException e)
		{
			throw new ServletException(e);
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String entityIdStr = request.getParameter("entityId");
		String attributeNameStr = request.getParameter("attributeName");
		String attributeMeasureUnitStr = request.getParameter("attributeMeasureUnit");
		String attributeTypeStr = request.getParameter("attributeType");

		Integer entityId = ValidateUtils.validatePositiveInt(entityIdStr, request, "entityIdEmpty", "entityIdError", "entityIdError");
		String attributeName = ValidateUtils.validateString(attributeNameStr, request, "attributeNameEmpty");
		String attributeMeasureUnit = ValidateUtils.validateString(attributeMeasureUnitStr, request, "attributeMeasureUnitEmpty"); 
		Integer attributeType = ValidateUtils.validateNonNegativeInt(attributeTypeStr, request, "attributeTypeEmpty", "attributeTypeError", "attributeTypeEmpty");

		AttributeType type = Attribute.getAttributeType(attributeType);
		if (type == null)
			attributeType = null;


		ConstructorService service = ServiceFactory.getConstructorService();
		Entity entity;

		try
		{
			entity = service.getEntity(entityId);
		}
		catch (SQLException e)
		{
			throw new ServletException(e);
		}


		if (entityId == null || attributeName == null || attributeMeasureUnit == null || attributeType == null)
		{ // error in validation
			request.setAttribute("entity", entity);
			request.setAttribute("attributeNameStr", getEncodedString(attributeNameStr));
			request.setAttribute("attributeMeasureUnitStr", getEncodedString(attributeMeasureUnitStr));
			request.setAttribute("attributeTypeStr", getEncodedString(attributeTypeStr));
			request.getRequestDispatcher("constructor/attributeCreate.jsp").forward(request, response);
			return;
		}

		try
		{
			try
			{
				Attribute attribute = new Attribute();
				attribute.setName(getEncodedString(attributeName));
				attribute.setMeasureUnit(getEncodedString(attributeMeasureUnit));
				attribute.setType(type);
				service.createAttribute(entityId, attribute);

				request.getRequestDispatcher("entityEdit?entityId=" + entityId.toString()).forward(request, response);
			}
			catch (NotUniqueException e)
			{
				request.setAttribute("attributeNameExists", true);

				request.setAttribute("entity", entity);
				request.setAttribute("attributeNameStr", getEncodedString(attributeNameStr));
				request.setAttribute("attributeMeasureUnitStr", getEncodedString(attributeMeasureUnitStr));
				request.setAttribute("attributeTypeStr", getEncodedString(attributeTypeStr));
				request.getRequestDispatcher("constructor/attributeCreate.jsp").forward(request, response);
			}
			catch (ConstraintException e)
			{
				throw new ServletException(e); // todo: think about this handling
			}
		}
		catch (SQLException e)
		{
			throw new ServletException(e); // todo: think about this handling
		}
	}
}