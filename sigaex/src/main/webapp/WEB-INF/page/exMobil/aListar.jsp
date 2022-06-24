<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>

<siga:pagina titulo="Lista de Expedientes" popup="${popup}">
	<script type="text/javascript" language="Javascript1.1">
		function alteraTipoDaForma(abrir) {
			if ($('#idFormaDoc-spinner').hasClass('d-none')) {
				$('#idFormaDoc-spinner').removeClass('d-none');
				$('#idFormaDoc').prop('disabled', 'disabled');
				SetInnerHTMLFromAjaxResponse(
						'/sigaex/app/expediente/doc/carregar_lista_formas?tipoForma='
								+ document.getElementById('tipoForma').value
								+ '&idFormaDoc=' + '${idFormaDoc}', document
								.getElementById('comboFormaDiv'), null, 
								(abrir? function(){	$('#idFormaDoc').select2('open'); } : null)							
								);
			}
		}
	
		function alteraForma(abrir) {
			if ($('#idMod-spinner').hasClass('d-none')) {
				$('#idMod-spinner').removeClass('d-none');
				$('#idMod').prop('disabled', 'disabled');
				var idFormaDoc = document.getElementById('idFormaDoc');
				SetInnerHTMLFromAjaxResponse(
						'/sigaex/app/expediente/doc/carregar_lista_modelos?forma='
								+ (idFormaDoc != null ? idFormaDoc.value : '${idFormaDoc}' )
								+ '&idMod='	+ '${idMod}', document
								.getElementById('comboModeloDiv'), null, 
								(abrir? function(){	$('#idMod').select2('open'); 
													podeDescricao(true);} : 
										function(){	podeDescricao(true);})							
								);
			}
		}
		
		function sbmtAction(id, action) {
			var frm = document.getElementById(id);
			frm.action = action;
			frm.submit();
			return;
		}
	
		function sbmt(offset) {
			sigaSpinner.mostrar();
			if (offset == null) {
				offset = 0;
			}
			listar["paramoffset"].value = offset;
			listar["p.offset"].value = offset;
			listar.submit();
		}
		
		function submitBusca() {
			if(${siga_cliente == 'GOVSP'}) {
				sigaSpinner.mostrar();
				var descricao = document.getElementById('descrDocumento').value.trim();
				if(descricao.length != 0 && descricao.length < 5) {
					sigaSpinner.ocultar();
					sigaModal.alerta("Preencha no mínimo 5 caracteres no campo descrição");
					return;
				}
			}
			$('#buscandoSpinner').removeClass('d-none');
			document.getElementById("btnBuscar").disabled = true;
			listar.submit();
		}
	</script>
	
	<link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css" media="screen, projection" />
	
	<div id="inicio" class="container-fluid content mb-3">
		<h5 id="pesqTitle">Pesquisar Documentos</h5>
		<c:set var="formOrigem" value="lista" scope="request"/>
		<c:if
			test="${((empty primeiraVez) or (primeiraVez != 'sim')) and ((empty apenasRefresh) or (apenasRefresh != 1)) and !pesquisaLimitadaPorData}">
			<c:choose>
				<c:when test="${siga_cliente != 'GOVSP'}">
					<jsp:include page="./lista.jsp"/>
				</c:when>
				<c:otherwise>
					<jsp:include page="./listaSP.jsp"/>
				</c:otherwise>
			</c:choose>
		</c:if>	
		<form id="listar" name="listar" onsubmit="javascript: return limpaCampos()" action="listar" method="get" class="form100">
			<input type="hidden" name="popup" value="${popup}" /> 
			<input type="hidden" name="propriedade" value="${propriedade}" /> 
			<input type="hidden" name="postback" value="1" /> 
			<input type="hidden" name="apenasRefresh" value="0" /> 
			<input type="hidden" name="paramoffset" value="0" /> 
			<input type="hidden" name="p.offset" value="0" /> 
			<input type="hidden" id="limiteDias" name="limiteDias" value="${f:resource('/siga.pesquisa.limite.dias')}" />
			
			<c:if test="${ehPublicoExterno}">
				<input type="hidden" name="ultMovIdEstadoDoc" name="ultMovIdEstadoDoc" value="${ultMovIdEstadoDoc}" />
			</c:if>
			
			<c:if test="${not ehPublicoExterno}">
				<div class="card bg-light ${pesquisaLimitadaPorData? 'sticky-top':''}">
					<jsp:include page="./headerMeses.jsp"/>
					<div id="pesqFiltros" class="card-body collapse show">
						<div class="tab-content" id="nav-tabContent">
							<div class="tab-pane fade show active" id="content1">
									<c:if test="${siga_cliente == 'GOVSP'}">
										<div class="form-row">
											<div class="form-group col-12">
												<label for="descrDocumento"><fmt:message key="documento.descricao"/></label> <input
													class="form-control" type="text" name="descrDocumento" id="descrDocumento"
													value="${descrDocumento}" size="80"
													<c:if test="${!podePesquisarDescricao}">
														readonly placeholder="Não é possível realizar a pesquisa pela descrição"
													</c:if>
													/>
													<c:if test="${podePesquisarDescricao && podePesquisarDescricaoLimitada}">
														<small>Campo "Descrição" habilitado para pesquisa após o preenchimento dos campos "Órgão", "Espécie" e "Ano de Emissão"</small>
													</c:if>
											</div>
										</div>
									</c:if>
									<div class="form-row">
										<div class="form-group col-md-6">
											<label for="ultMovIdEstadoDoc">Situação</label> <select
												class="form-control" id="ultMovIdEstadoDoc"
												name="ultMovIdEstadoDoc">
												<option value="0">[Todos]</option>
												<c:forEach items="${estados}" var="item" varStatus="sts">
													<c:if test="${sts.first or (item.idFinalidade.grupo != g)}">
														<c:if test="${not sts.first}">
															</optgroup>
														</c:if>
														<optgroup label="${item.idFinalidade.grupo.nome}">
														<c:set var="g" value="${item.idFinalidade.grupo}"/>
													</c:if>
													<option value="${item.idMarcador}"
														${item.idMarcador == ultMovIdEstadoDoc ? 'selected' : ''}>
														${item.descrMarcador}</option>
												</c:forEach>
												</optgroup>
											</select>
										</div>
				
										<div class="form-group col-md-2">
											<label for="ultMovTipoResp"><fmt:message key="usuario.pessoa"/>/<fmt:message key="usuario.lotacao"/></label> 
											<select
												class="form-control" id="ultMovTipoResp" name="ultMovTipoResp"
												onchange="javascript:alteraAtendente();">
												<c:forEach items="${listaTipoResp}" var="item">
													<option value="${item.key}"
														${item.key == ultMovTipoResp ? 'selected' : ''}>
														${item.value}</option>
												</c:forEach>
											</select>
										</div>
										<c:if test="${ultMovTipoResp == 1}">
											<div id="divUltMovResp" style="display:"
												class="form-group col-md-4">
												<label for="ultMovTipoResp"><fmt:message key="tela.pesquisa.pessoa"/></label>
												<siga:selecao propriedade="ultMovResp" tema="simple"
													paramList="buscarFechadas=true" modulo="siga" />
											</div>
											<div id="divUltMovLotaResp" style="display: none"
												class="form-group col-md-4">
												<label for="ultMovTipoResp"><fmt:message key="usuario.lotacao"/></label>
												<siga:selecao propriedade="ultMovLotaResp" tema="simple"
													paramList="buscarFechadas=true" modulo="siga" />
											</div>
										</c:if>
										<c:if test="${ultMovTipoResp == 2}">
											<div id="divUltMovResp" style="display: none"
												class="form-group col-md-4">
												<label for="ultMovTipoResp"><fmt:message key="tela.pesquisa.pessoa"/></label>
												<siga:selecao propriedade="ultMovResp" tema="simple"
													paramList="buscarFechadas=true" modulo="siga" />
											</div>
											<div id="divUltMovLotaResp" style="display:"
												class="form-group col-md-4">
												<label for="ultMovTipoResp"><fmt:message key="usuario.lotacao"/></label>
												<siga:selecao propriedade="ultMovLotaResp" tema="simple"
													paramList="buscarFechadas=true" modulo="siga" />
											</div>
										</c:if>
									</div>
				
									<div class="form-row">
										<div class="form-group col-md-3">
											<label for="orgaoUsu">Órgão</label> 
											<select class="form-control  siga-select2" id="orgaoUsu" name="orgaoUsu" onchange="podeDescricao(true)">
												<c:if test="${siga_cliente != 'GOVSP' || orgaoUsu == '0'}">
													<option value="0">[Todos]</option>
												</c:if>
												<c:forEach items="${orgaosUsu}" var="item">
													<option value="${item.idOrgaoUsu}"
														${item.idOrgaoUsu == orgaoUsu ? 'selected' : ''}>
														${item.nmOrgaoUsu}</option>
												</c:forEach>
											</select>
										</div>
										<c:if test="${siga_cliente != 'GOVSP'}">
											<div class="form-group col-md-3">
												<label for="idTpDoc">Origem</label> <select class="form-control"
													id="idTpDoc" name="idTpDoc"
													onchange="javascript:alteraOrigem();">
												<option value="0">[Todos]</option>
												<c:forEach items="${tiposDocumento}" var="item">
													<option value="${item.idTpDoc}"
														${item.idTpDoc == idTpDoc ? 'selected' : ''}>
														${item.descrTipoDocumento}</option>
												</c:forEach>
												</select>
											</div>
										</c:if>
										<div class="form-group col-md-3 ${pesquisaLimitadaPorData? 'd-none':''}">
											<label for="dtDocString">Data Inicial</label> <input
												class="form-control campoData" placeholder="dd/mm/aaaa" autocomplete="off" 
												type="text" name="dtDocString" id="dtDocString" value="${dtDocString}"
												onblur="javascript:verifica_data(this,0);" />
										</div>
										<div class="form-group col-md-3 ${pesquisaLimitadaPorData? 'd-none':''}">
											<label for="dtDocFinalString">Data Final</label> <input
												class="form-control campoData" placeholder="dd/mm/aaaa" autocomplete="off" 
												type="text" name="dtDocFinalString"	id="dtDocFinalString" value="${dtDocFinalString}"
												onblur="javascript:verifica_data(this,0);" />
										</div>
									</div>
									
									<div id="trTipo" class="form-row">
										<div class="form-group col-md-3 ${hide_only_GOVSP}">
											<label for="tipoForma">Tipo da Espécie</label> 
											<select
												class="form-control" id="tipoForma" name="idTipoFormaDoc"
												onchange="javascript:alteraTipoDaForma(false);">
												<option value="0">[Todos]</option>
												<c:forEach items="${tiposFormaDoc}" var="item">
													<option value="${item.idTipoFormaDoc}"
														${item.idTipoFormaDoc == idTipoFormaDoc ? 'selected' : ''}>
														${item.descTipoFormaDoc}</option>
												</c:forEach>
											</select>
										</div>
				
										<div class="form-group col-md-3">
											<div style="display: inline" id="comboFormaDiv">
												<div class="form-group" id="idFormaDocGroup" onclick="javascript:alteraTipoDaForma(true);"
												 		onfocus="javascript:alteraTipoDaForma(true);">
													<label><fmt:message key="documento.label.especie"/> <span id="idFormaDoc-spinner" class="spinner-border text-secondary d-none"></span></label> 
													<select class="form-control siga-select2" id="idFormaDoc" name="idFormaDoc">
														<option value="0">[Todos]</option>
													</select>
												</div>
											</div>
										</div>
				
										<div class="form-group col-md-6">
											<div style="display: inline" id="comboModeloDiv">
												<div class="form-group" id="idModGroup" onclick="javascript:alteraForma(true);"
														onfocus="javascript:alteraForma(true);">
													<label><fmt:message key="documento.modelo3"/> <span id="idMod-spinner" class="spinner-border text-secondary d-none"></span></label> 
													<select class="form-control siga-select2" id="idMod" name="idMod">
														<option value="0" >[Todos]</option>
													</select>
												</div>
											</div>
										</div>
									</div>
				
									<div class="form-row">
										<div class="form-group col-md-3">
											<label for="anoEmissaoString">Ano de Emissão</label> <select
												class="form-control" id="anoEmissaoString"
												name="anoEmissaoString" onchange="podeDescricao(true)">
												<option value="0">[Todos]</option>
												<c:forEach items="${listaAnos}" var="item">
													<option value="${item}"
														${item == anoEmissaoString ? 'selected' : ''}>${item}</option>
												</c:forEach>
											</select>
										</div>
											<div class="form-group col-md-3">
												<label for="numExpediente"><fmt:message key="documento.numero"/></label>
											    <input type="text" size="10" id="numExpediente" name="numExpediente" value="${numExpediente}" 
											    	onchange="validarFiltrosPesquisa()" maxlength="10" class="form-control" />
											</div>
									</div>
									<div class="form-row">
										<div class="form-group col-md-3" id="trNumOrigDoc"
											style="display:${idTpDoc == 2 || idTpDoc == 3 ? '' : 'none'}">
											<label for="numExtDoc">Nº Original do Documento</label> <input
												class="form-control" type="text" name="numExtDoc" size="16"
												id="numExtDoc" value="${numExtDoc}" />
										</div>
				
										<div class="form-group col-md-6" id="trOrgExterno"
											style="display:${idTpDoc == 3 ? '' : 'none'}">
											<label for="cpOrgao">Órgão Externo</label>
											<siga:selecao propriedade="cpOrgao" modulo="siga"
												titulo="Órgão Externo" tema="simple" />
										</div>
				
										<div class="form-group col-md-3" id="trNumDocSistAntigo"
											style="display:${idTpDoc == 3 ? '' : 'none'}">
											<label for="numAntigoDoc">Nº do Documento no Sistema
												Antigo</label> <input class="form-control" type="text"
												name="numAntigoDoc" value="${numAntigoDoc}" size="16" value=""
												id="numAntigoDoc" />
										</div>
				
										<div class="form-group col-md-3" id="trSubscritorExt"
											style="display:${idTpDoc == 3 or idTpDoc == 4 ? '' : 'none'}">
											<label for="nmSubscritorExt"><fmt:message key="documento.subscritor.antigo"/></label> <input
												class="form-control" type="text" label="Subscritor"
												name="nmSubscritorExt" value="${nmSubscritorExt}" size="80" />
										</div>
				
										<div class="form-group col-md-6" id="trSubscritor"
											style="display:${idTpDoc != 3 and idTpDoc != 4 ? '' : 'none'}">
											<label for="subscritor"><fmt:message key="documento.subscritor"/></label>
											<siga:selecao titulo="Subscritor:" propriedade="subscritor"
												paramList="buscarFechadas=true" modulo="siga" tema="simple" />
										</div>
									</div>
				
									<div class="form-row">
										<div class="form-group col-md-2">
											<label for="tipoCadastrante"><fmt:message key="documento.cadastrante"/></label> <select
												class="form-control" id="tipoCadastrante" name="tipoCadastrante"
												onchange="javascript:alteraCadastranteDocumento();">
												<c:forEach items="${listaTipoResp}" var="item">
													<option value="${item.key}"
														${item.key == tipoCadastrante ? 'selected' : ''}>
														${item.value}</option>
												</c:forEach>
											</select>
										</div>
										<c:if test="${tipoCadastrante == 1}">
											<div id="divCadastrante" style="display:"
												class="form-group col-md-4">
												<label for="ultMovTipoResp"><fmt:message key="tela.pesquisa.pessoa"/></label>
												<siga:selecao propriedade="cadastrante" tema="simple"
													paramList="buscarFechadas=true" modulo="siga" />
											</div>
											<div id="divLotaCadastrante" style="display: none"
												class="form-group col-md-4">
												<label for="ultMovTipoResp"><fmt:message key="usuario.lotacao"/></label>
												<siga:selecao propriedade="lotaCadastrante" tema="simple"
													paramList="buscarFechadas=true" modulo="siga" />
											</div>
										</c:if>
										<c:if test="${tipoCadastrante == 2}">
											<div id="divCadastrante" style="display: none"
												class="form-group col-md-4">
												<label for="ultMovTipoResp"><fmt:message key="tela.pesquisa.pessoa"/></label>
												<siga:selecao propriedade="cadastrante" tema="simple"
													paramList="buscarFechadas=true" modulo="siga" />
											</div>
											<div id="divLotaCadastrante" style="display:"
												class="form-group col-md-4">
												<label for="ultMovTipoResp"><fmt:message key="usuario.lotacao"/></label>
												<siga:selecao propriedade="lotaCadastrante" tema="simple"
													paramList="buscarFechadas=true" modulo="siga" />
											</div>
										</c:if>
										<div class="form-group col-md-2">
											<label for="tipoDestinatario"><fmt:message key="documento.destinatario"/></label> <select
												class="form-control" id="tipoDestinatario"
												name="tipoDestinatario"
												onchange="javascript:alteraDestinatarioDocumento();">
												<c:forEach items="${listaTipoDest}" var="item">
													<option value="${item.key}"
														${item.key == tipoDestinatario ? 'selected' : ''}>
														${item.value}</option>
												</c:forEach>
											</select>
										</div>
										<div id="divDestinatario"
											style="display:${tipoDestinatario == 1 ? '':'none'}"
											class="form-group col-md-4">
											<label for="destinatario"><fmt:message key="tela.pesquisa.pessoa"/></label>
											<siga:selecao propriedade="destinatario" tema="simple"
												paramList="buscarFechadas=true" modulo="siga" />
										</div>
										<div id="divLotaDestinatario"
											style="display: ${tipoDestinatario == 2 ? '':'none'}"
											class="form-group col-md-4">
											<label for="lotacaoDestinatario"><fmt:message key="usuario.lotacao"/></label>
											<siga:selecao propriedade="lotacaoDestinatario" tema="simple"
												paramList="buscarFechadas=true" modulo="siga" />
										</div>
										<div id="divOrgaoExternoDestinatario"
											style="display: ${tipoDestinatario == 3 ? '':'none'}"
											class="form-group col-md-4">
											<label for="orgaoExternoDestinatario">Órgão Externo</label>
											<siga:selecao propriedade="orgaoExternoDestinatario"
												tema="simple" modulo="siga" />
										</div>
										<div id="divNmDestinatario"
											style="display: ${tipoDestinatario == 4 ? '':'none'}"
											class="form-group col-md-4">
											<label for="nmDestinatario">Campo Livre</label> <input
												class="form-control" type="text" name="nmDestinatario"
												id="nmDestinatario" value="${nmDestinatario}" size="80" />
										</div>
									</div>
				
									<c:if test="${siga_cliente != 'GOVSP'}">
										<div class="form-row">
											<div class="form-group col-md-6">
												<label for="classificacao"><fmt:message key="documento.descricao"/></label> <input
													class="form-control" type="text" name="descrDocumento"
													value="${descrDocumento}" size="80" />
											</div>
										</div>
									</c:if>
									
									${f:obterExtensaoBuscaTextualbs4(lotaTitular.orgaoUsuario, fullText)} 
				
									<div class="form-row ${hide_only_GOVSP}">
										<div class="form-group col-md-6">
											<label for="classificacao"><fmt:message key="tela.pesquisa.classificacao"/></label>
											<siga:selecao propriedade="classificacao" modulo="sigaex" tema="simple"
												urlAcao="buscar" urlSelecionar="selecionar" />
				
												
										</div>
				
										<div class="form-group col-md-3">
											<label for="ordem"><fmt:message key="tela.pesquisa.ordenacao"/></label> <select class="form-control"
												id="ordem" name="ordem" onchange="javascript:sbmt();">
												<c:forEach items="${listaOrdem}" var="item">
													<option value="${item.key}"
														${item.key == ordem ? 'selected' : ''}>${item.value}</option>
												</c:forEach>
											</select>
										</div>
				
										<div class="form-group col-md-3">
											<label for="visualizacao"><fmt:message key="tela.pesquisa.visualizacao"/></label> <select
												class="form-control" id="visualizacao" name="visualizacao"
												onchange="javascript:sbmt();">
												<c:forEach items="${listaVisualizacao}" var="item">
													<option value="${item.key}"
														${item.key == visualizacao ? 'selected' : ''}>
														${item.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
									<button id="btnBuscar" type="button" value="Buscar" class="btn btn-primary" onclick="submitBusca()">
										<span id="buscandoSpinner" class="spinner-border d-none" role="status"></span> Buscar
									</button>
									<c:if
										test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC;FE:Ferramentas;LD:Listar Documentos')}">
										<siga:monobotao inputType="button"
											onclique="sbmtAction('listar', '/sigaex/app/ferramentas/doc/listar');"
											value="Administrar Documentos" cssClass="btn btn-primary" />
									</c:if>
									<input type="button" value="Voltar" onclick="javascript:history.back();" class="btn btn-primary" />				
								
							</div>
						</div>				
					</div>
		<!-- 		</div> -->
		<!-- 	</div> -->
				<c:if
					test="${((empty primeiraVez) or (primeiraVez != 'sim')) and ((empty apenasRefresh) or (apenasRefresh != 1)) and pesquisaLimitadaPorData}">
					<div id="pesqResult" class="mx-3">
						<jsp:include page="./listaSP.jsp"/>
						<script>
			 				$('html, body').animate({scrollTop: $('#pesqResult').offset().top - 30 }, 'slow');	 				
						</script>
					</div>
				</c:if>
			</div>
		</c:if>
	</form>
	</div>
	<script type="text/javascript" src="/siga/javascript/select2/select2.min.js"></script>
	<script type="text/javascript" src="/siga/javascript/select2/i18n/pt-BR.js"></script>
	<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>
</siga:pagina>