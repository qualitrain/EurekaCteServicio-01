<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Json</title>
	<link href="./estilo.css" rel="stylesheet" type="text/css">
</head>
<body>
	<%@include file="encabezado.html" %>
	<div id="cajaPrincipal">
	<h3>Carga de art&iacute;culos</h3>
	<form action="TestRest" method="post">
 		<label for="idArticulos">Articulos (Json):</label>
		<textarea name="articulos" id="idArticulos" rows="10" cols="50">
		</textarea><br>
 	    <input type="hidden" name="vista" value="cargaJson.jsp">
	    <input type="hidden" name="operacion" value="cargaJson">
		<input type="submit" value="Cargar datos">
	</form>
	<form action="TestRest" method="post">
 	    <input type="hidden" name="vista" value="cargaJson.jsp">
	    <input type="hidden" name="operacion" value="regresar">
		<input type="submit" value="Regresar">
	</form>
	<hr>
	<p>
	<c:forEach var="respuestaI" items="${respuestasCarga}">
		<span class="estado clave">${respuestaI.key}</span>&nbsp;
		<span class="estado valor">${respuestaI.value}</span> <br>
	</c:forEach>
	</div>

</body>
</html>