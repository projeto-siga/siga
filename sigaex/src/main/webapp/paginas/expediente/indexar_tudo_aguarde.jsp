<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<c:set var="cabecalho_meta" scope="request">
	<meta http-equiv="refresh"
		content="3;url=<ww:url includeParams="all"/>" />
</c:set>
<siga:pagina titulo="IndexaÃ§Ã£o de Documentos" meta="${cabecalho_meta}">

	<p class="no_results">Indexando, aguarde...</p>

	<c:if test="${not empty mensagemAguarde and mensagemAguarde != '0'}">
		<p class="no_results">${mensagemAguarde}</p>
	</c:if>

</siga:pagina>