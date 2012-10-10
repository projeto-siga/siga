<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://cheditor.com" prefix="FCK"%>
<%@ taglib prefix="ww" uri="/webwork"%>

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
		reler="sim" /></td>
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


