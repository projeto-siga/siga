<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Incluir Sala"> 
<link rel="stylesheet" href="/sigapp/stylesheets/jquery-ui.css" type="text/css" media="screen, projection" />
<script language="javascript" type="text/javascript">
function chamada(novaURL){
	 $(window.document.location).attr('href',novaURL);
 }
function formata(){
	var aux = document.getElementById('cod_local01').value.toUpperCase();
	document.getElementById('cod_local01').value = aux;	
 }
 </script>
<form name="" action="${linkTo[SalaController].insert}" onsubmit="formata()" method="post"><br/>
	<font style="position:absolute;left:5%;right:76%;" class="ui-state-default" size="3px">C&oacute;digo da sala: </font> <input id="cod_local01" type="text"  class="ui-widget" style="position:absolute;left:25%;" name="formLocal.cod_local" maxlength="3" size="3" /> <!-- String --><br/>
	<br/> 
	<font style="position:absolute;left:5%;right:76%;" class="ui-state-default" size="3px">Descri&ccedil;&atilde;o: </font> <input type="text" class="ui-widget" style="position:absolute;left:25%;" name="formLocal.local" maxlength="30" size="50" /> <!-- String --><br/>
	<br/>
	<font style="position:absolute;left:5%;right:76%;" class="ui-state-default" size="3px">C&oacute;digo do forum: </font> <input type="text" class="ui-widget" style="position:absolute;left:25%;" name="cod_forum" id="cod_forum01" maxlength="3" size="50" onchange="if(isNaN(document.getElementById('cod_forum01').value) ){alert('Esse campo tem que ser numérico');}" /> <!-- int --><br/>
	<br/>
	<font style="position:absolute;left:5%;right:76%;" class="ui-state-default" size="3px">Dias de atendimento: </font> <input type="text" class="ui-widget" style="position:absolute;left:25%;" name="formLocal.dias" maxlength="40" size="50" /> <!-- String --><br/>
	<br/>
	<font style="position:absolute;left:5%;right:76%;" class="ui-state-default" size="3px">Hor&aacute;rio inicial: </font><input type="text" class="ui-widget" style="position:absolute;left:25%;" name="formLocal.hora_ini" maxlength="8" size="50" /> <!-- String --><br/>
	<br/>
	<font style="position:absolute;left:5%;right:76%" class="ui-state-default" size="3px">Hor&aacute;rio final: </font><input type="text" class="ui-widget" style="position:absolute;left:25%;" name="formLocal.hora_fim" maxlength="8" size="50" /> <!--  String  --><br/>
	<br/>
	<input type="hidden" name="formLocal.intervalo_atendimento" value="10" /> <!-- int -->
	<input type="hidden" name="formLocal.exibir" value="1" /> <!-- int -->
	<font style="position:absolute;left:5%;right:76%" class="ui-state-default" size="3px">Endere&ccedil;o: </font><input type="text" class="ui-widget" style="position:absolute;left:25%;" name="formLocal.endereco" maxlength="100" size="50" /> <!-- String --><br/>
	<input type="hidden" name="formLocal.ordem_apresentacao" value="0" />  <!-- int --> 
<br/>
	<div style="position:absolute;left:5%;">
	<input class="ui-button" type="submit" value="Grava"  /> <input class="ui-button" type="button" value="Lista" onclick="chamada('${linkTo[SalaController].listar}');" />
	<br/>
	</div>
</form>
<a style="position:absolute;left:5%;top:150%;" class="ui-state-hover" href="/sigapp/">In&iacute;cio</a>
</siga:pagina>