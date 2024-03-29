<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

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

<div class="container-fluid">
	<form id="pesquisaForm" action="#" enctype="multipart/form-data">
		<input type="hidden" name="pesquisa.idPesquisa" id="idPesquisa"
			value="${idAtributo}"> <input type="hidden"
			name="pesquisa.hisIdIni" id="hisIdIni" value="${hisIdIni}">
		<div class="gt-form-row gt-width-66">
			<label>Nome <span>*</span></label> <input type="text"
				name="pesquisa.nomePesquisa" id="nomePesquisa"
				value="${nomePesquisa}" size="50" maxlength="255" required
				class="form-control" />
		</div>
		<div class="gt-form-row gt-width-66">
			<label>Descri&ccedil;&atilde;o</label> <input type="text"
				name="pesquisa.descrPesquisa" id="descrPesquisa"
				value="${descrPesquisa}" size="60" maxlength="255"
				class="form-control" />
		</div>
		<div class="card mt-4 mb-2">
			<div class="card-header">
				<label>Perguntas</label>
			</div>
			<div class="card-body">
				<ul id="perguntas" style="color: #365b6d"></ul>
				<input type="button" value="Incluir" id="botaoIncluir"
					class="btn btn-primary">
			</div>
		</div>
	</form>

	<sigasr:configuracaoAssociacao orgaos="${orgaos}" locais="${locais}"
		itemConfiguracaoSet="${itemConfiguracaoSet}" acoesSet="${acoesSet}"
		modoExibicao='pesquisa'
		urlGravar="${linkTo[AssociacaoController].gravarAssociacaoPesquisa}"></sigasr:configuracaoAssociacao>

	<div class="gt-form-row">
		<input type="button" value="Gravar" onclick="pesquisaService.gravar()"
			class="btn btn-primary" />
		<a class="btn btn-secondary" style="color: #fff"
			onclick="pesquisaService.cancelarGravacao()">Cancelar</a>
		<input type="button" value="Aplicar" class="btn btn-primary"
			style="color: #fff" onclick="pesquisaService.aplicar()" />
	</div>
</div>

<sigasr:modal nome="pergunta" titulo="Incluir Pergunta">
	<div id="dialog" class="container-fluid">
		<div class="card mt-2 mb-2">
			<div class="card-body">
				<form id="perguntaForm">
					<div class="gt-form-row gt-width-66">
						<label>Pergunta <span>*</span></label> <input type="text"
							id="descrPergunta" name="descrPergunta" value="${descrPergunta}"
							size="60" maxlength="255" required class="form-control"/>
					</div>
					<div class="gt-form-row gt-width-66">
						<label>Tipo</label>
						<siga:select name="tipoPergunta" list="tipos"
							listKey="idTipoPergunta" id="tipoPergunta"
							headerValue="[Indefinido]" headerKey="0"
							listValue="nomeTipoPergunta" theme="simple"
							value="${idTipoPergunta}" />
					</div>
					<div class="gt-form-row gt-width-66 mt-2">
						<input type="button" id="modalOk" value="Ok"
							class="btn btn-primary" /> <input type="button" value="Cancelar"
							id="modalCancel" class="btn btn-secondary" style="color: #fff" />
					</div>
				</form>
			</div>
		</div>
	</div>
</sigasr:modal>

<script type="text/javascript">
	var pesquisaFormValidator, perguntaFormValidator = null;

	associacaoService.getUrlDesativarReativar = function(desativados) {
		var url = '${linkTo[PesquisaSatisfacaoController].listarAssociacaoPesquisa}';
		var idAtributo = $("[id=idPesquisa]").val();
		var exibirInativo = "";

		if(desativados)
			exibirInativo = "&exibirInativos=true";

		return url + "?idPesquisa=" + idAtributo + exibirInativo;
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
        	document.getElementById("descrPergunta").className = "";
			jDialog.data('acao', perguntas.incluirItem).dialog('open');
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
                		perguntaFormValidator.resetForm();

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

        		var idTipoPerguntaSelecionado = $("#tipoPergunta").val();
             	if (idTipoPerguntaSelecionado == null || idTipoPerguntaSelecionado == undefined || idTipoPerguntaSelecionado == 0) {
                	alert("Informe um Tipo de Pergunta diferente de Indefinido.");
                	return false;
             	}

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

        pesquisaFormValidator = $("#pesquisaForm").validate();
        perguntaFormValidator = $("#perguntaForm").validate();

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
