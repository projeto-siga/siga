<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina popup="false" titulo="Administração do SIGA WF">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h1>Administração do SIGA WF</h1>

			<h2>Encerrar Processo</h2>
			<div class="gt-form gt-content-box">
				<ww:form action="endProcessInstance">
					<div class="gt-form-row gt-width-100">
						<div class="gt-left-col gt-width-33">
							<label>ID do Task Instance:</label>
							<ww:textfield name="idTI" cssClass="gt-form-text" theme="simple"/>
						</div>
						<div class="gt-left-col gt-width-33">
							<label>Data de fim do Processo (Opcional):</label>
							<ww:textfield name="dtFim" cssClass="gt-form-text" theme="simple"/>
							<ww:submit label="OK" value="OK" theme="simple" cssClass="gt-btn-medium gt-btn-left" />
						</div>
					</div>
				</ww:form>
			</div>
			
		</div>
	</div>
</siga:pagina>