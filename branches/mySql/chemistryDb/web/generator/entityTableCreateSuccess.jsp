<%@ page import="ru.pda.chemistry.entities.Entity" %>
<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>���������� ������������ �������</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/main.css"/>
</head>
<body>
<div id="ruler"></div>
<div id="all">
	<div id="content">
<%
	Entity entity = (Entity) request.getAttribute("entity");
%>
		<h4>���������� &laquo;<%=entity.getName()%>&raquo; ������� ������������</h4>
		<p><a href="<%=request.getContextPath()%>/entityValuesEdit?entityId=<%=entity.getId()%>">������� � �������������� ������ ����������� &laquo;<%=entity.getName()%>&raquo;</a></p>
		<p><a href="<%=request.getContextPath()%>/entityEdit?entityId=<%=entity.getId()%>">��������� � ������ ��������� ����������� &laquo;<%=entity.getName()%>&raquo;</a></p>
	</div>
</div>
</body>
</html>