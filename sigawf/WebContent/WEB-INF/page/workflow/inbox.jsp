<%@ include file="/WEB-INF/page/include.jsp"%><!--  -->

<!-- Tabela com a lista de Tarefas -->
<table class="index" width="100%">
	<tr class="header">
		<td colspan="1">Tarefa</td>
		<!-- <td colspan="1">Procedimento</td> -->
		<td colspan="1" align="center">Atendente</td>
		<td colspan="1" align="center">Prioridade</td>
		<td colspan="1" align="right">Início</td>
	</tr>
	<c:forEach var="taskInstance" items="${taskInstances}">
		<tr class="count">
			<td><ww:url id="url" action="task">
				<ww:param name="tiId">${taskInstance.id}</ww:param>
			</ww:url>

			<a href="${url}">${taskInstance.task.name}</a><%-- <c:if
				test="${empty taskInstance.actorId and wf:podePegarTarefa(cadastrante,titular,lotaCadastrante, lotaTitular,taskInstance) == true}">
				<ww:url id="url" action="pegar">
					<ww:param name="tiId">${taskInstance.id}</ww:param>
				</ww:url>
				<a href="${url}">(pegar)</a>
			</c:if>--%></td>

			<!-- <td>${taskInstance.task.processDefinition.name}</td> -->
			<td align="center"><c:set var="atendente">
				<ww:property value="%{getPooledActors(#attr.taskInstance)}" />
			</c:set> <c:set var="atendenteNome">
				<ww:property value="%{getDescricao(atendente)}" />
			</c:set> <c:if
				test="${atendente != titular.sigla and atendente != lotaTitular.sigla}">
				<span style="color: silver">
			</c:if> <siga:selecionado sigla="${atendente}" descricao="${atendenteNome}" /><c:if
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
			<td align="right">${f:espera(taskInstance.create)}</td>
		</tr>
	</c:forEach>
</table>
<br />

<c:if
	test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF;OPERAR:Executar comandos da tela inicial')}">
	<ww:form name="frmWF" action="initializeProcess" namespace="/"
		method="get" theme="simple">
		<input type="hidden" name="orgao"
			value="${lotaTitular.orgaoUsuario.acronimoOrgaoUsu}" />
		<input type="hidden" name="procedimento" value="" />
		<input type="hidden" name="secaoUsuario"
			value="${lotaTitular.orgaoUsuario.descricaoMaiusculas}" />
		<table width="100%" class="zero">
			<tr>
				<td><ww:select name="docAcao"
					list="#{'/sigawf/initializeProcess.action':'Iniciar', '/sigawf/configurar.action':'Configurar', '/sigawf/pesquisarDesignacao.action':'Designar Tarefas', '/sigawf/medir.action':'Apresentar Métricas'}"
					onchange="javascript: if (this.value == '04') window.location='${url4}'; if (this.value == '05') window.location='${url5}'; if (this.value == '06') window.location='${url6}'; if (this.value == '07') window.location='${url7}';" />

				<ww:select name="pdId" list="processDefinitions" listKey="id"
					listValue="name" theme="simple" /> <input type="button" name="ok"
					value="Ok" onclick="javascript:submitWF();" /></td>
			</tr>
		</table>
	</ww:form>

</c:if>

<script type="text/javascript">
submitWF = function() {
	frmWF.action=frmWF.docAcao.value;
	//frmWF.docAcao="";
	frmWF.procedimento.value=frmWF.pdId[frmWF.pdId.selectedIndex].text
	frmWF.submit();
}

</script>
