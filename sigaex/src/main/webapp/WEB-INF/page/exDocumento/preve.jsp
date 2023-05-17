<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="libs"%>

<c:set var="titulo_pagina" scope="request">
	Visualizar
</c:set>
<c:if test="${not empty modelo.nmArqMod}">
	<c:set var="jsp" scope="request" value="${modelo.nmArqMod}" />
</c:if>
<c:if test="${(empty jsp) and (not empty nmArqMod)}">
	<c:set var="jsp" scope="request" value="${nmArqMod}" />
</c:if>

<siga:pagina titulo="Visualizar documento" popup="true">

<div style="word-wrap: break-word" class="divDoc bg-white p-3 pb-5 w-100">
<tags:fixdocumenthtml>
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
		</tags:fixdocumenthtml>
</div>
</siga:pagina>  

