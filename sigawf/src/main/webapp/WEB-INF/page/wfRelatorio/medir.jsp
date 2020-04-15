<%@ include file="/WEB-INF/page/include.jsp"%>

<script type="text/javascript">
	function exibirOpcoesExtras() {
		if ($("#selecaoRelatorio").val() == '2'
				|| $("#selecaoRelatorio").val() == '3') {
			$("#opcoesExtras").show();
		} else {
			$("#opcoesExtras").hide();
		}
		if ($("#selecaoRelatorio").val() == '1') {
			$("#opcaoPercentualMediaTruncada").show();
		} else {
			$("#opcaoPercentualMediaTruncada").hide();
		}
		if ($("#selecaoRelatorio").val() == '3') {
			$("#opcaoGrupos").show();
		} else {
			$("#opcaoGrupos").hide();
		}

	}
</script>

<siga:pagina titulo="Estatística de procedimento">
	<div class="container-fluid content">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>${titulo_pagina}</h5>
			</div>
			<div class="card-body">
				<div class="gt-content-box gt-for-table">
					<form name="frmRelatorio" method="get" class="form" action="${linkTo[WfRelatorioController].relatorio}">
						<input type="hidden" name="pdId" value="${pdId}" /> <input
							type="hidden" name="orgao" value="${orgao}" /> <input
							type="hidden" name="procedimento" value="${procedimento.nome}" />
						<table class="gt-form-table">
							<tr>
								<td>Órgão</td>
								<td>${orgao}</td>
							</tr>
							<tr>
								<td>Procedimento</td>
								<td>${procedimento.nome}</td>
							</tr>
							<tr>
								<siga:select list="listaTipoRelatorio" listKey="id"
									listValue="descr" label="Relatório" name="selecaoRelatorio"
									id="selecaoRelatorio"
									onchange="javascript:exibirOpcoesExtras()" />
							</tr>
							<tr>
								<td width="25%">Procedimento iniciado de:</td>
								<td><input type="text" name="dataInicialDe"
									onblur="javascript:verifica_data(this, true);comparaData(dataInicialDe,dataInicialAte);
				comparaData(dataInicialAte,dataFinalDe)"
									size="12" maxlength="10" /> até <input type="text"
									name="dataInicialAte"
									onblur="javascript:verifica_data(this, true);comparaData(dataInicialDe,dataInicialAte);
				comparaData(dataInicialAte,dataFinalDe)"
									size="12" maxlength="10" /></td>
							</tr>
							<tr>
								<td>Procedimento finalizado de:</td>
								<td><input type="text" name="dataFinalDe"
									onblur="javascript:verifica_data(this,true);comparaData(dataInicialDe,dataInicialAte);
				comparaData(dataInicialAte,dataFinalDe);"
									size="12" maxlength="10" /> até <input type="text"
									name="dataFinalAte"
									onblur="javascript:verifica_data(this,true);comparaData(dataInicialDe,dataInicialAte);
				comparaData(dataInicialAte,dataFinalDe);"
									size="12" maxlength="10" /></td>
							</tr>
							<tr id="opcoesExtras" style="display: none">
								<td><input type="checkbox" id="incluirAbertos"
									name="incluirAbertos" style="float: left"
									class="gt-form-checkbox"></input> <label>&nbsp;Incluir
										Procedimentos Abertos</label></td>
							</tr>
							<tr id="opcaoPercentualMediaTruncada">
								<td><label>Percentual&nbsp;da&nbsp;Média&nbsp;Truncada
										(entre ${minMediaTruncada}% e ${maxMediaTruncada}%):</label></td>
								<td><input type="text" id="percentualMediaTruncada"
									name="percentualMediaTruncada" style="float: left"
									class="gt-form-text" value="${minMediaTruncada}"></input></td>
							</tr>
							<tr id="opcaoGrupos" style="display: none">
								<td>Agrupar tarefas (opcional)</td>
								<td><label>Tarefa inicial:</label> <siga:select id="grpIni"
										name="grpIni" list="lstGruposIni" listValue="nome"
										listKey="id" headerKey="-1" headerValue="[Escolha uma tarefa]"
										theme="simple" /> <label>Tarefa final:</label> <siga:select
										id="grpFim" name="grpFim" list="lstGruposFim" listValue="nome"
										listKey="id" headerKey="-1" headerValue="[Escolha uma tarefa]"
										theme="simple" /></td>
							</tr>
						</table>
						<div class="row mt-2">
							<div class="col-sm-8">
								<button type="button" onclick="javascript:frmRelatorio.submit()"
									class="btn btn-primary">Gerar</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</siga:pagina>
