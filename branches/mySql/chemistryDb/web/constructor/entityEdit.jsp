<%@ page import="ru.pda.chemistry.entities.Attribute" %>
<%@ page import="ru.pda.chemistry.entities.AttributeType" %>
<%@ page import="ru.pda.chemistry.entities.Entity" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>������ ���������</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/main.css"/>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/popup.js"></script>
	<!--[if IE 6]>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/main-ie.css"/>
	<![endif]-->
</head>
<body>
<div id="ruler"></div>
<div id="all">
	<div id="content">
<%
	Entity entity = (Entity) request.getAttribute("entity");
%>
		<h4>�������� ����������� &laquo;<%=entity.getName()%>&raquo;</h4>
		<table cellpadding="0" cellspacing="0">
			<tr>
				<th>��������</th>
				<th>���</th>
				<th>������� ���������</th>
<%
	if (entity.isEditable())
	{
%>
				<th colspan="2">&nbsp;</th>
<%
	}
%>
			</tr>
<%
	@SuppressWarnings("unchecked")
	List<Attribute> attributes = (List<Attribute>) request.getAttribute("attributes");

	if (attributes.isEmpty())
	{
%>
			<tr>
				<td class="empty" colspan="5">� ����������� ��� ���������</td>
			</tr>
<%
	}
	else
	{
		for (Attribute attribute : attributes)
		{
%>
			<tr>
				<td class="title"><%=attribute.getName()%></td>
				<td class="title"><%=attribute.getTypeStr()%></td>
				<td class="title"><%=attribute.getMeasureUnit()%></td>
<%
			if (entity.isEditable())
			{
				if (attribute.getType() == AttributeType.ENUM)
				{
%>
				<td class="button"><a href="<%=request.getContextPath()%>/attributeEdit?attributeId=<%=attribute.getId()%>"><span class="edit">&nbsp;</span></a></td>
<%
				}
				else
				{
%>
				<td class="button"><span class="empty">&nbsp;</span></td>
<%
				}
%>
				<td class="button"><a href="#" onclick="showDeleteForm(<%=attribute.getId()%>)"><span class="delete">&nbsp;</span></a></td>
			</tr>
<%
			}
		}
	}
%>
<%
	if (entity.isEditable())
	{
%>
			<tr>
				<td class="title" colspan="3">&nbsp;</td>
				<td class="button" colspan="2"><a href="<%=request.getContextPath()%>/attributeCreate?entityId=<%=entity.getId()%>" class="add"><span class="add">&nbsp;</span></a></td>
<%
	}
%>
			</tr>
		</table>
<%
	if (entity.isEditable())
	{
		if (attributes.isEmpty())
		{
%>
		<p><input type="button" value="������������ ����������" disabled="disabled"/></p>
<%
		}
		else
		{
%>
		<p><input type="button" value="������������ ����������" onclick="location.replace('<%=request.getContextPath()%>/entityTableCreate?entityId=<%=entity.getId()%>');"/></p>
<%
		}
	}
	else
	{
%>
		<p><input type="button" value="������� ������� �����������" onclick="showDeleteTableForm()"/></p>
		<p><a href="<%=request.getContextPath()%>/entityValuesEdit?entityId=<%=entity.getId()%>">������� � �����������</a></p>
<%
	}
%>
		<p><a href="<%=request.getContextPath()%>/sectionEdit?sectionId=<%=entity.getSection().getId()%>">��������� � ������ ������������ ������� &laquo;<%=entity.getSection().getName()%>&raquo;</a></p>
	</div>
</div>

<div id="hiddenForm" class="frm">
	<div class="msg">
		<input type="hidden" value="" id="attributeId"/>
		<div class="confirm">�� ������������� ������ ������� �������?</div>
		<div class="buttons">
			<input type="button" onclick="location.replace('<%=request.getContextPath()%>/attributeDelete?attributeId=' + document.getElementById('attributeId').value)" value="��"/>&nbsp;
			<input type="button" onclick="hideForm('hiddenForm')" value="���"/>
		</div>
	</div>
</div>

<div id="hiddenTableForm" class="frm">
	<div class="msg">
		<div class="warn">��� �������� ������� ����� ������� ��� ������ �� �����������!</div>
		<div class="confirm">�� ������������� ������ ������� ������� �����������?</div>
		<div class="buttons">
			<input type="button" onclick="location.replace('<%=request.getContextPath()%>/entityTableDelete?entityId=' + <%=entity.getId()%>)" value="��"/>&nbsp;
			<input type="button" onclick="hideForm('hiddenTableForm')" value="���"/>
		</div>
	</div>
</div>

<script type="text/javascript">
	function showDeleteForm(id) {
		document.getElementById('attributeId').value = id;
		showForm('hiddenForm');
	}

	function showDeleteTableForm() {
		showForm('hiddenTableForm');
	}
</script>
</body>
</html>