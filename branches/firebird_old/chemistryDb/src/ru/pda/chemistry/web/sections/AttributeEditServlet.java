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
 * Time: 2:25:20
 */
public class AttributeEditServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// todo: if get attributeId fails, redirect to sections list
		Integer attributeId = Integer.parseInt(request.getParameter("attributeId"));

		try
		{
			ConstructorService service = ServiceFactory.getConstructorService();
			request.setAttribute("attribute", service.getAttribute(attributeId));
			request.setAttribute("enumValues", service.getEnumValues(attributeId));

			// todo: if attr is not enum, redirect to attributes list

			request.getRequestDispatcher("constructor/attributeEdit.jsp").forward(request, response);
		}
		catch (SQLException e)
		{
			throw new ServletException(e);
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}