<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet" type="text/css" href="/sigasr/stylesheets/jquery.multiselect.css">
<script src="/sigasr/javascripts/jquery.dataTables.min.js"></script>
<script src="/sigasr/javascripts/dataTables.colVis.min.js"></script>
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

<div class="card mb-2">
	<div class="card-body">
		<div class="siga-multiple-select">
			<select id="selectColunasTabela" name="colunasTabela" multiple="multiple"/></select>
			<c:if test="${modoExibicao == 'solicitacao'}">
			    <select id="selectColunasDetalhamento" name="colunasDetalhamento" multiple="multiple"></select>
		    </c:if>
		</div>	
		<c:set var="jsonEscaped"><c:out value="${requestScope[solicitacaoListaVO].toJson()}" escapeXml='true'/></c:set>
		<div class="table-responsive">
			<table id="solicitacoes_table" border="0" class="table" data-json="${jsonEscaped}">
				<thead></thead>
				<tbody id="sortable" class="ui-sortable"></tbody>
			</table>
		</div>
	</div>
	<div class="card-footer">
		<div class="legenda-prioridade">
			<div class="PRIORIDADE-IMEDIATO">
				<span class="cor"></span>
				<span class="descricao">Imediata</span>
			</div>
			
			<div class="PRIORIDADE-ALTO">
				<span class="cor"></span>
				<span class="descricao">Alta</span>
			</div>
			
			<div class=PRIORIDADE-MEDIO>
				<span class="cor"></span>
				<span class="descricao">M&eacute;dia</span>
			</div>
			
			<div class="PRIORIDADE-BAIXO">
				<span class="cor"></span>
				<span class="descricao">Baixa</span>
			</div>
			
			<div class="PRIORIDADE-PLANEJADO">
				<span class="cor"></span>
				<span class="descricao">Planejada</span>
			</div>
		</div>	
	</div>
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
		tabelaDinamica.table = new SigaTable('#solicitacoes_table', '')
			.configurar("aoColumns", tabelaDinamica.objetoTabela.estrutura)
			.configurar("iDisplayLength", 25)
			.configurar("bSort", tabelaDinamica.objetoTabela.podeOrdenar)
			.configurar("bFilter", tabelaDinamica.objetoTabela.podeFiltrar)
			.configurar("bPaginate", tabelaDinamica.objetoTabela.podePaginar)
			.configurar("serverSide", true)
			.configurar("ajax",{
				"url":  "${linkTo[SolicitacaoController].buscar}",
				"type": "POST",
				"beforeSend": function () {
					jQuery.blockUI(objBlock);
				    },
				"complete": function () {
					jQuery.unblockUI();
				    },
				"data": function (d) {
					$("#frm").find("input").each(function(){
						if (this.name && this.value) {			
							d[this.name] = this.value;
						}
					});
					$("#frm").find("select").each(function(){
						if (this.name && $(this).val())
							d[this.name] = $(this).val();
					});
					d['filtro.pesquisar'] = true;
					d['filtro.start'] = d['start'];
					d['filtro.length'] = d['length'];
					if (d['order'].length > 0){
						d['filtro.orderBy'] = d['columns'][d['order'][0].column].name;
						d['filtro.sentidoOrdenacao'] = d['order'][0].dir.toUpperCase();
					}
				    }
				})
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

		//Edson: insere os menus de sele��o de colunas dentro da dataTable
		$(".siga-multiple-select").insertAfter("#solicitacoes_table_length");
	});

	function formatarDetalhes(data) {
		var quantidadeColunas = tabelaDinamica.table.table.find('th').size();
		return tabelaDinamica.formatarDetalhes(data, tabelaDinamica.objetoTabela, quantidadeColunas);
	}

	function solicitacoesRowCallback( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
		if (aData) 
			$(nRow).addClass(aData.cssClass);

		$(nRow).attr('id', aData.idSolicitacao);
		$(nRow).attr('data-json', JSON.stringify(aData));
		$(nRow).attr('data-json-id', aData.idSolicitacao);
	}
</script>
