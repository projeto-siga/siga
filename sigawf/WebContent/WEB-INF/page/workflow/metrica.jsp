<%@ include file="/WEB-INF/page/include.jsp"%>

<script type="text/javascript" language="Javascript1.1">
	<ww:url id="url" action="emitirRelatorio"></ww:url>
	function sbmt() {
		frmRelatorio.action = '<ww:property value="%{url}"/>';
		frmRelatorio.submit();
	}
</script>

<script type="text/javascript">
	function exibirOpcoesExtras(){
		if ($("#selecaoRelatorio").val() == '2' || $("#selecaoRelatorio").val() == '3'){
			$("#opcoesExtras").show();
		}else{
			$("#opcoesExtras").hide();
		}
		if ($("#selecaoRelatorio").val() == '1'){
			$("#opcaoPercentualMediaTruncada").show();
		}else{
			$("#opcaoPercentualMediaTruncada").hide();
		}
		if ($("#selecaoRelatorio").val() == '3'){
			$("#opcaoGrupos").show();
		}else{
			$("#opcaoGrupos").hide();
		}
		
		
	}
</script>

<siga:pagina titulo="Estatística de procedimento">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>${titulo_pagina}</h2>
			<div class="gt-content-box gt-for-table">
				<form name="frmRelatorio" method="get" class="form">
					<table class="gt-form-table">
						<ww:hidden name="procedimento" value="${procedimento}" />
						<ww:hidden name="pdId" value="${pdId}" />
						<ww:label name="orgao" label="Órgão" value="${orgao}" />
						<ww:label name="Procedimento" label="Procedimento"
							value="${procedimento}" />
						<tr>
							<ww:select list="#{'1':'Estatísticas gerais', '2':'Tempo de documentos','3':'Tempo de documentos detalhado'}" label="Relatório"
								name="selecaoRelatorio" onchange="javascript:exibirOpcoesExtras()" >
							</ww:select>
						</tr>
						<tr>
							<td width="25%">Procedimento iniciado de:</td>
							<td><ww:textfield name="dataInicialDe"
									onblur="javascript:verifica_data(this, true);comparaData(dataInicialDe,dataInicialAte);
				comparaData(dataInicialAte,dataFinalDe)"
									theme="simple" size="12" maxlength="10" /> até <ww:textfield
									name="dataInicialAte"
									onblur="javascript:verifica_data(this, true);comparaData(dataInicialDe,dataInicialAte);
				comparaData(dataInicialAte,dataFinalDe)"
									theme="simple" size="12" maxlength="10" /></td>
						</tr>
						<tr>
							<td>Procedimento finalizado de:</td>
							<td><ww:textfield name="dataFinalDe"
									onblur="javascript:verifica_data(this,true);comparaData(dataInicialDe,dataInicialAte);
				comparaData(dataInicialAte,dataFinalDe);"
									theme="simple" size="12" maxlength="10" /> até <ww:textfield
									name="dataFinalAte"
									onblur="javascript:verifica_data(this,true);comparaData(dataInicialDe,dataInicialAte);
				comparaData(dataInicialAte,dataFinalDe);"
									theme="simple" size="12" maxlength="10" /></td>
						</tr>
						<tr id="opcoesExtras" style="display: none" >
							<td>
								<input type="checkbox" id="incluirAbertos" name="incluirAbertos" style="float: left" class="gt-form-checkbox"></input>
								<label>&nbsp;Incluir Procedimentos Abertos</label>
							</td>
						</tr>
						<tr id="opcaoPercentualMediaTruncada"  >
							<td>
								<label>Percentual&nbsp;da&nbsp;Média&nbsp;Truncada (entre ${minMediaTruncada}% e ${maxMediaTruncada}%):</label>
							</td>
							<td>
								<input type="text" id="percentualMediaTruncada" name="percentualMediaTruncada" style="float: left" class="gt-form-text" value="${minMediaTruncada}"></input>
							</td>
						</tr>
						<tr id="opcaoGrupos"  style="display: none"  >
							<td>
								<ww:select id="grpIni" name="grpIni" list="lstGruposIni" listValue="name" listKey="id" headerKey="-1" headerValue="[Escolha uma tarefa]" theme="simple"></ww:select>
								<ww:select id="grpFim" name="grpFim" list="lstGruposFim" listValue="name" listKey="id" headerKey="-1" headerValue="[Escolha uma tarefa]" theme="simple"></ww:select>
							</td>
						</tr>
						
						<tr>
							<td colspan="2"><input type="button"
								onclick="javascript:sbmt()" value="Gerar relatório"
								class="gt-btn-medium gt-btn-left" />
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
