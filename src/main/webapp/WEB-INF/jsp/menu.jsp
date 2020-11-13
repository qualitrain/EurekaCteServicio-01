<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Test REST</title>
	<link href="./estilo.css" rel="stylesheet" type="text/css">
	<style>
	form.btn{
		width: 140px;
		display: inline-block;
		margin-right: 10px;
	}
	</style>
</head>
<body>
	<%@include file="encabezado.html" %>
	<div id="cajaPrincipal">
    <form action="TestRest" method="post">
     	<label for="idCveArt">Clave:</label>
    	<select name="cveArt" id="idCveArt">
	    	<c:forEach var="par" items="${mapaArticulos}">
		    	<option value="${par.value}">${par.key}</option>
	    	</c:forEach>
	    </select>
	    <input type="hidden" name="vista" value="menu.jsp">
	    <input type="hidden" name="operacion" value="consulta">
	    <input type="submit" value="consultar">
    </form>
    <form action="TestRest" method="post" class="btn">
 	    <input type="hidden" name="vista" value="menu.jsp">
	    <input type="hidden" name="operacion" value="alta">
	    <input type="submit" value="Crear nuevo">
    </form>
    <form action="TestRest" method="post" class="btn">
 	    <input type="hidden" name="vista" value="menu.jsp">
	    <input type="hidden" name="operacion" value="carga">
	    <input type="submit" value="Cargar articulo json">
    </form>
    <form action="TestAsync" method="post" class="btn">
 	    <input type="hidden" name="vista" value="menu.jsp">
	    <input type="hidden" name="operacion" value="Async">
	    <input type="submit" value="Solicitud as&iacute;ncrona">
    </form>
    </div>
</body>
</html>
