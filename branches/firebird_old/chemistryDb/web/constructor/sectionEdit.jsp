<%@ page import="ru.pda.chemistry.entities.Section" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.pda.chemistry.entities.Entity" %>
<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>Список справочников</title>
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
	Section section = (Section) request.getAttribute("section");
%>
		<h4>Справочники в разделе &laquo;<%=section.getName()%>&raquo;</h4>
		<table cellpadding="0" cellspacing="0">
<%
	@SuppressWarnings("unchecked")
	List<Entity> entities = (List<Entity>) request.getAttribute("entities");

	if (entities.isEmpty())
	{
%>
			<tr>
				<td class="empty" colspan="3">В разделе нет справочников</td>
			</tr>
<%
	}
	else
	{
		for (Entity entity : entities)
		{
%>
			<tr>
				<td class="title"><%=entity.getName()%></td>
				<%--<td class="button"><a href="<%=request.getContextPath()%>/sectionSave?sectionId=<%=section.getId()%>"><div class="save"></div></a></td>--%>
				<td class="button"><a href="<%=request.getContextPath()%>/entityEdit?entityId=<%=entity.getId()%>"><span class="edit">&nbsp;</span></a></td>
				<td class="button"><a href="#" onclick="showDeleteForm(<%=entity.getId()%>)"><span class="delete">&nbsp;</span></a></td>
			</tr>
<%
		}
	}
%>
			<tr>
				<td class="title">&nbsp;</td>
				<td class="button" colspan="2"><a href="<%=request.getContextPath()%>/entityCreate?sectionId=<%=section.getId()%>" class="add"><span class="add">&nbsp;</span></a></td>
			</tr>
		</table>
		<p><a href="<%=request.getContextPath()%>/sectionsList">Вернуться к списку разделов химии</a></p>
	</div>
</div>

<div id="hiddenForm" class="frm">
	<div class="msg">
		<input type="hidden" value="" id="entityId"/>
		<div class="warn">При удалении справочника будут удалены все атрибуты справочника!</div>
		<div class="confirm">Вы действительно хотите удалить справочник?</div>
		<div class="buttons">
			<input type="button" onclick="location.replace('<%=request.getContextPath()%>/entityDelete?entityId=' + document.getElementById('entityId').value)" value="Да"/>&nbsp;
			<input type="button" onclick="hideForm('hiddenForm')" value="Нет"/>
		</div>
	</div>
</div>

<script type="text/javascript">
	function showDeleteForm(id) {
		document.getElementById('entityId').value = id;
		showForm('hiddenForm');
	}
</script>
</body>
</html>