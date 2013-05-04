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
 * Date: 08.05.2010
 * Time: 22:28:21
 */
public class EntityEditServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// todo: if get entityId fails, redirect to sections list
		Integer entityId = Integer.parseInt(request.getParameter("entityId"));

		try
		{
			ConstructorService service = ServiceFactory.getConstructorService();
			request.setAttribute("entity", service.getEntity(entityId));
			request.setAttribute("attributes", service.getAttributes(entityId));

			request.getRequestDispatcher("constructor/entityEdit.jsp").forward(request, response);
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