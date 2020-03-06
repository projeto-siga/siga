<%@ include file="/WEB-INF/page/include.jsp"%>

<c:if test="${mobilMap.size() == 0}"><!-- Siga-Wf consultado com sucesso, mas nenhum fluxo do associado a este documento. --></c:if>
<c:forEach var="item" items="${mobilMap}">
	<!--ajax:${item.key}-->
	<c:forEach var="t" items="${item.value}">
	    <c:set var="doc_ref" value="${item.key}" scope="request" />
		<c:set var="task" value="${t}" scope="request" />
		<c:set var="variableList" value="${task.variableList}" scope="request" />
		<c:set var="taskInstance" value="${task.taskInstance}" scope="request" />
		<c:set var="tiId" value="${task.taskInstance.id}" scope="request" />
		<c:set var="name" value="${task.taskInstance.task.name}"
			scope="request" />
		<c:set var="descricao" value="${task.descricao}" scope="request" />
		<c:set var="conhecimentoEditavel" value="${task.conhecimentoEditavel}"
			scope="request" />
		<c:set var="ajax" value="sim" scope="request" />

		<form method="POST" action="${linkTo[WfAppController].continuar}"
			style="margin: 0px; padding: 0px; border: 0px;" id="form${tiId}">
			<input name="result" type="hidden" value="doc" /> <input
				name="sigla" type="hidden" value="${item.key}" /> <input
				id="transitionName${tiId}" name="transitionName" type="hidden" value="" />

			<%@ include file="tarefa.jsp"%>
		</form>
	</c:forEach>
	<!--/ajax:${item.key}-->
</c:forEach>

