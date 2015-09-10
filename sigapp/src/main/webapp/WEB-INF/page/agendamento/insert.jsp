<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Agenda Insert">
	<link rel="stylesheet" href="/sigapp/stylesheets/jquery-ui.css" type="text/css" media="screen, projection" />
	<h4 class="ui-widget" style="position:absolute;left:5%; color:red;">${resposta}</h4><br>
	<br>
	<form name="frm_agendamento_insert01" method="get" action="${linkTo[AgendamentoController].incluirAjax}" enctype="multipart/form-data">
	 <input type="hidden" name="fixo_perito_juizo" value="${perito_juizo}" />
	 <input class="ui-button" style="position:absolute;left:35%;" type="submit" value="Continuar agendando." />
	</form>
	<br><br><br>
	<a style="position:absolute;left:5%;" class="ui-state-hover" href="/sigapp/">Voltar</a>
</siga:pagina>