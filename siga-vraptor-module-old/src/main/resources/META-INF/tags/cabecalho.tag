<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ attribute name="titulo"%>
<%@ attribute name="menu"%>
<%@ attribute name="idpagina"%>
<%@ attribute name="barra"%>
<%@ attribute name="ambiente"%>
<%@ attribute name="popup"%>
<%@ attribute name="meta"%>
<%@ attribute name="pagina_de_erro"%>
<%@ attribute name="onLoad"%>
<%@ attribute name="desabilitarbusca"%>
<%@ attribute name="desabilitarmenu"%>
<%@ attribute name="incluirJs"%>
<%@ attribute name="compatibilidade"%>
<%@ attribute name="desabilitarComplementoHEAD"%>

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

<c:set var="XUACompatible" scope="request">IE=EDGE</c:set>
<c:if test="${not empty compatibilidade}">
	<c:set var="XUACompatible" scope="request">${compatibilidade}</c:set>
</c:if>

<c:set var="siga_version"  scope="request" value="${f:sigaVersion()}" />

<c:set var="siga_cliente_sso" scope="request" value="${f:resource('/siga.integracao.sso')}" />
<c:set var="siga_cliente_sso_btn_txt" scope="request" value="${f:resource('/siga.integracao.sso.nome')}" /> 

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

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>SIGA - ${titulo_pagina}</title>
<meta http-equiv="X-UA-Compatible" content="${XUACompatible}" />
<META HTTP-EQUIV="Expires" CONTENT="0">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="content-type" CONTENT="text/html; charset=UTF-8">
${meta}

<c:set var="path" scope="request">${pageContext.request.contextPath}</c:set>

<link rel="stylesheet" href="/siga/bootstrap/css/bootstrap.min.css" type="text/css" media="screen, projection"/>

<link rel="stylesheet" href="/siga/css/ecoblue/css/reset-fonts.css"
	type="text/css" media="screen, projection">
<link rel="stylesheet" href="/siga/css/ecoblue/css/gt-styles.css"
	type="text/css" media="screen, projection">
<link rel="stylesheet" href="/siga/css/ecoblue/css/custom.css"
	type="text/css" media="screen, projection">
<link type="application/opensearchdescription+xml" rel="search"
	href="/siga/opensearchdescription.xml" />

<!-- <link rel="StyleSheet" href="/sigalibs/siga.css" type="text/css"	title="SIGA Estilos" media="screen"> -->

<script src="/siga/javascript/siga8.js" 	type="text/javascript" charset="utf-8"></script>
<script src="/siga/javascript/picketlink.js" type="text/javascript"
	charset="utf-8"></script>


<!-- <link href="${pageContext.request.contextPath}/sigalibs/menu.css"
	rel="stylesheet" type="text/css" /> -->

<link rel="shortcut icon" href="/siga/imagens/siga.ico" />

<script src="/siga/public/javascript/jquery/jquery-1.11.2.min.js"
	type="text/javascript"></script>
<script src="/siga/javascript/jquery/jquery-migrate-1.2.1.min.js"
	type="text/javascript"></script>
<script
	src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"
	type="text/javascript"></script>
<script src="/siga/javascript/json2.js" type="text/javascript"></script>
<link rel="stylesheet" href="/siga/javascript/jquery-ui-1.10.3.custom/css/ui-lightness/jquery-ui-1.10.3.custom.min.css" type="text/css" media="screen, projection">
<script src="/siga/popper-1-14-3/popper.min.js"></script>
<script language="JavaScript" src="/siga/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script language="JavaScript" src="/siga/javascript/datepicker-pt-BR.js" type="text/javascript"></script>
<!-- <link rel="stylesheet" href="/siga/javascript/jquery-ui-1.10.3.custom/development-bundle/themes/base/jquery.ui.all.css"
	type="text/css" media="screen, projection"> -->
<c:if test="${not empty incluirJs}">
	<script src="${incluirJs}" type="text/javascript"></script>
</c:if>

<%-- Desabilitado porque requer o jquery 1.7 ou maior. 	
<script language="JavaScript"
	src="/siga/javascript/autogrow.min.js" type="text/javascript"></script>
--%>
<!--[if gte IE 5.5]><script language="JavaScript" src="/siga/javascript/jquery.ienav.js" type="text/javascript"></script><![endif]-->

<script type="text/javascript">
	$(document).ready(function() {
		$('.links li code').hide();
		$('.links li p').click(function() {
			$(this).next().slideToggle('fast');
		});
		$('.once').click(function(e) {
			if (this.beenSubmitted)
				e.preventDefault();
			else
				this.beenSubmitted = true;
		});
		//$('.autogrow').css('overflow', 'hidden').autogrow();
	});
</script>
<c:catch>
	<c:if test="${desabilitarComplementoHEAD != 'sim'}">
		<c:if test="${not empty titular}">
			${f:getComplementoHead(cadastrante.orgaoUsuario)}
		</c:if>
	</c:if>
</c:catch>
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
						<c:choose>
								<c:when test="${siga_cliente_sso and logadoviaGovBr == true and f:resource('/siga.integracao.sso.dominio.logout') != ''}">
									<c:set var="siga_cliente_sso_logout_url" scope="request" value="${f:resource('/siga.integracao.sso.dominio.logout')}?post_logout_redirect_uri=${f:resource('/siga.base.url')}" />
								</c:when>
								<c:otherwise>
									<c:set var="siga_cliente_sso_logout_url" scope="request" value="" />
								</c:otherwise>
						</c:choose>
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
											href="${siga_cliente_sso_logout_url}/siga/public/app/logout">sair</a>
									</p>
									<p style="text-align: right; padding-top: 10px;">
										<c:catch>
											<c:choose>
												<c:when
													test="${not empty titular && titular.idPessoa!=cadastrante.idPessoa}">Substituindo: <strong>${f:maiusculasEMinusculas(titular.nomePessoa)}</strong>
													<span class="gt-util-separator">|</span>
													<a href="/siga/app/substituicao/finalizar">finalizar</a>
												</c:when>
												<c:when
													test="${not empty lotaTitular && lotaTitular.idLotacao!=cadastrante.lotacao.idLotacao}">Substituindo: <strong>${f:maiusculasEMinusculas(lotaTitular.nomeLotacao)}</strong>
													<span class="gt-util-separator">|</span>
													<a href="/siga/app/substituicao/finalizar">finalizar</a>
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
						<a href="/siga" title="Página inicial" class="link-sem-estilo">
							<div class="gt-logo" style="padding: 0;">
								<img style="margin-top: 3px; margin-bottom: -13px;"
									src="/siga/imagens/logo.png">
							</div>
							<div class="gt-company">
								<strong>${f:resource('siga.cabecalho.titulo')} <c:catch>
										<c:if test="${not empty titular.orgaoUsuario.descricao}">- ${titular.orgaoUsuario.descricao}</c:if>
									</c:catch>
								</strong>
							</div>
							<div class="gt-version">
								<c:choose>
									<c:when test="${not empty f:resource('siga.cabecalho.titulo') && fn:contains(f:resource('siga.cabecalho.titulo'), 'Governo do Estado de S')}">
										Sistema de Gest&atilde;o Arquiv&iacute;stica de Documentos
									</c:when>
									<c:otherwise>
										Sistema Integrado de Gest&atilde;o Administrativa
									</c:otherwise>
								</c:choose>
								<span>
									<c:choose>
										<c:when test="${f:resource('/siga.ambiente') eq 'desenv'}">
											- Ambiente de Desenvolvimento
										</c:when>
										<c:when test="${f:resource('/siga.ambiente') eq 'prod'}">
											- Ambiente Oficial
										</c:when>
										<c:when test="${f:resource('/siga.ambiente') eq 'treinamento'}">
											- Ambiente de Simulação
										</c:when>
										<c:when test="${f:resource('/siga.ambiente') eq 'configuracao'}">
											- Ambiente de Configuração
										</c:when>
										<c:when test="${f:resource('/siga.ambiente') eq 'homolog'}">
											- Ambiente de Homologação
										</c:when>
									</c:choose>
								</span>
								<span >- ${siga_version}</span>
							</div> <!-- / logo -->
						</a>
					</div>
				</div>

				<!-- /head top -->
				<!-- navbar -->
				<c:if test="${desabilitarmenu != 'sim'}">
					<div class="gt-navbar clearfix">
						<div class="gt-fixed-wrap clearfix">
							<!-- navigation -->
							<div class="gt-nav">
								<ul id="navmenu-h">
									<siga:menuprincipal />
								</ul>
							</div>
							<!-- / navigation -->
							<!-- search -->
							<c:if test="${desabilitarbusca != 'sim'}">
								<div class="gt-search">
									<div class="gt-search-inner" onclick=""
										style="-webkit-box-sizing: content-box; box-sizing: content-box;">
										<siga:selecao propriedade="buscar" tipo="generico"
											tema="simple" ocultardescricao="sim" buscar="nao"
											siglaInicial="Buscar" modulo="siga/public" urlAcao="buscar"
											urlSelecionar="selecionar"
											matricula="${titular.siglaCompleta}" />
										<script type="text/javascript">
											var lis = document.getElementsByTagName('li');

											for (var i = 0, li; li = lis[i]; i++) {
												var link = li.getElementsByTagName('a')[0];

												if (link) {
													link.onfocus = function() {
														var ul = this.parentNode.getElementsByTagName('ul')[0];
														if (ul) {
															ul.style.display = 'block';
														}
													}
													var ul = link.parentNode.getElementsByTagName('ul')[0];
													if (ul) {
														var ullinks = ul.getElementsByTagName('a');
														var ullinksqty = ullinks.length;
														var lastItem = ullinks[ullinksqty - 1];
														if (lastItem) {
															lastItem.onblur = function() {
																this.parentNode.parentNode.style.display = 'none';
																if (this.id == "relclassificados") {
																	var rel = document.getElementById("relatorios");
																	rel.style.display = 'none';
																}
															}
															lastItem.parentNode.onblur = function() {
																this.parentNode.style.display = '';
															}
														}
													}
												}
											}

											var fld = document.getElementsByName('buscar_genericoSel.sigla')[0];
											fld.setAttribute("class", "gt-search-text");
											fld.className = "gt-search-text";
											fld.onfocus = function() {
												if (this.value == 'Buscar') {
													this.value = '';
												}
											};
											fld.onblur = function() {
												if (this.value == '') {
													this.value = 'Buscar';
													return;
												}
												if (this.value != 'Buscar')
													ajax_buscar_generico();
											};
											fld.onkeypress = function(event) {
												var fid = document.getElementsByName('buscar_genericoSel.id')[0];

												event = (event) ? event : window.event
												var keyCode = (event.which) ? event.which : event.keyCode;
												if (keyCode == 13) {
													if (fid.value == null || fid.value == "") {
														fld.onblur();
													}
													return false;
												} else {
													fid.value = '';
													return true;
												}
											};

											self.resposta_ajax_buscar_generico = function(response, d1, d2, d3) {
												var sigla = document.getElementsByName('buscar_genericoSel.sigla')[0].value;
												var data = response.split(';');
												if (data[0] == '1') {
													retorna_buscar_generico(data[1], data[2], data[3]);
													if (data[1] != null && data[1] != "") {
														window.location.href = data[3];
													}
													return

																								

												}
												retorna_buscar_generico('', '', '');

												return;

											}
										</script>
									</div>
							</c:if>
						</div>
					</div>
				</c:if>
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