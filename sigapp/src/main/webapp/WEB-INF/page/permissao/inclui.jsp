<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>

<siga:pagina titulo="Incluir Permissao" >
    <link rel="stylesheet" href="/sigapp/stylesheets/jquery-ui.css" type="text/css" media="screen, projection" /> 
	<form name="frm_inclui_permissao" method="post" action="${linkTo[PermissaoController].inclui}" >
	<br/>
	<div class="ui-state-default" style="position: absolute;left:5%;right:77%;"> Matr&iacute;cula:</div><input type="text" name="matricula_permitida" style="position: absolute;left:25%;" value="" /><div class="ui-state-default" style="position:absolute;left:45%;"> &nbsp; (Exemplo: 11222)</div>
	<br/><br/>
	<div class="ui-state-default" style="position: absolute;left:5%;right:77%;"> SESB:</div>
		<select name="sesb_permitida" class="ui-widget" style="position: absolute;left:25%;" >
		 <option value="T2">T2</option><option value="RJ">RJ</option><option value="ES">ES</option>
		</select>
	<br/><br/>
	<br/><br/>
	<br/><br/>
	<div class="ui-state-default" style="position: absolute;left:5%;right:77%;" > Nome:</div><input type="text" name="nome_permitido" style="position:absolute;left:25%;" value="" />
	<br/><br/>
	<div class="ui-state-default" style="position: absolute;left:5%;right:77%;"> C&oacute;digo do Forum:</div><input type="text" name="forum_permitido" style="position:absolute;left:25%" value="" maxlength="3" />
	<br/><br/>
	<input class="ui-button" style="position: absolute;left:5%;" type="submit" value="Permite usu&aacute;rio" />
	<br/><br/><br/>
	<br/><br/><br/>
	<h4 class="ui-widget" style="position: absolute;left:5%; color:red">${mensagem}</h4>
	</form>
	<a style="position: absolute;left:5%;top:120%" class="ui-state-hover" href="/sigapp/">Voltar</a>
</siga:pagina>
	