#{extends 'main.html' /} #{set title:'Edição de atributo' /}
<script src="/sigasr/public/javascripts/jquery.validate.min.js"></script>
<script src="/sigasr/public/javascripts/language/messages_pt_BR.min.js"></script>

<div class="gt-form gt-content-box">
	<form id="atributoForm" action="#" enctype="multipart/form-data">
		<input type="hidden" name="idAtributo" id="idAtributo" value="${idAtributo}">
		<input type="hidden" name="hisIdIni" id="hisIdIni" value="${hisIdIni}">
		<div class="gt-form-row box-wrapper">
			<div class="box box-left gt-width-50">
				<label>Nome <span>*</span></label> <input type="text"
					name="nomeAtributo"
					id="nomeAtributo"
					value="${nomeAtributo}" size="50" maxlength="255" required/>
			</div>
			<div class="box gt-width-50">
				<label>Descrição</label> <input maxlength="255" type="text"
					name="descrAtributo"
					id="descrAtributo"
					value="${descrAtributo}" 
					style="width: 372px;" />
			</div>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>C&oacute;digo</label> 
			<input type="text" name="codigoAtributo" id="codigoAtributo"
				value="${codigoAtributo}" size="60" maxlength="255"/>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>Objetivo do atributo<span>*</span></label> 
			#{select id:'objetivoAtributo', name:'objetivoAtributo', items:objetivos,
				labelProperty:'descrObjetivo', value:idObjetivo, style:'width:393px;',
				onchange:'javascript:ocultaAssociacoes();',
				class:'select-siga'/}
		</div>
		<div class="gt-form-row gt-width-100">
			<label>Tipo de atributo</label> #{select name:'tipoAtributo', items:models.SrTipoAtributo.values(),
			labelProperty:'descrTipoAtributo', value:tipoAtributoAnterior, style:'width:100%', id:'tipoAtributo',
			class:'select-siga' /}
		</div>
		<div class="gt-form-row gt-width-66" id="vlPreDefinidos" style="display: none;">
			<label>Valores pré-definidos (Separados por ponto-e-vígula(;))</label> 
			<input maxlength="255" type="text"
				name="descrPreDefinido"
				id="descrPreDefinido"
				value="${descrPreDefinido}" size="60" />
		</div>
	</form>
	
	#{configuracaoAssociacao orgaos:orgaos,
		locais:locais, 
		itemConfiguracaoSet:itemConfiguracaoSet,
		acoesSet:acoesSet,
		modoExibicao:'atributo',
		urlGravar:@Application.gravarAssociacao(),
		validarCadastrar:podeCadastrarAssociacaoAtributo}
	#{/configuracaoAssociacao}
		
	<div class="gt-form-row" style="padding-top: 10px;">
		<input type="button" value="Gravar" class="gt-btn-medium gt-btn-left" onclick="atributoService.gravar()"/>
		<a class="gt-btn-medium gt-btn-left" onclick="atributoService.cancelarGravacao()">Cancelar</a>
		<input type="button" value="Aplicar" class="gt-btn-medium gt-btn-left" onclick="atributoService.aplicar()"/>
	</div>
</div>

<script>
	associacaoService.getUrlDesativarReativar = function(desativados) {
		var url = '@{Application.listarAssociacaoAtributo()}',
			idAtributo = $("[name=idAtributo]").val();

		if(desativados)
			url = '@{Application.listarAssociacaoAtributoDesativados()}';
			
		return url + "?idAtributo=" + idAtributo;
	}

	var validatorAtributoForm;
	jQuery( document ).ready(function( $ ) {
		validatorAtributoForm = $("#atributoForm").validate({
			onfocusout: false
		});
	});
	
	function ocultaAssociacoes(){
		if ($("#objetivo").val() == 1){
			$("#associacoes").show();
		} else {
			$("#associacoes").hide();
		}
	}

	function detalhesListaAssociacao(d, associacao) {
		var tr = $('<tr class="detail">'),
			td = $('<td colspan="6">'),
			table = $('<table class="datatable" cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">');
			
		table.append(htmlConteudo(d, "Item de configuração:", associacao.itemConfiguracaoUnitario.siglaItemConfiguracao, associacao.itemConfiguracaoUnitario.tituloItemConfiguracao, table));
		table.append(htmlConteudo(d, "A&ccedil;&atilde;o:", associacao.acaoUnitaria.sigla, associacao.acaoUnitaria.descricao, table));
		
		td.append(table);
		tr.append(td);
	    
	    return tr;
	};

	function htmlConteudo(d, titulo, sigla, descricao, table) {
		var trItem = $('<tr>'),
			tdTitulo = $('<td><b>' + titulo + '</b></td'),
			tdConteudo = $('<td>'),
			table = $('<table>'),
			trDetalhe = $('<tr>'),
			tdSigla = $('<td>' + sigla + "</td>"),
			tdDescricao = $('<td>' +  descricao + '</td>');
		
		trDetalhe.append(tdSigla);
		trDetalhe.append(tdDescricao);
		table.append(trDetalhe);
		tdConteudo.append(table);
		trItem.append(tdTitulo);
		return trItem.append(tdConteudo);
	};

	associacaoService.atualizarListaAssociacoes = function(jSon) {
		associacaoTable.dataTable.api().clear().draw();
		
		if (jSon && jSon.associacoesVO) {
			// cria a lista de associacoes, e adiciona na tela
			for (i = 0; i < jSon.associacoesVO.length; i++) {
				var assoc = jSon.associacoesVO[i],
					html = 
					'<td class="gt-celula-nowrap" style="font-size: 13px; font-weight: bold; border-bottom: 1px solid #ccc !important; padding: 7px 10px;">' +
						'<a class="once desassociar gt-btn-ativar" onclick="desassociar(event, ' + assoc.idConfiguracao + ')" title="Remover permissão">' +
							'<input class="idAssociacao" type="hidden" value="' + assoc.idConfiguracao + '"/>' +
							'<img id="imgCancelar" src="/siga/css/famfamfam/icons/cancel_gray.png" style="margin-right: 5px;">' + 
						'</a>' +
					'</td>',

					rowAssoc = [
								' ',
								assoc.itemConfiguracaoUnitario? assoc.itemConfiguracaoUnitario.id : ' ',
								assoc.itemConfiguracaoUnitario? formatDescricaoLonga(assoc.itemConfiguracaoUnitario.tituloItemConfiguracao) : ' ',
								assoc.itemConfiguracaoUnitario? assoc.itemConfiguracaoUnitario.sigla : ' ',
								assoc.acaoUnitaria? assoc.acaoUnitaria.id : ' ',
								assoc.acaoUnitaria? formatDescricaoLonga(assoc.acaoUnitaria.titulo) : ' ',
								assoc.acaoUnitaria? assoc.acaoUnitaria.sigla : ' ',
								$("#checkatributoObrigatorio")[0].checked = assoc.atributoObrigatorio,
								getAtributoObrigatorioString(),
								assoc.idConfiguracao,
								html
			   				];
	   				
				// Adiciona na tabela de Associações
				var newRow = associacaoTable.dataTable
					.api()
					.row
					.add(rowAssoc),
					node = $(newRow.node());
				
				newRow.draw();
				associacaoService.adicionarFuncionalidadesNaLinha(node, assoc);
			}
		}
	}

	function transformStringToBoolean(value) {
		if (value.constructor.name == 'String')
			return value == 'true';
		else
			return value;
	}

	function formatDescricaoLonga(descricao) {
		if (descricao && descricao.length > 10) {
			return descricao.substr(0, 10) + " ...";
		}
		else return (descricao || ' ');
	}

	function getAtributoObrigatorioString() {
		var isChecked = $("#checkatributoObrigatorio")[0].checked;
		return isChecked ? "Sim": "Não";
	}

	function desassociar(event, idAssociacaoDesativar) {
		event.stopPropagation()
		
		var me = $(this),
			tr = $(event.currentTarget).parent().parent()[0],
			row = associacaoTable.dataTable.api().row(tr).data(),
			idAssociacao = idAssociacaoDesativar ? idAssociacaoDesativar : row[colunas.idAssociacao];
			idAtributo = $("#idAtributo").val();
			
			$.ajax({
		         type: "POST",
		         url: "@{Application.desativarAssociacaoEdicao()}",
		         data: {idAtributo : idAtributo, idAssociacao : idAssociacao},
		         dataType: "text",
		         success: function(response) {
		        	 associacaoTable.dataTable.api().row(tr).remove().draw();
		         },
		         error: function(response) {
		        	$('#modal-associacao').hide(); 

		        	var modalErro = $('#"modal-associacao-error"');
		        	modalErro.find("h3").html(response.responseText);
		        	modalErro.show(); 
		         }
	       });
	}
=======
>>>>>>> SigaSr
	
	function verificarTipoAtributo() {
		if($("select[name='att.tipoAtributo']").val() === 'VL_PRE_DEFINIDO') {
			$('#vlPreDefinidos').show();
			return;
		}
		$('#vlPreDefinidos').hide();
	};
	
	verificarTipoAtributo();
	
	$("select[name='att.tipoAtributo']").change(function() {
		verificarTipoAtributo(); 
	});
	
	if($('#erroDescrPreDefinido').html()) {
		$("select[name='att.tipoAtributo']").val('VL_PRE_DEFINIDO');
		$('#vlPreDefinidos').show();
	};

	function podeCadastrarAssociacao() {
		var atributoId = $("#idAtributo");
        if ((atributoId == undefined || atributoId.val() == "")  && atributoService.aplicar() == false) 
            return false;
        else
            return true;
    }
</script>