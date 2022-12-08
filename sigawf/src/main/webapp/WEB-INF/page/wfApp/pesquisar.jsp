<%@ include file="/WEB-INF/page/include.jsp"%>
<siga:pagina titulo="${pi.sigla}"
	incluirJs="/siga/javascript/svg-pan-zoom/svg-pan-zoom.min.js">

	<div class="container-fluid content" id="page">

		<div class="row">
			<div class="col">
				<h2 class="">Pesquisar Procedimentos</h2>
			</div>
		</div>

		<div class="card bg-light pt-3">
			<form id="frm" name="frm" action="pesquisar-procedimentos"
				method="get" class="form100 row pl-3 pr-3">
				<input type="hidden" name="offset" id="offset" value="0" />

				<div id="divUltMovLotaResp" class="form-group col col-9">
					<label>Defini&ccedil;&atilde;o de Procedimento</label>
					<siga:select name="idDefinicaoDeProcedimento"
						list="definicoesDeProcedimentos" listKey="id" listValue="descricaoExterna"
						theme="simple" headerValue="[Todas]" headerKey="0"
						value="${idDefinicaoDeProcedimento}" />
				</div>
				<div class="form-group col col-3">
					<label for="ativos">Status</label> <select class="form-control"
						id="ativos" name="ativos">
						<option value="" ${ativos == null ? 'selected' : ''}>[Todos]</option>
						<option value="true" ${ativos == true ? 'selected' : ''}>Ativos</option>
						<option value="false" ${ativos == false ? 'selected' : ''}>Inativos</option>
					</select>
				</div>
				<div class="form-group col col-12">
					<input type="submit" id="btnBuscar" type="button" value="Buscar"
						class="btn btn-primary" />
				</div>
			</form>
		</div>

		<h3 class="mt-3">Procedimentos</h3>
		<table class="table table-sm table-striped">
			<thead class="thead-dark">
				<tr>
					<th colspan="5" style="text-align: center">Procedimento</th>
					<th colspan="3" style="text-align: center" class="bg-primary">Tarefa</th>
				</tr>
				<tr>
					<th>Código</th>
					<th>Principal</th>
					<th style="text-align: center">Prioridade</th>
					<th style="text-align: right">Duração</th>
					<th style="text-align: right">Status</th>
					<th class="bg-primary">Título</th>
					<th style="text-align: center" class="bg-primary">Atendente</th>
					<th style="text-align: right" class="bg-primary">Duração</th>
				</tr>
			</thead>
			<tbody>
				<siga:paginador var="pi" maxItens="${itemPagina}" maxIndices="10"
					totalItens="${tamanho}" itens="${itens}">
					<tr class="count">
						<td><a href="${linkTo[WfAppController].procedimento(pi.id)}">${pi.sigla}</a></td>
						<td><c:choose>
								<c:when
									test="${pi.tipoDePrincipal eq 'DOCUMENTO' and not empty pi.principal}">
									<a
										href="/sigaex/app/expediente/doc/exibir?sigla=${pi.principal}">${pi.principal}</a>
								</c:when>
								<c:otherwise>${pi.principal}</c:otherwise>
							</c:choose></td>
						<td align="center">${pi.prioridade.descr}</td>
						<td align="right"
							title="Data de início: ${f:formatarDDMMYY(pi.hisDtIni)}">${pi.duracaoDoProcedimento}</td>
						<td align="right">${pi.statusDescr}</td>

						<td>${pi.definicaoDeTarefaCorrente.nome}</td>

						<td align="center"><siga:selecionado sigla="${pi.atendente}"
								pessoaParam="${pi.eventoPessoa.sigla}"
								lotacaoParam="${pi.eventoLotacao.sigla}" /></td>
						<td align="right"
							title="Data de início: ${f:formatarDDMMYY(pi.dtInicioDaTarefa)}">${pi.duracaoDaTarefa}</td>
					</tr>
				</siga:paginador>
			</tbody>
		</table>
	</div>

	<script>
		function sbmt(page) {
			var offset = document.getElementById("offset");
			offset.value = page;
			var frm = document.getElementById("frm");
			frm.submit();
		}
	</script>
</siga:pagina>