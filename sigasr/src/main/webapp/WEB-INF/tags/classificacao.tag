<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<%@ attribute name="metodo" required="true"%>
<%@ attribute name="exibeLotacaoNaAcao" required="false"%>
<%@ attribute name="exibeConhecimento" required="false"%>

<div id="${metodo}" class="gt-form-row" style="min-width: 550px;">
	<label>Produto, Servi&ccedil;o ou Sistema relacionado &agrave; Solicita&ccedil;&atilde;o</label>
	<siga:selecao2 propriedade="solicitacao.itemConfiguracao" 
		tipo="itemConfiguracao" 
		tema="simple" 
		modulo="sigasr" 
		tamanho="grande"
		onchange="dispararFuncoesOnBlurItem();"
		checarInput="true"
		paramList="sol.id=${solicitacao.id};sol.solicitante.id=${solicitante.idPessoa};sol.local.id=${local.idComplexo};sol.titular.id=${titular.idPessoa};sol.lotaTitular.id=${lotaTitular.idLotacao}" />
	<br/><span id="itemNaoInformado" class="error" style="color: red; display: none;">Item não informado</span>
	<br/>
	<div id="divAcao" depende="solicitacao.itemConfiguracao" >
		<c:if test="${exibeConhecimento}">
			<c:if test="${solicitacao.itemConfiguracao != null && podeUtilizarServicoSigaGC}">
				<c:if test="${podeVerGestorItem && not empty solicitacao.itemConfiguracao.gestorSet}">
					<div class="gt-form-row">
						<label>Gestor do Produto</label>
					    <c:forEach var="g" items="${solicitacao.itemConfiguracao.gestorSet}">
					        <p>
					        <c:choose>
					            <c:when test="${not empty g.dpPessoa}">
					                <a href="/siga/app/pessoa/exibir?sigla=${g.dpPessoa.siglaCompleta}" target="${g.dpPessoa.siglaCompleta}">${g.dpPessoa.nomePessoa}</a>
					            </c:when>
					            <c:otherwise>
					                <a href="/siga/app/lotacao/exibir?sigla=${g.dpLotacao.siglaCompleta}" target="${g.dpLotacao.siglaCompleta}">${g.dpLotacao.siglaCompleta} - ${g.dpLotacao.nomeLotacao}</a>
					            </c:otherwise>
					        </c:choose>
					        </p>
					    </c:forEach>
					</div>	
				</c:if>
				<!-- CONHECIMENTOS RELACIONADOS -->
				<div style="display: inline-block" >
					<div id="gc-ancora-item"></div>
				</div>
				<script>
					var tituloItem = '${f:urlEncode(solicitacao.itemConfiguracao.tituloItemConfiguracao)}';
					var gcTag = '${solicitacao.itemConfiguracao.gcTagAbertura}';
					exibirConhecimentoRelacionadoAoItem(tituloItem, gcTag);
				</script>
			</c:if>
		</c:if>
		<c:set var="acoesEAtendentes" value="${solicitacao.acoesEAtendentes}" />
		<c:if test="${not empty solicitacao.itemConfiguracao && not empty acoesEAtendentes}"> 
			<div class="gt-form-row" style="margin-top: 10px;">
				<label>A&ccedil;&atilde;o</label>	
				<select name="solicitacao.acao.id" id="selectAcao" onchange="carregarAcao();">
					<c:if test="${metodo == 'editar'}">
						<option value=""></option>
					</c:if>	
					<c:forEach items="${acoesEAtendentes.keySet()}" var="cat">
						<optgroup  label="${cat.tituloAcao}">
							<c:forEach items="${acoesEAtendentes.get(cat)}" var="tarefa">
								<option value="${tarefa.acao.idAcao}" ${solicitacao.acao.idAcao.equals(tarefa.acao.idAcao) ? 'selected' : ''}> 
									${tarefa.acao.tituloAcao}
									<c:if test="${exibeLotacaoNaAcao}">(${tarefa.conf.atendente.lotacaoAtual.siglaCompleta})</c:if>
								</option>
							</c:forEach>					 
						</optgroup>
					</c:forEach>
				</select>
				<br/><span id="acaoNaoInformada" class="error" style="color: red; display: none;">Ação não informada</span>
			</div>
			<c:if test="${exibeLotacaoNaAcao}">
				<div class="gt-form-row" style="margin-top: 10px;">
					<!-- Necessario listar novamente a lista "acoesEAtendentes" para ter a lotacao designada da cada acao
							ja que acima no select nao tem como "esconder" essa informacao -->
					<c:forEach items="${acoesEAtendentes.keySet()}" var="cat" varStatus="catPosition">
						<c:forEach items="${acoesEAtendentes.get(cat)}" var="t" varStatus="tPosition">
							<span class="idDesignacao-${t.acao.idAcao}" style="display:none;">${t.conf.idConfiguracao}</span>
							<span class="lotacao-${t.acao.idAcao}" style="display:none;">${t.conf.atendente.siglaCompleta} 
												- ${t.conf.atendente.descricao}</span>
							<span class="idLotacao-${t.acao.idAcao}" style="display:none;">${t.conf.atendente.idLotacao}</span>
						</c:forEach>
					</c:forEach>
			
					<label id="labelAtendentePadrao">Atendente</label>
					<span id="atendentePadrao" style="display:block;"></span>
					<input type="hidden" name="solicitacao.designacao.id" id="idDesignacao" value="" />
					<input type="hidden" name="atendente.id" id="idAtendente" value="" />
				</div>
				<c:if test="${metodo == 'escalonar'}">
					<a href="javascript: modalAbrir('lotacaoAtendente')" class="gt-btn-medium" style="margin: 5px 0 0 -3px;">
						Alterar atendente
					</a>
				</c:if>
			</c:if>
			<div id="divAtributos" depende="solicitacao.acao">
				<c:if test="${exibeConhecimento}">
					<c:if test="${not empty solicitacao.itemConfiguracao && not empty solicitacao.acao && podeUtilizarServicoSigaGC}">
						<!-- CONHECIMENTOS RELACIONADOS -->
						<div style="display: inline-block" >
							<div id="gc-ancora-item-acao"></div>
						</div>
						<script>
							var tituloItemAcao = '${f:urlEncode(solicitacao.gcTituloAbertura)}';
							var gcTag = '${solicitacao.gcTagAbertura}';
							exibirConhecimentoRelacionadoAoItemEAcao(tituloItemAcao, gcTag);
						</script>
					</c:if>	
				</c:if>
				<sigasr:atributo atributoSolicitacaoMap="${atributoSolicitacaoMap}" 
					atributoAssociados="${atributoAssociados}"
					entidade="solicitacao" />
			</div>
		</c:if>
	</div>
</div>
<script>
$(document).ready(function($) {
	removerAcaoRepetida();
	selecionarOpcaoDefault();
	carregarLotacaoDaAcao();
});

function validarCampos() {
	$("#itemNaoInformado").hide();
	$("#erroJustificativa").hide();

	if (!$("#formulario_solicitacaoitemConfiguracao_id").val()){
		$("#itemNaoInformado").show();
		addMensagemErroGeral();
		return false;
	}
	if (!$("#selectAcao").val()) {
		$("#acaoNaoInformada").show();
		addMensagemErroGeral();
		return false;
	}
	return true;
}

function carregarAcao() {
	var idSelecionado = $("#${metodo} #selectAcao").find(":selected").val();
	if (typeof idSelecionado !== 'undefined') {
		carregarLotacaoDaAcao();
		removerAcaoRepetida();
		apagarMsgErroFechamentoAutomatico();
		sbmt('solicitacao.acao', postbackURL()+'&solicitacao.acao.id='+idSelecionado, false, null);
	}
}
function apagarMsgErroFechamentoAutomatico() {
	$('#erroCheckFechadoAuto').hide();
	if (typeof onchangeCheckFechaAutomatico === 'function')
		onchangeCheckFechaAutomatico();
}
function dispararFuncoesOnBlurItem() {
	var executarFuncao = carregarLotacaoDaAcao;
	$('#itemNaoInformado').hide();
	if ('${metodo}' !== 'editar')
		executarFuncao = carregarAcao;
	sbmt('solicitacao.itemConfiguracao', null, false, executarFuncao);
}

var inputHandler = function() {
	mensagemHandler("keyup", "input");
}

var selectHandler = function() {
	mensagemHandler("change", "select");
}

var textareaHandler = function() {
	mensagemHandler("keyup", "textarea");
}

function mensagemHandler(evento, targetElement) {
	$("#${metodo}").closest("form").on(evento, targetElement, function() {
		event.stopPropagation();
		$(this).siblings("span.error").text("");
		removerMensagemErroGeral();
	});
}

function limparMensagemListener() {
	inputHandler();
	selectHandler();
	textareaHandler();
}

function addMensagemErroGeral() {
	$('div.error-message').find('p')
		.addClass('gt-error')
		.text('Alguns campos obrigatórios não foram preenchidos. Verificar mensagens abaixo.');

	$('html, body').animate({ scrollTop: 0 }, 'fast');
}

function removerMensagemErroGeral() {
	$('.error-message').find('p')
		.removeClass('gt-error')
		.text('');	
}

function removerAcaoRepetida() {
	if ('${exibeLotacaoNaAcao}' !== 'true') {
		$("#${metodo} #selectAcao option").each(function(){
			  $(this).siblings("[value='"+ this.value+"']").remove();
		});
	}
}

function selecionarOpcaoDefault() {
	$("#${metodo} #selectAcao option[selected]").prop('selected', true);
}

function exibirConhecimentoRelacionadoAoItem(titulo, gcTag) {
	carregarConhecimento(titulo, gcTag, $("#gc-ancora-item"));
}

function exibirConhecimentoRelacionadoAoItemEAcao(titulo, gcTag) {
	carregarConhecimento(titulo, gcTag, $("#gc-ancora-item-acao"));
}

function carregarConhecimento(titulo, gcTag, div) {
	var url = "/../sigagc/app/knowledgeInplace?testarAcesso=true&popup=true&podeCriar=${exibirMenuConhecimentos}&msgvazio=empty" +
	"&titulo=" + titulo + gcTag + "&pagina=exibir";
	
	Siga.ajax(url, null, "GET", function(response) {
		div.html(response);
	});
}

function getLotacaoDaAcao(conteudoAcao) {
	var regExp = /\(([^)]+)\)/g;
	var matches = conteudoAcao.match(regExp);
	var lotacoes = [];
	for (var i = 0; i < matches.length; i++) {
	    var str = matches[i];
	    var texto = str.substring(1, str.length - 1);
	    if (texto.length > 2 && (texto.substring(0, 2) == "ES" || texto.substring(0, 2) == "T2" 
	        || texto.substring(0, 2) == "RJ")){
	    	lotacoes[i]= texto;
	    }
	}
	return lotacoes[lotacoes.length-1];
}

function carregarLotacaoDaAcao() {
	if ('${exibeLotacaoNaAcao}' === 'true') {
		//preenche o campo atendente com a lotacao designada a cada alteracao da acao 
		var opcaoSelecionada = $("#${metodo} #selectAcao option:selected");
		if (typeof opcaoSelecionada.html() !== 'undefined' && opcaoSelecionada.html() !== '') {
			var idAcao = opcaoSelecionada.val();
			var siglaLotacao = getLotacaoDaAcao(opcaoSelecionada.html()); 
			var spanLotacao = $(".lotacao-" + idAcao + ":contains(" + siglaLotacao + ")");
			var descLotacao = spanLotacao.html();
			var idLotacao = spanLotacao.next().html();
			var idDesignacaoDaAcao = spanLotacao.prev().html();
			
			definirDesignacaoEAtendente(idDesignacaoDaAcao, descLotacao, idLotacao);
			$("#labelAtendentePadrao").show();
		}
		else {
			definirDesignacaoEAtendente('', '', '');
			$("#labelAtendentePadrao").hide();
		}
	}
}

function definirDesignacaoEAtendente(idDesignacaoDaAcao, descLotacao, idLotacao) {
	$("#idDesignacao").val(idDesignacaoDaAcao);
	$("#atendentePadrao").html(descLotacao);
	$("#idAtendente").val(idLotacao);
	//garante que quando alterar a acao o atendenteNaoDesignado fique vazio
	$("#atendenteNaoDesignado").val('');
}

// retirar esse metodo daqui. Nao esta intuitivo que ele existe dentro do classificacao.tag
function gravar() {
	limparMensagemListener();
	
	if (!validarCampos()) 
		return false; 
	
	var formJquery = $("#${metodo}").closest("form");
	var formDOM = formJquery.get(0);
	var formData = new FormData(formDOM);
	$.ajax({
    	type: "POST",
    	url: submitURL(),
    	data: formData,
    	processData: false,
    	contentType: false,
    	"beforeSend": function () {
    		jQuery.blockUI(objBlock);
    	},
    	success: function(response) {
        	window.location.href = "${linkTo[SolicitacaoController].exibir}" + response;
    	},
    	error: function(response) {
        	//criar funcao separa para tratar dos erros  		
			if (response.getResponseHeader('Validation') === "true") {
				addMensagemErroGeral();
				$("span.error").remove();
	        	var errors = JSON.parse(response.responseText);
	      		for(var i = 0; i < errors.length; i++) {
	     			var span = $('&nbsp<span class="error" style="color: red"></span>');
	     			span.html(errors[i].message);
	      			span.insertAfter($("[name='" + errors[i].category + "']"));
	      		}
			}
			else 
				carregouAjaxserver_error(response.responseText, $("#responseText"));
			jQuery.unblockUI();
        }
   	});
}
</script>