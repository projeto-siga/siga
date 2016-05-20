<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<siga:pagina titulo="Pesquisa de Satisfação">

	<jsp:include page="../main.jsp"></jsp:include>


	<script src="/sigasr/javascripts/detalhe-tabela.js"></script>
	<script src="/sigasr/javascripts/jquery.dataTables.min.js"></script>
	<script src="/sigasr/javascripts/jquery.serializejson.min.js"></script>
	<script src="/sigasr/javascripts/jquery.populate.js"></script>
	<script src="/sigasr/javascripts/base-service.js"></script>
	<script src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
	<script src="/sigasr/javascripts/jquery.blockUI.js"></script>
	<script src="/sigasr/javascripts/jquery.validate.min.js"></script>
	<script src="/sigasr/javascripts/base-service.js"></script>
	<script src="/sigasr/javascripts/language/messages_pt_BR.min.js"></script>

	<div class="gt-bd clearfix">
		<div class="gt-content">
			<h2>Pesquisas de Satisfa&ccedil;&atilde;o</h2>
			<!-- content bomex -->
			<div class="gt-content-box dataTables_div">
				<div class="gt-form-row dataTables_length">
					<label> <siga:checkbox name="mostrarDesativados"
							value="${mostrarDesativados}"></siga:checkbox> <b>Incluir
							Inativas</b>
					</label>
				</div>

				<table id="pesquisa_table" class="gt-table display">
					<thead>
						<tr>
							<th>Nome</th>
							<th>Descri&ccedil;&atilde;o</th>
							<th></th>
						</tr>
					</thead>

					<tbody>
						<c:forEach items="${pesquisas}" var="pesquisa">
							<tr data-json-id="${pesquisa.idPesquisa}" data-json='${pesquisa.toJson()}' onclick="pesquisaService.editar($(this).data('json'), 'Alterar pesquisa')"	style="cursor: pointer;">
								<td>${pesquisa.nomePesquisa}</td>
								<td>${pesquisa.descrPesquisa}</td>
								<td class="acoes">
									<sigasr:desativarReativar id="${pesquisa.idPesquisa}" onReativar="pesquisaService.reativar" onDesativar="pesquisaService.desativar" isAtivo="${pesquisa.isAtivo()}"></sigasr:desativarReativar>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<!-- /content box -->
			<div class="gt-table-buttons">
				<a onclick="pesquisaService.cadastrar('Incluir Pesquisa')" class="gt-btn-medium gt-btn-left">Incluir</a>
			</div>
		</div>
	</div>
	<sigasr:modal nome="pesquisa" titulo="Cadastrar Pesquisa">
		<div id="divEditarPesquisaForm">
			<jsp:include page="editar.jsp" />
		</div>
	</sigasr:modal>
</siga:pagina>

<script>
	var colunasPesquisa = {
			nome: 0,
			descricao: 1,
			acoes: 2
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

	var opts = {
		urlDesativar : "${linkTo[PesquisaSatisfacaoController].desativar}?",
		urlReativar : "${linkTo[PesquisaSatisfacaoController].reativar}?",
		urlGravar : '${linkTo[PesquisaSatisfacaoController].gravarPesquisa}',
		dialogCadastro : $('#pesquisa_dialog'),
		tabelaRegistros : $('#pesquisa_table'),
		objectName : 'pesquisa',
		formCadastro : $('#pesquisaForm'),
		mostrarDesativados : QueryString.mostrarDesativados,
		colunas : colunasPesquisa.acoes
	};

	$(document).ready(function() {
		$("#checkmostrarDesativados").click(function() {
			jQuery.blockUI(objBlock);
			if (document.getElementById('checkmostrarDesativados').checked)
				location.href = '${linkTo[PesquisaSatisfacaoController].listar}' + '?mostrarDesativados=true';
			else
				location.href = '${linkTo[PesquisaSatisfacaoController].listar}' + '?mostrarDesativados=false';
 		});

		opts.dataTable= $('#pesquisa_table').dataTable({
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
			        "sortAscending":  ": clique para ordenação crescente",
			        "sortDescending": ": clique para ordenação decrescente"
			    }
			},
			"columnDefs": [{
				"targets": [colunasPesquisa.acoes],
				"searchable": false,
				"sortable" : false
			}],
			"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
				var pesquisa = undefined;

				try {
					pesquisa = JSON.parse($(nRow).data('json'));
				}
				catch(err) {
					pesquisa = $(nRow).data('json');
				}

				if (pesquisa) {
					if (pesquisa.ativo == false)
						$('td', nRow).addClass('item-desativado');
					else
						$('td', nRow).removeClass('item-desativado');
				}
			}
		});
	});

	// Define a "classe" pesquisaService
	function PesquisaService(opts) {
		// super(opts)
		BaseService.call(this, opts);
	}
	// pesquisaService extends BaseService
	PesquisaService.prototype = Object.create(BaseService.prototype);

	var pesquisaService = new PesquisaService(opts);

	pesquisaService.getId = function(pesquisa) {
		return pesquisa.idPesquisa || pesquisa['pesquisa.idPesquisa'];
	}

	pesquisaService.getRow = function(pesquisa) {
		return [pesquisa.nomePesquisa || ' ', pesquisa.descrPesquisa || ' ', 'COLUNA_ACOES'];
	}
	pesquisaService.onRowClick = function(pesquisa) {
		pesquisaService.editar(pesquisa, 'Alterar pesquisa');
	}

	/**
	* Customiza o metodo editar
	*/
	pesquisaService.editar = function(obj, title) {
		perguntas.limpar();

		BaseService.prototype.editar.call(this, obj, title); // super.editar();

		for(var i = 0; i < obj.perguntasSet.length; i++) {
			var pergunta = obj.perguntasSet[i];
			perguntas.incluirItem(pergunta.descrPergunta, pergunta.tipoPergunta.idTipoPergunta, pergunta.tipoPergunta.nomeTipoPergunta, pergunta.idPergunta);
		}

		this.buscarAssociacoes(obj);
	}

	/**
	* Sobescreve o metodo cadastrar para limpar a tela.
	*/
	pesquisaService.cadastrar = function(title) {
		BaseService.prototype.cadastrar.call(this, title); // super.cadastrar();

		$('#perguntas').html('');
		// limpa a lista de AssociaÃ§Ãµes
		associacaoService.limparDadosAssociacoes();
		associacaoService.atualizarListaAssociacoes({});
	}

	pesquisaService.serializar = function(obj) {
		var query = BaseService.prototype.serializar.call(this, obj);
		return query + "&pesquisa=" + this.getId(obj) + pesquisaService.paramsPerguntas();
	}

	pesquisaService.buscarAssociacoes = function(assoc) {
		associacaoService.limparDadosAssociacoes();
		if (assoc && this.getId(assoc)) {
			$.ajax({
		    	type: "GET",
		    	url: "${linkTo[PesquisaSatisfacaoController].buscarAssociacaoPesquisa}?idPesquisa=" + this.getId(assoc),
		    	dataType: "text",
		    	success: function(obj) {
		    		var associacaoJson = JSON.parse(obj);
		    		// alimenta a lista de AssociaÃÂ§ÃÂµes
					associacaoService.atualizarListaAssociacoes(associacaoJson);
		    	},
		    	error: function(error) {
		        	alert("Não foi possível carregar as Associações deste Atributo.");
		    	}
		   	});
		}
	}

	pesquisaService.paramsPerguntas = function() {
	    var jForm = $("form"),
	    	params = new String();
	    	$("#perguntas").find("li").each(function(i){
	            var jDivs=$(this).find("span");
	            params += '&perguntaSet[' + i + '].descrPergunta=' + jDivs[0].innerHTML;
	            params += '&perguntaSet[' + i + '].tipoPergunta.idTipoPergunta=' + jDivs[1].id;
	            params += '&perguntaSet[' + i + '].ordemPergunta=' + i;
	            if (this.id.indexOf("novo_") < 0)
	                    params += '&perguntaSet[' + i + '].idPergunta=' + this.id;
	    	});
    	return params;
	}

</script>