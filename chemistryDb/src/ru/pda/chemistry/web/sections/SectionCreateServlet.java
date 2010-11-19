package ru.pda.chemistry.web.sections;

import ru.pda.chemistry.service.ConstructorService;
import ru.pda.chemistry.service.ServiceFactory;
import ru.pda.chemistry.service.NotUniqueException;
import ru.pda.chemistry.entities.Section;
import ru.pda.chemistry.util.ValidateUtils;
import static ru.pda.chemistry.util.StringUtils.getEncodedString;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * User: 1
 * Date: 05.05.2010
 * Time: 23:39:56
 */
public class SectionCreateServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("constructor/sectionCreate.jsp").forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sectionNameStr = request.getParameter("sectionName");
		String sectionName = ValidateUtils.validateString(sectionNameStr, request, "sectionNameEmpty");

		if (sectionName == null)
		{ // error in validation
			request.setAttribute("sectionNameStr", getEncodedString(sectionNameStr));
			request.getRequestDispatcher("constructor/sectionCreate.jsp").forward(request, response);
			return;
		}

		try
		{
			ConstructorService service = ServiceFactory.getConstructorService();

			try
			{
				Section section = new Section();
				section.setName(getEncodedString(sectionName));
				service.createSection(section);
			}
			catch (NotUniqueException e)
			{
				request.setAttribute("sectionNameExists", true);
				request.setAttribute("sectionNameStr", getEncodedString(sectionNameStr));
				request.getRequestDispatcher("constructor/sectionCreate.jsp").forward(request, response);
				return;
			}

			response.sendRedirect("sectionsList");
		}
		catch (SQLException e)
		{
			throw new ServletException(e);
		}
	}
}
