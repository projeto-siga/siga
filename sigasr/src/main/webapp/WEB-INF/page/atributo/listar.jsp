<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="Atributos">
	
	<jsp:include page="../main.jsp"></jsp:include>
	
	<script src="/sigasr/javascripts/detalhe-tabela.js"></script>
	<script src="//cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
	<script src="/sigasr/javascripts/jquery.serializejson.min.js"></script>
	<script src="/sigasr/javascripts/jquery.populate.js"></script>
	<script src="/sigasr/javascripts/base-service.js"></script>
	<script src="/sigasr/javascripts/jquery.validate.min.js"></script>
	
	<style>
		.ui-widget-content a[disabled] {
			opacity:0.5;
		}
		
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
	
	<div class="gt-bd clearfix">
		<div class="gt-content">
			<h2>Pesquisa de atributos</h2>
					 
			<!-- content bomex -->
			<div class="gt-content-box dataTables_div">
				<div class="gt-form-row dataTables_length">
					<label>
						<siga:checkbox name="mostrarDesativado" value="${mostrarDesativados}"></siga:checkbox>
						<b>Incluir Inativas</b>
					</label>
				</div>
				
				<table id="atributo_table" class="gt-table display">
					<thead>
						<tr>
							<th>Nome</th>
							<th>Descrição</th>
							<th>Objetivo</th>
							<th>Código</th>
							<th>Formato</th>
							<th></th>
							<th style="display: none;">jSon</th>
						</tr>
					</thead>
	
					<tbody>
						<c:forEach items="${atts}" var="att">
							<tr data-json-id="${att.idAtributo}" data-json='${att.toJson()}' onclick="atributoService.editar($(this).data('json'), 'Alterar atributo')" 
								style="cursor: pointer;">
								<td>${att.nomeAtributo}</td>
								<td>${att.descrAtributo}</td>
								<td>${att.objetivoAtributo.descricao}</td>
								<td>${att.codigoAtributo}</td>
								<td>${att.tipoAtributo.descrTipoAtributo}</td>
								<td class="acoes">
									<siga:desativarReativar id="${att.idAtributo}"
															onReativar="atributoService.reativar"
															onDesativar="atributoService.desativar"
															isAtivo="${att.isAtivo()}"></siga:desativarReativar>
								</td>
								<td style="display: none;">
									${att.toJson()}
								</td>
							</tr>
						 </c:forEach>
					</tbody>
				</table>
			</div>
			<!-- /content box -->
			<div class="gt-table-buttons">
				<a onclick="atributoService.cadastrar('Incluir Atributo')" class="gt-btn-medium gt-btn-left">Incluir</a>
			</div>
		</div>
	</div>
	
	<siga:modal nome="editarAtributo" titulo="Cadastrar Atributo" largura="820">
		<div id="divEditarAtributoForm"><jsp:include page="editar.jsp"></jsp:include></div>
	</siga:modal>
	
</siga:pagina>

<script type="text/javascript">
	var colunasAtributo = 
	{
		nomeAtributo:       0,
		descrAtributo:      1,
		descrObjetivoAtributo: 2,
		codigoAtributo: 3,
		descrTipoAtributo:  4,
		acoes:              5,
		jSon:               6
	};
	
	var QueryString = function () {
		// This function is anonymous, is executed immediately and
		// the return value is assigned to QueryString!
		var query_string = {};
		var query = window.location.search.substring(1);
		var vars = query.split("&");
		for (var i=0;i<vars.length;i++) {
			var pair = vars[i].split("=");
			// If first entry with this name
			if (typeof query_string[pair[0]] === "undefined") {
				query_string[pair[0]] = pair[1];
				// If second entry with this name
			} else if (typeof query_string[pair[0]] === "string") {
				var arr = [ query_string[pair[0]], pair[1] ];
				query_string[pair[0]] = arr;
				// If third or later entry with this name
			} else {
				query_string[pair[0]].push(pair[1]);
			}
		}
		return query_string;
	}();
	
	jQuery(document).ready(function($) {
	if (QueryString.mostrarDesativados != undefined) {
		document.getElementById('checkmostrarDesativado').checked = QueryString.mostrarDesativados == 'true';
		document.getElementById('checkmostrarDesativado').value = QueryString.mostrarDesativados == 'true';
	}
		
	$("#checkmostrarDesativado").click(function() {
		jQuery.blockUI(objBlock);
		if (document.getElementById('checkmostrarDesativado').checked)
			location.href = '${linkTo[AtributoController].listar}?mostrarDesativados=true';
		else
			location.href = '${linkTo[AtributoController].listar}';
	});
	
	opts.dataTable= $('#atributo_table').dataTable({
		    stateSave : true,
			"language": {
				"emptyTable":     "Não existem resultados",
			    "info":           "Mostrando de _START_ a _END_ do total de _TOTAL_ registros",
			    "infoEmpty":      "Mostrando de 0 a 0 do total de 0 registros",
			    "infoFiltered":   "(filtrando do total de _MAX_ registros)",
			    "infoPostFix":    "",
			    "thousands":      ".",
			    "lengthMenu":     "Mostrar _MENU_ registros",
			    "loadingRecords": "Carregando...",
			    "processing":     "Processando...",
			    "search":         "Filtrar:",
			    "zeroRecords":    "Nenhum registro encontrado",
			    "paginate": {
			        "first":      "Primeiro",
			        "last":       "Úšltimo",
			        "next":       "Próximo",
			        "previous":   "Anterior"
			    },
			    "aria": {
			        "sortAscending":  ": clique para ordenação crescente",
			        "sortDescending": ": clique para ordenação decrescente"
			    }
			},
			"columnDefs": [{
				"targets": [colunasAtributo.acoes, colunasAtributo.jSon ],
				"searchable": false,
				"sortable" : false
			},
			// Coluna JSon 
			{
				"targets": [colunasAtributo.jSon],
				"visible": false
			}],
			"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
				var atributo = undefined;
				
				try {
					atributo = JSON.parse($(nRow).data('json'));
				}
				catch(err) {
					atributo = $(nRow).data('json');
				}
				
				if (atributo) {
					if (atributo.ativo == false)
						$('td', nRow).addClass('item-desativado');
					else
						$('td', nRow).removeClass('item-desativado');
				}
			}
		});
	});
	
	var opts = {
		 urlDesativar : '${linkTo[AtributoController].desativarAtributo}?',
		 urlReativar : '${linkTo[AtributoController].reativarAtributo}?',
		 urlGravar : '${linkTo[AtributoController].gravarAtributo}',
		 dialogCadastro : $('#editarAtributo_dialog'),
		 tabelaRegistros : $('#atributo_table'),
		 objectName : 'atributo',
		 formCadastro : jQuery('#atributoForm'),
		 mostrarDesativados : QueryString.mostrarDesativados,
		 colunas : colunasAtributo.acoes
	};
	
	// Define a "classe" AtributoService
	function AtributoService(opts) {
		// super(opts)
		BaseService.call(this, opts);
	}
	
	// AtributoService extends BaseService
	AtributoService.prototype = Object.create(BaseService.prototype);
	
	var atributoService = new AtributoService(opts);
	
	atributoService.getId = function(atributo) {
		return atributo.idAtributo || atributo['atributo.idAtributo'];
	}
	
	atributoService.onRowClick = function(atributo) {
		atributoService.editar(atributo, 'Alterar Atributo');
	}
	
	atributoService.getRow = function(atributo) {
		var marginLeft = (atributo.nivel-1) * 2,
			fontWeight = (atributo.nivel == 1) ? 'bold' : 'normal',
			span = $('<span></span>');
		
		var spanHTML = '<span style="margin-left:{margin-left};font-weight:{font-weight}">{descricao}</span>';
		spanHTML = spanHTML.replace('{margin-left}', marginLeft + 'em');
		spanHTML = spanHTML.replace('{font-weight}', fontWeight);
		spanHTML = spanHTML.replace('{descricao}', atributo.tituloAtributo);
		return [atributo.nomeAtributo, 
				atributo.descrAtributo, 
				atributo.objetivoAtributo ? atributo.objetivoAtributo.descricao : '', 
				atributo.codigoAtributo, 
				atributo.descrTipoAtributo,
				'COLUNA_ACOES',
				atributo];		
	}
	
	/**
	* Customiza o metodo editar
	*/
	atributoService.editar = function(obj, title) {
		BaseService.prototype.editar.call(this, obj, title); // super.editar();
// 		atualiza a lista de Associações
		this.buscarAssociacoes(obj);
		associacaoService.verificarTipoAtributo();
	}
	
	/**
	* Sobescreve o metodo cadastrar para limpar a tela.
	*/
	atributoService.cadastrar = function(title) {
		BaseService.prototype.cadastrar.call(this, title); // super.cadastrar();
		
		// limpa a lista de Associações
		associacaoService.limparDadosAssociacoes();
		associacaoService.atualizarListaAssociacoes({});
		associacaoService.verificarTipoAtributo();
	}

	atributoService.serializar = function(obj) {
		var query = BaseService.prototype.serializar.call(this, obj);
		return query + "&atributo=" + this.getId(obj);
	}
	
	atributoService.buscarAssociacoes = function(assoc) {
		associacaoService.limparDadosAssociacoes();
		if (assoc && this.getId(assoc)) {
			$.ajax({
		    	type: "GET",
		    	url: "${linkTo[AtributoController].buscarAssociacaoAtributo}?idAtributo=" + this.getId(assoc),
		    	dataType: "text",
		    	success: function(obj) {
		    		var associacaoJson = JSON.parse(obj);
		    		// alimenta a lista de AssociaÃ§Ãµes
					associacaoService.atualizarListaAssociacoes(associacaoJson);
		    	},
		    	error: function(error) {
		        	alert("Não foi possível carregar as Associações deste Atributo.");
		    	}
		   	});
		}
	}

	
</script>