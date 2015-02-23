<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
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

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<html lang="pt_BR">
<head>
<meta charset="utf-8">
<title>SIGA - ${titulo_pagina}</title>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" />
<META HTTP-EQUIV="Expires" CONTENT="0">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="content-type" CONTENT="text/html; charset=UTF-8">
${meta}

<c:set var="path" scope="request">${pageContext.request.contextPath}</c:set>

<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="/siga/public/devoops/plugins/bootstrap/bootstrap.css"
	rel="stylesheet">
<link href="/siga/public/devoops/plugins/jquery-ui/jquery-ui.min.css"
	rel="stylesheet">
<link
	href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css"
	rel="stylesheet">
<link href='http://fonts.googleapis.com/css?family=Righteous'
	rel='stylesheet' type='text/css'>
<link href="/siga/public/devoops/plugins/fancybox/jquery.fancybox.css"
	rel="stylesheet">
<link href="/siga/public/devoops/plugins/fullcalendar/fullcalendar.css"
	rel="stylesheet">
<link href="/siga/public/devoops/plugins/xcharts/xcharts.min.css"
	rel="stylesheet">
<link href="/siga/public/devoops/plugins/select2/select2.css"
	rel="stylesheet">
<link
	href="/siga/public/devoops/plugins/justified-gallery/justifiedGallery.css"
	rel="stylesheet">
<link href="/siga/public/devoops/css/style_v1.css" rel="stylesheet">
<link href="/siga/public/devoops/plugins/chartist/chartist.min.css"
	rel="stylesheet">
<link href="/siga/public/css/siga.css"
	rel="stylesheet">
<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
		<script src="http://getbootstrap.com/docs-assets/js/html5shiv.js"></script>
		<script src="http://getbootstrap.com/docs-assets/js/respond.min.js"></script>
<![endif]-->

<script src="/siga/sigalibs/ajax.js" type="text/javascript"></script>
<script src="/siga/sigalibs/static_javascript.js" type="text/javascript"
	charset="utf-8"></script>
<script src="/siga/javascript/picketlink.js" type="text/javascript"
	charset="utf-8"></script>

<link rel="shortcut icon" href="/siga/sigalibs/siga.ico" />

<script src="/siga/javascript/jquery/jquery-1.11.2.min.js" type="text/javascript"></script>
<script src="/siga/javascript/jquery/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
<script src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js" type="text/javascript"></script>
<script src="/siga/javascript/json2.js" type="text/javascript"></script>
<link rel="stylesheet"
	href="/siga/javascript/jquery-ui-1.10.3.custom/css/ui-lightness/jquery-ui-1.10.3.custom.min.css"
	type="text/css" media="screen, projection">
<!-- <link rel="stylesheet" href="/siga/javascript/jquery-ui-1.10.3.custom/development-bundle/themes/base/jquery.ui.all.css"
	type="text/css" media="screen, projection"> -->
<c:if test="${not empty incluirJs}">
	<script src="/siga/javascript/${incluirJs}" type="text/javascript"></script>
</c:if>

<%-- Desabilitado porque requer o jquery 1.7 ou maior. 	
<script language="JavaScript" src="/siga/javascript/autogrow.min.js" type="text/javascript"></script>
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

<c:if test="${not empty titular}">
	${f:getComplementoHead(cadastrante.orgaoUsuario)}
</c:if>
</head>

<body onload="${onLoad}">
	<%--
	<c:if test="${popup!='true'}">
		<div class="gt-hd clearfix">
			<!-- leaf watermark -->
			<div class="gt-leaf-watermark clearfix">
				<!-- head top -->
				<div class="gt-hd-top clearfix">
					<div class="gt-fixed-wrap clearfix">
						<!-- utility box -->
						<!-- / utility box -->
						<!-- logo -->
						<a href="/siga" title="P·gina inicial" class="link-sem-estilo">
							<div class="gt-logo" style="padding: 0;">
								<img style="margin-top: 3px; margin-bottom: -13px;"
									src="/siga/imagens/logo.png">
							</div>
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
									<c:import url="/sigalibs/menuprincipal.jsp" />
								</ul>
							</div>
							<!-- / navigation -->
							<!-- search -->
							<c:if test="${desabilitarbusca != 'sim'}">
								<div class="gt-search">
									<div class="gt-search-inner" onclick="">
										<siga:selecao propriedade="buscar" tipo="generico"
											tema="simple" ocultardescricao="sim" buscar="nao"
											siglaInicial="Buscar" modulo="siga" />
										<script type="text/javascript">
											var lis = document
													.getElementsByTagName('li');

											for (var i = 0, li; li = lis[i]; i++) {
												var link = li
														.getElementsByTagName('a')[0];

												if (link) {
													link.onfocus = function() {
														var ul = this.parentNode
																.getElementsByTagName('ul')[0];
														if (ul) {
															ul.style.display = 'block';
														}
													}
													var ul = link.parentNode
															.getElementsByTagName('ul')[0];
													if (ul) {
														var ullinks = ul
																.getElementsByTagName('a');
														var ullinksqty = ullinks.length;
														var lastItem = ullinks[ullinksqty - 1];
														if (lastItem) {
															lastItem.onblur = function() {
																this.parentNode.parentNode.style.display = 'none';
																if (this.id == "relclassificados") {
																	var rel = document
																			.getElementById("relatorios");
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

											var fld = document
													.getElementById("buscar_genericoSel_sigla");
											fld.setAttribute("class",
													"gt-search-text");
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
												var fid = document
														.getElementById("buscar_genericoSel_id");

												event = (event) ? event
														: window.event
												var keyCode = (event.which) ? event.which
														: event.keyCode;
												if (keyCode == 13) {
													if (fid.value == null
															|| fid.value == "") {
														fld.onblur();
													} else {
														window.alert("1");
														window.location.href = '${request.scheme}://${request.serverName}:${request.serverPort}/sigaex/expediente/doc/exibir.action?sigla='
																+ fld.value;
													}
													return false;
												} else {
													fid.value = '';
													return true;
												}
											};

											self.resposta_ajax_buscar_generico = function(
													response, d1, d2, d3) {
												var sigla = document
														.getElementsByName('buscar_genericoSel.sigla')[0].value;
												var data = response.split(';');
												if (data[0] == '1') {
													retorna_buscar_generico(
															data[1], data[2],
															data[3]);
													if (data[1] != null
															&& data[1] != "") {
														window.location.href = data[3];
													}
													return
												}
												retorna_buscar_generico('', '',
														'');

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
 --%>

	<div id="carregando"
		style="position: absolute; top: 0px; right: 0px; background-color: red; font-weight: bold; padding: 4px; color: white; display: none">Carregando...</div>



















	<!--Start Header-->
	<div id="screensaver">
		<canvas id="canvas"></canvas>
		<i class="fa fa-lock" id="screen_unlock"></i>
	</div>
	<div id="modalbox">
		<div class="devoops-modal">
			<div class="devoops-modal-header">
				<div class="modal-header-name">
					<span>Basic table</span>
				</div>
				<div class="box-icons">
					<a class="close-link"> <i class="fa fa-times"></i>
					</a>
				</div>
			</div>
			<div class="devoops-modal-inner"></div>
			<div class="devoops-modal-bottom"></div>
		</div>
	</div>
	<header class="navbar">
		<div class="container-fluid expanded-panel">
			<div class="row">
				<div id="logo" class="col-xs-12 col-sm-2">
					<a href="/siga/principal.action">SIGA</a>
					<%--
					<div class="gt-company">
						<strong>${f:resource('siga.cabecalho.titulo')} <c:catch>
								<c:if test="${not empty titular.orgaoUsuario.descricao}">- ${titular.orgaoUsuario.descricao}</c:if>
							</c:catch>
						</strong>
					</div>
					<div class="gt-version">
						Sistema Integrado de Gest&atilde;o Administrativa
						<c:if test="${not empty env}"> - <span style="color: red">${env}</span>
						</c:if>
					</div>
					<!-- / logo -->
 --%>
				</div>
				<div id="top-panel" class="col-xs-12 col-sm-10">
					<div class="row">
						<div class="col-xs-8 col-sm-4">
							<div id="search">
								<input type="text" placeholder="pesquisar" /> <i
									class="fa fa-search"></i>
							</div>
						</div>
						<c:if test="${not empty cadastrante}">
							<div class="col-xs-4 col-sm-8 top-panel-right">
								<ul class="nav navbar-nav pull-right panel-menu">
									<li class="hidden-xs"><a href="index.html"
										class="modal-link"> <i class="fa fa-bell"></i> <span
											class="badge">7</span>
									</a></li>
									<li class="hidden-xs"><a class="ajax-link"
										href="ajax/calendar.html"> <i class="fa fa-calendar"></i>
											<span class="badge">7</span>
									</a></li>
									<li class="hidden-xs"><a href="ajax/page_messages.html"
										class="ajax-link"> <i class="fa fa-envelope"></i> <span
											class="badge">7</span>
									</a></li>


									<li class="dropdown"><a href="#"
										class="dropdown-toggle account" data-toggle="dropdown">
											<div class="avatar">
												<img src="/siga/public/devoops/img/avatar.jpg"
													class="img-circle" alt="avatar" />
											</div> <i class="fa fa-angle-down pull-right"></i>
											<div class="user-mini pull-right">
												<span class="welcome">Ol·,</span> <span><c:catch>
														<c:out default="Convidado"
															value="${cadastrante.primeiroNomeEIniciais}" />
														<c:choose>
															<c:when test="${not empty cadastrante.lotacao}">
						 - ${cadastrante.lotacao.sigla}</c:when>
														</c:choose>
													</c:catch></span>
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
											</div>
									</a>
										<ul class="dropdown-menu">
											<li><a href="/siga/trocar_senha.action"> <i
													class="fa fa-user"></i> <span>Trocar senha</span></a></li>

											<li><a href="/siga/substituicao/substituir.action">
													<i class="fa fa-user"></i> <span>Entrar como
														substituto</span>
											</a></li>

											<li class="dropdown"><a href="#" class="dropdown-toggle">
													<i class="fa fa-dashboard"></i> <span class="hidden-xs">Substituir</span>
											</a>
												<ul class="dropdown-menu">
													<c:forEach var="substituicao" items="${meusTitulares}">
														<li><a
															style="border-left: 0px; float: right; padding-left: 0.5em; padding-right: 0.5em;"
															href="javascript:if (confirm('Deseja excluir substituição?')) location.href='/siga/substituicao/excluir.action?id=${substituicao.idSubstituicao}&porMenu=true';">
																<img style="display: inline;"
																src="/siga/css/famfamfam/icons/cancel_gray.png"
																title="Excluir"
																onmouseover="this.src='/siga/css/famfamfam/icons/cancel.png';"
																onmouseout="this.src='/siga/css/famfamfam/icons/cancel_gray.png';">
														</a> <a
															href="/siga/substituicao/substituir_gravar.action?idTitular=${substituicao.titular.idPessoa}&idLotaTitular=${substituicao.lotaTitular.idLotacao}">
																<c:choose>
																	<c:when test="${not empty substituicao.titular}">
						${f:maiusculasEMinusculas(substituicao.titular.nomePessoa)}
					</c:when>
																	<c:otherwise>
						${f:maiusculasEMinusculas(substituicao.lotaTitular.nomeLotacao)}
					</c:otherwise>
																</c:choose>
														</a></li>
													</c:forEach>
												</ul></li>

											<li><a href="#"> <i class="fa fa-cog"></i> <span>Configurações</span>
											</a></li>
											<li><a
												href="${pageContext.request.contextPath}/?GLO=true"> <i
													class="fa fa-power-off"></i> <span>Sair</span>
											</a></li>
										</ul></li>
							</div>
						</c:if>
					</div>
				</div>
			</div>

		</div>
	</header>
	<!--End Header-->
	<!--Start Container-->
	<div id="main" class="container-fluid">
		<div class="row">
			<div id="sidebar-left" class="col-xs-2 col-sm-2">
				<ul class="nav main-menu">
					<c:import url="/sigalibs/menuprincipal.jsp" />
				</ul>
			</div>
			<!--Start Content-->
			<div id="content" class="col-xs-12 col-sm-10">
				<%--				<div class="preloader">
					<img src="/siga/public/devoops/img/devoops_getdata.gif"
						class="devoops-getdata" alt="preloader" />
				</div>
 --%>
				<div id="ajax-content">
