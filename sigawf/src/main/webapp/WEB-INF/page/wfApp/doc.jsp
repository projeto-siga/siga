<%@ include file="/WEB-INF/page/include.jsp"%>


<c:if test="${mobilMap.size() == 0}">
	<!-- Siga-Wf consultado com sucesso, mas nenhum fluxo do associado a este documento. -->
</c:if>
<c:forEach var="item" items="${mobilMap}">
	<!--ajax:${item.key}-->
	<c:forEach var="t" items="${item.value}">
		<c:set var="task" value="${t}" scope="request" />
		<c:set var="piId" value="${t.id}" scope="request" />
		<c:set var="ajax" value="sim" scope="request" />

		<div class="card mb-3 border-info">
			<div class="card-header bg-info text-white">
				<a href="${linkTo[WfAppController].procedimento(piId)}"
					style="color: white; text-decoration: underline;">${t.nomeDoProcedimento}</a>
				- ${t.nomeDaTarefa}
			</div>
			<div class="card-body">
				<form method="POST"
					action="${linkTo[WfAppController].continuar(piId)}?sigla=${param.sigla}"
					style="margin: 0px; padding: 0px; border: 0px;" id="form${piId}">
					<%@ include file="inc-form.jsp"%>
				</form>
			</div>
		</div>

	</c:forEach>
	<!--/ajax:${item.key}-->
</c:forEach>
