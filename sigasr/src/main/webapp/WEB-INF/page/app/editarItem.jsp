<script src="/sigasr/public/javascripts/jquery.validate.min.js"></script>
<script src="/sigasr/public/javascripts/language/messages_pt_BR.min.js"></script>
<script src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>

<style>
.inline {
	display: inline-flex !important;
}

.tabela {
	margin-top: -10px;
	min-width: 200px;
}

.tabela tr {
 	border: solid;
 	border-color: rgb(169, 169, 169);
 	border-width: 1px;
 	font-weight: bold;
	line-height: 20px;
}

.tabela th {
 	color: #365b6d;
 	font-size: 100%;
 	padding: 5px 10px;
 	border: solid 1px rgb(169, 169, 169);
}

.tabela td {
	color: #000;
 	padding-right: 10px !important;
}

.gt-form-table td {
	padding-right: 0px;
}

.barra-subtitulo {
	background-color: #eee;
	color: #365b6d;
	padding: 10px 15px;
	border: 1px solid #ccc;
	border-radius: 0;
	font-weight: bold;
	margin: 0 0 10px -16px;
}

.barra-subtitulo-top {
	border-radius: 5px 5px 0 0;
	margin: -16px 0 10px -16px;
}

.div-editar-item {
	border-left-width: 0px;
	border-right-width: 0px;
	border-top-width: 0px;
	border-bottom-width: 0px;
	width: 800px !important;
	max-width: 800px !important;
}
</style>

<div class="gt-form gt-content-box div-editar-item">
	<div>
		<div class="gt-content-box gt-for-table">
			<form id="formItemConfiguracao" class="form100" enctype="multipart/form-data">
				<input type="hidden" name="id" id="idItemConfiguracao">
				<input type="hidden" name="hisIdIni" id="hisIdIni">
				<table class="gt-form-table">
					<tr class="header">
						<td align="center" valign="top" colspan="2" style="border-radius: 5px;">Dados Básicos</td>
					</tr>
					<tr>
						<td width="5%">
							<label class="inline">Código: <span>*</span></label> 
						</td>
						<td>
							<input id="siglaItemConfiguracao"
								   type="text"
								   name="siglaItemConfiguracao"
								   value="${itemConfiguracao?.siglaItemConfiguracao}"
								   maxlength="255"
								   required /> 
						</td>
					</tr>
					<tr>
						<td>
							<label class="inline"
							style="margin-left: 10px;">Título: <span>*</span></label> 
						</td>
						<td>	
							<input type="text" 
								   id="tituloItemConfiguracao"
								   name="tituloItemConfiguracao"
								   value="${itemConfiguracao?.tituloItemConfiguracao}"
								   style="width: 67.6%;"
								   maxlength="255"
								   required />
						</td>
					</tr>
					<tr>
						<td>
							<label class="inline">Descrição:</label> 
						</td>
						<td colspan="2">
							<input type="text"
								id="descrItemConfiguracao"
								name="descrItemConfiguracao"
								value="${itemConfiguracao?.descrItemConfiguracao}"
								style="width: 690px" maxlength="255" />
						</td>
					</tr>
					<tr>
						<td>
							<label>Gestores: </label>
						</td>
					</tr>
					<tr>
						<td colspan="3" style="padding: 0 10px;">
							<ul id="gestoresUl" style="color: #365b6d"></ul>
						</td>
					</tr>
					<tr>
						<td style="padding-top: 0px;">
							<input type="button" value="Incluir" id="botaoIncluir"
                                                class="gt-btn-small gt-btn-left" style="font-size: 10px;" />
						</td>
					</tr>
					<tr>
						<td colspan="3">
							<label class="inline">Similaridade (Separar itens com
								ponto e vírgula):</label>
							<textarea cols="63" rows="3" maxlength="8192" 
								name="descricaoSimilaridade"
								id="descricaoSimilaridade">${itemConfiguracao?.descricaoSimilaridade}</textarea>
						</td>
					</tr>
				</table>
				<table class="gt-form-table">
					<tr class="header"><td align="center" valign="top" colspan="3">Priorização</td></tr>

					<tr>
						<td width="10.5%">
							<label class="inline">Fator de Multiplicação: <span>*</span></label> 
						</td>
						<td>
							<input onkeypress="javascript: var tecla=(window.event)?event.keyCode:e.which;if((tecla>47 && tecla<58)) return true; else{ if (tecla==8 || tecla==0) return true; else return false; }"
								   type="text" id="numFatorMultiplicacaoGeral" 
								   name="numFatorMultiplicacaoGeral"
								   value="${itemConfiguracao?.numFatorMultiplicacaoGeral}"
								   size="20"
								   maxlength="9"
								   required
								   min="1"/>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<label class="inline">Fator de Multiplicação por Solicitante:</label> 
						</td>
					</tr>
					<tr>
						<td colspan="3" style="padding: 0 10px;">
							<ul id="fatoresUl" style="color: #365b6d"></ul>
						</td>
					</tr>
					<tr>
						<td style="padding-top: 0px;">
							<input type="button" value="Incluir" id="botaoIncluirFator"
                                                class="gt-btn-small gt-btn-left" style="font-size: 10px;" />
						</td>
					</tr>
				</table>
				
				<div id="divDesignacoes" class="gt-form-row">
					
				</div>
				
				<table class="gt-form-table">
					<tr>
						<tr>
							<td>
								<p class="gt-error" style="display:none;" id="erroCamposObrigatorios">Não foi possível gravar o registro.</p>
							</td>
						</tr>
						<div class="gt-form-row">
							<input type="button" value="Gravar" class="gt-btn-medium gt-btn-left" onclick="itemConfiguracaoService.gravar()"/>
							<a class="gt-btn-medium gt-btn-left" onclick="itemConfiguracaoService.cancelarGravacao()">Cancelar</a>
							<input type="button" value="Aplicar" class="gt-btn-medium gt-btn-left" onclick="itemConfiguracaoService.aplicar()"/>
						</div>						
					</tr>
				</table>
			</form>
		</div>
	</div>
</div>

<div id="dialog">
	<div class="gt-content">
		<form id="formGestor" enctype="multipart/form-data">
			<div class="gt-form gt-content-box">
				<div class="gt-form-row">
					<label>Gestor: <span>*</span></label>
					<div id="divGestor">#{pessoaLotaSelecao
						nomeSelPessoa:'gestor.pessoa', nomeSelLotacao:'gestor.lotacao',
						valuePessoa:pessoaAtual, valueLotacao:lotacaoAtual,
						disabled:disabled,
						requiredValue:'required' /}</div>
				</div>
				<div class="gt-form-row">
					<input type="button" id="modalOk" value="Ok"
						class="gt-btn-medium gt-btn-left" /> <input type="button"
						value="Cancelar" id="modalCancel" class="gt-btn-medium gt-btn-left" />
				</div>
			</div>
		</form>
	</div>
</div>

<div id="dialogFator">
	<div class="gt-content">
		<form id="formFator" enctype="multipart/form-data">
			<div class="gt-form gt-content-box">
				<div class="gt-form-row ">
					<label>Solicitante: <span>*</span></label>
					<div id="divFator">#{pessoaLotaSelecao
						nomeSelPessoa:'fator.pessoa', nomeSelLotacao:'fator.lotacao',
						valuePessoa:pessoaAtual, valueLotacao:lotacaoAtual,
						disabled:disabled,
						requiredValue:'required'/}</div>
				</div>
				<div class="gt-form-row ">
					<label>Fator de Multiplicação: </label>
					<input id="numfatorMult" onkeypress="javascript: var tecla=(window.event)?event.keyCode:e.which;if((tecla>47 && tecla<58)) return true;  else{  if (tecla==8 || tecla==0) return true;  else  return false;  }"
						   type="text" name="numfatorMult" value="1" size="43" maxlength="9"
						   required 
						   min="1"/>
						   <span style="display: none; color: red;" id="erroNumFatorMult">Fator de multiplicação menor que 1</span>
				</div>
				<div class="gt-form-row">
					<input type="button" id="modalOkFator" value="Ok"
						class="gt-btn-medium gt-btn-left" /> <input type="button"
						value="Cancelar" id="modalCancelFator" class="gt-btn-medium gt-btn-left" />
				</div>
			</div>
		</form>
	</div>
</div>

<div id="designacaoComponent">
	#{designacoes designacoes:designacoesItem, 
			modoExibicao:'item', 
			orgaos:orgaos, 
			locais:locais, 
			unidadesMedida:unidadesMedida, 
			pesquisaSatisfacao:pesquisaSatisfacao, 
			listasPrioridade:listasPrioridade /}
</div>

<script>
	var jGestores = null,
		gestores = null,
		jDialog = null,
		dialog = null,
		jSelect = null,
		jFatores = null,
	    fatores = null,
	    jDialogFator = null,
	    dialogFator = null,
	    jSelectFator = null,
	    jNumFatorMult = null,
		validatorFormItem = null,
		validatorFormGestor = null,
		validatorFormFator = null;

	jQuery(document).ready(function($) {
		// DB1: adiciona as designações no local correto programaticamente, pois
		// por problemas de inicialização precisamos mudar o local do componente no html
		jQuery("#divDesignacoes").append(jQuery("#designacaoComponent"));
		
		$("#siglaItemConfiguracao").mask("99.99.99");
		
		validatorFormItem = $("#formItemConfiguracao").validate({
			onfocusout: false
		});
		
	    validatorFormGestor = $("#formGestor").validate({
	    	onfocusout: false
	    });
	    
	    validatorFormFator = $("#formFator").validate({
	    	onfocusout: false
	    });

	 	// POPUP PARA ADICIONAR UM GESTOR
	    jGestores = $("#gestoresUl");
	    gestores = jGestores[0];
	    jDialog = $("#dialog");
	    dialog = jDialog[0];
	    jSelect = $("#gestorpessoagestorlotacao");

	    $( "#gestoresUl" ).sortable({placeholder: "ui-state-highlight"});
	    $( "#gestoresUl" ).disableSelection();
	  
	    $("#botaoIncluir").click(function(){
	        jDialog.data('acao',gestores.incluirItem).dialog('open');
	    });
	     
	    jDialog.dialog({
	        autoOpen: false,
	        height: 'auto',
	        width: 'auto',
	        modal: true,
	        resizable: false,
	        close: function() {
	        	$("#gestorpessoa_sigla").val('');
	        	$("#gestorlotacao_sigla").val('');
	        	$("#gestorpessoa_descricao").val('');
	        	$("#gestorlotacao_descricao").val('');
	        	$("#gestorpessoaSpan").html('');  
	        	$("#gestorlotacaoSpan").html('');  
	            jDialog.data('gestorSet','');

	            validatorFormGestor.resetForm();
	        },
	      	open: function(){
	          	if (jDialog.data("gestorSet"))
	              	jDialog.dialog('option', 'title', 'Alterar Gestor');
	          	else
	              	jDialog.dialog('option', 'title', 'Incluir Gestor');  
	      	}
	    });

	    gestores["index"] = 0;
	    gestores.incluirItem = function(siglaGestor, nomeGestor, tipoGestor, idDerivadoGestor,  id){
			if (!id)
	        	id = 'novo_' + ++gestores["index"];
	    	
	        jGestores.append("<li style=\"cursor: move\" id =\"" + id + "\"></li>");
	       	var jNewTr = jGestores.find("li:last-child");
	       	jNewTr.append("<span id=\"" + tipoGestor + "\">" + siglaGestor + "</span> - <span style=\"display: inline-block\" id=\"" 
	   	        + idDerivadoGestor + "\">" + nomeGestor + "</span>&nbsp;&nbsp;<img src=\"/siga/css/famfamfam/icons/cross.png\" style=\"cursor: pointer;\" />");
	       	jNewTr.find("img:eq(0)").click(function(){
	       		gestores.removerItem(jNewTr.attr("id"));
	       	});
	       	jNewTr.mouseover(function(){
	        	jNewTr.find("img").css("visibility", "visible");
	       	});
	       	jNewTr.mouseout(function(){
	            jNewTr.find("img").css("visibility", "hidden");
	       	});
		}

	    gestores.removerItem = function(idItem){
	        $("#"+idItem).remove();
	        gestores["index"]--;
		}

	   	//POPUP PARA ADICIONAR UM FATOR DE MULTIPLICAÇÃO E SOLICITANTE
	    jFatores = $("#fatoresUl");
	    fatores = jFatores[0];
	    jDialogFator = $("#dialogFator");
	    dialogFator = jDialogFator[0];
	    jSelectFator = $("#fatorpessoafatorlotacao");
	    jNumFatorMult = $("#numfatorMult");

	    $( "#fatoresUl" ).sortable({placeholder: "ui-state-highlight"});
	    $( "#fatoresUl" ).disableSelection();
	   
	    $("#botaoIncluirFator").click(function(){
	        jDialogFator.data('acaoFator',fatores.incluirItem).dialog('open');
	    });
	     
	    jDialogFator.dialog({
	        autoOpen: false,
	        height: 'auto',
	        width: 'auto',
	        modal: true,
	        resizable: false,
	        close: function() {
	        	$("#fatorpessoa_sigla").val('');
	        	$("#fatorlotacao_sigla").val('');
	        	$("#fatorpessoa_descricao").val('');
	        	$("#fatorlotacao_descricao").val('');
	        	$("#numfatorMult").val('1');
	        	$("#fatorpessoaSpan").html('');  
	        	$("#fatorlotacaoSpan").html('');  
	        	jDialogFator.data('fatorMultiplicacaoSet','');

	        	validatorFormFator.resetForm();
	    	}, 
	    	open: function(){
	        	$('#erroNumFatorMult').hide();

	        	if (jDialogFator.data("gestorSet"))
	            	jDialogFator.dialog('option', 'title', 'Alterar Fator de Multiplicação');
	            else
	              	jDialogFator.dialog('option', 'title', 'Incluir Fator de Multiplicação');  
	           	}
	    });

	    fatores["index"] = 0;
	    fatores.incluirItem = function( siglaSolicitante, nomeSolicitante, numFator, tipoFator, idDerivadoFator, idF){
	        if (!idF)
	            idF = 'novo_fator' + ++fatores["index"];

	        jFatores.append("<li style=\"cursor: move\" id =\"" + idF + "\"></li>");

	        var jNewFatorTr = jFatores.find("li:last-child");
	        jNewFatorTr.append("<span id=\"" + tipoFator + "\">" + siglaSolicitante + "-" 
	 	      	+ nomeSolicitante + "</span> <span style=\"display: inline-block\" id=\"" + idDerivadoFator + "\"> / Fator: "
	          	+ numFator + "</span>&nbsp;&nbsp;<img src=\"/siga/css/famfamfam/icons/cross.png\" style=\" visibility:hidden; cursor: pointer;\" />");
	      	
	        jNewFatorTr.find("img:eq(0)").click(function(){
	            fatores.removerItemFator(jNewFatorTr.attr("id"));
	        });

	        jNewFatorTr.mouseover(function(){
	            jNewFatorTr.find("img").css("visibility", "visible");
	        });

	        jNewFatorTr.mouseout(function(){
	            jNewFatorTr.find("img").css("visibility", "hidden");
	        });
	    };

	    fatores.removerItemFator = function(idItemF){
	        $("#"+idItemF).remove();
	        fatores["index"]--;
	    };
	});

    $("#modalOk").click(function(){
        if (!jQuery("#formGestor").valid())
            return false;
        
    	var acao = jDialog.data('acao');
    	var jTipoEscolhido = jSelect.find("option:selected");
       
		if(jTipoEscolhido.val() == 1) {
			acao($("#gestorpessoa_sigla").val(), $("#gestorpessoa_descricao").val(), 'pessoa', $("#gestorpessoa").val(), jDialog.data("id"));
       	} else if (jTipoEscolhido.val() == 2) {
       		acao($("#gestorlotacao_sigla").val(), $("#gestorlotacao_descricao").val(), 'lotacao', $("#gestorlotacao").val(), jDialog.data("id"));
		}
       
		jDialog.dialog('close');
	});

    $("#modalCancel").click(function(){
    	jDialog.dialog('close');
    	validatorFormGestor.resetForm();
	});

    $("#modalOkFator").click(function() {
        if (!jQuery("#formFator").valid())
            return false;
        
        var acaoFator = jDialogFator.data('acaoFator');
        var jTipoEscolhidoFator = jSelectFator.find("option:selected");
        var numFatorMult = $('#numfatorMult');

        if(numFatorMult.val() > 0) {
            if(jTipoEscolhidoFator.val() == 1) {
                acaoFator($("#fatorpessoa_sigla").val(), $("#fatorpessoa_descricao").val(), $("#numfatorMult").val(), 'pessoa', $("#fatorpessoa").val(), jDialogFator.data("id"));
            } else if (jTipoEscolhidoFator.val() == 2) {
                acaoFator($("#fatorlotacao_sigla").val(), $("#fatorlotacao_descricao").val(), $("#numfatorMult").val(), 'lotacao', $("#fatorlotacao").val(), jDialogFator.data("id"));
            }
            
        	  jDialogFator.dialog('close');
        } else {
            $('#erroNumFatorMult').show();
        }
    });
    
    $("#modalCancelFator").click(function(){
        jDialogFator.dialog('close');
        validatorFormFator.resetForm();
    });

    designacaoService.gravar = function() {
    	var id = itemConfiguracaoService.getIdEdicao();
		designacaoOpts.urlGravar = '@{Application.gravarDesignacaoItem()}?idItemConfiguracao=' + id;
		BaseService.prototype.gravar.call(this);
    }

    designacaoService.onGravar = function(obj, objSalvo) {
		var tr = BaseService.prototype.onGravar.call(this, obj, objSalvo),
			itemConfiguracaoAntivo = {
				id : itemConfiguracaoService.getIdEdicao()
			};
		afterGravarDesignacao(tr, objSalvo, this);
		itemConfiguracaoService.onGravar(itemConfiguracaoAntivo, objSalvo.itemConfiguracao);
		$('#idItemConfiguracao').val(objSalvo.itemConfiguracao.id);
		return tr;
    }

    designacaoService.cadastrar = function(titulo) {
		if (!itemConfiguracaoService.getIdEdicao() && itemConfiguracaoService.aplicar() == false) {
			return;
		}
    	BaseService.prototype.cadastrar.call(this, titulo);
    	atualizarDesignacaoEdicao();
    }    
</script>