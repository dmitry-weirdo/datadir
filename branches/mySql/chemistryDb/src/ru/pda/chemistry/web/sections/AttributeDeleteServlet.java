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
 * Date: 09.05.2010
 * Time: 1:33:35
 */
public class AttributeDeleteServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// todo: nice validation and on error forwarding
		Integer attributeId = Integer.parseInt(request.getParameter("attributeId"));

		try
		{
			ConstructorService service = ServiceFactory.getConstructorService();
			Integer entityId = service.getAttribute(attributeId).getEntity().getId(); 
			service.deleteAttribute(attributeId);

			request.getRequestDispatcher("entityEdit?entityId=" + entityId.toString()).forward(request, response);
		}
		catch(SQLException e)
		{
			throw new ServletException(e);
		}
	}
}