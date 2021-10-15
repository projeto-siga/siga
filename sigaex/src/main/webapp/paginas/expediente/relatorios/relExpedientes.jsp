<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>



<script type="text/javascript" language="Javascript1.1">
<ww:url id="url" action="relExpedientes" namespace="/expediente/rel">
</ww:url>
function sbmt() {
	frmRelExpedientes.action='<ww:property value="%{url}"/>';
	frmRelExpedientes.submit();	
}
</script>
<c:set var="titulo_pagina" scope="request">Relatório de Expedientes</c:set>
<ww:hidden name="secaoUsuario"
	value="${lotaTitular.orgaoUsuario.descricaoMaiusculas}" />
<ww:hidden name="tipoRelatorio" value="relExpedientes.jrxml" />
<h1>${titulo_pagina}</h1>
<tr>
	<td>Lotação</td>
	<td><siga:selecao propriedade="lotacaoDestinatario" tema="simple"
		reler="sim" modulo="siga"/></td>
</tr>
<input type="hidden" name="lotacao" value="${lotacaoDestinatarioSel.id}" />
<input type="hidden" name="siglalotacao"
	value="${lotacaoDestinatarioSel.sigla}" />
<tr>
	<td>Data Início</td>
	<td><ww:textfield name="dataInicio"
		onblur="javascript:verifica_data(this, true);comparaData(dataInicio,dataFim);"
		theme="simple" size="12" maxlength="10" /></td>
</tr>
<tr>
	<td>Data Fim</td>
	<td><ww:textfield name="dataFim"
		onblur="javascript:verifica_data(this,true);comparaData(dataInicio,dataFim);"
		theme="simple" size="12" maxlength="10" /></td>
</tr>


