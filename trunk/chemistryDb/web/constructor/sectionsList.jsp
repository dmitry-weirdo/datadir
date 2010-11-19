<%@ page import="ru.pda.chemistry.entities.Section" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>Список разделов химии</title>
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
		<h4>Разделы химии</h4>
		<table cellpadding="0" cellspacing="0">
<%
	@SuppressWarnings("unchecked")
	List<Section> sections = (List<Section>) request.getAttribute("sections");

	if (sections.isEmpty())
	{
%>
			<tr>
				<td class="empty" colspan="3">Не создано ни одного раздела химии</td>
			</tr>
<%
	}
	else
	{
		for (Section section : sections)
		{
%>
			<tr>
				<td class="title"><%=section.getName()%></td>
				<td class="button"><a href="<%=request.getContextPath()%>/sectionEdit?sectionId=<%=section.getId()%>"><span class="edit">&nbsp;</span></a></td>
				<td class="button"><a href="#" onclick="showDeleteForm(<%=section.getId()%>)"><span class="delete">&nbsp;</span></a></td>
			</tr>
<%
		}
	}
%>
			<tr>
				<td class="title">&nbsp;</td>
				<td class="button" colspan="2"><a href="<%=request.getContextPath()%>/sectionCreate" class="add"><span class="add">&nbsp;</span></a></td>
			</tr>
		</table>
		<p><a href="<%=request.getContextPath()%>/index.jsp">Вернуться на главную</a></p>
	</div>
</div>

<div id="hiddenForm" class="frm">
	<div class="msg">
		<input type="hidden" value="" id="sectionId"/>
		<div class="warn">При удалении раздела будут удалены все справочники внутри раздела!</div>
		<div class="confirm">Вы действительно хотите удалить раздел?</div>
		<div class="buttons">
			<input type="button" onclick="location.replace('<%=request.getContextPath()%>/sectionDelete?sectionId=' + document.getElementById('sectionId').value)" value="Да"/>&nbsp;
			<input type="button" onclick="hideForm('hiddenForm')" value="Нет"/>
		</div>
	</div>
</div>

<script type="text/javascript">
	function showDeleteForm(id) {
		document.getElementById('sectionId').value = id;
		showForm('hiddenForm');
	}
</script>
</body>
</html>