<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Inicio</title>
</head>
<body> <%
          RequestDispatcher dispatcher = 
          				application.getRequestDispatcher("/TestRest?vista=inicio.jsp");
		  dispatcher.forward(request, response);
		%>
</body>
</html>