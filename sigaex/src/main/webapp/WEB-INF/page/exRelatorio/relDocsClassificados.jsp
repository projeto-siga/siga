<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

	
<script src="/siga/javascript/mascara.js"></script>
<script type="text/javascript">
 	var elementosComMascara = ['#codificacao'];		
</script>

<input id="mask_in" type="hidden" value="${mascaraEntrada}"/>
<input id="mask_out" type="hidden" value="${mascaraSaida}">
<input id="mask_js" type="hidden" value="${mascaraJavascript}">

<c:set var="titulo_pagina" scope="request">Relatório de Classificação Documental</c:set>
<c:set var="secaoUsuario" scope="request">"${lotaTitular.orgaoUsuario.descricaoMaiusculas}"</c:set>
<div class="row">
	<div class="col-sm-6">
		<label><fmt:message key="usuario.lotacao"/></label>
		<siga:selecao propriedade="lotacaoDestinatario" tema="simple" modulo="siga"/>
	</div>
	<div class="col-sm-4">
		<label>Subárvore da Classificação documental (opcional)</label>
		<input type="text" id="codificacao" name="codificacao" onblur="javascript:aplicarMascara(this)" class="form-control"/>
	</div>
</div>
<input type="hidden" name="orgaoUsuario" value="${lotaTitular.orgaoUsuario.idOrgaoUsu}" />



