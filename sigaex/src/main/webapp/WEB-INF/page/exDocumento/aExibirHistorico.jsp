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
				<button type="button" name="voltar" onclick="javascript:history.back();" class="btn btn-secondary float-right ${hide_only_TRF2}" accesskey="r">Volta<u>r</u></button>
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
			test="${m.mob.geral or true or (((mob.geral or (mob.id == m.mob.id)) and (m.mob.ultimaMovimentacaoNaoCancelada != null) ))}">
			<h4 style="margin-top: 10px; margin-bottom: 0px;">
				${m.getDescricaoCompletaEMarcadoresEmHtml(cadastrante,lotaTitular)}
				<c:if test="${docVO.digital and not empty m.tamanhoDeArquivo}">
				 	- ${m.tamanhoDeArquivo}
				 </c:if>
			</h4>
			<c:set var="ocultarCodigo" value="${true}" />

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
					<c:set var="temmov" value="${true}" />
			</c:forEach>
			<c:if test="${temmov}">
					<table class="table table-sm table-hover table-striped  table-responsive mov mt-2">
						<thead class="${thead_color} align-middle text-center">
							<tr>
								<th style="width: 5%" class="text-left" rowspan="2">
									Data
								</th>
								<th style="width: 15%" class="text-left" rowspan="2">
									Evento
								</th>
								<th style="width: 25%" colspan="2" align="left">
									Responsável Pelo Evento
								</th>
								<th rowspan="2">
									Descrição
								</th>
								<th style="width: 5%" class="text-center" rowspan="2">
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
							</tr>
						</thead>
						<c:forEach var="mov" items="${m.movs}">
								<tr class="${mov.classe} ${mov.disabled}">
									<c:set var="dt" value="${mov.dtRegMovDDMMYYHHMMSS}" />
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
									<td> 
										${mov.descricao}
										<c:if test='${mov.idTpMov != 2}'>
											${mov.complemento}
										</c:if>
										<c:set var="assinadopor" value="${true}" />
										<siga:links inline="${true}"
											separator="${not empty mov.descricao and mov.descricao != null}">
											<c:forEach var="acao" items="${mov.acoes}">
												<siga:link title="${acao.nomeNbsp}" pre="${acao.pre}" pos="${acao.pos}" 
													url="${pageContext.request.contextPath}${acao.url}" test="${true}" popup="${acao.popup}" 
													confirm="${acao.msgConfirmacao}" ajax="${acao.ajax}" 
													idAjax="${mov.idMov}" classe="${acao.classe}" post="${acao.post}"/>
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
							${siga_cliente=='GOVSP'?'Propriedades do Documento (':'Documento '}${docVO.doc.exTipoDocumento.descricao}${siga_cliente=='GOVSP'?')':''}
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
									<fmt:message key="documento.data.assinatura"/>:
								</b>
								
								<c:choose>
									<c:when test="${not empty docVO.dataPrimeiraAssinatura}">
										${docVO.dataPrimeiraAssinatura}
									</c:when>
									<c:otherwise>
										${docVO.dtFinalizacao}
										<c:if test="${not empty docVO.originalData}">- <b>original:</b> ${docVO.originalData}</c:if>
									</c:otherwise>
								</c:choose>
								
								
								<!-- 
								<c:choose>
									<c:when test="${siga_cliente=='GOVSP'}">
										${docVO.dataPrimeiraAssinatura}
									</c:when>
									<c:otherwise>
										${docVO.dtDocDDMMYY}
										<c:if test="${not empty docVO.originalData}">- <b>original:</b> ${docVO.originalData}</c:if>
									</c:otherwise>
								</c:choose>
								 -->
								<!-- ${docVO.dataPrimeiraAssinatura} -->
								<!--  ${docVO.dtDocDDMMYY} -->
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
