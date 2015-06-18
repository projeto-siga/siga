<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="libs"%>

<%@page import="br.gov.jfrj.siga.ex.ExMovimentacao"%>
<%@page import="br.gov.jfrj.siga.ex.ExMobil"%>
<siga:cabecalho titulo="Documento" popup="${param.popup}" />

<c:if test="${not docVO.digital}">
	<script type="text/javascript">$("html").addClass("fisico");</script>
</c:if>

<div class="gt-bd" style="padding-bottom: 0px;">
	<div class="gt-content">

		<c:if test="${not empty param.msg}">
			<p align="center">
				<b>${param.msg}</b>
			</p>
		</c:if>

		<ww:form name="frm" action="exibir" namespace="/expediente/doc"
			theme="simple" method="POST">
			<ww:token />
		</ww:form>

		<h2 style="margin-bottom: 0px;">
			<c:if test="${empty ocultarCodigo}">${docVO.sigla}</c:if>
		</h2>
		<c:set var="primeiroMobil" value="${true}" />
		<c:forEach var="m" items="${docVO.mobs}" varStatus="loop">
			<ww:if
				test="%{#attr.m.mob.geral or true or (((mob.geral or (mob.id == #attr.m.mob.id)) and (exibirCompleto or (#attr.m.mob.getUltimaMovimentacaoNaoCancelada() != null))))}">
				<h3 style="margin-top: 10px; margin-bottom: 0px;">
					<ww:property
						value="%{#attr.m.getDescricaoCompletaEMarcadoresEmHtml(cadastrante,lotaTitular)}"
						escape="false" />
					<c:if test="${docVO.digital and not empty m.tamanhoDeArquivo}"> - ${m.tamanhoDeArquivo}</c:if>
				</h3>

				<c:set var="ocultarCodigo" value="${true}" />

				<!-- Links para as aÃ§Ãµes de cada mobil -->
				<c:if test='${param.popup!="true"}'>
					<c:set var="acoes" value="${m.acoesOrdenadasPorNome}" />
					<%-- 			<ww:if test="%{#attr.m.mob.geral}"><c:set var="acoes" value="${docVO.acoesOrdenadasPorNome}"/></ww:if>--%>

					<siga:links>
						<c:forEach var="acao" items="${acoes}">
							<ww:url id="url" action="${acao.acao}"
								namespace="${acao.nameSpace}">
								<c:forEach var="p" items="${acao.params}">
									<ww:param name="${p.key}">${p.value}</ww:param>
								</c:forEach>
							</ww:url>
							<siga:link icon="${acao.icone}" title="${acao.nomeNbsp}"
								pre="${acao.pre}" pos="${acao.pos}" url="${url}" test="${true}"
								popup="${acao.popup}" confirm="${acao.msgConfirmacao}" classe="${acao.classe}" />
						</c:forEach>
					</siga:links>
				</c:if>

				<!-- Somente quando o workflow estÃ¡ ativado -->
				<c:if test="${f:resource('isWorkflowEnabled')}">
				<!-- Se for um processo administrativo, colocar a caixa do wf geral no Ãºltimo volume -->
				<ww:if test="${ (primeiroMobil) and (docVO.tipoFormaDocumento == 'processo_administrativo')}">
						<div id="${docVO.sigla}" depende=";wf;" />
						<!--ajax:${doc.codigo}-${i}-->
						<!--/ajax:${doc.codigo}-${i}-->
						</div>
					<c:set var="primeiroMobil" value="${false}" />
				</ww:if>
				
				<ww:if test="%{(not #attr.m.mob.geral) or (docVO.tipoFormaDocumento != 'processo_administrativo')}">
						<div id="${m.sigla}" depende=";wf;" />
						<!--ajax:${doc.codigo}-${i}-->
						<!--/ajax:${doc.codigo}-${i}-->
						</div>
				</ww:if>
				
				</c:if>
	
	<c:set var="dtUlt" value="" />

	<!-- Verifica se haverÃ¡ alguma movimentaÃ§Ã£o para ser exibida -->
	<c:set var="temmov" value="${false}" />
	<c:forEach var="mov" items="${m.movs}">
		<c:if
			test="${ (exibirCompleto == 'true') or (mov.idTpMov != 14 and
							          not mov.cancelada)}">
			<c:set var="temmov" value="${true}" />
		</c:if>
	</c:forEach>

	<!-- Tabela de movimentaÃ§Ãµes -->
	<c:if test="${temmov}">
		<div class="gt-content-box gt-for-table" style="margin-bottom: 25px;">
			<table class="gt-table mov">
				<thead>
					<tr>
						<th align="center" rowspan="2">Data</th>
						<th rowspan="2">Evento</th>
						<th colspan="2" align="left">Cadastrante</th>
						<c:if test="${ (exibirCompleto == 'true')}">
							<th colspan="2" align="left">ResponsÃ¡vel</th>
						</c:if>
						<th colspan="2" align="left">Atendente</th>
						<th rowspan="2">DescriÃ§Ã£o</th>
						<th align="center" rowspan="2">DuraÃ§Ã£o</th>
					</tr>
					<tr>
						<th align="left">LotaÃ§Ã£o</th>
						<th align="left">Pessoa</th>
						<c:if test="${ (exibirCompleto == 'true')}">
							<th align="left">LotaÃ§Ã£o</th>
							<th align="left">Pessoa</th>
						</c:if>
						<th align="left">LotaÃ§Ã£o</th>
						<th align="left">Pessoa</th>
					</tr>
				</thead>
				<c:set var="evenorodd" value="odd" />
				<c:forEach var="mov" items="${m.movs}">
					<c:if
						test="${ (exibirCompleto == 'true') or (mov.idTpMov != 14 and
							          not mov.cancelada and not mov.mov.exMovimentacaoRef.cancelada) }">
						<tr class="${mov.classe} ${mov.disabled}">
							<c:if test="${ (exibirCompleto == 'true')}">
								<c:set var="dt" value="${mov.dtRegMovDDMMYYHHMMSS}" />
							</c:if>
							<c:if test="${ (exibirCompleto != 'true')}">
								<c:set var="dt" value="${mov.dtRegMovDDMMYY}" />
							</c:if>
							<ww:if test="${dt == dtUlt}">
								<c:set var="dt" value="" />
							</ww:if>
							<ww:else>
								<c:set var="dtUlt" value="${dt}" />
							</ww:else>

							<td align="center">${dt}</td>
							<td>${mov.descrTipoMovimentacao}</td>
							<td align="left"><siga:selecionado
									sigla="${mov.parte.lotaCadastrante.sigla}"
									descricao="${mov.parte.lotaCadastrante.descricaoAmpliada}"
									lotacaoParam="${mov.parte.lotaCadastrante.siglaOrgao}${mov.parte.lotaCadastrante.sigla}" />

							</td>
							<td align="left"><siga:selecionado
									sigla="${mov.parte.cadastrante.nomeAbreviado}"
									descricao="${mov.parte.cadastrante.descricao} - ${mov.parte.cadastrante.sigla}"
									pessoaParam="${mov.parte.cadastrante.sigla}" />

							</td>
							<c:if test="${ (exibirCompleto == 'true')}">
								<td align="left"><siga:selecionado
										sigla="${mov.parte.lotaSubscritor.sigla}"
										descricao="${mov.parte.lotaSubscritor.descricaoAmpliada}" 
										lotacaoParam="${mov.parte.lotaSubscritor.siglaOrgao}${mov.parte.lotaSubscritor.sigla}" />

								</td>
								<td align="left"><siga:selecionado
										sigla="${mov.parte.subscritor.nomeAbreviado}"
										descricao="${mov.parte.subscritor.descricao} - ${mov.parte.subscritor.sigla}" 
										pessoaParam="${mov.parte.subscritor.sigla}" />

								</td>
							</c:if>
							<td align="left"><siga:selecionado
									sigla="${mov.parte.lotaResp.sigla}"
									descricao="${mov.parte.lotaResp.descricaoAmpliada}" 
									lotacaoParam="${mov.parte.lotaResp.siglaOrgao}${mov.parte.lotaResp.sigla}" /></td>
							<td align="left"><siga:selecionado sigla="${mov.parte.resp.nomeAbreviado}"



									descricao="${mov.parte.resp.descricao} - ${mov.parte.resp.sigla}" 
									pessoaParam="${mov.parte.resp.sigla}"/>
							</td>
							<td>${mov.descricao} <c:if test='${mov.idTpMov != 2}'> ${mov.complemento} </c:if>
								<c:set var="assinadopor" value="${true}" /> <siga:links
									inline="${true}"
									separator="${not empty mov.descricao and mov.descricao != null}">
									<c:forEach var="acao" items="${mov.acoes}">
										<c:choose>
											<c:when test='${mov.idTpMov == 32}'>
												<ww:url id="url" value="${acao.nameSpace}/${acao.acao}">
													<c:forEach var="p" items="${acao.params}">
														<ww:param name="${p.key}">${p.value}</ww:param>
													</c:forEach>
												</ww:url>
											</c:when>
											<c:otherwise>
												<ww:url id="url" action="${acao.acao}"
													namespace="${acao.nameSpace}">
													<c:forEach var="p" items="${acao.params}">
														<ww:param name="${p.key}">${p.value}</ww:param>
													</c:forEach>
												</ww:url>
											</c:otherwise>
										</c:choose>
										<siga:link title="${acao.nomeNbsp}" pre="${acao.pre}"
											pos="${acao.pos}" url="${url}" test="${true}"
											popup="${acao.popup}" confirm="${acao.msgConfirmacao}"
											ajax="${acao.ajax}" idAjax="${mov.idMov}" classe="${acao.classe}" />
										<c:if test='${assinadopor and mov.idTpMov == 2}'> ${mov.complemento}
									<c:set var="assinadopor" value="${false}" />
										</c:if>
									</c:forEach>
								</siga:links></td>

							<c:if test="${exibirCompleto != 'true' and mov.duracaoSpan > 0}">
								<td align="center" class="duracaoborderbottom"
									rowspan="${mov.duracaoSpan}">${mov.duracao}</td>
							</c:if>
							<c:if
								test="${exibirCompleto == 'true' and mov.duracaoSpanExibirCompleto > 0}">
								<td align="center" class="duracaoborderbottom"
									rowspan="${mov.duracaoSpanExibirCompleto}">${mov.duracao}</td>
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
		</div>
	</c:if>

	<!-- Lista sucinta de documentos filhos - Falta incluir -->
	<c:if test="${not empty m.expedientesFilhosNaoCancelados}">
		<c:set var="first" value="true" />
		<p class="apensados" style="margin-top: 0pt;">
			Documento
			<c:if test="%{#attr.apensos.size() gt 1}">s</c:if>
			Filho
			<c:if test="%{#attr.apensos.size() gt 1}">s</c:if>
			:
			<c:forEach var="docFilho" items="${m.expedientesFilhosNaoCancelados}">
				<ww:url id="url" action="exibir" namespace="/expediente/doc" >
					<ww:param name="sigla">${docFilho.sigla}</ww:param>
				</ww:url>
				<c:if test="${not first}">, </c:if>
				<ww:a href="%{url}" title="${docFilho.descrDocumento}">
					<b>${docFilho.sigla}</b>
				</ww:a>
				<c:set var="first" value="false" />
			</c:forEach>
		</p>
	</c:if>
	
	<!-- Lista sucinta de documentos filhos - Falta incluir -->
	<c:if test="${not empty m.processosFilhosNaoCancelados}">
		<c:set var="first" value="true" />
		<p class="apensados" style="margin-top: 0pt;">
			Subprocesso
			<c:if test="%{#attr.apensos.size() gt 1}">s</c:if>
			:
			<c:forEach var="docFilho" items="${m.processosFilhosNaoCancelados}">
				<ww:url id="url" action="exibir" namespace="/expediente/doc">
					<ww:param name="sigla">${docFilho.sigla}</ww:param>
				</ww:url>
				<c:if test="${not first}">, </c:if>
				<ww:a href="%{url}" title="${docFilho.descrDocumento}">
					<b>${docFilho.siglaCurtaSubProcesso}</b>
				</ww:a>
				<c:set var="first" value="false" />
			</c:forEach>
		</p>
	</c:if>	

	<!-- Lista sucinta de documentos apensados -->
	<c:if test="${not empty m.apensos}">
		<c:set var="first" value="true" />
		<p class="apensados" style="margin-top: 0pt;">
			Documento
			<c:if test="%{#attr.apensos.size() gt 1}">s</c:if>
			Apensado
			<c:if test="%{#attr.apensos.size() gt 1}">s</c:if>
			:
			<c:forEach var="mobItem" items="${m.apensos}">
				<ww:url id="url" action="exibir" namespace="/expediente/doc">
					<ww:param name="sigla">${mobItem.sigla}</ww:param>
				</ww:url>
				<c:if test="${not first}">, </c:if>
				<ww:a href="%{url}" title="${mobItem.mob.doc.descrDocumento}">${mobItem.sigla}</ww:a>
				<c:set var="first" value="false" />
			</c:forEach>
		</p>
	</c:if>
	</ww:if>
	</c:forEach>



	<!-- VisualizaÃ§Ã£o dos principais dados do documento em questÃ£o -->

	<!-- Links para as aÃ§Ãµes de gerais do documento -->
	<%--
 --%>
</div>
</div>


<div class="gt-bd gt-cols clearfix"
	style="padding-top: 0px; margin-top: 25px;">
	<div class="gt-content">
		<!-- Dados do documento -->
		<div class="gt-content-box" style="padding: 10px;">
			<table style="width: 100%">
				<tr>
					<td><c:if test="${docVO.conteudoBlobHtmlString != null}">
							<tags:fixdocumenthtml>
			${docVO.conteudoBlobHtmlString}
		</tags:fixdocumenthtml>
						</c:if>
					</td>
				</tr>
			</table>
		</div>
	</div>

	<div class="gt-sidebar">
		<div class="gt-sidebar-content">
			<h3>${docVO.nomeCompleto}</h3>
			<p>
				<b>Suporte:</b> ${docVO.fisicoOuEletronico}
			</p>
			<p>
				<b>NÃ­vel de Acesso:</b> ${docVO.nmNivelAcesso} 
				<c:if test="${not empty docVO.listaDeAcessos}">
					<ww:if test="%{#attr.docVO.listaDeAcessos.size() eq 1}">
						<c:forEach var="acesso" items="${docVO.listaDeAcessos}" varStatus="loop">
							<ww:if test="${acesso eq 'PUBLICO'}">
								(PÃºblico)
							</ww:if>
							<ww:else>
								(${acesso.sigla})
							</ww:else>
						</c:forEach>
					</ww:if>
					<ww:else>
						<ul>	
						<c:forEach var="acesso" items="${docVO.listaDeAcessos}" varStatus="loop">
							<li>${acesso.sigla}</li>
						</c:forEach>
						</ul>
					</ww:else>
				</c:if>
			</p>
			<p>
				<b>Data:</b> ${docVO.dtDocDDMMYY}
			</p>
			<p>
				<b>De:</b> ${docVO.subscritorString}
			</p>
			<p>
				<b>Para:</b> ${docVO.destinatarioString}
			</p>
			<p>
				<b>Tipo:</b> ${docVO.forma}
			</p>
			<p>
				<b>Modelo:</b> ${docVO.modelo}
			</p>
			<p>
				<b>DescriÃ§Ã£o:</b> ${docVO.descrDocumento}
			</p>
			<p>
				<b>ClassificaÃ§Ã£o:</b> ${docVO.classificacaoDescricaoCompleta}
			</p>
			<%--	<p>
				<jsp:useBean id="now" class="java.util.Date"/>
				<div style="font-weight: bold; font-size: 85%; margin-bottom: 2px">Marcas:</div> 
				<c:forEach var="mobilVO" items="${docVO.mobs}" varStatus="loop">
					<c:if test="${!empty mobilVO.mob.exMarcaSet}">
						<p style="margin-left: 8px; margin-bottom: 2px">${mobilVO.mob.descricaoCompletaSemDestinacao}: 
						<c:forEach var="marca" items="${mobilVO.mob.exMarcaSet}" varStatus="loop">
							${marca.cpMarcador.descrMarcador} 
							<c:if test="${marca.dtIniMarca gt now}">
								em ${marca.dtIniMarcaDDMMYYYY}
							</c:if>
							<c:if test="${not empty marca.dtFimMarca}"> 
								atÃ© ${marca.dtFimMarcaDDMMYYYY}
							</c:if>
							[${marca.dpLotacaoIni.sigla}
							<c:if test="${not empty marca.dpPessoaIni}">
								&nbsp;${marca.dpPessoaIni.sigla}]
							</c:if>
						</c:forEach>
						</p>
					</c:if>
				</c:forEach>
			</p> --%>
			<p>
				<b>Cadastrante:</b> ${docVO.cadastranteString} ${docVO.lotaCadastranteString}
			</p>
			<c:if test="${not empty docVO.paiSigla}">
				<p>
					<b>Documento Pai:</b>
					<ww:url id="url" action="exibir" namespace="/expediente/doc">
						<ww:param name="sigla">${docVO.paiSigla}</ww:param>
					</ww:url>
					<ww:a href="%{url}">${docVO.paiSigla}</ww:a>
				</p>
			</c:if>
		
			<c:if test="${not empty docVO.documentosPublicados}">
				<p class="apensados" style="margin-top: 0pt;">
					<b>Documentos Publicados: </b>
					<c:forEach var="documentoPublicado" items="${docVO.documentosPublicados}">
						<ww:url id="url" action="exibir" namespace="/expediente/doc">
							<ww:param name="sigla">${documentoPublicado.sigla}</ww:param>
						</ww:url>
						<ww:a href="%{url}" title="${documentoPublicado.sigla}">
							${documentoPublicado.sigla}
						</ww:a>&nbsp;
					</c:forEach>
				</p>
			</c:if>

			<c:if test="${not empty docVO.boletim}">
				<p class="apensados" style="margin-top: 0pt;">
					<b>Publicado no Boletim: </b>
					<ww:url id="url" action="exibir" namespace="/expediente/doc">
						<ww:param name="sigla">${docVO.boletim.sigla}</ww:param>
					</ww:url>
					<ww:a href="%{url}" title="${docVO.boletim.sigla}">
						${docVO.boletim.sigla}
					</ww:a>
				</p>
			</c:if>
			
			<c:if test="${not empty docVO.dadosComplementares}">
	        	${docVO.dadosComplementares}
     		</c:if>
		</div>

		<div class="gt-sidebar-content" id="gc"></div>

		<!-- / sidebar -->
	</div>

</div>

<!-- Somente quando o workflow estÃ¡ ativado -->

<c:if test="${f:resource('isWorkflowEnabled')}">
	<script type="text/javascript">ReplaceInnerHTMLFromAjaxResponse("/sigawf/doc.action?sigla=${doc.codigo}&ts=${currentTimeMillis}",null,"wf");</script>
</c:if>
<c:if
	test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC')}">
	<c:url var="url" value="/../sigagc/app/knowledge">
		<c:param name="tags">@documento</c:param>
		<c:forEach var="tag" items="${docVO.tags}">
			<c:param name="tags">${tag}</c:param>
		</c:forEach>
		<c:param name="estilo">sidebar</c:param>
		<c:param name="ts">${currentTimeMillis}</c:param>
	</c:url>
	<script type="text/javascript">
		SetInnerHTMLFromAjaxResponse("${url}",document.getElementById('gc'));
	</script>
</c:if>


<siga:rodape />