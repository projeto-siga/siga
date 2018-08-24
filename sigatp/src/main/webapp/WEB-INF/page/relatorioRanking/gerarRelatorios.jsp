<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<siga:pagina titulo="Transportes">

	<style>
		table, tr, th, td {
			background-color: white;
			padding: 1px;
			wborder: 0px solid white;
			-moz-border-radius: 6px;
			-webkit-border-radius: 6px;
			border-radius: 6px;
		}
	
		table tr.header {
			background-color: white;
		}
	</style>

	<div class="gt-bd">
		<div class="gt-content">
			<h2>
				Ranking por Requisi&ccedil;&otilde;es do per&iacute;odo de <fmt:formatDate value="${relatorioRanking.dataInicio.time}" pattern="dd/MM/yyyy" />
				a
				<fmt:formatDate value="${relatorioRanking.dataFim.time}" pattern="dd/MM/yyyy" />
			</h2>

			<div style="width: 100%; display: block;">
				<div style="width: 49%; float: left; clear: both; padding: 0; margin: 0;">
					<div style="width: 100%; padding: 0; margin: 0;">
						<h2 class="gt-table-head">Condutores</h2>
						<table border="0" class="gt-table">
							<thead>
								<tr>
									<th width="30%">Condutor</th>
									<th width="5%" style="text-align: right">Requisi&ccedil;&otilde;es</th>
									<th width="5%" style="text-align: right">Miss&otilde;es</th>
								</tr>
							</thead>
							<tbody>
								<c:if test="${rc != null}">
									<c:forEach items="${rc}" var="item">
										<tr>
											<td>${item.condutor.dadosParaExibicao}</td>
											<td align="right">${item.requisicoes.size()}</td>
											<td align="right">${item.missoes.size()}</td>
										</tr>
									</c:forEach>
								</c:if>
							</tbody>
						</table>

						<br> <br>

						<h2 class="gt-table-head">Ve&iacute;culos</h2>
						<table border="0" class="gt-table margemInferior">
							<thead>
								<tr>
									<th width="5%">Ve&iacute;culo</th>
									<th width="5%" style="text-align: right">Requisi&ccedil;&otilde;es</th>
								</tr>
							</thead>
							<tbody>
								<c:if test="${rv != null}">
									<c:forEach items="${rv}" var="item">
										<tr>
											<td>${item.veiculo.dadosParaExibicao}</td>
											<td align="right">${item.requisicoes.size()}</td>
										</tr>
									</c:forEach>
								</c:if>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div style="width: 49%; float: right; padding: 0; margin: 0;">
				<div style="width: 100%; padding: 0; margin: 0;">
					<h2 class="gt-table-head">Finalidades</h2>
					<table border="0" class="gt-table">
						<thead>
							<tr>
								<th width="10%">Finalidade</th>
								<th width="5%" style="text-align: right">Requisi&ccedil;&otilde;es</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${rf != null}">
								<c:forEach items="${rf}" var="item">
									<tr>
										<td>${null != item.finalidade ? item.finalidade.descricao : ""}</td>
										<td align="right">${item.totalFinalidade}</td>
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
					</table>

					<br> <br>

					<h2 class="gt-table-head">Tipos de Passageiros</h2>
					<table border="0" class="gt-table">
						<thead>
							<tr>
								<th width="5%">Tipo</th>
								<th width="5%" style="text-align: right">Requisi&ccedil;&otilde;es</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${rtp != null}">
								<c:forEach items="${rtp}" var="item">
									<tr>
										<td>${item.tipoPassageiro}</td>
										<td align="right">${item.totalTipoPassageiros}</td>
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
				</div>
			</div>

			<div class="gt-table-buttons">
				<input type="button" value=<fmt:message key="views.botoes.voltar"/> onClick="javascript:location.href='${linkTo[RelatorioRankingController].consultar}'" class="gt-btn-medium gt-btn-left" />
			</div>
		</div>
	</div>
</siga:pagina>