<link rel="stylesheet" type="text/css" href="/sigasr/stylesheets/jquery.multiselect.css">
<script src="//cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script src="//datatables.net/release-datatables/extensions/ColVis/js/dataTables.colVis.min.js"></script>
<script src="/sigasr/javascripts/detalhe-tabela.js"></script>
<script src="/sigasr/javascripts/tabela-dinamica.js"></script>
<script src="/sigasr/javascripts/jquery.multiselect.min.js"></script>
<script src="/sigasr/javascripts/jquery.cookie.js"></script>
<script src="/sigasr/javascripts/HTMLSelectElement.prototype.selectedOptions.js"></script>

<%@ attribute name="solicitacaoListaVO" required="false"%>
<%@ attribute name="filtro" required="false"%>
<%@ attribute name="modoExibicao" required="false"%>

<style>
	.bt-expandir {
		background: none !important;
		cursor: pointer;
	}

	.bt-expandir.expandido {
		background: none !important;
	}

	.hide-sort-arrow.sorting_asc, .hide-sort-arrow.sorting_desc {
		background: none !important;
	}

	td.details-control {
		background: none !important;
		cursor: pointer;
	}

	tr.shown td.details-control {
		background: none !important;
	}	
</style>

<div class="gt-content-box gt-for-table dataTables_div">
	<div class="siga-multiple-select">
		<select id="selectColunasTabela" name="colunasTabela" multiple="multiple"/></select>
		<c:if test="${modoExibicao == 'solicitacao'}">
		    <select id="selectColunasDetalhamento" name="colunasDetalhamento" multiple="multiple"></select>
	    </c:if>
	</div>
	<div>
	</div>
	<table id="solicitacoes_table" border="0" class="gt-table-nowrap display" data-json='${requestScope[solicitacaoListaVO].toJson()}'>
		<thead></thead>
		<tbody id="sortable" class="ui-sortable"></tbody>
	</table>
</div>

<script type="text/javascript">
	var tabelaDinamica = new TabelaDinamica('#solicitacoes_table', '${modoExibicao}').criar();

	if (tabelaDinamica && tabelaDinamica.objetoTabela && tabelaDinamica.objetoTabela.podePriorizar) {
		$( "#sortable" ).sortable({placeholder: "ui-state-highlight"});
		$( "#sortable" ).disableSelection();
		$( "#sortable" ).sortable({update: function( event, ui ) {recalcularPosicao()}});
	}

	$(document).ready(function() {
		/* Table initialization */
		tabelaDinamica.table = new SigaTable('#solicitacoes_table')
			.configurar("aoColumns", tabelaDinamica.objetoTabela.estrutura)
			.configurar("data", tabelaDinamica.objetoTabela.dados)
			.configurar("iDisplayLength", 25)
			.configurar("bSort", tabelaDinamica.objetoTabela.podeOrdenar)
			.configurar("bFilter", tabelaDinamica.objetoTabela.podeFiltrar)
			.configurar("bPaginate", tabelaDinamica.objetoTabela.podePaginar)
			.configurar("fnRowCallback", solicitacoesRowCallback)
			.criar()
			.detalhes(formatarDetalhes);

		var selectColunasTabela = $("#selectColunasTabela").multiselect({
			header: false,
			//noneSelectedText: "Selecione",
			selectedText: 'Exibir/Ocultar Colunas',
			close: function(event) {
				tabelaDinamica.alterarColunasTabela(event.target.options, event.target.selectedOptions);
			}
		});

		tabelaDinamica.atualizarColunasSelecionadas(selectColunasTabela, tabelaDinamica.objetoTabela.colunasTabelaJson, tabelaDinamica.TIPO_COLUNA_TABELA);

		if("${modoExibicao}" == "solicitacao") {

			var selectColunasDetalhamento = $("#selectColunasDetalhamento").multiselect({
				header: false,
				//noneSelectedText: "Selecione",
				selectedText: 'Detalhamento',
				close: function(event) {
					tabelaDinamica.alterarColunasDetalhamento(event.target.selectedOptions);
				}
			});

			tabelaDinamica.atualizarColunasSelecionadas(selectColunasDetalhamento, tabelaDinamica.objetoTabela.colunasDetalhamentoJson, tabelaDinamica.TIPO_COLUNA_DETALHE);
		}
	});

	function formatarDetalhes(data) {
		return tabelaDinamica.formatarDetalhes(data, tabelaDinamica.objetoTabela);
	}

	function solicitacoesRowCallback( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
		if (aData && aData.prioridadeSolicitacaoVO)
			$(nRow).addClass('PRIORIDADE-' + aData.prioridadeSolicitacaoVO.prioridade);

		$(nRow).attr('id', aData.idSolicitacao);
		$(nRow).attr('data-json', JSON.stringify(aData));
		$(nRow).attr('data-json-id', aData.idSolicitacao);
	}
</script>
