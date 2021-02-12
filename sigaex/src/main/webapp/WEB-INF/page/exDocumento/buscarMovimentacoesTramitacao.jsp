<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="libs"%>

<siga:pagina titulo="Histórico de tramitação">
	<style type="text/css">
/* Quando se usa a classe 'disabled' TODOS os eventos de mouse são 
desabilitados, inclusive a exibição do title. Por isso fez-se necessário 
sobrescrever esse comportamento para poder mostrar o title.
 */
td.juntado.fa-fw>a.disabled {
	cursor: auto; /* mantém o cursor do mouse como o padrão. */
	pointer-events: inherit;
}
</style>
	<div class="container-fluid content" id="page">
		<h2 class="mt-3">
			Histórico de tramitação: ${mobil.sigla}
			${mobil.exDocumento.exModelo.nmMod} -
			${mobil.exDocumento.descrDocumento}
			<button type="button" name="voltar" onclick="window.history.back()"
				class="btn btn-secondary float-right" accesskey="r">
				Volta<u>r</u>
			</button>
		</h2>
		<div id="origensmovimentacao">
			<table id="tblTramitacoes"
				class="gt-table table table-sm table-hover">
				<thead class="${thead_color}">
					<tr>
						<c:if test="${docCancelado}">
							<th rowspan="2">Via Doc</th>
						</c:if>
						<th colspan="3">De</th>
						<th colspan="3">Para</th>
						<th colspan="2" rowspan="2">Evento</th>
					</tr>
					<tr>
						<th>Data</th>
						<th>Órgão / Unidade</th>
						<th>Usuário</th>
						<th>Data</th>
						<th>Órgão / Unidade</th>
						<th>Usuário</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="movimentacao" items="${movimentacoes}">
						<c:set var="isTramitacao"
							value="${movimentacao.exTipoMovimentacao.id == 3}" />
						<c:set var="movimentacaoRecebimento"
							value="${isTramitacao? recebimentos[movimentacao.idMov]: null}" />
						<tr id="movimentacao_${movimentacao.idMov}">
							<c:if test="${docCancelado}">
								<td class="via fa-fw">${movimentacao.exMobil.terminacaoSigla}</td>
							</c:if>
							<td class="de_data"><fmt:formatDate
									value="${movimentacao.dtIniMov}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
							<td class="de_unidade">
								${movimentacao.cadastrante.orgaoUsuario.nmOrgaoUsu} /
								${movimentacao.cadastrante.lotacao.nomeLotacao}</td>
							<td class="de_usuario">${movimentacao.cadastrante}
								${movimentacao.cadastrante.nomePessoa}</td>
							<td class="para_data">
								<%-- 
								--%> <c:if
									test="${isTramitacao && (not empty movimentacaoRecebimento)}">
									<%-- 
Se a movimentação é de Tramitação, verifica se a movimentação anterior 
(que na verdade aconteceu depois) é de recebimento. Em caso positivo, 
usa sua hora como recebimento da tramitação. 
										--%>
									<fmt:formatDate value="${movimentacaoRecebimento.dtIniMov}"
										pattern="dd/MM/yyyy HH:mm:ss" />
								</c:if> <%-- 
								--%>
							</td>
							<td class="para_unidade">
								<%-- 
								--%> <c:if test="${isTramitacao}">
										${movimentacao.lotaResp.orgaoUsuario.nmOrgaoUsu} / 
										${movimentacao.lotaResp.nomeLotacao}
									</c:if> <%-- 
								--%>
							</td>
							<td class="para_usuario">
								<%-- 
								--%>
								<c:if test="${isTramitacao}">
									<c:choose>
										<c:when test="${not empty movimentacao.resp}">
											<%-- 
Quando na tramitação indico o usuário destinatário 
													--%>
													${movimentacao.resp} ${movimentacao.resp.nomePessoa}
												</c:when>
										<c:when test="${not empty movimentacaoRecebimento}">
											<%-- 
Quando na tramitação não aponto o usuário destinatário mas sim a unidade. 
Então só saberei quem foi o usuário que recebeu depois que alguém da unidade de destino abri-lo. 
													--%>
												${movimentacaoRecebimento.resp} ${movimentacaoRecebimento.resp.nomePessoa}
											</c:when>
									</c:choose>
								</c:if> <%-- 
								--%>
							</td>
							<td class="evento">
								${movimentacao.exTipoMovimentacao.descrTipoMovimentacao}</td>
							<td class="juntado fa-fw">
								<%-- 
								--%> <c:if
									test="${(movimentacao.exTipoMovimentacao.id == 12) and (not empty movimentacao.exMobilRef)}">
									<%-- É Juntada? Tem documento Pai? --%>
									<div class="text-nowrap">${movimentacao.exMobilRef.sigla}</div>
									<c:choose>
										<c:when
											test="${empty movimentacao.exMobilRef.getMovimentacoesPorTipo(3, false)}">
											<c:set var="link" value="javascript:void(0)" />
											<c:set var="title" value="Não tem Histórico de Tramitação" />
											<c:set var="classDisabled" value="disabled" />
										</c:when>
										<c:otherwise>
											<c:set var="link"
												value="${pageContext.request.contextPath}/app/expediente/doc/exibirMovimentacoesTramitacao?idMobil=${movimentacao.exMobilRef.idMobil}" />
											<c:set var="title" value="Ver Histórico de Tramitação" />
											<c:set var="classDisabled" value="" />
										</c:otherwise>
									</c:choose>
									<a
										class="fa fa-search btn btn-default btn-sm xrp-label ${classDisabled}"
										title="${title}" href="${link}"></a>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</siga:pagina>