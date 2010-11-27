<%@ page import="ru.pda.chemistry.entities.Entity" %>
<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>Создание атрибута справочника</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/main.css"/>
</head>
<body>
<div id="ruler"></div>
<div id="all">
	<div id="content">
<%
	Entity entity = (Entity) request.getAttribute("entity");
%>
		<h4>Создание атрибута в справочнике &laquo;<%=entity.getName()%>&raquo;</h4>
<% if (request.getAttribute("attributeNameEmpty") != null) { %><p class="error">* Не указано название атрибута</p><% } %>
<% if (request.getAttribute("attributeNameExists") != null) { %><p class="error">* Атрибут с названием &laquo;<%=request.getAttribute("attributeNameStr")%>&raquo; в справочнике &laquo;<%=entity.getName()%>&raquo; уже существует</p><% } %>
<% if (request.getAttribute("attributeMeasureUnitEmpty") != null) { %><p class="error">* Не указана единица измерения атрибута</p><% } %>
<% if (request.getAttribute("attributeTypeEmpty") != null) { %><p class="error">* Не указан тип атрибута</p><% } %>
		<form action="<%=request.getContextPath()%>/attributeCreate" method="post">
			<input type="hidden" name="entityId" value="<%=entity.getId()%>"/>
			Название&nbsp;
			<input type="text" size="40" maxlength="40" name="attributeName" value="<%=request.getAttribute("attributeNameStr") == null? "": request.getAttribute("attributeNameStr")%>"/><br/><br/>
			Единица измерения&nbsp;
			<input type="text" size="40" maxlength="40" name="attributeMeasureUnit" value="<%=request.getAttribute("attributeMeasureUnitStr") == null? "": request.getAttribute("attributeMeasureUnitStr")%>"/><br/><br/>
			Тип&nbsp;
			<select name="attributeType">
<% if (request.getAttribute("attributeTypeStr") == null || request.getAttribute("attributeTypeStr").equals("-1")) { %><option value="-1" selected="selected">Не выбран</option><% } %>
<% if (request.getAttribute("attributeTypeStr") != null && request.getAttribute("attributeTypeStr").equals("0")) { %><option value="0" selected="selected">Целый</option><% } else { %><option value="0">Целый</option><% } %>
<% if (request.getAttribute("attributeTypeStr") != null && request.getAttribute("attributeTypeStr").equals("1")) { %><option value="1" selected="selected">Вещественный</option><% } else { %><option value="1">Вещественный</option><% } %>
<% if (request.getAttribute("attributeTypeStr") != null && request.getAttribute("attributeTypeStr").equals("2")) { %><option value="2" selected="selected">Строка</option><% } else { %><option value="2">Строка</option><% } %>
<% if (request.getAttribute("attributeTypeStr") != null && request.getAttribute("attributeTypeStr").equals("3")) { %><option value="3" selected="selected">Логический</option><% } else { %><option value="3">Логический</option><% } %>
<% if (request.getAttribute("attributeTypeStr") != null && request.getAttribute("attributeTypeStr").equals("4")) { %><option value="4" selected="selected">Множество</option><% } else { %><option value="4">Множество</option><% } %>
			</select>
			<br/><br/>

			<input type="submit" value="Создать"/>
			<input type="button" value="Отмена" onclick="location.replace('<%=request.getContextPath()%>/entityEdit?entityId=<%=entity.getId()%>')"/>
		</form>
	</div>
</div>
</body>
</html>