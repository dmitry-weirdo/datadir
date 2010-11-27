<%@ page import="ru.pda.chemistry.entities.Attribute" %>
<%@ page import="ru.pda.chemistry.entities.Entity" %>
<%@ page import="ru.pda.chemistry.entities.EnumValue" %>
<%@ page import="java.util.List" %>
<%@ page import="static ru.pda.chemistry.util.StringUtils.getConcatenation" %>
<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>Создание записи в справочнике</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/main.css"/>
</head>
<body>
<div id="ruler"></div>
<div id="all">
	<div id="content">
<%
	Entity entity = (Entity) request.getAttribute("entity");
%>
		<h4>Создание записи в справочнике &laquo;<%=entity.getName()%>&raquo;</h4>
<% if (request.getAttribute("noAttributesSet") != null) { %><p class="error">* Не задано значение ни одного атрибута</p><% } %>
<%
	if (request.getAttribute("errors") != null)
	{
		@SuppressWarnings("unchecked")
		List<String> errors = (List<String>) request.getAttribute("errors");
		for (String error : errors)
		{

%>
			<p class="error"><%=error%></p>
<%
		}
	}
%>
		<form action="<%=request.getContextPath()%>/entityValueCreate" method="post">
			<input type="hidden" name="entityId" value="<%=entity.getId()%>"/>
			<p>
				Источник&nbsp;<input type="text" name="source" size="100" maxlength="255" <% if (request.getAttribute("source_str") != null) { %>value="<%=request.getAttribute("source_str")%>"<% } %>/>
			</p>
			<div class="separator">&nbsp;</div>
<%
	StringBuffer sb = new StringBuffer();
	String attributeValue;

	for (Attribute attribute : entity.getAttributes())
	{
		switch (attribute.getType())
		{
			case BOOLEAN:
				attributeValue = (String) request.getAttribute(getConcatenation(sb, "bool_", attribute.getId().toString(), "_str"));
%>
			<p>
				<%=attribute.getName()%>&nbsp;
				<input type="radio" name="<%=attribute.getColumnName()%>" value="true" id="<%=attribute.getColumnName()%>_true" <% if (attributeValue != null && attributeValue.equalsIgnoreCase("true")) { %> checked="checked" <% } %>/>&nbsp;
				<label for="<%=attribute.getColumnName()%>_true">Да</label>&nbsp;&nbsp;
				<input type="radio" name="<%=attribute.getColumnName()%>" value="false" id="<%=attribute.getColumnName()%>_false" <% if (attributeValue != null && attributeValue.equalsIgnoreCase("false")) { %> checked="checked" <% } %>/>&nbsp;
				<label for="<%=attribute.getColumnName()%>_false">Нет</label>
			</p>
<%
				break;
			case ENUM:
				attributeValue = (String) request.getAttribute(getConcatenation(sb, "enum_", attribute.getId().toString(), "_str"));

				@SuppressWarnings("unchecked")
				List<EnumValue> enumValues = (List<EnumValue>) request.getAttribute(getConcatenation(sb, "enum_", attribute.getId().toString()));
%>
			<p>
				<%=attribute.getName()%>&nbsp;
				<select name="enum_<%=attribute.getId()%>">
					<option value="-1">Не выбрано</option>		
<%
				for (EnumValue enumValue : enumValues)
				{
%>
					<option value="<%=enumValue.getId()%>"<% if (attributeValue != null && attributeValue.equals(enumValue.getId().toString())) { %> selected="selected"<% } %>><%=enumValue.getName()%></option>
<%
				}
%>
				</select>
			</p>
<%
				break;
			case DOUBLE:
				attributeValue = (String) request.getAttribute(getConcatenation(sb, "double_", attribute.getId().toString(), "_str"));
%>
			<p>
				<%=attribute.getName()%>&nbsp;
				<input type="text" name="double_<%=attribute.getId()%>" size="10" maxlength="10" <% if (attributeValue != null ) { %>value="<%=attributeValue%>" <% } %>/>
			</p>
<%
				break;
			case INTEGER:
				attributeValue = (String) request.getAttribute(getConcatenation(sb, "int_", attribute.getId().toString(), "_str"));
%>
			<p>
				<%=attribute.getName()%>&nbsp;
				<input type="text" name="int_<%=attribute.getId()%>" size="10" maxlength="10" <% if (attributeValue != null ) { %>value="<%=attributeValue%>" <% } %>/>
			</p>
<%
				break;
			case STRING:
				attributeValue = (String) request.getAttribute(getConcatenation(sb, "string_", attribute.getId().toString(), "_str"));
%>
			<p>
				<%=attribute.getName()%>&nbsp;
				<input type="text" name="string_<%=attribute.getId()%>" size="100" maxlength="255" <% if (attributeValue != null ) { %>value="<%=attributeValue%>" <% } %>/>
			</p>
<%
				break;
		}
%>
<%
	}
%>
			<input type="submit" value="Создать"/>
			<input type="button" value="Отмена" onclick="location.replace('<%=request.getContextPath()%>/entityValuesEdit?entityId=<%=entity.getId()%>')"/>
		</form>
	</div>
</div>
</body>
</html>