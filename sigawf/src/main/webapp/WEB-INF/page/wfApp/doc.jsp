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

				<!-- Conhecimento -->
				<c:if
					test="${!pi.isDesabilitarConhecimento(titular, lotaTitular) && f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC')}">
					<div id="gc-ancora-${pi.id}" />
					<c:url var="url" value="/../sigagc/app/knowledgeInplaceMinimal"> 
						<c:param name="tags">${pi.ancora}</c:param>
						<c:param name="msgvazio">Ainda não existe uma descrição de como esta tarefa deve ser executada. Por favor, clique <a
								href="$1">aqui</a> para contribuir.</c:param>
						<c:param name="titulo">${pi.definicaoDeProcedimento.nome} - ${pi.currentTaskDefinition.nome}</c:param>
						<c:param name="ts">${currentTimeMillis}</c:param>
					</c:url>
					<script type="text/javascript">
						$.ajax({
							type : "GET",
							url : "${url}",
							cache : false,
							success : function(response) {
								$('#gc-ancora-${pi.id}').replaceWith(response);
							}
						});
					</script>
				</c:if>

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
