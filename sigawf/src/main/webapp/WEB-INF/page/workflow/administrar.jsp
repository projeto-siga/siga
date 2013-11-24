<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina popup="false" titulo="Administração do SIGA WF">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h1>Administração do SIGA WF</h1>

			<h2>Encerrar Processo</h2>
			<div class="gt-form gt-content-box">
				<ww:form action="endProcessInstance">
					<div class="gt-form-row gt-width-100">
						<div class="gt-left-col gt-width-100">
							<label>ID do Task Instance:</label>
							<ww:textfield name="idTI" cssClass="gt-form-text" theme="simple"/>
						</div>
					</div>
					<div class="gt-form-row gt-width-100">
						<div class="gt-left-col gt-width-100">
							<label>Data de fim do Processo (Opcional, use para não distorcer a estatística):</label>
							<ww:textfield name="dtFim" onblur="javascript:verifica_data(this, true);comparaData(dataInicial,dataFinal);" theme="simple" size="12" maxlength="10"  cssClass="gt-form-text"/>
							<ww:submit label="OK" value="OK" theme="simple" cssClass="gt-btn-medium gt-btn-left" />
						</div>
					</div>
				</ww:form>
			</div>
			
		</div>
	</div>
</siga:pagina>