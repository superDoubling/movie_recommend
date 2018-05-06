<%@ page language="java" import="java.util.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>My JSP 'index.jsp' starting page</title>
 <% String path = request.getContextPath();%>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
</head>

<body>
	<h2>Hello World!</h2>
	<form action="<%=path%>/user/save" method="get">
		<input id="name" name="name" value="1张三" /><br /> <input id="password"
			name="password" value="123456" /><br /> <input type="submit"
			value="提交" />
	</form>
</body>
</html>
