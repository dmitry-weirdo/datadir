<%@ page import="ru.pda.chemistry.entities.Entity" %>
<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>�������� �������� �����������</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/main.css"/>
</head>
<body>
<div id="ruler"></div>
<div id="all">
	<div id="content">
<%
	Entity entity = (Entity) request.getAttribute("entity");
%>
		<h4>�������� �������� � ����������� &laquo;<%=entity.getName()%>&raquo;</h4>
<% if (request.getAttribute("attributeNameEmpty") != null) { %><p class="error">* �� ������� �������� ��������</p><% } %>
<% if (request.getAttribute("attributeNameExists") != null) { %><p class="error">* ������� � ��������� &laquo;<%=request.getAttribute("attributeNameStr")%>&raquo; � ����������� &laquo;<%=entity.getName()%>&raquo; ��� ����������</p><% } %>
<% if (request.getAttribute("attributeMeasureUnitEmpty") != null) { %><p class="error">* �� ������� ������� ��������� ��������</p><% } %>
<% if (request.getAttribute("attributeTypeEmpty") != null) { %><p class="error">* �� ������ ��� ��������</p><% } %>
		<form action="<%=request.getContextPath()%>/attributeCreate" method="post">
			<input type="hidden" name="entityId" value="<%=entity.getId()%>"/>
			��������&nbsp;
			<input type="text" size="40" maxlength="40" name="attributeName" value="<%=request.getAttribute("attributeNameStr") == null? "": request.getAttribute("attributeNameStr")%>"/><br/><br/>
			������� ���������&nbsp;
			<input type="text" size="40" maxlength="40" name="attributeMeasureUnit" value="<%=request.getAttribute("attributeMeasureUnitStr") == null? "": request.getAttribute("attributeMeasureUnitStr")%>"/><br/><br/>
			���&nbsp;
			<select name="attributeType">
<% if (request.getAttribute("attributeTypeStr") == null || request.getAttribute("attributeTypeStr").equals("-1")) { %><option value="-1" selected="selected">�� ������</option><% } %>
<% if (request.getAttribute("attributeTypeStr") != null && request.getAttribute("attributeTypeStr").equals("0")) { %><option value="0" selected="selected">�����</option><% } else { %><option value="0">�����</option><% } %>
<% if (request.getAttribute("attributeTypeStr") != null && request.getAttribute("attributeTypeStr").equals("1")) { %><option value="1" selected="selected">������������</option><% } else { %><option value="1">������������</option><% } %>
<% if (request.getAttribute("attributeTypeStr") != null && request.getAttribute("attributeTypeStr").equals("2")) { %><option value="2" selected="selected">������</option><% } else { %><option value="2">������</option><% } %>
<% if (request.getAttribute("attributeTypeStr") != null && request.getAttribute("attributeTypeStr").equals("3")) { %><option value="3" selected="selected">����������</option><% } else { %><option value="3">����������</option><% } %>
<% if (request.getAttribute("attributeTypeStr") != null && request.getAttribute("attributeTypeStr").equals("4")) { %><option value="4" selected="selected">���������</option><% } else { %><option value="4">���������</option><% } %>
			</select>
			<br/><br/>

			<input type="submit" value="�������"/>
			<input type="button" value="������" onclick="location.replace('<%=request.getContextPath()%>/entityEdit?entityId=<%=entity.getId()%>')"/>
		</form>
	</div>
</div>
</body>
</html>