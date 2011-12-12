<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ attribute name="titulo"%>
<%@ attribute name="menu"%>
<%@ attribute name="idpagina"%>
<%@ attribute name="barra"%>
<%@ attribute name="ambiente"%>
<%@ attribute name="popup"%>
<%@ attribute name="meta"%>
<%@ attribute name="pagina_de_erro"%>
<%@ attribute name="onLoad"%>

<c:if test="${not empty titulo}">
	<c:set var="titulo" scope="request" value="${titulo}" />
</c:if>

<c:if test="${not empty pagina_de_erro}">
	<c:set var="pagina_de_erro" scope="request" value="${pagina_de_erro}" />
</c:if>

<c:if test="${not empty menu}">
	<c:set var="menu" scope="request">${menu}</c:set>
</c:if>

<c:if test="${not empty idpagina}">
	<c:set var="idpage" scope="request">${idpagina}</c:set>
</c:if>

<c:if test="${not empty barra}">
	<c:set var="barranav" scope="request">${barra}</c:set>
</c:if>

<c:set var="ambiente">
	<c:if test="${f:resource('isVersionTest') || f:resource('isBaseTest')}">
		<c:if test="${f:resource('isVersionTest')}">SISTEMA</c:if>
		<c:if
			test="${f:resource('isVersionTest') && f:resource('isBaseTest')}"> E </c:if>
		<c:if test="${f:resource('isBaseTest')}">BASE</c:if> DE TESTES
	</c:if>
</c:set>
<c:if test="${not empty ambiente}">
	<c:set var="env" scope="request">${ambiente}</c:set>
</c:if>

<html>
<head>
<title>SIGA - ${titulo_pagina}</title>
<META HTTP-EQUIV="Expires" CONTENT="0">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="content-type" CONTENT="text/html; charset=UTF-8">
${meta}

<c:set var="path" scope="request">${pageContext.request.contextPath}</c:set>
<link rel="StyleSheet" href="${path}/sigalibs/siga.css" type="text/css"
	title="SIGA Estilos" media="screen">
<script src="${path}/sigalibs/ajax.js" language="JavaScript1.1"
	type="text/javascript"></script>
<script src="${path}/sigalibs/static_javascript.js"
	language="JavaScript1.1" type="text/javascript" charset="utf-8"></script>

<link href="${pageContext.request.contextPath}/sigalibs/menu.css"
	rel="stylesheet" type="text/css" />

<link rel="shortcut icon" href="/sigalibs/siga.ico" />
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0"
	onload="${onLoad}" style="background: url(null) fixed no-repeat;">
	<c:if test="${popup!='true'}">
		<div style="background-color: #9DB1E5; height: 49px; width: 100%;">
			<div style="float: left">
				<img
					src="${pageContext.request.contextPath}/sigalibs/toplogo1_70.png" />
			</div>


			<div style="float: left">
				<p class="cabecalho-title">
					<strong>Justi&ccedil;a Federal <c:catch>
							<c:if test="${not empty titular.orgaoUsuario.descricao}">
- ${titular.orgaoUsuario.descricao}</c:if>
						</c:catch> </strong>
				</p>
				<p class="cabecalho-subtitle">
					Sistema Integrado de Gest&atilde;o Administrativa
					<c:if test="${not empty env}"> - <span style="color: red">${env}</span>
					</c:if>
				</p>
			</div>
			<div style="float: right">
				<img
					src="${pageContext.request.contextPath}/sigalibs/toplogo2_70.png"
					alt="Bandeira do Brasil">
			</div>
		</div>

		<div id="carregando"
			style="position: absolute; top: 0px; right: 0px; background-color: red; font-weight: bold; padding: 4px; color: white; display: none">Carregando...</div>
		<div id="quadroAviso"
			style="position: absolute; font-weight: bold; padding: 4px; color: white; visibility: hidden">-</div>

		<c:import url="/sigalibs/menuprincipal.jsp" />


		<TABLE WIDTH="100%" height="100%" BORDER=0 CELLPADDING=0 CELLSPACING=0>
			<TR>
				<c:if test="${titulo == 'P&aacute;gina Inicial'}">
					<TD colspan="3" valign="top"
						style="padding-left: 0; padding-top: 0; padding-right: 0; padding-bottom: 0;">
				</c:if>
				<c:if test="${titulo != 'P&aacute;gina Inicial'}">
					<TD colspan="4" valign="top"
						style="padding-left: 7; padding-top: 7; padding-right: 7; padding-bottom: 7;">
				</c:if>
<%--				<c:if test="${title=='false'}">
			</TR>
		</TABLE>
</body>
</html>
</c:if>--%>
</c:if>
