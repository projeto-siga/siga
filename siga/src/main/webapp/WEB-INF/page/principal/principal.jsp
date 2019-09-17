<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<siga:pagina titulo="P&aacute;gina Inicial"
	incluirJs="/siga/javascript/principal.js">

<!-- Pull the Wootric Snippet -->
<script type="text/javascript" src="https://cdn.wootric.com/wootric-sdk.js"></script>
<!-- begin Wootric code -->
<script type="text/javascript">
  window.wootricSettings = {
    email:'joaoluis@trf2.jus.br',// TODO: Required to uniquely identify a user. Email is recommended but this can be any unique identifier.
    created_at: 1234567890, // TODO: The current logged in user's sign-up date as a Unix timestamp.
    account_token: 'NPS-89f8dfcd'
  };

  // Request a survey
  window.wootric('run');
</script>
<!-- end Wootric code -->

	<div class="container-fluid content">
		<c:if test="${not empty acessoAnteriorData}">
			<div class="row">
				<div class="col">
					<p id="bem-vindo" class="alert alert-success mt-3 mb-0">Último acesso em ${acessoAnteriorData} no endereço ${acessoAnteriorMaquina}.</p>
					<script>
						setTimeout(function() {
							$('#bem-vindo').fadeTo(1000, 0, function() {
								$('#bem-vindo').slideUp(1000);
							});
						}, 5000);
					</script>
				</div>
			</div>
		</c:if>
		<c:if test="${not empty mensagem}">
			<div class="row">
				<div style="">
					<p id="mensagem" class="alert alert-success">${mensagem}</p>
					<script>
						setTimeout(function() {
							$('#mensagem').fadeTo(1000, 0, function() {
								$('#mensagem').slideUp(1000);
							});
						}, 5000);
					</script>
				</div>
			</div>
		</c:if>
		<div class="row">
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC:Módulo de Documentos')}">
				<div class="col col-sm-12 col-md-6">
					<h2 class="gt-table-head mt-3">Expedientes</h2>
					<div id='left'>
						<jsp:include page="loading.jsp" />
					</div>
					<h2 class="gt-table-head mt-3">Processos Administrativos</h2>
					<div id='leftbottom'>
						<jsp:include page="loading.jsp" />
					</div>
				</div>
			</c:if>
			<c:if
				test="${(f:resource('isWorkflowEnabled') and f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;WF:Módulo de Workflow')) or f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;SR') or f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC:Módulo de Gestão de Conhecimento') or f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TP:Módulo de Transportes')}">
				<div class="col col-sm-12 col-md-6">
					<c:if
						test="${(f:resource('isWorkflowEnabled') and f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;WF:Módulo de Workflow'))}">
						<h2 class="gt-table-head mt-3">Tarefas</h2>
						<span id='right' style="margin: 0; padding: 0;"> <jsp:include
								page="loading.jsp" />
						</span>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;SR')}">
						<h2 class="gt-table-head mt-3">Solicitações</h2>
						<div id='rightbottom'>
							<jsp:include page="loading.jsp" />
						</div>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC:Módulo de Gestão de Conhecimento')}">
						<h2 class="gt-table-head mt-3">Gestão de Conhecimento</h2>
						<div id='rightbottom2'>
							<jsp:include page="loading.jsp" />
						</div>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TP:Módulo de Transportes')}">
						<h2 class="gt-table-head mt-3">Transportes</h2>
						<!-- content box -->
						<div id='rightbottom3'>
							<jsp:include page="loading.jsp" />
						</div>
					</c:if>
				</div>
			</c:if>
		</div>
		<input type="hidden" id="idp" name="idp" value="${idp}">
	</div>
</siga:pagina>
