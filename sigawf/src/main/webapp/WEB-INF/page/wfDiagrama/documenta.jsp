<%@ include file="/WEB-INF/page/include.jsp"%>
<siga:pagina titulo="${pi.sigla}"
	incluirJs="/siga/javascript/svg-pan-zoom/svg-pan-zoom.min.js">


	<div class="container-fluid content" id="page">

		<div class="row" style="margin-bottom: -.6em;">
			<div class="col">
				<h2 class="">Documentar Diagrama ${pd.sigla}: ${pd.nome}</h2>
				<!-- lista de Ações -->
				<siga:links>
					<siga:link icon="" title="Voltar" url="javascript: history.back();"
						estilo="line-height: 160% !important" atalho="${true}"
						test="${true}" />
				</siga:links>
			</div>
		</div>
		<div class="row">
			<div class="col col-12">
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC')}">
					<span id="gc-ancora"></span>
					<c:url var="url" value="/../sigagc/app/knowledgeInplace">
						<c:param name="tags">${pd.ancora}</c:param>
						<c:param name="msgvazio">Ainda não existe uma descrição deste procedimento. Por favor, clique <a
								href="$1">aqui</a> para contribuir.</c:param>
						<c:param name="titulo">${pd.nome}</c:param>
						<c:param name="ts">${currentTimeMillis}</c:param>
						<c:param name="label">Documentação do Procedimento</c:param>
					</c:url>
					<script type="text/javascript">
						$.ajax({
							type : "GET",
							url : "${url}",
							cache : false,
							success : function(response) {
								$('#gc-ancora').replaceWith(response);
							}
						});
					</script>
				</c:if>

				<c:forEach items="${pd.definicaoDeTarefa}" var="td" varStatus="loop">
					<h6 class="mt-2" style="margin-bottom: -.8em;">${loop.count})&nbsp;${td.nome}</h6>

					<!-- Conhecimento -->
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC')}">
						<div class="card-deck">
							<span id="gc-ancora-descr-${td.id}"></span> <span
								id="gc-ancora-${td.id}"></span> <span id="gc-${td.id}"></span>
						</div>

						<c:url var="url" value="/../sigagc/app/knowledgeInplace">
							<c:param name="tags">${td.ancoraDescr}</c:param>
							<c:param name="msgvazio">Ainda não existe uma descrição desta tarefa. Por favor, clique <a
									href="$1">aqui</a> para contribuir.</c:param>
							<c:param name="titulo">${pd.nome} - ${td.nome}</c:param>
							<c:param name="ts">${currentTimeMillis}</c:param>
							<c:param name="label">Descrição da Tarefa</c:param>
						</c:url>
						<script type="text/javascript">
							$.ajax({
								type : "GET",
								url : "${url}",
								cache : false,
								success : function(response) {
									$('#gc-ancora-descr-${td.id}').replaceWith(
											response);
								}
							});
						</script>

						<c:url var="url" value="/../sigagc/app/knowledgeInplace">
							<c:param name="tags">${td.ancora}</c:param>
							<c:param name="msgvazio">Ainda não existe uma descrição de como esta tarefa deve ser executada. Por favor, clique <a
									href="$1">aqui</a> para contribuir.</c:param>
							<c:param name="titulo">${pd.nome} - ${td.nome}</c:param>
							<c:param name="ts">${currentTimeMillis}</c:param>
							<c:param name="label">Passo a Passo</c:param>
						</c:url>
						<script type="text/javascript">
							$.ajax({
								type : "GET",
								url : "${url}",
								cache : false,
								success : function(response) {
									$('#gc-ancora-${td.id}').replaceWith(
											response);
								}
							});
						</script>

						<c:if test="${false}">
							<c:url var="url" value="/../sigagc/app/knowledgeSidebar">
								<c:param name="tags">@workflow</c:param>
								<c:forEach var="tag" items="${td.tags}">
									<c:param name="tags">${tag}</c:param>
								</c:forEach>
								<c:param name="ts">${currentTimeMillis}</c:param>
							</c:url>
							<script type="text/javascript">
								$
										.ajax({
											type : "GET",
											url : "${url}",
											cache : false,
											success : function(response) {
												$('#gc-${td.id}').replaceWith(
														response);
											}
										});
							</script>
						</c:if>
					</c:if>
				</c:forEach>
			</div>
		</div>
	</div>
</siga:pagina>