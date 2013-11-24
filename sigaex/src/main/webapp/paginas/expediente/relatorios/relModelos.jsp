<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	frm.action='<ww:property value="%{url}"/>';
	frm.submit();
}
</script>

<c:set var="titulo_pagina" scope="request">Relação de Modelos</c:set>
<c:set var="secaoUsuario" scope="request">"${lotaTitular.orgaoUsuario.descricaoMaiusculas}"</c:set>
<h1>${titulo_pagina}</h1>
<input type="hidden" name="secaoUsuario"
			value="${lotaTitular.orgaoUsuario.descricaoMaiusculas}" />