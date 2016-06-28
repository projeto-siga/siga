<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<meta charset="ISO-8859-1">
<siga:pagina titulo="'Incluir Perito">
 <link rel="stylesheet" href="/sigapp/stylesheets/jquery-ui.css" type="text/css" media="screen, projection" />
	<form action="${linkTo[PeritoController].insert}"  method="post">
		<br>
		<font style="position: absolute;left:5%;right:77%;" class="ui-state-default" size="3px">CPF do Perito:</font><input type="text" name="cpf_perito" style="position:absolute;left:25%;" class="ui-widget"" size="11" maxlength="11" > <font style="position:absolute;left:50%;"" class="ui-state-default" size="3px">(digite sem pontos ou tra&ccedil;os)</font>
		<br><br>
		<font style="position: absolute;left:5%;right:77%;" class="ui-state-default" size="3px" >Nome:</font><input type="text" name="nome_perito" style="position:absolute;left:25%;" class="ui-widget" size="40" maxlength="200"><br><br>
		<div style="position:absolute;left:5%;right:77%;">
			<input class="ui-button" type="Submit" value="Incluir Perito" >
		</div>
		<div id="div_msg" class="ui-widget"></div>
		<a style="position:fixed;left:5%;top:55%;" class="ui-state-hover" href="/sigapp/">In&iacute;cio</a>
	</form>
</siga:pagina>