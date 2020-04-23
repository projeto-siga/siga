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
	<div class="container-fluid content" id="page">
		<h2 class="mt-3">
			Histórico de tramitação: ${mobil.sigla}
			${mobil.exDocumento.descrDocumento}
			<button type="button" name="voltar" onclick="window.history.back()"
				class="btn btn-secondary float-right" accesskey="r">
				Volta<u>r</u>
			</button>
		</h2>
		<div id="origensTramitacao">
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
					<c:forEach var="tramitacao" items="${tramitacoes}">
						<tr id="tramitacao_${tramitacao.idMov}">
							<td class="de_data"><fmt:formatDate
									value="${tramitacao.dtIniMov}" pattern="dd/MM/yyyy HH:mm:ss" />
							</td>
							<td class="de_unidade">
								${tramitacao.cadastrante.orgaoUsuario.nmOrgaoUsu} / ${tramitacao.cadastrante.lotacao.nomeLotacao}
							</td>
							<td class="de_usuario">${tramitacao.cadastrante}
								${tramitacao.cadastrante.nomePessoa}</td>
							<td class="para_data"><c:if
									test="${tramitacao.exTipoMovimentacao.id == 3}">
									<fmt:formatDate value="${tramitacao.dtFimMov}"
										pattern="dd/MM/yyyy HH:mm:ss" />
								</c:if></td>
							<td class="para_unidade"><c:if
									test="${tramitacao.exTipoMovimentacao.id == 3}">
								${tramitacao.resp.orgaoUsuario.nmOrgaoUsu}  / ${tramitacao.resp.lotacao.nomeLotacao}
								</c:if></td>
							<td class="para_usuario"><c:if
									test="${tramitacao.exTipoMovimentacao.id == 3}">
									${tramitacao.resp} ${tramitacao.resp.nomePessoa}
								</c:if></td>
							<td class="evento">
								${tramitacao.exTipoMovimentacao.descrTipoMovimentacao}</td>
							<td class="juntado"></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</siga:pagina>