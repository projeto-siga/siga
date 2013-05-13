<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<%@ taglib prefix="ww" uri="/webwork"%>

<script src="/siga/javascript/format4js.js"></script>
<script src="/siga/javascript/mascara.js"></script>

<c:set var="titulo_pagina" scope="request">Relatório de Classificação Documental</c:set>
<c:set var="secaoUsuario" scope="request">"${lotaTitular.orgaoUsuario.descricaoMaiusculas}"</c:set>
<input id="mask_in" type="hidden" value="${mascaraEntrada}"/>
<input id="mask_out" type="hidden" value="${mascaraSaida}">


<tr>
	<td width="30%">Lotação:</td>
	<td><siga:selecao propriedade="lotacaoDestinatario" tema="simple" /></td>
</tr>
<tr>
	<td>Classificação documental:</td>
	<td><input type="text" id="codificacao" name="codificacao" onblur="javascript:aplicarMascara(this)"/></td>
</tr>

<input type="hidden" name="orgaoUsuario" value="${lotaTitular.orgaoUsuario.idOrgaoUsu}" />



