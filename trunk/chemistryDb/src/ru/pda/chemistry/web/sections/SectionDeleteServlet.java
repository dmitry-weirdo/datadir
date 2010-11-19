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
 * Date: 06.05.2010
 * Time: 0:40:15
 */
public class SectionDeleteServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// todo: nice validation and on error forwarding

		Integer id = Integer.parseInt(request.getParameter("sectionId"));

		try
		{
			ConstructorService service = ServiceFactory.getConstructorService();
			service.deleteSection(id);
		}
		catch(SQLException e)
		{
			throw new ServletException(e);
		}

		response.sendRedirect("sectionsList");
	}
}
