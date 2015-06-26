#{extends 'main.html' /} #{set title:'Edição de atributo' /}

<script src="/sigasr/public/javascripts/detalhe-tabela.js"></script>
<script src="/sigasr/public/javascripts/jquery.validate.min.js"></script>
<script src="/sigasr/public/javascripts/language/messages_pt_BR.min.js"></script>

<div class="gt-form gt-content-box" style="width: 800px !important; max-width: 800px !important;">
	<div>
		<form id="acordoForm" enctype="multipart/form-data">
			<input type="hidden" name="id" id="id"> 
			<input type="hidden" name="hisIdIni" id="hisIdIni">
			<div class="gt-form-row gt-width-66">
				<label>Nome <span>*</span></label>
				<input type="text" name="nomeAcordo" size="60" required />
			</div>
			<div class="gt-form-row gt-width-66">
				<label>Descrição</label>
				<input type="text" name="descrAcordo" size="60" />
			</div>
			<div class="gt-form-row">
				<label>Par&acirc;metros</label>
				<ul id="parametrosAcordo" style="color: #365b6d">
				</ul>
				<input type="button" value="Incluir" id="botaoIncluir"
					class="gt-btn-small gt-btn-left" style="font-size: 10px;" />
			</div>

			<div class="container">
				<div class="title-table">
					<h3 style="padding-top: 25px;">Abrang&ecirc;ncia</h3>
				</div>
			</div>

			<div class="gt-content-box dataTables_div">
                <div class="gt-form-row dataTables_length">
                    <label>#{checkbox name:'mostrarAssocDesativada', value:mostrarAssocDesativada/} <b>Incluir Inativas</b></label>
                </div>        
				<table id="associacao_table" border="0" class="gt-table display">
					<thead>
						<tr>
							<th style="color: #333">
								<button type="button" class="bt-expandir" id="btnExpandirAssociacoes">
									<span id="iconeBotaoExpandirTodos">+</span>
								</button>
							</th>
							<th>ID Orgão</th>
							<th>Orgão</th>
							<th>ID Local</th>
							<th>Local</th>
							<th>Tipo Solicitante</th>
							<th>ID Solicitante</th>
							<th>Descr. Solicitante</th>
							<th>Solicitante</th>
							<th>ID Atendente</th>
							<th>Descr. Atendente</th>
							<th>Atendente</th>
							<th>ID Prioridade</th>
							<th>Prioridade</th>
							<th>idAssociacao</th>
							<th>Id Histórico Associação</th>
							<th></th>
							<th>JSon</th>
						</tr>
					</thead>
					<tbody>
						#{list items:abrangencias, as:'abrang'}
						<tr>
							<td class="gt-celula-nowrap details-control" style="text-align: center;">+</td>
							<td>${abrang.orgaoUsuario?.id}</td>
							<td>${abrang.orgaoUsuario?.acronimoOrgaoUsu}</td>
							<td>${abrang.complexo?.idComplexo}</td>
							<td>${abrang.complexo?.nomeComplexo}</td>
							<td>${abrang.tipoSolicitante }</td>
							<td>${abrang.solicitante?.id }</td>
							<td>${abrang.solicitante?.descricao }</td>
							<td>${abrang.solicitante?.sigla}</td>
							<td>${abrang.atendente?.lotacaoAtual?.id }</td>
							<td>${abrang.atendente?.lotacaoAtual?.nomeLotacao }</td>
							<td>${abrang.atendente?.lotacaoAtual?.siglaLotacao }</td>
							<td>${abrang.prioridade}</td>
							<td>${abrang.prioridade?.descPrioridade}</td>
							<td>${abrang.idConfiguracao}</td>
							<td>${abrang.hisIdIni}</td>
							<td class="gt-celula-nowrap"
								style="font-size: 13px; font-weight: bold; border-bottom: 1px solid #ccc !important; padding: 7px 10px;">
								<a class="once desassociar" onclick="desassociar(event, ${abrang?.idConfiguracao})"
									title="Remover permissão"> 
									<input class="idAssociacao" type="hidden" value="${abrang?.idConfiguracao}" /> 
									<img id="imgCancelar" src="/siga/css/famfamfam/icons/delete.png" style="margin-right: 5px;">
								</a>
							</td>
							<td>${abrang}</td>
						</tr>
						</a> #{/list}
					</tbody>
				</table>
			</div>
			<div class="gt-table-buttons">
				<a href="javascript: inserirAssociacao()" class="gt-btn-small gt-btn-left" style="font-size: 10px;">Incluir</a>
			</div>

			<div class="gt-form-row">
				<input type="button" value="Gravar" class="gt-btn-medium gt-btn-left" onclick="acordoService.gravar()" />
				<a class="gt-btn-medium gt-btn-left" onclick="acordoService.cancelarGravacao()">Cancelar</a>
				<input type="button" value="Aplicar" class="gt-btn-medium gt-btn-left" onclick="acordoService.aplicar()" />
			</div>
		</form>
	</div>
</div>


<div id="dialog">
	<div class="gt-content">
		<div class="gt-form gt-content-box">
			<form id="parametroForm" enctype="multipart/form-data">
				<div class="gt-form-row">
					<label>Par&acirc;metro</label> #{select name:'parametro',
					id:'parametro'} #{list items:parametros, as:'parametro'} #{option
					parametro.idAtributo} ${parametro.nomeAtributo} #{/option} #{/list}
					#{/select}
				</div>
				<div class="gt-form-row">
					<label>Valor <span>*</span></label> #{select id:'operador',
					name:'operador', items:models.SrOperador.values(),
					labelProperty:'nome' /} 
					<input type="text" id="valor" name="valor"
						value="" size="5" required /> 
					#{select name:'unidadeMedida',
					id:'unidadeMedida', items:unidadesMedida,
					valueProperty:'idUnidadeMedida', labelProperty:'plural'} #{/select}
				</div>
				<div class="gt-form-row">
					<input type="button" id="modalOk" value="Ok" class="gt-btn-medium gt-btn-left" />
					<input type="button" value="Cancelar" id="modalCancel" class="gt-btn-medium gt-btn-left" />
				</div>
			</form>
		</div>
	</div>
</div>

#{modal nome:'associacao', titulo:'Cadastrar Associação'}
<div class="gt-form gt-content-box" style="width: 800px !important; max-width: 800px !important;">
	<input id="idConfiguracao" type="hidden" name="idConfiguracao">
	<input id="hisIdIni" type="hidden" name="hisIdIni">

	<div id="divSolicitante" class="gt-form-row gt-width-100">
		<label>Solicitante</label> #{pessoaLotaFuncCargoSelecao
		nomeSelLotacao:'lotacao', nomeSelPessoa:'dpPessoa',
		nomeSelFuncao:'funcaoConfianca', nomeSelCargo:'cargo',
		nomeSelGrupo:'cpGrupo', valuePessoa:dpPessoa?.pessoaAtual,
		valueLotacao:lotacao?.lotacaoAtual, valueFuncao:funcaoConfianca,
		valueCargo:cargo, valueGrupo:cpGrupo, disabled:disabled /}
	</div>

	<div class="gt-form-row gt-width-100">
		<label>Órgão</label> #{select name:'orgaoUsuario', items:orgaos,
		valueProperty:'idOrgaoUsu', labelProperty:'acronimoOrgaoUsu',
		value:orgaoUsuario?.idOrgaoUsu, class:'select-siga', style:'width:
		100%;'} #{option 0}#{/option} #{/select}
	</div>

	<div class="gt-form-row gt-width-100">
		<label>Local</label> #{select name:'complexo', items:locais,
		valueProperty:'idComplexo', labelProperty:'nomeComplexo',
		value:complexo?.idComplexo, class:'select-siga', style:'width: 100%'}
		#{option 0}#{/option} #{/select}
	</div>
	<div class="gt-form-row gt-width-100">
		<label>Atendente</label>#{selecao tipo:'lotacao', nome:'atendente',
		value:atendente?.lotacaoAtual /}
	</div>

	<div class="gt-form-row gt-width-100">
		<label>Prioridade</label> #{select name:'prioridade',
		items:models.SrPrioridade.values(), labelProperty:'descPrioridade',
		style:'width:235px;' } #{option ''}#{/option} #{/select}
	</div>

	#{configuracaoItemAcao itemConfiguracaoSet:itemConfiguracaoSet,
						   acoesSet:acoesSet}#{/configuracaoItemAcao}
							 
	<div class="gt-form-row">
		<a href="javascript: gravarAssociacao()" class="gt-btn-medium gt-btn-left">Gravar</a>
		<a href="javascript: associacaoModalFechar()" class="gt-btn-medium gt-btn-left">Cancelar</a>
	</div>
</div>
<div class="gt-content-box" id="modal-associacao-error"
	style="display: none;">
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
#{/modal}

<script type="text/javascript">
	function getUrlDesativarReativar(desativados) {
	    
	    var url = '@{Application.buscarAbrangenciasAcordo()}',
	        idAcordo = $("[name=id]").val();
	
	    if(desativados)
	        url = '@{Application.buscarAbrangenciasAcordoDesativados()}';
	        
	    return url + "?id=" + idAcordo;
	}


	function findSelectedIndexByValue(comboBox, value) {
		for (var i = 0; i < comboBox.options.length; i++) {
			if (comboBox.options[i].value == value)
				return i;
		}
		
		return 0;
	};

	var tableAssociacao,
		colunasAssociacao = {};
	colunasAssociacao.botaoExpandir =               0 ;
	colunasAssociacao.idOrgao=						1 ;
	colunasAssociacao.orgao=						2 ;
	colunasAssociacao.idLocal =						3 ;
	colunasAssociacao.local=						4 ;
	colunasAssociacao.tipoSolicitante = 			5 ;
	colunasAssociacao.idSolicitante = 				6 ;
	colunasAssociacao.descricaoSolicitante =		7 ;
	colunasAssociacao.solicitante=					8 ;
	colunasAssociacao.idAtendente = 				9 ;
	colunasAssociacao.descricaoAtendente = 			10;
	colunasAssociacao.atendente=					11;
	colunasAssociacao.idPrioridade =				12;
	colunasAssociacao.prioridade=					13;
	colunasAssociacao.idAssociacao =                14;
	colunasAssociacao.hisIdIni = 					15;
	colunasAssociacao.botaoExcluir =                16;
	colunasAssociacao.jSon = 						17;

    jQuery("#checkmostrarAssocDesativada").click(function() {
    	tableAssociacao.api().clear().draw();
        
		$.ajax({
   	         type: "POST",
   	         url: getUrlDesativarReativar(document.getElementById('checkmostrarAssocDesativada').checked),
   	         dataType: "text",
	   	     success: function(response) {
	      		var listaJSon = JSON.parse(response);
	      		acordoService.populateFromJSonList(listaJSon, associacaoTable);
	      	 },
	      	 error: function(error) {
	          	alert("Não foi possível carregar as Abrangências deste Acordo.");
	      	 }
       });
    });
	var associacaoTable = new SigaTable('#associacao_table')
		.configurar("columnDefs", [{
			"targets": [colunasAssociacao.idOrgao, 
						colunasAssociacao.idLocal, 
						colunasAssociacao.tipoSolicitante, 
						colunasAssociacao.idSolicitante, 
						colunasAssociacao.descricaoSolicitante, 
						colunasAssociacao.idAtendente, 
						colunasAssociacao.idPrioridade, 
						colunasAssociacao.descricaoAtendente, 
						colunasAssociacao.idAssociacao,
						colunasAssociacao.hisIdIni,
						colunasAssociacao.jSon],
			"visible": false,
			"searchable": false
		},
		{
			"targets": [colunasAssociacao.botaoExpandir],
			"sortable": false,
			"searchable" : false
		}])
	.configurar("fnRowCallback", associacaoRowCallback)
	.configurar("iDisplayLength", 25)
	.criar()
	.detalhes(detalhesListaAssociacao);
	
	jQuery( document ).ready(function( $ ) {
        if (QueryString.mostrarDesativados != undefined) {
            document.getElementById('checkmostrarAssocDesativada').checked = QueryString.mostrarDesativados == 'true';
            document.getElementById('checkmostrarAssocDesativada').value = QueryString.mostrarDesativados == 'true';
        }

        $('#associacao_table tbody').on('click', 'tr', function () {
			var itemSelecionado = associacaoTable.dataTable.api().row(this).data();
			
			if (itemSelecionado != undefined) {
				associacaoTable.dataTable.$('tr.selected').removeClass('selected');
	            $(this).addClass('selected');
	            
				atualizarAssociacaoModal(itemSelecionado);
			    associacaoModalAbrir(true);
			}
		});
		
		tableAssociacao = associacaoTable.dataTable;
		$("#acordoForm").validate();
		$("#parametroForm").validate();
	});

	function associacaoRowCallback( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
		$('td:eq(' + colunasAssociacao.botaoExpandir + ')', nRow).addClass('details-control');
	}

	function inserirAssociacao() {
		var idAcordo = $("#id").val();
		if (idAcordo == ''){
			if (acordoService.aplicar() == false)
				return; 
		}
		
		limparDadosAssociacaoModal();
		associacaoModalAbrir(false);
	};

	function associacaoModalAbrir(isEdicao) {
		isEditing = isEdicao;
		
		if (isEdicao)
			jQuery("#associacao_dialog").dialog('option', 'title', 'Alterar Abrangencia');
		else {
			configuracaoItemAcaoService.atualizaDadosTabelaItemAcao({});
			jQuery("#associacao_dialog").dialog('option', 'title', 'Incluir Abrangencia');
		}
		jQuery("#associacao_dialog").dialog('open');
	};

	function detalhesListaAssociacao(itemArray) {
		var tr = $('<tr class="detail">'),
			td = $('<td colspan="6">'),
			trItens = $('<tr>'),
			trAcoes = $('<tr>'),
			table = $('<table class="datatable" cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">');

		if (itemArray && itemArray[colunasAssociacao.jSon]) {
			var associacao = itemArray[colunasAssociacao.jSon];
			
			TableHelper.detalheLista("<b>Itens de configuração:</b>", associacao.listaItemConfiguracaoVO, trItens);
			TableHelper.detalheLista("<b>Ações:</b>", associacao.listaAcaoVO, trAcoes);

			table.append(trItens);
			table.append(trAcoes);
		}
			
		td.append(table);
		tr.append(td);
	    
	    return tr;
	};

	// Limpa os dados da tela.
	function limparDadosAssociacaoModal() {
		unblock();

		$("#atendente").val('');
		$("#atendente_descricao").val('');
		$("#atendente_sigla").val('');
		$("#atendenteSpan").html('');
		$("#idConfiguracao").val('');
		$("#hisIdIni").val('');

		var jOrgaoUsuarioCbb = document.getElementsByName("orgaoUsuario")[0],
		jComplexoCbb = document.getElementsByName("complexo")[0];
		jPrioridadeCbb = document.getElementsByName("prioridade")[0];
		jPessoaLotaFuncCargoCbb = $("#dpPessoalotacaofuncaoConfiancacargocpGrupo")[0];
		
        jOrgaoUsuarioCbb.selectedIndex = 0;
		jComplexoCbb.selectedIndex = 0;
		jPrioridadeCbb.selectedIndex = 0;
		jPessoaLotaFuncCargoCbb.selectedIndex = 0;
		$("#dpPessoalotacaofuncaoConfiancacargocpGrupo")[0].onchange();
	}
	
	// Alimenta os campos do Popup antes de abrir ao usuário.
	function atualizarAssociacaoModal(itemArray) {
		limparDadosAssociacaoModal();

		// Atualiza campos Selecao
		$("#atendente").val(itemArray[colunasAssociacao.idAtendente]);
		$("#atendente_descricao").val(itemArray[colunasAssociacao.descricaoAtendente]);
		$("#atendente_sigla").val(itemArray[colunasAssociacao.atendente]);
		$("#atendenteSpan").html(itemArray[colunasAssociacao.descricaoAtendente]);
		$("#dpPessoa").val(itemArray[colunasAssociacao.idSolicitante]);
		$("#dpPessoa_sigla").val(itemArray[colunasAssociacao.solicitante]);
		$("#dpPessoaSpan").html(itemArray[colunasAssociacao.descricaoSolicitante]);
		$("#idConfiguracao").val(itemArray[colunasAssociacao.idAssociacao]);
		$("#hisIdIni").val(itemArray[colunasAssociacao.hisIdIni]);

		var jOrgaoUsuarioCbb = document.getElementsByName("orgaoUsuario")[0],
		jComplexoCbb = document.getElementsByName("complexo")[0];
		jPrioridadeCbb = document.getElementsByName("prioridade")[0];
		jPessoaLotaFuncCargoCbb = $("#dpPessoalotacaofuncaoConfiancacargocpGrupo")[0];
		
		jOrgaoUsuarioCbb.selectedIndex = findSelectedIndexByValue(jOrgaoUsuarioCbb, itemArray[colunasAssociacao.idOrgao]);
		jComplexoCbb.selectedIndex = findSelectedIndexByValue(jComplexoCbb, itemArray[colunasAssociacao.idLocal]);
		jPrioridadeCbb.selectedIndex = findSelectedIndexByValue(jPrioridadeCbb, itemArray[colunasAssociacao.idPrioridade]);
		jPessoaLotaFuncCargoCbb.selectedIndex = findSelectedIndexByValue(jPessoaLotaFuncCargoCbb, itemArray[colunasAssociacao.tipoSolicitante]);

		// atualiza os valores do componente pessoaLotaFuncCargoSelecao
		getIdFieldSolicitante(itemArray[colunasAssociacao.tipoSolicitante]).val(itemArray[colunasAssociacao.idSolicitante]);
		getDescricaoFieldSolicitante(itemArray[colunasAssociacao.tipoSolicitante]).val(itemArray[colunasAssociacao.descricaoSolicitante]);
        getSiglaFieldSolicitante(itemArray[colunasAssociacao.tipoSolicitante]).val(itemArray[colunasAssociacao.solicitante]);
        getSpanFieldSolicitante(itemArray[colunasAssociacao.tipoSolicitante]).html(itemArray[colunasAssociacao.descricaoSolicitante]);
        
        $("#dpPessoalotacaofuncaoConfiancacargocpGrupo")[0].onchange();

        // atualiza os dados da lista de Itens e Ações
        configuracaoItemAcaoService.atualizaDadosTabelaItemAcao(itemArray[colunasAssociacao.jSon]);
	}

	function transformStringToBoolean(value) {
		if (value.constructor.name == 'String')
			return value == 'true';
		else
			return value;
	}

	function getIdFieldSolicitante(tipo) {
		if (tipo == 1)
			return $("#dpPessoa");
		else if (tipo == 2)
			return $("#lotacao");
		else if (tipo == 3)
			return $("#funcaoConfianca");
		else if (tipo == 4)
			return $("#cargo");
		else 
			return $("#cpGrupo");
	};
	
	function getDescricaoFieldSolicitante(tipo) {
		if (tipo == 1)
			return $("#dpPessoa_descricao");
		else if (tipo == 2)
			return $("#lotacao_descricao");
		else if (tipo == 3)
			return $("#funcaoConfianca_descricao");
		else if (tipo == 4)

			return $("#cargo_descricao");
		else 
			return $("#cpGrupo_descricao");
	};
	
	function getSiglaFieldSolicitante(tipo) {
		if (tipo == 1)
			return $("#dpPessoa_sigla");
		else if (tipo == 2)
			return $("#lotacao_sigla");
		else if (tipo == 3)
			return $("#funcaoConfianca_sigla");
		else if (tipo == 4)

			return $("#cargo_sigla");
		else 
			return $("#cpGrupo_sigla");
	};
	
	function getSpanFieldSolicitante(tipo) {
		if (tipo == 1)
			return $("#dpPessoaSpan");
		else if (tipo == 2)
			return $("#lotacaoSpan");
		else if (tipo == 3)
			return $("#funcaoConfiancaSpan");
		else if (tipo == 4)
			return $("#cargoSpan");
		else
			return $("#cpGrupoSpan");

	};

	function serializeAssociacao(row) {
		var params = "";
		
		// caso exista algum item na tabela
		params += '&associacao.orgaoUsuario=' + row[colunasAssociacao.idOrgao];
		params += '&associacao.complexo=' + row[colunasAssociacao.idLocal];
		params += '&associacao.prioridade=' + row[colunasAssociacao.idPrioridade];
        params += '&associacao.atendente=' + row[colunasAssociacao.idAtendente];
        params += '&associacao.idConfiguracao=' + row[colunasAssociacao.idAssociacao];
        params += '&associacao.hisIdIni=' + row[colunasAssociacao.hisIdIni];
       	params += configuracaoItemAcaoService.getItemAcaoAsString('associacao');

       	// atualiza o solicitante
		params += getDadosSolicitante(row, i);

		if ($("#id").val() != undefined && $("#id").val() != '')
		params += '&associacao.acordo.idAcordo=' + $("#id").val();

		return params;
	};

	function getDadosSolicitante(rowValues, i) {
    	var params = '';
 
		if (rowValues[colunasAssociacao.tipoSolicitante] == 1){
             	params += '&associacao.dpPessoa=' + rowValues[colunasAssociacao.idSolicitante];
             	params += '&associacao.lotacao=';
             	params += '&associacao.funcaoConfianca=';
             	params += '&associacao.cargo=';
             	params += '&associacao.cpGrupo=';

		
		// caso seja lotação
		} else if (rowValues[colunasAssociacao.tipoSolicitante] == 2){
			params += '&associacao.lotacao=' + rowValues[colunasAssociacao.idSolicitante];
			params += '&associacao.dpPessoa=';
             	params += '&associacao.funcaoConfianca=';
             	params += '&associacao.cargo=';
             	params += '&associacao.cpGrupo=';

		
		// caso seja função
		} else if (rowValues[colunasAssociacao.tipoSolicitante] == 3){
			params += '&associacao.funcaoConfianca=' + rowValues[colunasAssociacao.idSolicitante];
			params += '&associacao.dpPessoa=';
             	params += '&associacao.lotacao=';
             	params += '&associacao.cargo=';
             	params += '&associacao.cpGrupo=';

		
		// caso seja cargo
		} else if (rowValues[colunasAssociacao.tipoSolicitante] == 4){
			params += '&associacao.cargo=' + rowValues[colunasAssociacao.idSolicitante];
			params += '&associacao.dpPessoa=';
             	params += '&associacao.funcaoConfianca=';
             	params += '&associacao.lotacao=';
             	params += '&associacao.cpGrupo=';

		// caso seja grupo
		} else if (rowValues[colunasAssociacao.tipoSolicitante] == 5){
			params += '&associacao.cpGrupo=' + rowValues[colunasAssociacao.idSolicitante];
			params += '&associacao.dpPessoa=';
     			params += '&associacao.funcaoConfianca=';
     			params += '&associacao.lotacao=';
     			params += '&associacao.cargo=';
		}
    	
    	return params;
    }

	function gravarAssociacao() {
		var idAssociacao = $("#idConfiguracao").val() != undefined ? $("#idConfiguracao").val() : '',
			hisIdIniAssociacao = $("#hisIdIni").val() != undefined ? $("#hisIdIni").val() : '';

		var jOrgaoUsuarioCbb = document.getElementsByName("orgaoUsuario")[0],
			jOrgaoUsuario = jOrgaoUsuarioCbb.options[jOrgaoUsuarioCbb.selectedIndex],
			jComplexoCbb = document.getElementsByName("complexo")[0],
			jComplexo = jComplexoCbb.options[jComplexoCbb.selectedIndex];
			jPrioridadeCbb = document.getElementsByName("prioridade")[0],
			jPrioridade = jPrioridadeCbb.options[jPrioridadeCbb.selectedIndex];
			jPessoaLotaFuncCargoCbb = $("#dpPessoalotacaofuncaoConfiancacargocpGrupo")[0],
			jPessoaLotaFuncCargo = jPessoaLotaFuncCargoCbb.options[jPessoaLotaFuncCargoCbb.selectedIndex];
		
		var row = [
					'+',                                                                    // colunasAssociacao.botaoExpandir
					jOrgaoUsuario.value > 0 ? jOrgaoUsuario.value : '',
					jOrgaoUsuario.value > 0 ? jOrgaoUsuario.text : '', 
	          		jComplexo.value > 0 ? jComplexo.value : '',
	          		jComplexo.value > 0 ? jComplexo.text : '',
	          		jPessoaLotaFuncCargo.value > 0 ? jPessoaLotaFuncCargo.value : '',
          			getIdFieldSolicitante(jPessoaLotaFuncCargo.value).val() > 0 ? getIdFieldSolicitante(jPessoaLotaFuncCargo.value).val() : '',
          			getDescricaoFieldSolicitante(jPessoaLotaFuncCargo.value).val(),
          			getSiglaFieldSolicitante(jPessoaLotaFuncCargo.value).val(),
	          		$("#atendente").val() > 0 ? $("#atendente").val() : '',
          			$("#atendente_descricao").val(),
          			$("#atendente_sigla").val(),
          			jPrioridade.value != '' ? jPrioridade.value : '',
	          		jPrioridade.value != '' ? jPrioridade.text : '',
					idAssociacao > 0 ? idAssociacao : '',															// colunasAssociacao.idAssociacao
					hisIdIniAssociacao > 0 ? hisIdIniAssociacao : '', 													// colunasAssociacao.hisIdIni
					'',                                                                     // colunasAssociacao.botaoExcluir
					''
	   			];

		$.ajax({
	         type: "POST",
	         url: "@{Application.gravarAbrangencia()}",
	         data: serializeAssociacao(row),
	         dataType: "text",
	         success: function(jSon) {
		        var associacao = JSON.parse(jSon); 
		        var html = 
					'<td class="gt-celula-nowrap" style="font-size: 13px; font-weight: bold; border-bottom: 1px solid #ccc !important; padding: 7px 10px;">' +
						'<a class="once desassociar" onclick="desassociar(event, '+ associacao.idConfiguracao + ')" title="Remover permissão">' +
							'<input class="idAssociacao" type="hidden" value="' + associacao.idConfiguracao + '}"/>' +
							'<img id="imgCancelar" src="/siga/css/famfamfam/icons/cancel_gray.png" style="margin-right: 5px;">' + 
						'</a>' +
					'</td>';

				row[colunasAssociacao.idAssociacao] = associacao.idConfiguracao;
		        row[colunasAssociacao.jSon] = associacao;						         
		        row[colunasAssociacao.botaoExcluir] = html;
		        
		        var trObject = isEditing ? tableAssociacao.api().row('.selected').data(row) : tableAssociacao.api().row.add(row).draw(),
				    tr = $(trObject.node());
			    
	          	atualizarComponenteDetalhes(tr);
	          	associacaoModalFechar();
	         },
	         error: function(response) {
	        	$('#modal-associacao').hide(); 

	        	var modalErro = $('#"modal-associacao-error"');
	        	modalErro.find("h3").html(response.responseText);
	        	modalErro.show(); 
	         }
       });
	}

	function atualizarComponenteDetalhes(tr) {
		tr.find('td').removeClass('item-desativado')
		tr.find('td:first').addClass('detail-control');
      	associacaoTable.detalhes(detalhesListaAssociacao);
	}
	function associacaoModalFechar() {
		isEditing = false;
		$("#associacao_dialog").dialog("close");
		limparDadosAssociacaoModal();
	}

	function formatDescricaoLonga(descricao) {
		if (descricao != null) {
			return descricao + " ...";
		}
		return descricao;
	}

	function desassociar(event, idAssociacaoDesativar) {
		event.stopPropagation();
		
		var me = $(this),
			tr = $(event.currentTarget).parent().parent()[0],
			row = associacaoTable.dataTable.api().row(tr).data(),
			idAssociacao = idAssociacaoDesativar ? idAssociacaoDesativar : row[colunasAssociacao.idAssociacao];
			idAcordo = $("#id").val(),
            mostrarDesativa = $("#mostrarAssocDesativada").val();
			
			$.ajax({
			     type: "POST",
			     url: '@{Application.desativarAbrangenciaEdicao()}?',
			     data: {idAcordo : idAcordo, idAssociacao : idAssociacao},
			     dataType: "text",
			     success: function(response) {
	                 if (mostrarDesativa == "true") {
	                     $('td', tr).addClass('item-desativado');
	                     $('td:last', tr).html(' ');

	                     $(tr).next('tr.detail').find('td').addClass('item-desativado');
	                 } else {
	                     associacaoTable.dataTable.api().row(tr).remove().draw();
	                 }
			     },
			     error: function(response) {
			    	var modalErro = $('#modal-error');
			    	modalErro.find("h3").html(response.responseText);
			    	modalErro.show(); 
			     }
			});
	}

	function serializeParametrosAcordo() {
		var params = '';
		
		$("#parametrosAcordo").find("li").each(function(i) {
			var jDivs=$(this).find("span");

			params += '&acordo.atributoAcordoSet[' + i + '].valor=' + jDivs[2].innerHTML;
		    params += '&acordo.atributoAcordoSet[' + i + '].atributo.idAtributo=' + jDivs[0].id;
		    params += '&acordo.atributoAcordoSet[' + i + '].unidadeMedida=' + jDivs[3].id;
		    params += '&acordo.atributoAcordoSet[' + i + '].operador=' + jDivs[1].id;

		    if (this.id.indexOf("novo_") < 1)
		    	params += '&acordo.atributoAcordoSet[' + i + '].idAtributoAcordo=' + this.id;
		});
		
		return params;
	}

	$(function() {
		var jParametrosAcordo = $("#parametrosAcordo"),
		parametrosAcordo = jParametrosAcordo[0],
		jDialog = $("#dialog"),
		dialog = jDialog[0],
		jValor = $("#valor"),
		jParametro = $("#parametro");
		jUnidadeMedida = $("#unidadeMedida");
		jOperador = $("#operador");
		
		$("#botaoIncluir").click(function(){
	        jDialog.data('acao',parametrosAcordo.incluirItem).dialog('open');
		});
		
		jDialog.dialog({
		        autoOpen: false,
		        height: 'auto',
		        width: 'auto',
		        modal: true,
		        resizable: false,
		        close: function() {
	                jValor.val('');
	                jDialog.data('valor','');
	                jDialog.data('parametro','');
	                jDialog.data('unidadeMedida','');
	                jDialog.data('operador','');
		        },
		        open: function(){
	                if (jDialog.data("valor"))
		                jDialog.dialog('option', 'title', 'Alterar Parametro');
	                else
		                jDialog.dialog('option', 'title', 'Incluir Parametro');
	                
	                jValor.val(jDialog.data("valor"));
	                jParametro.find("option[value=" + jDialog.data("parametro") + "]").prop('selected', true);
	                jUnidadeMedida.find("option[value=" + jDialog.data("unidadeMedida") + "]").prop('selected', true);
	                jOperador.find("option[value=" + jDialog.data("operador") + "]").prop('selected', true);
		        }
		});
		$("#modalOk").click(function(){
			if (!jQuery("#parametroForm").valid())
				return false;
		
	        var acao = jDialog.data('acao');
	        var jParametroEscolhido = jParametro.find("option:selected");
	        var jUnidadeEscolhida = jUnidadeMedida.find("option:selected");
	        var jOperadorEscolhido = jOperador.find("option:selected");
	        acao(jParametroEscolhido.val(), jParametroEscolhido.text(), jOperadorEscolhido.val(), jOperadorEscolhido.text(), jValor.val(), jUnidadeEscolhida.val(), jUnidadeEscolhida.text().trim(), jDialog.data("id"));
	        jDialog.dialog('close');
		});
		$("#modalCancel").click(function(){
			jDialog.dialog('close');
		});
		
		parametrosAcordo["index"] = 0;
		parametrosAcordo.incluirItem = function(idParametro, nomeParametro, idOperador, nomeOperador, valor, idUnidadeMedida, descrUnidadeMedida, id){
			if (!id)
				id = 'novo_' + ++parametrosAcordo["index"];
			jParametrosAcordo.append("<li style=\"cursor: move\" id =\"" + id + "\"></li>");
			var jNewTr = jParametrosAcordo.find("li:last-child");
			jNewTr.append("<span style=\"display: inline-block\" id=\"" + idParametro + "\">"
		        + nomeParametro + "</span>: <span id=\"" + idOperador + "\">" + nomeOperador + "</span> <span>" + valor + "</span> <span id=\"" + idUnidadeMedida + "\">" + descrUnidadeMedida + "</span>");                
		    jNewTr.append("&nbsp;&nbsp;<img src=\"/siga/css/famfamfam/icons/pencil.png\" style=\"visibility:hidden; cursor: pointer\" />");
		    jNewTr.append("&nbsp;<img src=\"/siga/css/famfamfam/icons/delete.png\" style=\"visibility: hidden; cursor: pointer\" />");
		    jNewTr.find("img:eq(0)").click(function(){
			    var jDivs=jNewTr.find("span");
			    jDialog.data("parametro",jDivs[0].id)
			    	.data("operador",jDivs[1].id)
               		.data("valor",jDivs[2].innerHTML)
               		.data("unidadeMedida",jDivs[3].id)
                    .data("id",id)
                    .data("acao", parametrosAcordo.alterarItem)
                    .dialog("open");
		        });
		        jNewTr.find("img:eq(1)").click(function(){
		            parametrosAcordo.removerItem(jNewTr.attr("id"));
		 	    });
		        jNewTr.mouseover(function(){
		            jNewTr.find("img").css("visibility", "visible");
		        });
		        jNewTr.mouseout(function(){
		            jNewTr.find("img").css("visibility", "hidden");
		        });
		}
		parametrosAcordo.alterarItem = function(idParametro, nomeParametro, idOperador, nomeOperador, valor, idUnidadeMedida, descrUnidadeMedida, id){
	        var jDivs=$("#"+id).find("span");
	        jDivs[0].id = idParametro;
	        jDivs[0].innerHTML = nomeParametro;
	        jDivs[1].id = idOperador;
	        jDivs[1].innerHTML = nomeOperador;
	        jDivs[2].innerHTML = valor;
	        jDivs[3].id = idUnidadeMedida;
	        jDivs[3].innerHTML = descrUnidadeMedida;
		}
		parametrosAcordo.removerItem = function(idItem){
	        $("#"+idItem).remove();
	        parametrosAcordo["index"]--;
		}
	});
</script>
