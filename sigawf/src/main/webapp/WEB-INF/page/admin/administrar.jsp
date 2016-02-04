<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina popup="false" titulo="Administração do SIGA WF">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">

			<h1>Administração do SIGA WF</h1>

			<h2>Encerrar Processo</h2>
			<div class="gt-form gt-content-box">
				<form action="endProcessInstance">

					<tr>
						<td><label>ID do Task Instance:</label></td>
						<td><input type="text" name="idTI" class="gt-form-text" /></td>
					</tr>
					
					<tr>
						<td><label>Data de fim do Processo (Opcional, use
								para não distorcer a estatística):</label></td>
						<td><input type="text" name="dtFim"
								onblur="javascript:verifica_data(this, true);comparaData(dataInicial,dataFinal);"
								size="12" maxlength="10" class="gt-form-text" /></td>
					</tr>
					
					<tr>
						<td><input type="submit" value="OK"
								class="gt-btn-medium gt-btn-left" /></td>
					</tr>

				</form>

			</div>
		</div>
</siga:pagina>