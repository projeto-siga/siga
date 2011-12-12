<%@ include file="/WEB-INF/page/include.jsp"%><!--  -->

<siga:pagina titulo="Tarefa">

	<h1>Procedimento: ${taskInstance.task.processDefinition.name} -
	Tarefa: ${taskInstance.task.name}</h1>
	<table width="100%">
		<tr>
			<td valign="top"><!-- Adicionando a lista de Tarefas --> <ww:url
				action="executeTask" id="url"></ww:url>
			<form method="GET" action="${url}"><%@ include
				file="/WEB-INF/page/workflow/tarefa.jsp"%></form>

			<!-- Tabela compartilhada com o siga-doc --> <!-- Fim da tabela compartilhada com o siga-doc -->
			<span style="size: 80px;" /> <c:if
				test="${(titular.sigla eq taskInstance.actorId) or (wf:podePegarTarefa(cadastrante, titular,lotaCadastrante,lotaTitular,taskInstance))}">
				<table width="100%" class="form">
					<tr class="header">
						<td colspan="2">Designaçao da Tarefa</td>
					</tr>
					<ww:url id="url" action="assignTask" />
					<form method="GET" action="${url}"><input name="tiId"
						type="hidden" value="${tiId}" />
					<tr>
						<td>Prioridade</td>
						<td><ww:select name="prioridade"
							list="#{1:'Muito Alta', 2:'Alta', 3:'Média', 4:'Baixa', 5:'Muito Baixa'}"
							theme="simple" /></td>
					</tr>
					<tr>
						<td>Pessoa</td>
						<td><siga:selecao propriedade="ator" modulo="../sigaex"
							tema="simple" /></td>
					</tr>
					<tr>
						<td>Lotação</td>
						<td><siga:selecao propriedade="lotaAtor" modulo="../sigaex"
							tema="simple" /></td>
					</tr>
					<tr>
						<td>Justificativa</td>
						<td><input type="text" size="80" name="justificativa" />
						(opcional)</td>
					</tr>
					<tr>
						<td></td>
						<td><input name="designar" type="submit" value="Designar" />
						<c:if test="${empty taskInstance.actorId}">
							<ww:url id="url" action="pegar">
								<ww:param name="tiId">${taskInstance.id}</ww:param>
							</ww:url>
							<input type="button" value="Pegar tarefa para mim"
								onclick="javascript:window.location.href='${url}'">
						</c:if></td>
					</tr>
					</form>
					<tr class="bump">
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr class="header">
						<td colspan="2">Comentários</td>
					</tr>
					<ww:url id="url" action="commentTask" />
					<form method="GET" action="${url}"><input name="tiId"
						type="hidden" value="${tiId}" />
					<tr>
						<td>Comentário</td>
						<td><input type="text" size="80" name="comentario" /></td>
					</tr>
					<tr>
						<td></td>
						<td><input name="butc" type="submit" value="Adicionar" /></td>
					</tr>
					</form>
				</table>
				<br />
			</c:if>
			<table class="message" width="100%">
				<tr class="header">
					<td><b>Procedimento:</b>
					${taskInstance.task.processDefinition.name}</td>
					<td><b>Cadastrante:</b> <ww:property
						value="%{#attr.taskInstance.getVariable('wf_cadastrante')}" /> (<ww:property
						value="%{#attr.taskInstance.getVariable('wf_lota_cadastrante')}" />)</td>
				</tr>
				<tr class="header">
					<td><b>Tarefa:</b> ${taskInstance.task.name}</td>
					<td><b>Titular:</b> <ww:property
						value="%{#attr.taskInstance.getVariable('wf_titular')}" /> (<ww:property
						value="%{#attr.taskInstance.getVariable('wf_lota_titular')}" />)</td>
				</tr>
				<tr class="header">
					<td><b>Início:</b> ${f:espera(taskInstance.create)}</td>
					<td><b>Prioridade:</b> <c:choose>
						<c:when test="${taskInstance.priority == 1}">Muito Alta</c:when>
						<c:when test="${taskInstance.priority == 2}">Alta</c:when>
						<c:when test="${taskInstance.priority == 3}">Média</c:when>
						<c:when test="${taskInstance.priority == 4}">Baixa</c:when>
						<c:when test="${taskInstance.priority == 51}">Muito Baixa</c:when>
					</c:choose></td>
				</tr>
				<tr>
					<td colspan="2">
					<table width="100%">
						<tr class="header">
							<td>Data/Hora</td>
							<td>Atendente</td>
							<td>Descrição</td>
						</tr>
						<c:forEach var="ti" items="${wf:ordenarTarefas(taskInstance)}">
							<c:forEach var="c" items="${wf:ordenarComentarios(ti)}">
								<tr>
									<td>${f:espera(c.time)}</td>
									<td><ww:property value="%{#attr.c.actorId}" /></td>
									<td><ww:property value="%{#attr.c.message}" /></td>
								</tr>
							</c:forEach>
							<tr>
								<td>${f:espera(ti.create)}</td>
								<td><ww:property value="%{#attr.ti.actorId}" /></td>
								<td><b><ww:property value="%{#attr.ti.name}" /></b></td>
							</tr>
						</c:forEach>
					</table>
					</td>
				</tr>
			</table></td>
			<td align="right" valign="top"><tags:wfImage
				task="${taskInstance.id}" token="${taskInstance.token.id}" /></td>
		</tr>
	</table>
</siga:pagina>