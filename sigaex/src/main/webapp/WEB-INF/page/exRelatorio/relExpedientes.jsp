<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	frmRelExpedientes.action='${pageContext.request.contextPath}/app/expediente/rel/relExpedientes}';
	frmRelExpedientes.submit();	
}
</script>
<c:set var="titulo_pagina" scope="request">
	Relat&oacute;rio de Expedientes
</c:set>
<input type="hidden" name="secaoUsuario" id="secaoUsuario" value="${lotaTitular.orgaoUsuario.descricaoMaiusculas}" />
<input type="hidden" name="tipoRelatorio" id="tipoRelatorio" value="relExpedientes.jrxml" />
<div class="row">
	<div class="col-sm-6">
		<label>Lota&ccedil;&atilde;o</label>
		<siga:selecao propriedade="lotacaoDestinatario" tema="simple" modulo="siga"/>
	</div>
	<div class="col-sm-2">
		<label>Data In&iacute;cio</label>
		<input type="text" name="dataInicio" id="dataInicio" onblur="javascript:verifica_data(this, true);comparaData(dataInicio,dataFim);"
			theme="simple" maxlength="10" class="form-control"/>
	</div>
	<div class="col-sm-2">
		<label>Data Fim</label>
		<input type="text" name="dataFim" id="dataFim" onblur="javascript:verifica_data(this,true);comparaData(dataInicio,dataFim);"
			theme="simple" maxlength="10" class="form-control" />
	</div>
</div>
<input type="hidden" name="lotacao" id="lotacao" value="${lotacaoDestinatarioSel.id}" />
<input type="hidden" name="siglalotacao" id="siglaLotacao" value="${lotacaoDestinatarioSel.sigla}" />

