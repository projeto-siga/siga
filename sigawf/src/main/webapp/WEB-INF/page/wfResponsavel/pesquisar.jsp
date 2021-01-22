<%@ include file="/WEB-INF/page/include.jsp"%>

<script type="text/javascript">
	function solicitarInformacao(tarefa) {

		var objSelecionado = document.getElementById('tipoResponsavel_'
				+ tarefa);

		document.getElementById('formulario_lotacao_' + tarefa
				+ '_lotacaoSel_id').value = "";
		document.getElementById('formulario_matricula_' + tarefa
				+ '_pessoaSel_id').value = "";

		document.getElementById('formulario_lotacao_' + tarefa
				+ '_lotacaoSel_sigla').value = "";
		document.getElementById('formulario_matricula_' + tarefa
				+ '_pessoaSel_sigla').value = "";

		document.getElementById('formulario_lotacao_' + tarefa
				+ '_lotacaoSel_descricao').value = "";
		document.getElementById('formulario_matricula_' + tarefa
				+ '_pessoaSel_descricao').value = "";

		document.getElementById('lotacao_' + tarefa
				+ '_lotacaoSelSpan').value = "";
		document.getElementById('matricula_' + tarefa
				+ '_pessoaSelSpan').value = "";

		switch (objSelecionado.selectedIndex) {

		//Não Definido
		case 0:
			break;
		//Matrícula
		case 1:
			var tipo = "matricula";
			break;
		//Lotação	
		case 2:
			var tipo = "lotacao";
			break;
		//Lotação superior	
		case 3:
			break;
		//Superior hierárquico	
		case 4:
			break;
		//Expressão	
		case 5:
			var tipo = "expressao";
			break;
		}

		var div = tipo + '_' + tarefa;
		document.getElementById('lotacao_' + tarefa).style.display = "none";
		document.getElementById('matricula_' + tarefa).style.display = "none";
		document.getElementById('expressao_' + tarefa).style.display = "none";

		if (document.getElementById(div) != null) {
			document.getElementById(div).style.display = "inline"
		}
	}

	function gravarResponsabilidade() {
		var frm = document.getElementById("formulario");
		frm.action = '${linkTo[DesignacaoController].gravar}';
		frm.submit();
	}

	function pesquisarProcedimento() {
		var frm = document.getElementById("formulario");
		var e1 = document.getElementById("orgao");
		var orgao = e1.options[e1.selectedIndex].value;
		var e2 = document.getElementById("procedimento");
		var procedimento = e2.options[e2.selectedIndex].value;
		window.location.href = '${linkTo[DesignacaoController].pesquisar}'.replace(/\/\/$/, '/' + orgao + '/' + procedimento);
	}
</script>

<siga:pagina titulo="Definição de Responsabilidades">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Definição de Responsabilidades</h2>
			<h4>Seleção de Órgão e Procedimento</h4>
			<div class="gt-content-box gt-for-table">
				<form id="formulario" action="" method="POST" class="form">

					<table class="gt-form-table">
						<siga:select label="Órgão" id="orgao" name="orgao" list="listaOrgao"
							listValue="acronimoOrgaoUsu" listKey="acronimoOrgaoUsu"
							multiple="false"/>
						<siga:select label="Procedimento" id="procedimento" name="procedimento"
							list="listaProcedimento" listValue="name" listKey="name"
							multiple="false" />
						<tr>
							<td colspan="2"><input type="button" value="Pesquisar"
								onclick="javascript:pesquisarProcedimento()"
								class="gt-btn-medium gt-btn-left" /></td>
						</tr>
					</table>
			</div>

			<br />
			<h4>Responsabilidades</h4>
			<div class="gt-content-box gt-for-table">
				<table class="gt-form-table" style="table-layout: fixed;">
					<tr class="header">
						<td width="10%">Raia</td>
						<td width="30%">Tarefa (Nome do nó)</td>
						<td width="60%">Responsável</td>
					</tr>

					<!-- raias -->

					<c:forEach var="r" items="${mapaDesignacaoRaia}">

						<c:if test="${not empty r.value[0].id}">
							<c:set var="idRaia" value="${r.value[0].id}" />
						</c:if>
						<c:if test="${not empty r.value[0].tipoResponsavel}">
							<c:set var="tipoResponsavel"
								value="${r.value[0].tipoResponsavel}" />
						</c:if>

						<tr>
							<td>${r.key}</td>

							<td><c:forEach var="d" items="${r.value}">
									<span style="cursor: help;" title="${d.nomeDoNo}">${d.tarefa}</span>
									<br />
								</c:forEach></td>

							<td>
								<siga:select id="tipoResponsavel_${idRaia}"
									name="tipoResponsavel_${idRaia}" list="listaTipoResponsavel"
									theme="simple" listValue="texto" listKey="id"
									value="${tipoResponsavel}"
									onchange="javascript:solicitarInformacao('${idRaia}');" />
								<div
									style="display: ${(tipoResponsavel == 1) ? 'inline' : 'none'};"
									id="matricula_${idRaia}">
									<siga:selecao modulo="siga" tipo="pessoa" tema="simple"
										propriedade="matricula_${idRaia}"
										siglaInicial="${r.value[0].ator.sigla}"
										idInicial="${r.value[0].ator.id}"
										descricaoInicial="${r.value[0].ator.descricao}" />
								</div>
								<div
									style="display: ${(tipoResponsavel == 2) ? 'inline' : 'none'};"
									id="lotacao_${idRaia}">
									<siga:selecao modulo="siga" tipo="lotacao" tema="simple"
										propriedade="lotacao_${idRaia}"
										siglaInicial="${r.value[0].lotaAtor.sigla}"
										idInicial="${r.value[0].lotaAtor.id}"
										descricaoInicial="${r.value[0].lotaAtor.descricao}" />
								</div>
								<div
									style="display: ${(tipoResponsavel == 5) ? 'inline' : 'none'};"
									id="expressao_${idRaia}">
									<input type="text" width="200" name="expressao_${idRaia}"
										value="${r.value[0].expressao}" />
								</div>
							</td>
						</tr>

						<c:set var="tipoResponsavel" value="" />
						<c:set var="idRaia" value="" />
					</c:forEach>

					<!-- tarefas -->
					<c:forEach var="dTarefa" items="${listaDesignacaoTarefa}">
						<tr>
							<td>${dTarefa.raia}</td>
							<td><span style="cursor: help;" title="${dTarefa.nomeDoNo}">${dTarefa.tarefa}</span>
							</td>

							<td>
							<siga:select id="tipoResponsavel_${dTarefa.id}"
									name="tipoResponsavel_${dTarefa.id}"
									list="listaTipoResponsavel" theme="simple" listValue="texto"
									listKey="id" value="${dTarefa.tipoResponsavel}"
									onchange="javascript:solicitarInformacao('${dTarefa.id}');" />
								<div
									style="display: ${(dTarefa.tipoResponsavel == 1) ? 'inline' : 'none'};"
									id="matricula_${dTarefa.id}">
									<siga:selecao modulo="siga" tipo="pessoa" tema="simple"
										propriedade="matricula_${dTarefa.id}"
										siglaInicial="${dTarefa.ator.sigla}"
										idInicial="${dTarefa.ator.id}"
										descricaoInicial="${dTarefa.ator.descricao}" />
								</div>
								<div
									style="display: ${(dTarefa.tipoResponsavel == 2) ? 'inline' : 'none'};"
									id="lotacao_${dTarefa.id}">
									<siga:selecao modulo="siga" tipo="lotacao" tema="simple"
										propriedade="lotacao_${dTarefa.id}"
										siglaInicial="${dTarefa.lotaAtor.sigla}"
										idInicial="${dTarefa.lotaAtor.id}"
										descricaoInicial="${dTarefa.lotaAtor.descricao}" />
								</div>
								<div
									style="display: ${(dTarefa.tipoResponsavel == 5) ? 'inline' : 'none'};"
									id="expressao_${dTarefa.id}">
									<input type="text" name="expressao_${dTarefa.id}"
										value="${dTarefa.expressao}" />
								</div>
								</td>
						</tr>
					</c:forEach>
					<tr>
						<td align="right" width="90%"><input type="button"
							value="Gravar" onclick="javascript:gravarResponsabilidade()"
							class="gt-btn-medium gt-btn-left" /></td>
					</tr>
				</table>
				<form>
			</div>
		</div>
	</div>
</siga:pagina>


