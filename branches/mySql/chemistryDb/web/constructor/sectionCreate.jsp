<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>Создание раздела химии</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/main.css"/>
</head>
<body>
<div id="ruler"></div>
<div id="all">
	<div id="content">
		<h4>Создание раздела химии</h4>
<% if (request.getAttribute("sectionNameEmpty") != null) { %><p class="error">* Не указано название раздела</p><% } %>
<% if (request.getAttribute("sectionNameExists") != null) { %><p class="error">* Раздел с названием &laquo;<%=request.getAttribute("sectionNameStr")%>&raquo; уже существует</p><% } %>
		<form action="<%=request.getContextPath()%>/sectionCreate" method="post">
			Название&nbsp;
			<input type="text" size="40" maxlength="40" name="sectionName" value="<%=request.getAttribute("sectionNameStr") == null? "": request.getAttribute("sectionNameStr")%>"/><br/><br/>
			<input type="submit" value="Создать"/>
			<input type="button" value="Отмена" onclick="location.replace('<%=request.getContextPath()%>/sectionsList')"/>
		</form>
	</div>
</div>
</body>
</html>