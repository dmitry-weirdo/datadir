<%@ page import="ru.pda.chemistry.entities.Entity" %>
<%@ page import="ru.pda.chemistry.entities.Attribute" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.pda.chemistry.entities.EntityValue" %>
<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>Справочник</title>
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
		<h4>Справочник &laquo;<%=entity.getName()%>&raquo;</h4>
		<table cellpadding="0" cellspacing="0">
			<tr>
				<th>Источник</th>
<%
		List<Attribute> attributes = entity.getAttributes();
		for (Attribute attribute : attributes)
		{
%>
				<th><%=attribute.getName()%></th>
<%
		}
%>
				<th colspan="2">&nbsp;</th>
			</tr>
<%
	@SuppressWarnings("unchecked")
	List<EntityValue> values = (List<EntityValue>) request.getAttribute("entityValues");

	if (values.isEmpty())
	{
%>
			<tr>
				<td class="empty" colspan="<%= attributes.size() + 3 %>">В справочнике нет записей</td>
			</tr>
<%
	}
	else
	{
		for (EntityValue value : values)
		{
%>
			<tr>
				<td class="title"><%=value.getSource()%></td>
<%
			for (Attribute attribute : attributes)
			{
%>
				<td class="title"><%=value.getAttributeValue(attribute.getName()).getHtmlAttributeValue()%></td>
<%
			}
%>
				<td class="button"><a href="<%=request.getContextPath()%>/entityValueEdit?entityValueId=<%=value.getId()%>&amp;entityId=<%=entity.getId()%>"><span class="edit">&nbsp;</span></a></td>
				<td class="button"><a href="#" onclick="showDeleteForm(<%=value.getId()%>)"><span class="delete">&nbsp;</span></a></td>
			</tr>
<%
		}
%>
<%
	}
%>
			<tr>
				<td class="title" colspan="<%= attributes.size() + 1%>">&nbsp;</td>
				<td class="button" colspan="2"><a href="<%=request.getContextPath()%>/entityValueCreate?entityId=<%=entity.getId()%>" class="add"><span class="add">&nbsp;</span></a></td>
			</tr>
		</table>
		<p><a href="<%=request.getContextPath()%>/entityEdit?entityId=<%=entity.getId()%>">Вернуться к списку атрибутов справочника</a></p>
	</div>
</div>

<div id="hiddenForm" class="frm">
	<div class="msg">
		<input type="hidden" value="" id="entityValueId"/>
		<div class="confirm">Вы действительно хотите удалить запись?</div>
		<div class="buttons">
			<input type="button" onclick="location.replace('<%=request.getContextPath()%>/entityValueDelete?entityValueId=' + document.getElementById('entityValueId').value + '&amp;entityId=<%=entity.getId()%>');" value="Да"/>&nbsp;
			<input type="button" onclick="hideForm('hiddenForm')" value="Нет"/>
		</div>
	</div>
</div>

<script type="text/javascript">
	function showDeleteForm(id) {
		document.getElementById('entityValueId').value = id;
		showForm('hiddenForm');
	}
</script>
</body>
</html>