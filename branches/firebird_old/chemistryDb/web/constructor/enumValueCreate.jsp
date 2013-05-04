<%@ page import="ru.pda.chemistry.entities.Attribute" %>
<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>�������� �������� �������� �����������</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/main.css"/>
</head>
<body>
<div id="ruler"></div>
<div id="all">
	<div id="content">
<%
	Attribute attribute = (Attribute) request.getAttribute("attribute");
%>
		<h4>�������� �������� �������� &laquo;<%=attribute.getName()%>&raquo; � ����������� &laquo;<%=attribute.getEntity().getName()%>&raquo;</h4>
<% if (request.getAttribute("enumValueNameEmpty") != null) { %><p class="error">* �� ������� ��������</p><% } %>
<% if (request.getAttribute("enumValueNameExists") != null) { %><p class="error">* �������� &laquo;<%=request.getAttribute("enumValueNameStr")%>&raquo; �������� &laquo;<%=attribute.getName()%>&raquo; ��� ����������</p><% } %>
		<form action="<%=request.getContextPath()%>/enumValueCreate" method="post">
			<input type="hidden" name="attributeId" value="<%=attribute.getId()%>"/>
			��������&nbsp;
			<input type="text" size="40" maxlength="40" name="enumValueName" value="<%=request.getAttribute("enumValueNameStr") == null? "": request.getAttribute("enumValueNameStr")%>"/><br/><br/>

			<input type="submit" value="�������"/>
			<input type="button" value="������" onclick="location.replace('<%=request.getContextPath()%>/attributeEdit?attributeId=<%=attribute.getId()%>')"/>
		</form>
	</div>
</div>
</body>
</html>