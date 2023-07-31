<%@ include file="/WEB-INF/page/include.jsp"%>


<c:if test="${mobilMap.size() == 0}">
	<!-- Siga-Wf consultado com sucesso, mas nenhum fluxo do associado a este documento. -->
</c:if>
<c:forEach var="item" items="${mobilMap}">
	<!--ajax:${item.key}-->
	<c:forEach var="pi" items="${item.value}">
		<c:set var="ajax" value="sim" scope="request" />

		<div id="quadro_destaque_tem_workflow_associado" class="card mb-3 border-warning">
			<div class="card-header bg-warning">
				<u><a class="text-dark" href="${linkTo[WfAppController].procedimento(pi.id)}"
					>${pi.sigla}</a></u> - 
				<strong>${pi.definicaoDeTarefaCorrente.nome}</strong>
						<script type="text/javascript">
							// Identifica o tipo de tarefa para condições no exibe.jsp
							var tipoDeTarefa = "${pi.definicaoDeTarefaCorrente.tipoDeTarefa}";
						</script>
			</div>
			<div class="card-body">

				<!-- Conhecimento -->
				<c:if
					test="${!pi.isDesabilitarConhecimento(titular, lotaTitular) && f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC')}">
					<div id="gc-ancora-${pi.id}" />
					<c:url var="url" value="/../sigagc/app/knowledgeInplaceMinimal"> 
						<c:param name="tags">${pi.ancora}</c:param>
						<c:param name="msgvazio">Ainda nÃ£o existe uma descriÃ§Ã£o de como esta tarefa deve ser executada. Por favor, clique <a
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
