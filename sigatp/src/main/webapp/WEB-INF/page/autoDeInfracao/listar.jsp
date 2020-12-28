<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<siga:pagina titulo="Transportes">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Autos de Infra&ccedil;&atilde;o</h2>
			
			<sigatp:erros />
			
			<c:choose>
				<c:when test="${autosDeInfracao.size() > 0}">
					<div class="gt-content-box gt-for-table">
						<table id="htmlgrid" class="gt-table tablesorter">
							<thead>
								<tr>
									<th width="1%" class="noSort"></th>
									<th width="1%" class="noSort"></th>
									<th>Data e Hora</th>
									<th>Ve&iacute;culo</th>
									<th>Condutor</th>
									<th>Nº Proc. Recurso</th>
									<th>Pago?</th>
									<th>Recebido?</th>
									<th>Descri&ccedil;&atilde;o</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${autosDeInfracao}" var="autoDeInfracao">
									<tr>
										<td>
											<sigatp:formatarColuna operacao="editar" href="${linkTo[AutoDeInfracaoController].editar(autoDeInfracao.id)}" titulo="auto de Infra&ccedil;&atilde;o" />
										</td>
										<td>
											<sigatp:formatarColuna operacao="excluir" href="${linkTo[AutoDeInfracaoController].excluir(autoDeInfracao.id)}" titulo="auto de Infra&ccedil;&atilde;o"
											 onclick="javascript:return confirm('Tem certeza de que deseja excluir este auto de infra&ccedil;&atilde;o?');" />
										</td>
										<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${autoDeInfracao.dataHora.time}"/></td>
										<td>${autoDeInfracao.veiculo.dadosParaExibicao}</td>
										<td>${autoDeInfracao.condutor.dadosParaExibicao}</td>
										<td>${autoDeInfracao.numeroDoProcesso}</td>
										<td>${autoDeInfracao.foiPago().descricao}</td>
										<td>${autoDeInfracao.foiRecebido.descricao}</td>
										<td>${autoDeInfracao.penalidade.dadosParaExibicao}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<sigatp:paginacao />
				</c:when>
				<c:otherwise>
					<br/>
					<h3>N&atilde;o existem autos de infra&ccedil;&atilde;o cadastrados.</h3>
				</c:otherwise>
			</c:choose>

			<div class="gt-table-buttons">
				<a href="${linkTo[AutoDeInfracaoController].incluir('AUTUACAO')}" class="gt-btn-medium gt-btn-left"><fmt:message key="views.botoes.incluirAutuacao"/></a>
				<a href="${linkTo[AutoDeInfracaoController].incluir('PENALIDADE')}" class="gt-btn-medium gt-btn-left"><fmt:message key="views.botoes.incluirPenalidade"/></a>
			</div>
		</div>
	</div>
</siga:pagina>