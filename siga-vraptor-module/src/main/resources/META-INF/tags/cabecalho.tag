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
<meta name="viewport" content="width=device-width,initial-scale=1,shrink-to-fit=no">
<c:choose>
	<c:when test="${siga_cliente == 'GOVSP'}">
		<meta name="theme-color" content="#35b44a">
	</c:when>
	<c:otherwise>
		<meta name="theme-color" content="#007bff">
	</c:otherwise>
</c:choose>
<title>SIGA - ${titulo_pagina}</title>
<meta http-equiv="X-UA-Compatible" content="${XUACompatible}" />
<META HTTP-EQUIV="Expires" CONTENT="0">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="content-type" CONTENT="text/html; charset=UTF-8">
${meta}

<c:set var="path" scope="request">${pageContext.request.contextPath}</c:set>

<link rel="stylesheet" href="/siga/bootstrap/css/bootstrap.min.css"	type="text/css" media="screen, projection" />

<!-- 
<link rel="stylesheet" href="/siga/css/ecoblue/css/ecoblue.css"
	type="text/css" media="screen, projection">
 -->

<link rel="stylesheet" href="/siga/css/ecoblue/css/custom.css"
	type="text/css" media="screen, projection">
	
	
<script src="/siga/public/javascript/jquery/jquery-1.11.2.min.js" type="text/javascript"></script>
	
<link rel="stylesheet" href="/siga/fontawesome/css/all.css"	type="text/css" />

<c:set var="collapse_Expanded" scope="request" value="collapsible expanded" />
	
<c:choose>
	<c:when test="${siga_cliente == 'GOVSP'}">
		<meta name="theme-color" content="#35b44">
		<link rel="stylesheet" href="/siga/css/style_siga_govsp.css" type="text/css" media="screen, projection">
		
		<c:set var="body_color" value="body_color" scope="request" />
		
		<c:if test="${desabilitarmenu == 'sim'}">
			<c:set var="body_color" value="login_body_color" scope="request" />
		</c:if>
		
		<c:set var="ico_siga" value="siga-doc.ico" />
		<c:set var="menu_class" value="menusp" />
		<c:set var="sub_menu_class" value="submenusp" />
		<c:set var="navbar_class" value="navbar-light" />
		<c:set var="navbar_logo" value="logo-sem-papel-cor.png" />
		<c:set var="navbar_logo_size" value="50" />
		<c:set var="button_class_busca" value="btn-primary" />
		<c:set var="collapse_Tramitacao" scope="request" value="collapsible closed" />
		<c:set var="collapse_NivelAcesso" scope="request" value="collapsible closed" />
		<c:set var="collapse_ArqAuxiliares" scope="request" value="not collapsible" />
		<c:set var="hide_only_GOVSP" scope="request"> d-none </c:set>
		<c:set var="hide_only_TRF2" scope="request"> </c:set>
	</c:when>
	<c:otherwise>
		<meta name="theme-color" content="bg-primary">
		<c:set var="ico_siga" value="siga.ico" />
		<c:set var="menu_class" value="bg-primary" /> 
		<c:set var="sub_menu_class" value="bg-secondary text-white" />
		
		<c:set var="navbar_class" value="navbar-dark bg-primary" />
		<c:if test="${f:resource('isBaseTest')}">
			<c:set var="navbar_class" value="navbar-dark bg-secondary" />
		</c:if>
		
		<c:set var="navbar_logo" value="logo-siga-novo-38px.png" />
		<c:set var="navbar_logo_size" value="38" />
		<c:set var="button_class_busca" value="btn-outline-light" />
		<c:set var="collapse_Tramitacao" scope="request" value="collapsible expanded" />
		<c:set var="collapse_NivelAcesso" scope="request" value="collapsible expanded" />
		<c:set var="collapse_ArqAuxiliares" scope="request" value="not collapsible" />
		<c:set var="hide_only_GOVSP" scope="request"> </c:set>
		<c:set var="hide_only_TRF2" scope="request"> d-none </c:set>
	</c:otherwise>
</c:choose>

<link rel="stylesheet" href="/siga/css/style_siga.css" type="text/css" media="screen, projection">
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
   		<nav class="navbar navbar-expand-lg ${navbar_class} ${menu_class}">
			<a class="navbar-brand pt-0 pb-0" href="/siga"> <img
				src="/siga/imagens/${navbar_logo}" height="${navbar_logo_size}">
			</a>
			
			<c:if test="${siga_cliente != 'GOVSP'}">
				<img id="logo-header2"
					 src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABsAAAAmCAYAAAA1MOAmAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAC4jAAAuIwF4pT92AAAAB3RJTUUH4QUPFCQBHI8nPQAAA2pJREFUWMO9l0uIllUYx3/P+DkalpO3RU1hoQ7jpUDSTS1Gogi6EAm1icxFi4aKFkHShSgolAgqyhYhtGhX1sKwoqhFg1BYQS1EjJpoSiq7OEmTM47+WvS8cnqZyzvTNz3wcs57zvme/3n/z/WDWYi6RO3l/xD1UnVopr/rmCXeGWDZnIKpZ6ezuWHHTIAiAnUrcF4FWFygbTaqxuv9R9aoJ9SuuQK6Ux0pwH5Wn27716l3+W9Zo36n/qVubgrY1GY3197HgdPAQmC/uh1YpXapi/PpUqP8Uash2Kna+xDwFXAJsAJ4BTgGjNTOfabeCpyJiMY0vlGjsaWuV0edXl5UO2dis/drChbm+lXqFw0AP1CXRQOgxcBBoKdai+SkiL2NwOa05WRyqDWVu6fOrcDqYnt/DWgNsBHozkDvzHG0br/pvmqbOlbQcUK9oLZ/vEbZM+omdUPxrFcXoS5K7rfk05cBfLBQcEodUvsKoC01kGPq5RPk0bMSajdwExC1rD5W2OAX4JOI+C0VzQM+Aq4sYzEi9lX0TiSt5Pt24OQkbP4KHADeK258IXBxcWYwIvYVdp7ULi11dwP3Hc2Mj7oyaa1kd6MSExHjEXEP8NA0ZzuBveraTFWljDUBaxVuvEvtAPprxbETWJJjAHuAG9tSSiZYP0e9Tj1U0HZRZv1KXmpbLcv5UvXbWokx43Ddf24LSs9Kt3++2J6XntoXEYfmoj3YnF/Tm7T2zGW/2JtgK9QFM20JWjPEq3gdjoixqbJFO5vUmDZbtAFsOJ+Ohh7cLL6mULaqqtQz7oizAPaq36iDxfhj1qsHM6a+Vr8EromIkxnY3+fZQfVAxuIj6h/pSG+pS+u3Xa7ep+5Ux9W38/0y9eUskNvU11PJberanO/Ns9vVc9Uj6gPqh7l/72Qcb1D/VB8t1vaogzm/Wj2t7lB7Ull/7eLzc7wl9+8/6/oNPEpgufoccANwGHgVOD/371A3AT8AT0VE1Xv0Zx8yUIVIU28czwZ0NfBxRBwtPHIk09Zw8c9mB3At8HBEfD6Zp01H4wtJzUp1Xc7vrumo6Hu8aCEmjLOO7N/n1+pZ5eqv5fhEsb+gAOoG3szieoX6LrBzsnR1FNgFvFOs7QU+zYwxoD4GHAd+Ap4FBoqzvwNPZsNUXf5Itfk35wXnXBwGnuIAAAAASUVORK5CYII="
				 	 alt="Logo TRF2" height="38" class="ml-2" />
			</c:if>
			
			<button class="navbar-toggler" type="button" data-toggle="collapse"
				data-target="#navbarSupportedContent"
				aria-controls="navbarSupportedContent" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<c:if test="${siga_cliente != 'GOVSP' or f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA')}">
				<div class="collapse navbar-collapse" id="navbarSupportedContent">
					<ul class="navbar-nav mr-auto">
						<!-- navigation -->
						<siga:menuprincipal />
						<!-- / navigation -->
					</ul>
	
	
					<c:if test="${desabilitarmenu != 'sim'}">
						<!-- search -->
						<c:if test="${desabilitarbusca != 'sim'}">
							<form class="form-inline my-2 my-lg-0">
								<siga:selecao propriedade="buscar" tipo="generico" tema="simple"
									ocultardescricao="sim" buscar="nao" siglaInicial=""
									modulo="siga/public" urlAcao="buscar" urlSelecionar="selecionar"
									matricula="${titular.siglaCompleta}" />
								<button class="btn ${button_class_busca} ml-2 my-2 my-sm-0" type="button" onclick="javascript:buscarDocumentoPorCodigo();">Buscar</button>
								<button id="link" class="btn btn-outline-success ml-2 my-2 my-sm-0" type="button">Tutoriais</button>

								<div id="tutorialModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="tutorialModalLabel" aria-hidden="true" >
								    <div class="modal-dialog modal-dialog-centered modal-lg" style="max-width: 80% !important;">
								        <div class="modal-content" >
								            <div class="modal-header bg-success">
								            	<h5 class="modal-title text-white" id="exampleModalLongTitle">Tutoriais SP Sem Papel</h5>
								                <button type="button" class="close text-white" data-dismiss="modal" aria-hidden="true">×</button>
								            </div>
									        <div class="modal-body bg-light">
								                <iframe width="100%" height="600" frameborder="0" allowfullscreen=""></iframe>
								            </div>

								        </div>
								    </div>
								</div>
								<script>
								    $('#link').click(function () {
								        var src = 'https://vimeopro.com/fcav/spsempapel';
								        $('#tutorialModal').modal('show');
								        $('#tutorialModal iframe').attr('src', src);
								    });
								
								    $('#tutorialModal button').click(function () {
								        $('#tutorialModal iframe').removeAttr('src');
								    });
								</script>
								

								
								
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
									fld.onblur = '';
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
		</nav>

		<div class="container-fluid content">
			<div class="row pt-2 pb-2 mb-3 ${sub_menu_class}" >
				<!-- usuário -->
				<div class="col col-12 col-sm-4">
					<div class="gt-company d-inline align-middle">
						<span class="h-100">
							<strong><span>${f:resource('siga.cabecalho.titulo')}</span> </strong>
							 <c:catch>
									<c:if test="${not empty titular.orgaoUsuario.descricao}"><span style="white-space: nowrap;"> <i class="fa fa-angle-right"></i> ${titular.orgaoUsuario.descricao}</span></h6></c:if>
							 </c:catch>
						</span>
					</div>
					
					<!-- 
					<div class="gt-version">
						Sistema Integrado de Gest&atilde;o Administrativa
						<c:if test="${not empty env}"> - <span style="color: red">${env}</span>
						</c:if>
					</div>
					 -->
				</div>
				<div class="col col-12 col-sm-4 text-center">
					<span class="h3 text-success align-middle ${hide_only_TRF2}">
						<c:choose>
							<c:when test="${f:resource('ambiente') eq 'prod'}">
								Ambiente Oficial
							</c:when>
							<c:when test="${f:resource('ambiente') eq 'treinamento'}">
								Ambiente de Simulação
							</c:when>
							<c:when test="${f:resource('ambiente') eq 'homolog'}">
								Ambiente de Homologação
							</c:when>
						</c:choose>
					</span>
				</div>
				<c:if test="${not empty cadastrante}">
					<div class="col col-12 col-sm-4 text-right">
						<div class="dropdown d-inline">
							<span class="align-middle">Olá, <i class="fa fa-user"></i> 
								<c:catch>
									<strong data-toggle="tooltip" data-placement="top" title="${cadastrante.sigla}">									
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
											<div class="dropdown-menu" aria-labelledby="dropdownLotaMenuButton">
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
						<button class="btn btn-danger btn-sm ml-3 mt-1 align-bottom" type="button" onclick="javascript:location.href='/siga/public/app/logout'"><i class="fas fa-sign-out-alt"></i> Sair</button>
						<div class="d-inline">
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
						</div>
					</div>
				</c:if>
			</div>
			<div class="row ${mensagemCabec==null?'d-none':''}" >
				<div class="col" >
					<div class="alert ${msgCabecClass} fade show" role="alert">
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