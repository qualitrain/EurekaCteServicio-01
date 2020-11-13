<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Alta</title>
	<link href="./estilo.css" rel="stylesheet" type="text/css">
	<style type="text/css">
	form > input[type="submit"]{
		margin-top:10px;
		width: 130px;
	}
	</style>
</head>
<body>
	<%@include file="encabezado.html" %>
	<div id="cajaPrincipal">
	<p>
	<h3>Art&iacute;culo:</h3>
	<form action="TestRest" method="post">
		<label for="idClave">Clave:</label>
		<input type="text" value="" id="idClave" name="clave" class="campoCorto"><br>
		<label for="idNombre">Nombre:</label>
		<input type="text" value="" id="idNombre" name="nombre" class="campoLargo"><br>
		<label for="idDescripcion">Descripci&oacute;n:</label>
		<input type="text"  value="" id="idDescripcion" name="descripcion" class="campoLargo"><br>
		<label for="idCosto">Costo unitario:</label>
		<input type="text"  value="" id="idCosto" name="costo" class="campoCorto"><br>
		<label for="idPrecio">Precio unitario:</label>
		<input type="text"  value="" id="idPrecio" name="precio" class="campoCorto"><br>
		<label for="idExistencia">Existencia:</label>
		<input type="text"  value="" id="idExistencia" name="existencia" class="campoCorto"><br>
 	    <input type="hidden" name="vista" value="alta.jsp">
	    <input type="hidden" name="operacion" value="insercion">
		<input type="submit" value="Dar de Alta">
	</form>
	<form action="TestRest" method="post" class="btn">
 	    <input type="hidden" name="vista" value="alta.jsp">
	    <input type="hidden" name="operacion" value="regresar">
		<input type="submit" value="Regresar">
	</form>
	<hr>
	<p> 
	<span id="estado">${resultadoInsert}</span>
	</div>
</body>
</html>