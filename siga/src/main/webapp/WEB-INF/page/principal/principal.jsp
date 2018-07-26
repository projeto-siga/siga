<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<siga:pagina titulo="P&aacute;gina Inicial"
	incluirJs="/siga/javascript/principal.js">

	<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
		<a class="navbar-brand pt-0 pb-0" href="#"> <img
			src="/siga/imagens/logo.png">
		</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#navbarSupportedContent"
			aria-controls="navbarSupportedContent" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav mr-auto">
				<li class="nav-item active"><a class="nav-link" href="#">Home
						<span class="sr-only">(current)</span>
				</a></li>
				<li class="nav-item"><a class="nav-link" href="#">Link</a></li>
				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
					role="button" data-toggle="dropdown" aria-haspopup="true"
					aria-expanded="false"> Dropdown </a>
					<div class="dropdown-menu" aria-labelledby="navbarDropdown">
						<a class="dropdown-item" href="#">Action</a> <a
							class="dropdown-item" href="#">Another action</a>
						<div class="dropdown-divider"></div>
						<a class="dropdown-item" href="#">Something else here</a>
					</div></li>
				<li class="nav-item"><a class="nav-link disabled" href="#">Disabled</a>
				</li>
			</ul>
			<form class="form-inline my-2 my-lg-0">
				<input class="form-control mr-sm-2" type="search">
				<button class="btn btn-outline-light my-2 my-sm-0" type="submit">Buscar</button>
			</form>
		</div>
	</nav>

	<div class="container-fluid content">
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
		</c:if>
		<div class="row mt-4">
			<c:if
				test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC:Módulo de Documentos')}">
				<div class="col col-sm-12 col-md-6"> 
					<div class="card bg-light mb-3">
						<div class="card-header">
							<h5>Expedientes</h5>
						</div>
						<div class="card-body">
							<div id='left'>
								<jsp:include page="loading.jsp" />
							</div>
						</div>
					</div>

					<div class="card bg-light mb-3">
						<div class="card-header">
							<h5>Processos Administrativos</h5>
						</div>
						<div class="card-body">
							<div id='leftbottom'>
								<jsp:include page="loading.jsp" />
							</div>
						</div>
					</div>
				</div>
			</c:if>
			<c:if
				test="${(f:resource('isWorkflowEnabled') and f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;WF:Módulo de Workflow')) or f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;SR') or f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC:Módulo de Gestão de Conhecimento') or f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TP:Módulo de Transportes')}">
				<div class="col col-sm-12 col-md-6">
					<c:if
						test="${(f:resource('isWorkflowEnabled') and f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;WF:Módulo de Workflow'))}">
						<div class="card bg-light mb-3">
							<div class="card-header">
								<h5>Tarefas</h5>
							</div>
							<div class="card-body">
								<div id='right'>
									<jsp:include page="loading.jsp" />
								</div>
							</div>
						</div>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;SR')}">
						<div class="card bg-light mb-3">
							<div class="card-header">
								<h5>Solicitações</h5>
							</div>
							<div class="card-body">
								<div id='rightbottom'>
									<jsp:include page="loading.jsp" />
								</div>
							</div>
						</div>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC:Módulo de Gestão de Conhecimento')}">
						<div class="card bg-light mb-3">
							<div class="card-header">
								<h5>Gestão de Conhecimento</h5>
							</div>
							<div class="card-body">
								<div id='rightbottom2'>
									<jsp:include page="loading.jsp" />
								</div>
							</div>
						</div>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TP:Módulo de Transportes')}">
						<div class="card bg-light mb-3">
							<div class="card-header">
								<h5>Transportes</h5>
							</div>
							<div class="card-body">
								<div id='rightbottom3'>
									<jsp:include page="loading.jsp" />
								</div>
							</div>
						</div>
					</c:if>
				</div>
			</c:if>
		</div>
		<input type="hidden" id="idp" name="idp" value="${idp}">
	</div>
</siga:pagina>
