#{extends 'main.html' /} #{set title:'Edição de Lista' /}
<script src="/sigasr/public/javascripts/jquery.validate.min.js"></script>
<script src="/sigasr/public/javascripts/language/messages_pt_BR.min.js"></script>
<script src="/sigasr/public/javascripts/detalhe-tabela.js"></script>

<div class="gt-form gt-content-box" style="width: 800px !important; max-width: 800px !important;">
	<form id="formLista" method="post" enctype="multipart/form-data">
		<input type="hidden" id="idLista" name="idLista" value="${lista?.idLista}">
		<input type="hidden" id="id" name="id" value="${lista?.idLista}">
		<input type="hidden" id="hisIdIni" name="hisIdIni" value="${lista?.hisIdIni}">
		<div class="gt-form-row gt-width-66">
			<label>Nome <span>*<span></label> <input type="text"
				id="nomeLista"
				name="nomeLista"
				value="${nomeLista}" 
				size="98"
				maxlength="255"
				required/>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>Abrangência</label>
			<textarea cols="98" rows="5" name="descrAbrangencia"
				id="descrAbrangencia" maxlength="8192">${descrAbrangencia}</textarea>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>Justificativa</label>
			<textarea cols="98" rows="5" name="descrJustificativa"
				id="descrJustificativa" maxlength="8192">${descrJustificativa}</textarea>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>Priorização</label>
			<textarea cols="98" rows="5" name="descrPriorizacao"
				id="descrPriorizacao" maxlength="8192">${descrPriorizacao}</textarea>
		</div>
		
		<div class="container">
			<div class="title-table">
				<h3 style="padding-top: 25px;">Permissões</h3>
			</div>
		</div>
		<div class="gt-content-box gt-for-table dataTables_div">
	        <div class="gt-form-row dataTables_length">
	            <label>#{checkbox name:'mostrarAssocDesativada', value:mostrarAssocDesativada/} <b>Incluir Inativas</b></label>
	        </div>        
			<table id="permissoes_table" border="0" class="gt-table display">
				<thead>
					<tr>
						<th>ID Orgão</th>
						<th>Orgão</th>
						<th>ID Local</th>
						<th>Local</th>
						<th>ID Lotação</th>
						<th>Nome Lotação</th>
						<th>Lotação</th>
						<th>ID Pessoa</th>
						<th>Nome Pessoa</th>
						<th>Pessoa</th>
						<th>ID Cargo</th>
						<th>Cargo</th>
						<th>Cargo</th>
						<th>ID Função</th>
						<th>Função</th>
						<th>Função</th>
						<th>Tipo Permissão JSON</th>
						<th>ID Tipo Permissao</th>
						<th>Tipo Permissão</th>	
						<th>Tipo Permissão</th>	
						<th></th>
					</tr>
				</thead>

				<tbody>
					#{list items:lista?.getPermissoes(), as:'perm'}
						<tr style="cursor: pointer !important;">
							<td>${perm.orgaoUsuario?.id}</td>
							<td>${perm.orgaoUsuario?.acronimoOrgaoUsu}</td>
							<td>${perm.complexo?.idComplexo}</td>
							<td>${perm.complexo?.nomeComplexo}</td>
							<td>${perm.lotacao?.lotacaoAtual?.id}</td>
							<td>${perm.lotacao?.lotacaoAtual?.nomeLotacao}</td>
							<td>${perm.lotacao?.lotacaoAtual?.siglaLotacao}</td>
							<td>${perm.dpPessoa?.id}</td>
							<td>${perm.dpPessoa?.nomePessoa}</td>
							<td>${perm.dpPessoa?.nomeAbreviado}</td>
							<td>${perm.cargo?.id}</td>
							<td>${perm.cargo?.sigla}</td>
							<td>${perm.cargo?.descricao}</td>
							<td>${perm.funcaoConfianca?.id}</td>
							<td>${perm.funcaoConfianca?.sigla}</td>
							<td>${perm.funcaoConfianca?.descricao}</td>
							<td>${perm.getSrConfiguracaoTipoPermissaoJson()}</td>
							<td>${perm.id}</td>
							<td></td>
							<td>${perm.descrTipoPermissao}</td>
							<td class="gt-celula-nowrap" style="font-size: 13px; font-weight: bold; border-bottom: 1px solid #ccc !important; padding: 7px 10px;">
								<a class="once desassociarPermissao" onclick="desativarPermissaoUsoListaEdicao(event, ${perm.id})" title="Remover permissão">
									<input class="idPermissao" type="hidden" value="${perm.id}"/>
									<img id="imgCancelar" src="/siga/css/famfamfam/icons/delete.png" style="margin-right: 5px;"> 
								</a>
							</td>
						</tr>
					#{/list}
				</tbody>
			</table>
		</div>
		<div class="gt-table-buttons">
            <a href="javascript: controleAcessoModalAbrir(false)" class="gt-btn-small gt-btn-left">Incluir</a>
		</div>
		
		<div class="container">
			<div class="title-table">
				<h3 style="padding-top: 25px;">Configurações de inclusão automática</h3>
			</div>
		</div>
		<div class="gt-content-box gt-for-table dataTables_div">
	        <div class="gt-form-row dataTables_length">
	            <label>
	            	<input id="incluirConfiguracaoInclusaoAutomaticaInativas" type="checkbox" onchange="configuracaoInclusaoAutomaticaService.incluirInativas()" /><b>&nbspIncluir Inativas</b>
	            </label>
	        </div>
			<table id="configuracao_inclusao_automatica_table" class="gt-table display">
				<thead>
					<tr>
						<th style="color: #333" class="hide-sort-arrow">
							<button class="bt-expandir" type="button">
								<span id="iconeBotaoExpandirTodos">+</span>
							</button>
						</th>
						<th>Solicitante</th>
						<th>Orgão</th>
						<th>Item</th>
						<th>Ação</th>
						<th>Lotação atendente</th>
						<th>Prioridade</th>
						<th>Prioridade na lista</th>
						<th>Ações</th>
					</tr>
				</thead>
				
				<tbody>
				</tbody>
			</table>
		</div>
		
		<div class="gt-table-buttons">
            <a href="javascript: configuracaoInclusaoAutomaticaService.cadastrar('Cadastro de configuração de inclusão automática')" class="gt-btn-small gt-btn-left">Incluir</a>
		</div>	
		
		<div class="gt-form-row">
			<input type="button" value="Gravar" class="gt-btn-medium gt-btn-left" onclick="listaService.gravar()"/>
			<a class="gt-btn-medium gt-btn-left" onclick="listaService.cancelarGravacao()">Cancelar</a>
			<input type="button" value="Aplicar" class="gt-btn-medium gt-btn-left" onclick="listaService.aplicar()"/>
		</div>

		<div class="gt-form-row gt-width-100">
			<p class="gt-error" style="display:none;" id="erroAoSalvar">Não foi possível salvar o registro.</p>
		</div>
	</form>
</div>

#{modal nome:'controleAcesso', titulo:'Cadastrar Permissão'}
	<form id="formControleAcesso" enctype="multipart/form-data">
		<div class="gt-form gt-content-box" id="modal-permissao">
			<div>
				<input id="idConfiguracao" type="hidden" name="idConfiguracao"> 
				<div class="gt-form-row div-modal-table">
					<label>Órgão</label> 
					#{select name:'orgaoUsuario', items:orgaos, valueProperty:'idOrgaoUsu',
					labelProperty:'acronimoOrgaoUsu', value:orgaoUsuario?.idOrgaoUsu}
					#{option 0}#{/option} 
					#{/select}
				</div>
				<span style="color: red">#{error
						'orgaoUsuario' /}</span>
				<div class="gt-form-row div-modal-table">		
					<label>Local</label> 
					#{select name:'complexo', items:locais, valueProperty:'idComplexo',
					labelProperty:'nomeComplexo', value:complexo?.idComplexo}
					#{option 0}#{/option} 
					#{/select}
				</div>		
				<div class="gt-form-row div-modal-table">
					<label>Lotação</label>#{selecao
						tipo:'lotacao', nome:'lotacao',
						value:lotacao?.lotacaoAtual /}
				</div>
				
				<div class="gt-form-row div-modal-table">
					<label>Pessoa</label>#{selecao
						tipo:'pessoa', nome:'dpPessoa',
						value:dpPessoa?.pessoaAtual /}
				</div>
				
				<div class="gt-form-row div-modal-table">
					<label>Cargo</label>#{selecao
						tipo:'cargo', nome:'cargo',
						value:cargo /}
				</div>
				
				<div class="gt-form-row div-modal-table">
					<label>Função</label>#{selecao
						tipo:'funcao', nome:'funcaoConfianca',
						value:funcaoConfianca /}
				</div>
				
				<div class="gt-form-row div-modal-table">
					<label>Tipo Permissão</label>
						<ul id="ulPermissoes" style="color: #365b6d" name="ulPermissoes"></ul>
						<input type="button" value="Incluir" id="botaoIncluir"
			                      class="gt-btn-small gt-btn-left" style="font-size: 10px;" />
					</div>
				</div>
				
				<div class="gt-form-row">
					<a href="javascript: inserirAcesso()" class="gt-btn-medium gt-btn-left">Gravar</a>
					<a href="javascript: controleAcessoModalFechar()" class="gt-btn-medium gt-btn-left">Cancelar</a>
					#{if idConfiguracao} 
						<input type="button" value="Desativar"
							class="gt-btn-medium gt-btn-left"
							onclick="location.href='@{Application.desativarPermissaoUsoLista()}?id=${idConfiguracao}'" />
					#{/if}
				</div>
			</div>
		</div>
		<div id="dialog">
			<div class="gt-content">
				<div class="gt-form gt-content-box">
					<div class="gt-form-row">
					
						<div id="tiposPermissaoContainer" data-json="${tiposPermissaoJson}"></div>
					
						<div class="gt-form-row gt-width-66">
							<label>Tipo de Permissão</label> #{select name:'itemTipoPermissao', id:'itemTipoPermissao'}
								 #{list items:tiposPermissao, as:'item'} #{option item.idTipoPermissaoLista} ${item.descrTipoPermissaoLista} #{/option} #{/list} #{/select}
						</div>
						<div class="gt-form-row">
							<input type="button" id="modalOk" value="Ok"
		                           class="gt-btn-medium gt-btn-left" />
							<input type="button" value="Cancelar" id="modalCancel" class="gt-btn-medium gt-btn-left" />
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="gt-content-box" id="modal-permissao-error" style="display: none;">
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
	</form>
#{/modal}

#{modal nome:'configuracao_inclusao_automatica', titulo:'Configuração para inclusão automática'}
	<form id="formConfiguracaoInclusaoAutomatica">
		<div class="gt-form gt-content-box" style="width: 800px !important; max-width: 800px !important;">
			<input id="idConfiguracao" type="hidden" name="idConfiguracao">
			<input id="hisIdIni" type="hidden" name="hisIdIni">
		
			<div id="divSolicitante" class="gt-form-row gt-width-100">
				<label>Solicitante</label> 
				#{selecao tipo:'pessoa', nome:'dpPessoaParaInclusaoAutomatica',
				/}
			</div>
		
			<div class="gt-form-row gt-width-100">
				<label>Órgão</label> 
				#{select name:'orgaoUsuario', items:orgaos, valueProperty:'idOrgaoUsu', labelProperty:'acronimoOrgaoUsu', class:'select-siga', style:'width: 100%;'} 
					#{option 0}#{/option} 
				#{/select}
			</div>
		
			<div class="gt-form-row gt-width-100">
				<label>Lotação</label>
				#{selecao tipo:'lotacao', nome:'lotacaoParaInclusaoAutomatica'/}
			</div>
			
			<div class="gt-form-row gt-width-100">
				<label>Prioridade</label> 
				#{select name:'prioridade', items:models.SrPrioridade.values(), labelProperty:'descPrioridade', style:'width:235px;' } 
					#{option ''}Nenhuma#{/option} 
				#{/select}
			</div>
			
			<div class="gt-form-row gt-width-100">
				<label>Prioridade na lista</label>
				#{select name:'prioridadeNaLista', items:models.SrPrioridade.values(), labelProperty:'descPrioridade', style:'width:235px;' } 
					#{option ''}Nenhuma#{/option} 
				#{/select}
			</div>
			
			#{configuracaoItemAcao itemConfiguracaoSet:itemConfiguracaoSet, acoesSet:acoesSet}
			#{/configuracaoItemAcao}
			
			<div class="gt-form-row">
				<a href="javascript: configuracaoInclusaoAutomaticaService.gravar()" class="gt-btn-medium gt-btn-left">Gravar</a>
				<a href="javascript: configuracaoInclusaoAutomaticaService.cancelarGravacao()" class="gt-btn-medium gt-btn-left">Cancelar</a>
			</div>
		</div>
	</form>
#{/modal}

<script language="javascript">
	function ConfiguracaoInclusaoAutomaticaDecorator(configuracaoInclusaoAutomatica, service) {
		/**
		 * Configura o objeto da forma em o mesmo deve ser gravado.
		 */
		this.getObjetoParaGravar = function() {
			var configuracao = Object.create(configuracaoInclusaoAutomatica);
			configuracao.listaPrioridade = service.getIdLista();
			configuracao.dpPessoa = configuracao.dpPessoaParaInclusaoAutomatica;
			configuracao.lotacao = configuracao.lotacaoParaInclusaoAutomatica;
			configuracao.itemConfiguracaoSet = configuracaoItemAcaoService.getItensArray();
			configuracao.acoesSet = configuracaoItemAcaoService.getAcoesArray();
	
			delete configuracao.solicitante;
			delete configuracao.lotacaoParaInclusaoAutomatica;
			
			return configuracao;
		}
		this.getColunaPessoa = function() {
			var descricao = configuracaoInclusaoAutomatica.dpPessoa ? configuracaoInclusaoAutomatica.dpPessoa.descricao : ' ';
			return nullSafe(descricao);
		}
		this.getColunaOrgao = function() {
			var descricao = configuracaoInclusaoAutomatica.orgaoUsuario ? configuracaoInclusaoAutomatica.orgaoUsuario.sigla : ' ';
			return nullSafe(descricao);
		}
		this.getColunaLotacao = function() {
			var descricao = configuracaoInclusaoAutomatica.lotacao ? configuracaoInclusaoAutomatica.lotacao.sigla : ' ';
			return nullSafe(descricao);
		}
		this.getColunaPrioridade = function() {
			var descricao = configuracaoInclusaoAutomatica.descPrioridade ? configuracaoInclusaoAutomatica.descPrioridade : ' ';
			return nullSafe(descricao);
		}
		this.getColunaPrioridadeNaLista = function() {
			var descricao = configuracaoInclusaoAutomatica.descPrioridadeNaLista ? configuracaoInclusaoAutomatica.descPrioridadeNaLista : ' ';
			return nullSafe(descricao);
		}
		this.getDescricaoItem = function() {
			var descricao = null,
				items = configuracaoInclusaoAutomatica.listaItemConfiguracaoVO;
					
			if(items && items.length > 0) {
				var item = items[0];
				descricao = item.siglaItemConfiguracao + " - " + item.tituloItemConfiguracao;
				
				if(items.length > 1)
					descricao += " ...";
			}
			return nullSafe(descricao);
		}
		this.getDescricaoAcao = function() {
			var descricao = " ",
				acoes = configuracaoInclusaoAutomatica.listaAcaoVO;
			
			if(acoes && acoes.length > 0) {
				var acao = acoes[0];
				descricao = acao.sigla + " - " + acao.titulo;
				
				if(acoes.length > 1)
					descricao += " ...";
			}
			return nullSafe(descricao);
		}
	}
	var COLUNA_ACAO = 8,
		optsConfiguracaoInclusao = {
			urlGravar : '@{Application.configuracaoAutomaticaGravar()}',
			urlDesativar : '@{Application.desativarConfiguracaoAutomaticaGravar()}',
			urlReativar : '@{Application.reativarConfiguracaoAutomaticaGravar()}',
			dialogCadastro : $('#configuracao_inclusao_automatica_dialog'),
			tabelaRegistros : $('#configuracao_inclusao_automatica_table'),
			mostrarDesativados : false,
			colunas : COLUNA_ACAO,
			objectName : 'configuracaoInclusaoAutomatica',
			formCadastro : $('#formConfiguracaoInclusaoAutomatica')
	};
	var configuracaoInclusaoAutomaticaService = BaseService.criar(optsConfiguracaoInclusao, function ConfiguracaoInclusaoAutomaticaService(optsConfiguracaoInclusao) {
		BaseService.call(this, optsConfiguracaoInclusao);
	});
	configuracaoInclusaoAutomaticaService.configurarDataTable = function() {
		this.configuracaoInclusaoAutomaticaTable = new SigaTable('#configuracao_inclusao_automatica_table')
			.configurar("iDisplayLength", 10)
			.configurar("columnDefs", [{
				"targets": [0, 8],
				"searchable": false,
				"sortable" : false
			}])
			.configurar("order", [[ 1, "asc" ]])
			.criar();
		
		this.opts.dataTable = this.configuracaoInclusaoAutomaticaTable.dataTable;
	}
	configuracaoInclusaoAutomaticaService.cadastrar = function(title) {
		// Se eh novo, entao salva
		if(!$("#idLista").val() && listaService.aplicar() == false) {
			return;
		}
		BaseService.prototype.cadastrar.call(this, title);
		configuracaoItemAcaoService.atualizaDadosTabelaItemAcao({});
	}
	configuracaoInclusaoAutomaticaService.onGravar = function(obj, objSalvo) {
		var tr = BaseService.prototype.onGravar.call(this, obj, objSalvo);
		tr.find('td:first').addClass('details-control');
		this.configuracaoInclusaoAutomaticaTable.table.mostrarDetalhes(DetalheConfiguracao.detalhes, this.configuracaoInclusaoAutomaticaTable.dataTable);
		return tr;
	}
	configuracaoInclusaoAutomaticaService.getId = function(configuracaoInclusaoAutomatica) {
		return configuracaoInclusaoAutomatica.idConfiguracao;
	}
	configuracaoInclusaoAutomaticaService.getObjetoParaGravar = function() {
		var configuracao = BaseService.prototype.getObjetoParaGravar.call(this);
		return new ConfiguracaoInclusaoAutomaticaDecorator(configuracao, this).getObjetoParaGravar();
	}
	configuracaoInclusaoAutomaticaService.getIdLista = function() {
		return $('[name=idLista]').val();
	}
	configuracaoInclusaoAutomaticaService.getRow = function(configuracaoInclusaoAutomatica) {
		var configuracao = new ConfiguracaoInclusaoAutomaticaDecorator(configuracaoInclusaoAutomatica, this);
		
		return [
				"+",
				configuracao.getColunaPessoa(),  
				configuracao.getColunaOrgao(), 
				configuracao.getDescricaoItem(), 
				configuracao.getDescricaoAcao(),
				configuracao.getColunaLotacao(),
				configuracao.getColunaPrioridade(),
				configuracao.getColunaPrioridadeNaLista(),
				'COLUNA_ACOES' ];
	}
	configuracaoInclusaoAutomaticaService.carregarParaLista = function(idLista) {
		if(idLista) {
			var mostrarDesativados = $("#incluirConfiguracaoInclusaoAutomaticaInativas").is(':checked'),
				urlConsulta = '@{Application.configuracoesParaInclusaoAutomatica()}?idLista=' + idLista + '&mostrarDesativados=' + mostrarDesativados;
	
			optsConfiguracaoInclusao.mostrarDesativados = mostrarDesativados;
			$.get(urlConsulta, function(configuracoes) {
				configuracaoInclusaoAutomaticaService.adicionarLista(JSON.parse(configuracoes));
			});
		}
	}
	configuracaoInclusaoAutomaticaService.adicionarLista = function(configuracoes) {
		var table = $('#configuracao_inclusao_automatica_table');

		if(this.configuracaoInclusaoAutomaticaTable) {
			this.configuracaoInclusaoAutomaticaTable.destruir();
		}
		
		for(var i = 0; i < configuracoes.length; i++) {
			var configuracao = configuracoes[i];
				tr = $('<tr>'), 
				data = this.getRow(configuracao);
				
			for(var j = 0; j < data.length; j++) {
				tr.append('<td>' + data[j] + '</td>');
			}
			table.append(tr);

			var indice = this.indiceAcoes(tr),
				tdAcoes = tr.find('td:nth(' + indice + ')'),
				detailControl = tr.find('td:nth(' + 0 + ')');
			
			detailControl.addClass('details-control');
			tdAcoes.addClass('acoes');
			tr.data('json', configuracao);
			tr.data('json-id', configuracao.idConfiguracao);
			tr.attr('data-json', configuracao);
			tr.attr('data-json-id', configuracao.idConfiguracao);
			
			new DesativarReativar(configuracaoInclusaoAutomaticaService)
				.ativo(configuracao.ativo)
				.innerHTML(tdAcoes, configuracao.idConfiguracao);
		}
		this.configurarDataTable();
		this.configuracaoInclusaoAutomaticaTable.onRowClick(function(tr) {
			configuracaoInclusaoAutomaticaService.onRowClick(tr.data('json'));
		});
		this.configuracaoInclusaoAutomaticaTable.detalhes(DetalheConfiguracao.detalhes);
	}
	configuracaoInclusaoAutomaticaService.onRowClick = function(configuracao) {
		configuracaoInclusaoAutomaticaService.editar(configuracao, 'Alterar configuração para inclusão automática');
	}
	configuracaoInclusaoAutomaticaService.editar = function(configuracao, title) {
		configuracaoItemAcaoService.atualizaDadosTabelaItemAcao(configuracao);
		BaseService.prototype.editar.call(this, configuracao, title);
	}
	configuracaoInclusaoAutomaticaService.incluirInativas = function() {
		configuracaoInclusaoAutomaticaService.carregarParaLista($('[name=idLista]').val());
	}
	function nullSafe(value) {
		return value || " ";
	}
	function getUrlDesativarReativar(desativados) {
	    var url = '@{Application.listarPermissaoUsoLista()}',
	        idLista = $("[name=idLista]").val();
	
	    if(desativados)
	        url = '@{Application.listarPermissaoUsoListaDesativados()}';
	        
	    return url + "?idLista=" + idLista;
	}

	var permissoesTable, 
		isEditingLista = false,
		isEditingContr = false,
		itemTipoPermissaoOptions = [],
		validatorFormLista,
		validatorPermissao,
		colunas = {};

	colunas.idOrgao = 		0;
	colunas.orgao = 		1;
	colunas.idLocal = 		2;
	colunas.local = 		3;
	colunas.idLotacao = 	4;
	colunas.nomeLotacao = 	5;
	colunas.lotacao = 		6;
	colunas.idPessoa = 		7;
	colunas.nomePessoa = 	8;
	colunas.pessoa = 		9;
	colunas.idCargo = 		10;
	colunas.siglaCargo =	11;
	colunas.descrCargo = 	12;
	colunas.idFuncao = 		13;
	colunas.siglaFuncao = 	14;
	colunas.descrFuncao = 	15;
	colunas.jSonTipoPerm =	16;
	colunas.idTipoPerm = 	17;
	colunas.tipoPerm = 		18;
	colunas.descrTipoPerm = 19;
	colunas.acoes = 		20,

    jQuery("#checkmostrarAssocDesativada").click(function() {
        $.ajax({
                type: "POST",
                url: getUrlDesativarReativar(document.getElementById('checkmostrarAssocDesativada').checked),
                dataType: "text",
                success: function(response) {
					var permissoesJSon = JSON.parse(response);
        			populatePermissoesFromJSonList(permissoesJSon);
                },
                error: function(response) {
                   $('#modal-associacao').hide(); 

                   var modalErro = $('#"modal-associacao-error"');
                   modalErro.find("h3").html(response.responseText);
                   modalErro.show(); 
                }
       });
    });
    
	QueryString = {};
	$( document ).ready(function( ) {
        if (QueryString.mostrarDesativados != undefined) {
            document.getElementById('checkmostrarAssocDesativada').checked = QueryString.mostrarDesativados == 'true';
            document.getElementById('checkmostrarAssocDesativada').value = QueryString.mostrarDesativados == 'true';
        }
		validatorFormLista = $("#formLista").validate();
		validatorPermissao = $("#formControleAcesso").validate({
			rules: {
				ulPermissoes: {
					required: true,
					minlength: 3
				}
			}
		});
	});

	$("#controleAcesso_dialog").dialog({
	    autoOpen: false,
	    height: 'auto',
	    width: 'auto',
	    modal: true,
	    resizable: false
	});

	function desativarPermissaoUsoListaEdicao(event, idPermissaoDesativar) {
		event.stopPropagation();
		
		var me = $(this),
			tr = $(event.currentTarget).parent().parent()[0],
			row = permissoesTable.api().row(tr).data(),
			idPermissao = idPermissaoDesativar ? idPermissaoDesativar : me.find('.idPermissao').val(),
			idLista = $("#idLista").val(),
			mostrarDesativa = $("#mostrarAssocDesativada").val();

		$.ajax({
	         type: "POST",
	         url: "@{Application.desativarPermissaoUsoListaEdicao()}",
	         data: {idLista : idLista, idPermissao : idPermissao},
	         dataType: "text",
	         success: function(response) {
	        	 if (mostrarDesativa == "true") {
                     $('td', tr).addClass('item-desativado');
                     $('td:last', tr).html(' ');
                 } else {
                     permissoesTable.api().row(tr).remove().draw();
                 }	        	 
	         },
	         error: function(response) {
	        	alert("Não foi possível desativar esta permissão.");
	         }
       });
	       
	}
	
	function findSelectedIndexByValue(comboBox, value) {
		for (var i = 0; i < comboBox.options.length; i++) {
			if (comboBox.options[i].value == value)
				return i;
		}
		
		return 0;
	};
	
	function atualizarControleAcessoModal(itemArray, tr) {
		var jOrgaoUsuarioCbb = document.getElementsByName("orgaoUsuario")[0],
			jComplexoCbb = document.getElementsByName("complexo")[0],
			permissao = tr.data('json');

		limparDadosAcessoModal();

		if(permissao.orgaoUsuario)
			jOrgaoUsuarioCbb.selectedIndex = findSelectedIndexByValue(jOrgaoUsuarioCbb, permissao.orgaoUsuario.id);
		
		if(permissao.complexo)
			jComplexoCbb.selectedIndex = findSelectedIndexByValue(jComplexoCbb, permissao.complexo.id);

		if(permissao.lotacao) {
			$("#lotacao").val(permissao.lotacao.id);
			$("#lotacao_descricao").val(permissao.lotacao.descricao);
			$("#lotacaoSpan").html(permissao.lotacao.descricao);
			$("#lotacao_sigla").val(permissao.lotacao.sigla);
		}
		if(permissao.dpPessoa) {
			$("#dpPessoa").val(permissao.dpPessoa.id);
			$("#dpPessoa_descricao").val(permissao.dpPessoa.descricao);
			$("#dpPessoaSpan").html(permissao.dpPessoa.descricao);
			$("#dpPessoa_sigla").val(permissao.dpPessoa.sigla);
		}
		if(permissao.cargo) {
			$("#cargo").val(permissao.cargo.id);
			$("#cargo_descricao").val(permissao.cargo.descricao);
			$("#cargoSpan").html(permissao.cargo.descricao);
			$("#cargo_sigla").val(permissao.cargo.sigla);
		}
		if(permissao.funcaoConfianca) { 
			$("#funcaoConfianca").val(permissao.funcaoConfianca.id);
			$("#funcaoConfianca_descricao").val(permissao.funcaoConfianca.descricao);
			$("#funcaoConfiancaSpan").html(permissao.funcaoConfianca.descricao);
			$("#funcaoConfianca_sigla").val(permissao.funcaoConfianca.sigla);
		}
		$("#idConfiguracao").val(permissao.idConfiguracao);
		atualizarListasTipoPermissaoEdicao(permissao);
	};
	
	function controleAcessoModalAbrir(isEdicao){
		isEditingContr = isEdicao;
		itemTipoPermissaoOptions = $("#itemTipoPermissao").clone();
		
		if (isEdicao)
			$("#controleAcesso_dialog").dialog('option', 'title', 'Alterar Permissão');
		else {
			limparDadosAcessoModal();
			$("#controleAcesso_dialog").dialog('option', 'title', 'Incluir Permissão');
		}

		atualizarComboTipoPermissao();

		if (($("#idLista").val() == undefined || $("#idLista").val() == "") && listaService.aplicar() == false)
			return;
		else
			$("#controleAcesso_dialog").dialog('open');
	}
	
	function controleAcessoModalFechar() {
		isEditingContr = false;
		$("#controleAcesso_dialog").dialog("close");
		
		limparDadosAcessoModal();
	};

	function limparDadosAcessoModal() {
		var jOrgaoUsuarioCbb = document.getElementsByName("orgaoUsuario")[0],
		jComplexoCbb = document.getElementsByName("complexo")[0];
		
		jOrgaoUsuarioCbb.selectedIndex = 0;
		jComplexoCbb.selectedIndex = 0;
		$("#lotacao").val('');
		$("#lotacao_descricao").val('');
		$("#lotacao_sigla").val('');
		$("#lotacaoSpan").html('');
		$("#dpPessoa").val('');
		$("#dpPessoa_descricao").val('');
		$("#dpPessoa_sigla").val('');
		$("#dpPessoaSpan").html('');
		$("#cargo").val('');
		$("#cargo_descricao").val('');
		$("#cargo_sigla").val('');
		$("#cargoSpan").html('');
		$("#funcaoConfianca").val('');
		$("#funcaoConfianca_descricao").val('');
		$("#funcaoConfiancaSpan").html('');
		$("#funcaoConfianca_sigla").val('');
		removerItensListaTipoPermissao();
		$("#idConfiguracao").val('');

		$('#modal-permissao-error').css('display', 'none');
		$('#modal-permissao').css('display', 'block');
		
		atualizarComboTipoPermissao();
	}

	function serializePermissao(row) {
		var params = "";
		
		// caso exista algum item na tabela
		if (row[colunas.idOrgao] != '' && row[colunas.idOrgao] > 0)
			params += '&permissao.orgaoUsuario.idOrgaoUsu=' + row[colunas.idOrgao];
		
		if (row[colunas.idLocal] != '' && row[colunas.idLocal] > 0)
        	params += '&permissao.complexo.idComplexo=' + row[colunas.idLocal];
		
		if (row[colunas.idLotacao] != '')
        	params += '&permissao.lotacao.id=' + row[colunas.idLotacao];
		
		if (row[colunas.idPessoa] != '')
        	params += '&permissao.dpPessoa.id=' + row[colunas.idPessoa];
		
		if (row[colunas.idCargo] != '')
        	params += '&permissao.cargo.id=' + row[colunas.idCargo];
		
		if (row[colunas.idFuncao] != '')
        	params += '&permissao.funcaoConfianca.idFuncao=' + row[colunas.idFuncao];
		
		if (row[colunas.idTipoPerm] != '')
			params += '&permissao.id=' + row[colunas.idTipoPerm];

		if ($("#idLista").val() != undefined && $("#idLista").val() != '')
			params += '&permissao.listaPrioridade.id=' + $("#idLista").val();

		// lista de TipoPermissao
		params += row[colunas.tipoPerm];

		return params;
	};
	
	function inserirAcesso() {
		var jOrgaoUsuarioCbb = document.getElementsByName("orgaoUsuario")[0],
			jOrgaoUsuario = jOrgaoUsuarioCbb.options[jOrgaoUsuarioCbb.selectedIndex],
			jComplexoCbb = document.getElementsByName("complexo")[0],
			jComplexo = jComplexoCbb.options[jComplexoCbb.selectedIndex];

		var row = [ 
		   			jOrgaoUsuario.value > 0 ? jOrgaoUsuario.value : '',
					jOrgaoUsuario.value > 0 ? jOrgaoUsuario.text : '',
          			jComplexo.value > 0 ? jComplexo.value : '',
          			jComplexo.value > 0 ? jComplexo.text : '',
          			$("#lotacao").val(),
          			$("#lotacao_descricao").val(),
          			$("#lotacao_sigla").val(),
          			$("#dpPessoa").val(),
          			$("#dpPessoa_descricao").val(),
          			$("#dpPessoa_sigla").val(),
          			$("#cargo").val(),
          			$("#cargo_descricao").val(),
          			$("#cargo_sigla").val(),
          			$("#funcaoConfianca_sigla").val(),
          			$("#funcaoConfianca_descricao").val(),
          			$("#funcaoConfianca").val(),
          			atualizaTipoPermissaoJson(),			//16
          			$("#idConfiguracao").val(),				//17    
          			getListasPermissaoAsString(),			//18
          			getDescricaoTipoPermissao(),			//19
          			''										//20
          			];
			
		$.ajax({
	         type: "POST",
	         url: "@{Application.gravarPermissaoUsoLista()}",
	         data: serializePermissao(row),
	         dataType: "text",
	         success: function(response) {
		         var permissao = JSON.parse(response),
		         	tr = atualizarTr(row, permissao);
		         atualizarRegistro(tr, permissao);
		         controleAcessoModalFechar();
	         },
	         error: function(response) {
	        	$('#modal-permissao').hide(); 

	        	var modalErro = $('#modal-permissao-error');
	        	modalErro.find("h3").html(response.responseText);
	        	modalErro.show(); 
	         }
        });
	};
	function atualizarTr(row, permissao) {
	  	var tr = null;

        row[colunas.idTipoPerm] = permissao.idConfiguracao;
        row[colunas.acoes] = getColunaAtivarDesativar(permissao);

        if (isEditingContr) {
	         var editedRow = permissoesTable.api().row('.selected');
	         editedRow.data(row);
	         tr = $(editedRow.node());
        }
        else {
	         var newRow = permissoesTable.api().row.add(row).draw();
	         tr = $(newRow.node());
        }
        $('.desassociarPermissao').bind('click', desativarPermissaoUsoListaEdicao);
        tr.find('td').removeClass('item-desativado');
        return tr;
	}
	function atualizarRegistro(tr, permissao) {
		 $(tr).data('json', permissao);
	}
	function getColunaAtivarDesativar (permissao) {
		return '<td class="gt-celula-nowrap" style="cursor:pointer; font-size: 13px; font-weight: bold; border-bottom: 1px solid #ccc !important; padding: 7px 10px;">' + 
					'<a class="once desassociarPermissao" title="Remover permissão">' + 
						'<input class="idPermissao" type="hidden" value="' + permissao.idConfiguracao + '"/>' + 
						'<img id="imgCancelar" src="/siga/css/famfamfam/icons/delete.png" style="margin-right: 5px;">' + 
					'</a>' + 
				'</td>';
	}
	function atualizarListasTipoPermissaoEdicao(permissao) {
		if (permissao.listaTipoPermissaoListaVO) {
			var ul = $("#ulPermissoes")[0];
			for (i = 0; i < permissao.listaTipoPermissaoListaVO.length; i++) {
				var lista = permissao.listaTipoPermissaoListaVO[i];
				ul.incluirItem(lista.idTipoPermissaoLista, lista.descrTipoPermissaoLista, "");
			}
		}
	}
	function getDescricaoTipoPermissaoByLista(listaTipoPermissaoListaVO) {
		var descricao = '';

		if (listaTipoPermissaoListaVO) {
			// cria os itens da lista de TipoPermissao, e adiciona na tela
			for (i = 0; i < listaTipoPermissaoListaVO.length; i++) {
				var lista = listaTipoPermissaoListaVO[i];

				if (i > 0)
					descricao += '; ';
				
				descricao += lista.descrTipoPermissaoLista;
			}
		}
		return descricao;
	}
	function getDescricaoTipoPermissao() {
		var descricao = $('#ulPermissoes').find("li:first-child").find("span:eq(0)").html();
		
		if (descricao != null || descricao != undefined) {
			return descricao + " ...";
		}
		else
			descricao = '';
		
		return descricao;
	}

	function removerItensListaTipoPermissao() {
    	$("#ulPermissoes").find("li").each(function(i){
            $(this).remove();
            $("#ulPermissoes")[0]["index"]--;
        });
    };

	function atualizaTipoPermissaoJson() {
		var listaTipoPermissaoListaVO = [];
    	
		// Percorre listas de TipoPermissao
    	$("#ulPermissoes").find("li").each(function(i){
            var jDivs=$(this).find("span");
            listaTipoPermissaoListaVO.push({
            	"idTipoPermissaoLista": jDivs[0].id,
            	"descrTipoPermissaoLista": jDivs[0].innerHTML
            });
        });

    	var tipoPermissaoJson = {
   			"listaTipoPermissaoListaVO": listaTipoPermissaoListaVO
   		};
   		return tipoPermissaoJson;
   	}

    function getListasPermissaoAsString() {
		var params = '';
	
		// Percorre listas de TipoPermissao
		$("#ulPermissoes").find("li").each(function(i){
	        var jDivs=$(this).find("span");
	        
	        // Atualiza a string serializada
        	params += '&permissao.tipoPermissaoSet[' + i + '].idTipoPermissaoLista=' + jDivs[0].id;
	    });
		return params;
    }
	
	$(function() {
		
		var jPermissoes = $("#ulPermissoes"),
		permissoes = jPermissoes[0],
        jDialog = $("#dialog"),
        dialog = jDialog[0],
        jTipoPermissao = $("#tipoPermissao"),
		jTipoPermissaoSelecionada = $("#itemTipoPermissao"),
		listaTipoPermissaoAux;
       
        $( "#ulPermissoes" ).sortable({placeholder: "ui-state-highlight"});
        $( "#ulPermissoes" ).disableSelection();
       
        $("#botaoIncluir").click(function(){
            if ($("#itemTipoPermissao")[0].options.length > 0)
            	jDialog.data('acao', permissoes.incluirItem).dialog('open');
            else
                alert("Não existem mais Tipos de Permissão para serem incluidos");
        });
       
        jDialog.dialog({
            autoOpen: false,
            height: 'auto',
            width: 'auto',
            modal: true,
            resizable: false,
            close: function() {
                    jDialog.data('tipoPermissao','');
            },
            open: function(){
                    jDialog.dialog('option', 'title', 'Incluir Tipo de Permissão');
                    jTipoPermissao.find("option[value=" + jDialog.data("tipoPermissao") + "]").prop('selected', true);
            }
        });
        $("#modalOk").click(function(){
            var acao = jDialog.data('acao');
            var jTipoPermissaoEscolhido = jTipoPermissaoSelecionada.find("option:selected");

            if (jTipoPermissaoEscolhido.val() != undefined && jTipoPermissaoEscolhido.val() != "") {
            	acao(jTipoPermissaoEscolhido.val(), jTipoPermissaoEscolhido.text(), jDialog.data("id"));
            	jTipoPermissaoSelecionada[0].remove(jTipoPermissaoSelecionada[0].selectedIndex);
            	jDialog.dialog('close');
            }
        });
        $("#modalCancel").click(function(){
            jDialog.dialog('close');
        });

        permissoes["index"] = 0;
        permissoes.incluirItem = function(idTipo, descrTipo, id){
            if (!id)
                id = 'novo_' + ++permissoes["index"];
            
            jPermissoes.append("<li class='permissao' style=\"cursor: move\" id =\"" + id + "\"></li>");
            var jNewTr = jPermissoes.find("li:last-child");
            jNewTr.append("<span style=\"display: inline-block\" id=\"" + idTipo + "\">" + descrTipo + "</span>");
            jNewTr.append("&nbsp;&nbsp;<img src=\"/siga/css/famfamfam/icons/delete.png\" style=\"visibility: hidden; cursor: pointer\" />");
            jNewTr.find("img:eq(0)").click(function(){
            	permissoes.removerItem(jNewTr.attr("id"));
            });
            jNewTr.mouseover(function(){
                jNewTr.find("img").css("visibility", "visible");
            });
            jNewTr.mouseout(function(){
                jNewTr.find("img").css("visibility", "hidden");
            });
        }
       
        permissoes.removerItem = function(idItem) {
			$('#'+idItem).remove();
            permissoes["index"]--;

            atualizarComboTipoPermissao();
        }

		$("[title='close']").click(function() {
			controleAcessoModalFechar();
		});
	});

	function copyItems(items) {
		var options = [];
		
		for (var i = 0; i < items.length; i++) {
			options.push(items[0]);
		}
	    return options;
	}
	
	function atualizarComboTipoPermissao() {
        var nodesPermissoesAdicionadas = $('.permissao span'),
    	tiposPermissao = $("#tiposPermissaoContainer").data('json'),
    	tiposPermissaoNode = $("#itemTipoPermissao"),
    	idsTiposPermissaoAdicionados = [];
	
	    // Remove todos os itens da lista.
		tiposPermissaoNode.find('option').remove();
		// Recupera os ids das permissoes adicionadas.
		nodesPermissoesAdicionadas.each(function() {
	    	var idTipoPermissao = $(this).attr('id');
	    	idsTiposPermissaoAdicionados.push(idTipoPermissao);
	   	});
		tiposPermissao.forEach(function(tipoPermissao) {
	    	if(idsTiposPermissaoAdicionados.indexOf(tipoPermissao.idTipoPermissaoLista.toString()) == -1) {
	        	var option = $('<option>');
	        	option.attr('value', tipoPermissao.idTipoPermissaoLista);
	        	option.html(tipoPermissao.descrTipoPermissaoLista);
	
	        	tiposPermissaoNode.append(option);
	       	}
	   	});
   	}

	function limparDadosListaModal() {
		if(permissoesTable) {
			permissoesTable.fnDestroy();
		}
		$('#permissoes_table').find('tbody tr').remove();
	}

	function populatePermissoesFromJSonList(permissoesJSon) {
		limparDadosListaModal();
		
		var table = $('#permissoes_table');
		
		for (var i = 0; i < permissoesJSon.length; i++) {
			var permissao = permissoesJSon[i],
				row = [ permissao.orgaoUsuario ? permissao.orgaoUsuario.id : ' ',
						permissao.orgaoUsuario ? permissao.orgaoUsuario.sigla : ' ', 
						permissao.complexo ? permissao.complexo.id : ' ',
						permissao.complexo ? permissao.complexo.descricao : ' ', 
						permissao.lotacao ? permissao.lotacao.id : ' ',
						permissao.lotacao ? permissao.lotacao.descricao : ' ', 
						permissao.lotacao ? permissao.lotacao.sigla : ' ', 
						permissao.dpPessoa ? permissao.dpPessoa.id : ' ',
						permissao.dpPessoa ? permissao.dpPessoa.descricao : ' ', 
						permissao.dpPessoa ? permissao.dpPessoa.sigla : ' ', 
						permissao.cargo ? permissao.cargo.id : ' ',
						permissao.cargo ? permissao.cargo.descricao : ' ', 
						permissao.cargo ? permissao.cargo.sigla : ' ',
						permissao.funcaoConfianca ? permissao.funcaoConfianca.id : ' ',
						permissao.funcaoConfianca ? permissao.funcaoConfianca.descricao : ' ', 
						permissao.funcaoConfianca ? permissao.funcaoConfianca.sigla : ' ',  
	          			JSON.stringify(permissao.listaTipoPermissaoListaVO),					//16
	          			permissao.idConfiguracao,												//17    
	          			' ',																	//18
	          			getDescricaoTipoPermissaoByLista(permissao.listaTipoPermissaoListaVO),	//19
	          			listaService.conteudoColunaAcao(permissao)
          			];
  			
			adicionarLinhaPermissao(table, row, permissao);
		}
		iniciarPermissaoDataTable();
	}

	function adicionarLinhaPermissao(table, row, permissao) {
		var tr = $('<tr>');
		
		for(var j = 0; j < row.length; j++) {
			tr.append('<td>' + row[j] + '</td>');
		}
		tr.data('json', permissao);
		table.append(tr);

		if (!permissao.ativo) {
			$('td', tr).addClass('item-desativado');
			$('td:last', tr).html(' ');
		}
	}
	
	function iniciarPermissaoDataTable() {
		/* Table initialization */
		if ( $.fn.dataTable.isDataTable( '#permissoes_table' ) ) {
			permissoesTable = $('#permissoes_table').dataTable();
		}
		else {
			permissoesTable = permissoesTable = $('#permissoes_table').dataTable({
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
					"targets": [colunas.idOrgao, colunas.idLocal, colunas.idLotacao, colunas.nomeLotacao, colunas.idPessoa, 
					            colunas.nomePessoa, colunas.idCargo, colunas.descrCargo, colunas.idFuncao, colunas.descrFuncao,
					            colunas.jSonTipoPerm, colunas.idTipoPerm, colunas.tipoPerm],
					"visible": false,
					"searchable": false
				},
				{
					"targets": [colunas.acoes],
					"sortable" : false,
					"searchable": false
				}]
			});

			$('#permissoes_table tbody').on('click', 'tr', function () {
				if (permissoesTable.api().row(this).data() != undefined) {
		        	permissoesTable.$('tr.selected').removeClass('selected');
		            $(this).addClass('selected');
		            
		            atualizarControleAcessoModal(permissoesTable.api().row(this).data(), $(this));
				    controleAcessoModalAbrir(true);
				}
			});
		}
	}

	function carregarPermissoes(id) {
        $.ajax({
        	type: "GET",
        	url: "@{Application.buscarPermissoesLista()}?idLista=" + id,
        	dataType: "text",
        	success: function(lista) {
        		var permissoesJSon = JSON.parse(lista);
        		populatePermissoesFromJSonList(permissoesJSon);
        	},
        	error: function(error) {
            	alert("Não foi possível carregar as Permissões desta Lista.");
        	}
       	});
    }

    $(document).ready(function() {
    	listaService.conteudoColunaAcao = function(permissao) {
    		if (permissao.ativo) { 		 
    			return '<a class="once desassociarPermissao" onclick="desativarPermissaoUsoListaEdicao(event, '+permissao.idConfiguracao+')" title="Remover permissão">' + 
    			'<input class="idPermissao" type="hidden" value="'+permissao.idConfiguracao+'}"/>' + 
    			'<img id="imgCancelar" src="/siga/css/famfamfam/icons/delete.png" style="margin-right: 5px;">' + 
    			'</a>'																	//20
    		}
    		return ' ';
    	}
    })
</script>
