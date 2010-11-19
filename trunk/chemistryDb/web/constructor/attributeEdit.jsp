<%@ page import="ru.pda.chemistry.entities.Attribute" %>
<%@ page import="ru.pda.chemistry.entities.EnumValue" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>Список атрибутов</title>
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
	Attribute attribute = (Attribute) request.getAttribute("attribute");
%>
		<h4>Значения атрибута &laquo;<%=attribute.getName()%>&raquo; справочника &laquo;<%=attribute.getEntity().getName()%>&raquo;</h4>
		<table cellpadding="0" cellspacing="0">
<%
	@SuppressWarnings("unchecked")
	List<EnumValue> enumValues = (List<EnumValue>) request.getAttribute("enumValues");

	if (enumValues.isEmpty())
	{
%>
			<tr>
				<td class="empty" colspan="2">У атрибута нет значений</td>
			</tr>
<%
	}
	else
	{
		for (EnumValue enumValue : enumValues)
		{
%>
			<tr>
				<td class="title"><%=enumValue.getName()%></td>
				<td class="button"><a href="#" onclick="showDeleteForm(<%=enumValue.getId()%>)"><span class="delete">&nbsp;</span></a></td>
			</tr>
<%
		}
	}
%>
			<tr>
				<td class="title">&nbsp;</td>
				<td class="button"><a href="<%=request.getContextPath()%>/enumValueCreate?attributeId=<%=attribute.getId()%>" class="add"><span class="add">&nbsp;</span></a></td>
			</tr>
		</table>
		<p><a href="<%=request.getContextPath()%>/entityEdit?entityId=<%=attribute.getEntity().getId()%>">Вернуться к списку атрибутов справочника &laquo;<%=attribute.getEntity().getName()%>&raquo;</a></p>
	</div>
</div>

<div id="hiddenForm" class="frm">
	<div class="msg">
		<input type="hidden" value="" id="enumValueId"/>
		<div class="confirm">Вы действительно хотите удалить значение атрибута?</div>
		<div class="buttons">
			<input type="button" onclick="location.replace('<%=request.getContextPath()%>/enumValueDelete?enumValueId=' + document.getElementById('enumValueId').value)" value="Да"/>&nbsp;
			<input type="button" onclick="hideForm('hiddenForm')" value="Нет"/>
		</div>
	</div>
</div>

<script type="text/javascript">
	function showDeleteForm(id) {
		document.getElementById('enumValueId').value = id;
		showForm('hiddenForm');
	}
</script>
</body>
</html>