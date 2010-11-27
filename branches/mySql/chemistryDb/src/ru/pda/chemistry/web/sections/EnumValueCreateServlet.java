package ru.pda.chemistry.web.sections;

import ru.pda.chemistry.service.ConstructorService;
import ru.pda.chemistry.service.ServiceFactory;
import ru.pda.chemistry.service.NotUniqueException;
import ru.pda.chemistry.service.ConstraintException;
import ru.pda.chemistry.util.ValidateUtils;
import static ru.pda.chemistry.util.StringUtils.getEncodedString;
import ru.pda.chemistry.entities.Attribute;
import ru.pda.chemistry.entities.EnumValue;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * User: 1
 * Date: 09.05.2010
 * Time: 2:25:57
 */
public class EnumValueCreateServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// todo: if get attributeId fails, forward to attributes list
		Integer attributeId = Integer.parseInt(request.getParameter("attributeId"));

		try
		{
			ConstructorService service = ServiceFactory.getConstructorService();
			request.setAttribute("attribute", service.getAttribute(attributeId));
			request.getRequestDispatcher("constructor/enumValueCreate.jsp").forward(request, response);
		}
		catch (SQLException e)
		{
			throw new ServletException(e);
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String attributeIdStr = request.getParameter("attributeId");
		String enumValueNameStr = request.getParameter("enumValueName");

		Integer attributeId = ValidateUtils.validatePositiveInt(attributeIdStr, request, "attributeIdEmpty", "attributedError", "attributeIdError");
		String enumValueName = ValidateUtils.validateString(enumValueNameStr, request, "enumValueNameEmpty");

		ConstructorService service = ServiceFactory.getConstructorService();
		Attribute attribute;

		try
		{
			attribute = service.getAttribute(attributeId);
		}
		catch (SQLException e)
		{
			throw new ServletException(e);
		}


		if (attributeId == null || enumValueName == null)
		{ // error in validation
			request.setAttribute("attribute", attribute);
			request.setAttribute("enumValueNameStr", getEncodedString(enumValueNameStr));
			request.getRequestDispatcher("constructor/enumValueCreate.jsp").forward(request, response);
			return;
		}

		try
		{
			try
			{
				EnumValue enumValue = new EnumValue();
				enumValue.setName(getEncodedString(enumValueName));
				service.createEnumValue(attributeId, enumValue);

				request.getRequestDispatcher("attributeEdit?=" + attributeId.toString()).forward(request, response);
			}
			catch (NotUniqueException e)
			{
				request.setAttribute("enumValueNameExists", true);

				request.setAttribute("attribute", attribute);
				request.setAttribute("enumValueNameStr", getEncodedString(enumValueNameStr));
				request.getRequestDispatcher("constructor/enumValueCreate.jsp").forward(request, response);
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