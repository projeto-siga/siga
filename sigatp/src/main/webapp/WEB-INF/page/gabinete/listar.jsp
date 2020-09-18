<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<siga:pagina titulo="Transportes">
	<form id="formulario">
		<div class="gt-bd clearfix">
			<div class="gt-content clearfix">
				<h2>Abastecimentos</h2>
	
				<sigatp:erros />
	
				<c:choose>
					<c:when test="${abastecimentos.size() > 0}">
						<div class="gt-content-box gt-for-table">
							<table id="htmlgrid" class="gt-table tablesorter" width="100%">
								<thead>
									<tr class="header">
										<th width="1%" class="noSort"></th>
										<th width="1%" class="noSort"></th>
										<th>Data e Hora</th>
										<th>Tipo</th>
										<th>Qtd.(l)</th>
										<th>Pre&ccedil;o/litro (R$)</th>
										<th>Valor da NF (R$)</th>
										<th>Ve&iacute;culo</th>
										<th>Condutor</th>
										<th>Nota Fiscal</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${abastecimentos}" var="abastecimento">
										<tr>
											<td>
												<sigatp:formatarColuna operacao="editar" href="${linkTo[GabineteController].editar(abastecimento.id)}" titulo="abastecimento" />
											</td>
											<td>
												<sigatp:formatarColuna operacao="excluir" href="${linkTo[GabineteController].excluir(abastecimento.id)}" titulo="abastecimento"
												class="lnkMotivoLog" />
											</td>
											<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${abastecimento.dataHora.time}" /></td>
											<td>${abastecimento.tipoDeCombustivel.descricao}</td>
											<td>${abastecimento.formataMoedaBrasileiraSemSimbolo(abastecimento.quantidadeEmLitros)}</td>
											<td>${abastecimento.formataMoedaBrasileiraSemSimbolo(abastecimento.precoPorLitro)}</td>
											<td>${abastecimento.formataMoedaBrasileiraSemSimbolo(abastecimento.valorTotalDaNotaFiscal)}</td>
											<td>${abastecimento.veiculo.placa}</td>
											<td>${abastecimento.condutor.dadosParaExibicao}</td>
											<td>${abastecimento.numeroDaNotaFiscal}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<sigatp:paginacao />  
					</c:when>
					<c:otherwise>
						<br />
						<h3>N&atilde;o existem abastecimentos cadastrados.</h3>
					</c:otherwise>
				</c:choose>
	
				<div class="gt-table-buttons">
					<a href="${linkTo[GabineteController].incluir}"
						id="botaoIncluirAbastecimento" class="gt-btn-medium gt-btn-left"><fmt:message
							key="views.botoes.incluir" /></a>
				</div>
			</div>
		</div>
		<sigatp:motivoLog />
	</form>
</siga:pagina>