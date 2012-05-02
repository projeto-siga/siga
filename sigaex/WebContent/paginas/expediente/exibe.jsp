<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<%@page import="br.gov.jfrj.siga.ex.ExMovimentacao"%>
<%@page import="br.gov.jfrj.siga.ex.ExMobil"%>
<siga:cabecalho titulo="Documento" popup="${param.popup}" />

<%--<div style="display: inline; top: 70px; right: 10px"></div> --%>

<c:if test="${not empty param.msg}">
	<p align="center"><b>${param.msg}</b></p>
</c:if>

<ww:form name="frm" action="exibir" namespace="/expediente/doc"
	theme="simple" method="POST">
	<ww:token />
</ww:form>

<c:forEach var="m" items="${docVO.mobs}" varStatus="loop">
	<ww:if
		test="%{#attr.m.mob.geral or true or (((mob.geral or (mob.id == #attr.m.mob.id)) and (exibirCompleto or (#attr.m.mob.getUltimaMovimentacaoNaoCancelada() != null))))}">
		<table width="100%">
			<tr>
				<td>
				<h1><c:if test="${empty ocultarCodigo}">${docVO.sigla} - </c:if><ww:property
			value="%{#attr.m.getDescricaoCompletaEMarcadoresEmHtml(cadastrante,lotaTitular)}"
			escape="false" /><c:if test="${docVO.digital and not empty m.tamanhoDeArquivo}"> - ${m.tamanhoDeArquivo}</c:if></h1>
				</td>
				<td align="right">
				<c:if test="${loop.index == 0}">
				<ww:form name="frm2"
							cssStyle="display:inline" action="exibir"
							namespace="/expediente/doc" method="post" theme="simple">
				Ir para o documento:<siga:selecao tema="simple" buscar="nao"
								propriedade="documentoVia" ocultardescricao="sim" />
							<input style="display: inline" type="submit" name="ok" value="Ok"
								onclick="javascript: var id=document.getElementById('editar_documentoViaSel_id').value; if (id==null || id=='') {return;}" />
						
				</ww:form>
				</c:if>
				</td>
			</tr>

		</table>

		<c:set var="ocultarCodigo" value="${true}" />

		<!-- Links para as ações de cada mobil -->
		<c:if test='${param.popup!="true"}'>
			<siga:links>
				<c:forEach var="acao" items="${m.acoesOrdenadasPorNome}">
					<ww:url id="url" action="${acao.acao}"
						namespace="${acao.nameSpace}" >
						<c:forEach var="p" items="${acao.params}">
							<ww:param name="${p.key}">${p.value}</ww:param>
						</c:forEach>
					</ww:url>
					<siga:link title="${acao.nomeNbsp}" pre="${acao.pre}"
						pos="${acao.pos}" url="${url}" test="${true}"
						popup="${acao.popup}" confirm="${acao.msgConfirmacao}" />
				</c:forEach>
			</siga:links>
		</c:if>

		<!-- Somente quando o workflow está ativado -->
		<c:if test="${f:resource('isWorkflowEnabled')}">
			<div id="${m.sigla}" depende=";wf;" /><!--ajax:${doc.codigo}-${i}--><!--/ajax:${doc.codigo}-${i}-->
			</div>
		</c:if>

		<c:set var="dtUlt" value="" />

		<!-- Tabela de movimentações -->
		<table class="mov" width="100%">
			<tr class="${docVO.classe}">
				<td align="center" rowspan="2">Data</td>
				<td rowspan="2">Evento</td>
				<td colspan="2" align="left">Cadastrante</td>
				<c:if test="${ (exibirCompleto == 'true')}">
					<td colspan="2" align="left">Responsável</td>
				</c:if>
				<td colspan="2" align="left">Atendente</td>
				<td rowspan="2">Descrição</td>
				<td align="center" rowspan="2">Duração</td>
			</tr>
			<tr class="${docVO.classe}">
				<td align="left">Lotação</td>
				<td align="left">Pessoa</td>
				<c:if test="${ (exibirCompleto == 'true')}">
					<td align="left">Lotação</td>
					<td align="left">Pessoa</td>
				</c:if>
				<td align="left">Lotação</td>
				<td align="left">Pessoa</td>
			</tr>
			<c:set var="evenorodd" value="odd" />
			<c:forEach var="mov" items="${m.movs}">
				<c:if
					test="${ (exibirCompleto == 'true') or (mov.idTpMov != 14 and
							          not mov.cancelada)}">
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
							descricao="${mov.parte.lotaCadastrante.descricaoAmpliada}" /></td>
						<td align="left"><siga:selecionado
							sigla="${mov.parte.cadastrante.nomeAbreviado}"
							descricao="${mov.parte.cadastrante.descricao} - ${mov.parte.cadastrante.sigla}" /></td>
						<c:if test="${ (exibirCompleto == 'true')}">
							<td align="left"><siga:selecionado
								sigla="${mov.parte.lotaSubscritor.sigla}"
								descricao="${mov.parte.lotaSubscritor.descricaoAmpliada}" /></td>
							<td align="left"><siga:selecionado
								sigla="${mov.parte.subscritor.nomeAbreviado}"
								descricao="${mov.parte.subscritor.descricao} - ${mov.parte.subscritor.sigla}" /></td>
						</c:if>
						<td align="left"><siga:selecionado
							sigla="${mov.parte.lotaResp.sigla}"
							descricao="${mov.parte.lotaResp.descricaoAmpliada}" /></td>
						<td align="left"><siga:selecionado
							sigla="${mov.parte.resp.nomeAbreviado}"
							descricao="${mov.parte.resp.descricao} - ${mov.parte.resp.sigla}" /></td>
						<td>${mov.descricao}<c:if test='${mov.idTpMov != 2}'> ${mov.complemento}</c:if>
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
									ajax="${acao.ajax}" idAjax="${mov.idMov}" />
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

		<!-- Lista sucinta de documentos filhos - Falta incluir -->
		<c:if test="${not empty m.filhosNaoCancelados}">
			<c:set var="first" value="true" />
			<p class="apensados" style="margin-top: 0pt;">Documento<c:if
				test="%{#attr.apensos.size() gt 1}">s</c:if> Filho<c:if
				test="%{#attr.apensos.size() gt 1}">s</c:if>: <c:forEach
				var="docFilho" items="${m.filhosNaoCancelados}">
				<ww:url id="url" action="exibir" namespace="/expediente/doc">
					<ww:param name="sigla">${docFilho.sigla}</ww:param>
				</ww:url>
				<c:if test="${not first}">, </c:if>
				<ww:a href="%{url}">
					<b>${docFilho.sigla}</b>
				</ww:a>
				<c:set var="first" value="false" />
			</c:forEach></p>
		</c:if>

		<!-- Lista sucinta de documentos apensados -->
		<c:if test="${not empty m.apensos}">
			<c:set var="first" value="true" />
			<p class="apensados" style="margin-top: 0pt;">Documento<c:if
				test="%{#attr.apensos.size() gt 1}">s</c:if> Apensado<c:if
				test="%{#attr.apensos.size() gt 1}">s</c:if>: <c:forEach
				var="mobItem" items="${m.apensos}">
				<ww:url id="url" action="exibir" namespace="/expediente/doc">
					<ww:param name="sigla">${mobItem.sigla}</ww:param>
				</ww:url>
				<c:if test="${not first}">, </c:if>
				<ww:a href="%{url}">${mobItem.sigla}</ww:a>
				<c:set var="first" value="false" />
			</c:forEach></p>
		</c:if>
	</ww:if>
</c:forEach>



<!-- Visualização dos principais dados do documento em questão -->
<h1>${docVO.nomeCompleto}</h1>

<!-- Links para as ações de gerais do documento -->
<c:if test='${param.popup!="true"}'>
	<siga:links>
		<c:forEach var="acao" items="${docVO.acoesOrdenadasPorNome}">
			<ww:url id="url" action="${acao.acao}" namespace="${acao.nameSpace}">
				<c:forEach var="p" items="${acao.params}">
					<ww:param name="${p.key}">${p.value}</ww:param>
				</c:forEach>
			</ww:url>
			<siga:link title="${acao.nomeNbsp}" pre="${acao.pre}"
				pos="${acao.pos}" url="${url}" test="${true}" popup="${acao.popup}"
				confirm="${acao.msgConfirmacao}" />
		</c:forEach>
	</siga:links>
</c:if>

<!-- Dados do documento -->
<table class="message" width="100%">
	<tr class="${docVO.classe}">
		<td width="50%"><b>${docVO.nomeCompleto}</td>
		<td>
		<div style="width: 100%;">
		<div style="float: left;"><b>Data:</b> ${docVO.dtDocDDMMYY}</div>
		<div style="width: 100%; text-align: right;"><b>${docVO.fisicoOuEletronico}</b>
		</div>
		</div>
		</td>
	</tr>
	<tr class="${docVO.classe}">
		<td><b>De:</b> ${docVO.subscritorString}</td>
		<td><b>Classificação:</b> ${docVO.classificacaoDescricaoCompleta}</td>
	</tr>
	<tr class="${docVO.classe}">
		<td><b>Para:</b> ${docVO.destinatarioString}</td>
		<td><b>Descrição:</b> ${docVO.descrDocumento}</td>
	</tr>
	<tr class="${docVO.classe}">
		<td><b>Nível de Acesso:</b> ${docVO.nmNivelAcesso}</td>
		<td><c:if test="${not empty docVO.paiSigla}">
			<b>Documento Pai:</b>
			<ww:url id="url" action="exibir" namespace="/expediente/doc">
				<ww:param name="sigla">${docVO.paiSigla}</ww:param>
			</ww:url>
			<ww:a href="%{url}">${docVO.paiSigla}</ww:a>
		</c:if></td>
	</tr>
	<c:if test="${not empty docVO.dadosComplementares}">
	    ${docVO.dadosComplementares}   
	</c:if>
	<c:if test="${docVO.conteudoBlobHtmlString != null}">
		<tr>
			<td colspan="2"><tags:fixdocumenthtml>
			${docVO.conteudoBlobHtmlString}
			</tags:fixdocumenthtml></td>
		</tr>
	</c:if>
</table>

<!-- Somente quando o workflow está ativado -->

<c:if test="${f:resource('isWorkflowEnabled')}">
	<script type="text/javascript">ReplaceInnerHTMLFromAjaxResponse("/sigawf/doc.action?sigla=${doc.codigo}&ts=${currentTimeMillis}",null,"wf");</script>
</c:if>

<siga:rodape />