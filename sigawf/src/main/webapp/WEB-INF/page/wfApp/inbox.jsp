<%@ include file="/WEB-INF/page/include.jsp"%><!--  -->

<!-- Tabela com a lista de Tarefas -->
<div id="sigawf"></div>
<div class="gt-content-box gt-for-table">
	<table border="0" class="gt-table">
		<thead>
			<tr>
				<th>Tarefa</th>
				<th style="text-align: center">Atendente</th>
				<th style="text-align: center">Prioridade</th>
				<th style="text-align: right">Início</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="taskInstance" items="${taskInstances}">
				<tr class="count">
					<td>
						<a href="${linkTo[WfAppController].procedimento[taskInstance.id]}">${taskInstance.task.name}</a></td>

					<!-- <td>${taskInstance.task.processDefinition.name}</td> -->
					<td align="center"><c:set var="atendente">${pooledActors[taskInstance]}</c:set>
						<c:set var="atendenteNome">${pooledActorsDescricao[atendente]}</c:set>
						<c:if
							test="${atendente != titular.sigla and atendente != lotaTitular.sigla}">
							<span style="color: silver">
						</c:if> <siga:selecionado sigla="${atendente}"
							descricao="${atendenteNome}" /> <c:if
							test="${atendente != titular.sigla and atendente != lotaTitular.sigla}">
							</span>
						</c:if></td>
					<td align="center"><c:choose>
							<c:when test="${taskInstance.priority == 1}">Muito Alta</c:when>
							<c:when test="${taskInstance.priority == 2}">Alta</c:when>
							<c:when test="${taskInstance.priority == 3}">Média</c:when>
							<c:when test="${taskInstance.priority == 4}">Baixa</c:when>
							<c:when test="${taskInstance.priority == 51}">Muito Baixa</c:when>
						</c:choose></td>
					<td align="right">${fn:replace(f:esperaSimples(taskInstance.create),
						" ", "&nbsp;")}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>