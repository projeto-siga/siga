<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Novo Documento">
<link rel="stylesheet" href="/siga/javascript/hierarchy-select/hierarchy-select.css" type="text/css" media="screen, projection"/>

<script type="text/javascript" src="/ckeditor/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="../../../javascript/exDocumentoEdita.js"></script>
<script type="text/javascript" src="/siga/javascript/jquery.blockUI.js"></script>
<script src="/siga/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/siga/javascript/hierarchy-select/hierarchy-select.js"></script>
<div class="gt-bd clearfix">
	<div class="gt-content clearfix">
	
		<h2>
			<c:choose>
				<c:when test='${empty exDocumentoDTO.doc}'>
					Novo Documento
				</c:when>
				<c:otherwise>
					<span id="codigoDoc">${exDocumentoDTO.doc.codigo}</span>
					<!-- de: <span id="dataDoc">${exDocumentoDTO.doc.dtRegDocDDMMYY}</span>-->
				</c:otherwise>
			</c:choose>
		</h2>
				
		<div class="gt-content-box gt-for-table">
			<form id="frm" name="frm" theme="simple" method="post" enctype="multipart/form-data">
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

				<table class="gt-form-table">
					<tr class="header">
						<c:choose>
							<c:when test='${empty exDocumentoDTO.doc}'>
								<td colspan="4">Novo Documento</td>
							</c:when>
							<c:otherwise>
								<td colspan="4">Dados básicos:</td>
							</c:otherwise>
						</c:choose>
					</tr>
					<c:choose>
						<c:when test="${(exDocumentoDTO.doc.eletronico) && (exDocumentoDTO.doc.numExpediente != null)}">
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
					<c:if test='${ exDocumentoDTO.tipoDocumento != "externo"}'>
						<c:choose>
							<c:when test="${possuiMaisQueUmModelo}">
								<tr>
									<td>Modelo:</td>
									<td colspan="3">
										<div class="btn-group hierarchy-select" data-resize="auto"
											id="modelos-select">
											<button type="button" class="btn btn-default dropdown-toggle"
												data-toggle="dropdown">
												<span class="selected-label pull-left">&nbsp;</span> <span
													class="caret"></span> <span class="sr-only">Toggle
													Dropdown</span>
											</button>
											<div class="dropdown-menu open">
												<div class="hs-searchbox">
													<input type="text" class="form-control" autocomplete="off" placeholder="Pesquisar modelo...">
												</div>
												<ul class="dropdown-menu inner" role="menu">
													<c:forEach items="${hierarquiaDeModelos}" var="item">
														<li data-value="${item.value}" data-level="${item.level}"
															data-search="${item.searchText}"
															${item.group ? 'data-group' : ''}
															${item.selected ? 'data-default-selected' : ''}><a
															href="#">${item.text}</a></li>
													</c:forEach>
												</ul>
											</div>
											<input class="hidden hidden-field" name="exDocumentoDTO.idMod"
												readonly="readonly" onchange="alterouModeloSelect()"
												aria-hidden="true" type="text" value="${exDocumentoDTO.idMod}"/>
										</div>
		
									</td>
								</tr>
							</c:when>
							<c:otherwise>
								<input type="hidden" name="exDocumentoDTO.idMod" value="${exDocumentoDTO.modelo.idMod}" />
							</c:otherwise>
						</c:choose>
					</c:if>
					<c:if test='${exDocumentoDTO.tipoDocumento == "externo" }'>
						<input type="hidden" name="exDocumentoDTO.idMod" value="${exDocumentoDTO.idMod}"/>
					</c:if>
					
					<!-- Origem -->
					<tr style="display: ${((exDocumentoDTO.tiposDocumento).size() != 1 or (exDocumentoDTO.tipoDocumento != 'interno_capturado' and podeEditarData) or (exDocumentoDTO.listaNivelAcesso.size() != 1) or (!exDocumentoDTO.eletronicoFixo)) ? 'table-row' : 'none'};">
						<td width="10%"></td>
						<td colspan="3">
							<span style="margin-right: 1em; display: ${(exDocumentoDTO.tiposDocumento).size() != 1 ? 'inline' : 'none'};">Origem:
							<select  name="exDocumentoDTO.idTpDoc" onkeypress="presskeySelect(event, this, null)" onmousedown="javascript:document.getElementById('clickSelect').value='true';"
							onchange="alterouOrigem(); mouseSelect(event, this, null)" style="${estiloTipo}">
								<c:forEach items="${exDocumentoDTO.tiposDocumento}" var="item">
									<option value="${item.idTpDoc}" ${item.idTpDoc == exDocumentoDTO.idTpDoc ? 'selected' : ''}>
										${item.descrTipoDocumento}
									</option>  
								</c:forEach>
							</select>
							</span>
							<span style="margin-right: 1em; display: ${exDocumentoDTO.tipoDocumento != 'interno_capturado' and podeEditarData ? 'inline' : 'none'};">
								<input type="hidden" name="campos" value="dtDocString" />
									Data:
								<input type="text" name="exDocumentoDTO.dtDocString" size="10" onblur="javascript:verifica_data(this, true);" value="${exDocumentoDTO.dtDocString}" /> &nbsp;&nbsp; 
							</span>
							<span style="margin-right: 1em; display: ${(exDocumentoDTO.listaNivelAcesso).size() != 1 ? 'inline' : 'none'};">
								<input type="hidden" name="campos" value="nivelAcesso" />
								Acesso 
								<select  name="exDocumentoDTO.nivelAcesso" >
									<c:forEach items="${exDocumentoDTO.listaNivelAcesso}" var="item">
										<option value="${item.idNivelAcesso}" ${item.idNivelAcesso == exDocumentoDTO.nivelAcesso ? 'selected' : ''}>
											${item.nmNivelAcesso}
										</option>  
									</c:forEach>
								</select>								 
							</span>
							<span style="float: right; display: ${exDocumentoDTO.eletronicoFixo ? 'none' : 'inline'};">
								<input type="hidden" name="campos" value="eletronico" /> 
									
								<c:choose>
									<c:when test="${exDocumentoDTO.eletronicoFixo}">
										<input type="hidden" name="exDocumentoDTO.eletronico" id="eletronicoHidden" value="${exDocumentoDTO.eletronico}" />
										${exDocumentoDTO.eletronicoString}
										<c:if test="${exDocumentoDTO.eletronico == 2}">
											<script type="text/javascript">
											$("html").addClass("fisico");$("body").addClass("fisico");
											</script>
										</c:if>
									</c:when>
									<c:otherwise>
									    <input type="radio" name="exDocumentoDTO.eletronico" id="eletronicoCheck1" value="1" 
									    	onchange="javascript:setFisico();" 
									    	<c:if test="${exDocumentoDTO.eletronicoFixo}">disabled</c:if>
									    	<c:if test="${exDocumentoDTO.eletronico == 1}">checked</c:if>	
									    />
									    <label for="eletronicoCheck1">Digital</label>
									    
									    <input type="radio" name="exDocumentoDTO.eletronico" id="eletronicoCheck2" value="2" 
									    	onchange="javascript:setFisico();" 
									    	<c:if test="${exDocumentoDTO.eletronicoFixo}">disabled</c:if>
									    	<c:if test="${exDocumentoDTO.eletronico == 2}">checked</c:if>
									    />
									    <label for="eletronicoCheck2">Físico</label>
									    
										<script type="text/javascript">
											function setFisico() {
												if ($('input[name=exDocumentoDTO\\.eletronico]:checked').val() == 2) {
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
							</span>
						</td>
					</tr>

					
					<c:if test='${exDocumentoDTO.tipoDocumento == "antigo"}'>
						<tr>
							<td>Nº original:</td>
							<input type="hidden" name="campos" value="numExtDoc" />
							<td colspan="3">
								<input type="text" name="exDocumentoDTO.numExtDoc" size="16" maxLength="32" value="${exDocumentoDTO.numExtDoc}"/>
							</td>
						</tr>
						<tr style="font-weight: bold">
							<td>Nº antigo:</td>
							<input type="hidden" name="campos" value="numAntigoDoc" />
							<td colspan="3">
								<input type="text" name="exDocumentoDTO.numAntigoDoc" size="16" maxLength="32" value="${exDocumentoDTO.numAntigoDoc}"/> 
								(informar o número do documento no antigo sistema de controle de expedientes ou de processos administrativos [SISAPA] ou [PROT])
							</td>
						</tr>
					</c:if>
					<c:if test='${exDocumentoDTO.tipoDocumento == "externo" or exDocumentoDTO.tipoDocumento == "externo_capturado"}'>
						<tr>
							<td>Nº original:</td>
							<input type="hidden" name="campos" value="dtDocOriginalString" />
							<td colspan="3">
								<input type="hidden" name="campos" value="numExtDoc" /><input type="text" name="exDocumentoDTO.numExtDoc" size="32" maxLength="32" value="${exDocumentoDTO.numExtDoc}"/>
								<span style="margin-left: 1em;">Data: <input type="text" name="exDocumentoDTO.dtDocOriginalString" size="10" onblur="javascript:verifica_data(this, true);" value="${exDocumentoDTO.dtDocOriginalString}"/></span>
								<span style="margin-left: 1em;">Órgão: <input type="hidden" name="campos" value="cpOrgaoSel.id" /><siga:selecao propriedade="cpOrgao" inputName="exDocumentoDTO.cpOrgao" tema="simple" modulo="siga"/></span>
							</td>
						</tr>
						<c:if test='${exDocumentoDTO.tipoDocumento == "externo"}'>
							<tr>
								<td>Obs. sobre o Órgão Externo:</td>
								<input type="hidden" name="campos" value="obsOrgao" />
								<td colspan="3">
									<input type="text" size="120" name="exDocumentoDTO.obsOrgao" maxLength="256" value="${exDocumentoDTO.obsOrgao}"/>
								</td>
							</tr>
							<tr>
								<td>Nº antigo:</td>
								<input type="hidden" name="campos" value="numAntigoDoc" />
								<td colspan="3">
									<input type="text" name="exDocumentoDTO.numAntigoDoc" size="32" maxLength="34" value="${exDocumentoDTO.numAntigoDoc}" /> 
									(informar o número do documento no antigo sistema de controle de expedientes, caso tenha sido cadastrado)
								</td>
							</tr>
						</c:if>
					</c:if>
					<input type="hidden" name="exDocumentoDTO.desativarDocPai" value="${exDocumentoDTO.desativarDocPai}" />
					<tr style="display: none;">
						<td>Documento Pai:</td>
						<td colspan="3">
							<siga:selecao titulo="Documento Pai:" propriedade="mobilPai" inputName="exDocumentoDTO.mobilPai" tema="simple" modulo="sigaex" desativar="${exDocumentoDTO.desativarDocPai}" reler="sim" />
						</td>
					</tr>
					<tr>
						<c:choose>
							<c:when test='${exDocumentoDTO.tipoDocumento == "externo" or exDocumentoDTO.tipoDocumento == "externo_capturado"}'>
								<td>Subscritor:</td>
								<input type="hidden" name="campos" value="nmSubscritorExt" />
								<td colspan="3">
									<input type="text" name="exDocumentoDTO.nmSubscritorExt" size="80" maxLength="256" value="${exDocumentoDTO.nmSubscritorExt}"/>
								</td>
							</c:when>
							<c:otherwise>
								<input type="hidden" id="temCossignatarios" value="${not empty exDocumentoDTO.doc.cosignatarios}" />
								<td>Subscritor:</td>
								<input type="hidden" name="campos" value="subscritorSel.id" />
								<input type="hidden" name="campos" value="substituicao" />
								<input type="hidden" name="campos" value="personalizacao" />
								<td colspan="3">
									<siga:selecao propriedade="subscritor" inputName="exDocumentoDTO.subscritor" modulo="siga" tema="simple" />&nbsp;&nbsp;
									<input type="checkbox" name="exDocumentoDTO.substituicao" onclick="javascript:displayTitular(this);"
										<c:if test="${exDocumentoDTO.substituicao}">checked</c:if>/>
									Substituto&nbsp;&nbsp;
									<input type="checkbox" name="exDocumentoDTO.personalizacao" onclick="javascript:displayPersonalizacao(this);"
										<c:if test="${exDocumentoDTO.personalizacao}">checked</c:if>/>
									Personalizar
								</td>
							</c:otherwise> 
						</c:choose>
					</tr>
					<tr id="tr_titular" style="display: ${exDocumentoDTO.substituicao ? '' : 'none'};">
						<td>Titular:</td>
						<input type="hidden" name="campos" value="titularSel.id" />
						<td colspan="3">
							<siga:selecao propriedade="titular" inputName="exDocumentoDTO.titular" tema="simple" modulo="siga"/>
						</td>
					</tr>
					<tr id="tr_personalizacao" style="display: ${exDocumentoDTO.personalizacao ? '' : 'none'};">
						<td>Personalização:</td>
						<td colspan="3">
							<input type="hidden" name="campos" value="nmFuncaoSubscritor" />
							<input type="text" name="exDocumentoDTO.nmFuncaoSubscritor" size="50" maxlength="128" id="frm_nmFuncaoSubscritor" value="${exDocumentoDTO.nmFuncaoSubscritor}">							
							(Opcionalmente informe forma: Função;Lotação;Localidade;Nome)
						</td>
					</tr>
					
<c:if test='${exDocumentoDTO.tipoDocumento != "interno_capturado" }'>
					<tr>
						<td>Destinatário:</td>
						<input type="hidden" name="campos" value="tipoDestinatario" />
						<td colspan="3">
							
							<select  name="exDocumentoDTO.tipoDestinatario" onchange="javascript:sbmt();">
							
								<c:forEach items="${exDocumentoDTO.listaTipoDest}" var="item">
									<option value="${item.key}" ${item.key == exDocumentoDTO.tipoDestinatario ? 'selected' : ''}>
										${item.value}
									</option>  
								</c:forEach>
							</select>			
							
							<siga:span id="destinatario" depende="tipoDestinatario">
								<c:choose>
									<c:when test='${exDocumentoDTO.tipoDestinatario == 1}'>
										<input type="hidden" name="campos" value="destinatario" />
										<siga:selecao propriedade="destinatario" inputName="exDocumentoDTO.destinatario" tema="simple" idAjax="destinatario" reler="ajax" modulo="siga" />
										<!--  idAjax="destinatario"  -->										    
									</c:when>
									<c:when test='${exDocumentoDTO.tipoDestinatario == 2}'>
										<input type="hidden" name="campos" value="lotacaoDestinatarioSel.id" />
										<siga:selecao propriedade="lotacaoDestinatario" inputName="exDocumentoDTO.lotacaoDestinatario" tema="simple" idAjax="destinatario2" reler="ajax" modulo="siga" />
										</td>			
										<!--  idAjax="destinatario" -->				   
									</c:when>
									<c:when test='${exDocumentoDTO.tipoDestinatario == 3}'>
										<input type="hidden" name="campos" value="orgaoExternoDestinatarioSel.id" />
										<siga:selecao propriedade="orgaoExternoDestinatario" inputName="exDocumentoDTO.orgaoExternoDestinatario" tema="simple" idAjax="destinatario3" reler="ajax" modulo="siga" />
										<!--  idAjax="destinatario" -->
										<br>
										<input type="text" name="exDocumentoDTO.nmOrgaoExterno" size="120" maxLength="256" value="${exDocumentoDTO.nmOrgaoExterno}"/>
										<input type="hidden" name="campos" value="nmOrgaoExterno" />
										</td>	
									</c:when>
									<c:otherwise>
										<input type="text" name="exDocumentoDTO.nmDestinatario" size="80" maxLength="256" value="${exDocumentoDTO.nmDestinatario}"/>
										<input type="hidden" name="campos" value="nmDestinatario" />
										</td>
									</c:otherwise>
								</c:choose>
							</siga:span>
					</tr>
</c:if>

					<tr style="display:<c:choose><c:when test="${exDocumentoDTO.modelo.exClassificacao!=null}">none</c:when><c:otherwise>visible</c:otherwise></c:choose>">
						<td>Classificação:</td>
						<c:if test="${exDocumentoDTO.modelo.exClassificacao!=null}">
							<c:set var="desativarClassif" value="sim" />
						</c:if>
						<td colspan="3">
						    <input type="hidden" name="campos" value="classificacaoSel.id" />
							<siga:span id="classificacao" depende="forma;modelo">
							<!-- OI -->
							<siga:selecao desativar="${desativarClassif}" modulo="sigaex" propriedade="classificacao"  inputName="exDocumentoDTO.classificacao" urlAcao="buscar" urlSelecionar="selecionar" tema="simple" />
							<!--  idAjax="classificacao" -->
						</siga:span></td>
					</tr>
					<c:if
						test="${exDocumentoDTO.classificacaoSel.id!=null && exDocumentoDTO.classificacaoIntermediaria}">
						<tr>
							<td>Descrição da Classificação:</td>
							<td colspan="3">
								<siga:span id="descrClassifNovo" depende="forma;modelo;classificacao">
								<input type="text" name="exDocumentoDTO.descrClassifNovo" size="80" value="${exDocumentoDTO.descrClassifNovo}" maxLength="4000" />
							</siga:span></td>
						</tr>
					</c:if>
					<c:choose>
						<c:when test='${exDocumentoDTO.modelo.descricaoAutomatica or (not podeEditarDescricao)}'>
    					   <c:set var="displayDescricao" value="none" />
						</c:when>
						<c:otherwise>
            				<c:set var="displayDescricao" value="visible" />
						</c:otherwise>
					</c:choose>
					<tr style="display:${displayDescricao}">
					    <c:if test="${exDocumentoDTO.modelo.descricaoAutomatica or (not podeEditarDescricao)}">
							<input type="hidden" id="descricaoAutomatica" value="sim" />
						</c:if>
						<input type="hidden" name="campos" value="descrDocumento" />
						<td>Descrição:</td>
						<td colspan="3">
							<textarea name="exDocumentoDTO.descrDocumento" cols="80" rows="2" id="descrDocumento" cssClass="gt-form-textarea" value="${exDocumentoDTO.descrDocumento}" >${exDocumentoDTO.descrDocumento}</textarea> <br>
							<span><b>(preencher o campo acima com palavras-chave, sempre usando substantivos, gênero masculino e singular)</b></span>
						</td>
					</tr>
					
					<c:if test='${empty exDocumentoDTO.doc.idDoc and exDocumentoDTO.tipoDocumento == "interno_capturado" or  exDocumentoDTO.tipoDocumento == "externo_capturado"}'>
						<tr>
							<input type="hidden" name="campos" value="descrDocumento" />
							<td>Arquivo PDF:</td>
							<td colspan="3">
								<input type="file" name="arquivo" accept="application/pdf" onchange="testpdf(this.form)" />
							</td>
						</tr>
					</c:if>
				
					<c:if test='${exDocumentoDTO.tipoDocumento == "interno" or exDocumentoDTO.tipoDocumento == "interno_capturado" or exDocumentoDTO.tipoDocumento == "externo_capturado"}'>
						<c:if test="${exDocumentoDTO.modelo.conteudoTpBlob == 'template/freemarker' or not empty exDocumentoDTO.modelo.nmArqMod}">
							<tr class="header">
								<td colspan="4">Dados complementares</td>
							</tr>
							<tr>
								<td colspan="4">
									<siga:span id="spanEntrevista" depende="tipoDestinatario;destinatario;forma;modelo">
										<c:if test="${exDocumentoDTO.modelo.conteudoTpBlob == 'template/freemarker'}">
											${f:processarModelo(exDocumentoDTO.doc, 'entrevista', par, exDocumentoDTO.preenchRedirect)}
										</c:if>
										<c:if test="${exDocumentoDTO.modelo.conteudoTpBlob != 'template/freemarker'}">
											<c:import url="/paginas/expediente/modelos/${exDocumentoDTO.modelo.nmArqMod}?entrevista=1" />
										</c:if>
									</siga:span>
								</td>
							</tr>
						</c:if>
					</c:if>
					<tr>
						<td colspan="4">
							<input type="button" onclick="javascript: gravarDoc();" name="gravar" value="Ok" class="gt-btn-small gt-btn-left"/>
						 	<c:if test='${exDocumentoDTO.tipoDocumento == "interno"}'>
							<c:if test="${not empty exDocumentoDTO.modelo.nmArqMod or exDocumentoDTO.modelo.conteudoTpBlob == 'template/freemarker'}">
								<input type="button" name="ver_doc" value="Visualizar o Documento" onclick="javascript: popitup_documento(false);" class="gt-btn-large gt-btn-left"/>
								<input type="button" name="ver_doc_pdf" onclick="javascript: popitup_documento(true);" value="Visualizar a Impressão" class="gt-btn-large gt-btn-left"/>
							</c:if>
						</c:if></td>
					</tr>
				</table>
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
	document.getElementById('alterouModelo').value='true'
}

function alterouModeloSelect() {
	var valor = $('input[name="exDocumentoDTO.idMod"]').val();
	var valorOriginal = $('input[name="exDocumentoDTO.idMod.original"]').val();
	if (valor !== '' && valor !== valorOriginal) {
		document.getElementById('alterouModelo').value='true';
		sbmt();
	}
}

function presskeySelect(event, id, parameter) {
    if (event.type == 'keypress') {
        if(event.keyCode == '13'){
        	sbmt(parameter);
        }
    } 
}
function mouseSelect(event, id, parameter) {	
	if (event.type == 'change') {
        var click = document.getElementById('clickSelect').value;
        if(click){
			sbmt(parameter);
        }
    }
}

$(document).ready(function() {
    $('#modelos-select').hierarchySelect({
        width: 'auto',
        height: 'auto'
    });
});
// window.customOnsubmit = function() {return true;};
// {
//	var frm = document.getElementById('frm');
//	if (typeof(frm.submitsave) == "undefined")
//		frm.submitsave = frm.submit;
// }
</script>