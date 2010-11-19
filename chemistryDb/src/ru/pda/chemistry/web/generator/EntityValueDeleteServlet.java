package ru.pda.chemistry.web.generator;

import ru.pda.chemistry.service.ConstructorService;
import ru.pda.chemistry.service.ServiceFactory;
import ru.pda.chemistry.service.GeneratorService;
import ru.pda.chemistry.entities.Entity;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * User: 1
 * Date: 08.06.2010
 * Time: 2:02:01
 */
public class EntityValueDeleteServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// todo: nice validation and on error forwarding
		Integer entityId = Integer.parseInt(request.getParameter("entityId"));
		Integer entityValueId = Integer.parseInt(request.getParameter("entityValueId"));

		try
		{
			ConstructorService constructorService = ServiceFactory.getConstructorService();
			GeneratorService service = ServiceFactory.getGeneratorService();
			service.deleteEntityValue(constructorService.getEntity(entityId), entityValueId);

			request.getRequestDispatcher("entityValuesEdit?entityId=" + entityId.toString()).forward(request, response);
		}
		catch(SQLException e)
		{
			throw new ServletException(e);
		}
	}
}