<%@ page import="java.io.*,java.util.*"%>
<html>
<head>
<title></title>
</head>
<body>
	<center>
		<h1></h1>
	</center>
	<%
	    // New location to be redirected
	    String url = request.getContextPath() + "/jsp/home.jsp";
	    response.sendRedirect(url);
	%>
</body>
</html>
