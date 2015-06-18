<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="req" value="${pageContext.request}" />
<c:set var="url">${req.requestURL}</c:set>
<c:set var="uri" value="${req.requestURI}" />
<c:set var="baseURL" value="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}" />

<c:set var="titulo_pagina" scope="request">
	Visualizar
</c:set>
<c:if test="${not empty modelo.nmArqMod}">
	<c:set var="jsp" scope="request" value="${modelo.nmArqMod}" />
</c:if>
<c:if test="${(empty jsp) and (not empty nmArqMod)}">
	<c:set var="jsp" scope="request" value="${nmArqMod}" />
</c:if>

<c:import url="${baseURL}/siga/paginas/cabecalho_popup.jsp" />

<table width="100%" border="0">
	<tr>
		<td style="padding: 10;"><tags:fixdocumenthtml>
			<c:if test="${modelo.conteudoTpBlob == 'template/freemarker'}">
			    <c:choose>
				    <c:when test="${empty mov}">
					    ${f:processarModelo(doc, 'processar_modelo', par, null)}
					</c:when>
					<c:otherwise>
                        ${f:processarModelo(mov, 'processar_modelo', par, null)}
					</c:otherwise>
			    </c:choose>
			</c:if>
			<c:if test="${modelo.conteudoTpBlob != 'template/freemarker'}">
				<c:import url="/paginas/expediente/modelos/${jsp}" />
			</c:if>
		</tags:fixdocumenthtml></td>
	</tr>
</table>

<c:import url="${baseURL}/siga/paginas/rodape_popup.jsp" />
