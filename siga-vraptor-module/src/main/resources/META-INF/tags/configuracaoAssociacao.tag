<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<%@ attribute name="orgaos" type="java.util.List" required="false"%>
<%@ attribute name="locais" type="java.util.List" required="false"%>
<%@ attribute name="modoExibicao" required="false"%>
<%@ attribute name="mostrarDesativado" required="false"%>
<%@ attribute name="desativar" required="false"%>
<%@ attribute name="urlGravar" required="false"%>

<%@ attribute name="itemConfiguracaoSet" type="java.util.List" required="false"%>
<%@ attribute name="acoesSet" type="java.util.List" required="false"%>

<div class="gt-content" style="padding-top: 10px;">
	<h3>Associações</h3>
	<div class="gt-content-box dataTables_div">
        <div class="gt-form-row dataTables_length">
            <label>
            	<siga:checkbox name="mostrarAssocDesativada" value="${mostrarDesativado}"></siga:checkbox>
            	<b>Incluir Inativas</b>
            </label>
        </div>        
		<table id="associacao_table" border="0" class="gt-table display">
			<thead>
				<tr>
					<th style="color: #333">
						<span class="bt-expandir">
							<span id="iconeBotaoExpandirTodos">+</span>
						</span>
					</th>
					<th>idAssociacao</th>
					<th>Orgão</th>
					<th>Local</th>
					<th>Solicitante</th>
					<th><i>Item &darr;</i></th>
					<th>Açãoo</th>
					<th>Obrigatório</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
	<div class="gt-table-buttons">
           <a href="javascript: associacaoService.cadastrar('Cadastrar Associação')" class="gt-btn-small gt-btn-left">Incluir</a>
	</div>
</div>

<siga:modal nome="associacao" titulo="Cadastrar associação">
	<div class="gt-form gt-content-box" style="width: 800px !important; max-width: 800px !important;">
	
		<form id="associacaoForm">
			<input id="idConfiguracao" type="hidden" name="idConfiguracao">
			
			<div id="divSolicitante" class="gt-form-row gt-width-100">
				<label>Solicitante</label> 
				<siga:pessoaLotaFuncCargoSelecao
						nomeSelLotacao="lotacao"
						nomeSelPessoa="dpPessoa"
						nomeSelFuncao="funcaoConfianca"
						nomeSelCargo="cargo"
						nomeSelGrupo="cpGrupo"
						valuePessoa="${dpPessoa != null ? dpPessoa.pessoaAtual :'' }"
						valueLotacao="${lotacao != null ? lotacao.lotacaoAtual : '' }"
						valueFuncao="${funcaoConfianca }"
						valueCargo="${cargo}"
						valueGrupo="${cpGrupo}"
						disabled="${desativar}">
				</siga:pessoaLotaFuncCargoSelecao>
			</div>
		
			<div class="gt-form-row gt-width-100">
				<label>Órgão</label>
				
				<select name="orgaoUsuario" class="select-siga" style="width: 100%;">
					<option value="0">Nenhum</option>
					<c:forEach items="${orgaos}" var="orgao">
						<option value="${orgao.idOrgaoUsu}">${orgao.nmOrgaoUsu}</option>
					</c:forEach>
				</select>
			</div>
		
			<div class="gt-form-row gt-width-100">
				<label>Local</label>
				
				<select name="complexo" class="select-siga" style="width: 100%;">
					<option value="0">Nenhum</option>
					<c:forEach items="${locais}" var="local">
						<option value="${local.idComplexo}">${local.nomeComplexo}</option>
					</c:forEach>
				</select>
			</div>
		
<%-- 			#{configuracaoItemAcao itemConfiguracaoSet:_itemConfiguracaoSet, --%>
<%-- 							 acoesSet:_acoesSet}#{/configuracaoItemAcao} --%>
			
			<c:if test="${modoExibicao == 'atributo'}">
				<div class="gt-form-row">
					<label><siga:checkbox name="atributoObrigatorio" value="${atributoObrigatorio}"></siga:checkbox>
						Obrigatório
					</label>
				</div>
			</c:if>
			
			<div class="gt-form-row">
				<a href="javascript: associacaoService.gravar()" class="gt-btn-medium gt-btn-left">Gravar</a>
				<a href="javascript: associacaoService.cancelarGravacao()" class="gt-btn-medium gt-btn-left">Cancelar</a>
			</div>
		</form>
	</div>
	<div class="gt-content-box" id="modal-associacao-error" style="display: none;">
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
</siga:modal>

<script type="text/javascript">
	function AssociacaoService(opts) {
		BaseService.call(this, opts);
	}
	
	AssociacaoService.prototype = Object.create(BaseService.prototype);

	var colunas = {
			'botaoExpandir': 				0,
			'idAssociacao': 				1,
			'orgao': 						2,
			'local': 						3,
			'solicitante': 					4,
			'itemConfiguracao': 			5,
			'acao': 						6,
			'atributoObrigatorioString': 	7,
			'botaoExcluir': 				8
			};

	    jQuery("#checkmostrarAssocDesativada").click(function() {
			$.ajax({
	   	         type: "POST",
	   	         url: associacaoService.getUrlDesativarReativar(document.getElementById('checkmostrarAssocDesativada').checked),
	   	         dataType: "text",
	   	         success: function(response) {
		   	    	 associacaoService.atualizarListaAssociacoes({ 
			   	    	 associacoesVO : JSON.parse(response)
			   	    },
			   	    true);
	   	         },
	   	         error: function(response) {
	   	        	$('#modal-associacao').hide(); 
	
	   	        	var modalErro = $('#"modal-associacao-error"');
	   	        	modalErro.find("h3").html(response.responseText);
	   	        	modalErro.show(); 
	   	         }
	       });
	    });
		table = null,
		isEditingAssociacao = false,
		associacaoService = null;

	jQuery( document ).ready(function( $ ) {
		$("#associacaoForm").validate({
			onfocusout: false
		});
	});

	var associacaoServiceConfig = {
		urlGravar : '${urlGravar}?',
		dialogCadastro : jQuery('#associacao_dialog'),
		tabelaRegistros : jQuery('#associacao_table'),
		objectName : 'associacao',
		formCadastro : jQuery('#associacaoForm')
	};

	associacaoService = new AssociacaoService(associacaoServiceConfig);

	associacaoService.getId = function(obj) {
		return obj.idConfiguracao;
	}

	associacaoService.cadastrar = function(title) {
		if (!podeCadastrarAssociacao())
            return;

		// reset no componente de pessoaLotaFuncCargoSelecao
		$("#dpPessoalotacaofuncaoConfiancacargocpGrupo")[0].changeValue(1);
		configuracaoItemAcaoService.iniciarDataTables();
		BaseService.prototype.cadastrar.call(this, title);	
	}

	/**
	 * Customiza o metodo editar
	 */
	 associacaoService.editar = function(obj, title) {
		// reset no componente de pessoaLotaFuncCargoSelecao
		$("#dpPessoalotacaofuncaoConfiancacargocpGrupo")[0].changeValue(1);
		
		BaseService.prototype.editar.call(this, obj, title); // super.editar();
	}
	
	associacaoService.getObjetoParaGravar = function() {
		var obj = BaseService.prototype.getObjetoParaGravar.call(this);

		var modoExibicao = ${modoExibicao};
		if (modoExibicao == 'atributo'){
			obj.atributo = { idAtributo : $('#idAtributo').val() };
		} else if (modoExibicao == 'pesquisa'){ 
			obj.pesquisaSatisfacao = { idPesquisa : $('#idPesquisa').val() };
		}
		return obj;
	}

	associacaoService.onGravar = function(obj, objSalvo) {
		var tr = BaseService.prototype.onGravar.call(this, obj, objSalvo);
		var modoExibicao = ${modoExibicao};
		
		if (modoExibicao == 'atributo'){
			item =  this.getItemPaiPorId(obj.atributo.idAtributo);
		} else if (modoExibicao == 'pesquisa'){
			item = this.getItemPaiPorId(obj.pesquisaSatisfacao.idPesquisa);
		}

		// Se eh edicao mantem o json atualizado
		if(obj.idConfiguracao != "") {
			var linha = this.row(obj);
			linha.remove();
			item.associacoesVO.forEach(function(associacao) {
				if(associacao.idConfiguracao == obj.idConfiguracao) {
					var index = item.associacoesVO.indexOf(associacao);
					item.associacoesVO.splice(index, 1);
				}
			});
		}
		item.associacoesVO.push(objSalvo);
		associacaoService.adicionarFuncionalidadesNaLinha(this.row(objSalvo), objSalvo);
		associacaoService.associacaoTable.atualizarDetalhes(this.getId(objSalvo));	// Para atualizar o detalhe caso esteja aberto
		associacaoService.associacaoTable.detalhes(detalhesListaAssociacao);
		tr.find('td:first-child').html("+");
		tr.find('td.acoes').html(associacaoService.conteudoColunaAcao(objSalvo));
		return tr;
	}
	
	associacaoService.adicionarFuncionalidadesNaLinha = function(node, assoc) {
		node.find('td:first').addClass('details-control');
		node.data('json', assoc);
		node.attr('data-json-id', assoc.idConfiguracao);
		node.attr('data-json', JSON.stringify(assoc));
		node.on('click', function() {
			associacaoService.editar(assoc, 'Alterar AssociaÃƒÂ§ÃƒÂ£o');
			configuracaoItemAcaoService.atualizaDadosTabelaItemAcao(assoc);
			$("#checkatributoObrigatorio").attr('checked', assoc.atributoObrigatorio);
		});
	}
	
	associacaoService.getItemPaiPorId = function (id) {
		return $("#${modoExibicao}_table")
					.dataTable()
					.$("tr[data-json-id=" + id + "]")
					.data('json');
	}

	associacaoService.getListasAsString = function () {
		return configuracaoItemAcaoService.getItemAcaoAsString(associacaoService.opts.objectName);
    }

	associacaoService.serializar = function(obj) {
		return BaseService.prototype.serializar.call(this, obj)  + "&" + associacaoService.getListasAsString();
	}
	
	associacaoService.getRow = function(assoc) {
		return [
					' ',
					assoc.idConfiguracao,
					assoc.orgaoUsuario ? assoc.orgaoUsuario.sigla : ' ',
					assoc.complexo ? assoc.complexo.descricao : ' ',
					assoc.solicitante ? assoc.solicitante.sigla : ' ',
					formatDescricaoLonga(assoc.listaItemConfiguracaoVO),
					formatDescricaoLonga(assoc.listaAcaoVO),
					getAtributoObrigatorioString(assoc.atributoObrigatorio),
					associacaoService.conteudoColunaAcao(assoc)
   				];
	}

	associacaoService.conteudoColunaAcao = function(assoc) {
		if(assoc.ativo) {
			return 		'<a class="once desassociar gt-btn-ativar" onclick="desassociar(event, ' + assoc.idConfiguracao + ')" title="Remover associaÃƒÂ§ÃƒÂ£o">' +
							'<input class="idAssociacao" type="hidden" value="' + assoc.idConfiguracao + '"/>' +
							'<img id="imgCancelar" src="/siga/css/famfamfam/icons/cancel_gray.png" style="margin-right: 5px;"/>' + 
						'</a>';
		}
		return ' ';
	}

	associacaoService.configurarDataTable = function() {
		this.associacaoTable = new SigaTable('#associacao_table')
			.configurar("columnDefs", [{
					"targets": [colunas.idAssociacao, colunas.botaoExpandir, colunas.botaoExcluir],
					"searchable": false,
					"sortable" : false
				},
				{
					"targets": [colunas.idAssociacao],
					"visible": false
				}])
			.configurar("aaSorting",  [[colunas.itemConfiguracao, 'asc']])
			.configurar("fnRowCallback", associacaoRowCallback)
			.criar()
			.detalhes(detalhesListaAssociacao);

		this.opts.dataTable = this.associacaoTable.dataTable;
	}
	
	associacaoService.limparDadosAssociacoes = function() {
		if(this.associacaoTable) {
			this.associacaoTable.destruir();
		}
	}

	associacaoService.atualizarListaAssociacoes = function(jSon, clickCheck) {
		this.limparDadosAssociacoes();
		
		if (!clickCheck) {
	        document.getElementById('checkmostrarAssocDesativada').checked = false;
	        document.getElementById('checkmostrarAssocDesativada').value = false;
        }

		var table = associacaoServiceConfig.tabelaRegistros;

		if (jSon && jSon.associacoesVO) {
			// cria a lista de associacoes, e adiciona na tela
			for (i = 0; i < jSon.associacoesVO.length; i++) {
				var assoc = jSon.associacoesVO[i],
					rowAssoc = [
							'+',
							assoc.idConfiguracao,
							assoc.orgaoUsuario ? assoc.orgaoUsuario.sigla : ' ',
							assoc.complexo ? assoc.complexo.descricao : ' ',
							assoc.solicitante ? assoc.solicitante.sigla: ' ',
							formatDescricaoLonga(assoc.listaItemConfiguracaoVO),
							formatDescricaoLonga(assoc.listaAcaoVO),
							getAtributoObrigatorioString(assoc.atributoObrigatorio),
							' '
						];
				var tr = TableHelper.criarTd(rowAssoc),
					indiceAcoes = this.indiceAcoes(tr),
					tdAcoes = tr.find('td:nth(' + indiceAcoes + ')').addClass('acoes');

				tr.data('json', assoc);
				tr.data('json-id', assoc.idConfiguracao);
				tr.attr('data-json', JSON.stringify(assoc));
				tr.attr('data-json-id', assoc.idConfiguracao);
				
				tdAcoes.html(associacaoService.conteudoColunaAcao(assoc));
				
				table.append(tr);
				associacaoService.adicionarFuncionalidadesNaLinha(tr, assoc);
			}
		}
		associacaoService.configurarDataTable();
	}

	function associacaoRowCallback( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
		$('td:eq(' + colunas.acaoExpandir + ')', nRow).addClass('details-control');

		var assoc = $(nRow).data('json');
		if (assoc) {
			if (assoc.ativo == false)
				$('td', nRow).addClass('item-desativado');
			else
				$('td', nRow).removeClass('item-desativado');
		}
	}

	
	function ocultaAssociacoes() {
		if ($("#objetivo").val() == 1){
			$("#associacoes").show();
		} else {
			$("#associacoes").hide();
		}
	}

	function detalhesListaAssociacao(d, associacao) {
		var tr = $('<tr class="detail">'),
			td = $('<td colspan="6">'),
			table = $('<table class="datatable" cellpadding="5" cellspacing="0" border="0" style="margin-left:60px;">'),
			trItens = $('<tr>'),
			trAcoes = $('<tr>');

		TableHelper.detalheLista("<b>Itens de configuraÃƒÂ§ÃƒÂ£o:</b>", associacao.listaItemConfiguracaoVO, trItens);
		TableHelper.detalheLista("<b>AÃ§Ãµes:</b>", associacao.listaAcaoVO, trAcoes);
			
		table.append(trItens);
		table.append(trAcoes);
		
		td.append(table);
		tr.append(td);
	    return tr;
	};

	function transformStringToBoolean(value) {
		if (value.constructor.name == 'String')
			return value == 'true';
		else
			return value;
	}

	function formatDescricaoLonga(lista) {
		var str = ' ';
		if (lista) {
			for (var i = 0; i < lista.length; i++) {
				var obj = lista[i];

				if (str.length > 10) {
					str = str + '...';
					break;
				}
				
				if (i > 0)
					str = str + '; ';

				str = str + obj.descricao;
			}
		}
		
		return str;
	}

	function getAtributoObrigatorioString(value) {
		return value ? 'Sim': 'NÃ£o';
	}

	function desassociar(event, idAssociacaoDesativar) {
		event.stopPropagation()
		
		var me = $(this),
			tr = $(event.currentTarget).parent().parent()[0],
			row = associacaoService.associacaoTable.dataTable.api().row(tr).data(),
			idAssociacao = idAssociacaoDesativar ? idAssociacaoDesativar : row[colunas.idAssociacao],
			idItem = $("#hisIdIni").val(),
			mostrarDesativa = $('#checkmostrarAssocDesativada').is(':checked');


		var modoExibicao = ${modoExibicao};
		var vUrl, vData = null;
		if (modoExibicao == 'atributo'){
			vUrl = "@{Application.desativarAssociacaoEdicao()}";
			vData = {idAtributo : idItem, idAssociacao : idAssociacao};
		} else if (modoExibicao == 'pesquisa'){
			vUrl = "@{Application.desativarAssociacaoPesquisaEdicao()}";
			vData = {idPesquisa : idItem, idAssociacao : idAssociacao};
		}
		
		$.ajax({
	         type: "POST",
	         url: vUrl,
	         data: vData,
	         dataType: "text",
	         success: function(response) {
		         if (mostrarDesativa == "true") {
		        	 $('td', tr).addClass('item-desativado');
		        	 $('td:last', tr).html(' ');
		        	 associacaoTable.atualizarDetalheTr($(tr));
		         } else {
		        	 associacaoService.associacaoTable.dataTable.api().row(tr).remove().draw();
		         }
	         },
	         error: function(response) {
	        	$('#modal-associacao').hide(); 

	        	var modalErro = $('#"modal-associacao-error"');
	        	modalErro.find("h3").html(response.responseText);
	        	modalErro.show(); 
	         }
       });
	}
</script>