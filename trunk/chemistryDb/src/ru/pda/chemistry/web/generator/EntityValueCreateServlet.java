package ru.pda.chemistry.web.generator;

import ru.pda.chemistry.service.ConstructorService;
import ru.pda.chemistry.service.ServiceFactory;
import ru.pda.chemistry.service.GeneratorService;
import ru.pda.chemistry.entities.Entity;
import ru.pda.chemistry.entities.Attribute;
import ru.pda.chemistry.entities.AttributeType;
import ru.pda.chemistry.entities.attributeValue.*;
import static ru.pda.chemistry.util.StringUtils.getConcatenation;
import static ru.pda.chemistry.util.StringUtils.getEncodedString;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.Enumeration;
import java.util.ArrayList;

/**
 * User: 1
 * Date: 08.06.2010
 * Time: 1:55:47
 */
public class EntityValueCreateServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer entityId = Integer.parseInt(request.getParameter("entityId"));

		try
		{
			// set entity attributes to request attributes
			ConstructorService service = ServiceFactory.getConstructorService();
			Entity entity = service.getEntity(entityId);
			List<Attribute> attributes = service.getAttributes(entityId);
			entity.setAttributes(attributes);

			// set enum values to request attributes
			StringBuffer sb = new StringBuffer();
			for (Attribute attribute : attributes)
				if (attribute.getType() == AttributeType.ENUM)
					request.setAttribute(getConcatenation(sb, "enum_", attribute.getId().toString()) , service.getEnumValues(attribute.getId()));

			request.setAttribute("entity", entity);
			request.getRequestDispatcher("generator/entityValueCreate.jsp").forward(request, response);
		}
		catch (SQLException e)
		{
			throw new ServletException(e);
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();
		List<String> errors = new ArrayList<String>();
		ConstructorService constructorService = ServiceFactory.getConstructorService();
		String source;

		Attribute attribute = null;
		StringBuffer sb = new StringBuffer();

		try
		{
			// check source
			source = request.getParameter("source");
			if (source == null || source.isEmpty())
				errors.add(getConcatenation(sb, "* Не указан источник"));

			Enumeration parameterNames = request.getParameterNames();
			while (parameterNames.hasMoreElements())
			{
				String paramName = (String) parameterNames.nextElement();
				String paramValue = request.getParameter(paramName);

				if (paramName.startsWith("bool_") || paramName.startsWith("int_") || paramName.startsWith("double_") || paramName.startsWith("string_") || paramName.startsWith("enum_"))
					attribute = constructorService.getAttribute(getAttributeId(paramName));

				// todo: merge into one if

				if (paramName.startsWith("bool_"))
				{
					Boolean value = validateBoolean(paramValue);
					if (value != null)
					{
						AttributeValue attributeValue = new BooleanAttributeValue();
						attributeValue.setValue(value);
						attributeValue.setAttribute(attribute);
						attributeValues.add(attributeValue);
					}
					else if (paramValue != null && !paramValue.isEmpty())
					{
						errors.add(getConcatenation(sb, "* Неверно задано значение атрибута &laquo;", attribute.getName(), "&raquo;"));
					}
				}
				else if (paramName.startsWith("int_"))
				{
					Long value = validateInt(paramValue);
					if (value != null)
					{
						AttributeValue attributeValue = new IntegerAttributeValue();
						attributeValue.setValue(value);
						attributeValue.setAttribute(attribute);
						attributeValues.add(attributeValue);
					}
					else if (paramValue != null && !paramValue.isEmpty())
					{
						errors.add(getConcatenation(sb, "* Неверно задано значение атрибута &laquo;", attribute.getName(), "&raquo;"));
					}
				}
				else if (paramName.startsWith("double_"))
				{
					Double value = validateDouble(paramValue);
					if (value != null)
					{
						AttributeValue attributeValue = new DoubleAttributeValue();
						attributeValue.setValue(value);
						attributeValue.setAttribute(attribute);
						attributeValues.add(attributeValue);
					}
					else if (paramValue != null && !paramValue.isEmpty())
					{
						errors.add(getConcatenation(sb, "* Неверно задано значение атрибута &laquo;", attribute.getName(), "&raquo;"));
					}
				}
				else if (paramName.startsWith("string_"))
				{
					String value = validateString(paramValue);
					if (value != null)
					{
						AttributeValue attributeValue = new StringAttributeValue();
						attributeValue.setValue(value);
						attributeValue.setAttribute(attribute);
						attributeValues.add(attributeValue);
					}
					else
					{
						// string is null or empty, nothing is set
					}
				}
				else if (paramName.startsWith("enum_"))
				{
					Integer value = validateEnum(paramValue);
					if (value != null)
					{
						AttributeValue attributeValue = new EnumAttributeValue();
						attributeValue.setValue(value);
						attributeValue.setAttribute(attribute);
						attributeValues.add(attributeValue);
					}
					else if (paramValue != null && !paramValue.isEmpty() && !paramValue.equals("-1"))
					{
						errors.add(getConcatenation(sb, "* Неверно задано значение атрибута &laquo;", attribute.getName(), "&raquo;"));
					}
				}
			}


			boolean back = false;
			if (!errors.isEmpty())
			{
				request.setAttribute("errors", errors);
				back = true;
			}
			if (attributeValues.isEmpty())
			{
				request.setAttribute("noAttributesSet", true);
				back = true;
			}


			if (back)
			{
				// save all param values
				parameterNames = request.getParameterNames();
				while (parameterNames.hasMoreElements())
				{
					String paramName = (String) parameterNames.nextElement();
					String paramValue = request.getParameter(paramName);

					if (paramValue != null)
						request.setAttribute(getConcatenation(sb, paramName, "_str"), getEncodedString(paramValue));
				}


				Integer entityId = Integer.parseInt(request.getParameter("entityId"));
				Entity entity = constructorService.getEntity(entityId);
				List<Attribute> attributes = constructorService.getAttributes(entityId);
				entity.setAttributes(attributes);

				sb.delete(0, sb.length());
				for (Attribute entityAttribute : attributes)
					if (entityAttribute.getType() == AttributeType.ENUM)
						request.setAttribute(getConcatenation(sb, "enum_", entityAttribute.getId().toString()) , constructorService.getEnumValues(entityAttribute.getId()));

				request.setAttribute("entity", entity);
				request.getRequestDispatcher("generator/entityValueCreate.jsp").forward(request, response);
				return;
			}
		}
		catch (SQLException e)
		{
			throw new ServletException(e);
		}


		Integer entityId = Integer.parseInt(request.getParameter("entityId"));
		try
		{
			Entity entity = constructorService.getEntity(entityId);

			GeneratorService service = ServiceFactory.getGeneratorService();
			Integer entityValueId = service.createEntityValue(entity, getEncodedString(source));
			service.setAttributeValues(entityValueId, attributeValues);

			request.getRequestDispatcher("entityValuesEdit?entityId=" + entityId.toString()).forward(request, response);
		}
		catch (SQLException e)
		{
			throw new ServletException(e);
		}
	}

	private Integer getAttributeId(String paramName) {
		return Integer.parseInt(paramName.substring(paramName.indexOf("_") + "_".length(), paramName.length()));
	}

	private Boolean validateBoolean(String param) {
		if (param == null || param.length() == 0)
			return null;

		return Boolean.parseBoolean(param); // if String is not 'true', always returns false 
	}
	private Long validateInt(String param) {
		try
		{
			return Long.parseLong(param.trim());
		}
		catch (NumberFormatException e)
		{
			return null; // returns if null string, empty string, or contain any non-digits
		}
	}
	private Double validateDouble(String param) {
		if (param == null || param.length() == 0)
			return null;

		try
		{
			return Double.parseDouble(param.replace(',', '.'));
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}
	private String validateString(String param) throws UnsupportedEncodingException {
		if (param == null || param.trim().isEmpty())
			return null;

		return getEncodedString(param.trim());
	}
	private Integer validateEnum(String param) {
		try
		{
			Integer value = Integer.parseInt(param);

			if (value == -1)
				return null;

			return value;
		}
		catch (NumberFormatException e)
		{
			return null; // returns if null string, empty string, or contain any non-digits
		}
	}
}