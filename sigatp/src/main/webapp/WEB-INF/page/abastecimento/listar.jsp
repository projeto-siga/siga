<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<siga:pagina titulo="Transportes">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<c:choose>
				<c:when test="${!menuAbastecimentosMostrarAvancado}">
					<h2>Filtro Avan&ccedil;ado de Abastecimentos</h2>
				</c:when>
				<c:otherwise>
					<h2>Abastecimentos</h2>
				</c:otherwise>
			</c:choose>

			<jsp:include page="menu.jsp"/>
			
			<div style="display:block;">
				<c:choose>
					<c:when test="${abastecimentos.size() > 0}">
						<div class="gt-content-box gt-for-table">
							<table id="htmlgrid" class="gt-table tablesorter">
								<thead>
									<tr>
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
												<sigatp:formatarColuna operacao="editar" href="${linkTo[AbastecimentoController].editar(abastecimento.id)}"	titulo="abastecimento" />
											</td>
											<td>
												<sigatp:formatarColuna operacao="excluir" href="${linkTo[AbastecimentoController].excluir(abastecimento.id)}" titulo="abastecimento" 
												classe="lnkMotivoLog" />
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
						<c:choose>
							<c:when test="${!menuAbastecimentosMostrarAvancado}">
								<h3><fmt:message key="abastecimentos.filtro.listaVazia" /></h3>
							</c:when>
							<c:otherwise>
								<h3><fmt:message key="abastecimentos.listaVazia" /></h3>
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</div>
			
			<div class="gt-table-buttons">
				<a href="${linkTo[AbastecimentoController].incluir}" id="botaoIncluirAbastecimento" 
				class="gt-btn-medium gt-btn-left"><fmt:message key="views.botoes.incluir" /></a>
			</div>
		</div>
	</div>
	
	<form id="formulario">
		<sigatp:motivoLog />
	</form>
</siga:pagina>