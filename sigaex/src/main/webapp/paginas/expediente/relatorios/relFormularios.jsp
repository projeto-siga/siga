<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>



<script type="text/javascript" language="Javascript1.1">

function sbmt() {
	frmRelExpedientes.action='<ww:property value="%{url}"/>';
	frmRelExpedientes.submit();	
}
</script>
<c:set var="titulo_pagina" scope="request">Relação de formulários</c:set>
<ww:hidden name="secaoUsuario"
	value="${lotaTitular.orgaoUsuario.descricaoMaiusculas}" />

<input type="hidden" name="lotacaoTitular"
	value="${lotaTitular.siglaLotacao}" />
<input type="hidden" name="orgaoUsuario"
	value="${lotaTitular.orgaoUsuario.idOrgaoUsu}" />
<input type="hidden" name="idTit" value="${titular.id}" />
