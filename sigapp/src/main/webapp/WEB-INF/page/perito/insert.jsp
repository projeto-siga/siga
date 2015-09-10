<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<meta charset="ISO-8859-1">
<siga:pagina titulo="Ok?">
	<link rel="stylesheet" href="/sigapp/stylesheets/jquery-ui.css" type="text/css" media="screen, projection" />
	<h4 class="ui-widget" style="position:absolute;left:5%; color:red;">${resposta}</h4>
	<br><br>
	<a style="position:absolute;left:5%;" class="ui-state-hover" href="/sigapp/">Voltar</a>
</siga:pagina>