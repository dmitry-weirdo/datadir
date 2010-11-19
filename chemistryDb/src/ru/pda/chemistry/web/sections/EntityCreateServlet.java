package ru.pda.chemistry.web.sections;

import ru.pda.chemistry.util.ValidateUtils;
import static ru.pda.chemistry.util.StringUtils.getEncodedString;
import ru.pda.chemistry.service.ConstructorService;
import ru.pda.chemistry.service.ServiceFactory;
import ru.pda.chemistry.service.NotUniqueException;
import ru.pda.chemistry.service.ConstraintException;
import ru.pda.chemistry.entities.Section;
import ru.pda.chemistry.entities.Entity;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * User: 1
 * Date: 08.05.2010
 * Time: 21:35:29
 */
public class EntityCreateServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// todo: if get sectionId fails, redirect to sections list
		Integer sectionId = Integer.parseInt(request.getParameter("sectionId"));

		try
		{
			ConstructorService service = ServiceFactory.getConstructorService();
			request.setAttribute("section", service.getSection(sectionId));
			request.getRequestDispatcher("constructor/entityCreate.jsp").forward(request, response);
		}
		catch (SQLException e)
		{
			throw new ServletException(e);
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sectionIdStr = request.getParameter("sectionId");
		String entityNameStr = request.getParameter("entityName");

		Integer sectionId = ValidateUtils.validatePositiveInt(sectionIdStr, request, "sectionIdEmpty", "sectionIdError", "sectionIdError");
		String entityName = ValidateUtils.validateString(entityNameStr, request, "entityNameEmpty");

		ConstructorService service = ServiceFactory.getConstructorService();
		Section section;

		try
		{

			section = service.getSection(sectionId);
		}
		catch (SQLException e)
		{
			throw new ServletException(e);
		}


		if (sectionId == null || entityName == null)
		{ // error in validation
			request.setAttribute("section", section);
			request.setAttribute("entityNameStr", getEncodedString(entityNameStr));
			request.getRequestDispatcher("constructor/entityCreate.jsp").forward(request, response);
			return;
		}

		try
		{
			try
			{
				Entity entity = new Entity();
				entity.setName(getEncodedString(entityName));
				service.createEntity(sectionId, entity);

				request.getRequestDispatcher("sectionEdit?sectionId=" + sectionId.toString()).forward(request, response);
			}
			catch (NotUniqueException e)
			{
				request.setAttribute("entityNameExists", true);

				request.setAttribute("section", section);
				request.setAttribute("entityNameStr", getEncodedString(entityNameStr));
				request.getRequestDispatcher("constructor/entityCreate.jsp").forward(request, response);
			}
			catch (ConstraintException e)
			{
				throw new ServletException(e); // todo: think about this handling
			}
		}
		catch (SQLException e)
		{
			throw new ServletException(e);
		}
	}
}