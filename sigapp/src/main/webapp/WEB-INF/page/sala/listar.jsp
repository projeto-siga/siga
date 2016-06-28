<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Lista Salas"> 
<link rel="stylesheet" href="/sigapp/stylesheets/jquery-ui.css" type="text/css" media="screen, projection" />
<script language="JavaScript" type="text/javascript">
function formata(){
	var aux = document.getElementById('cod_local01').value.toUpperCase();
	document.getElementById('cod_local01').value = aux;
	document.charset = 'ISO-8859-1';
}
</script>
<br>
<table class="ui-tabs"  align="center" style="font-size: 100%;">
<tr  bgcolor="silver">
<th>&nbsp; C&oacute;digo &nbsp;</th>
<th>&nbsp; Sala &nbsp;</th>
<th>&nbsp; Forum &nbsp;</th>
<th>&nbsp; Dias &nbsp;</th>
<th>&nbsp; Abre as &nbsp;</th>
<th>&nbsp; Fecha as &nbsp;</th>
<th>&nbsp; Endere&ccedil;o &nbsp;</th>
<th></th>
</tr>
	<c:forEach items="${listLocais}" var="locais">
		<tr class="ui-button-icon-only"
			<c:if test="${!b}"> 
				bgcolor="#dddddd"
			</c:if>
		/>
		<c:set var="b" value="${!b}"/>		
		<td>&nbsp; ${locais.cod_local}
		</td>
		<td>&nbsp; ${locais.local}</td>
		<td>&nbsp; ${locais.forumFk.descricao_forum}</td>
		<td>&nbsp; ${locais.dias}</td>
		<td>&nbsp; ${locais.hora_ini}</td>
		<td>&nbsp; ${locais.hora_fim}</td>
		<td>&nbsp; ${locais.endereco}</td>
		<td>&nbsp;
			<form name="sala_deleta01" action="${linkTo[SalaController].delete}" method="post"> &nbsp;
				<img  src="/siga/css/famfamfam/icons/delete.png">&nbsp;
				<input type="hidden" name="cod_sala" value="${locais.cod_local}" />
				<input type="submit"  value="Exclui" />
			</form>
		</td>
	</tr>
	</c:forEach>
</table>
<br> 
<form  style="border-style: groove; border-color: silver;" name="restringe_foruns" action="${linkTo[SalaController].listar}" method="get" onsubmit="formata()" enctype="multipart/form-data" accept-charset="ISO-8859-1">
<br>
<font class="ui-state-default" size="3px" style="position:absolute;left:5%;right:79%;"> C&oacute;digo: </font><input id="cod_local01" type="text" name="cod_local" class="ui-widget" style="position:absolute;left:25%;" maxlength="3" size="3" /><br>
<br>
<font class="ui-state-default" size="3px" style="position:absolute;left:5%;right:79%;"> Sala: </font><input type="text" name="sala" class="ui-widget" style="position:absolute;left:25%;" maxlength="50" size="50" /><br>
<br>
<font class="ui-state-default" size="3px%" style="position:absolute;left:5%;right:79%"> Descri&ccedil;&atilde;o Forum: </font><input type="text" name="desc_forum" class="ui-widget" style="position:absolute;left:25%;" maxlength="50" size="50" /><br>
<br>
<input type="submit" class="ui-button" style="position:absolute;left:5%;" value="Busca" onclick="" /> 
</form>
<br><br>
<a class="ui-state-hover" style="position:absolute;left:5%;" href="/sigapp/">In&iacute;cio</a>
</siga:pagina>