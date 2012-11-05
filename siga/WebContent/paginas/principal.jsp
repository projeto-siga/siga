<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<siga:pagina titulo="P&aacute;gina Inicial">
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
					style="width: 49%; float: left; clear: both; padding:0; margin:0;">
					<!-- Expedientes -->
					<div style="width: 100%; padding:0; margin:0;">
						<!-- Files Table -->
						<!-- This table can be used for listing things like files, pictures, documents, etc -->
						<h2 class="gt-table-head">Quadro de Expedientes</h2>
						<!-- content box -->
						<div id='left'></div>
						<script type="text/javascript">
							SetInnerHTMLFromAjaxResponse(
									"/sigaex/expediente/doc/gadget.action?apenasQuadro=true&ts=${currentTimeMillis}&idTpFormaDoc=1",
									document.getElementById('left'));
						</script>
						<!-- Expedientes -->
						<br/>
						<!-- Processos -->
						<h2 class="gt-table-head">Quadro de Processos Administrativos</h2>
						<!-- content box -->
						<div id='leftbottom'></div>

						<script type="text/javascript">
							SetInnerHTMLFromAjaxResponse(
									"/sigaex/expediente/doc/gadget.action?ts=${currentTimeMillis}&idTpFormaDoc=2",
									document.getElementById('leftbottom'));
						</script>
						<!-- Processos -->
					</div>
				</div>
				<div
					style="width: 49%; float: right; padding:0; margin:0;">
					<div style="width: 100%; padding:0; margin:0;">
						<c:if
							test="${f:resource('isWorkflowEnabled') and f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF')}">
							<h2 class="gt-table-head">Quadro de Tarefas</h2>
							<!-- content box -->
							<span id='right' style="margin:0;padding:0;"></span>
							<script type="text/javascript">
								SetInnerHTMLFromAjaxResponse(
										"/sigawf/inbox.action?ts=${currentTimeMillis}",
										document.getElementById('right'));
							</script>
							<!-- 
							<div class="gt-table-buttons">
								<a href="" class="gt-btn-green-large gt-btn-right">Iniciar
									Procedimento</a>
							 -->
							<!-- segunda metade da tela -->
						</c:if>
						<h2 class="gt-table-head">Quadro de Solicitações</h2>
						<!-- content box -->
						<span id='rightbottom' style="margin:0;padding:0;"></span>
						<script type="text/javascript">
							SetInnerHTMLFromAjaxResponse(
									"/siga/proxy.action?URL=http://localhost:9000/gadget",
									document.getElementById('rightbottom'));
						</script>
						<!-- Expedientes -->
						<br/>
					</div>
				</div>
			</div>




			<input type="hidden" name="webwork.token.name" value="webwork.token"><input
				type="hidden" name="webwork.token"
				value="4V5NHN4JMVPK6WUKD6O5BS3S64ADKB9D">
			<%--
			<!-- dois quadros do siga-doc -->
			<div style="display: block; overflow: auto;">
				<!-- Expedientes -->
				<div style="width: 49%; float: left; clear: both;">
					<div style="width: 100%; float: left; clear: both;">

						<!-- Files Table -->
						<!-- This table can be used for listing things like files, pictures, documents, etc -->
						<h2 class="gt-table-head">Quadro de Expedientes</h2>
						<!-- content box -->
						<span id='left'></span>

						<script type="text/javascript">
							SetInnerHTMLFromAjaxResponse(
									"/sigaex/expediente/doc/gadget.action?apenasQuadro=true&ts=${currentTimeMillis}&idTpFormaDoc=1",
									document.getElementById('left'));
						</script>
						<!-- Expedientes -->
						<br>
						<!-- Processos -->
						<h2 class="gt-table-head">Quadro de Processos Administrativos</h2>
						<!-- content box -->
						<span id='leftbottom'></span>

						<script type="text/javascript">
							SetInnerHTMLFromAjaxResponse(
									"/sigaex/expediente/doc/gadget.action?ts=${currentTimeMillis}&idTpFormaDoc=2",
									document.getElementById('leftbottom'));
						</script>
						<!-- Processos -->
					</div>
				</div>
				<c:if
					test="${f:resource('isWorkflowEnabled') and f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF')}">
					<div style="width: 49%; float: right;">
						<h2 class="gt-table-head">Quadro de Tarefas</h2>
						<!-- content box -->
						<span id='right'></span>
						<script type="text/javascript">
							SetInnerHTMLFromAjaxResponse(
									"/sigawf/inbox.action?ts=${currentTimeMillis}",
									document.getElementById('right'));
						</script>
						<!-- 
							<div class="gt-table-buttons">
								<a href="" class="gt-btn-green-large gt-btn-right">Iniciar
									Procedimento</a>
							</div>
							 -->
						<!-- segunda metade da tela -->
				</c:if>
				<!-- Dois quadros do siga-doc -->
			</div>
			 --%>

			<!-- / main content -->
		</div>
		<!-- / body -->
		<!-- footer -->
		<!--
      <div class="gt-footer">
        <div class="gt-footer-inner">
          <p>
            RENATO DO AMARAL CRIVANO MACHADO - Seção de Sistemas Especializados
          </p>
        </div>
      </div> -->
		<!-- /footer -->
	</div>
</siga:pagina>
