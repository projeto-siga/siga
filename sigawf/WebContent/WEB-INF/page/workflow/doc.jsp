<%@ include file="/WEB-INF/page/include.jsp"%>

<c:forEach var="item" items="${mobilMap}">
	<!--ajax:${item.key}-->
	<c:forEach var="t" items="${item.value}">
		<c:set var="task" value="${t}" scope="request" />
		<form method="POST" action="/sigawf/executeTask.action" style="margin: 0px; padding: 0px; border: 0px;">
		<input name="result" type="hidden" value="doc" />
		<input name="sigla" type="hidden" value="${item.key}" />

		<c:set var="variableList" value="${task.variableList}" scope="request" />
		<c:set var="taskInstance" value="${task.taskInstance}" scope="request" />
		<c:set var="tiId" value="${task.taskInstance.id}" scope="request" />
		<c:set var="name" value="${task.taskInstance.task.name}"
			scope="request" />
		<c:set var="descricao" value="${task.descricao}" scope="request" />
		<c:set var="conhecimentoEditavel" value="${task.conhecimentoEditavel}" scope="request" />

		<c:set var="ajax" value="sim" scope="request" />
		<%@ include file="tarefa.jsp"%>
		</form>
	</c:forEach>
	<!--/ajax:${item.key}-->
</c:forEach>

