package ru.pda.chemistry.web.generator;

import ru.pda.chemistry.service.ConstructorService;
import ru.pda.chemistry.service.ServiceFactory;
import ru.pda.chemistry.service.GeneratorService;
import ru.pda.chemistry.entities.Entity;
import ru.pda.chemistry.entities.Attribute;
import ru.pda.chemistry.entities.AttributeType;
import ru.pda.chemistry.entities.EntityValue;
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
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * User: 1
 * Date: 08.06.2010
 * Time: 2:01:29
 */
public class EntityValueEditServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer entityId = Integer.parseInt(request.getParameter("entityId"));
		Integer entityValueId = Integer.parseInt(request.getParameter("entityValueId"));

		try
		{
			ConstructorService service = ServiceFactory.getConstructorService();
			Entity entity = service.getEntity(entityId);
			List<Attribute> attributes = service.getAttributes(entityId);
			entity.setAttributes(attributes);

			// set enum values to request attributes
			StringBuffer sb = new StringBuffer();
			for (Attribute attribute : attributes)
				if (attribute.getType() == AttributeType.ENUM)
					request.setAttribute(getConcatenation(sb, "enum_", attribute.getId().toString()) , service.getEnumValues(attribute.getId()));


			GeneratorService generatorService = ServiceFactory.getGeneratorService();
			EntityValue entityValue = generatorService.getEntityValue(entity, entityValueId);

			// set attribute values
			request.setAttribute("source_str", entityValue.getSource());

			Object value;
			for (Attribute attribute : attributes)
			{
				value = entityValue.getAttributeValue(attribute.getName()).getValue();
				switch(attribute.getType())
				{
					case BOOLEAN:
						Boolean booleanValue = (Boolean) value;
						if (booleanValue != null)
						{
							if (booleanValue)
								request.setAttribute(getConcatenation(sb, "bool_", attribute.getId().toString(), "_str"), "true");
							else
								request.setAttribute(getConcatenation(sb, "bool_", attribute.getId().toString(), "_str"), "false");
						}

						break;

					case ENUM:
						Integer enumValue = (Integer) value;
						if (enumValue != null)
							request.setAttribute(getConcatenation(sb, "enum_", attribute.getId().toString(), "_str"), enumValue.toString());

						break;

					case DOUBLE:
						Double doubleValue = (Double) value;
						if (doubleValue != null)
							request.setAttribute(getConcatenation(sb, "double_", attribute.getId().toString(), "_str"), entityValue.getAttributeValue(attribute.getName()).getHtmlAttributeValue());

						break;

					case INTEGER:
						Long intValue = (Long) value;
						if (intValue != null)
							request.setAttribute(getConcatenation(sb, "int_", attribute.getId().toString(), "_str"), intValue.toString());						

						break;

					case STRING:
						String stringValue = (String) value;
						if (stringValue != null)
							request.setAttribute(getConcatenation(sb, "string_", attribute.getId().toString(), "_str"), stringValue);						

						break;
				}
			}

			request.setAttribute("entity", entity);
			request.setAttribute("entityValue", entityValue);
			request.getRequestDispatcher("generator/entityValueEdit.jsp").forward(request, response);
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

				Integer entityValueId = Integer.parseInt(request.getParameter("entityValueId"));
				GeneratorService generatorService = ServiceFactory.getGeneratorService();
				EntityValue entityValue = generatorService.getEntityValue(entity, entityValueId);

				request.setAttribute("entity", entity);
				request.setAttribute("entityValue", entityValue);
				request.getRequestDispatcher("generator/entityValueEdit.jsp").forward(request, response);
				return;
			}
		}
		catch (SQLException e)
		{
			throw new ServletException(e);
		}




		Integer entityId = Integer.parseInt(request.getParameter("entityId"));
		Integer entityValueId = Integer.parseInt(request.getParameter("entityValueId")); 
		try
		{
			GeneratorService service = ServiceFactory.getGeneratorService();

			Entity entity = constructorService.getEntity(entityId);

			// set nulls to attributes that are not present in attributes list
			List<Attribute> attributes = constructorService.getAttributes(entityId);
			entity.setAttributes(attributes);

			for (Attribute entityAttribute : attributes)
			{
				boolean present = false;
				for (AttributeValue attributeValue : attributeValues)
				{
					if (attributeValue.getAttribute().getId().equals(entityAttribute.getId()))
					{
						present = true;
						break;
					}
				}

				if (!present)
				{
					AttributeValue value = AttributeValueFactory.createAttributeValue(entityAttribute);
					value.setAttribute(entityAttribute);
					value.setValue(null);
					attributeValues.add(value);
				}
			}

			service.setSource(entity, entityValueId, getEncodedString(source));
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