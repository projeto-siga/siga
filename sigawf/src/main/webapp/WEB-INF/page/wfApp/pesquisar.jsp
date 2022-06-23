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
						list="definicoesDeProcedimentos" listKey="id" listValue="nome"
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
					<th>Procedimento</th>
					<th>Tarefa</th>
					<th>Principal</th>
					<th style="text-align: center">Atendente</th>
					<th style="text-align: center">Prioridade</th>
					<th style="text-align: right">In&iacute;cio da Tarefa</th>
					<th style="text-align: right">In&iacute;cio do Procedimento</th>
					<th style="text-align: right">Status</th>
				</tr>
			</thead>
			<tbody>
				<siga:paginador var="pi" maxItens="${itemPagina}" maxIndices="10"
					totalItens="${tamanho}" itens="${itens}">
					<tr class="count">
						<td><a href="${linkTo[WfAppController].procedimento(pi.id)}">${pi.sigla}</a></td>
						<td>${pi.definicaoDeTarefaCorrente.nome}</td>
						<td><c:choose>
								<c:when
									test="${pi.tipoDePrincipal eq 'DOCUMENTO' and not empty pi.principal}">
									<a
										href="/sigaex/app/expediente/doc/exibir?sigla=${pi.principal}">${pi.principal}</a>
								</c:when>
								<c:otherwise>${pi.principal}</c:otherwise>
							</c:choose></td>

						<!-- <td>${pi.definicaoDeProcedimento.nome}</td> -->
						<td align="center"><c:set var="atendente">${pi.atendente}</c:set>
							<c:set var="atendenteNome">${pooledActorsDescricao[atendente]}</c:set>
							<c:if
								test="${atendente != titular.sigla and atendente != lotaTitular.sigla}">
								<span style="color: silver">
							</c:if> <siga:selecionado sigla="${atendente}"
								descricao="${atendenteNome}" /> <c:if
								test="${atendente != titular.sigla and atendente != lotaTitular.sigla}">
								</span>
							</c:if></td>
						<td align="center">${pi.prioridade.descr}</td>
						<td align="right" title="${f:formatarDDMMYY(pi.dtInicioDaTarefa)}">${f:esperaSimples(pi.dtInicioDaTarefa)}</td>
						<td align="right" title="${f:formatarDDMMYY(pi.hisDtIni)}">${f:esperaSimples(pi.hisDtIni)}</td>
						<td align="right" >${pi.statusDescr}</td>
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