#{extends 'main.html' /} #{set title:'Acordos' /}

<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="//cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script src="/sigasr/public/javascripts/jquery.serializejson.min.js"></script>
<script src="/sigasr/public/javascripts/jquery.populate.js"></script>
<script src="/sigasr/public/javascripts/jquery.maskedinput.min.js"></script>
<script src="/sigasr/public/javascripts/base-service.js"></script>

<div class="gt-bd clearfix">
	<div class="gt-content">
		<h2>Acordos</h2>
		<!-- content bomex -->
		<div class="gt-content-box dataTables_div">
			<div class="gt-form-row dataTables_length">
				<label>#{checkbox name:'mostrarDesativado', value:mostrarDesativado/} <b>Incluir Inativas</b></label>
			</div>
			
			<table id="acordo_table" border="0" class="gt-table display">
				<thead>
					<tr>
						<th>Nome</th>
						<th>Descrição</th>
						<th></th>
					</tr>
				</thead>

				<tbody>
					#{list items:acordos, as:'acordo'}
					<tr
						#{if popup}
							onclick="javascript:opener.retorna_acordo${nome}('${acordo.id}','${acordo.id}','${acordo.nomeAcordo}');window.close()"
						#{/if}
						#{else}
							data-json-id="${acordo.id}" data-json="${acordo.toJson()}" onclick="acordoService.editar($(this).data('json'), 'Alterar Acordo')"
						#{/else}
							style="cursor: pointer;">
						<td >${acordo.nomeAcordo}</td>
						<td>${acordo.descrAcordo}</td>
						<td class="acoes">
							#{desativarReativar id:acordo.id, 
												onDesativar :'acordoService.desativar',
												onReativar :'acordoService.reativar',
												isAtivo:acordo.isAtivo() }
							#{/desativarReativar}
						</td>
					</tr>
					 #{/list}
				</tbody>
			</table>
		</div>
		<!-- /content box -->
		<div class="gt-table-buttons">
		<a onclick="acordoService.cadastrar('Incluir Acordo')" class="gt-btn-medium gt-btn-left">Incluir</a>
		</div>

	</div>
</div>

<br />
<br />
<br />

#{modal nome:'editarAcordo', titulo:'Editar Acordo'}
	<div id="divEditarAcordo">#{include
				'Application/editarAcordo.html' /}</div>
#{/modal}

<script>
	var acordoTable,
	 	colunasAcordo = {};

	colunasAcordo.nome =	0;
	colunasAcordo.lotacao = 1;
	colunasAcordo.acoes = 	2;
	
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
			if (document.getElementById('checkmostrarDesativado').checked)
				location.href = '@{Application.buscarAcordoDesativadas()}';
			else
				location.href = '@{Application.buscarAcordo()}';	
		});
		
		optsAcordo.acordoTable = $('#acordo_table').dataTable({
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
			        "last":       "Último",
			        "next":       "Próximo",
			        "previous":   "Anterior"
			    },
			    "aria": {
			        "sortAscending":  ": clique para ordenação crescente",
			        "sortDescending": ": clique para ordenação decrescente"
			    }
			},
			"columnDefs": [{
				"targets": [colunasAcordo.acoes],
				"searchable": false,
				"sortable" : false
			}],
			"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
				var acordo = undefined;
				
				try {
					acordo = JSON.parse($(nRow).data('json'));
				}
				catch(err) {
					acordo = $(nRow).data('json');
				}
				
				if (acordo) {
					if (acordo.ativo == false)
						$('td', nRow).addClass('item-desativado');
					else
						$('td', nRow).removeClass('item-desativado');
				}
			}
		});

		optsAcordo.dataTable = optsAcordo.acordoTable;
	});

	var optsAcordo = {
			 urlDesativar : '@{Application.desativarAcordo()}?',
			 urlReativar : '@{Application.reativarAcordo()}?',
			 urlGravar : '@{Application.gravarAcordo()}',
			 dialogCadastro : $('#editarAcordo_dialog'),
			 tabelaRegistros : $('#acordo_table'),
			 objectName : 'acordo',
			 formCadastro : jQuery('#acordoForm'),
			 mostrarDesativados : QueryString.mostrarDesativados,
			 colunas : colunasAcordo.acoes
	};

	// Define a "classe" AcordoService
	function AcordoService(optsAcordo) {
		// super(opts)
		BaseService.call(this, optsAcordo);
	}

	// AcordoService extends BaseService
	AcordoService.prototype = Object.create(BaseService.prototype);
	
	var acordoService = new AcordoService(optsAcordo);
	
	acordoService.getId = function(acordo) {
		return acordo.id;
	}

	acordoService.getRow = function(acordo) {
		return [acordo.nomeAcordo, acordo.descrAcordo, 'COLUNA_ACOES'];
	}
	
	acordoService.onRowClick = function(acordo) {
		acordoService.editar(acordo, 'Alterar Acordo');
	}

	/**
	* Sobrescreve o método gravar para tratar a lista de parâmetros.
	*/
	acordoService.gravar = function() {
		gravarAplicar(this, false);
	}

	/**
	* Sobrescreve o método aplicar para tratar a lista de parâmetros.
	*/
	acordoService.aplicar = function() {
		return gravarAplicar(this, true);
	}

	function gravarAplicar(baseService, isAplicar) {
		if (!baseService.isValidForm())
			return false;
		
		var obj = baseService.getObjetoParaGravar(),
			url = baseService.opts.urlGravar,
			wrapper = {},
			success = function(objSalvo) {
				if(baseService.onGravar) {
					baseService.onGravar(obj, JSON.parse(objSalvo));
				}
				
				if (isAplicar) {
					baseService.formularioHelper.populateFromJson(JSON.parse(objSalvo));
					
					alert("Cadastro salvo com sucesso.");
				}
				else
					baseService.opts.dialogCadastro.dialog("close");
			}
			
		wrapper[baseService.opts.objectName] = obj;
		var params = jQuery.param(wrapper) + "&" + serializeParametrosAcordo();
		
		$.ajax({
	    	type: "POST",
	    	url: url,
	    	data: params,
	    	dataType: "text",
	    	success: success,
	    	error: baseService.errorHandler
	   	});
	}
	
	/**
	 * Customiza o metodo editar
	 */
	acordoService.editar = function(obj, title) {
		BaseService.prototype.editar.call(this, obj, title); // super.editar();

		// Atualiza a lista de parâmetros
		atualizarParametrosAcordo(obj);

		// carrega a Abrangências do Acordo
		carregarAbrangenciasAcordo(obj.id);
	}

	function carregarAbrangenciasAcordo(id) {
		tableAssociacao.api().clear().draw();

		if (id) {
			$.ajax({
	        	type: "GET",
	        	url: "@{Application.buscarAbrangenciasAcordo()}?id=" + id,
	        	dataType: "text",
	        	success: function(lista) {
	        		var listaJSon = JSON.parse(lista);
	        		acordoService.populateFromJSonList(listaJSon, associacaoTable);
	        	},
	        	error: function(error) {
	            	alert("Não foi possível carregar as Abrangências deste Acordo.");
	        	}
	       	});
		}
    }

	acordoService.populateFromJSonList = function(listaJSon, dataTable) {
		for (var i = 0; i < listaJSon.length; i++) {
			var abrangencia = listaJSon[i];
				row = [
						'',                                                                    // colunas.botaoExpandir
						abrangencia.orgaoUsuario ? abrangencia.orgaoUsuario.id : '',
		          		abrangencia.orgaoUsuario ? abrangencia.orgaoUsuario.sigla : '', 
		          		abrangencia.complexo ? abrangencia.complexo.id : '',
		        		abrangencia.complexo ? abrangencia.complexo.descricao : '', 
		          		abrangencia.solicitante ? abrangencia.solicitante.sigla : '',
	          			abrangencia.solicitante ? abrangencia.solicitante.id : '',
	          			abrangencia.solicitante ? abrangencia.solicitante.descricao : '',
	    	          	abrangencia.solicitante ? abrangencia.solicitante.sigla : '',
   	          			abrangencia.atendente ? abrangencia.atendente.id : '',
   	    	          	abrangencia.atendente ? abrangencia.atendente.descricao : '',
   	    	    	    abrangencia.atendente ? abrangencia.atendente.sigla : '',
	          			abrangencia.prioridade ? abrangencia.prioridade : '',
		          		abrangencia.descPrioridade ? abrangencia.descPrioridade : '',
				        abrangencia.idConfiguracao,												// colunas.idAssociacao
				        abrangencia.hisIdIni,													// colunas.idAssociacao
						'',																		// colunas.botaoExcluir
				        abrangencia																// colunas.jSon
		   			];

				
	        row[colunasAssociacao.botaoExcluir] = acordoService.conteudoColunaAcao(abrangencia);
	        var tr = tableAssociacao.api().row.add(row).draw().node();
            if (!abrangencia.ativo) {
                $('td', $(tr)).addClass('item-desativado');
                $('td:last', $(tr)).html(' ');
            }    
	    	
		}
	}

	acordoService.conteudoColunaAcao = function(abrangencia){
        if (abrangencia.ativo) {          
            return '<td class="gt-celula-nowrap" style="font-size: 13px; font-weight: bold; border-bottom: 1px solid #ccc !important; padding: 7px 10px;">' +
					'<a class="once desassociar" onclick="desassociar(event, ' + abrangencia.idConfiguracao + ')" title="Remover permissão">' +
					'<input class="idAssociacao" type="hidden" value="'+abrangencia.idConfiguracao+'"/>' +
					'<img id="imgCancelar" src="/siga/css/famfamfam/icons/cancel_gray.png" style="margin-right: 5px;">' + 
					'</a>' +	
					'</td>';
        }
        return ' ';
	}	

	// Atualizando lista de Parâmetros de Acordo
	function atualizarParametrosAcordo(acordo) {
		removerItensLista('parametrosAcordo');

		if (acordo) {
			for (var i = 0; i < acordo.atributoAcordoSet.length; i++) {
				var item = acordo.atributoAcordoSet[i];

				$("#parametrosAcordo")[0].incluirItem(item.atributo.idAtributo, item.atributo.nomeAtributo, item.operador, item.operadorNome, item.valor, item.unidadeMedida.idUnidadeMedida, item.unidadeMedidaPlural, item.idAcordoAtributo);
			}
		}
	}

	function removerItensLista(nomeLista) {
		$("#"+nomeLista).find("li").each(function(i){
            this.remove();
            $("#"+nomeLista)[0]["index"]--;
        });
    }

	/**
	 * Sobescreve o metodo cadastrar para limpar a tela.
	 */
	acordoService.cadastrar = function(title) {
		// Atualiza a lista de parâmetros
		atualizarParametrosAcordo();

		// carrega a Abrangências do Acordo
		carregarAbrangenciasAcordo();
		
		BaseService.prototype.cadastrar.call(this, title); // super.editar();
	}	
</script>