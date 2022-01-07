<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<script src="/siga/public/javascript/jquery/jquery-1.11.2.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css" media="screen, projection" />
<script type="text/javascript" language="Javascript1.1">
window.onload = function() {
	var frmsel = document.getElementById('idFormaDoc');
	var modsel = document.getElementById('idMod');
	${idFormaDoc != null && idFormaDoc != 0? 'alteraTipoDaForma(false);' : ''}
	${idMod != null && idMod != 0? 'alteraForma(false);' : ''}
};

function alteraTipoDaFormaAntigo(){
	ReplaceInnerHTMLFromAjaxResponse('${pageContext.request.contextPath}/app/expediente/doc/carregar_lista_formas?tipoForma='+document.getElementById('tipoForma').value+'&idFormaDoc='+'${idFormaDoc}', null, document.getElementById('comboFormaDiv'))
}

function alteraFormaAntigo(){
	ReplaceInnerHTMLFromAjaxResponse('${pageContext.request.contextPath}/app/expediente/doc/carregar_lista_modelos?forma='+document.getElementById('idFormaDoc').value+'&idMod='+'${idMod}', null, document.getElementById('comboModeloDiv'))
}

function alteraTipoDaForma(abrir) {
	if ($('#idFormaDoc-spinner').hasClass('d-none')) {
		$('#idFormaDoc-spinner').removeClass('d-none');
		$('#idFormaDoc').prop('disabled', 'disabled');
		SetInnerHTMLFromAjaxResponse(
				'/sigaex/app/expediente/doc/carregar_lista_formas?tipoForma='
						+ document.getElementById('tipoForma').value
						+ '&idFormaDoc=' + '${idFormaDoc}', document
						.getElementById('comboFormaDiv'), null, 
						(abrir? function(){	$('#idFormaDoc').select2('open'); buscar.appendChild(document.getElementById("idFormaDoc")); } : null)							
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
											podeDescricao(true);buscar.appendChild(document.getElementById("idMod"));} : 
								function(){	podeDescricao(true);buscar.appendChild(document.getElementById("idMod"));})							
						);
	}
}

function sbmt(offset) {
	sigaSpinner.mostrar();
	if (offset==null) {
		offset=0;
	}
	buscar["paramoffset"].value=offset;
	buscar.submit();
}

function submitBusca(cliente) {
	if(cliente == 'GOVSP') {
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
	buscar.submit();
}
</script>

<siga:pagina titulo="Lista de Expedientes" popup="${popup}">
	<!-- main content bootstrap -->
	<c:set var="formOrigem" value="busca" scope="request"/>
	<div id="inicio" class="container-fluid p-0" id="content-busca" onload="carregado();">
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
		<div class="card bg-light mb-3">
			<jsp:include page="./headerMeses.jsp"/>
		<c:if test="${((empty primeiraVez) or (primeiraVez != 'sim')) and ((empty apenasRefresh) or (apenasRefresh != 1)) and !pesquisaLimitadaPorData}">
			<div id="pesqResult" class="small p-0">
				<jsp:include page="./lista.jsp"/>
				<div class="gt-content-box gt-for-table d-none">
					<table class="table table-striped table-hover ">
						<thead class="${thead_color}">
							<tr>
								<th rowspan="3" align="right">
									Número
								</th>
								<th colspan="3" align="center">
									Documento
								</th>
								<th colspan="4" align="center">
									Situação
								</th>
								<th rowspan="3">
									Tipo
								</th>
								<th rowspan="3">
									Modelo
								</th>
								<th rowspan="3">
									Descrição
								</th>
								<c:if test="${visualizacao == 1}"> 
									<th rowspan="3">
										Última Anotação
									</th>
								</c:if>
							</tr>
							<tr>
								<th rowspan="2" align="center">
									Data
								</th>
								<th colspan="2" align="center">
									<fmt:message key="documento.subscritor"/>
								</th>
								<th rowspan="2" align="center">
									Data
								</th>
								<th colspan="2" align="center">
									Atendente
								</th>
								<th rowspan="2" align="center">
									Situação
								</th>
							</tr>
							<tr>
								<th align="center">
									<fmt:message key="usuario.lotacao"/>
								</th>
								<th align="center">
									<fmt:message key="usuario.pessoa2"/>
								</th>
								<th align="center">
									<fmt:message key="usuario.lotacao"/>
								</th>
								<th align="center">
									<fmt:message key="usuario.pessoa2"/>
								</th>
							</tr>
						</thead>

						<siga:paginador maxItens="${itemPagina}" maxIndices="10" totalItens="${tamanho}" itens="${itens}" var="documento">
							<c:choose>
								<c:when test="${documento[0].eletronico}">
									<c:set var="exibedoc" value="even" />
								</c:when>
								<c:otherwise>
									<c:set var="exibedoc" value="fisicoeven" />
								</c:otherwise>
							</c:choose>

							<tr class="${exibedoc}">
								<c:set var="podeAcessar" value="${f:testaCompetencia('acessarDocumento',titular,lotaTitular, documento[1])}" />
								<td width="11.5%" align="right">
									<c:choose>
										<c:when test='${popup!="true"}'>
											<c:choose>
												<c:when test="${podeAcessar eq true}">
													<a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${documento[1].sigla}">
														${documento[1].codigo}
													</a>
												</c:when>
												<c:otherwise> 
													${documento[1].codigo}
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<a href="javascript:parent.retorna_${propriedade}('${documento[1].id}','${documento[1].sigla}','${f:selDescricaoConfidencial(documento[1], lotaTitular, titular)}');">
												${documento[1].codigo}
											</a>
										</c:otherwise>
									</c:choose>
								</td>
								<c:if test="${documento[1].numSequencia != 0}">
									<td width="5%" align="center">
										${documento[0].dtDocDDMMYY}</td>
									<td width="4%" align="center"><siga:selecionado
										sigla="${documento[0].lotaSubscritor.sigla}"
										descricao="${documento[0].lotaSubscritor.descricao}"
										lotacaoParam="${documento[0].lotaSubscritor.orgaoUsuario.siglaOrgaoUsu}${documento[0].lotaSubscritor.sigla}" />
									</td>
									<td width="4%" align="center"><siga:selecionado
											sigla="${documento[0].subscritor.iniciais}"
											descricao="${documento[0].subscritor.descricao}"
											pessoaParam="${documento[0].subscritor.sigla}" /></td>
									<td width="5%" align="center">
										${documento[2].dtIniMarcaDDMMYYYY}</td>
									<td width="4%" align="center"><siga:selecionado
											sigla="${documento[2].dpLotacaoIni.lotacaoAtual.sigla}"
											descricao="${documento[2].dpLotacaoIni.lotacaoAtual.descricao}"
											lotacaoParam="${documento[2].dpLotacaoIni.orgaoUsuario.siglaOrgaoUsu}${documento[2].dpLotacaoIni.sigla}" />
									</td>
									<td width="4%" align="center"><siga:selecionado
											sigla="${documento[2].dpPessoaIni.iniciais}"
											descricao="${documento[2].dpPessoaIni.descricao}"
											pessoaParam="${documento[2].dpPessoaIni.sigla}" />
									</td>
		
									<td width="10.5%" align="center">
										${documento[2].cpMarcador.descrMarcador}</td>
								</c:if>
								<c:if test="${documento[1].numSequencia == 0}">
									<td width="5%" align="center">
										${documento[0].dtDocDDMMYY}</td>
									<td width="4%" align="center"><siga:selecionado
										sigla="${documento[0].lotaSubscritor.sigla}"
										descricao="${documento[0].lotaSubscritor.descricao}"
										lotacaoParam="${documento[0].lotaSubscritor.orgaoUsuario.siglaOrgao}${documento[0].lotaSubscritor.sigla}" />
									</td>
									<td width="4%" align="center"><siga:selecionado
										sigla="${documento[0].subscritor.iniciais}"
										descricao="${documento[0].subscritor.descricao}"
										pessoaParam="${documento[0].subscritor.sigla}" /></td>
		
									<td width="5%" align="center">tag1</td>
									<td width="4%" align="center"></td>
									<td width="4%" align="center"></td>
									<td width="10.5%" align="center">tag4</td>
								</c:if>

								<td width="6%">
									${documento[0].descrFormaDoc}
								</td>

								<td width="6%">${documento[0].nmMod}</td>

								<c:set var="acessivel" value="" />
								<c:set var="acessivel" value="${f:testaCompetencia('acessarDocumento',titular,lotaTitular,documento[1])}" />
								<c:choose>
									<c:when test="${acessivel eq true}">
										<c:set var="estilo" value="" />
										<c:if test="${f:mostraDescricaoConfidencial(documento[0], titular, lotaTitular) eq true}">
											<c:set var="estilo" value="confidencial" />
										</c:if>
										<td class="${estilo}" width="38%">
											${f:descricaoSePuderAcessar(documento[0], titular, lotaTitular)}
										</td>
										<c:if test="${visualizacao == 1}"> 
											<td class="${estilo}" width="38%">
												${f:anotacaoConfidencial(documento[1], titular,lotaTitular)}
											</td>
										</c:if>
									</c:when>
									<c:otherwise>
										<td>
											CONFIDENCIAL
										</td>
										<c:if test="${visualizacao == 1}"> 
											<td>
												CONFIDENCIAL
											</td>
										</c:if>
									</c:otherwise>
								</c:choose>
							</tr>
						</siga:paginador>
						<br />
					</table>
				</div>
			</div>
		</c:if>
		<c:if test="${!pesquisaLimitadaPorData}">
		</div>
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Dados do Documento</h5>
			</div>
		</c:if>
		<div id="pesqFiltros" class="card-body collapse show">
			<div class="tab-content" id="nav-tabContent">
				<div class="tab-pane fade show active" id="content1">
				  <form id="buscar" name="buscar" onsubmit="javascript: return limpaCampos()"
						action="buscar" method="get" class="form100">
					<input type="hidden" name="popup" value="${popup}" />
					<input type="hidden" name="propriedade" value="${propriedade}" />
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="apenasRefresh" value="0" />
					<input type="hidden" name="paramoffset" value="0" />
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label>Situação</label> 
								<select id="ultMovIdEstadoDoc" name="ultMovIdEstadoDoc" onchange="javascript:sbmt();" class="form-control">
									<option value="0">
										[Todos]
									</option>
									<c:forEach items="${estados}" var="item">
										<option value="${item.idMarcador}" ${item.idMarcador == ultMovIdEstadoDoc ? 'selected' : ''}>
											${item.descrMarcador}
										</option>  
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-sm-3 ${hide_only_GOVSP}">
							<div class="form-group">
								<label>Ordenação</label> 
								<select name="ordem" onchange="javascript:sbmt();" class="form-control" >
									<c:forEach items="${listaOrdem}" var="item">
										<option value="${item.key}" ${item.key == ordem ? 'selected' : ''}>
											${item.value}
										</option>  
									</c:forEach>
								</select> 

							</div>
						</div>
						<div class="col-sm-3 ${hide_only_GOVSP}">
							<div class="form-group">
								<label>Visualização</label> 
								<select name="visualizacao" onchange="javascript:sbmt();" class="form-control">
									<c:forEach items="${listaVisualizacao}" var="item">
										<option value="${item.key}" ${item.key == visualizacao ? 'selected' : ''}>
											${item.value}
										</option>  
									</c:forEach>
								</select>

							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-4">
							<div class="form-group">
								<label><fmt:message key="usuario.pessoa"/>/<fmt:message key="usuario.lotacao"/></label> 
								<select id="ultMovTipoResp" name="ultMovTipoResp" onchange="javascript:alteraAtendente();" class="form-control">
									<c:forEach items="${listaTipoResp}" var="item">
										<option value="${item.key}" ${item.key == ultMovTipoResp ? 'selected' : ''}>
											${item.value}
										</option>  
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-sm-8">
							<div class="form-group">
								<label>&nbsp;</label> 
								<c:if test="${ultMovTipoResp == 1}">
									<span id="divUltMovResp" style="display: ">
										<siga:selecao propriedade="ultMovResp" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
									</span>
									<span id="divUltMovLotaResp" style="display: none">
										<siga:selecao propriedade="ultMovLotaResp" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
									</span>
								</c:if>
								<c:if test="${ultMovTipoResp == 2}">
									<span id="divUltMovResp" style="display: none">
										<siga:selecao propriedade="ultMovResp" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
									</span>
									<span id="divUltMovLotaResp" style="display: ">
										<siga:selecao propriedade="ultMovLotaResp" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
									</span>
								</c:if>
							</div>
						</div>	
					</div>
					<div class="form-row">
						<div class="form-group col-md-3">
							<label for="orgaoUsu">Órgão</label> <select class="form-control"
								id="orgaoUsu" name="orgaoUsu" onchange="podeDescricao(true)">
								<c:if test="${siga_cliente != 'GOVSP'}">
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
					</div>
					<div class="form-row">
						<div class="form-group col-md-3 ${pesquisaLimitadaPorData? 'd-none':''}">
							<label for="dtDocString">Data Inicial</label> <input
								class="form-control campoData" placeholder="dd/mm/aaaa" autocomplete="off" 
								type="text" name="dtDocString" id="dtDocString" value="${dtDocString}"
								onblur="javascript:verifica_data(this,0);" />
						</div>
						<div class="form-group col-md-3 ${pesquisaLimitadaPorData? 'd-none':''}">
							<label for="dtDocFinalString">Data Final</label> <input
								class="form-control campoData" placeholder="dd/mm/aaaa" autocomplete="off" 
								type="text" name="dtDocFinalString" id="dtDocFinalString" value="${dtDocFinalString}"
								onblur="javascript:verifica_data(this,0);" />
						</div>
					</div>
					<c:choose>
						<c:when test="${tipoDocumento != 'externo'}">
							<c:set var="trTipo" value="display: " />
						</c:when>
						<c:otherwise>
							<c:set var="trTipo" value="display: none" />
						</c:otherwise>
					</c:choose>
					<div class="row" style="${trTipo}" id="trTipo">
						<div class="col-sm-4">
							<div class="form-group">
								<label>Tipo</label>
								<select id="tipoForma" name="idTipoFormaDoc" onchange="javascript:alteraTipoDaForma(false);" class="form-control">
									<option value="0">
										[Todos]
									</option>
									<c:forEach items="${tiposFormaDoc}" var="item">
										<option value="${item.idTipoFormaDoc}" ${item.idTipoFormaDoc == idTipoFormaDoc ? 'selected' : ''}>
											${item.descTipoFormaDoc}
										</option>  
									</c:forEach>
								</select>
							</div>
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
					<div class="row">
						<div class="col-sm-4">
							<div class="form-group">
								<label>Ano de Emissão</label>
								<select id="anoEmissaoString" name="anoEmissaoString" class="form-control" onchange="podeDescricao(true)">
									<option value="0">
										[Todos]
									</option>
									<c:forEach items="${listaAnos}" var="item">
										<option value="${item}" ${item == anoEmissaoString ? 'selected' : ''}>
											${item}
										</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-sm-4">
							<div class="form-group">
								<label><fmt:message key="documento.numero"/></label>
								<input id="numExpediente" type="text" size="10" name="numExpediente" value="${numExpediente}" 
									onchange="validarFiltrosPesquisa()" maxlength="10" class="form-control"/>
							</div>
						</div>
					</div>
					<c:choose>
						<c:when test='${tipoDocumento == "externo" || tipoDocumento == "antigo"}'>
							<c:set var="trNumOrigDoc" value="display: " />
						</c:when>
						<c:otherwise>
							<c:set var="trNumOrigDoc" value="display: none" />
						</c:otherwise>
					</c:choose>
					<div class="row" style="${trNumOrigDoc}" id="trNumOrigDoc">
						<div class="col-sm">
							<div class="form-group">
								<label for="numExtDoc" class="label">Nº Original do Documento</label>
								<input type="text" name="numExtDoc" value="${numExtDoc}" size="16" value="" id="numExtDoc" class="form-control"/>
							</div>
						</div>
					</div>
					<c:choose>
						<c:when test='${tipoDocumento == "externo"}'>
							<c:set var="trOrgExterno" value="display: " />
						</c:when>
						<c:otherwise>
							<c:set var="trOrgExterno" value="display: none" />
						</c:otherwise>
					</c:choose>
					<div class="row" style="${trOrgExterno}" id="trOrgExterno">
						<div class="col-sm">
							<div class="form-group">
								<label for="numExtDoc" class="label">Órgão Externo</label>
								<siga:selecao propriedade="cpOrgao" modulo="siga" titulo="Órgão Externo" tema="simple" />
							</div>
						</div>
					</div>
					<c:choose>
						<c:when test='${tipoDocumento == "externo" || tipoDocumento == "antigo"}'>
							<c:set var="trNumDocSistAntigo" value="display: " />
						</c:when>
						<c:otherwise>
							<c:set var="trNumDocSistAntigo" value="display: none" />
						</c:otherwise>
					</c:choose>
					<div class="row" style="${trNumDocSistAntigo}" id="trNumDocSistAntigo">
						<div class="col-sm">
							<div class="form-group">
								<label for="numExtDoc" class="label">Nº do Documento no Sistema Antigo</label>
								<input type="text" name="numAntigoDoc" value="${numAntigoDoc}" size="16" value="" id="numAntigoDoc" class="form-control"/>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-sm">
							<div class="form-group">
								<label for="numExtDoc" class="label"><fmt:message key="tela.aBuscar.subscritor" /></label>											
								<c:choose>
									<c:when test="${tipoDocumento == 'externo'}">
										<input type="text" label="Subscritor" name="nmSubscritorExt" value="${nmSubscritorExt}" size="80" class="form-control"/>
									</c:when>
									<c:otherwise>
										<siga:selecao titulo="Subscritor:" propriedade="subscritor" paramList="buscarFechadas=true" tema="simple" modulo="siga"/>
									</c:otherwise>
								</c:choose>		
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-4">
							<div class="form-group">
								<label for="numExtDoc" class="label">Cadastrante</label>											
								<select id="tipoCadastrante" name="tipoCadastrante" onchange="javascript:alteraCadastranteDocumento();" class="form-control" >
									<c:forEach items="${listaTipoResp}" var="item">
										<option value="${item.key}" ${item.key == tipoCadastrante ? 'selected' : ''}>
											${item.value}
										</option>  
									</c:forEach>
								</select> 
							</div>
						</div>
						<div class="col-sm-8">
							<div class="form-group">
								<label>&nbsp;</label>
								<c:if test="${tipoCadastrante == 1}">
									<div id="divCadastrante" style="display: ">
										<siga:selecao propriedade="cadastrante" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
									</div>
									<div id="divLotaCadastrante" style="display: none">
										<siga:selecao propriedade="lotaCadastrante" tema="simple" paramList="buscarFechadas=true" modulo="siga" />
									</div>
								</c:if> 
								<c:if test="${tipoCadastrante == 2}">
									<div id="divCadastrante" style="display: none">
										<siga:selecao propriedade="cadastrante" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
									</div>
									<div id="divLotaCadastrante" style="display: ">
										<siga:selecao propriedade="lotaCadastrante" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
									</div>
								</c:if>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-4">
							<div class="form-group">
								<label>Destinatário</label>
								<select id="tipoDestinatario" name="tipoDestinatario"  onchange="javascript:alteraDestinatarioDocumento();" class="form-control" >
									<c:forEach items="${listaTipoDest}" var="item">
										<option value="${item.key}" ${item.key == tipoDestinatario ? 'selected' : ''}>
											${item.value}
										</option>  
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-sm-8">
							<div class="form-group">
								<label>&nbsp;</label>
								<c:choose>
									<c:when test='${tipoDestinatario == 1}'>
										<div id="divDestinatario" style="display: ">
											<siga:selecao propriedade="destinatario" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
										</div>
										<div id="divLotaDestinatario" style="display: none">
											<siga:selecao propriedade="lotacaoDestinatario" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
										</div>
										<div id="divOrgaoExternoDestinatario" style="display: none">
											<siga:selecao propriedade="orgaoExternoDestinatario" tema="simple" modulo="siga"/>
										</div>
										<div id="divNmDestinatario" style="display: none">
											<input type="text" name="nmDestinatario" value="${nmDestinatario}" size="80" />
										</div>
									</c:when>
									<c:when test='${tipoDestinatario == 2}'>
										<div id="divDestinatario" style="display: none">
											<siga:selecao propriedade="destinatario" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
										</div>
										<div id="divLotaDestinatario" style="display: ">
											<siga:selecao propriedade="lotacaoDestinatario" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
										</div>
										<div id="divOrgaoExternoDestinatario" style="display: none">
											<siga:selecao propriedade="orgaoExternoDestinatario" tema="simple" modulo="siga"/>
										</div>
										<div id="divNmDestinatario" style="display: none">
											<input type="text" name="nmDestinatario" value="${nmDestinatario}" size="80" class="form-control"/>
										</div>
									</c:when>
									<c:when test='${tipoDestinatario == 3}'>
										<div id="divDestinatario" style="display: none">
											<siga:selecao propriedade="destinatario" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
										</div>
										<div id="divLotaDestinatario" style="display: none">
											<siga:selecao propriedade="lotacaoDestinatario" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
										</div>
										<div id="divOrgaoExternoDestinatario" style="display: ">
											<siga:selecao propriedade="orgaoExternoDestinatario" tema="simple" modulo="siga"/>
										</div>
										<div id="divNmDestinatario" style="display: none">
											<input type="text" name="nmDestinatario" value="${nmDestinatario}" size="80" class="form-control" />
										</div>
									</c:when>
									<c:otherwise>
										<div id="divDestinatario" style="display: none">
											<siga:selecao propriedade="destinatario" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
										</div>
										<div id="divLotaDestinatario" style="display: none">
											<siga:selecao propriedade="lotacaoDestinatario" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
										</div>
										<div id="divOrgaoExternoDestinatario" style="display: none">
											<siga:selecao propriedade="orgaoExternoDestinatario" tema="simple" modulo="siga"/>
										</div>
										<div id="divNmDestinatario" style="display: ">
											<input type="text" name="nmDestinatario" value="${nmDestinatario}" size="80" class="form-control"/>
										</div>
									</c:otherwise>
								</c:choose>
							</div>
						</div>	
					</div>
					<div class="row ${hide_only_GOVSP}">
						<div class="col-sm-6">
							<div class="form-group">
								<label><fmt:message key="tela.pesquisa.classificacao"/></label>	
								<siga:selecao titulo="Classificação:" propriedade="classificacao" tema="simple" modulo="sigaex" urlAcao="buscar" urlSelecionar="selecionar"/>
							</div>
						</div>
					</div>	
					
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">	
								<label>Descrição</label>
								<input type="text" name="descrDocumento" id="descrDocumento" value="${descrDocumento}" size="80" class="form-control"
									<c:if test="${!podePesquisarDescricao}">
										readonly placeholder="Não é possível realizar a pesquisa pela descrição"
									</c:if>
									/>
								<c:if test="${podePesquisarDescricao && podePesquisarDescricaoLimitada}">
									<small>Campo "Descrição" habilitado para pesquisa após o preenchimento dos campos "Órgão", "Espécie" e "Ano de Emissão"</small>
								</c:if>
							</div>
						</div>
					</div>	
					<div class="row">
						<div class="col-sm-4">
							<div class="form-group">	
								${f:obterExtensaoBuscaTextual(lotaTitular.orgaoUsuario, fullText)}
							</div>
						</div>
					</div>							
					<div class="row">
						<div class="col-sm-4">
							<div class="form-group">	
								<button id="btnBuscar" type="button" value="Buscar" class="btn btn-primary" onclick="submitBusca('${siga_cliente}')">
									<span id="buscandoSpinner" class="spinner-border d-none" role="status"></span> Buscar
								</button>
							</div>
						</div>
					</div>					
			      </form>
			    </div>
			</div>
		</div>
<!-- 	</div> -->
	<c:if test="${((empty primeiraVez) or (primeiraVez != 'sim')) and ((empty apenasRefresh) or (apenasRefresh != 1)) and pesquisaLimitadaPorData}">
		<div id="pesqResult" class="small pt-2 p-0">
			<jsp:include page="./lista.jsp"/>
			<script>
 				$('html, body').animate({scrollTop: $('#pesqResult').offset().top - 60 }, 'slow');	 				
			</script>
		</div>
	</c:if>		
	<c:if test="${!pesquisaLimitadaPorData}">
		</div>
	</c:if>
	<script type="text/javascript" src="/siga/javascript/select2/select2.min.js"></script>
	<script type="text/javascript" src="/siga/javascript/select2/i18n/pt-BR.js"></script>
	<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>
</siga:pagina>