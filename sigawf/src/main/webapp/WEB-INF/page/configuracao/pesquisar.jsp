<%@ include file="/WEB-INF/page/include.jsp"%><!--  -->

<script type="text/javascript">
	function solicitarInformacao(tarefa) {

		var objSelecionado = document.getElementById('tipoResponsavel_'
				+ tarefa);

		document.getElementById('lotacao_' + tarefa
				+ '_lotacaoSel_id').value = "";
		document.getElementById('matricula_' + tarefa
				+ '_pessoaSel_id').value = "";

		document.getElementById('lotacao_' + tarefa
				+ '_lotacaoSel_sigla').value = "";
		document.getElementById('matricula_' + tarefa
				+ '_pessoaSel_sigla').value = "";

		document.getElementById('lotacao_' + tarefa
				+ '_lotacaoSel_descricao').value = "";
		document.getElementById('matricula_' + tarefa
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
		}

		var div = tipo + '_' + tarefa;
		document.getElementById('lotacao_' + tarefa).style.display = "none";
		document.getElementById('matricula_' + tarefa).style.display = "none";

		if (document.getElementById(div) != null) {
			document.getElementById(div).style.display = "inline"
		}
	}

	function gravarConfiguracao() {
		var frm = document.getElementById("formulario");
		frm.action = '${linkTo[ConfiguracaoController].gravar}';
		frm.submit();
	}

	function pesquisarProcedimento() {
		var frm = document.getElementById("formulario");
		frm.action = '${linkTo[ConfiguracaoController].pesquisar}';
		frm.submit();
	}
</script>

<siga:pagina
	titulo="Configuração de Permissões para Instaciação de Procedimentos">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Configuração de Permissão para Instanciar Procedimento</h2>
			<div class="gt-content-box gt-for-table">
				<form id="formulario" action="" method="POST" cssClass="form">
					<input type="hidden" name="procedimento" value="${procedimento}" />
					<input type="hidden" name="orgao" value="${orgao}" />
					<table class="gt-form-table">
						<tr class="header">
							<td colspan="2">Dados da Configuração</td>
						</tr>
						<tr>
							<td>Órgão:</td>
							<td>${orgao}</td>
						</tr>
						<tr class="header">
							<td>Procedimento:</td>
							<td>${procedimento}</td>
						</tr>
					</table>
					<table class="gt-form-table" style="table-layout: fixed;">
						<c:if test="${not empty listaPermissao}">
							<tr class="header">
								<td width="100%">Permissão para Instanciar Procedimento
									concedida para:</td>
							</tr>

							<!-- permissoes -->

							<c:forEach var="p" items="${listaPermissao}">
								<tr>
									<c:choose>
										<c:when test="${not empty p.lotacao}">
											<td><siga:select id="tipoResponsavel_${p.id}"
													name="tipoResponsavel_${p.id}" list="listaTipoResponsavel"
													theme="simple" listValue="texto" listKey="id"
													value="${p.tipoResponsavel}" 
													onchange="javascript:solicitarInformacao('${p.id}');" />
										</c:when>
										<c:when test="${not empty p.pessoa}">
											<td><siga:select id="tipoResponsavel_${p.id}"
													name="tipoResponsavel_${p.id}" list="listaTipoResponsavel"
													theme="simple" listValue="texto" listKey="id"
													value="${p.tipoResponsavel}"
													onchange="javascript:solicitarInformacao('${p.id}');" />
										</c:when>
									</c:choose>

									<c:choose>
										<c:when test="${p.tipoResponsavel == 1}">
											<!-- MATRÍCULA -->
											<div style="display: none;" id="lotacao_${p.id}">
												<siga:selecao modulo="siga" tipo="lotacao"
													tema="simple" propriedade="lotacao_${p.id}"
													siglaInicial="${p.lotacao.sigla}"
													idInicial="${p.lotacao.id}"
													descricaoInicial="${p.lotacao.descricao}" />
											</div>
											<div style="display: inline;" id="matricula_${p.id}">
												<siga:selecao modulo="siga" tipo="pessoa" tema="simple"
													propriedade="matricula_${p.id}"
													siglaInicial="${p.pessoa.sigla}" idInicial="${p.pessoa.id}"
													descricaoInicial="${p.pessoa.descricao}" />
											</div>
										</c:when>
										<c:when test="${p.tipoResponsavel == 2}">
											<!-- LOTAÇÃO -->
											<div style="display: inline;" id="lotacao_${p.id}">
												<siga:selecao modulo="siga" tipo="lotacao"
													tema="simple" propriedade="lotacao_${p.id}"
													siglaInicial="${p.lotacao.sigla}"
													idInicial="${p.lotacao.id}"
													descricaoInicial="${p.lotacao.descricao}" />
											</div>
											<div style="display: none;" id="matricula_${p.id}">
												<siga:selecao modulo="siga" tipo="pessoa" tema="simple"
													propriedade="matricula_${p.id}"
													siglaInicial="${p.pessoa.sigla}" idInicial="${p.pessoa.id}"
													descricaoInicial="${p.pessoa.descricao}" />
											</div>
										</c:when>
									</c:choose>


									</td>

								</tr>

							</c:forEach>
						</c:if>
						<tr class="header">
							<td>Nova configuração</td>
						</tr>

						<tr>
							<td>
							<%-- <siga:select id="tipoResponsavel_${idNovaPermissao}"
									name="tipoResponsavel_${idNovaPermissao}"
									list="listaTipoResponsavel" theme="simple"
									onchange="javascript:solicitarInformacao('${idNovaPermissao}');" />
							--%> 
								<div style="display: none;" id="lotacao_${idNovaPermissao}">
									<siga:selecao modulo="siga" tipo="lotacao" tema="simple"
										propriedade="lotacao_${idNovaPermissao}" />
								</div>
								<div style="display: none;" id="matricula_${idNovaPermissao}">
									<siga:selecao modulo="siga" tipo="pessoa" tema="simple"
										propriedade="matricula_${idNovaPermissao}" />
								</div>
							</td>
						</tr>
						<tr>
							<td align="right" width="100%"><input type="button"
								value="Gravar" name="gravar"
								onclick="javascript:gravarConfiguracao()"
								class="gt-btn-medium gt-btn-left" /></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>


