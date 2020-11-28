<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>


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


<div class="container-fluid">
	<form id="formItemConfiguracao" class="form100" enctype="multipart/form-data">
		<input type="hidden" name="itemConfiguracao.idItemConfiguracao" id="idItemConfiguracao" value="${id}">
		<input type="hidden" name="itemConfiguracao.hisIdIni" id="hisIdIni" value="${hisIdIni} }">
		
		<div class="card mb-2">
			<h5 class="card-header">Dados Básicos</h5>
			<div class="card-body">
			
				<!---------- Codigo ---------------->
				<div class="form-group">
					<label>Código * </label>
					<input id="siglaItemConfiguracao"
						   type="text"
						   class="form-control"
						   name="itemConfiguracao.siglaItemConfiguracao"
						   maxlength="255"
						   required /> 
				</div>
				
				<!---------- Titulo ---------------->
				<div class="form-group">
					<label>Título *</label>
					<input type="text" 
						   class="form-control"
						   id="tituloItemConfiguracao"
						   name="itemConfiguracao.tituloItemConfiguracao"
						   maxlength="255"
						   required />
				</div>
			
				<!---------- Descricao ---------------->
				<div class="form-group">
					<label>Descrição</label>
					<input type="text"
							class="form-control"
							id="descrItemConfiguracao"
							name="itemConfiguracao.descrItemConfiguracao"
							maxlength="255" />
				</div>
				
				<!---------- Gestores ---------------->
				<div class="form-group">
					<label>Gestores</label>
					<ul id="gestoresUl" class="list-group" style="color: #365b6d"></ul>
					<input type="button" value="Incluir" id="botaoIncluir"
									class="btn btn-primary btn-sm mt-2"  />
				</div>
				
				<!---------- Similaridade ---------------->
				<div class="form-group">
					<label>Similaridade (Separar itens com ponto e vírgula)</label>
					<textarea class="form-control" rows="3" maxlength="8192" 
						name="itemConfiguracao.descricaoSimilaridade"
						id="descricaoSimilaridade"></textarea>
				</div>
			</div>
		</div>
	
		<div class="card mb-2">
			<h5 class="card-header">Priorização</h5>
			<div class="card-body">
			
				<!---------- Fator ---------------->
				<div class="form-group">
					<label>Fator de Multiplicação *</label>
					<input onkeypress="javascript: var tecla=(window.event)?event.keyCode:e.which;if((tecla>47 && tecla<58)) return true; else{ if (tecla==8 || tecla==0) return true; else return false; }"
						   type="text" id="numFatorMultiplicacaoGeral" 
						   name="itemConfiguracao.numFatorMultiplicacaoGeral"
						   size="20"
						   maxlength="9"
						   class="form-control"
						   required
						   min="1"/>
				</div>
					
				<!---------- Fator por solicitante ---------------->
				<div class="form-group">
					<label class="inline">Fator de Multiplicação por Solicitante</label>
					<ul id="fatoresUl" style="color: #365b6d"></ul>
					<input type="button" value="Incluir" id="botaoIncluirFator"
	                       class="btn btn-primary btn-sm mt-2" />
	            </div>
			</div>
		</div>
	
		<div class="card mb-2">
			<h5 class="card-header">Designações</h5>
			<div class="card-body">
				<div id="divDesignacoes" class="gt-form-row">
				</div>
			</div>
		</div>
		
		<p class="gt-error" style="display:none;" id="erroCamposObrigatorios">Não foi possível gravar o registro.</p>
		
		<input type="button" value="Gravar" class="btn btn-primary" onclick="itemConfiguracaoService.gravar()"/>
		<a class="btn btn-secondary" style="color: #fff" onclick="itemConfiguracaoService.cancelarGravacao()">Cancelar</a>
		<input type="button" value="Aplicar" class="btn btn-primary" onclick="itemConfiguracaoService.aplicar()"/>
		
	</form>
</div>	


<!-- ===================== -->
<!--  Modal de Designacao  -->
<!-- ===================== -->
<div id="designacaoComponent">
	<sigasr:designacao modoExibicao="item" designacoes="designacoesItem" orgaos="orgaos" locais="locais"
 		unidadesMedida="unidadesMedida" pesquisaSatisfacao="pesquisaSatisfacao" listasPrioridade="listasPrioridade" />
</div>



<!-- ========================= -->
<!-- Modal para incluir Gestor -->
<!-- ========================= -->
<div id="dialog">
	<form id="formGestor" enctype="multipart/form-data">
		<div class="form-group">			
			<div id="divGestor">
				<sigasr:pessoaLotaSelecao2
						propriedadePessoa="gestorPessoaSel"
						propriedadeLotacao="gestorLotacaoSel"
						labelPessoaLotacao="Gestor *"
						/>
			</div>
			<input type="button" id="modalOk" value="Ok" class="btn btn-primary" /> 
			<input type="button" value="Cancelar" id="modalCancel" class="btn btn-secondary" />
		</div>
	</form>
</div>

<!-- ============================================= -->
<!-- Script referente ao modal para incluir Gestor -->
<!-- ============================================= -->
<script type="text/javascript">

	var jDialog = $('#dialog');
	
	jDialog.dialog({
	    autoOpen: false,
	    height: 'auto',
	    width: '80%',
	    modal: true,
	    resizable: false,
	    close: function() {
	        $("#formulario_gestorPessoaSel_sigla").val('');
			$("#formulario_gestorLotacaoSel_sigla").val('');
			$("#formulario_gestorPessoaSel_descricao").val('');
			$("#formulario_gestorLotacaoSel_descricao").val('');				
			$("#gestorPessoaSel_pessoaSelSpan").html('');  
			$("#gestorLotacaoSel_lotacaoSelSpan").html('');
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

	// Click no botao "Incluir" Gestores:
	$("#botaoIncluir").click(function(){
	    jDialog.data('acao',gestores.incluirItem).dialog('open');
	});

	// Click no botao Ok do modal:
	$("#modalOk").click(function(){
		if (!jQuery("#formGestor").valid())
		    return false;
	    
		var acao = jDialog.data('acao');
		var jTipoEscolhido = jSelect.find("option:selected");
		
		if(jTipoEscolhido.val() == 1) {
			acao($("#formulario_gestorPessoaSel_sigla").val(), $("#formulario_gestorPessoaSel_descricao").val(), 'pessoa', $("#formulario_gestorPessoaSel_id").val(), jDialog.data("id"));
			} else if (jTipoEscolhido.val() == 2) {
				acao($("#formulario_gestorLotacaoSel_sigla").val(), $("#formulario_gestorLotacaoSel_descricao").val(), 'lotacao', $("#formulario_gestorLotacaoSel_id").val(), jDialog.data("id"));
		}
		
		jDialog.dialog('close');
	});

	// Click no botao Cancelar do modal:
	$("#modalCancel").click(function(){
		jDialog.dialog('close');
		validatorFormGestor.resetForm();
	});
	
</script>




<!-- ==================================================== -->
<!-- Modal Incluir Fator de Multiplicacao por Solicitante -->
<!-- ==================================================== -->
<div id="dialogFator">
	<form id="formFator" enctype="multipart/form-data">
		<div class="form-group">
			<div id="divFator">
				<sigasr:pessoaLotaSelecao2
						propriedadePessoa="fatorPessoaSel"
						propriedadeLotacao="fatorLotacaoSel"
						labelPessoaLotacao="Solicitante *"
						/>
			</div>
		</div>
		<div class="form-group">
			<label>Fator de Multiplicação: </label>
			<input id="numfatorMult" 
				onkeypress="javascript: var tecla=(window.event)?event.keyCode:e.which;if((tecla>47 && tecla<58)) return true;  else{  if (tecla==8 || tecla==0) return true;  else  return false;  }"
				type="text" name="numfatorMult" value="1" size="43" maxlength="9"
				required
				class="form-control" 
				min="1"/>
		   <span style="display: none; color: red;" id="erroNumFatorMult">Fator de multiplicação menor que 1</span>
		</div>
		<input type="button" id="modalOkFator" value="Ok"
				class="btn btn-primary" /> 
		<input type="button"
				value="Cancelar" id="modalCancelFator" 
				class="btn btn-secondary" />
	</form>
</div>

<!-- ================================================== -->
<!-- Script referente ao modal para incluir Priorizacao -->
<!-- ================================================== -->
<script type="text/javascript">
	var jDialogFator = $("#dialogFator");
	jDialogFator.dialog({
	    autoOpen: false,
	    height: 'auto',
	    width: '80%',
	    modal: true,
	    resizable: false,
	    close: function() {
	    	$("#formulario_fatorPessoaSel_sigla").val('');
			$("#formulario_fatorLotacaoSel_sigla").val('');
			$("#formulario_fatorPessoaSel_descricao").val('');
			$("#formulario_fatorLotacaoSel_descricao").val('');
			$("#numfatorMult").val('1');
			$("#fatorPessoaSel_pessoaSelSpan").html('');  
			$("#fatorLotacaoSel_lotacaoSelSpan").html('');
			
			jDialogFator.data('fatorMultiplicacaoSet','');
	    	validatorFormFator.resetForm();
		}, 
		open: function(){
	    	$('#erroNumFatorMult').hide();
	
	    	if (jDialogFator.data("fatorMultiplicacaoSet"))
	        	jDialogFator.dialog('option', 'title', 'Alterar Fator de Multiplicação');
	        else
	          	jDialogFator.dialog('option', 'title', 'Incluir Fator de Multiplicação');  
	       	}
	});

	// Click no botao "Incluir" Priorizacao:
	$("#botaoIncluirFator").click(function(){
	    jDialogFator.data('acaoFator',fatores.incluirItem).dialog('open');
	});

	// Click no botao Ok do modal:
	$("#modalOkFator").click(function() {
		if (!jQuery("#formFator").valid())
		    return false;
		
		var acaoFator = jDialogFator.data('acaoFator');
		var jTipoEscolhidoFator = jSelectFator.find("option:selected");
		var numFatorMult = $('#numfatorMult');
		
		if(numFatorMult.val() > 0) {
		    if(jTipoEscolhidoFator.val() == 1) {
		        acaoFator($("#formulario_fatorPessoaSel_sigla").val(), $("#formulario_fatorPessoaSel_descricao").val(), $("#numfatorMult").val(), 'pessoa', $("#formulario_fatorPessoaSel_id").val(), jDialogFator.data("id"));
		    } else if (jTipoEscolhidoFator.val() == 2) {
		        acaoFator($("#formulario_fatorLotacaoSel_sigla").val(), $("#formulario_fatorLotacaoSel_descricao").val(), $("#numfatorMult").val(), 'lotacao', $("#formulario_fatorLotacaoSel_id").val(), jDialogFator.data("id"));
		    }
		    
			  jDialogFator.dialog('close');
		} else {
		    $('#erroNumFatorMult').show();
		}
	});

	// Click no botao Cancelar do modal:
	$("#modalCancelFator").click(function(){
		jDialogFator.dialog('close');
		validatorFormFator.resetForm();
	});	

	
</script>




<script type="text/javascript">
	var jGestores = null,
		gestores = null,
		jSelect = null,
		jFatores = null,
		fatores = null,
		dialogFator = null,
		jSelectFator = null,
		jNumFatorMult = null,
		validatorFormItem = null,
		validatorFormGestor = null,
		validatorFormFator = null;
		
	jQuery(document).ready(function($) {
		// DB1: adiciona as designaÃ§Ãµes no local correto programaticamente, pois
		// por problemas de inicializaÃ§Ã£o precisamos mudar o local do componente no html
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
		jSelect = $("#gestorPessoaSelgestorLotacaoSel");
		
		$( "#gestoresUl" ).sortable({placeholder: "ui-state-highlight"});
		$( "#gestoresUl" ).disableSelection();
		
			
		gestores["index"] = 0;
		gestores.incluirItem = function(siglaGestor, nomeGestor, tipoGestor, idDerivadoGestor, id) {
			if (!id)
		    	id = 'novo_' + ++gestores["index"];
			
		    jGestores.append("<li class=\"list-group-item m-0\" style=\"cursor: move\" id =\"" + id + "\"></li>");
		   	var jNewTr = jGestores.find("li:last-child");
		   	jNewTr.append("<span id=\"" + tipoGestor + "\">" + siglaGestor + "</span> - <span style=\"display: inline-block\" id=\"" 
			        + idDerivadoGestor + "\">" + nomeGestor + "</span>&nbsp;&nbsp;<img src=\"/siga/css/famfamfam/icons/cross.png\" style=\" visibility:hidden; cursor: pointer;\" />");

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
		
			//POPUP PARA ADICIONAR UM FATOR DE MULTIPLICAÃÃO E SOLICITANTE
		jFatores = $("#fatoresUl");
		fatores = jFatores[0];
		
		jSelectFator = $("#fatorPessoaSelfatorLotacaoSel");
		jNumFatorMult = $("#numfatorMult");
		
		$( "#fatoresUl" ).sortable({placeholder: "ui-state-highlight"});
		$( "#fatoresUl" ).disableSelection();
		
		
			 
	
		
		fatores["index"] = 0;
		fatores.incluirItem = function( siglaSolicitante, nomeSolicitante, numFator, tipoFator, idDerivadoFator, idF){
		    if (!idF)
		        idF = 'novo_fator' + ++fatores["index"];
		
		    jFatores.append("<li class=\"list-group-item m-0\" style=\"cursor: move\" id =\"" + idF + "\"></li>");
		
		    var jNewFatorTr = jFatores.find("li:last-child");
		    jNewFatorTr.append("<span id=\"" + tipoFator + "\">" + siglaSolicitante + " - " 
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
		

		
	
</script>