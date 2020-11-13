<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Espera async</title>
	<link href="./estilo.css" rel="stylesheet" type="text/css">
	<style>
	form{
		margin-top: 10px;
		width: 150px;
		margin-right: 10px;
		display: inline-block;
	}
	textarea {
		color: hsl(300,90%,30%);
		font-family: monospace;
		font-weight: bold;
		font-size: .8em;
	}
	</style>
</head>
<body>
	<%@include file="encabezado.html" %>
	<div id="cajaPrincipal">
	<span>${mensaje}</span> <br>
    <form action="TestAsync" method="post">
 	    <input type="hidden" name="vista" value="espera.jsp">
	    <input type="hidden" name="operacion" value="leerResultado">
	    <input type="hidden" name="folio" value="${folio}">	    
	    <input type="${boton}" value="Checar respuesta">
    </form>
    	<form action="TestAsync" method="post">
 	    <input type="hidden" name="vista" value="espera.jsp">
	    <input type="hidden" name="operacion" value="regresar">
		<input type="submit" value="Regresar">
	</form>
    
	<hr>
	<textarea rows="30" cols="100">${respuesta}</textarea>
	</div>
</body>
</html>