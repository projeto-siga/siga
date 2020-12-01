<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<div>
	<form id="acordoForm" enctype="multipart/form-data">
		
		<input type="hidden" name="acordo.id" id="id" value="${idAcordo}"> 
		<input type="hidden" name="acordo.hisIdIni" id="hisIdIni" value="${hisIdIni}">
		
		<div class="form-group">
			<label>Nome <span>*</span></label>
			 <input type="text"	name="acordo.nomeAcordo"
				id="nomeAcordo"	value="${nomeAcordo}"
				class="form-control" 
				maxlength="255" required/>
		</div>
		<div class="form-group">
			<label>Descri&ccedil;&atilde;o</label>
			<input maxlength="255" type="text"
				name="acordo.descrAcordo" id="descrAcordo"
				class="form-control"
				value="${descrAcordo}" />
		</div>
		
		<div class="form-group">
			<label>Par&acirc;metros</label>
			<ul id="parametrosAcordo" style="color: #365b6d">
			</ul>
			<input type="button" value="Incluir" id="botaoIncluir"
				class="btn btn-primary btn-sm" />
		</div>
		
		<div class="card mb-2">
			<h5 class="card-header">Abrang&ecirc;ncia</h5>
			<div class="card-body">
               	<label>
                   <siga:checkbox name="mostrarAssocDesativada" value="${requestScope[mostrarAssocDesativada]}"></siga:checkbox>
                   <b>Incluir Inativas</b>
               	</label>
               
               <div class="table-responsive mb-2">
	               <table id="associacao_table" class="table">
					<thead>
						<tr>
							<th style="color: #333">
								<button type="button" class="bt-expandir" id="btnExpandirAssociacoes">
									<span id="iconeBotaoExpandirTodos">+</span>
								</button>
							</th>
							<th>ID Org&atilde;o</th>
							<th>Org&atilde;o</th>
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
							<th>Id Hist&oacute;rico Associa&ccedil;&atilde;o</th>
							<th></th>
							<th>JSon</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${requestScope[abrangencias]}" var="abrang">
							<tr>
								<td class="gt-celula-nowrap details-control" style="text-align: center;">+</td>
								<td>${abrang.orgaoUsuario.id}</td>
								<td>${abrang.orgaoUsuario.acronimoOrgaoUsu}</td>
								<td>${abrang.complexo.idComplexo}</td>
								<td>${abrang.complexo.nomeComplexo}</td>
								<td>${abrang.tipoSolicitante }</td>
								<td>${abrang.solicitante.id }</td>
								<td>${abrang.solicitante.descricao }</td>
								<td>${abrang.solicitante.sigla}</td>
								<td>${abrang.atendente.lotacaoAtual.id }</td>
								<td>${abrang.atendente.lotacaoAtual.nomeLotacao }</td>
								<td>${abrang.atendente.lotacaoAtual.siglaLotacao }</td>
								<td>${abrang.prioridade}</td>
								<td>${abrang.prioridade.descPrioridade}</td>
								<td>${abrang.idConfiguracao}</td>
								<td>${abrang.hisIdIni}</td>
								<td class="gt-celula-nowrap"
									style="font-size: 13px; font-weight: bold; border-bottom: 1px solid #ccc !important; padding: 7px 10px;">
									<a class="once desassociar" onclick="desassociar(event, ${abrang.idConfiguracao})"
										title="Remover permiss&atilde;o"> 
										<input class="idAssociacao" type="hidden" value="${abrang.idConfiguracao}" /> 
										<img id="imgCancelar" src="/siga/css/famfamfam/icons/delete.png" style="margin-right: 5px;">
									</a>
								</td>
								<td>${abrang}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
	               
               </div>    
               
				<!-- Botao de Incluir -->
				<a href="javascript: inserirAssociacao()" class="btn btn-primary" style="color: #fff">Incluir</a>
                   
			</div>
			
		</div>


		<input type="button" value="Gravar" class="btn btn-primary" onclick="preparaObjeto();acordoService.gravar()" />
		<a class="btn btn-secondary" style="color: #fff" onclick="acordoService.cancelarGravacao()">Cancelar</a>
		<input type="button" value="Aplicar" class="btn btn-primary" onclick="preparaObjeto();acordoService.aplicar()" />
	</form>
</div>

<!-- ==================== -->
<!-- Modal de Parametros  -->
<!-- ==================== -->
<div id="dialog">
	<form id="parametroForm" enctype="multipart/form-data">
		<div class="form-group">
			<label>Par&acirc;metro</label> 
			<select id="parametro" name="parametro" class="form-control">
				<c:forEach items="${parametros}" var="parametro">
					<option value="${parametro}">${parametro.descricao}</option>
				</c:forEach>
			</select>
		</div>
		<div class="form-group mb-2">
			<label>Valor *</label>
			
			<div class="form-group row">
				<div class="col-sm-5">
					<select id="operador" name="operador" class="form-control">
						<c:forEach items="${operadores}" var="ope">
							<option value="${ope}">${ope.nome}</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-sm-3">
					<input type="text" id="valor" name="valor" value="" maxlength="4" class="form-control" required />
				</div>
				<div class="col-sm-4">
					<select id="unidadeMedida" name="unidadeMedida" class="form-control">
						<c:forEach items="${unidadesMedida}" var="unidadeMedida">
							<option value="${unidadeMedida.idUnidadeMedida}">${unidadeMedida.plural}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
		</div>
		<input type="button" id="modalOk" value="Ok" class="btn btn-primary" />
		<input type="button" value="Cancelar" id="modalCancel" class="btn btn-secondary" />
	</form>
</div>


<!-- ================== -->
<!-- Modal Abrangencia  -->
<!-- ================== -->
<sigasr:modal nome="associacao" titulo="Cadastrar Associa&ccedil;&atilde;o" largura="80%">
	<div class="gt-form gt-content-box">
		<input id="idConfiguracao" type="hidden" name="idConfiguracao">
		<input type="hidden" name="associacao">
		<input id="hisIdIni" type="hidden" name="hisIdIni">
	
		<div id="divSolicitante" class="form-group">
			<label>Solicitante</label> 
			<sigasr:pessoaLotaFuncCargoSelecao
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
			</sigasr:pessoaLotaFuncCargoSelecao>
			
			
		</div>
		<div class="form-group">
			<label>&Oacute;rg&atilde;o</label> 
			<siga:select name="orgaoUsuario" list="orgaos"
				listKey="idOrgaoUsu" id="orgaoUsuario"
				headerValue="Nenhum" headerKey=""
				listValue="nmOrgaoUsu"
				value="${idOrgaoUsu}" />
		</div>
		<div class="form-group">
			<label>Local</label> 
			<siga:select name="complexo" list="locais"
				listKey="idComplexo" id="complexo"
				headerValue="Nenhum" headerKey=""
				listValue="nomeComplexo" theme="simple"
				value="${idComplexo}" />
		</div>
		<div class="form-group">
			<label>Atendente</label>
				<input type="hidden" name="atendente.id" id="atendente" class="selecao">
				<siga:selecao propriedade="atendente" tema="simple" modulo="siga" urlAcao="buscar" inputName="atendente"/>
		</div>

		<div class="form-group">
			<label>Prioridade</label> 
		    <select name="prioridade" id="prioridade" class="form-control">
				<option value=""></option>
				<c:forEach items="${prioridades}" var="item">
				   <option value="${item}">${item.descPrioridade}</option>
				</c:forEach>
		    </select>
		</div>

		<sigasr:configuracaoItemAcao itemConfiguracaoSet="${itemConfiguracaoSet}" acoesSet="${acoesSet}"></sigasr:configuracaoItemAcao>
								 
		<a href="javascript: gravarAssociacao()" class="btn btn-primary" style="color: #fff">Gravar</a>
		<a href="javascript: associacaoModalFechar()" class="btn btn-secondary" style="color: #fff">Cancelar</a>
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
</sigasr:modal>

<script type="text/javascript">
	function getUrlDesativarReativar(desativados) {
	    var url = '${linkTo[AcordoController].buscarAbrangenciasAcordo}';
	    var idAcordo = $("[id=id]").val();
	    var exibirInativo = "";
	
	    if(desativados)
	    	exibirInativo = "&exibirInativos=true";
	        
	    return url + "?id=" + idAcordo + exibirInativo;
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

	var preparaObjeto = function() {
		var solicitanteTypes = ["lotacao", "dpPessoa", "funcaoConfianca", "cargo", "cpGrupo"];
		
		solicitanteTypes.forEach(function(entry) {
			var inputName = entry + "Sel.id";
			var inputValue = $( "input[name='" + inputName + "']" ).val();
		    $("input[name='associacao." + entry + ".id']" ).val(inputValue);
		});

		var orgaoUsuarioValue = $('#orgaoUsuario').find(":selected").val();
		$("input[name='associacao.orgaoUsuario']").val(orgaoUsuarioValue);
			
		var complexoValue = $('#complexo').find(":selected").val();
		$("input[name='associacao.complexo']").val(complexoValue);

		var atendenteValue =$( "input[name='atendenteSel.id']" ).val();
		$("input[name='associacao.atendente']").val(atendenteValue);
	}
	
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
	          	alert("NÃ£o foi possÃ­vel carregar as AbrangÃªncias deste Acordo.");
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
	.criar()
	.detalhes(detalhesListaAssociacao);


	var validatorAcordoForm;
	
	jQuery( document ).ready(function( $ ) {
		validatorAcordoForm = $("#acordoForm").validate({
			onfocusout: false
		});
		
        if (QueryString.mostrarDesativados != undefined) {
            document.getElementById('checkmostrarAssocDesativada').checked = QueryString.mostrarDesativados == 'true';
            document.getElementById('checkmostrarAssocDesativada').value = QueryString.mostrarDesativados == 'true';
        }

		tableAssociacao = associacaoTable.dataTable;

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
			jQuery("#associacao_dialog").dialog('option', 'title', 'Alterar Abrang\u00eancia');
		else {
			configuracaoItemAcaoService.atualizaDadosTabelaItemAcao({});
			jQuery("#associacao_dialog").dialog('option', 'title', 'Incluir Abrang\u00eancia');
		}
		jQuery("#associacao_dialog").dialog('open');
	};

	function detalhesListaAssociacao(itemArray, associacao) {
		var tr = $('<tr class="detail">'),
			td = $('<td colspan="6">'),
			trItens = $('<tr>'),
			trAcoes = $('<tr>'),
			table = $('<table class="datatable" cellpadding="5" cellspacing="0" style="padding-left:50px;">');

		if (associacao) {
			TableHelper.detalheLista("<b>Itens de configura&ccedil;&atilde;o:</b>", associacao.listaItemConfiguracaoVO || [], trItens);
			TableHelper.detalheLista("<b>A&ccedil;&otilde;es:</b>", associacao.listaAcaoVO || [], trAcoes);

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

		//limpa o atendente
		$("#formulario_atendenteSel_id").val('');
		$("#formulario_atendenteSel_descricao").val('');
		$("#formulario_atendenteSel_sigla").val('');
		$("#atendenteSelSpan").html('');

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
		//limpa o solicitante
		$("#dpPessoalotacaofuncaoConfiancacargocpGrupo")[0].clearAll();
		$("#dpPessoalotacaofuncaoConfiancacargocpGrupo")[0].onchange();
	}
	
	// Alimenta os campos do Popup antes de abrir ao usuÃÂ¡rio.
	function atualizarAssociacaoModal(itemArray, associacao) {
		limparDadosAssociacaoModal();

		// Atualiza campos Selecao
		$("#formulario_atendenteSel_id").val(itemArray[colunasAssociacao.idAtendente]);
		$("#formulario_atendenteSel_descricao").val(itemArray[colunasAssociacao.descricaoAtendente]);
		$("#formulario_atendenteSel_sigla").val(itemArray[colunasAssociacao.atendente]);
		$("#atendenteSelSpan").html(itemArray[colunasAssociacao.descricaoAtendente]);
		$("#idConfiguracao").val(itemArray[colunasAssociacao.idAssociacao]);
		$("#hisIdIni").val(itemArray[colunasAssociacao.hisIdIni]);
		
		preencheDadosSolicitante(associacao.solicitante);

		var jOrgaoUsuarioCbb = document.getElementsByName("orgaoUsuario")[0],
		jComplexoCbb = document.getElementsByName("complexo")[0];
		jPrioridadeCbb = document.getElementsByName("prioridade")[0];
		jPessoaLotaFuncCargoCbb = $("#dpPessoalotacaofuncaoConfiancacargocpGrupo")[0];
		
		jOrgaoUsuarioCbb.selectedIndex = findSelectedIndexByValue(jOrgaoUsuarioCbb, itemArray[colunasAssociacao.idOrgao]);
		jComplexoCbb.selectedIndex = findSelectedIndexByValue(jComplexoCbb, itemArray[colunasAssociacao.idLocal]);
		jPrioridadeCbb.selectedIndex = findSelectedIndexByValue(jPrioridadeCbb, itemArray[colunasAssociacao.idPrioridade]);
		jPessoaLotaFuncCargoCbb.selectedIndex = findSelectedIndexByValue(jPessoaLotaFuncCargoCbb, itemArray[colunasAssociacao.tipoSolicitante]);

		// atualiza os valores do componente pessoaLotaFuncCargoSelecao
		if (associacao.solicitante != null)
			$("#dpPessoalotacaofuncaoConfiancacargocpGrupo")[0].changeValue(associacao.solicitante.tipo);

		getIdFieldSolicitante(itemArray[colunasAssociacao.tipoSolicitante]).val(itemArray[colunasAssociacao.idSolicitante]);
		getDescricaoFieldSolicitante(itemArray[colunasAssociacao.tipoSolicitante]).val(itemArray[colunasAssociacao.descricaoSolicitante]);
        getSiglaFieldSolicitante(itemArray[colunasAssociacao.tipoSolicitante]).val(itemArray[colunasAssociacao.solicitante]);
        getSpanFieldSolicitante(itemArray[colunasAssociacao.tipoSolicitante]).html(itemArray[colunasAssociacao.descricaoSolicitante]);

        // atualiza os dados da lista de Itens e AÃ§Ãµes
        configuracaoItemAcaoService.atualizaDadosTabelaItemAcao(associacao);
	}

	function preencheDadosSolicitante(solicitante) {
		var tipos = ["dpPessoa", "lotacao", "funcaoConfianca", "cargo", "cpGrupo"];
		var tipo = "";
		
		try {
			tipo = tipos[solicitante.tipo - 1];
		} catch(err) {
			tipo = 0;
		}


		if(solicitante != null) {
			$("#formulario_" + tipo + "Sel_id").val(solicitante.id);
			$("#formulario_" + tipo + "Sel_descricao").val(solicitante.descricao);
			$("#formulario_" + tipo + "Sel_sigla").val(solicitante.sigla);
			$("#formulario_" + tipo + "Sel_buscar").val(solicitante.buscar);
			$("#"+ tipo + "SelSpan").html(solicitante.descricao);
		}
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

		params += '&associacao.orgaoUsuario.id=' + row[colunasAssociacao.idOrgao];
			
		params += '&associacao.complexo.id=' + row[colunasAssociacao.idLocal];

		params += '&associacao.prioridade.id=' + row[colunasAssociacao.idPrioridade];

		// caso exista algum item na tabela
        params += '&associacao.id=' + row[colunasAssociacao.idAssociacao];
        params += '&associacao.hisIdIni=' + row[colunasAssociacao.hisIdIni];
       	params += configuracaoItemAcaoService.getItemAcaoAsString('associacao');

		var atendenteValue =$( "input[name='atendenteSel.id']" ).val();
		params += '&associacao.atendente.id=' + $('#formulario_atendenteSel_id').val();
       	
       	// atualiza o solicitante
		params += getDadosSolicitante(row);
		
		if ($("#id").val() != undefined && $("#id").val() != '')
		params += '&associacaoAcordoId=' + $("#id").val();

		return params;
	};

	function getDadosSolicitanteSel() {
		var params = "";

		var solicitante = getSolicitante();
		params += '&associacao.' + solicitante.name + '.id=' + solicitante.id;

		return params;
	};

	function endsWith(src, s) {
		return src.length >= s.length && src.substr(src.length - s.length) == s;
	}

	function getSolicitante() {
		var solicitante = {"name": '', "id":'', "descricao": '', "sigla": ''};
		$('#divSolicitante span').each(function(i,span){
			$(span).find('input').filter(function(i, input){
			    if($(input).attr('name') !== undefined){
			      
			    	if(!solicitante.name && endsWith($(input).attr('name'),'descricao'))
			       		solicitante.descricao = $(input).val();
			     
			     	if(!solicitante.sigla && endsWith($(input).attr('name'),'sigla'))
			      		solicitante.sigla = $(input).val();
			    
			      	if(!solicitante.id && endsWith($(input).attr('name'),'id')) {
			        	solicitante.name = $(input).closest('span').find(".pessoaLotaFuncCargoSelecao").attr('name');
			        	solicitante.id = $(input).val();
			      	}
			    }
			});
		});

		return solicitante;
	}
	
	function getDadosSolicitante(rowValues, i) {
    	var params = '';
 
		if (rowValues[colunasAssociacao.tipoSolicitante] == 1){
             	params += '&associacao.dpPessoa.id=' + rowValues[colunasAssociacao.idSolicitante];
             	params += '&associacao.lotacao.id=';
             	params += '&associacao.funcaoConfianca.id=';
             	params += '&associacao.cargo.id=';
             	params += '&associacaoCpGrupoId=';

		
		// caso seja lotaÃ§Ã£o
		} else if (rowValues[colunasAssociacao.tipoSolicitante] == 2){
			params += '&associacao.lotacao.id=' + rowValues[colunasAssociacao.idSolicitante];
			params += '&associacao.dpPessoa.id=';
             	params += '&associacao.funcaoConfianca.id=';
             	params += '&associacao.cargo.id=';
             	params += '&associacaoCpGrupoId=';

		
		// caso seja funÃ§Ã£o
		} else if (rowValues[colunasAssociacao.tipoSolicitante] == 3){
			params += '&associacao.funcaoConfianca.id=' + rowValues[colunasAssociacao.idSolicitante];
			params += '&associacao.dpPessoa.id=';
             	params += '&associacao.lotacao.id=';
             	params += '&associacao.cargo.id=';
             	params += '&associacaoCpGrupoId=';

		
		// caso seja cargo
		} else if (rowValues[colunasAssociacao.tipoSolicitante] == 4){
			params += '&associacao.cargo.id=' + rowValues[colunasAssociacao.idSolicitante];
			params += '&associacao.dpPessoa.id=';
             	params += '&associacao.funcaoConfianca.id=';
             	params += '&associacao.lotacao.id=';
             	params += '&associacaoCpGrupoId=';

		// caso seja grupo
		} else if (rowValues[colunasAssociacao.tipoSolicitante] == 5){
			params += '&associacaoCpGrupoId=' + rowValues[colunasAssociacao.idSolicitante];
			params += '&associacao.dpPessoa.id=';
     			params += '&associacao.funcaoConfianca.id=';
     			params += '&associacao.lotacao.id=';
     			params += '&associacao.cargo.id=';
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
		var solicitante = getSolicitante();

		var row = [
					'+',                                                                    // colunasAssociacao.botaoExpandir
					jOrgaoUsuario.value > 0 ? jOrgaoUsuario.value : '',
					jOrgaoUsuario.value > 0 ? jOrgaoUsuario.text : '', 
	          		jComplexo.value > 0 ? jComplexo.value : '',
	          		jComplexo.value > 0 ? jComplexo.text : '',
	          		jPessoaLotaFuncCargo.value > 0 ? jPessoaLotaFuncCargo.value : '',
	          		solicitante.id,
	          		solicitante.descricao,
	          		solicitante.sigla,
					$("#formulario_atendenteSel_id").val() > 0 ? $("#formulario_atendenteSel_id").val() : '',
          			$("#formulario_atendenteSel_descricao").val(),
          			$("#formulario_atendenteSel_sigla").val(),
          			jPrioridade.value != '' ? jPrioridade.value : '',
	          		jPrioridade.value != '' ? jPrioridade.text : '',
					idAssociacao > 0 ? idAssociacao : '',															// colunasAssociacao.idAssociacao
					hisIdIniAssociacao > 0 ? hisIdIniAssociacao : '', 													// colunasAssociacao.hisIdIni
					'',                                                                     // colunasAssociacao.botaoExcluir
					''
	   			];

		$.ajax({
	         type: "POST",
	         url: "${linkTo[AcordoController].gravarAbrangencia}",
	         data: serializeAssociacao(row),
	         dataType: "text",
				"beforeSend": function () {
					jQuery.blockUI(objBlock);
				},
				"complete": function () {
					jQuery.unblockUI();
				},
	         success: function(jSon) {
		        var associacao = JSON.parse(jSon); 

				row[colunasAssociacao.idAssociacao] = associacao.idConfiguracao;
		        row[colunasAssociacao.jSon] = associacao;						         
		        var trObject = isEditing ? tableAssociacao.api().row('.selected').data(row) : tableAssociacao.api().row.add(row).draw(),
				    tr = $(trObject.node());
			    
	          	atualizarComponenteDetalhes(tr);
	          	acordoService.adicionarFuncionalidadesNaLinhaDeAssociacao(tr, associacao, row);
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
			     url: "${linkTo[AcordoController].desativarAbrangenciaEdicao}?idAcordo=" + idAcordo + "&idAssociacao=" + idAssociacao,
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

			params += '&acordo.parametroAcordoSet[' + i + '].valor=' + jDivs[2].innerHTML;
		    params += '&acordo.parametroAcordoSet[' + i + '].parametro=' + jDivs[0].id;
		    params += '&acordo.parametroAcordoSet[' + i + '].unidadeMedida[' + i + '].id=' + jDivs[3].id;
		    params += '&acordo.parametroAcordoSet[' + i + '].operador=' + jDivs[1].id;

		    if (this.id.indexOf("novo_") < 0)
		    	params += '&parametroAcordoSet[' + i + '].id=' + this.id;
		});

		return params;
	}

	var jDialog = $("#dialog");
	var jParametro = $("#parametro");
	var jValor = $("#valor");
	var jUnidadeMedida = $("#unidadeMedida");
	var jOperador = $("#operador");

	jDialog.dialog({
        autoOpen: false,
        height: 'auto',
        width: '80%',
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
	        limparErros();
	        
        	jParametro.on('change', function() {
    			limparErros();
    		});
	        
            if (jDialog.data("valor")){
                jDialog.dialog('option', 'title', 'Alterar Parametro');
                jValor.val(jDialog.data("valor"));
                jParametro.find("option[value=" + jDialog.data("parametro") + "]").prop('selected', true);
                jUnidadeMedida.find("option[value=" + jDialog.data("unidadeMedida") + "]").prop('selected', true);
                jOperador.find("option[value=" + jDialog.data("operador") + "]").prop('selected', true);
            } else
                jDialog.dialog('option', 'title', 'Incluir Parametro');
        }
	});

	$(function() {
		var jParametrosAcordo = $("#parametrosAcordo"),
		parametrosAcordo = jParametrosAcordo[0],
		dialog = jDialog[0];

		$("#botaoIncluir").click(function(){
	        jDialog.data('acao',parametrosAcordo.incluirItem).dialog('open');
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

	function limparErros() {
		$("#valor").addClass('valid');
		$("#valor-error").css('display', 'none');
	}

	$('#valor').keydown(function (e) {
        if ($.inArray(e.keyCode, [46, 8, 9, 27, 13]) !== -1 ||
             // Allow: Ctrl+A
            (e.keyCode == 65 && e.ctrlKey === true) ||
             // Allow: Ctrl+C
            (e.keyCode == 67 && e.ctrlKey === true) ||
             // Allow: Ctrl+X
            (e.keyCode == 88 && e.ctrlKey === true) ||
             // Allow: home, end, left, right
            (e.keyCode >= 35 && e.keyCode <= 39)) {
                 return;
        }
        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
            e.preventDefault();
        }
	});
</script>
