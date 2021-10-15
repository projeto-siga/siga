<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>


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