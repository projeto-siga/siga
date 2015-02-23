<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<siga:pagina titulo="P&aacute;gina Inicial" incluirJs="principal.js">

	<c:if test="${not empty mensagem}">
		<div id="mensagem" class="gt-success">${mensagem}</div>
		<script>
			setTimeout(function() {
				$('#mensagem').fadeTo(1000, 0, function() {
					$('#mensagem').slideUp(1000);
				});
			}, 5000);
		</script>
	</c:if>

	<div class="row">
		<!-- main content -->

		<!-- Expedientes -->
		<div class="col-xs-12 col-sm-6 col-lg-4 gadget">
			<!-- Files Table -->
			<!-- This table can be used for listing things like files, pictures, documents, etc -->
			<h3>Expedientes</h3>
			<!-- content box -->
			<div id='left'>
				<jsp:include page="loading.jsp" />
			</div>
			<!-- Expedientes -->
			<!-- Processos -->
			<h3>Processos Administrativos</h3>
			<!-- content box -->
			<div id='leftbottom'>
				<jsp:include page="loading.jsp" />
			</div>
			<!-- Processos -->
		</div>
		<div class="col-xs-12 col-sm-6 col-lg-8">
			<div class="row">
				<div class="col-xs-12 col-sm-12 col-lg-6 gadget">
					<c:if
						test="${f:resource('isWorkflowEnabled') and f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;WF:Módulo de Gestão de Conhecimento')}">
						<h3>Tarefas</h3>
						<!-- content box -->
						<span id='right' style="margin: 0; padding: 0;"> <jsp:include
								page="loading.jsp" />
						</span>
						<!-- 
									<div class="gt-table-buttons">
										<a href="" class="gt-btn-green-large gt-btn-right">Iniciar
											Procedimento</a>
									 -->
						<!-- segunda metade da tela -->
					</c:if>
				</div>
				<div class="col-xs-12 col-sm-12 col-lg-6 gadget">
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;SR')}">
						<h3>Solicitações</h3>
						<!-- content box -->
						<span id='rightbottom' style="margin: 0; padding: 0;"> <jsp:include
								page="loading.jsp" />
						</span>
					</c:if>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC:Módulo de Gestão de Conhecimento')}">
						<h3>Gestão de Conhecimento</h3>
						<!-- content box -->
						<div id='rightbottom2'>
							<jsp:include page="loading.jsp" />
						</div>
					</c:if>
				</div>
 				<input type="hidden" id="idp" name="idp" value="${idp}">
			</div>
		</div>
</siga:pagina>