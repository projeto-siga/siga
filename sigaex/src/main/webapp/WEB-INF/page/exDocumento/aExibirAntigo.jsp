<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="libs"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@page import="br.gov.jfrj.siga.ex.ExMovimentacao"%>
<%@page import="br.gov.jfrj.siga.ex.ExMobil"%>

<c:if test="${not docVO.digital}">
	<script type="text/javascript">
		$("html").addClass("fisico");
		$("body").addClass("fisico");
	</script>
</c:if>
<siga:pagina titulo="Documento" popup="${popup}" >
<input type="hidden" id="visualizador" value="${f:resource('/sigaex.pdf.visualizador') }"/>
<!-- main content bootstrap -->
<div class="container-fluid content" id="page">
	<c:if test="${not empty msg}">
		<div class="row mt-3">
			<p align="center">
				<b>${msg}</b>
			</p>
		</div>
	</c:if>
	<div class="row mt-3">
		<div class="col">
			<h2>
				<c:if test="${empty ocultarCodigo}">
					${docVO.sigla}
				</c:if>
			</h2>
		</div>
	</div>
	<c:set var="primeiroMobil" value="${true}" />
	<c:forEach var="m" items="${docVO.mobs}" varStatus="loop">
		<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;WF:Módulo de Workflow')}">
			<script type="text/javascript">
				var url = "/sigawf/app/doc?sigla=${m.sigla}&ts=1${currentTimeMillis}";
	            $.ajax({
	                url: url,
	                type: "GET"
	            }).fail(function(jqXHR, textStatus, errorThrown){
	                var div = $(".wf_div${m.mob.codigoCompacto}:last");
					$(div).html(errorThrown);
	            }).done(function(data, textStatus, jqXHR ){
	                var div = $(".wf_div${m.mob.codigoCompacto}:last");
					$(div).html(data);
	            });
			</script>
		</c:if>
		
		<c:if
			test="${m.mob.geral or true or (((mob.geral or (mob.id == m.mob.id)) and (exibirCompleto or (m.mob.ultimaMovimentacaoNaoCancelada != null) ) ))}">
			<h3 style="margin-top: 10px; margin-bottom: 0px;">
				${m.getDescricaoCompletaEMarcadoresEmHtml(cadastrante,lotaTitular)}
				<c:if test="${docVO.digital and not empty m.tamanhoDeArquivo}">
				 	- ${m.tamanhoDeArquivo}
				 </c:if>
			</h3>
			<c:set var="ocultarCodigo" value="${true}" />
			<c:if test='${popup!="true"}'>
				<c:set var="acoes" value="${m.acoesOrdenadasPorNome}" />
				<siga:links>
					<c:forEach var="acao" items="${acoes}">
						<siga:link icon="${acao.icone}" title="${acao.nomeNbsp}"
								pre="${acao.pre}" pos="${acao.pos}"
								url="${pageContext.request.contextPath}${acao.url}"
								popup="${acao.popup}" confirm="${acao.msgConfirmacao}"
								classe="${acao.classe}" estilo="line-height: 160% !important"
								atalho="${true}" modal="${acao.modal}"
								explicacao="${acao.explicacao}" post="${acao.post}"
								test="${acao.pode}" />
					</c:forEach>
				</siga:links>
			</c:if>

			<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;WF:Módulo de Workflow')}">
				<c:if test="${(not m.mob.geral)}">
					<div id="${m.sigla}" depende=";wf;" class="wf_div${m.mob.codigoCompacto}" >
					</div>
				</c:if>
				<!--ajax:${m.sigla}-${i}-->
				<!--/ajax:${m.sigla}-${i}-->
			
			</c:if>
			<c:set var="dtUlt" value="" />
			<c:set var="temmov" value="${false}" />
			<c:forEach var="mov" items="${m.movs}">
				<c:if test="${ (exibirCompleto == true) or (mov.idTpMov != 14 and not mov.cancelada)}">
					<c:set var="temmov" value="${true}" />
				</c:if>
			</c:forEach>
			<c:if test="${temmov}">
					<table class="table table-sm table-hover table-striped mov mt-2">
						<thead class="${thead_color} align-middle text-center">
							<tr>
								<th class="text-center" rowspan="2">
									Data
								</th>
								<th rowspan="2">
									Evento
								</th>
								<th colspan="2" align="left">
									Cadastrante
								</th>
								<c:if test="${ (exibirCompleto == 'true')}">
									<th colspan="2" align="left">
										Responsável
									</th>
								</c:if>
								<th colspan="2" align="left">
									Atendente
								</th>
								<th rowspan="2">
									Descrição
								</th>
								<th class="text-center" rowspan="2">
									Duração
								</th>
							</tr>
							<tr>
								<th class="text-left">
									<fmt:message key="usuario.lotacao"/>
								</th>
								<th class="text-left">
									<fmt:message key="usuario.pessoa"/>
								</th>
								<c:if test="${ (exibirCompleto == 'true')}">
									<th class="text-left">
										<fmt:message key="usuario.lotacao"/>
									</th>
									<th class="text-left">
										<fmt:message key="usuario.pessoa"/>
									</th>
								</c:if>
								<th class="text-left">
									<fmt:message key="usuario.lotacao"/>
								</th>
								<th class="text-left">
									<fmt:message key="usuario.pessoa"/>
								</th>
							</tr>
						</thead>
						<c:set var="evenorodd" value="odd" />
						<c:forEach var="mov" items="${m.movs}">
							<c:if test="${ (exibirCompleto == true) or (mov.idTpMov != 14 and not mov.cancelada)}">
								<tr class="${mov.classe} ${mov.disabled}">
									<c:if test="${ (exibirCompleto == 'true')}">
										<c:set var="dt" value="${mov.dtRegMovDDMMYYHHMMSS}" />
									</c:if>
									<c:if test="${ (exibirCompleto != 'true')}">
										<c:set var="dt" value="${mov.dtRegMovDDMMYY}" />
									</c:if>
									<c:choose>
										<c:when test="${dt == dtUlt}">
											<c:set var="dt" value="" />
										</c:when>
										<c:otherwise>
											<c:set var="dtUlt" value="${dt}" />
										</c:otherwise>
									</c:choose>
									<td class="text-center">
										${dt}
									</td>
									<td>
										${mov.descrTipoMovimentacao}
									</td>
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
									<c:if test="${ (exibirCompleto == 'true')}">
										<td class="text-left">
											<siga:selecionado isVraptor="true" sigla="${mov.parte.lotaSubscritor.siglaOrgao}${mov.parte.lotaSubscritor.sigla}" 
												descricao="${mov.parte.lotaSubscritor.descricaoAmpliada}" 
												lotacaoParam="${mov.parte.lotaSubscritor.siglaOrgao}${mov.parte.lotaSubscritor.sigla}" />
										</td>
										<td class="text-left">
											<siga:selecionado isVraptor="true"
												sigla="${mov.parte.subscritor.nomeAbreviado}"
												descricao="${mov.parte.subscritor.descricao} - ${mov.parte.subscritor.sigla}" 
												pessoaParam="${mov.parte.subscritor.sigla}" />
										</td>
									</c:if>
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
												<siga:link title="${acao.nomeNbsp}" pre="${acao.pre}" pos="${acao.pos}" 
													url="${pageContext.request.contextPath}${acao.url}" popup="${acao.popup}" 
													confirm="${acao.msgConfirmacao}" ajax="${acao.ajax}" 
													idAjax="${mov.idMov}" classe="${acao.classe}" post="${acao.post}" 
													explicacao="${acao.explicacao}"	test="${acao.pode}" />
												<c:if test='${assinadopor and mov.idTpMov == 2}'>
													${mov.complemento}
													<c:set var="assinadopor" value="${false}" />
												</c:if>
											</c:forEach>
										</siga:links>
									</td>
									<c:if test="${exibirCompleto != 'true' and mov.duracaoSpan > 0}">
										<td class="duracaoborderbottom text-center bg-white" rowspan="${mov.duracaoSpan}">
											${mov.duracao}
										</td>
									</c:if>
									<c:if test="${exibirCompleto == 'true' and mov.duracaoSpanExibirCompleto > 0}">
										<td class="duracaoborderbottom text-center bg-white" rowspan="${mov.duracaoSpanExibirCompleto}">
											${mov.duracao}
										</td>
									</c:if>
								</tr>
								<c:choose>
									<c:when test='${evenorodd == "even"}'>
										<c:set var="evenorodd" value="odd" />
									</c:when>
									<c:otherwise>
										<c:set var="evenorodd" value="even" />
									</c:otherwise>
								</c:choose>
							</c:if>
						</c:forEach>
					</table>
			</c:if>
			<c:if test="${not empty m.expedientesFilhosNaoCancelados}">
				<c:set var="first" value="true" />
				<p class="apensados" style="margin-top: 0pt;">
						Documento
					<c:if test="${m.apensos.size() gt 1}">
						s
					</c:if>
					<fmt:message key="documento.filho"/>
					<c:if test="${m.apensos.size() gt 1}">
						s
					</c:if>
					:
					<c:forEach var="docFilho" items="${m.expedientesFilhosNaoCancelados}">
						<c:if test="${!docFilho.doc.cancelado}">
						<c:if test="${not first}">
							, 
						</c:if>
						<a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${docFilho.sigla}" title="${docFilho.descrDocumento}">
							<b>
								${docFilho.sigla}
							</b>
						</a>
						<c:set var="first" value="false" />
						</c:if>
					</c:forEach>
				</p>
			</c:if>
			<c:if test="${not empty m.processosFilhosNaoCancelados}">
				<c:set var="first" value="true" />
				<p class="apensados" style="margin-top: 0pt;">
					Subprocesso
					<c:if test="${m.apensos.size() gt 1}">
						s
					</c:if>
					:
					<c:forEach var="docFilho" items="${m.processosFilhosNaoCancelados}">
						<c:if test="${!docFilho.doc.cancelado}">
						<c:if test="${not first}">
							, 
						</c:if>
						<a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${docFilho.sigla}" title="${docFilho.descrDocumento}">
							<b>
								${docFilho.siglaCurtaSubProcesso}
							</b>
						</a>
						<c:set var="first" value="false" />
						</c:if>
					</c:forEach>
				</p>
			</c:if>	
			<c:if test="${not empty m.apensos}">
				<c:set var="first" value="true" />siga.
				<p class="apensados" style="margin-top: 0pt;">
					Documento
					<c:if test="${m.apensos.size() gt 1}">
						s
					</c:if>
					Apensado
					<c:if test="${m.apensos.size() gt 1}">
						s
					</c:if>
					:
					<c:forEach var="mobItem" items="${m.apensos}">

						<c:if test="${not first}">
							, 
						</c:if>
						<a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${mobItem.sigla}" title="${mobItem.mob.doc.descrDocumento}">
							${mobItem.sigla}
						</a>
						<c:set var="first" value="false" />
					</c:forEach>
				</p>
			</c:if>
		</c:if>
	</c:forEach>
</div>
<div class="container-fluid content" id="page">
	<div class="row mt-2">
		<div class="col col-sm-12 col-md-8">
			<c:if test="${docVO.conteudoBlobHtmlString != null}">
				<div class="card-sidebar card border-alert bg-white mb-3">
					<div class="card-body">
						<tags:fixdocumenthtml>
							${docVO.conteudoBlobHtmlString}
						</tags:fixdocumenthtml>
					</div>
				</div>
			</c:if>
		</div>
		<div class="col col-sm-12 col-md-4">
			<div class="gt-sidebar">
				<div class="gt-sidebar-content">
					<div class="card-sidebar card bg-light mb-3">
						<div class="card-header">
								${docVO.nomeCompleto}
						</div>
						<div class="card-body">
							<p>
								<b>
									Suporte:
								</b>
								${docVO.fisicoOuEletronico}
							</p>
							<p>
								<b>
									Nível de Acesso:
								</b>
									 ${docVO.nmNivelAcesso} 
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
													<li>
														${acesso.sigla}
													</li>
												</c:forEach>
											</ul>
										</c:otherwise>
									</c:choose>
								</c:if>
							</p>
							<p>
								<b>
									Data:
								</b>
								 ${docVO.dtDocDDMMYY}
							</p>
							<p>
								<b>
									De:
								</b>
								 ${docVO.subscritorString}
							</p>
							<p>
								<b>
									Para:
								</b>
								 ${docVO.destinatarioString}
							</p>
							<p>
								<b>
									Espécie:
								</b>
								 ${docVO.forma}
							</p>
							<p>
								<b>
									Modelo:
								</b>
								 ${docVO.modelo}
							</p>
							<p>
								<b>
									Descrição:
								</b>
								 ${docVO.descrDocumento}
							</p>
							<p>
								<b>
									Classificação:
								</b>
								 ${docVO.classificacaoDescricaoCompleta}
							</p>
							<p>
								<b>
									Cadastrante:
								</b>
								 ${docVO.cadastranteString} ${docVO.lotaCadastranteString}
							</p>
							<c:if test="${not empty docVO.paiSigla}">
								<p>
									<b>
										Documento Pai:
									</b>
									<a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${docVO.paiSigla}">
										${docVO.paiSigla}
									</a>
								</p>
							</c:if>
							<c:if test="${not empty docVO.documentosPublicados}">
								<p class="apensados" style="margin-top: 0pt;">
									<b>
										Documentos Publicados: 
									</b>
									<c:forEach var="documentoPublicado" items="${docVO.documentosPublicados}">
										<a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${documentoPublicado.sigla}" title="${documentoPublicado.sigla}">
											${documentoPublicado.sigla}
										</a>
										&nbsp;
									</c:forEach>
								</p>
							</c:if>
							<c:if test="${not empty docVO.boletim}">
								<p class="apensados" style="margin-top: 0pt;">
									<b>
										Publicado no Boletim: 
									</b>
									<a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${docVO.boletim.sigla}" title="${docVO.boletim.sigla}">
										${docVO.boletim.sigla}
									</a>
								</p>
							</c:if>
							<c:if test="${not empty docVO.dadosComplementares}">
					        	${docVO.dadosComplementares}
				     		</c:if>
						</div>
					</div>
				</div>
					<!-- tabela de móbiles e marcas -->
					<c:if test="${not empty docVO.outrosMobsLabel and not empty docVO.marcasPorMobil}">
						<div class="card-sidebar card bg-light mb-3">
							<c:set var="butRefresh"><a title="Atualizar marcas"
								style="float: right; margin-top: 0px; padding-left: 1em; padding-right: 1em;"
								href="${linkTo[ExDocumentoController].aAtualizarMarcasDoc(sigla)}?sigla=${sigla}"
								${popup?'target="_blank" ':''}> <img
								src="/siga/css/famfamfam/icons/arrow_refresh.png">
								
							</a></c:set>
							<tags:collapse title="${docVO.outrosMobsLabel}" id="OutrosMob" collapseMode="${collapse_Expanded}" addToTitle="${butRefresh}" classInfo="m-0 p-0">
								<div class="table-responsive">
								<table class="table table-sm mb-0 w-100">
								<!-- <thead class="align-middle text-center">
									<tr>
										<th class="text-left"></th>
										<th class="text-left">Marca</th>
										<th class="text-left"><fmt:message key="usuario.pessoa"/></th>
										<th class="text-left"><fmt:message key="usuario.lotacao"/></th>
										<th class="text-left">Texto</th>
									</tr>
								</thead> -->
								<tbody>
									<c:forEach var="entry" items="${docVO.marcasPorMobil}">
										<c:set var="outroMob" value="${entry.key}" />
										<c:set var="mobNome" value="${outroMob.isGeral() ? 'Geral' : outroMob.terminacaoSigla}" />
										<c:forEach var="marca" items="${entry.value}" varStatus="loop">
											<c:set var="lotacaoAtual" value="${marca.dpLotacaoIni.lotacaoAtual}"/>
											<c:set var="pessoaAtual" value="${marca.dpPessoaIni.pessoaAtual}"/>
											<tr class="${mov.classe} ${mov.disabled}">
											<c:if test="${loop.first}">
											<td rowspan="${entry.value.size()}" style="padding-left: 1.25rem"><c:choose>
													<c:when test="${(not outroMob.geral) and outroMob.numSequencia == m.mob.numSequencia}">
														<i><b>${mobNome}</b></i>
													</c:when>
													<c:otherwise>
														<a
															href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${outroMob.sigla}"
															title="${outroMob.doc.descrDocumento}"
															style="text-decoration: none">
															${mobNome} </a>
													</c:otherwise>
												</c:choose></td>
											</c:if>
											<td>${marca.descricaoComDatas}</td>
											<td><siga:selecionado isVraptor="true" sigla="${pessoaAtual.nomeAbreviado}"
												descricao="${pessoaAtual.descricao} - ${pessoaAtual.sigla}"
												pessoaParam="${pessoaAtual.siglaCompleta}" /></td>
											<td><siga:selecionado isVraptor="true" sigla="${marca.dpLotacaoIni.lotacaoAtual.sigla}"
												descricao="${marca.dpLotacaoIni.lotacaoAtual.descricaoAmpliada}"
												lotacaoParam="${marca.dpLotacaoIni.lotacaoAtual.siglaCompleta}" /></td>
											<c:choose>
												<c:when test="${not empty marca.exMovimentacao.descrMov}">
													<td>${marca.exMovimentacao.descrMov}</td>
												</c:when>
												<c:otherwise>
													<td style="padding-left:0; padding-right: 0"></td>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${marca.exMovimentacao.podeCancelar(titular, lotaTitular)}">
													<td style="padding-left:.25em; padding-right: 0"><a href="javascript:postToUrl('/sigaex/app/expediente/mov/cancelar_movimentacao_gravar?id=${marca.exMovimentacao.idMov}&sigla=${sigla}')" title="${marca.exMovimentacao.expliquePodeCancelar(titular, lotaTitular)}"><i class="far fa-trash-alt"></i></a></td>
												</c:when>
												<c:otherwise>
													<td style="padding-left:0; padding-right: 0"></td>
												</c:otherwise>
											</c:choose>
											<td style="padding-left:0; padding-right: 1.25rem"></td>
											</tr>
										</c:forEach>
									</c:forEach>
								</tbody>
							</table>
							</div>
							</tags:collapse>
						</div>
					</c:if>
					
					<div class="gt-sidebar-content" id="gc">
				</div>
			</div>
		</div>
		
		
	</div>
</div>
<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC')}">
	<c:url var="url" value="/../sigagc/app/knowledge">
		<c:param name="tags">
			@documento
		</c:param>
		<c:forEach var="tag" items="${docVO.tags}">
			<c:param name="tags">
				${tag}
			</c:param>
		</c:forEach>
		<c:param name="estilo">
			sidebar
		</c:param>
		<c:param name="ts">
			${currentTimeMillis}
		</c:param>
	</c:url>
	<script type="text/javascript">
		SetInnerHTMLFromAjaxResponse("${url}",document.getElementById('gc'));
	</script>
</c:if>
</siga:pagina>
