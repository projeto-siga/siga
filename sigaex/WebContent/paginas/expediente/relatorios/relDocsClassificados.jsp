<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<%@ taglib prefix="ww" uri="/webwork"%>

	<!-- mascara.js -->
	
  		<script src="/siga/javascript/mascara.js"></script>
  
  		<script type="text/javascript">
 				var elementosComMascara = ['#codificacao'];		
  		</script>

  		<input id="mask_in" type="hidden" value="${mascaraEntrada}"/>
  		<input id="mask_out" type="hidden" value="${mascaraSaida}">
  		<input id="mask_js" type="hidden" value="${mascaraJavascript}">

	<!-- mascara.js -->

<c:set var="titulo_pagina" scope="request">Relatório de Classificação Documental</c:set>
<c:set var="secaoUsuario" scope="request">"${lotaTitular.orgaoUsuario.descricaoMaiusculas}"</c:set>


<tr>
	<td width="30%">Lotação:</td>
	<td><siga:selecao propriedade="lotacaoDestinatario" tema="simple" /></td>
</tr>
<tr>
	<td>Subárvore da Classificação documental (opcional):</td>
	<td><input type="text" id="codificacao" name="codificacao" onblur="javascript:aplicarMascara(this)"/></td>
</tr>

<input type="hidden" name="orgaoUsuario" value="${lotaTitular.orgaoUsuario.idOrgaoUsu}" />



