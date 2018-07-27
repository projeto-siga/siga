<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<siga:pagina titulo="P&aacute;gina Inicial"
	incluirJs="/siga/javascript/principal.js">

	<div class="container-fluid content">
		<div class="row bg-light pt-2 pb-2">
			<!-- usuário -->
			<div class="col col-sm-6">
				<div class="gt-company">
					<strong>${f:resource('siga.cabecalho.titulo')} <c:catch>
							<c:if test="${not empty titular.orgaoUsuario.descricao}">- ${titular.orgaoUsuario.descricao}</c:if>
						</c:catch>
					</strong>
				</div>
				<!-- 
					<div class="gt-version">
						Sistema Integrado de Gest&atilde;o Administrativa
						<c:if test="${not empty env}"> - <span style="color: red">${env}</span>
						</c:if>
					</div>
					 -->
			</div>
			<c:if test="${not empty cadastrante}">
				<div class="col col-sm-6">
					<div class="text-right">
						<div>
							Olá, <strong><c:catch>
									<c:out default="Convidado"
										value="${f:maiusculasEMinusculas(cadastrante.nomePessoa)}" />
									<c:choose>
										<c:when test="${not empty cadastrante.lotacao}">
						 - ${cadastrante.lotacao.sigla}</c:when>
									</c:choose>
								</c:catch> </strong> <span class="gt-util-separator">|</span> <a
								href="/sigaidp/jwt/logout">sair</a>
						</div>
						<div>
							<c:catch>
								<c:choose>
									<c:when
										test="${not empty titular && titular.idPessoa!=cadastrante.idPessoa}">Substituindo: <strong>${f:maiusculasEMinusculas(titular.nomePessoa)}</strong>
										<span class="gt-util-separator">|</span>
										<a href="/siga/app/substituicao/finalizar">finalizar</a>
									</c:when>
									<c:when
										test="${not empty lotaTitular && lotaTitular.idLotacao!=cadastrante.lotacao.idLotacao}">Substituindo: <strong>${f:maiusculasEMinusculas(lotaTitular.nomeLotacao)}</strong>
										<span class="gt-util-separator">|</span>
										<a href="/siga/app/substituicao/finalizar">finalizar</a>
									</c:when>
									<c:otherwise></c:otherwise>
								</c:choose>
							</c:catch>
						</div>
					</div>
				</div>
			</c:if>
		</div>
	</div>


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
						<div class="card-header">Expedientes</div>
						<div class="card-body">
							<div id='left'>
								<jsp:include page="loading.jsp" />
							</div>
						</div>
					</div>

					<div class="card bg-light mb-3">
						<div class="card-header">Processos Administrativos</div>
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
							<div class="card-header">Tarefas</div>
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
							<div class="card-header">Solicitações</div>
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
							<div class="card-header">Gestão de Conhecimento</div>
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
							<div class="card-header">Transportes</div>
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
