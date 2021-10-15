<%@ include file="/WEB-INF/page/include.jsp"%>


<c:if test="${mobilMap.size() == 0}">
	<!-- Siga-Wf consultado com sucesso, mas nenhum fluxo do associado a este documento. -->
</c:if>
<c:forEach var="item" items="${mobilMap}">
	<!--ajax:${item.key}-->
	<c:forEach var="pi" items="${item.value}">
		<c:set var="ajax" value="sim" scope="request" />

		<div class="card mb-3 border-info">
			<div class="card-header bg-info text-white">
				<a href="${linkTo[WfAppController].procedimento(pi.id)}"
					style="color: white; text-decoration: underline;">${pi.sigla}</a> -
				${pi.definicaoDeProcedimento.nome} -
				${pi.definicaoDeTarefaCorrente.nome}
			</div>
			<div class="card-body">
				<form method="POST"
					action="${linkTo[WfAppController].continuar(pi.id)}?sigla=${param.sigla}"
					style="margin: 0px; padding: 0px; border: 0px;" id="form${pi.id}">
					<%@ include file="inc-form.jsp"%>
				</form>
			</div>
		</div>

	</c:forEach>
	<!--/ajax:${item.key}-->
</c:forEach>
