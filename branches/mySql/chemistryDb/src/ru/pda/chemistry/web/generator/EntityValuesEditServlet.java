package ru.pda.chemistry.web.generator;

import ru.pda.chemistry.service.GeneratorService;
import ru.pda.chemistry.service.ServiceFactory;
import ru.pda.chemistry.service.ConstructorService;
import ru.pda.chemistry.entities.Entity;
import ru.pda.chemistry.entities.EntityValue;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * User: 1
 * Date: 07.06.2010
 * Time: 22:29:28
 */
public class EntityValuesEditServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// todo: if get entityId fails, redirect to entities list
		Integer entityId = Integer.parseInt(request.getParameter("entityId"));

		try
		{
			ConstructorService constructorService = ServiceFactory.getConstructorService();
			GeneratorService service = ServiceFactory.getGeneratorService();

			Entity entity = constructorService.getEntity(entityId);
			entity.setAttributes(constructorService.getAttributes(entityId));

			List<EntityValue> entityValues = service.getEntityValues(entity);

			request.setAttribute("entity", entity);
			request.setAttribute("entityValues", entityValues);
			request.getRequestDispatcher("generator/entityValuesEdit.jsp").forward(request, response);
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