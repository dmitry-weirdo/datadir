package ru.pda.chemistry.web.sections;

import ru.pda.chemistry.service.ConstructorService;
import ru.pda.chemistry.service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * User: 1
 * Date: 08.05.2010
 * Time: 22:16:07
 */
public class EntityDeleteServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// todo: nice validation and on error forwarding
		Integer entityId = Integer.parseInt(request.getParameter("entityId"));

		try
		{
			ConstructorService service = ServiceFactory.getConstructorService();
			Integer sectionId = service.getEntity(entityId).getSection().getId();
			service.deleteEntity(entityId);

			request.getRequestDispatcher("sectionEdit?sectionId=" + sectionId.toString()).forward(request, response);
		}
		catch(SQLException e)
		{
			throw new ServletException(e);
		}
	}
}