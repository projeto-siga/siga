<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<siga:pagina titulo="P&aacute;gina Inicial" incluirJs="/siga/javascript/principal.js">

	<div class="gt-bd">
		<div class="gt-content">
			<div style="width: 100%; display: block;">
				<c:if test="${not empty mensagem}">
					<div id="mensagem" class="gt-success">
						${mensagem}
					</div>
					<script>
						setTimeout(function() {
							$('#mensagem').fadeTo(1000, 0, function() {
								$('#mensagem').slideUp(1000);
							});
						}, 5000);
					</script>
				</c:if>
				<div
					style="width: 49%; float: left; clear: both; padding: 0; margin: 0;">
					<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC:Módulo de Documentos')}">
						<div style="width: 100%; padding: 0; margin: 0;">
							<h2 class="gt-table-head">
								Expedientes
							</h2>
							<div id='left'>
	                            <jsp:include page="loading.jsp" />
							</div>
							<br />
							<h2 class="gt-table-head">
								Processos Administrativos
							</h2>
							<div id='leftbottom'>
	                            <jsp:include page="loading.jsp" />
							</div>
						</div>
					</c:if>
				</div>
				<div style="width: 49%; float: right; padding: 0; margin: 0;">
					<div style="width: 100%; padding: 0; margin: 0;">
						<c:if
							test="${f:resource('isWorkflowEnabled') and f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;WF:Módulo de Gestão de Conhecimento')}">
							<h2 class="gt-table-head">
								Tarefas
							</h2>
							<span id='right' style="margin: 0; padding: 0;">
                                <jsp:include page="loading.jsp" />
							</span>
						</c:if>
						<br />
						<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;SR')}">
                            <h2 class="gt-table-head">
                            	Solicitações
                            </h2>
                            <span id='rightbottom' style="margin:0;padding:0;">
                                 <jsp:include page="loading.jsp" />
                            </span>
                            </c:if>
                            <br />
						<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC:Módulo de Gestão de Conhecimento')}">
							<h2 class="gt-table-head">
								Gestão de Conhecimento
							</h2>
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
            <input type="hidden" id="idp" name="idp" value="${idp}">
		</div>
	</div>
</siga:pagina>
