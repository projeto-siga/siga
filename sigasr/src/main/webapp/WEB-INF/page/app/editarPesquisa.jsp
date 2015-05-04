#{extends 'main.html' /} #{set title:'Edição de Pesquisa' /}
<script src="/sigasr/public/javascripts/jquery.validate.min.js"></script>
<script src="/sigasr/public/javascripts/language/messages_pt_BR.min.js"></script>

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
	<form id="pesquisaForm">
		<input type="hidden" name="idPesquisa" id="idPesquisa" />
		<input type="hidden" name="hisIdIni" id="hisIdIni"/>
		
		<div class="gt-form-row gt-width-66">
			<label>Nome <span>*</span></label> <input type="text"
				name="nomePesquisa" id="nomePesquisa" size="60" maxlength="255" required />
		</div>
		<div class="gt-form-row gt-width-66">
			<label>Descrição</label> <input type="text" name="descrPesquisa" id="descrPesquisa"
				size="60" maxlength="255" />
		</div>

		<div class="gt-form-row">
			<label>Perguntas</label>
			<ul id="perguntas" style="color: #365b6d"></ul>
			<input type="button" value="Incluir" id="botaoIncluir"
				class="gt-btn-small gt-btn-left" style="font-size: 10px;" />
		</div>
	</form>
		
	#{configuracaoAssociacao orgaos:orgaos,
		locais:locais, 
		itemConfiguracaoSet:itemConfiguracaoSet,
		acoesSet:acoesSet,
		modoExibicao:'pesquisa',
		urlGravar:@Application.gravarAssociacaoPesquisa() }#{/configuracaoAssociacao}
	
	<div class="gt-form-row">
		<input type="button" value="Gravar" onclick="pesquisaService.gravar()" class="gt-btn-medium gt-btn-left" />
		<a class="gt-btn-medium gt-btn-left" onclick="pesquisaService.cancelarGravacao()">Cancelar</a>
		<input type="button" value="Aplicar" class="gt-btn-medium gt-btn-left" onclick="pesquisaService.aplicar()"/>
	</div>
</div>

<div id="dialog">
	<div class="gt-content">
		<div class="gt-form gt-content-box">
			<form id="perguntaForm">
				<div class="gt-form-row">
					<label>Pergunta <span>*</span></label> <input type="text" id="descrPergunta"
						name="descrPergunta" value="" size="60" maxlength="255" required/>
				</div>
				<div class="gt-form-row">
					<label>Tipo</label> #{select name:'tipoPergunta', id:'tipoPergunta'}
					#{list items:tipos, as:'tipo'} #{option tipo.idTipoPergunta}
					${tipo.nomeTipoPergunta} #{/option} #{/list} #{/select}
				</div>
				<div class="gt-form-row">
					<input type="button" id="modalOk" value="Ok" class="gt-btn-medium gt-btn-left" />
					<input type="button" value="Cancelar" id="modalCancel" class="gt-btn-medium gt-btn-left" />
				</div>
			</form>
		</div>
	</div>
</div>

<script>
	associacaoService.getUrlDesativarReativar = function(desativados) {
	    var url = '@{Application.listarAssociacaoPesquisa()}',
	        idPesquisa = $("[name=idPesquisa]").val();
	
	    if(desativados)
	        url = '@{Application.listarAssociacaoPesquisaDesativados()}';
	        
	    return url + "?idPesquisa=" + idPesquisa;
	}

	jQuery(document).ready(function($) {
		
    	var jPerguntas = $("#perguntas"),
	        perguntas = jPerguntas[0],
	        jDialog = $("#dialog"),
	        dialog = jDialog[0],
	        jDescrPergunta = $("#descrPergunta"),
	        jTipoPergunta = $("#tipoPergunta");
       
        $( "#perguntas" ).sortable({placeholder: "ui-state-highlight"});
        $( "#perguntas" ).disableSelection();
       
        $("#botaoIncluir").click(function() {
                jDialog.data('acao',perguntas.incluirItem).dialog('open');
        });
       
        jDialog.dialog({
                autoOpen: false,
                height: 'auto',
                width: 'auto',
                modal: true,
                resizable: false,
                close: function() {
                        jDescrPergunta.val('');
                        jDialog.data('descrPergunta','');
                        jDialog.data('tipoPergunta','');
                },
                open: function(){
                        if (jDialog.data("descrPergunta"))
                                jDialog.dialog('option', 'title', 'Alterar Pergunta');
                        else
                                jDialog.dialog('option', 'title', 'Incluir Pergunta');                  
                        jDescrPergunta.val(jDialog.data("descrPergunta"));
                        jTipoPergunta.find("option[value=" + jDialog.data("tipoPergunta") + "]").prop('selected', true);
                }
        });
        $("#modalOk").click(function(){
        	if (!jQuery("#perguntaForm").valid())
                return false;
            
                var acao = jDialog.data('acao');
                var jTipoEscolhido = jTipoPergunta.find("option:selected");
                acao(jDescrPergunta.val(), jTipoEscolhido.val(), jTipoEscolhido.text(), jDialog.data("id"));
                jDialog.dialog('close');
        });
        $("#modalCancel").click(function(){
                jDialog.dialog('close');
        });

        perguntas["index"] = 0;
        perguntas.incluirItem = function(descr, idTipo, descrTipo, id){
                if (!id)
                    id = 'novo_' + ++perguntas["index"];
                jPerguntas.append("<li style=\"cursor: move\" id =\"" + id + "\"></li>");
                var jNewTr = jPerguntas.find("li:last-child");
                jNewTr.append("<span>" + descr + "</span> - <span style=\"display: inline-block\" id=\"" + idTipo + "\">" + descrTipo + "</span>");
                jNewTr.append("&nbsp;&nbsp;<img src=\"/siga/css/famfamfam/icons/pencil.png\" style=\"visibility:hidden; cursor: pointer\" />");
                jNewTr.append("&nbsp;<img src=\"/siga/css/famfamfam/icons/delete.png\" style=\"visibility: hidden; cursor: pointer\" />");
                jNewTr.find("img:eq(0)").click(function() {
                        var jDivs=jNewTr.find("span");
                        jDialog.data("descrPergunta",jDivs[0].innerHTML)
                                .data("tipoPergunta",jDivs[1].id)
                                .data("id",id)
                                .data("acao", perguntas.alterarItem)
                                .dialog("open");
                });
                jNewTr.find("img:eq(1)").click(function(){
                        perguntas.removerItem(jNewTr.attr("id"));
                });
                jNewTr.mouseover(function(){
                        jNewTr.find("img").css("visibility", "visible");
                });
                jNewTr.mouseout(function(){
                        jNewTr.find("img").css("visibility", "hidden");
                });
        }
        perguntas.alterarItem = function(descr, idTipo, descrTipo, id){
                var jDivs=$("#"+id).find("span");
                jDivs[0].innerHTML = descr;
                jDivs[1].id = idTipo;
                jDivs[1].innerHTML = descrTipo;
        }
        perguntas.removerItem = function(idItem){
                $("#"+idItem).remove();
                perguntas["index"]--;
        }

        perguntas.limpar = function() {
       		$("#perguntas").html('');
       	}
        var pesquisaFormValidator = $("#pesquisaForm").validate();
        var perguntaFormValidator = $("#perguntaForm").validate();
	});

	function isValidForm() {
	    return jQuery("#pesquisaForm").valid();
	}

	function resetErrosForm() {
		pesquisaFormValidator.reset();
		perguntaFormValidator.reset();
	}

	function podeCadastrarAssociacao() {
		var pesquisaId = $("#idPesquisa");
        if ((pesquisaId == undefined || pesquisaId.val() == "")  && pesquisaService.aplicar() == false) 
            return false;
        else
            return true;
    }
</script>
