<%@ page contentType="text/html;charset=windows-1251" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
  <title>Test page</title>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1251">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/main.css"/>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/popup.js"></script>
	<!--[if IE 6]>
	<link rel="stylesheet" type="text/css" href="../css/main-ie.css"/>
	<![endif]-->
</head>
<body>
<div id="ruler"></div>
<div id="all">
	<div id="content">
		<h4>Главная</h4>
		<p class="title"><a href="<%=request.getContextPath()%>/sectionsList">Конструктор справочников</a></p>
		<p class="title-disabled">Просмотр и редактирование справочников</p>
		<p class="title-disabled">Управление пользователями</p>
		<%-- todo: add other subsections --%>
	</div>
</div>
</body>
</html>