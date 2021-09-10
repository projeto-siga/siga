<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<siga:pagina titulo="Listas">
	
	<jsp:include page="../main.jsp"></jsp:include>

	<script src="/sigasr/javascripts/detalhe-tabela.js"></script>
	<script src="/sigasr/javascripts/jquery.dataTables.min.js"></script>
	<script src="/sigasr/javascripts/jquery.serializejson.min.js"></script>
	<script src="/sigasr/javascripts/jquery.populate.js"></script>
	<script src="/sigasr/javascripts/base-service.js"></script>
	<script src="/sigasr/javascripts/jquery.validate.min.js"></script>
	<script src="/sigasr/javascripts/base-service.js"></script>
	<script src="/sigasr/javascripts/language/messages_pt_BR.min.js"></script>

<div class="gt-bd clearfix">
	<div class="gt-content">
		<h2>Pesquisa de Listas</h2>
		<!-- content bomex -->
		<div class="gt-content-box gt-for-table dataTables_div">
			<div class="gt-form-row dataTables_length">
				<label> 
					<siga:checkbox name="mostrarDesativados"
						value="${mostrarDesativados}"></siga:checkbox> 
						<b>Incluir Inativas</b>
				</label>
			</div>
			<table id="listas_table" border="0" class="gt-table display">
				<thead>
					<tr>
						<th>Nome</th>
						<th>Lota&ccedil;&atilde;o</th>
						<th>A&ccedil;&otilde;es</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach items="${listas}" var="item">
						<tr data-json-id="${item.idLista}" data-json='${item.toJson()}'
							<c:if test="${item.podeConsultar(lotaTitular, titular)}">
 								onclick="javascript:window.location='${linkTo[SolicitacaoController].exibirLista(item.idLista)}'" style="cursor: pointer;"
 							</c:if>>
 							
							<td >${item.nomeLista}</td>
							<td>${item.lotaCadastrante.nomeLotacao}</td>

							<td class="acoes"> 
								<c:if test="${item.podeEditar(lotaTitular, titular)}">	
									<sigasr:desativarReativar id="${item.idLista}" onReativar="listaService.reativar" onDesativar="listaService.desativar" isAtivo="${item.isAtivo()}"></sigasr:desativarReativar>
									<a onclick="javascript:editarLista(event, $(this).parent().parent().data('json'))"> 
										<img src="/siga/css/famfamfam/icons/pencil.png" style="margin-right: 5px;">
									</a>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="gt-content-box" id="modal-error" style="display: none;">
				<table width="100%">
					<tr>
						<td align="center" valign="middle">
							<table class="form" width="50%">
								<tr>
									<td style="text-align: center; padding-top: 10px;">
										<h3></h3>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<!-- /content box -->
 		<div class="gt-table-buttons">
			<a onclick="listaService.cadastrar('Incluir Lista')" class="gt-btn-medium gt-btn-left">Incluir</a>
			<div class="gt-table-buttons"> 
		</div>
	</div> 
</div>
</div>
	
<sigasr:modal nome="editarLista" titulo="Cadastrar Acordo">
	<div id="divEditarLista"><jsp:include page="editarLista.jsp"></jsp:include></div>
</sigasr:modal>

</siga:pagina>


<script type="text/javascript">
	var listaTable,
		colunasLista = {};

	colunasLista.nome =		0;
	colunasLista.lotacao = 	1;
	colunasLista.acoes = 	2;
	
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

	var opts = {
			 urlDesativar : "${linkTo[SolicitacaoController].desativarLista}",
			 urlReativar : "${linkTo[SolicitacaoController].reativarLista}",
			 urlGravar : "${linkTo[SolicitacaoController].gravarLista}",
 			 dialogCadastro : $('#editarLista_dialog'),
			 tabelaRegistros : $('#listas_table'),
			 objectName : 'lista',
			 formCadastro : $('#formLista'),
			 mostrarDesativados : QueryString.mostrarDesativados,
			 colunas : colunasLista.acoes
		};	
	
	$(document).ready(function() {
		if (QueryString.mostrarDesativados != undefined) {
			document.getElementById('checkmostrarDesativados').checked = QueryString.mostrarDesativados == 'true';
			document.getElementById('checkmostrarDesativados').value = QueryString.mostrarDesativados == 'true';
		}

		$("#checkmostrarDesativados").click(function() {
			jQuery.blockUI(objBlock);
			if (document.getElementById('checkmostrarDesativados').checked)
				location.href = "${linkTo[SolicitacaoController].listarLista}" + '?mostrarDesativados=true';
			else
				location.href = "${linkTo[SolicitacaoController].listarLista}" + '?mostrarDesativados=false';
		});

		/* Table initialization */
		opts.dataTable= $('#listas_table').dataTable({
			stateSave : true,
			"language": {
				"emptyTable":     "N&atilde;o existem resultados",
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
			        "last":       "&Uacute;ltimo",
			        "next":       "Pr&oacute;ximo",
			        "previous":   "Anterior"
			    },
			    "aria": {
			        "sortAscending":  ": clique para ordena&ccedil;&atilde;o crescente",
			        "sortDescending": ": clique para ordena&ccedil;&atilde;o decrescente"
			    }
			},
			"columnDefs": [{
				"targets" : [colunasLista.acoes],
				"searchable": false,
				"sortable" : false
			}],
			"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
				var lista = undefined;
				
				try {
					lista = JSON.parse($(nRow).data('json'));
				}
				catch(err) {
					lista = $(nRow).data('json');
				}
				
				if (lista) {
					if (lista.ativo == false)
						$('td', nRow).addClass('item-desativado');
					else
						$('td', nRow).removeClass('item-desativado');
				}
			},
			"iDisplayLength": 25
		});
	});

	// Define a "classe" listaService
	function ListaService(opts) {
		// super(opts)
		BaseService.call(this, opts);
	}
	// listaService extends BaseService
	ListaService.prototype = Object.create(BaseService.prototype);

	var listaService = new ListaService(opts);
	// Sobescreve o metodo cadastrar para limpara tela
	listaService.cadastrar = function(title) {
		BaseService.prototype.cadastrar.call(this, title);
		populatePermissoesFromJSonList({});
		configuracaoInclusaoAutomaticaService.adicionarLista('{}');
	}

	listaService.getId = function(lista) {
		return lista.lista || lista['lista.id'] || lista.idLista;
	}

	listaService.getRow = function(lista) {
		return [lista.nomeLista, lista.nomeLotacao, 'COLUNA_ACOES'];
	}
	
	listaService.editarButton = '<a onclick="javascript:editarLista(event, $(this).parent().parent().data(\'json\'))"><img src="/siga/css/famfamfam/icons/pencil.png" style="margin-right: 5px;"></a>';
	
	/**
	 * Customiza o metodo editar
	 */
	listaService.editar = function(obj, title) {
		BaseService.prototype.editar.call(this, obj, title); // super.editar();
		limparDadosListaModal();
		// carrega as permissÃÂµes da lista
		carregarPermissoes(obj.idLista);
		configuracaoInclusaoAutomaticaService.carregarParaLista(obj.idLista);
	}

	listaService.onGravar = function(obj, objSalvo) {
		var tr = BaseService.prototype.onGravar.call(this, obj, objSalvo);
		afterGravarLista(tr, objSalvo);
		return tr;
	}

	function afterGravarLista(tr, objSalvo) {
	    if (objSalvo.podeEditar) {
			var acoes = tr.find('td.acoes');
			if (acoes)
				acoes = acoes.append(" " + listaService.editarButton);
	    }
	    

		if (objSalvo.podeConsultar) {
		    tr.attr('onclick',"javascript:window.location='${linkTo[SolicitacaoController].exibirLista}"+listaService.getId(objSalvo)+"'");
		    tr.attr('style',"cursor: pointer;");
		}
	}

	/**
	 * Sobrescreve o mÃÂ©todo para adicionar tambÃÂ©m o botÃÂ£o editar.
	 */
	listaService.gerarColunaAtivar = function(id) {
		var column = BaseService.prototype.gerarColunaAtivar.call(this, id);
		column = column + " " + listaService.editarButton;
		return column;
	}
	/**
	 * Sobrescreve o mÃÂ©todo para adicionar tambÃÂ©m o botÃÂ£o editar.
	 */
	listaService.gerarColunaDesativar = function(id) {
		var column = BaseService.prototype.gerarColunaDesativar.call(this, id);
		column = column + " " + listaService.editarButton;
		return column;
	}

	listaService.serializar = function(obj) {
		var query = BaseService.prototype.serializar.call(this, obj);
		return query;
	}

	function editarLista(event, jSonItem) {
		event.stopPropagation();
 		listaService.editar(jSonItem, 'Alterar Lista');
	}
</script>
