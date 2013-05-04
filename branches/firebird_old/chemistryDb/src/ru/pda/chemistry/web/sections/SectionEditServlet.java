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
 * Time: 21:18:10
 */
public class SectionEditServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// todo: if get sectionId fails, redirect to sections list
		Integer sectionId = Integer.parseInt(request.getParameter("sectionId"));

		try
		{
			ConstructorService service = ServiceFactory.getConstructorService();
			request.setAttribute("section", service.getSection(sectionId));
			request.setAttribute("entities", service.getEntities(sectionId));

			request.getRequestDispatcher("constructor/sectionEdit.jsp").forward(request, response);
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
