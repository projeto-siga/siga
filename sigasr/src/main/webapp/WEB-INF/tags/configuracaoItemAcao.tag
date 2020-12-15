<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<%@ attribute name="itemConfiguracaoSet" type="java.util.List" required="false"%>
<%@ attribute name="acoesSet" type="java.util.List" required="false"%>


<!-- Itens de Configuracao -->
<div class="card mb-2">
	<div class="card-header">Itens de Configuração</div>
	<div class="card-body">
		<!-- content bomex -->
		<div class="table-responsive">
			<table id="itemConfiguracao_table" class="table">
				<thead>
					<tr>
						<th>ID</th>
						<th>Sigla</th>
						<th>Titulo</th>
						<th>Descrição</th>
						<th>Itens Similares</th>
						<th></th>
					</tr>
				</thead>
				
				<tbody>
					<c:forEach items="${itemConfiguracaoSet}" var="item">
						<tr>
							<td>${item.id }</td>
							<td>${item.sigla}</td>
							<td>${item.tituloItemConfiguracao}</td>
							<td>${item.descricao }</td>
							<td>${item.descricaoSimilaridade }</td>
							<td><a class="itemConfiguracao_remove"><img src="/siga/css/famfamfam/icons/delete.png" style="visibility: inline; cursor: pointer" /></a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="gt-table-buttons">
			<a href="javascript: configuracaoItemAcaoService.modalAbrir('itemConfiguracao')" class="btn btn-primary" style="color: #fff">Incluir</a>
		</div>
	</div>
</div>

<!-- Acoes -->
<div class="card mb-2">
	<div class="card-header">Ações</div>
	<div class="card-body">
		<!-- content bomex -->
		<div class="table-responsive">
			<table id="acao_table" class="table">
				<thead>
					<tr>
						<th>ID</th>
						<th>Sigla</th>
						<th>Título</th>
						<th></th>
					</tr>
				</thead>
				
				<tbody>
					<c:forEach items="${acoesSet}" var="acao">
						<tr>
							<td>${acao.id }</td>
							<td>${acao.sigla}</td>
							<td>${acao.tituloAcao }</td>
							<td><a class="acao_remove"><img src="/siga/css/famfamfam/icons/delete.png" style="visibility: inline; cursor: pointer" /></a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<a href="javascript: configuracaoItemAcaoService.modalAbrir('acao')" class="btn btn-primary" style="color: #fff">Incluir</a>
	</div>
</div>	
	

<sigasr:modal nome="itemConfiguracao" titulo="Adicionar Item de Configuração" largura="80%">
	<script>
	//Edson: esta funcao evita que o usuario de ok sem a busca por ajax ter terminado
	function bloqueiaItemOkSeVazio(){
		if ($("#formulario_itemConfiguracao_id").val() && $("#formulario_itemConfiguracao_sigla").val() && $("#itemConfiguracaoSpan").text())
			$("#modalItemOk").removeAttr('disabled');
		else 
			$("#modalItemOk").attr("disabled", "disabled");
	}
	</script>
	<div id="dialogItemConfiguracao">
		<div class="form-group">
			<label>Item de Configuração</label>
			<sigasr:selecao3 propriedade="itemConfiguracao" tipo="itemConfiguracao" tema="simple" modulo="sigasr" onchange="bloqueiaItemOkSeVazio()"/>
			<span style="display:none;color: red" id="designacao.itemConfiguracao">Item de Configuração não informado.</span>
		</div>
		<button id="modalItemOk" onclick="javascript: configuracaoItemAcaoService.inserirItemConfiguracao()" class="btn btn-primary" disabled>Ok</button>
		<a href="javascript: configuracaoItemAcaoService.modalFechar('itemConfiguracao')" class="btn btn-secondary" style="color: #fff">Cancelar</a>
	</div>
</sigasr:modal>

<sigasr:modal nome="acao" titulo="Adicionar Ação" largura="80%">
	<script>
	//Edson: esta funcao evita que o usuario de ok sem a busca por ajax ter terminado
	function bloqueiaAcaoOkSeVazio() {
		if ($("#formulario_acao_id").val() && $("#formulario_acao_sigla").val() && $("#acaoSpan").text())
			$("#modalAcaoOk").removeAttr('disabled');
		else 
			$("#modalAcaoOk").attr("disabled", "disabled");
	}
	</script>
	<div id="dialogAcao">
		<div class="form-group">
			<label>Ação</label> 
			<sigasr:selecao3 propriedade="acao" tipo="acao" tema="simple" modulo="sigasr" onchange="bloqueiaAcaoOkSeVazio()"/>
			<span style="display:none;color: red" id="designacao.acao">Ação não informada.</span>
		</div>
		<button id="modalAcaoOk" onclick="javascript: configuracaoItemAcaoService.inserirAcao()" class="btn btn-primary" disabled>Ok</button>
		<a href="javascript: configuracaoItemAcaoService.modalFechar('acao')" class="btn btn-primary" style="color: #fff">Cancelar</a>
	</div>
</sigasr:modal>

<script>
	jQuery( document ).ready(function($) {
		// Delete Item ConfiguraÃƒÂ§ÃƒÂ£o
		$('#itemConfiguracao_table tbody').on('click', 'a.itemConfiguracao_remove', function () {
			configuracaoItemAcaoService.itemConfiguracaoTable.api().row($(this).closest('tr')).remove().draw(false);
		});
	 	// Delete AÃƒÂ§ÃƒÂ£o
	    $('#acao_table tbody').on( 'click', 'a.acao_remove', function () {
	    	configuracaoItemAcaoService.acaoTable.api().row($(this).closest('tr')).remove().draw(false);
	    } );

	});

	/**
	 *  Item configuracao service
	 **/
	var configuracaoItemAcaoService = {};
	configuracaoItemAcaoService.configurarItemConfiguracaoDataTable = function() {
		this.itemConfiguracaoTable = $('#itemConfiguracao_table').dataTable({
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
			"columnDefs": [{ "targets": [0], "visible": false, "searchable": false},
			               { "width": "80px", "targets": 1 },
			               { "width": "100px", targets: [2,3]},
			               { "width": "320px", targets: [4]},
			               { "width": "5px", targets: [5]}],
			"iDisplayLength": 3,
			"aLengthMenu": [3, 10, 25, 50, 100]
		});
	}

	configuracaoItemAcaoService.limparItemConfiguracao = function() {
		// limpando campos do componente de busca
		$("#formulario_itemConfiguracao_id").val('');
		$("#formulario_itemConfiguracao_descricao").val('');
		$("#formulario_itemConfiguracao_sigla").val('');
		$("#itemConfiguracaoSpan").html('');
	}

	configuracaoItemAcaoService.limparAcao = function() {
		$("#formulario_acao_id").val('');
		$("#formulario_acao_descricao").val('');
		$("#formulario_acao_sigla").val('');
		$("#acaoSpan").html('');
	}
	
	configuracaoItemAcaoService.inserirItemConfiguracao = function() {
		var idSelecionado = $("#formulario_itemConfiguracao_id").val();
		
		if (idSelecionado == undefined || idSelecionado == '') {
			alert("Por favor, selecione um item de configuração antes de continuar, ou clique em Cancelar.");
			return;
		}
		if(configuracaoItemAcaoService.podeAdicionarItem(idSelecionado)) {
			var row = [	$("#formulario_itemConfiguracao_id").val(),
			           	$("#formulario_itemConfiguracao_sigla").val(),
			           	$("#formulario_itemConfiguracao_descricao").val(),
			           	$("#formulario_itemConfiguracao_descricao").val(),
			           	"",
			           	"<a class=\"itemConfiguracao_remove\"><img src=\"/siga/css/famfamfam/icons/delete.png\" style=\"visibility: inline; cursor: pointer\" /></a>"];
			
			this.itemConfiguracaoTable.api().row.add(row).draw();
	        			
			configuracaoItemAcaoService.limparItemConfiguracao();
			
			configuracaoItemAcaoService.modalFechar('itemConfiguracao');
		} 
		else alert('O item selecionado já foi adicionado');
	}
	configuracaoItemAcaoService.podeAdicionarItem = function(idConfiguracao) {
		var itensAdicionados = configuracaoItemAcaoService.getItensArray();
		for(var i = 0; i < itensAdicionados.length; i++) {
			var itemConfiguracao = itensAdicionados[i];
			
			if(itemConfiguracao != null && idConfiguracao == itemConfiguracao.idItemConfiguracao) 
				return false;
		}
		return true;
	}
	configuracaoItemAcaoService.podeAdicionarAcao = function(idAcao) {
		var acoesAdicionadas = configuracaoItemAcaoService.getAcoesArray();
		for(var i = 0; i < acoesAdicionadas.length; i++) {
			var acao = acoesAdicionadas[i];
			
			if(acao != null && idAcao == acao.idAcao) 
				return false;
		}
		return true;
	}
	configuracaoItemAcaoService.configurarAcaoDataTable = function() {
		this.acaoTable = $('#acao_table').dataTable( {
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
			        "last":       "Útimo",
			        "next":       "Próximo",
			        "previous":   "Anterior"
			    },
			    "aria": {
			        "sortAscending":  ": clique para ordenação crescente",
			        "sortDescending": ": clique para ordenação decrescente"
			    }
			},
			"columnDefs": [{"targets": [0], "visible": false, "searchable": false},
			               { "width": "5px", "targets": 3 }],
			"iDisplayLength": 3,
			"aLengthMenu": [3, 10, 25, 50, 100]
	    } );
	}
	configuracaoItemAcaoService.inserirAcao = function() {
		var idSelecionado = $("#formulario_acao_id").val();
		
		if (idSelecionado == undefined || idSelecionado == '') {
			alert("Por favor, selecione uma ação antes de continuar, ou clique em Cancelar.");
			return;
		}
		if(configuracaoItemAcaoService.podeAdicionarAcao(idSelecionado)) {
			var row = [	$("#formulario_acao_id").val(),
	        			$("#formulario_acao_sigla").val(),
	        			$("#formulario_acao_descricao").val(),
	        			"<a class=\"acao_remove\"><img src=\"/siga/css/famfamfam/icons/delete.png\" style=\"visibility: inline; cursor: pointer\" /></a>"];
			
			this.acaoTable.api().row.add(row).draw();
			
			// limpando campos do componente de busca
			configuracaoItemAcaoService.limparAcao();
			
			configuracaoItemAcaoService.modalFechar('acao');
		}
		else alert('A ação selecionada já foi adicionada');
	}
    
	configuracaoItemAcaoService.modalAbrir = function(componentId) {
		$("#" + componentId + "_dialog").dialog('open');
		configuracaoItemAcaoService.limparItemConfiguracao();
		configuracaoItemAcaoService.limparAcao();
	}
	
	configuracaoItemAcaoService.modalFechar = function(componentId) {
		$("#" + componentId + "_dialog").dialog('close');
	}
	
	configuracaoItemAcaoService.getItemAcaoAsString = function(objectName) {
		var params = '';
		
		// Percorre lista de Itens de ConfiguraÃƒÂ§ÃƒÂ£o
		this.itemConfiguracaoTable.api().rows().indexes().each(function (i) {
			var rowValues = configuracaoItemAcaoService.itemConfiguracaoTable.api().row(i).data();
			
			// Atualiza a string serializada
			if (rowValues) {
	        	params 	+= '&itemConfiguracaoSet[' + i + '].id=' + rowValues[0];
			}
		});
		
		// Percorre lista de AÃƒÂ§ÃƒÂµes
		this.acaoTable.api().rows().indexes().each(function (i) {
			var rowValues = configuracaoItemAcaoService.acaoTable.api().row(i).data();
			
			// Atualiza a string serializada
			if (rowValues) {
				params 	+= '&acoesSet[' + i + '].id=' + rowValues[0];
			}
		});
		
		return params;
	}

	configuracaoItemAcaoService.getItensArray = function() {
		var itens = [];
		
		this.itemConfiguracaoTable.api().rows().indexes().each(function (i) {
			var rowValues = configuracaoItemAcaoService.itemConfiguracaoTable.api().row(i).data();
			if (rowValues) {
	        	itens.push({
	        		idItemConfiguracao : rowValues[0]
	        	});
			}
		});

		if(itens.length == 0) {
			itens.push(null);
		}
		return itens;
	}

	configuracaoItemAcaoService.getAcoesArray = function() {
		var acoes = [];
		
		this.acaoTable.api().rows().indexes().each(function (i) {
			var rowValues = configuracaoItemAcaoService.acaoTable.api().row(i).data();
			if (rowValues) {
				acoes.push({
					idAcao : rowValues[0]
				});
			}
		});
		if(acoes.length == 0) {
			acoes.push(null);
		}
		return acoes;
	}
	
	configuracaoItemAcaoService.atualizaDadosTabelaItemAcao = function(jSon) {
		var tableItem = $('#itemConfiguracao_table'), tableAcao = $('#acao_table');
		
		if (jSon) {
			this.limparDadosTabelaItemAcao();
			
			if (jSon.listaItemConfiguracaoVO) {
				// cria a lista de Itens de ConfiguraÃƒÂ§ÃƒÂ£o, e adiciona na tela
				for (i = 0; i < jSon.listaItemConfiguracaoVO.length; i++) {
					var item = jSon.listaItemConfiguracaoVO[i],
						rowItem = [item.id ? item.id : '', 
						           item.siglaItemConfiguracao ? item.siglaItemConfiguracao : '',
						           item.tituloItemConfiguracao ? item.tituloItemConfiguracao : '',
						           item.descrItemConfiguracao ? item.descrItemConfiguracao : '', 
						           item.descricaoSimilaridade ? item.descricaoSimilaridade : '',
						           "<a class=\"itemConfiguracao_remove\"><img src=\"/siga/css/famfamfam/icons/delete.png\" style=\"visibility: inline; cursor: pointer\" /></a>"];

					var tr = TableHelper.criarTd(rowItem);
					tableItem.append(tr);
				}
			}
			
			if (jSon.listaAcaoVO) {
				// cria a lista de aÃƒÂ§ÃƒÂµes, e adiciona na tela
				for (i = 0; i < jSon.listaAcaoVO.length; i++) {
					var acao = jSon.listaAcaoVO[i],
						rowAcao = [acao.id ? acao.id : '',
								   acao.sigla ? acao.sigla : '',
						           acao.tituloAcao ? acao.tituloAcao : '',
						           "<a class=\"acao_remove\"><img src=\"/siga/css/famfamfam/icons/delete.png\" style=\"visibility: inline; cursor: pointer\" /></a>"];

					var tr = TableHelper.criarTd(rowAcao);
					tableAcao.append(tr);
				}
			}
		}
		this.configurarItemConfiguracaoDataTable();
		this.configurarAcaoDataTable();
	}

	// Limpa os dados das tabelas.
	configuracaoItemAcaoService.limparDadosTabelaItemAcao = function() {
		if(this.itemConfiguracaoTable) {
			this.itemConfiguracaoTable.fnDestroy();
			this.itemConfiguracaoTable.find('tbody tr').remove();
			this.itemConfiguracaoTable = null;
		}
		if(this.acaoTable) {
			this.acaoTable.fnDestroy();
			this.acaoTable.find('tbody tr').remove();
			this.acaoTable = null;
		}
	}
	
	configuracaoItemAcaoService.iniciarDataTables = function() {
		configuracaoItemAcaoService.limparDadosTabelaItemAcao();
		configuracaoItemAcaoService.configurarAcaoDataTable();
		configuracaoItemAcaoService.configurarItemConfiguracaoDataTable();
	}
</script>