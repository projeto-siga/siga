<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://localhost/functiontag" prefix="fn"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="libs"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@page import="br.gov.jfrj.siga.ex.ExMovimentacao"%>
<%@page import="br.gov.jfrj.siga.ex.ExMobil"%>

<siga:pagina titulo="Painel Administrativo">
<script type="text/javascript" language="Javascript1.1">
function sbmtDoc() {
	sigaSpinner.mostrar();
    document.getElementById('btnSubmitDoc').disabled = true;
   	frm = document.getElementById('frmDoc');
    frm.action = 'exibir?documentoRefSel.sigla=${sigla}&postback=1';
    frm.submit();
    document.getElementById('btnSubmitDoc').disabled = false;
    sigaSpinner.mostrar();
}
</script>
<!-- main content bootstrap -->
<div class="container-fluid content" >
	<div class="card bg-light mb-3">
		<div class="card-header">
			<div class="row">
				<div class="col-8">
					<div class="h4">Painel Administrativo</div>
					<p class="h6">Pesquisa por Documento, Via ou Volume</p>
				</div>
				<div class="col-4">
					<button type="button" name="voltar" onclick="javascript:history.back();" 
							class="btn btn-sm btn-secondary float-right " accesskey="r">Volta<u>r</u></button>			
				</div>
			</div>
		</div>
		<div class="card-body">
			<form id="frmDoc" action="exibir" enctype="multipart/form-data" class="form" method="get">
				<div class="row">
					<input type="hidden" name="postback" value="1" />
					<div class="col-9">
						<div class="form-group">
							<label>Número do Documento</label>
							<siga:selecao tema='simple' titulo="Sigla do Documento" propriedade="documentoRef" urlAcao="expediente/buscar" urlSelecionar="expediente/selecionar" modulo="sigaex" />
						</div>
					</div>
					<div class="col-3 mt-auto">
						<div class="form-group">
							<button id="btnSubmitDoc" type="button" class="btn btn-primary" onclick="sbmtDoc();">
								Pesquisar Documento
							</button>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>	
	<c:if test="${erroSemMobil}">
		<div class="row mb-2">
			<div class="col-12">
				<div class="alert alert-warning" role="alert">
					Atenção: O documento ${documentoRefSel.sigla} está corrompido (faltando mobil de via ou volume)
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC;FE;PAINEL;CORRIGEMOBIL:Corrige Documento sem Mobil de Via ou Volume')}">
						<a class="btn btn-sm btn-primary float-right" title="Corrige falta de via ou volume"
						  href="corrigeDocSemMobil?documentoRefSel.sigla=${documentoRefSel.sigla}"
						  ${popup?'target="_blank" ':''}><i class="fas fa-file-medical mr-2"></i>Corrige Falta de Via ou Volume</a>
					</c:if>
				</div>
			</div>
		</div>
	</c:if>
	<c:if test="${erroSemDescricao}">
		<div class="row mb-2">
			<div class="col-12">
				<div class="alert alert-warning" role="alert">
					Atenção: O documento ${documentoRefSel.sigla} está sem a descri&ccedil;&atilde;o.
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC;FE;PAINEL;CORRIGEMOBIL:Corrige Documento sem Mobil de Via ou Volume')}">
						<a class="btn btn-sm btn-primary float-right" title="Corrige falta de descricao"
						  href="corrigeDocSemDescricao?documentoRefSel.sigla=${documentoRefSel.sigla}"
						  ${popup?'target="_blank" ':''}><i class="fas fa-file-medical mr-2"></i>Corrige Falta de Descri&ccedil;&atilde;o</a>
					</c:if>
				</div>
			</div>
		</div>
	</c:if>
	<c:if test="${erroFilhoSemDescricao}">
		<div class="row mb-2">
			<div class="col-12">
				<div class="alert alert-warning" role="alert">
					Atenção: O documento juntado ${siglaFilho} está sem a descri&ccedil;&atilde;o.
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC;FE;PAINEL;CORRIGEMOBIL:Corrige Documento sem Mobil de Via ou Volume')}">
						<a class="btn btn-sm btn-primary float-right" title="Corrige falta de descricao"
						  href="corrigeDocSemDescricao?documentoRefSel.sigla=${siglaFilho}"
						  ${popup?'target="_blank" ':''}><i class="fas fa-file-medical mr-2"></i>Corrige Falta de Descri&ccedil;&atilde;o</a>
					</c:if>
				</div>
			</div>
		</div>
	</c:if>
<c:if test="${not empty docVO}">
	<div class="card bg-light mb-3">
		<tags:collapse title="${docVO.nomeCompleto}" id="docDados" collapseMode="${collapse_Expanded}">
			<div class="row mb-2">
				<div class="col-12">
					<a class="btn btn-sm btn-primary" title="Atualiza a situação do documento (marcas)"
					  href="${linkTo[ExDocumentoController].aAtualizarMarcasDoc()}?sigla=${docVO.sigla}&redir=/app/expediente/painel/exibir?documentoRefSel.sigla=${docVO.sigla}"
					  ${popup?'target="_blank" ':''}><i class="fas fa-pen-square"></i> Atualizar Marcas</a>
	    		</div>
	    	</div>
			<div class="row">
				<div class="col-md-8">
					<div class="d-box">
						<div class="d-box-title">
							<div class="d-box-col">Dados do Documento</div>
						</div>
						<div class="d-box-content">
							<div class="row">
								<div class="d-box-col col-md-4">
									<div class="text-sm font-weight-bold">Data</div>
									<div class="">${docVO.dtDocDDMMYY}</div>
								</div>
								<div class="d-box-col col-md-4">
									<div class="font-weight-bold">De</div>
									<div class="">${docVO.subscritorString}</div>
								</div>
								<div class="d-box-col col-md-4">
									<div class="font-weight-bold">Espécie</div><div class="">${docVO.forma}</div>
								</div>
							</div>
							<div class="row">
								<div class="d-box-col col-md-12">
									<div class="font-weight-bold">Modelo</div><div class="">${docVO.modelo}</div>
								</div>
							</div>
							<div class="row">
								<div class="d-box-col col-md-12">
									<div class="font-weight-bold">Classificação</div><div class="">${docVO.classificacaoDescricaoCompleta}</div>
								</div>
							</div>
							<div class="row">
								<div class="d-box-col col-md-4">
									<div class="font-weight-bold">Cadastrante</div><div class="">${docVO.cadastranteString} ${docVO.lotaCadastranteString}</div>
								</div>
								<div class="d-box-col col-md-4">
									<c:if test="${not empty docVO.paiSigla}">
										<div class="font-weight-bold">Documento Pai:</div>
										<div class="">
											<a href="${pageContext.request.contextPath}/app/expediente/painel/exibir?documentoRefSel.sigla=${docVO.paiSigla}">
												${docVO.paiSigla}
											</a>
										</div>
									</c:if>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-4">
					<div class="d-box">
						<div class="d-box-title">Nível de Acesso:</div>
						<div class="d-box-content">
							<span>${docVO.nmNivelAcesso}</span> 
							<c:if test="${not empty docVO.listaDeAcessos}">
								<c:choose>
									<c:when test="${docVO.listaDeAcessos.size() eq 1}">
										<c:forEach var="acesso" items="${docVO.listaDeAcessos}" varStatus="loop">
											<c:choose>
												<c:when test="${acesso eq 'PUBLICO'}">
													(Público)
												</c:when>
												<c:otherwise>
													(${acesso.sigla})
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<ul>	
											<c:forEach var="acesso" items="${docVO.listaDeAcessos}" varStatus="loop">
												<li>${acesso.sigla}</li>
											</c:forEach>
										</ul>
									</c:otherwise>
								</c:choose>
							</c:if>						
						</div>
					</div>
				</div>
				<div class="col-md-12">
					<div class="d-box">
						<div class="d-box-title">
							<div class="d-box-col">Dados da Tabela de Documentos</div>
						</div>
						<div class="d-box-content">
							<div class="row">
								<div class="d-box-col col-md-2">
									<div class="text-sm font-weight-bold">IdDoc</div>
									<div class="">${docVO.doc.idDoc}</div>
								</div>
								<div class="d-box-col col-md-2">
									<div class="text-sm font-weight-bold">IdTpDoc</div>
									<div class="">${docVO.doc.exTipoDocumento.idTpDoc} - ${docVO.doc.exTipoDocumento.descrTipoDocumento}</div>
								</div>
								<div class="d-box-col col-md-2">
									<c:if test="${docVO.doc.exMobilPai != null}">
										<div class="text-sm font-weight-bold">IdMobPai</div>
										<div class="">${docVO.doc.exMobilPai.idMobil}</div>
									</c:if>
								</div>
								<div class="d-box-col col-md-4">
									<div class="text-sm font-weight-bold">IdFormaDoc</div>
									<div class="">${docVO.doc.exFormaDocumento.idFormaDoc} - ${docVO.doc.exFormaDocumento.descrFormaDoc}</div>
								</div>
							</div>
							<div class="row bg-light">
								<div class="d-box-col col-12">
									<div class="text-sm font-weight-bold">IdMod</div>
									<div class="">${docVO.doc.exModelo.idMod} - ${docVO.doc.exModelo.nmMod}</div>
								</div>
							</div>
							<div class="row">
								<div class="d-box-col col-md-2">
									<div class="text-sm font-weight-bold">DtDoc</div>
									<div class="">${docVO.doc.dtDoc != null? docVO.doc.dtDoc : '-'}</div>
								</div>
								<div class="d-box-col col-md-2">
									<div class="text-sm font-weight-bold">DtRegDoc</div>
									<div class="">${docVO.doc.dtRegDoc}</div>
								</div>
								<div class="d-box-col col-md-2">
									<div class="text-sm font-weight-bold">DtFinalizacao</div>
									<div class="">${docVO.doc.dtFinalizacao != null? docVO.doc.dtFinalizacao : '-'}</div>
								</div>
								<div class="d-box-col col-md-3">
									<div class="text-sm font-weight-bold">DtPrimeiraAssinatura</div>
									<div class="">${docVO.doc.dtPrimeiraAssinatura != null? docVO.doc.dtPrimeiraAssinatura : '-'}</div>
								</div>
								<div class="d-box-col col-md-2">
									<div class="text-sm font-weight-bold">HisDtAlt</div>
									<div class="">${docVO.doc.hisDtAlt != null? docVO.doc.hisDtAlt : '-'}</div>
								</div>
							</div>
							<div class="row bg-light">
								<div class="d-box-col col-md-3">
									<div class="text-sm font-weight-bold">IdOrgaoUsu</div>
									<div class="">${docVO.doc.orgaoUsuario.idOrgaoUsu}</div>
									<small class="">${docVO.doc.orgaoUsuario.nmOrgaoUsu}</small>
								</div>
								<div class="d-box-col col-md-3">
									<div class="text-sm font-weight-bold">IdCadastrante</div>
									<div class="">${docVO.doc.cadastrante.idPessoa} - ${docVO.doc.cadastrante} </div>
									<small class="">${docVO.doc.cadastrante.nomePessoa}</small>
								</div>
								<div class="d-box-col col-md-3">
									<div class="text-sm font-weight-bold">IdLotaCadastrante</div>
									<div class="">${docVO.doc.lotaCadastrante.idLotacao} - ${docVO.doc.lotaCadastrante} </div>
									<small class="">${docVO.doc.lotaCadastrante.nomeLotacao}</small>
								</div>
							</div>
							<div class="row">
								<div class="d-box-col col-md-3">
									<div class="text-sm font-weight-bold">IdSubscritor</div>
									<div class="">${docVO.doc.subscritor.idPessoa} - ${docVO.doc.subscritor} </div>
									<small class="">${docVO.doc.subscritor.nomePessoa}</small>
								</div>
								<div class="d-box-col col-md-3">
									<div class="text-sm font-weight-bold">IdLotaSubscritor</div>
									<div class="">${docVO.doc.lotaSubscritor.idLotacao} - ${docVO.doc.lotaSubscritor} </div>
									<small class="">${docVO.doc.lotaSubscritor.nomeLotacao}</small>
								</div>
								<div class="d-box-col col-md-3">
									<div class="text-sm font-weight-bold">IdTitular</div>
									<div class="">${docVO.doc.titular.idPessoa} - ${docVO.doc.titular} </div>
									<small class="">${docVO.doc.titular.nomePessoa}</small>
								</div>
								<div class="d-box-col col-md-3">
									<div class="text-sm font-weight-bold">IdLotaTitular</div>
									<div class="">${docVO.doc.lotaTitular.idLotacao} - ${docVO.doc.lotaTitular}</div>
									<small class="">${docVO.doc.lotaTitular.nomeLotacao}</small>
								</div>
							</div>
							<div class="row bg-light">
								<div class="d-box-col col-md-3">
									<div class="text-sm font-weight-bold">dnmDtAcesso</div>
									<div class="">${docVO.doc.dnmDtAcesso}</div>
								</div>
								<div class="d-box-col col-md-3">
									<div class="text-sm font-weight-bold">dnmAcesso</div>
									<div class="">${docVO.doc.dnmAcesso}</div>
								</div>
								<div class="d-box-col col-md-3">
									<div class="text-sm font-weight-bold">IdNivelAcesso</div>
									<div class="" title="${docVO.doc.exNivelAcesso.dscNivelAcesso}">${docVO.doc.exNivelAcesso.idNivelAcesso} - ${docVO.doc.exNivelAcesso.nmNivelAcesso}</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</tags:collapse>
	</div>
			
	<c:set var="primeiroMobil" value="${true}" />
	<c:forEach var="m" items="${docVO.mobs}" varStatus="loop">
		<c:if
			test="${m.mob.geral or true or (((mob.geral or (mob.id == m.mob.id)) and (exibirCompleto or (m.mob.ultimaMovimentacaoNaoCancelada != null) ) ))}">
			<div class="card bg-light mb-3">
				<tags:collapse title="${m.getDescricaoCompletaEMarcadoresEmHtml(cadastrante,lotaTitular)}" id="docMobs-${m.mob.id}" collapseMode="${collapse_Expanded}">
					<div class="d-box-content">
						<div class="row">
							<div class="d-box-col col-md-2">
								<div class="text-sm font-weight-bold">IdMobil</div>
								<div class="">${m.mob.idMobil}</div>
							</div>
							<div class="d-box-col col-md-3">
								<div class="text-sm font-weight-bold">IdTpDoc</div>
								<div class="">${m.mob.exTipoMobil.idTipoMobil} - ${m.mob.exTipoMobil.descTipoMobil}</div>
							</div>
						</div>
						<div class="row">
							<div class="d-box-col col-12">
								<div class="text-sm font-weight-bold">Marcas</div>
								<div class="row">
								<c:forEach var="marca" items="${m.marcasAtivas}" varStatus="loop">
									<div class="col-md-3 d-box mr-1" >
										<div class="d-box-content">
											<div class="row text-size-7">
												<div class="col-12 font-weight-bold text-left">${marca.cpMarcador.idMarcador} - ${marca.cpMarcador.descrMarcador}</div>
											</div>
											<div class="row text-size-8">
												<div class="col-4">${marca.idMarca}</div>
												<div class="col-8">${marca.dtIniMarca}</div> 
											</div>
											<div class="row text-size-8">
												<span class="col-12">${marca.dpPessoaIni} - ${marca.dpPessoaIni.nomePessoa}</span>
												<span class="col-12">${marca.dpLotacaoIni} - ${marca.dpLotacaoIni.nomeLotacao}</span>
											</div>
										</div>
									</div>
								</c:forEach>							
								</div>
							</div>
						</div>
					</div>				
					<c:set var="ocultarCodigo" value="${true}" />
					<c:set var="dtUlt" value="" />
					<c:set var="temmov" value="${false}" />
					<c:forEach var="mov" items="${m.movs}">
							<c:set var="temmov" value="${true}" />
					</c:forEach>
					<c:if test="${temmov}">
						<table class="table table-sm table-hover table-striped mov mt-2 text-size-7">
							<thead class="${thead_color} align-middle text-center">
								<tr>
									<th style="width: 5%" class="text-center" rowspan="2">Data</th>
									<th style="width: 15%" class="text-left" rowspan="2">Evento</th>
									<th style="width: 18%" colspan="2" align="left">Cadastrante</th>
									<th style="width: 18%" colspan="2" align="left">Responsável</th>
									<th rowspan="2">Descrição</th>
									<th style="width: 4%" class="text-center" rowspan="2">IdMov</th>
									<th style="width: 4%" class="text-center" rowspan="2">IdMovCancel</th>
									<th style="width: 4%" class="text-center" rowspan="2">IdMovRef</th>
								</tr>
								<tr>
									<th class="text-left">
										<fmt:message key="usuario.lotacao"/>
									</th>
									<th class="text-left">
										<fmt:message key="usuario.pessoa"/>
									</th>
									<th class="text-left">
										<fmt:message key="usuario.lotacao"/>
									</th>
									<th class="text-left">
										<fmt:message key="usuario.pessoa"/>
									</th>
								</tr>
							</thead>
							<c:forEach var="mov" items="${m.movs}">
								<tr class="${mov.classe} ${mov.disabled}">
									<c:set var="dt" value="${mov.dtRegMovDDMMYYHHMMSS}" />
									<td class="text-center">${dt}</td>
									<td>${mov.idTpMov} - ${mov.descrTipoMovimentacao}</td>
									<td class="text-left">
										<siga:selecionado isVraptor="true" sigla="${mov.parte.lotaCadastrante.siglaOrgao}${mov.parte.lotaCadastrante.sigla}"
 											descricao="${mov.parte.lotaCadastrante.descricaoAmpliada}" 
 											lotacaoParam="${mov.parte.lotaCadastrante.siglaOrgao}${mov.parte.lotaCadastrante.sigla}" /> 
									</td>
									<td class="text-left">
										<siga:selecionado isVraptor="true" sigla="${mov.parte.cadastrante.nomeAbreviado}"
 											descricao="${mov.parte.cadastrante.descricao} - ${mov.parte.cadastrante.sigla}" 
 											pessoaParam="${mov.parte.cadastrante.sigla}" /> 
									</td>
									<td class="text-left">
										<siga:selecionado isVraptor="true" sigla="${mov.parte.lotaResp.siglaOrgao}${mov.parte.lotaResp.sigla}"
											descricao="${mov.parte.lotaResp.descricaoAmpliada}" 
											lotacaoParam="${mov.parte.lotaResp.siglaOrgao}${mov.parte.lotaResp.sigla}" />
									</td>
									<td class="text-left">
										<siga:selecionado isVraptor="true" sigla="${mov.parte.resp.nomeAbreviado}"
											descricao="${mov.parte.resp.descricao} - ${mov.parte.resp.sigla}" 
											pessoaParam="${mov.parte.resp.sigla}"/>
									</td>
									
									<td>
										${mov.descricao}
										<c:if test='${mov.idTpMov != 2}'>
											${mov.complemento}
										</c:if>
										<c:set var="assinadopor" value="${true}" />
										<siga:links buttons="${false}" inline="${true}"
											separator="${not empty mov.descricao and mov.descricao != null}">
											<c:forEach var="acao" items="${mov.acoes}">
												<c:if test='${not(fn:contains(acao.nomeNbsp, "Cancelar") 
													or fn:contains(acao.nomeNbsp, "Excluir")
													or fn:contains(acao.nomeNbsp, "Ver")
													or fn:contains(acao.nomeNbsp, "Ver/Assinar"))}'>
													<siga:link classe="text-size-8" title="${acao.nomeNbsp}"
														pre="${acao.pre}" pos="${acao.pos}"
														url="${pageContext.request.contextPath}${fn:replace(acao.url, '/doc/exibir?sigla=', 
														'/painel/exibir?documentoRefSel.sigla=')}" />
													<c:if test='${assinadopor and mov.idTpMov == 2}'>
														${mov.complemento}
														<c:set var="assinadopor" value="${false}" />
													</c:if>
												</c:if>
											</c:forEach>
										</siga:links>
									</td>
									<td class="text-center bg-white" title="${mov.mov.dtTimestamp}">
										${mov.idMov}
									</td>
									<td class="text-center bg-white">
										${mov.mov.exMovimentacaoCanceladora.idMov}
									</td>
									<td class="text-center bg-white">
										${mov.mov.exMovimentacaoRef.idMov}
									</td>
								</tr>
							</c:forEach>
						</table>
					</c:if>
					
					<c:if test="${not empty m.expedientesFilhosNaoCancelados}">
						<div class="docFilhos">
							<div class="apensados">
								<div class="col">Documento
									<c:if test="${m.apensos.size() gt 1}">s</c:if>
									<fmt:message key="documento.filho"/>
									<c:if test="${m.apensos.size() gt 1}">s</c:if>:
								</div>
								<c:forEach var="docFilho" items="${m.expedientesFilhosNaoCancelados}">
									<div class="d-box">
										<div class="d-box-title">
											<div class="d-box-col">
												<a class="${docFilho.doc.cancelado?'disabled':''}" 
													href="${pageContext.request.contextPath}/app/expediente/painel/exibir?documentoRefSel.sigla=${docFilho.sigla}" 
													title="${docFilho.descrDocumento}">
													<b>${docFilho.sigla} ${docFilho.doc.cancelado?'(Cancelado)':''}</b>
												</a>
											</div>
										</div>
										<div class="d-box-content">
											<div class="row">
												<div class="d-box-col col-md-2">
													<div class="text-sm font-weight-bold">IdDoc</div>
													<div class="">${docFilho.doc.idDoc}</div>
												</div>
												<div class="d-box-col col-md-4">
													<div class="text-sm font-weight-bold">DtDoc</div>
													<div class="">${docFilho.doc.dtDoc != null? docFilho.doc.dtDoc : '-'}</div>
												</div>
												<div class="d-box-col col-md-6">
													<div class="text-sm font-weight-bold">IdFormaDoc</div>
													<div class="">${docFilho.doc.exFormaDocumento.idFormaDoc} - ${docFilho.doc.exFormaDocumento.descrFormaDoc}</div>
												</div>
											</div>
											<div class="row">
												<div class="d-box-col col-12">
													<div class="text-sm font-weight-bold">IdMod</div>
													<div class="">${docFilho.doc.exModelo.idMod} - ${docFilho.doc.exModelo.nmMod}</div>
												</div>
											</div>
										</div>
									</div>
								</c:forEach>
							</div>
						</div>
					</c:if>
					<c:if test="${not empty m.processosFilhosNaoCancelados}">
						<div class="docSubProcessos">
							<div class="apensados">Subprocesso
								<c:if test="${m.apensos.size() gt 1}">s</c:if>:
								<c:forEach var="docFilho" items="${m.processosFilhosNaoCancelados}">
									<c:if test="${!docFilho.doc.cancelado}">
										<div class="d-box">
											<div class="d-box-title">
												<div class="d-box-col">											
													<a class="${docFilho.doc.cancelado?'disabled':''}" 
														href="${pageContext.request.contextPath}/app/expediente/painel/exibir?documentoRefSel.sigla=${docFilho.sigla}" 
														title="${docFilho.descrDocumento}">
														<b>${docFilho.siglaCurtaSubProcesso} ${docFilho.doc.cancelado?'(Cancelado)':''}</b>
													</a>
												</div>
											</div>
											<div class="d-box-content">
												<div class="row">
													<div class="d-box-col col-md-2">
														<div class="text-sm font-weight-bold">IdDoc</div>
														<div class="">${docFilho.doc.idDoc}</div>
													</div>
													<div class="d-box-col col-md-4">
														<div class="text-sm font-weight-bold">DtDoc</div>
														<div class="">${docFilho.doc.dtDoc != null? docFilho.doc.dtDoc : '-'}</div>
													</div>
													<div class="d-box-col col-md-6">
														<div class="text-sm font-weight-bold">IdFormaDoc</div>
														<div class="">${docFilho.doc.exFormaDocumento.idFormaDoc} - ${docFilho.doc.exFormaDocumento.descrFormaDoc}</div>
													</div>
												</div>
												<div class="row">
													<div class="d-box-col col-12">
														<div class="text-sm font-weight-bold">IdMod</div>
														<div class="">${docFilho.doc.exModelo.idMod} - ${docFilho.doc.exModelo.nmMod}</div>
													</div>
												</div>
											</div>
										</div>
									</c:if>
								</c:forEach>
							</div>
						</div>
					</c:if>	
					<c:if test="${not empty m.apensos}">
						<div class="docApensados">
							<div class="apensados">Documento
								<c:if test="${m.apensos.size() gt 1}">s</c:if>Apensado
								<c:if test="${m.apensos.size() gt 1}">s</c:if>:
								<c:forEach var="mobItem" items="${m.apensos}">
									<div class="d-box">
										<div class="d-box-title">
											<div class="d-box-col">											
												<a href="${pageContext.request.contextPath}/app/expediente/painel/exibir?documentoRefSel.sigla=${mobItem.sigla}" 
													title="${mobItem.mob.doc.descrDocumento}">
													<b>${mobItem.sigla}</b>
												</a>
											</div>
										</div>
										<div class="d-box-content">
											<div class="row">
												<div class="d-box-col col-md-2">
													<div class="text-sm font-weight-bold">IdDoc</div>
													<div class="">${mobItem.mob.doc.idDoc}</div>
												</div>
												<div class="d-box-col col-md-4">
													<div class="text-sm font-weight-bold">DtDoc</div>
													<div class="">${mobItem.mob.doc.dtDoc != null? mobItem.mob.doc.dtDoc : '-'}</div>
												</div>
												<div class="d-box-col col-md-6">
													<div class="text-sm font-weight-bold">IdFormaDoc</div>
													<div class="">${mobItem.mob.doc.exFormaDocumento.idFormaDoc} - ${mobItem.mob.doc.exFormaDocumento.descrFormaDoc}</div>
												</div>
											</div>
											<div class="row">
												<div class="d-box-col col-12">
													<div class="text-sm font-weight-bold">IdMod</div>
													<div class="">${mobItem.mob.doc.exModelo.idMod} - ${mobItem.mob.doc.exModelo.nmMod}</div>
												</div>
											</div>
										</div>
									</div>
								</c:forEach>
							</div>
						</div>
					</c:if>
				</tags:collapse>
			</div>
		</c:if>
	</c:forEach>
</c:if>
</div>	
</siga:pagina>
