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
	<c:if test="${f:resource('isVersionTest') or f:resource('isBaseTest')}">
		<c:if test="${f:resource('isVersionTest')}">SISTEMA</c:if>
		<c:if
			test="${f:resource('isVersionTest') and f:resource('isBaseTest')}"> E </c:if>
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

<link rel="stylesheet" href="/siga/css/ecoblue/css/reset-fonts.css"
	type="text/css" media="screen, projection">
<link rel="stylesheet" href="/siga/css/ecoblue/css/gt-styles.css"
	type="text/css" media="screen, projection">
<link rel="stylesheet" href="/siga/css/ecoblue/css/custom.css"
	type="text/css" media="screen, projection">

<!-- <link rel="StyleSheet" href="${path}/sigalibs/siga.css" type="text/css"	title="SIGA Estilos" media="screen"> -->

<script src="/siga/sigalibs/ajax.js" language="JavaScript1.1"
	type="text/javascript"></script>
<script src="/siga/sigalibs/static_javascript.js"
	language="JavaScript1.1" type="text/javascript" charset="utf-8"></script>

<!-- <link href="${pageContext.request.contextPath}/sigalibs/menu.css"
	rel="stylesheet" type="text/css" /> -->

<link rel="shortcut icon" href="/sigalibs/siga.ico" />

<script language="JavaScript"
	src="/siga/javascript/jquery/1.3/jquery.min.js" type="text/javascript"></script>
<!--[if gte IE 5.5]><script language="JavaScript" src="/siga/javascript/jquery.ienav.js" type="text/javascript"></script><![endif]-->
<script language="JavaScript" type="text/javascript">
	$(document).ready(function() {
		$('.links li code').hide();
		$('.links li p').click(function() {
			$(this).next().slideToggle('fast');
		});
	});
</script>

</head>

<body onload="${onLoad}">
	<c:if test="${popup!='true'}">
		<div class="gt-hd clearfix">
			<!-- leaf watermark -->
			<div class="gt-leaf-watermark clearfix">
				<!-- head top -->
				<div class="gt-hd-top clearfix">
					<div class="gt-fixed-wrap clearfix">
						<!-- utility box -->
						<c:if test="${not empty cadastrante}">
							<div class="gt-util-box">
								<div class="gt-util-box-inner"
									style="padding-top: 10px; font-size: 100%;">
									<p style="text-align: right;">
										Olá, <strong><c:catch>
												<c:out default="Convidado"
													value="${f:maiusculasEMinusculas(cadastrante.nomePessoa)}" />
												<c:choose>
													<c:when test="${not empty cadastrante.lotacao}">
						 - ${cadastrante.lotacao.sigla}</c:when>
												</c:choose>
											</c:catch> </strong> <span class="gt-util-separator">|</span> <a
											href="/siga/logoff.action">sair</a>
									</p>
									<p style="text-align: right; padding-top: 10px;">
										<c:catch>
											<c:choose>
												<c:when
													test="${not empty titular && titular.idPessoa!=cadastrante.idPessoa}">Substituindo: <strong>${f:maiusculasEMinusculas(titular.nomePessoa)}</strong>
													<span class="gt-util-separator">|</span>
													<a href="/siga/substituicao/finalizar.action">finalizar</a>
												</c:when>
												<c:when
													test="${not empty lotaTitular && lotaTitular.idLotacao!=cadastrante.lotacao.idLotacao}">Substituindo: <strong>${f:maiusculasEMinusculas(lotaTitular.nomeLotacao)}</strong>
													<span class="gt-util-separator">|</span>
													<a href="/siga/substituicao/finalizar.action">finalizar</a>
												</c:when>
												<c:otherwise></c:otherwise>
											</c:choose>
										</c:catch>
									</p>
								</div>
							</div>
						</c:if>
						<!-- / utility box -->
						<!-- logo -->
						<div class="gt-logo" style="padding: 0;">
							<img style="margin-top: 3px; margin-bottom: -13px;"
								src="/siga/imagens/logo.png">
						</div>
						<div class="gt-company">
							<strong>Justi&ccedil;a Federal <c:catch>
									<c:if test="${not empty titular.orgaoUsuario.descricao}">
- ${titular.orgaoUsuario.descricao}
</c:if>
								</c:catch> </strong>
						</div>
						<div class="gt-version">
							Sistema Integrado de Gest&atilde;o Administrativa
							<c:if test="${not empty env}"> - <span style="color: red">${env}</span>
							</c:if>
						</div>
						<!-- / logo -->
					</div>
				</div>
				<!-- /head top -->
				<!-- navbar -->
				<div class="gt-navbar clearfix">
					<div class="gt-fixed-wrap clearfix">
						<!-- navigation -->
						<div class="gt-nav">
							<ul id="navmenu-h">
								<c:import url="/sigalibs/menuprincipal.jsp" />
							</ul>
						</div>
						<!-- / navigation -->
						<!-- search -->
						<div class="gt-search">
							<div class="gt-search-inner">
								<input type="text" class="gt-search-text"
									value="Buscar documento"
									onfocus="javascript:if(this.value=='Buscar documento')this.value='';"
									onblur="javascript:if(this.value=='')this.value='Buscar documento';">
							</div>
						</div>
					</div>
				</div>
				<!-- /navbar -->
			</div>
			<!-- /leaf watermark -->
		</div>

		<div id="quadroAviso"
			style="position: absolute; font-weight: bold; padding: 4px; color: white; visibility: hidden">-</div>

	</c:if>

	<div id="carregando"
		style="position: absolute; top: 0px; right: 0px; background-color: red; font-weight: bold; padding: 4px; color: white; display: none">Carregando...</div>