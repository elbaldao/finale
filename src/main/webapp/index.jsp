<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Proyecto ASR new...</title>
</head>
<body>
<h1>Ejemplo de Proyecto de ASR con Cloudant ahora con DevOps</h1>
<hr />
<p>Opciones sobre la base de datos Cloudant versión 2019:</p>
<ul>
<li><a href="listar">Listar</a></li>
<li><a href="insertar?palabra=hola">Insertar</a></li>
</ul>

<form method="POST" action="texttospeech">
   <p>Nombre Cancion : <input type="text" name="nombre"></p>
   <p>Letra : <input type="text" name="letra"></p>
   <input type="submit" id="Invia" value="Invia">
</form>
</body>

</html>