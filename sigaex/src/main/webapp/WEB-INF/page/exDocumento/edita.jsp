<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Novo Documento">
	<link rel="stylesheet" href="/siga/javascript/hierarchy-select/hierarchy-select.css" type="text/css" media="screen, projection" />
	<script type="text/javascript" src="/ckeditor/ckeditor/ckeditor.js"></script>
	<script type="text/javascript" src="../../../javascript/exDocumentoEdita.js"></script>
	<script type="text/javascript" src="/siga/javascript/jquery.blockUI.js"></script>
	<script type="text/javascript" src="/siga/javascript/hierarchy-select/hierarchy-select.js"></script>

	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>
					<c:choose>
						<c:when test='${empty exDocumentoDTO.doc}'>
							Novo Documento
						</c:when>
						<c:otherwise>
							<span id="codigoDoc">${exDocumentoDTO.doc.codigo}</span>
							<!-- de: <span id="dataDoc">${exDocumentoDTO.doc.dtRegDocDDMMYY}</span>-->
						</c:otherwise>
					</c:choose>
				</h5>
			</div>
			<div class="card-body">
			<form id="frm" name="frm" theme="simple" method="post" enctype="multipart/form-data" class="mb-0">
				<input type="hidden" id="idTamanhoMaximoDescricao" name="exDocumentoDTO.tamanhoMaximoDescricao" value="${exDocumentoDTO.tamanhoMaximoDescricao}" /> 
				<input type="hidden" id="alterouModelo" name="exDocumentoDTO.alterouModelo" /> 
				<input type="hidden" id="clickSelect" name="clickSelect" /> 
				<input type="hidden" name="postback" value="1" /> 
				<input type="hidden" id="sigla" name="exDocumentoDTO.sigla" value="${exDocumentoDTO.sigla}" /> 
				<input type="hidden" name="exDocumentoDTO.nomePreenchimento" value="" /> 
				<input type="hidden" name="campos" value="criandoAnexo" />  
				<input type="hidden" name="campos" value="autuando" /> 
				<input type="hidden" name="exDocumentoDTO.autuando" value="${exDocumentoDTO.autuando}" /> 
				<input type="hidden" name="exDocumentoDTO.criandoAnexo" value="${exDocumentoDTO.criandoAnexo}" /> 
				<input type="hidden" name="campos" value="idMobilAutuado" /> 
				<input type="hidden" name="exDocumentoDTO.idMobilAutuado" value="${exDocumentoDTO.idMobilAutuado}" /> 
				<input type="hidden" name="exDocumentoDTO.id" value="${exDocumentoDTO.doc.idDoc}" /> 
				<input type="hidden" name="exDocumentoDTO.idMod.original" value="${exDocumentoDTO.modelo.idMod}" /> 
				<input type="hidden" name="jsonHierarquiaDeModelos" value="${jsonHierarquiaDeModelos}" />
				<c:choose>
					<c:when	test="${(exDocumentoDTO.doc.eletronico) && (exDocumentoDTO.doc.numExpediente != null)}">
						<c:set var="estiloTipo" value="display: none" />
						<c:set var="estiloTipoSpan" value="" />
					</c:when>
					<c:otherwise>
						<c:set var="estiloTipo" value="" />
						<c:set var="estiloTipoSpan" value="display: none" />
					</c:otherwise>
				</c:choose>
				<input type="hidden" name="campos" value="idTpDoc" />
	
				<!-- Modelo -->
				<div class="row">
					<div class="col-sm-12">
						<c:choose>
							<c:when test="${possuiMaisQueUmModelo}">
								<div class="form-group">
									<label for="modelos-select"><fmt:message key="documento.modelo"/></label>

									<div class="btn-group hierarchy-select form-control p-0" data-resize="auto" id="modelos-select">
										<button type="button" class="btn btn-light dropdown-toggle bg-white"  <c:if test='${podeEditarModelo}'>disabled</c:if>
											id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" data-disabled="true">
											<span class="selected-label pull-left">&nbsp;</span>
										</button>
										<div class="dropdown-menu form-control" aria-labelledby="dropdownMenuButton">
											<div class="hs-searchbox">
												<input type="text" class="form-control" autocomplete="off" placeholder="Pesquisar modelo...">
											</div>
											<ul class="dropdown-menu show inner" role="menu">
												<c:forEach items="${hierarquiaDeModelos}" var="item">
													<li class="dropdown-item" data-value="${item.value}"
														data-level="${item.level}" data-search="${item.searchText}"
														${item.group ? 'data-group' : ''}
														${item.selected ? 'data-default-selected' : ''}>
														<c:if test="${item.group}">
															<a href="#">${item.text}</a>
														</c:if>
														<c:if test="${!item.group}">
															<a href="#" class="d-inline">${item.text}<small class="pl-2 text-muted">${item.keywords}</small></a>
														</c:if>
													</li>
												</c:forEach>
											</ul>
										</div>
										<input class="hidden hidden-field" name="exDocumentoDTO.idMod" readonly="readonly" onchange="alterouModeloSelect()"
											aria-hidden="true" type="text" value="${exDocumentoDTO.idMod}" />
									</div>
									<small class="form-text text-muted"><fmt:message key="documento.help.modelo"/></small>
								</div>
							</c:when>
							<c:otherwise>
								<input type="hidden" name="exDocumentoDTO.idMod" value="${exDocumentoDTO.modelo.idMod}" />
							</c:otherwise>
						</c:choose>
						<c:if test='${exDocumentoDTO.tipoDocumento == "externo" }'>
							<input type="hidden" name="exDocumentoDTO.idMod" value="${exDocumentoDTO.idMod}" />
						</c:if>
					</div>
				</div>
					<div class="row ${((exDocumentoDTO.tiposDocumento).size() != 1 or (exDocumentoDTO.tipoDocumento != 'interno_capturado' and podeEditarData) or (exDocumentoDTO.listaNivelAcesso.size() != 1) or (!exDocumentoDTO.eletronicoFixo))? '': 'd-none'}">
						<div class="col-sm-2 ${(exDocumentoDTO.tiposDocumento).size() != 1? '': 'd-none'} ${hide_only_GOVSP}">
							<div class="form-group">
								<label for="exDocumentoDTO.idTpDoc">Origem</label>
								<select name="exDocumentoDTO.idTpDoc" onkeypress="presskeySelect(event, this, null)" onmousedown="javascript:document.getElementById('clickSelect').value='true';"
										onchange="alterouOrigem(); mouseSelect(event, this, null)" style="${estiloTipo}" class="form-control">
									<c:forEach items="${exDocumentoDTO.tiposDocumento}" var="item">
										<option value="${item.idTpDoc}"
											${item.idTpDoc == exDocumentoDTO.idTpDoc ? 'selected' : ''}>
											${item.descrTipoDocumento}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-sm-2 ${exDocumentoDTO.tipoDocumento != 'interno_capturado' and podeEditarData? '': 'd-none'}  ${hide_only_GOVSP}">
							<div class="form-group">
								<input type="hidden" name="campos" value="dtDocString" />						
								<label class=" " for="exDocumentoDTO.dtDocString">Data</label>
								<input type="text" name="exDocumentoDTO.dtDocString" size="10" onblur="javascript:verifica_data(this, true);" value="${exDocumentoDTO.dtDocString}" class="form-control"/>
							</div>
						</div>

						<div class="col-sm-2 ${(exDocumentoDTO.listaNivelAcesso).size() != 1? '': 'd-none'}">
							<div class="form-group">
								<input type="hidden" name="campos" value="nivelAcesso" /> 
								<label for="exDocumentoDTO.dtDocString">Acesso</label>
								<select name="exDocumentoDTO.nivelAcesso" class="form-control">
									<c:forEach items="${exDocumentoDTO.listaNivelAcesso}" var="item">
										<option value="${item.idNivelAcesso}"
											${item.idNivelAcesso == exDocumentoDTO.nivelAcesso ? 'selected' : ''}>
											${item.nmNivelAcesso}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-sm-2 ${exDocumentoDTO.eletronicoFixo? 'd-none': ''}">
							<div class="form-group">
								<input type="hidden" name="campos" value="eletronico" /> 
								<c:choose>
									<c:when test="${exDocumentoDTO.eletronicoFixo}">
										<input type="hidden" name="exDocumentoDTO.eletronico" id="eletronicoHidden" value="${exDocumentoDTO.eletronico}" />
												${exDocumentoDTO.eletronicoString}
										<c:if test="${exDocumentoDTO.eletronico == 2}">
										<script type="text/javascript">
											$("html").addClass("fisico");
											$("body").addClass("fisico");
										</script>
										</c:if>
									</c:when>
									<c:otherwise>
										<input type="radio" class="mt-4" name="exDocumentoDTO.eletronico" id="eletronicoCheck1" value="1" onchange="javascript:setFisico();"
											<c:if test="${exDocumentoDTO.eletronicoFixo}">disabled</c:if>
											<c:if test="${exDocumentoDTO.eletronico == 1}">checked</c:if> />
										<label for="eletronicoCheck1">Digital</label>
										<input type="radio" class="mt-4 ml-2" name="exDocumentoDTO.eletronico" id="eletronicoCheck2" value="2" onchange="javascript:setFisico();"
											<c:if test="${exDocumentoDTO.eletronicoFixo}">disabled</c:if>
											<c:if test="${exDocumentoDTO.eletronico == 2}">checked</c:if> />
										<label for="eletronicoCheck2">Físico</label>
										<script type="text/javascript">
											function setFisico() {
												if ($(
														'input[name=exDocumentoDTO\\.eletronico]:checked')
														.val() == 2) {
													$("html").addClass("fisico");
													$('body').addClass('fisico');
												} else {
													$('html').removeClass('fisico');
													$('body').removeClass('fisico');
												}
											};
											setFisico();
										</script>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
				<c:if test='${exDocumentoDTO.tipoDocumento == "antigo"}'>
				<input type="hidden" name="campos" value="numExtDoc" />
				<input type="hidden" name="campos" value="numAntigoDoc" />
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<label for="exDocumentoDTO.numExtDoc">Nº original</label>
							<input type="text" name="exDocumentoDTO.numExtDoc" size="16" maxLength="32" value="${exDocumentoDTO.numExtDoc}" class="form-control"/>
						</div>
					</div>
					<div class="col-sm-4">
						<div class="form-group">
							<label for="exDocumentoDTO.numAntigoDoc">Nº antigo</label>
							<input type="text" name="exDocumentoDTO.numAntigoDoc" size="16" maxLength="32" value="${exDocumentoDTO.numAntigoDoc}" class="form-control" />
							<small class="form-text text-muted">(informar o número do documento no antigo sistema de controle de expedientes ou de
											processos administrativos [SISAPA] ou [PROT]).</small>
						</div>
					</div>
				</div>
				</c:if>
				<input type="hidden" name="exDocumentoDTO.desativarDocPai" value="${exDocumentoDTO.desativarDocPai}" />
				<div class="row d-none">
					<div class="col-sm-8">
						<div class="form-group">
							<label>Documento Pai</label>
							<siga:selecao titulo="Documento Pai:" propriedade="mobilPai" inputName="exDocumentoDTO.mobilPai"
											tema="simple" modulo="sigaex" desativar="${exDocumentoDTO.desativarDocPai}" reler="sim" />					
						</div>
					</div>
				</div>
				<c:choose>
					<c:when test='${exDocumentoDTO.tipoDocumento == "externo" or exDocumentoDTO.tipoDocumento == "externo_capturado"}'>
					</c:when>
					<c:otherwise>
						<div class="row">
							<div class="col-sm-8">
								<div class="form-group">
									<input type="hidden" name="campos" value="subscritorSel.id" />
									<input type="hidden" name="campos" value="substituicao" />
									<input type="hidden" name="campos" value="personalizacao" />
									<input type="hidden" id="temCossignatarios" value="${not empty exDocumentoDTO.doc.cosignatarios}" />
									<label><fmt:message key="documento.subscritor"/></label>
									<siga:selecao propriedade="subscritor" inputName="exDocumentoDTO.subscritor" modulo="siga" tema="simple" />
								</div>
							</div>
							<div class="col-sm-2">
								<div class="form-group">
									<div class="form-check form-check-inline mt-4">
										<fmt:message key="documento.help.substituto" var="documento_help_substituto" />
										<input type="checkbox" name="exDocumentoDTO.substituicao" class="form-check-input" onclick="javascript:displayTitular(this);"
											<c:if test="${exDocumentoDTO.substituicao}">checked</c:if> />
										<label class="form-check-label" for="exDocumentoDTO.substituicao">Substituto </label>
										<a class="fas fa-info-circle text-secondary ml-1  ${hide_only_TRF2}" data-toggle="tooltip" data-trigger="click" data-placement="bottom" title="${documento_help_substituto}"></a>
										<input type="checkbox" name="exDocumentoDTO.personalizacao" class="form-check-input ml-3"  onclick="javascript:displayPersonalizacao(this);" 
											<c:if test="${exDocumentoDTO.personalizacao}">checked</c:if> />
										<label class="form-check-label" for="exDocumentoDTO.personalizacao">Personalizar</label>
									</div>
								</div>
							</div>
						</div>						
					</c:otherwise>
				</c:choose>
				<input type="hidden" name="campos" value="titularSel.id" />
				<div id="tr_titular" style="display: ${exDocumentoDTO.substituicao ? '' : 'none'};">
					<div class="row">
						<div class="col-sm-8">
							<div class="form-group">
								<label><fmt:message key="documento.titular"/></label>
								<siga:selecao propriedade="titular" inputName="exDocumentoDTO.titular" tema="simple" modulo="siga" />
							</div>
						</div>
					</div>
				</div>
				<input type="hidden" name="campos" value="nmFuncaoSubscritor" />
				<input type="hidden" name="exDocumentoDTO.nmFuncaoSubscritor" maxlength="128" id="frm_nmFuncaoSubscritor" value="${exDocumentoDTO.nmFuncaoSubscritor}" />
				<div id="tr_personalizacao" style="display: ${exDocumentoDTO.personalizacao? '': 'none'};">
					<div class="row ml-1">
						<h6>Personalização</h6>
					</div>
					<div class="row">
						<div class="col-sm-2">
							<div class="form-group">
								<label>Função</label>
								<input type="text" id="personalizarFuncao" maxlength="125" class="form-control">
							</div>
						</div>
						<div class="col-sm-2">
							<div class="form-group">
								<label><fmt:message key="usuario.lotacao"/></label>
								<input type="text" id="personalizarUnidade" maxlength="125" class="form-control">
							</div>
						</div>
						<div class="col-sm-2 ${hide_only_GOVSP}">
							<div class="form-group">
								<label>Cidade</label>
								<input type="text" id="personalizarLocalidade" maxlength="125" class="form-control">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="form-group ${hide_only_GOVSP}">
								<label>Nome</label>
								<input type="text" id="personalizarNome"  maxlength="125" class="form-control">
							</div>
						</div>
					</div>
				</div>
				<input type="hidden" name="campos" value="tipoDestinatario" />
				<c:if test='${exDocumentoDTO.tipoDocumento != "interno_capturado" }'> 
				<div class="row ${hide_only_GOVSP}">
					<div class="col-sm-2">
						<div class="form-group">
							<label>Destinatário</label>
							<select name="exDocumentoDTO.tipoDestinatario" onchange="javascript:sbmt();" class="form-control">
								<c:forEach items="${exDocumentoDTO.listaTipoDest}" var="item">
									<option value="${item.key}"
										${item.key == exDocumentoDTO.tipoDestinatario ? 'selected' : ''}>
										${item.value}</option>
								</c:forEach>
							</select> 
						</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
							<siga:span id="destinatario" depende="tipoDestinatario">
							<c:choose>
								<c:when test='${exDocumentoDTO.tipoDestinatario == 1}'>
									<input type="hidden" name="campos" value="destinatario" />
									<label>&nbsp;&nbsp;&nbsp;</label>
									<siga:selecao propriedade="destinatario" inputName="exDocumentoDTO.destinatario" tema="simple" idAjax="destinatario1" reler="ajax" modulo="siga" />
										<!--  idAjax="destinatario"  -->
								</c:when>
								<c:when test='${exDocumentoDTO.tipoDestinatario == 2}'>
									<input type="hidden" name="campos" value="lotacaoDestinatarioSel.id" />
									<label>&nbsp;&nbsp;&nbsp;</label>
									<siga:selecao propriedade="lotacaoDestinatario" inputName="exDocumentoDTO.lotacaoDestinatario" tema="simple" idAjax="destinatario2" reler="ajax" modulo="siga" />
									<!--  idAjax="destinatario" -->
								</c:when>
								<c:when test='${exDocumentoDTO.tipoDestinatario == 3}'>
									<input type="hidden" name="campos" value="orgaoExternoDestinatarioSel.id" />
									<label>&nbsp;&nbsp;&nbsp;</label>
									<siga:selecao propriedade="orgaoExternoDestinatario" inputName="exDocumentoDTO.orgaoExternoDestinatario" tema="simple" idAjax="destinatario3" reler="ajax" modulo="siga" />
									<!--  idAjax="destinatario" -->
										</div>
									</div>
									<div class="col-sm-4">
										<div class="form-group">
									<input type="hidden" name="campos" value="nmOrgaoExterno" />
									<label>&nbsp;&nbsp;&nbsp;</label>
									<input type="text" name="exDocumentoDTO.nmOrgaoExterno" size="120" maxLength="256" value="${exDocumentoDTO.nmOrgaoExterno}" class="form-control" />
								</c:when>
								<c:otherwise>
									<input type="hidden" name="campos" value="nmDestinatario" />
									<label>&nbsp;&nbsp;&nbsp;</label>
									<input type="text" name="exDocumentoDTO.nmDestinatario" size="80" maxLength="256" value="${exDocumentoDTO.nmDestinatario}" class="form-control" />
								</c:otherwise>
							</c:choose>
							</siga:span>
						</div>
					</div>
				</div>
				</c:if>	
				<c:if test='${ exDocumentoDTO.tipoDocumento == "interno" }'>
				<div class="row">
					<input type="hidden" name="campos" value="preenchimento" />
					<div class="col-sm-12">
						<div class="form-group">
							<label><fmt:message key="documento.preenchimento.automatico"/></label>							
							<div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
								<div class="input-group">
									<select id="preenchimento" name="exDocumentoDTO.preenchimento" onchange="javascript:carregaPreench()" class="form-control">
										<c:forEach items="${exDocumentoDTO.preenchimentos}" var="item">
											<option value="${item.idPreenchimento}"
												${item.idPreenchimento == exDocumentoDTO.preenchimento ? 'selected' : ''}>
												${item.nomePreenchimento}</option>
										</c:forEach>
									</select>
								</div>
								<c:if test="${empty exDocumentoDTO.preenchimento or exDocumentoDTO.preenchimento==0}">
									<c:set var="desabilitaBtn"> disabled </c:set>
								</c:if> 
								<button type="button" name="btnAlterar" onclick="javascript:alteraPreench()" class="btn btn-sm btn-secondary ml-2" ${desabilitaBtn}>
									<i class="far fa-edit"></i>
									<span class="${hide_only_GOVSP}">Alterar</span>
								</button>
								<button type="button" name="btnRemover" onclick="javascript:removePreench()" class="btn btn-sm btn-secondary ml-2" ${desabilitaBtn}>
									<i class="far fa-trash-alt"></i>
									<span class="${hide_only_GOVSP}">Remover</span>
								</button>
								<button type="button"  name="btnAdicionar" onclick="javascript:adicionaPreench()" class="btn btn-sm btn-secondary ml-2">
									<i class="fas fa-plus"></i>
									<span class="${hide_only_GOVSP}">Adicionar</span>
								</button>
							</div>
						</div>
					</div>
				</div>
			</c:if>
				<div id="tr_personalizacao" style="display: ${exDocumentoDTO.modelo.exClassificacao!=null? 'none': ''};">
					<div class="row  ${hide_only_GOVSP}">
						<c:if test="${exDocumentoDTO.modelo.exClassificacao!=null}">
							<c:set var="desativarClassif" value="sim" />
						</c:if>
						<div class="col-sm-5">
							<div class="form-group">
							<input type="hidden" name="campos" value="classificacaoSel.id" /> 
								<label>Classificação</label>
								<siga:span id="classificacao" depende="forma;modelo">
									<!-- OI -->
									<siga:selecao desativar="${desativarClassif}" modulo="sigaex" propriedade="classificacao"
										inputName="exDocumentoDTO.classificacao" urlAcao="buscar" urlSelecionar="selecionar" tema="simple" />
									<!--  idAjax="classificacao" -->
								</siga:span>
							</div>
						</div>
					</div>			
				</div>
				<c:if test="${exDocumentoDTO.classificacaoSel.id!=null && exDocumentoDTO.classificacaoIntermediaria}">
				<div class="row ${hide_only_GOVSP}">
					<div class="col-sm-5">
						<div class="form-group">
							<label>Descrição da Classificação</label>
							<siga:span id="descrClassifNovo" depende="forma;modelo;classificacao">
								<input type="text" name="exDocumentoDTO.descrClassifNovo" size="80" value="${exDocumentoDTO.descrClassifNovo}"  maxLength="4000" class="form-control" />
							</siga:span>
						</div>
					</div>
				</div>
				</c:if>	
				
				<c:choose>
					<c:when test='${exDocumentoDTO.modelo.descricaoAutomatica or (not podeEditarDescricao) or siga_cliente == "GOVSP"}'>
   					   <c:set var="displayDescricao" value="d-none" />
					</c:when>
					<c:otherwise>
           				<c:set var="displayDescricao" value="" />
					</c:otherwise>
				</c:choose>
				<c:if test="${exDocumentoDTO.modelo.descricaoAutomatica or (not podeEditarDescricao)}">
					<input type="hidden" id="descricaoAutomatica" value="sim" />
				</c:if>
				<div class="${displayDescricao}">
					<div class="row">
						<div class="col-sm-8">
							<div class="form-group">
								<label>Descrição</label>
								<textarea name="exDocumentoDTO.descrDocumento" cols="80" rows="2" id="descrDocumento" class="form-control">${exDocumentoDTO.descrDocumento}</textarea>
								<small class="form-text text-muted">(preencher o campo acima com palavras-chave, sempre usando substantivos, gênero masculino e
									singular).</small>
							</div>
						</div>
					</div>
				</div>
				<c:if test='${(not exDocumentoDTO.doc.finalizado) and (exDocumentoDTO.tipoDocumento == "interno_capturado" or  exDocumentoDTO.tipoDocumento == "externo_capturado")}'>
				<div class="row">
					<div class="col-sm-8">
						<div class="form-group">
							<input type="hidden" name="campos" value="descrDocumento" />
							<label> <fmt:message key = "usuario.novodocumento.arquivo"/></label>
							<br>
							  <div class="form-group">
							    <input type="file" class="form-control-file" id="arquivo" name="arquivo" accept="application/pdf" onchange="testpdf(this.form)">
							  </div>
						</div>
					</div>
				</div>						
				</c:if>
				<c:if test='${exDocumentoDTO.tipoDocumento == "externo"}'>
				<div class="row">
					<h6>Dados do Documento Original</h6>
				</div>
				<div class="row">
					<input type="hidden" name="campos" value="dtDocOriginalString" />
					<input type="hidden" name="campos" value="numExtDoc" />
					<div class="col-sm-2">
						<div class="form-group">
							<label>Nº original</label>
							<input type="text" 	name="exDocumentoDTO.numExtDoc" size="32" maxLength="32" value="${exDocumentoDTO.numExtDoc}" class="form-control"/> 
						</div>
					</div>
					<div class="col-sm-2">
						<div class="form-group">
							<label>Data</label>
							<input type="text" name="exDocumentoDTO.dtDocOriginalString" size="10" onblur="javascript:verifica_data(this, true);"  value="${exDocumentoDTO.dtDocOriginalString}" class="form-control" />
						</div>
					</div>
					<c:if test='${exDocumentoDTO.tipoDocumento == "externo"}'>
						<div class="col-sm-2">
							<div class="form-group">
								<input type="hidden" name="campos" value="numAntigoDoc" />
								<label>Nº antigo</label>
								<input type="text" name="exDocumentoDTO.numAntigoDoc" size="32" maxLength="34" value="${exDocumentoDTO.numAntigoDoc}" />
							</div>
						</div>
					</c:if>
				</div>
				<div class="row">
					<input type="hidden" name="campos" value="dtDocOriginalString" />
					<input type="hidden" name="campos" value="numExtDoc" />
					<div class="col-sm-2">
						<div class="form-group">
							<label>Emitente</label>
							<select name="exDocumentoDTO.tipoEmitente" onchange="javascript:sbmt();" class="form-control">
								<c:forEach items="${exDocumentoDTO.listaTipoEmitente}" var="item">
									<option value="${item.key}"
										${item.key == exDocumentoDTO.tipoEmitente ? 'selected' : ''}>
										${item.value}</option>
								</c:forEach>
							</select> 
						</div>
					</div>
					<div class="col-sm-4">
						<div class="form-group">
							<siga:span id="emitente">
								<c:choose>
									<c:when test='${exDocumentoDTO.tipoEmitente == 1}'>
										<input type="hidden" name="campos" value="cpOrgaoSel.id" />
										<siga:selecao propriedade="cpOrgao" inputName="exDocumentoDTO.cpOrgao" tema="simple" modulo="siga" />
										<label for="exDocumentoDTO.nmSubscritorExt"><fmt:message key="documento.subscritor"/></label>									
										<input type="hidden" name="campos" value="nmSubscritorExt" />
										<input type="text" name="exDocumentoDTO.nmSubscritorExt" size="30" maxLength="256" value="${exDocumentoDTO.nmSubscritorExt}" class="form-control"/>
									</c:when>
									<c:when test='${exDocumentoDTO.tipoEmitente == 2}'>
										<input type="hidden" name="campos" value="obsOrgao" />
										<input type="text" size="30" name="exDocumentoDTO.obsOrgao" maxLength="256" value="${exDocumentoDTO.obsOrgao}" class="form-control mt-4" />
									</c:when>
								</c:choose>
							</siga:span>
						</div>
					</div>
				</div>
				</c:if>
				<c:if test='${exDocumentoDTO.tipoDocumento == "interno" or exDocumentoDTO.tipoDocumento == "interno_capturado" or exDocumentoDTO.tipoDocumento == "externo_capturado"}'>
					<c:if test="${exDocumentoDTO.modelo.conteudoTpBlob == 'template/freemarker' or not empty exDocumentoDTO.modelo.nmArqMod}">
						<div class="row">
							<div class="col-sm">
								<siga:span id="spanEntrevista" depende="tipoDestinatario;destinatario;forma;modelo">
								<c:if test="${exDocumentoDTO.modelo.conteudoTpBlob == 'template/freemarker'}">
											${f:processarModelo(exDocumentoDTO.doc, 'entrevista', par, exDocumentoDTO.preenchRedirect)}
								</c:if>
								<c:if test="${exDocumentoDTO.modelo.conteudoTpBlob != 'template/freemarker'}">
									<c:import url="/paginas/expediente/modelos/${exDocumentoDTO.modelo.nmArqMod}?entrevista=1" />
								</c:if>
								</siga:span>
							</div>
						</div>
					</c:if>
				</c:if>	
				<div class="row mt-4">
					<div class="col-sm-8">
						<button id="btnGravar" type="button" onclick="javascript: gravarDoc(); return false;" name="gravar" class="btn btn-primary" accesskey="o"><u>O</u>K</button> 
						<c:if test='${exDocumentoDTO.tipoDocumento == "interno"}'>
							<c:if test="${not empty exDocumentoDTO.modelo.nmArqMod or exDocumentoDTO.modelo.conteudoTpBlob == 'template/freemarker'}">
								<button type="button" name="ver_doc" onclick="javascript: popitup_documento(false); return false;" class="btn btn-info ${hide_only_GOVSP}" accesskey="v"><u>V</u>er Documento</button>
								<button type="button" name="ver_doc_pdf" onclick="javascript: popitup_documento(true); return false;" class="btn btn-info" accesskey="i"><fmt:message key="documento.btn.ver.impressao2"/></button>
								<button type="button" name="voltar" onclick="javascript: history.back();" class="btn btn-info ${hide_only_TRF2}" accesskey="r">Volta<u>r</u></button>
							</c:if>
						</c:if>
					</div>
				</div>
			</form>
			</div>
		</div>
	</div>
	<!--  tabela do rodapé -->
</siga:pagina>

<script type="text/javascript">
	function alterouOrigem() {
		<c:if test="${exDocumentoDTO.doc.codigo == 'NOVO' and exDocumentoDTO.tipoDocumento == 'interno'}">
		retorna_subscritor('', '', '', ''); // remove o subscritor default quando troca a origem
		</c:if>
		document.getElementById('alterouModelo').value = 'true'
	}

	function alterouModeloSelect() {
		var valor = $('input[name="exDocumentoDTO.idMod"]').val();
		var valorOriginal = $('input[name="exDocumentoDTO.idMod.original"]')
				.val();
		if (valor !== '' && valor !== valorOriginal) {
			document.getElementById('alterouModelo').value = 'true';
			sbmt();
		}
	}

	function presskeySelect(event, id, parameter) {
		if (event.type == 'keypress') {
			if (event.keyCode == '13') {
				sbmt(parameter);
			}
		}
	}
	function mouseSelect(event, id, parameter) {
		if (event.type == 'change') {
			var click = document.getElementById('clickSelect').value;
			if (click) {
				sbmt(parameter);
			}
		}
	}

	$(document).ready(function() {
		$('#modelos-select').hierarchySelect({
			width : 'auto',
			height : 'auto'
		});

		personalizacaoSeparar();
	});
	// window.customOnsubmit = function() {return true;};
	// {
	//	var frm = document.getElementById('frm');
	//	if (typeof(frm.submitsave) == "undefined")
	//		frm.submitsave = frm.submit;
	// }

	$('a[data-toggle="tooltip"]').tooltip({
	    placement: 'bottom',
	    trigger: 'click'
	});

</script>