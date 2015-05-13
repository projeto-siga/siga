<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<%@ attribute name="orgaos" required="false"%>
<%@ attribute name="locais" required="false"%>
<%@ attribute name="unidadesMedida" required="false"%>
<%@ attribute name="pesquisaSatisfacao" required="false"%>
<%@ attribute name="listasPrioridade" required="false"%>
<%@ attribute name="designacoes" required="false"%>
<%@ attribute name="modoExibicao" required="false"%>
<%@ attribute name="mostrarDesativados" required="false"%>


<div class="gt-content">
	<!-- content bomex -->
	<div class="gt-content-box dataTables_div">
		<c:if test="${requestScope[modoExibicao] == 'designacao'}">
			<div class="gt-form-row dataTables_length">
				<label>
					<siga:checkbox name="mostrarDesativados" value="${requestScope[mostrarDesativados]}"></siga:checkbox>
					<b>Incluir Inativas</b>
				</label>
			</div>
		</c:if>

		<table id="designacoes_table" border="0" class="gt-table display">
			<thead>
				<tr>
					<th style="color: #333">
						<button class="bt-expandir" type="button">
							<span id="iconeBotaoExpandirTodos">+</span>
						</button>
					</th>
					<th>Org&atilde;o</th>
					<th>Local</th>
					<th>Solicitante</th>
					<th>Descri&ccedil;&atilde;o</th>
					<th>Atendente</th>
					<th>A&ccedil;&otilde;es</th>
					<th>JSon - Designa&ccedil;&atilde;o</th>
					<th>Checkbox Heran&ccedil;a</th>
					<th>Herdado</th>
					<th>Utilizar Herdado</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${requestScope[designacoes]}" var="design">
					<tr data-json-id="${design.id}" data-json='${design.toVO().toJson()}'
						onclick="designacaoService.editar($(this).data('json'), 'Alterar designacao')"
						style="cursor: pointer;">
						<td class="gt-celula-nowrap details-control" style="text-align: center;">+</td>
						<td>${design.orgaoUsuario != null ? design.orgaoUsuario.acronimoOrgaoUsu : ""}</td>
						<td>${design.complexo != null ? design.complexo.nomeComplexo : ""}</td>
						<td>${design.solicitante != null ? design.solicitante.sigla : "" }</td>
						<td>${design.descrConfiguracao}</td>
						<td>${design.atendente != null && design.atendente.lotacaoAtual != null ? design.atendente.lotacaoAtual.siglaLotacao : "" }</td>
						<td class="acoes"> 
							<siga:desativarReativar id="${design.id}" onReativar="designacaoService.reativar" onDesativar="designacaoService.desativar" isAtivo="${design.isAtivo()}"></siga:desativarReativar>
							<a class="once gt-btn-ativar" onclick="duplicarDesignacao(event)" title="Duplicar">
								<img src="/siga/css/famfamfam/icons/application_double.png" style="margin-right: 5px;"> 
							</a>
						</td>
						<td>${design.getSrConfiguracaoJson()}</td>
						<td class="checkbox-hidden"
							style="width: 25px !important; padding-left: 5px; padding-right: 5px;">
							<input type="checkbox" checked="${design.utilizarItemHerdado}"
							id="check${design.id}" />
						</td>
						<th>${design.herdado}</th>
						<th>${design.utilizarItemHerdado}</th>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<!-- /content box -->
	<div class="gt-table-buttons">
		<a onclick="designacaoService.cadastrar('Incluir Designa&ccedil;&atilde;o')" class="gt-btn-medium gt-btn-left">Incluir</a>
	</div>
</div>

<siga:modal nome="designacao" titulo="Cadastrar Designacao">
	<div id="divEditarDesignacaoItem">
	
		<style>
		#sortable ul {
		        height: 1.5em;
		        line-height: 1.2em;
		}
		
		.ui-state-highlight {
		        height: 1.5em;
		        line-height: 1.2em;
		}
		</style>
		<div class="gt-form gt-content-box" style="width: 800px !important; max-width: 800px !important;">
			<form id="formDesignacao">
				<input type="hidden" id="designacao" name="designacao" value="" />
				<input type="hidden" id="idConfiguracao" name="designacao.idConfiguracao" value="${idConfiguracao}" />
				<input type="hidden" id="hisIdIni" name="designacao.hisIdIni" value="${hisIdIni}" />
				<div>
					<div class="gt-form-row">
						<label>Descri&ccedil;&atilde;o <span>*</span></label>
						<input id="descrConfiguracao" type="text" name="designacao.descrConfiguracao" value="${descrConfiguracao}" maxlength="255" style="width: 791px;" required/>
						<span style="display:none;color: red" id="designacao.descrConfiguracao">Descri&ccedil;&atilde;o n&atilde;o informada.</span>
					</div>
					<div class="gt-form-row box-wrapper">
						<div id="divSolicitante" class="box box-left gt-width-50">
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
								disabled="disabled">
							</siga:pessoaLotaFuncCargoSelecao>
						</div>
						<div class="box gt-width-50">
							<label>&Oacute;rg&atilde;o</label>
							<siga:select id="orgaoUsuario" name="orgaoUsuario" list="orgaos" listKey="idOrgaoUsu" listValue="nmOrgaoUsu" value="${orgaoUsuario.idOrgaoUsu}" headerKey="0" headerValue="Nenhum"/>
						</div>
					</div>
					<input type="hidden" id="designacao.lotacao" name="designacao.lotacao" />
					<input type="hidden" id="designacao.dpPessoa" name="designacao.dpPessoa" />
					<input type="hidden" id="designacao.funcaoConfianca" name="designacao.funcaoConfianca" />
					<input type="hidden" id="designacao.cargo" name="designacao.cargo" />
					<input type="hidden" id="designacao.cpGrupo" name="designacao.cpGrupo" />
					<input type="hidden" id="designacao.complexo" name="designacao.complexo" />
					<input type="hidden" id="designacao.orgaoUsuario" name="designacao.orgaoUsuario" />
					<input type="hidden" name="designacao.atendente" id="designacao.atendente">
		
					<div class="gt-form-row box-wrapper">
						<div class="box box-left gt-width-50">
							<label>Local</label>
							<siga:select id="complexo" name="complexo" list="locais" listKey="idComplexo" listValue="nomeComplexo" value="${complexo.idComplexo}" headerKey="0" headerValue="Nenhum"/>
						</div>
						<div class="box gt-width-50">
							<label>Atendente <span>*</span></label>
							
							<input type="hidden" name="atendente" id="atendente" class="selecao">
							<siga:selecao propriedade="lotacao" tema="simple" modulo="siga" urlAcao="buscar" inputName="atendente" desativar="${requestScope[modoExibicao] == 'equipe' ? 'true' : disabled}"/>

		 
		<%-- 					#{selecao --%>
		<%-- 						tipo:'lotacao', nome:'atendente', value:atendente?.lotacaoAtual, --%>
		<%-- 						disabled:_modoExibicao == 'equipe' ? 'true' : disabled /} --%>
		
							<span style="display:none;color: red" id="designacao.atendente">Atendente n&atilde;o informado;</span>
						</div>
					</div>
					<siga:configuracaoItemAcao itemConfiguracaoSet="${itemConfiguracaoSet}" acoesSet="${acoesSet}"></siga:configuracaoItemAcao>
		
					<div class="gt-form-row">
						<div class="gt-form-row">
							<input type="button" value="Gravar" class="gt-btn-medium gt-btn-left" onclick="preparaObjeto();designacaoService.gravar()"/>
							<a class="gt-btn-medium gt-btn-left" onclick="designacaoService.cancelarGravacao()">Cancelar</a>
							<input type="button" value="Aplicar" class="gt-btn-medium gt-btn-left" onclick="designacaoService.aplicar()"/>
						</div>
					</div>
		
					<div class="gt-form-row gt-width-100">
						<p class="gt-error" style="display:none;" id="erroCamposObrigatorios">Alguns campos obrigat&oacute;rios n&atilde;o foram
							preenchidos.</p>
					</div>
				</div>
			</form>
		</div>
	</div>
</siga:modal>
		
<siga:modal nome="erroAoSalvar" titulo="Problemas ao Salvar" altura="200" largura="450">
	<div id="divProblemaAoSalvar" class="gt-form gt-content-box"
		style="text-align: justify;">
		<label style="padding-top: 5px;">Pelo menos um dos campos
			"Atendente", "Pr&eacute;-Atendente" ou "P&oacute;s-Atendente" precisa
			necess&aacute;riamente ser a mesma lota&cccedil;&atilde;o que foi selecionada na tela de
			equipe. Por favor verifique e tente novamente.</label>
	</div>
	<div class="gt-form-row" style="margin-left: 297px;">
		<a href="javascript: fecharModalErroAoSalvar()"
			class="gt-btn-medium gt-btn-left">OK</a>
	</div>
</siga:modal>

<script type="text/javascript">
	var parametroTela = '${requestScope[modoExibicao]}';
	
	var colunasDesignacao = {};
	colunasDesignacao.acaoExpandir=					0 ;
	colunasDesignacao.orgao=						1 ;
	colunasDesignacao.local=						2 ;
	colunasDesignacao.solicitante=					3 ;
	colunasDesignacao.descrConfiguracao = 			4 ;
	colunasDesignacao.atendente=					5 ;
	colunasDesignacao.acoes=						6 ;
	colunasDesignacao.jSonDesignacao= 				7 ;
	colunasDesignacao.checkBoxHeranca = 			8;
	colunasDesignacao.herdado= 						9;
	colunasDesignacao.utilizarHerdado= 				10;

	var preparaObjeto = function() {
		var solicitanteTypes = ["lotacao", "dpPessoa", "funcaoConfianca", "cargo", "cpGrupo"];
		
		solicitanteTypes.forEach(function(entry) {
			var inputName = entry + "Sel.id";
			var inputValue = $( "input[name='" + inputName + "']" ).val();
		    if(inputValue != "")
			    $("input[name='designacao." + entry + "']" ).val(inputValue);
		});

		var orgaoUsuarioValue = $('#orgaoUsuario').find(":selected").val();
		if(orgaoUsuarioValue != "") 
		    $("input[name='designacao.orgaoUsuario']").val(orgaoUsuarioValue);
			
		var complexoValue = $('#complexo').find(":selected").val();
		if(complexoValue != "")
		    $("input[name='designacao.complexo']").val(complexoValue);

		var atendenteValue =$( "input[name='atendenteSel.id']" ).val();
		if(atendenteValue != "") 
		    $("input[name='designacao.atendente']").val(atendenteValue);
	}
	
	designacaoOpts = {
		 urlDesativar : '${linkTo[DesignacaoController].desativar}',
		 urlReativar : '${linkTo[DesignacaoController].reativar}',
		 urlGravar : '${linkTo[DesignacaoController].gravar}',
		 dialogCadastro : jQuery('#designacao_dialog'),
		 tabelaRegistros : jQuery('#designacoes_table'),
		 objectName : 'designacao',
		 formCadastro : jQuery('#formDesignacao'),
		 mostrarDesativados : $('#checkmostrarDesativados').attr('checked') ? true : false,
		 colunas : colunasDesignacao.acoes,
	};	
	
	// Define a "classe" designacaoService
	function DesignacaoService(opts) {
		// super(opts)
		BaseService.call(this, opts);
	}
	// designacaoService extends BaseService
	DesignacaoService.prototype = Object.create(BaseService.prototype);

	var designacaoService = new DesignacaoService(designacaoOpts);
	// Sobescreve o metodo cadastrar para limpar a tela
	designacaoService.cadastrar = function(title) {
		BaseService.prototype.cadastrar.call(this, title);
		// atualiza os dados da DesignaÃ§Ã£o
		atualizarDesignacaoEdicao();
	}

	designacaoService.getId = function(designacao) {
		if (designacao)
			return designacao.idConfiguracao || designacao["designacao.idConfiguracao"];
		else
			return;
	}
	
	designacaoService.getRow = function(designacao) {
		return ['+',
				designacao.orgaoUsuario != null ? designacao.orgaoUsuario.sigla : ' ',
				designacao.complexo != null ? designacao.complexo.descricao : ' ',
				designacao.solicitante != null ? designacao.solicitante.sigla : ' ',
				designacao.descrConfiguracao,
				designacao.atendente != null ? designacao.atendente.sigla : '',
				'COLUNA_ACOES',
				JSON.stringify(designacao),
				' ',
				designacao.herdado,
				designacao.utilizarItemHerdado];
	}
	
	designacaoService.onRowClick = function(designacao) {
		designacaoService.editar(designacao, 'Alterar designacao');
	}
	/**
	 * Customiza o metodo editar
	 */
	designacaoService.editar = function(obj, title) {
		BaseService.prototype.editar.call(this, obj, title); // super.editar();
		// atualiza as listas
		atualizarDesignacaoEdicao(obj);
	}

	designacaoService.duplicarButton = '<a class="once gt-btn-ativar" onclick="duplicarDesignacao(event)" title="Duplicar"><img src="/siga/css/famfamfam/icons/application_double.png" style="margin-right: 5px;"></a>';

	designacaoService.serializar = function(obj) {
		return BaseService.prototype.serializar.call(this, obj)  + "&" + designacaoService.getListasAsString();
	}
	
	designacaoService.onGravar = function(obj, objSalvo) {
		var tr = BaseService.prototype.onGravar.call(this, obj, objSalvo);
		afterGravarDesignacao(tr, objSalvo, this);
		return tr;
	}

	designacaoService.isValidForm = function() {
	    return jQuery(this.opts.formCadastro).valid() && camposObrigatoriosPreenchidos();
	}

	designacaoService.desativar = function(event, id) {
		var ajax = BaseService.prototype.desativar.call(this, event, id),
			service = this;

		ajax.success().done(function(jSonDesignacao) {
			var design = JSON.parse(jSonDesignacao),
				tr = designacaoService.opts.tabelaRegistros.find("tr[data-json-id=" + service.getId(design) + "]");
			afterGravarDesignacao(tr, design, service);
		});
	}

	designacaoService.reativar = function(event, id) {
		var ajax = BaseService.prototype.reativar.call(this, event, id),
			service = this;

		ajax.success().done(function(jSonDesignacao) {
			var design = JSON.parse(jSonDesignacao),
				tr = designacaoService.opts.tabelaRegistros.find("tr[data-json-id=" + service.getId(design) + "]");
 			afterGravarDesignacao(tr, design, service);
		});
	}

	designacaoService.opts.formCadastro.resetForm = function(form) {
		$("#dpPessoalotacaofuncaoConfiancacargocpGrupo")[0].changeValue(1);
	}

	function designacaoRowCallback( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
		$('td:eq(' + colunasDesignacao.acaoExpandir + ')', nRow).addClass('details-control');
		var desig = undefined;
		
		try {
			desig = JSON.parse($(nRow).data('json'));
		}
		catch(err) {
			desig = $(nRow).data('json');
		}
		
		if (desig) {
			if (desig.ativo == false)
				$('td', nRow).addClass('item-desativado');
			else
				$('td', nRow).removeClass('item-desativado');
		}
	}

	function possuiValor(inputField) {
		return inputField.val() != '' && inputField.val() != undefined;
	}
	
	function camposObrigatoriosPreenchidos() {
		if (possuiValor($("#formulario_atendenteSel_id")))
			return true;
		else {
			alert("Por favor preencha pelo menos um dos seguintes campos: Atendente")
			return false;
		}
	}

	designacaoService.populateFromJSonList = function(listaJSon) {
		if(designacaoService.designacaoTable) {
			designacaoService.designacaoTable.destruir();
		}
		var table = designacaoOpts.tabelaRegistros;
		
		for (var i = 0; i < listaJSon.length; i++) {
			designacaoService.adicionarLinha(table, listaJSon[i]);
		}
		designacaoService.configurarDataTable();
	}

	designacaoService.configurarDataTable = function() {
		this.designacaoTable = new SigaTable('#designacoes_table')
		.configurar("columnDefs", [{
				"targets": [colunasDesignacao.checkBoxHeranca, 
							colunasDesignacao.jSonDesignacao,  
							colunasDesignacao.herdado, 
							colunasDesignacao.utilizarHerdado],
				"visible": false,
				"searchable": false
			},
			{
				"targets": [0],
				"sortable": false,
				"searchable" : false
			}])
		.configurar("fnRowCallback", designacaoRowCallback)
		.configurar("iDisplayLength", 100)
		.criar()
		.detalhes(detalhesDesignacao);
	
		this.opts.dataTable = designacaoService.designacaoTable.dataTable;
	}
	
	designacaoService.adicionarLinha = function(table, design) {
		var tr = TableHelper.criarTd(this.getRow(design));
		var tdAcoes = tr.find('td:nth(' + this.indiceAcoes(tr) + ')');
		tdAcoes.addClass('acoes');
		tr.find('td:first').addClass('details-control');
		tr.data('json', design);
		tr.data('json-id', design.idConfiguracao);
		tr.attr('data-json', JSON.stringify(design));
		tr.attr('data-json-id', design.idConfiguracao);

		tr.on('click', function() {
			var json = $(this).data('json');
			designacaoService.editar(json, 'Alterar designacao');
		});

		new DesativarReativar(this)
			.ativo(design.ativo)
			.innerHTML(tdAcoes, design.idConfiguracao);
		
		afterGravarDesignacao(tr, design, this);

		table.append(tr);
	}

	designacaoService.getListasAsString = function () {
		return configuracaoItemAcaoService.getItemAcaoAsString(designacaoService.opts.objectName);
    }

	function afterGravarDesignacao(tr, designacao, service) {
		var acoes = tr.find('td.acoes');

		// tratamentos de heranÃ§a e botÃ£o duplicar
		if (designacao && (designacao.herdado == 'true' || designacao.herdado == true))
			$('td', tr).addClass('configuracao-herdada');
		else
			acoes = acoes.append(" " + designacaoService.duplicarButton);
		
		// Para atualizar o detalhe caso esteja aberto
		if(designacaoService.designacaoTable)
			designacaoService.designacaoTable.atualizarDetalhes(service.getId(designacao));
	}

	function duplicarDesignacao(event) {
		var tr = $(event.currentTarget).parent().parent();
		event.stopPropagation();
		designacaoService.editar(tr.data('json'), 'Duplicar DesignaÃ§Ã£o');
		resetId();
	}

	function resetId() {
		$("#idConfiguracao").val('');
	}
	
	function detalhesDesignacao(d, designacao) {
		var tr = designacao.ativo == true ? $('<tr class="detail">') : $('<tr class="detail item-desativado">'),
			td = $('<td colspan="10">'),
			table  = $('<table style="margin-left: 60px;">'),
			trDescricao = $('<tr>'),
			tdDescricao = $('<td>'),
			trItens = $('<tr>'),
			trAcoes = $('<tr>'),
			descricao = designacao.descrConfiguracao != undefined ? designacao.descrConfiguracao : '';

		TableHelper.detalheLista("<b>Itens de configura&ccedil;&atilde;o:</b>", designacao.listaItemConfiguracaoVO, trItens);
		TableHelper.detalheLista("<b>A&ccedil;&otilde;es:</b>", designacao.listaAcaoVO, trAcoes);
		detalheDescricaoLista("<b>Descri&ccedil;&atilde;o:</b>", descricao, trDescricao);

		if (designacao.ativo == false) {
			$('td', trDescricao).addClass('item-desativado');
			$('td', trItens).addClass('item-desativado');
			$('td', trAcoes).addClass('item-desativado');
		}
				
		table.append(trDescricao);
		table.append(trItens);
		table.append(trAcoes);
		
		td.append(table);
		tr.append(td);
		return tr;
	}

	function detalheDescricaoLista(label, descricao, tr) {
		var tdTituloItem = $('<td colspan="2">' + label + '</td>'),
		    tdDadosItem = $('<td colspan="5">'),
		    table = $('<table>'),
		    item = descricao,
	    	trItem = $('<tr>'),
	    	tdDescricao = $('<td>');
	
		tdDescricao.html(item);
		trItem.append(tdDescricao);
		table.append(trItem);
		
		tdDadosItem.append(table);
		
		tr.append(tdTituloItem);
		tr.append(tdDadosItem);
	}

	function atualizarDesignacaoEdicao(designacaoJson) {
		if(!designacaoJson) {
			configuracaoItemAcaoService.iniciarDataTables();
		}else configuracaoItemAcaoService.atualizaDadosTabelaItemAcao(designacaoJson);

		if("${requestScope[modoExibicao]}" == "equipe") {
			// Caso seja cadastro a partir da tela de equipe, atualiza os dados da LotaÃ§Ã£o atendente
			var lota = JSON.parse($("#lotacaoUsuario").val());

			if(!designacaoJson) {
				designacaoJson = {};
			}
			designacaoJson.atendente = lota.id,
			designacaoJson.atendente_descricao = lota.descricao,
			designacaoJson.atendenteSpan = lota.descricao,
			designacaoJson.atendente_sigla = lota.sigla
		
			// chama o editar para popular o campo da lotaÃ§Ã£o
			designacaoService.formularioHelper.populateFromJson(designacaoJson);
		}
	}
	
	function designacaoModalFechar() {
		isEditing = false;
		$("#designacao_dialog").dialog("close");
	};
	
	function fecharModalErroAoSalvar() {
		$("#erroAoSalvar_dialog").dialog("close");
	}
</script>