<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>REST Cte</title>
	<link href="./estilo.css" rel="stylesheet" type="text/css">
	<style>
	form#idFmRegresar{
		margin-top: 10px;
	}
	</style>
</head>
<body>
	<%@include file="encabezado.html" %>
	<div id="cajaPrincipal">
	<p>
	<h3>Datos de Art&iacute;culo</h3>
	<label for="idClave">Clave:</label>
	<input type="text" readonly="readonly" value="${articulo.clave}" id="idClave" class="campoCorto"><br>
	<label for="idNombre">Nombre:</label>
	<input type="text" readonly="readonly" value="${articulo.nombre}" id="idNombre" class="campoLargo"><br>
	<label for="idDesc">Descripci&oacute;n:</label>
	<input type="text" readonly="readonly" value="${articulo.descripcion}" id="idDesc" class="campoLargo"><br>
	<label for="idCosto">Costo unitario:</label>
	<input type="text" readonly="readonly" value="${articulo.costo}" id="idCosto" class="campoCorto"><br>
	<label for="idPrecio">Precio unitario:</label>
	<input type="text" readonly="readonly" value="${articulo.precio}" id="idPrecio" class="campoCorto"><br>
	<label for="idExist">Existencia:</label>
	<input type="text" readonly="readonly" value="${articulo.existencia}" id="idExist" class="campoCorto"><br>
	<form action="TestRest" method="post" id="idFmRegresar">
 	    <input type="hidden" name="vista" value="salida.jsp">
	    <input type="hidden" name="operacion" value="regresar">
		<input type="submit" value="Regresar">
	</form>
	<hr>
	<p>
	<span id="estado">${articulo}</span>
	</div>
</body>
</html>