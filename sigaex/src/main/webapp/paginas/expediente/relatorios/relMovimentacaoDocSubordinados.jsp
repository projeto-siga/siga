<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	frm.action='<ww:property value="%{url}"/>';
	frm.submit();
}
</script>

<c:set var="titulo_pagina" scope="request">RelatÃ³rio de MovimentaÃ§Ã£o de Documentos em Setores Subordinados</c:set>
<c:set var="secaoUsuario" scope="request">"${lotaTitular.orgaoUsuario.descricaoMaiusculas}"</c:set>
<tr>
	<td>Tipo de RelatÃ³rio:</td>
	<td><ww:select name="tipoRel"
		list="#{'1':'Documentos Ativos', '2':'Como Gestor', '3':'Como Interessado'}" /></td>
</tr>
<tr>
	<td width="15%"><fmt:message key="usuario.lotacao"/>:</td>
	<td><siga:selecao propriedade="lotacaoDestinatario" tema="simple" modulo="siga"/></td>
</tr>
<tr>
	<td>Incluir setores subordinados?</td>
	<td><ww:checkbox name="incluirSubordinados" /></td>
<tr>
	<td>Tipo de Documento:</td>
	<td><ww:select name="tipoFormaDoc" list="listaExTipoFormaDoc"
		listValue="descricao" listKey="descricao" /></td>
</tr>

<input type="hidden" name="lotacaoTitular"
	value="${lotaTitular.siglaLotacao}" />
<input type="hidden" name="orgaoUsuario"
	value="${lotaTitular.orgaoUsuario.idOrgaoUsu}" />
<input type="hidden" name="idTit"
	value="${titular.id}" />



