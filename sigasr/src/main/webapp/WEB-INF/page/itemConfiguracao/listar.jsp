<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<siga:pagina titulo="Designação de Solicitações">

	<jsp:include page="../main.jsp"></jsp:include>
	
	<script src="/sigasr/javascripts/jquery.dataTables.min.js"></script>
	<script src="/sigasr/javascripts/jquery.serializejson.min.js"></script>
	<script src="/sigasr/javascripts/jquery.populate.js"></script>
	<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>
	<script src="/sigasr/javascripts/detalhe-tabela.js"></script>
	<script src="/sigasr/javascripts/base-service.js"></script>
	<script src="/sigasr/javascripts/jquery.validate.min.js"></script>
	<script src="/sigasr/javascripts/language/messages_pt_BR.min.js"></script>

	<style>
	.item-pai {
		padding-left: 0em !important;
		font-weight: bold !important;
	}
	
	.item-filho {
		padding-left: 2em !important;
		font-style: italic !important;
	}
	
	.item-neto {
		padding-left: 4em !important;
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
	
	<div class="container-fluid mb-2">
			<h2>Itens de Configuração</h2>
			<!-- content bomex -->
			<div class="card card-body mb-2">
				<div class="gt-form-row dataTables_length">
					<label>
						<siga:checkbox name="mostrarDesativados" value="${mostrarDesativados}"></siga:checkbox>
						<b>Incluir Inativas</b>
					</label>
				</div>
				<div class="table-responsive">
					<table id="itens_configuracao_table" class="table">
						<thead>
							<tr>
								<th style="color: #333" class="hide-sort-arrow">
									<button class="bt-expandir">
										<span id="iconeBotaoExpandirTodos">+</span>
									</button>
								</th>
								<th>Código</th>
								<th>Título</th>
								<th>Descrição</th>
								<th>Similaridade</th>
								<th></th>
								<th style="display: none;">VO Item</th>
							</tr>
						</thead>
		
						<tbody>
							<c:forEach items="${itens}" var="item">
								<tr data-json-id="${item.idItemConfiguracao}" data-json='${item.toVO().toJson()}' 
									onclick="itemConfiguracaoService.editar($(this).data('json'), 'Alterar item de configuração')"
									style="cursor: pointer;">
									<td class="gt-celula-nowrap details-control" style="text-align: center;">+</td>
									<td>${item.siglaItemConfiguracao}</td>
									<td>${item.tituloItemConfiguracao}</td>
									<td>${item.descrItemConfiguracao}</td>
									<td>${item.descricaoSimilaridade}</td>
									<td class="acoes">
										<sigasr:desativarReativar id="${item.idItemConfiguracao}" onReativar="itemConfiguracaoService.reativar" onDesativar="itemConfiguracaoService.desativar" isAtivo="${item.isAtivo()}"></sigasr:desativarReativar>
									</td>
									<td style="display: none;">${item.srItemConfiguracaoJson}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<!-- /content box -->
			<a onclick="itemConfiguracaoService.cadastrar('Incluir Item')" class="btn btn-primary" style="color: #fff">Incluir</a>
	</div>
	
	<sigasr:modal nome="editarItem" titulo="Editar Item" largura="80%">
		<div id="divEditarItem"><jsp:include page="editar.jsp"></jsp:include></div>
	</sigasr:modal>
	
</siga:pagina>



<script type="text/javascript">
	var colunasLista = {
		expandir: 0,
		codigo: 1,
		titulo: 2,
		descricao: 3,
		similaridade: 4,
		acoes: 5,
		jSon: 6
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
			document.getElementById('checkmostrarDesativados').checked = QueryString.mostrarDesativados == 'true';
			document.getElementById('checkmostrarDesativados').value = QueryString.mostrarDesativados == 'true';
		}
			
		$("#checkmostrarDesativados").click(function() {
			jQuery.blockUI(objBlock);
			
			if (document.getElementById('checkmostrarDesativados').checked)
				location.href = '${linkTo[ItemConfiguracaoController].listar}' + '?mostrarDesativados=true';
			else
				location.href = '${linkTo[ItemConfiguracaoController].listar}' + '?mostrarDesativados=false';
		});
	});
	

	var itemTable = new SigaTable('#itens_configuracao_table')
		.configurar("aaSorting", [[colunasLista.codigo, 'asc']])
		.configurar("columnDefs",  [
     		// Coluna com expandir da grid, coluna de acoes e JSon
        	{
        		"targets": [colunasLista.expandir, colunasLista.acoes, colunasLista.jSon],
        		"searchable": false,
        		"sortable": false
        	},
        	// Coluna similaridade e JSon 
        	{
        		"targets": [colunasLista.similaridade, colunasLista.jSon],
        		"visible": false
        	}
        ])
        .configurar("fnRowCallback", function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
        	var jSonItem = getJsonByString($(nRow).data('json'));
    		
        	if (jSonItem) {
				$('td:eq('+colunasLista.expandir+')', nRow).addClass('details-control');

				if (jSonItem.nivel == 1)
					$('td:eq('+colunasLista.titulo+')', nRow).addClass('item-pai');
				else if (jSonItem.nivel == 2)
					$('td:eq('+colunasLista.titulo+')', nRow).addClass('item-filho');
				else if (jSonItem.nivel == 3)
					$('td:eq('+colunasLista.titulo+')', nRow).addClass('item-neto');

				if (jSonItem.ativo == false)
					$('td', nRow).addClass('item-desativado');
				else
					$('td', nRow).removeClass('item-desativado');
       		}
		})
		.criar()
		.detalhes(detalhesItemConfiguracaoFormat);
	
	var optsLista = {
			 urlDesativar : "${linkTo[ItemConfiguracaoController].desativar}",
			 urlReativar : "${linkTo[ItemConfiguracaoController].reativar}",
			 urlGravar : "${linkTo[ItemConfiguracaoController].gravar}",
			 dialogCadastro : $('#editarItem_dialog'),
			 tabelaRegistros : $('#itens_configuracao_table'),
			 objectName : 'itemConfiguracao',
			 formCadastro : jQuery('#formItemConfiguracao'),
			 mostrarDesativados : QueryString.mostrarDesativados,
			 colunas : colunasLista.acoes,
			 dataTable : itemTable.dataTable
	};
	
	// Define a "classe" ItemConfiguracaoService
	function ItemConfiguracaoService(optsLista) {
		// super(opts)
		BaseService.call(this, optsLista);
	}

	// ItemConfiguracaoService extends BaseService
	ItemConfiguracaoService.prototype = Object.create(BaseService.prototype);
	
	var itemConfiguracaoService = new ItemConfiguracaoService(optsLista);
	
	itemConfiguracaoService.getId = function(itemConfiguracao) {
		return itemConfiguracao['itemConfiguracao.idItemConfiguracao'] || itemConfiguracao['idItemConfiguracao'] || '';
	}
	itemConfiguracaoService.getIdEdicao = function() {
		return $('#idItemConfiguracao').val();
	}
	itemConfiguracaoService.getRow = function(itemConfiguracao) {
		return ['', 
				itemConfiguracao.siglaItemConfiguracao, 
				itemConfiguracao.tituloItemConfiguracao,
				itemConfiguracao.descrItemConfiguracao,
				itemConfiguracao.descricaoSimilaridade,
				'COLUNA_ACOES',
				itemConfiguracao];
	}

	itemConfiguracaoService.onRowClick = function(item) {
		itemConfiguracaoService.editar(item, 'Alterar Item');
	}

	itemConfiguracaoService.onGravar = function(obj, objSalvo) {
		var tr = BaseService.prototype.onGravar.call(this, obj, objSalvo);
		itemTable.atualizarDetalhes(this.getId(objSalvo));	// Para atualizar o detalhe caso esteja aberto
		return tr;
	}
	
	/**
	 * Customiza o metodo editar
	 */
	itemConfiguracaoService.editar = function(obj, title) {
		BaseService.prototype.editar.call(this, obj, title); // super.editar();
		atualizarModalItem(obj);
		// carrega as designaÃÂ§ÃÂµes do item
		carregarDesignacoes(this.getId(obj));
	}

	/**
	 * Sobescreve o metodo cadastrar para limpar a tela.
	 */
	itemConfiguracaoService.cadastrar = function(title) {
		BaseService.prototype.cadastrar.call(this, title); // super.editar();
		atualizarModalItem();
		designacaoService.populateFromJSonList({});
	}

	itemConfiguracaoService.serializar = function(obj) {
		return BaseService.prototype.serializar.call(this, obj)  + "&" + itemConfiguracaoService.getListasAsString() + "&itemConfiguracao=" + this.getId(obj);
	}

	itemConfiguracaoService.getListasAsString = function() {
        var params = '';

        $("#gestoresUl").find("li").each(function(i){
            var jDivs=$(this).find("span"),
            	tipo = jDivs[0].id;
            
            if(tipo === 'pessoa'){
            	params += '&gestorSet[' + i + '].dpPessoa.idPessoa=' + jDivs[1].id;
            } else if (tipo === 'lotacao') {
            	params += '&gestorSet[' + i + '].dpLotacao.idLotacao=' + jDivs[1].id;
            }                            
        });
        $("#fatoresUl").find("li").each(function(i){
            var jDivsF=$(this).find("span"),
            	tipoF = jDivsF[0].id;
        	
            if(tipoF === 'pessoa') {
                params += '&fatorMultiplicacaoSet[' + i + '].dpPessoa.idPessoa=' + jDivsF[1].id;
            } else if (tipoF === 'lotacao') {
                params += '&fatorMultiplicacaoSet[' + i + '].dpLotacao.idLotacao=' + jDivsF[1].id;
            }

            params += '&fatorMultiplicacaoSet[' + i + '].numFatorMultiplicacao=' + parseInt(jDivsF[1].innerHTML.replace(" / Fator: ", ""));
   	 	});

        return params;
    }

	function carregarDesignacoes(id) {
		designacaoService.reset();
		
        $.ajax({
        	type: "GET",
        	url: "/sigasr/app/itemConfiguracao/" + id + "/designacoes",
        	dataType: "text",
        	success: function(lista) {
        		var listaJSon = JSON.parse(lista);
        		designacaoService.populateFromJSonList(listaJSon);
        	},
        	error: function(error) {
            	alert("Não foi possível carregar as Designações deste item.");
        	}
       	});
    }

	function detalhesItemConfiguracaoFormat( d, obj) {
		var tr = obj.ativo == true ? $('<tr class="detail">') : $('<tr class="detail configuracao-herdada">'),
			detailHTML = '<td colspan="6"><table class="datatable" cellpadding="5" cellspacing="0" style="margin-left:56px;">'+
			'<tr style="border-top: hidden">'+
				'<td style="padding-right: 0px;"><b>Similaridade:</b></td>'+
					'<td style="padding-left: 5px;">' + (obj.descricaoSimilaridade || "") + '</td>'+
		        '</tr>'+
		    '</table><td>';
		    
	    return tr.append(detailHTML);
	}

	function atualizaItemConfiguracaoJson() {
		var gestorSetVO = [],
			fatorMultiplicacaoSetVO = [];
    	
		// Percorre listas de prioridade
    	$("#gestoresUl").find("li").each(function(i){
            var jDivs=$(this).find("span"),
            	tipoGestor = jDivs[0].id,
            	atributoGestor = '',
            	jSon = {};

            if(tipoGestor === 'pessoa'){
            	atributoGestor = 'dpPessoaVO';
            } else if (tipoGestor === 'lotacao') {
            	atributoGestor = 'dpLotacaoVO';
            }

            jSon[atributoGestor] = jDivs[1].id;
            
            gestorSetVO.push(jSon);
        });

    	$("#fatoresUl").find("li").each(function(i){
            var jDivsF=$(this).find("span"),
            	tipoFator = '',
            	atributoFator = '',
            	jSon = {};
            
            if(jDivsF.length == 0) return;

            var tipoFator = jDivsF[0].id,
            	indice = i-1;
        	
            if(tipoFator === 'pessoa') {
            	atributoFator = 'dpPessoaVO';
            } else if (tipoFator === 'lotacao') {
                atributoFator = 'dpLotacaoVO';
            }

            jSon[atributoFator] = jDivsF[1].id;
            jSon['numFatorMultiplicacao'] = parseInt(jDivsF[1].innerHTML.replace(" / Fator: ", ""));

            fatorMultiplicacaoSetVO.push(jSon);
   	 	});

 		var itemConfiguracaoJSon = {
			'id': $("#idItemConfiguracao").val(),
			'hisIdIni': $("#hisIdIni").val(),
			'sigla': $("#siglaItemConfiguracao").val(),
			'titulo': $("#tituloItemConfiguracao").val(),
			'descricao': $("#descrItemConfiguracao").val(),
			'descricaoSimilaridade': $("#descricaoSimilaridade").val(),
			'numFatorMultiplicacaoGeral': $("#numFatorMultiplicacaoGeral").val(),
 			'gestorSetVO': gestorSetVO,
 			'fatorMultiplicacaoSetVO': fatorMultiplicacaoSetVO
 		};
		
 		return itemConfiguracaoJSon;
	}

	function atualizarModalItem(jSonItem) {
		// limpar a lista
		limparDadosItemModal();
		
		if (jSonItem) {
			atualizarListaGestores(jSonItem);
			atualizarListaFatorMultiplicacao(jSonItem);
		}
	}

	function limparDadosItemModal() {
		removerItensLista('gestoresUl');
		removerItensLista('fatoresUl');
	}

	function removerItensLista(nomeLista) {
		$("#"+nomeLista).find("li").each(function(i){
            this.remove();
            $("#"+nomeLista)[0]["index"]--;
        });
    }

	function getJsonByString(value) {
		var jSonItem;
		
		try{
			jSonItem = JSON.parse(value);
		} catch(e){
			jSonItem = value;
		}

		return jSonItem;
	}

	function atualizarListaGestores(jSon) {
		var gestoresUI = $("#gestoresUl")[0];
		
		if (jSon.gestorSetVO) {
			// cria os itens da lista de gestores, e adiciona na tela
			for (i = 0; i < jSon.gestorSetVO.length; i++) {
				var gestorItem = jSon.gestorSetVO[i];

				if (gestorItem.dpPessoaVO)
					gestoresUI.incluirItem(gestorItem.dpPessoaVO.sigla, gestorItem.dpPessoaVO.descricao, 'pessoa', gestorItem.dpPessoaVO.id, gestorItem.idGestorItem);
				else if (gestorItem.dpLotacaoVO)
		        	gestoresUI.incluirItem(gestorItem.dpLotacaoVO.sigla, gestorItem.dpLotacaoVO.descricao, 'lotacao', gestorItem.dpLotacaoVO.id, gestorItem.idGestorItem);
			}
		}
	}

	function atualizarListaFatorMultiplicacao(jSon) {
		var fatoresUI = $("#fatoresUl")[0];
		
		if (jSon.fatorMultiplicacaoSetVO) {
			// cria os itens da lista de gestores, e adiciona na tela
			for (i = 0; i < jSon.fatorMultiplicacaoSetVO.length; i++) {
				var fatorItem = jSon.fatorMultiplicacaoSetVO[i];

				if (fatorItem.dpPessoaVO)
					fatoresUI.incluirItem(fatorItem.dpPessoaVO.sigla, fatorItem.dpPessoaVO.descricao, fatorItem.numFatorMultiplicacao, 'pessoa', fatorItem.dpPessoaVO.id, fatorItem.idFatorMultiplicacao);
				else if (fatorItem.dpLotacaoVO)
					fatoresUI.incluirItem(fatorItem.dpLotacaoVO.sigla, fatorItem.dpLotacaoVO.descricao, fatorItem.numFatorMultiplicacao, 'lotacao', fatorItem.dpLotacaoVO.id, fatorItem.idFatorMultiplicacao);
			}
		}
	}
</script>