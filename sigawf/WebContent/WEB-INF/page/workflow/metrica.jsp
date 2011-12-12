<%@ include file="/WEB-INF/page/include.jsp"%>

<script type="text/javascript" language="Javascript1.1">

<ww:url id="url" action="emitirRelatorio"></ww:url>
function sbmt() {
	frmRelatorio.action='<ww:property value="%{url}"/>';
	frmRelatorio.submit();	
}
</script>

<siga:pagina>
	<c:set var="titulo_pagina" scope="request">Estatística de procedimento</c:set>

	<h1>${titulo_pagina}</h1>
	<form name="frmRelatorio" method="get">
	<table class="list" width="100%">
		<ww:hidden name="secaoUsuario" value="${secaoUsuario}" />
		<ww:hidden name="procedimento" value="${procedimento}" />
		<ww:label name="orgao" label="Órgão" value="${orgao}" />
		<ww:label name="Procedimento" label="Procedimento"
			value="${procedimento}" />
		<tr>
			<ww:select list="relatoriosMetricas" label="Relatório" name="selecaoRelatorio">
			</ww:select>
		</tr>
		<tr>
			<td width="20%">Procedimento iniciado de:</td>
			<td><ww:textfield name="dataInicialDe"
				onblur="javascript:verifica_data(this, true);comparaData(dataInicialDe,dataInicialAte);
				comparaData(dataInicialAte,dataFinalDe)"
				theme="simple" size="12" maxlength="10" /> até 
				<ww:textfield name="dataInicialAte"
				onblur="javascript:verifica_data(this, true);comparaData(dataInicialDe,dataInicialAte);
				comparaData(dataInicialAte,dataFinalDe)"
				theme="simple" size="12" maxlength="10" />
			</td>
		</tr>
		<tr>
			<td width="20%">Procedimento finalizado de:</td>
			<td><ww:textfield name="dataFinalDe"
				onblur="javascript:verifica_data(this,true);comparaData(dataInicialDe,dataInicialAte);
				comparaData(dataInicialAte,dataFinalDe);"
				theme="simple" size="12" maxlength="10" /> até 
				<ww:textfield name="dataFinalAte"
				onblur="javascript:verifica_data(this,true);comparaData(dataInicialDe,dataInicialAte);
				comparaData(dataInicialAte,dataFinalDe);"
				theme="simple" size="12" maxlength="10" />
				</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="button" onclick="javascript:sbmt()"
				value="Gerar relatório" /></td>
		</tr>
	</table>


	</form>
</siga:pagina>
