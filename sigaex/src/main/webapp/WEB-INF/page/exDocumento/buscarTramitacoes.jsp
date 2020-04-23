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
					<c:forEach var="movimentacao" items="${tramitacoes}">
						<tr id="movimentacao_${movimentacao.idMov}">
							<td class="de_data">
								<fmt:formatDate
									value="${movimentacao.dtIniMov}" 
									pattern="dd/MM/yyyy HH:mm:ss" />
							</td>
							<td class="de_unidade">
								${movimentacao.cadastrante.orgaoUsuario.nmOrgaoUsu} /
								${movimentacao.cadastrante.lotacao.nomeLotacao}
							</td>
							<td class="de_usuario">
								${movimentacao.cadastrante}
								${movimentacao.cadastrante.nomePessoa}
							</td>
							<td class="para_data">
								<c:if test="${movimentacao.exTipoMovimentacao.id == 3}">
									<fmt:formatDate value="${movimentacao.dtFimMov}"
										pattern="dd/MM/yyyy HH:mm:ss" />
								</c:if>
							</td>
							<td class="para_unidade">
								<c:if test="${movimentacao.exTipoMovimentacao.id == 3}">
									${movimentacao.resp.orgaoUsuario.nmOrgaoUsu} / 
									${movimentacao.resp.lotacao.nomeLotacao}
								</c:if>
							</td>
							<td class="para_usuario">
								<c:if test="${movimentacao.exTipoMovimentacao.id == 3}">
									${movimentacao.resp} ${movimentacao.resp.nomePessoa}
								</c:if>
							</td>
							<td class="evento">
								${movimentacao.exTipoMovimentacao.descrTipoMovimentacao}
							</td>
							<td class="juntado fa-fw">
								<c:if test="${not empty movimentacao.exMobilRef}">
									<div class="text-nowrap">${movimentacao.exMobilRef.sigla}</div>
									<c:set var="temTramitacoes"
										value="${not empty movimentacao.exMobilRef.getMovimentacoesPorTipo(3)}" />
									<c:choose>
										<c:when test="${empty movimentacao.exMobilRef.getMovimentacoesPorTipo(3)}">
											<c:set var="link" value="javascript:void(0)" />
											<c:set var="title" value="Não tem Histórico de Tramitação"/>
											<c:set var="classDisabled" value="disabled"/>
										</c:when>
										<c:otherwise>
											<c:set var="link" value="${f:concat(f:concat(pageContext.request.contextPath, '/app/expediente/doc/exibirTramitacao?idMovimentacao='), movimentacao.exMobilRef.idMobil)}" />
											<c:set var="title" value="Ver Histórico de Tramitação"/>
											<c:set var="classDisabled" value=""/>
										</c:otherwise>
									</c:choose>
									<a class="fa fa-search btn btn-default btn-sm xrp-label ${classDisabled}"
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