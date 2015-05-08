<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<siga:pagina titulo="P&aacute;gina Inicial" incluirJs="siga/javascript/principal.js">
	<ww:token />

	<div class="gt-bd">
		<!-- main content -->
		<div class="gt-content">
			<div style="width: 100%; display: block;">
				<c:if test="${not empty mensagem}">
					<div id="mensagem" class="gt-success">${mensagem}</div>
					<script>
						setTimeout(function() {
							$('#mensagem').fadeTo(1000, 0, function() {
								$('#mensagem').slideUp(1000);
							});
						}, 5000); // <-- time in milliseconds
					</script>
				</c:if>

				<!-- Expedientes -->
				<div
					style="width: 49%; float: left; clear: both; padding: 0; margin: 0;">
					<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC:Módulo de Documentos')}">
						<!-- Expedientes -->
						<div style="width: 100%; padding: 0; margin: 0;">
							<!-- Files Table -->
							<!-- This table can be used for listing things like files, pictures, documents, etc -->
							<h2 class="gt-table-head">Expedientes</h2>
							<!-- content box -->
							<div id='left'>
	                            <jsp:include page="loading.jsp" />
							</div>
							<!-- Expedientes -->
							<br />
							<!-- Processos -->
							<h2 class="gt-table-head">Processos Administrativos</h2>
							<!-- content box -->
							<div id='leftbottom'>
	                            <jsp:include page="loading.jsp" />
							</div>
							<!-- Processos -->
						</div>
					</c:if>
				</div>
				<div style="width: 49%; float: right; padding: 0; margin: 0;">
					<div style="width: 100%; padding: 0; margin: 0;">
						<c:if
							test="${f:resource('isWorkflowEnabled') and f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;WF:Módulo de Gestão de Conhecimento')}">
							<h2 class="gt-table-head">Tarefas</h2>
							<!-- content box -->
							<span id='right' style="margin: 0; padding: 0;">
                                <jsp:include page="loading.jsp" />
							</span>
							<!--
							<div class="gt-table-buttons">
								<a href="" class="gt-btn-green-large gt-btn-right">Iniciar
									Procedimento</a>
							 -->
							<!-- segunda metade da tela -->
						</c:if>
						<br />
						<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;SR')}">
                            <h2 class="gt-table-head">Solicitações</h2>
                            <!-- content box -->
                            <span id='rightbottom' style="margin:0;padding:0;">
                                 <jsp:include page="loading.jsp" />
                            </span>
                            </c:if>
                            <br />
						<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC:Módulo de Gestão de Conhecimento')}">
							<h2 class="gt-table-head">Gestão de Conhecimento</h2>
							<!-- content box -->
							<div id='rightbottom2'>
                                <jsp:include page="loading.jsp" />
							</div>
						</c:if>
						<br />
						<c:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;TP:Módulo de Transportes')}">
							<h2 class="gt-table-head">Transportes</h2>
							<!-- content box -->
							<div id='rightbottom3'>
                                <jsp:include page="loading.jsp" />
							</div>
							<!-- script type="text/javascript">
								SetInnerHTMLFromAjaxResponse(
								"/sigatp/gadget",
								document.getElementById('rightbottom3'));
							</script> -->
						</c:if>
						<br />
					</div>
				</div>
			</div>

			<input type="hidden" name="webwork.token.name" value="webwork.token">
            <input type="hidden" name="webwork.token" value="4V5NHN4JMVPK6WUKD6O5BS3S64ADKB9D">
            <input type="hidden" id="idp" name="idp" value="${idp}">
		</div>
	</div>
</siga:pagina>
