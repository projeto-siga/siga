<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Agendamento Cancelar">
	<link rel="stylesheet" href="/sigapp/stylesheets/jquery-ui.css" type="text/css" media="screen, projection" />
	<br>
	<h4 class="ui-widget" style="position:absolute;left:5%; color:red;">${resultado}</h4>
	<br>
	<form name="frm_agendamento_deleta01" method="get" action="${linkTo[AgendamentoController].excluir}" enctype="multipart/form-data">
		<input type="hidden" name="data" value=${dtt} />
		<input class="ui-button" style="position:absolute;left:35%;" type="submit" value="Retornar a tela anterior" />
	</form>
	<br/><br/><br/>
	<a class="ui-state-hover" style="position:absolute;left:5%;" href="/sigapp/">Voltar</a>
</siga:pagina>