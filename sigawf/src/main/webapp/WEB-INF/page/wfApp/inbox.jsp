<%@ include file="/WEB-INF/page/include.jsp"%><!--  -->

<!-- Tabela com a lista de Tarefas -->
<div id="sigawf"></div>
<c:if test="${empty tarefas}">
	Nenhuma tarefa ativa
</c:if>
<c:if test="${not empty tarefas}">
	<div class="gt-content-box gt-for-table">
		<table border="0" class="gt-table">
			<thead>
				<tr>
					<th>Tarefa</th>
					<th>Principal</th>
					<th style="text-align: center">Atendente</th>
					<th style="text-align: center">Prioridade</th>
					<th style="text-align: right">Início</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="taskInstance" items="${tarefas}">
					<tr class="count">
						<td><a
							href="${linkTo[WfAppController].procedimento(taskInstance.instanciaDeProcedimento.id)}">${taskInstance.definicaoDeTarefa.nome}</a></td>
						<td><c:choose>
								<c:when
									test="${taskInstance.instanciaDeProcedimento.tipoDePrincipal eq 'DOCUMENTO' and not empty taskInstance.instanciaDeProcedimento.principal}">
									<a
										href="/sigaex/app/expediente/doc/exibir?sigla=${taskInstance.instanciaDeProcedimento.principal}">${taskInstance.instanciaDeProcedimento.principal}</a>
								</c:when>
								<c:otherwise>${taskInstance.instanciaDeProcedimento.principal}</c:otherwise>
							</c:choose></td>

						<!-- <td>${taskInstance.instanciaDeProcedimento.definicaoDeProcedimento.nome}</td> -->
						<td align="center"><c:set var="atendente">${taskInstance.instanciaDeProcedimento.atendente}</c:set>
							<c:set var="atendenteNome">${pooledActorsDescricao[atendente]}</c:set>
							<c:if
								test="${atendente != titular.sigla and atendente != lotaTitular.sigla}">
								<span style="color: silver">
							</c:if> <siga:selecionado sigla="${atendente}"
								descricao="${atendenteNome}" /> <c:if
								test="${atendente != titular.sigla and atendente != lotaTitular.sigla}">
								</span>
							</c:if></td>
						<td align="center">${taskInstance.instanciaDeProcedimento.prioridade}</td>
						<td align="right">${fn:replace(f:esperaSimples(taskInstance.instanciaDeProcedimento.eventoData),
						" ", "&nbsp;")}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</c:if>
