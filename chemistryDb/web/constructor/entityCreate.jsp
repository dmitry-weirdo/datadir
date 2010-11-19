<%@ page import="ru.pda.chemistry.entities.Section" %>
<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>�������� ������� �����</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/main.css"/>
</head>
<body>
<div id="ruler"></div>
<div id="all">
	<div id="content">
<%
	Section section = (Section) request.getAttribute("section");
%>
		<h4>�������� ����������� � ������� &laquo;<%=section.getName()%>&raquo;</h4>
<% if (request.getAttribute("entityNameEmpty") != null) { %><p class="error">* �� ������� �������� �����������</p><% } %>
<% if (request.getAttribute("entityNameExists") != null) { %><p class="error">* ���������� � ��������� &laquo;<%=request.getAttribute("entityNameStr")%>&raquo; ��� ����������</p><% } %>
		<form action="<%=request.getContextPath()%>/entityCreate" method="post">
			��������&nbsp;
			<input type="hidden" name="sectionId" value="<%=section.getId()%>"/>
			<input type="text" size="40" maxlength="40" name="entityName" value="<%=request.getAttribute("entityNameStr") == null? "": request.getAttribute("entityNameStr")%>"/><br/><br/>
			<input type="submit" value="�������"/>
			<input type="button" value="������" onclick="location.replace('<%=request.getContextPath()%>/sectionEdit?sectionId=<%=section.getId()%>')"/>
		</form>
	</div>
</div>
</body>
</html>