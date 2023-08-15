<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
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
<%@ attribute name="compatibilidade"%>
<%@ attribute name="desabilitarComplementoHEAD"%>
<%@ attribute name="incluirBS" required="false" %>

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

<c:set var="logo_topo_orgao" scope="request" value="${f:resource('/siga.logo.topo.orgao')}" />

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta name="viewport" content="width=device-width,initial-scale=1,shrink-to-fit=no">
<c:choose>
	<c:when test="${siga_cliente == 'GOVSP'}">
		<meta name="theme-color" content="#35b44a">
	</c:when>
	<c:otherwise>
		<meta name="theme-color" content="#007bff">
	</c:otherwise>
</c:choose>
<title>
<c:choose>
	<c:when test="${siga_cliente == 'GOVSP'}">
		SP Sem Papel - ${titulo_pagina}
	</c:when>
	<c:otherwise>
		SIGA - ${titulo_pagina}	
	</c:otherwise>
</c:choose>
</title>
<meta http-equiv="X-UA-Compatible" content="${XUACompatible}" />
<META HTTP-EQUIV="Expires" CONTENT="0">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="content-type" CONTENT="text/html; charset=UTF-8">
${meta}

<c:set var="path" scope="request">${pageContext.request.contextPath}</c:set>

<c:if test="${empty incluirBS or incluirBS}" >
 	<link rel="stylesheet" href="/siga/bootstrap/css/bootstrap.min.css?v=4.1.1"	type="text/css" media="screen, projection, print" />
</c:if> 


<!--   <link rel="stylesheet" href="/siga/css/menuhover.css" type="text/css"/> -->

<!-- 
<link rel="stylesheet" href="/siga/css/ecoblue/css/ecoblue.css"
	type="text/css" media="screen, projection">
 -->

<link rel="stylesheet" href="/siga/css/ecoblue/css/custom-bs4.css"
	type="text/css" media="screen, projection">
	
	
<script src="/siga/public/javascript/jquery/jquery-1.11.2.min.js" type="text/javascript"></script>
	
<link rel="stylesheet" href="/siga/fontawesome/css/all.css"	type="text/css" />

<c:set var="collapse_Expanded" scope="request" value="collapsible expanded" />

<c:set var="siga_version"  scope="request" value="${f:sigaVersion()}" />

<c:set var="siga_cliente_sso" scope="request" value="${f:resource('/siga.integracao.sso')}" />
<c:set var="siga_cliente_sso_btn_txt" scope="request" value="${f:resource('/siga.integracao.sso.nome')}" /> 
  
<c:choose>
	<c:when test="${siga_cliente == 'GOVSP'}">
		<meta name="theme-color" content="#35b44">
		<link rel="stylesheet" href="/siga/css/style_siga_govsp.css?v=1681243175" type="text/css" media="screen, projection">
		
		<c:set var="body_color" value="body_color_govsp" scope="request" />
		
		<c:set var="thead_color" value="thead-dark" scope="request" />
		
		<c:if test="${desabilitarmenu == 'sim'}">
			<c:set var="body_color" value="login_body_color" scope="request" />
		</c:if>
												
		<c:set var="ico_siga" value="sem-papel.ico" />
		<c:set var="menu_class" value="menusp" />
		<c:set var="sub_menu_class" value="submenusp" />
		<c:set var="ambiente_class" value="ambiente_class" />
		<c:set var="navbar_class" value="navbar-light" />
		<c:set var="navbar_logo" value="/siga/imagens/logo-sem-papel-cor.png" /> 
		<c:set var="navbar_logo_size" value="50" />
		<c:set var="button_class_busca" value="btn-primary" />
		<c:set var="collapse_Tramitacao" scope="request" value="collapsible closed" />
		<c:set var="collapse_NivelAcesso" scope="request" value="collapsible closed" />
		<c:set var="collapse_ArqAuxiliares" scope="request" value="not collapsible" />
		<c:set var="hide_only_GOVSP" scope="request"> d-none </c:set>
		<c:set var="hide_only_TRF2" scope="request"> </c:set>
		<c:set var="uri_logo_siga_pequeno" value="${f:resource('/siga.base.url')}/siga/imagens/logo-sem-papel-150x70.png" scope="request" />
	</c:when>
	<c:otherwise>
		<meta name="theme-color" content="bg-primary">
		<c:set var="body_color" value="body_color_default" scope="request" />
		<c:set var="thead_color" value="thead-light" scope="request" />
									
		<c:set var="ico_siga" value="siga.ico" />
		<c:set var="menu_class" value="bg-primary" /> 
		<c:set var="sub_menu_class" value="bg-secondary text-white" />
		
		<c:set var="navbar_class" value="navbar-dark bg-primary" />
		<c:if test="${f:resource('/siga.ambiente') != 'prod'}">
			<c:set var="navbar_class" value="navbar-dark bg-secondary" />
		</c:if>
		
		<c:set var="navbar_logo" value="/siga/imagens/logo-siga-novo-38px.png" />
		<c:set var="navbar_logo2" value="${f:resource('/siga.cabecalho.logo')}" />
		<c:set var="navbar_logo_size" value="38" />
		<c:set var="button_class_busca" value="btn-outline-light" />
		<c:set var="collapse_Tramitacao" scope="request" value="collapsible expanded" />
		<c:set var="collapse_NivelAcesso" scope="request" value="collapsible expanded" />
		<c:set var="collapse_ArqAuxiliares" scope="request" value="not collapsible" />
		<c:set var="hide_only_GOVSP" scope="request"> </c:set>
		<c:set var="hide_only_TRF2" scope="request"> d-none </c:set>
		<c:set var="uri_logo_siga_pequeno" value="${f:resource('/siga.base.url')}/siga/imagens/logo-siga-140x40.png" scope="request" />		
	</c:otherwise>
</c:choose>

<link rel="stylesheet" href="/siga/css/style_siga.css?v=1681243175" type="text/css" media="screen, projection">
<link rel="shortcut icon" href="/siga/imagens/${ico_siga}" />


<c:catch>
	<c:if test="${desabilitarComplementoHEAD != 'sim'}">
		<c:if test="${not empty titular}">
			${f:getComplementoHead(cadastrante.orgaoUsuario)}
		</c:if>
	</c:if>
</c:catch>
</head>


<body onload="${onLoad}" class="${body_color}">
	<c:if test="${popup!='true'}">
   		<nav id="siga-top-menu" class="navbar navbar-expand-lg ${navbar_class} ${menu_class}">
			<a class="navbar-brand pt-0 pb-0" href="/siga"> <img
				src="${navbar_logo}" height="${navbar_logo_size}">
			</a>
			
			<c:if test="${siga_cliente != 'GOVSP'}">
				<c:choose>
				<c:when test="${not empty logo_topo_orgao}">
					<img id="logo-header2"
					 src="${logo_topo_orgao}"
					 alt="Logo do Órgão" height="38" class="ml-2" />
				</c:when>
				<c:otherwise>
					<img id="logo-header2"
					 src="${navbar_logo2}"
				 	 alt="${f:resource('siga.cabecalho.titulo')}" height="${navbar_logo_size}" class="ml-2" />
				</c:otherwise>
				</c:choose>
			</c:if>

			<c:if test="${popup ne 'somenteComLogo'}">
				<button class="navbar-toggler" type="button" data-toggle="collapse"
					data-target="#navbarSupportedContent"
					aria-controls="navbarSupportedContent" aria-expanded="false"
					aria-label="Toggle navigation">
					<span class="navbar-toggler-icon"></span>
				</button>
				<c:if test="${siga_cliente != 'GOVSP' or f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA')}">
					<div class="collapse navbar-collapse" id="navbarSupportedContent">
						<c:if test="${desabilitarmenu != 'sim'}">
							<ul class="navbar-nav mr-auto">
								<!-- navigation -->
								<siga:menuprincipal />
								<!-- / navigation -->
							</ul>
		
							<!-- search -->
							<c:if test="${desabilitarbusca != 'sim'}">
								<form class="form-inline my-2 my-lg-0">
									<siga:selecao propriedade="buscar" tipo="generico" tema="simple"
										ocultardescricao="sim" buscar="nao" siglaInicial=""
										modulo="siga/public" urlAcao="buscar" urlSelecionar="selecionar"
										matricula="${titular.siglaCompleta}" />
									<button class="btn ${button_class_busca} ml-2 my-2 my-sm-0" type="button" onclick="javascript:buscarDocumentoPorCodigo();">Buscar</button>
									<c:if test="${siga_cliente eq 'GOVSP'}">
										<button id="btnTutorial" class="btn btn-outline-success ml-2 my-2 my-sm-0" type="button">Tutoriais</button>
		
										<div id="tutorialModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="tutorialModalLabel" aria-hidden="true" >
										    <div class="modal-dialog modal-dialog-centered modal-lg" style="max-width: 80% !important;">
										        <div class="modal-content" >
										            <div class="modal-header bg-success">
										            	<h5 class="modal-title text-white">Tutoriais SP Sem Papel</h5>
										                <button type="button" class="close text-white" data-dismiss="modal" aria-hidden="true">×</button>
										            </div>
											        <div class="modal-body bg-light">
										                <iframe width="100%" height="600" frameborder="0" allowfullscreen=""></iframe>
										            </div>
		
										        </div>
										    </div>
										</div>
										<script>
										    $('#btnTutorial').click(function () {
										        var src = 'https://vimeopro.com/fcav/spsempapel';
										        $('#tutorialModal').appendTo("body").modal('show');
										        $('#tutorialModal iframe').attr('src', src);
										    });
										
										    $('#tutorialModal button').click(function () {
										        $('#tutorialModal iframe').removeAttr('src');
										    });
										</script>
									</c:if>
																	
									<script type="text/javascript">
										if (false) {
											var lis = document
													.getElementsByTagName('li');
		
											for (var i = 0, li; li = lis[i]; i++) {
												var link = li.getElementsByTagName('a')[0];
		
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
										}
		
										var fld = document
												.getElementsByName('buscar_genericoSel.sigla')[0];
										fld.placeholder = 'Número de Documento';
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
													.getElementsByName('buscar_genericoSel.id')[0];
		
											event = (event) ? event : window.event
											var keyCode = (event.which) ? event.which
													: event.keyCode;
											if (keyCode == 13) {
												if (fid.value == null
														|| fid.value == "") {
													buscarDocumentoPorCodigo();
												}
												return false;
											} else {
												fid.value = '';
												return true;
											}
										};
	
										function buscarDocumentoPorCodigo() {
											if (this.value == '') {
												this.value = placeholder;
												return;
											}
											ajax_buscar_generico();
										};
		
										self.resposta_ajax_buscar_generico = function(
												response, d1, d2, d3) {
											var sigla = document
													.getElementsByName('buscar_genericoSel.sigla')[0].value;
											var data = response.split(';');
											if (data[0] == '1') {
												retorna_buscar_generico(data[1],
														data[2], data[3]);
												if (data[1] != null && data[1] != "") {
													window.location.href = data[3];
												}
												return;
											}
											retorna_buscar_generico('', '', '');
											return;
										}
									</script>
								</form>
							</c:if>
						</c:if>
					</div>
				</c:if>
			</c:if>
		</nav>

		<div class="container-fluid content">
			<div class="row pt-2 pb-2 mb-3 ${sub_menu_class}" >
				<c:if test="${popup ne 'somenteComLogo'}">
					<!-- usuário -->
					<div class="col col-12 col-md-6">
						<div class="row">
							<div class="col gt-company d-inline align-middle">
								<span class="h-100">
									<strong><span>${f:resource('/siga.cabecalho.titulo')}</span> </strong>
									 <c:catch>
											<c:if test="${not empty titular.orgaoUsuario.descricao}"><span style="white-space: nowrap;"> <i class="fa fa-angle-right"></i> ${titular.orgaoUsuario.descricao}</span></h6></c:if>
									 </c:catch>
								</span>
							</div>
						</div>
						<div class="row  ${ambiente_class}">
							<div class="col">
								<span>
									<c:choose>
										<c:when test="${f:resource('/siga.ambiente') eq 'desenv'}">
											Ambiente de Desenvolvimento
										</c:when>
										<c:when test="${f:resource('/siga.ambiente') eq 'prod'}">
											Ambiente Oficial
										</c:when>
										<c:when test="${f:resource('/siga.ambiente') eq 'treinamento'}">
											Ambiente de Simulação
										</c:when>
										<c:when test="${f:resource('/siga.ambiente') eq 'configuracao'}">
											Ambiente de Configuração
										</c:when>
										<c:when test="${f:resource('/siga.ambiente') eq 'homolog'}">
											Ambiente de Homologação
										</c:when>
									</c:choose>
								</span>
								<span >- ${siga_version}</span>
							</div>
						</div>
	
						
	
					</div>
					
					<c:if test="${not empty cadastrante}">
						<div class="col col-12 col-md-6 text-right">
							<div class="dropdown d-inline">
								<span class="align-middle">Olá, <i class="fa fa-user"></i> 
									<c:catch>
										<strong id="cadastrante" data-toggle="tooltip" data-placement="top" data-cadastrante="${cadastrante.sigla}" title="${cadastrante.sigla}">																		
												<c:out default="Convidado" value="${f:maiusculasEMinusculas(cadastrante.nomePessoa)}" />
										</strong>
										<c:if test="${not empty cadastrante.lotacao}">
											<c:if test="${cadastrante.lotacoes[1] != null}">
												<button class="btn btn-light btn-sm dropdown-toggle ml-1" type="button" id="dropdownLotaMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
											</c:if>
						 						<strong>
						 							<span style="white-space: nowrap;"><i class="fa fa-building"></i> ${cadastrante.lotacao.sigla}</span>
						 						</strong>
											<c:if test="${cadastrante.lotacoes[1] != null}">
												</button>
												<div class="dropdown-menu" style="z-index: 1040" aria-labelledby="dropdownLotaMenuButton">
													<c:forEach var="lota" items="${cadastrante.lotacoes}">
														<c:if test="${!(lota[0]==cadastrante.sigla || lota[1]==cadastrante.lotacao)}">
															<a class="dropdown-item" href="/siga/app/swapUser?username=${lota[0]}">
																${lota[1]} (${lota[0]})
																<p class="mb-0"><small>${lota[2]}</small></p>
															</a>
														</c:if>
													</c:forEach>
												</div>
											</c:if>
					 					</c:if>
									</c:catch> 
								</span>
							</div>
							<c:choose>
								<c:when test="${siga_cliente_sso and logadoviaGovBr == true and f:resource('/siga.integracao.sso.dominio.logout') != ''}">
									<c:set var="siga_cliente_sso_logout_url" scope="request" value="${f:resource('/siga.integracao.sso.dominio.logout')}?post_logout_redirect_uri=${f:resource('/siga.base.url')}" />
								</c:when>
								<c:otherwise>
									<c:set var="siga_cliente_sso_logout_url" scope="request" value="" />
								</c:otherwise>
							</c:choose>
							
							<button class="btn btn-danger btn-sm ml-3 mt-1 align-bottom" type="button" onclick="delSession();javascript:location.href='${siga_cliente_sso_logout_url}/siga/public/app/logout'"><i class="fas fa-sign-out-alt"></i> Sair</button>
							
							<div class="pt-1">
								<c:catch>
									<c:choose>
										<c:when
											test="${not empty titular && titular.idPessoa!=cadastrante.idPessoa}">Substituindo: <strong title="${titular.sigla}">${f:maiusculasEMinusculas(titular.nomePessoa)}</strong>
											<button class="btn btn-secondary btn-sm" type="button" onclick="delSession();javascript:location.href='/siga/app/substituicao/finalizar'">Finalizar</button>
										</c:when>
										<c:when
											test="${not empty lotaTitular && lotaTitular.idLotacao!=cadastrante.lotacao.idLotacao}">Substituindo: <strong title="${lotaTitular.sigla}">${f:maiusculasEMinusculas(lotaTitular.nomeLotacao)}</strong>
											<button class="btn btn-secondary btn-sm" type="button" onclick="delSession();javascript:location.href='/siga/app/substituicao/finalizar'">Finalizar</button>
										</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</c:catch>
							</div>
						</div>
					</c:if>
				</c:if>
			</div>
			<script>
				if('${mensagemConsole}' != '')
					console.log('${mensagemConsole}');
			</script>
			<div class="row ${mensagemCabec==null?'d-none':''}" id="mensagemCabecId" >
				<div class="col" >
					<div class="alert ${msgCabecClass} fade show" id="mensagemCabec" role="alert">
						${mensagemCabec}
						<button type="button" class="close" data-dismiss="alert" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>			
				</div>
			</div>
		</div>

		<div id="quadroAviso"
			style="position: absolute; font-weight: bold; padding: 4px; color: white; visibility: hidden">-</div>

	</c:if>

	<div id="carregando"
		style="position: absolute; top: 0px; right: 0px; background-color: red; font-weight: bold; padding: 4px; color: white; display: none">Carregando...</div>	
    
<script type="text/javascript" language="Javascript1.1">
setTimeout(function() {
	$('#mensagemCabec.fade-close').fadeTo(1000, 0, function() {
		$('#mensagemCabec.fade-close').slideUp(1000);
	});
}, 5000);

function delSession() {
	sessionStorage.removeItem('timeout' + document.getElementById('cadastrante').title);
	sessionStorage.removeItem('mesa' + document.getElementById('cadastrante').title);
	sessionStorage.removeItem('listaNotificacaoSilenciada' + document.getElementById('cadastrante').title);

	for (var obj in sessionStorage) {
      if (sessionStorage.hasOwnProperty(obj) && (obj.includes("pessoa.") || obj.includes("lotacao."))) {
    	  sessionStorage.removeItem(obj);
      }
	}
}
</script>		
