package ru.pda.chemistry.web.sections;

import ru.pda.chemistry.service.ConstructorService;
import ru.pda.chemistry.service.ServiceFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * User: 1
 * Date: 09.05.2010
 * Time: 2:26:12
 */
public class EnumValueDeleteServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// todo: nice validation and on error forwarding
		Integer enumValueId = Integer.parseInt(request.getParameter("enumValueId"));

		try
		{
			ConstructorService service = ServiceFactory.getConstructorService();
			Integer attributeId = service.getEnumValue(enumValueId).getAttribute().getId();
			service.deleteEnumValue(enumValueId);

			request.getRequestDispatcher("attributeEdit?attributeId=" + attributeId.toString()).forward(request, response);
		}
		catch (SQLException e)
		{
			throw new ServletException(e);
		}
	}
}