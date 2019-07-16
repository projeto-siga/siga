<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script type="text/javascript" language="Javascript1.1">

function sbmt() {
	frmRelExpedientes.action='${url}';
	frmRelExpedientes.submit();	
}
</script>
<c:set var="titulo_pagina" scope="request">
	Relação de documentos entre duas datas
</c:set>
<input type="hidden" name="secaoUsuario" id="secaoUsuario" value="${lotaTitular.orgaoUsuario.descricaoMaiusculas}" />
<input type="hidden" name="campos" value="idTpDoc" />
<div class="row">
	<div class="col-sm-2">
		<label>Origem</label>
		<select name="origem" class="form-control">
			<c:forEach items="${listaExTipoDocumento}" var="item">
				<option value="${item.idTpDoc}">
					${item.descrTipoDocumento}
				</option>  
			</c:forEach>
		</select>
	</div>
	<div class="col-sm-6">
		<label><fmt:message key="usuario.lotacao"/></label>
		<siga:selecao propriedade="lotacaoDestinatario" tema="simple" reler="sim" modulo="siga"/>
	</div>
	<input type="hidden" name="lotacao" value="${lotacaoDestinatarioSel.id}" />
	<input type="hidden" name="siglalotacao" value="${lotacaoDestinatarioSel.sigla}" />
</div>
<div class="row">
	<div class="col-sm-2">
		<label>Data Inicial</label>
		<input type="text" name="dataInicial" id="dataInicial" onblur="javascript:verifica_data(this, true);comparaData(dataInicial,dataFinal);"
			theme="simple" maxlength="10" class="form-control" />
	</div>
	<div class="col-sm-2">
		<label>Data Final</label>
		<input type="text" name="dataFinal" id="dataFinal" onblur="javascript:verifica_data(this,true);comparaData(dataInicial,dataFinal);"
			theme="simple" maxlength="10" class="form-control" />
	</div>
</div>
<input type="hidden" name="lotacaoTitular" value="${lotaTitular.siglaLotacao}" />
<input type="hidden" name="orgaoUsuario" value="${lotaTitular.orgaoUsuario.idOrgaoUsu}" />
<input type="hidden" name="idTit" value="${titular.id}" />


