<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<div class="gt-content-box gt-form">
<form action="#" method="post" enctype="multipart/form-data" id="frm">
	<input type="hidden" name="todoOContexto" value="${todoOContexto}" />
	<input type="hidden" name="ocultas" value="${ocultas}" />
	<input type="hidden" id="sigla" name="solicitacao.codigo" value="${siglaCompacta}" />
	<div id="reclassificacao" class="gt-form-row">
		<label>Produto, Servi&ccedil;o ou Sistema relacionado &agrave; Solicita&ccedil;&atilde;o</label>
		<siga:selecao2 propriedade="solicitacao.itemConfiguracao" 
			tipo="itemConfiguracao" 
			tema="simple" 
			modulo="sigasr" 
			tamanho="grande"
			onchange="dispararFuncoesOnBlurItem();" 
			paramList="sol.solicitante.id=${solicitante.idPessoa};sol.local.id=${local.idComplexo};sol.titular.id=${cadastrante.idPessoa};sol.lotaTitular.id=${lotaTitular.idLotacao}" />
		<br/><span id="itemNaoInformado" style="color: red; display: none;">Item não informado</span>
		<br/>
		<div id="divAcao" depende="solicitacao.itemConfiguracao">
			
			<c:set var="acoesEAtendentes" value="${solicitacao.acoesEAtendentes}" />
			<c:if test="${not empty solicitacao.itemConfiguracao && not empty acoesEAtendentes}"> 
				<div class="gt-form-row">
					<label>A&ccedil;&atilde;o</label>	
					<select name="solicitacao.acao.id" id="selectAcao" onchange="carregarAcao();">
						<c:forEach items="${acoesEAtendentes.keySet()}" var="cat">
							<optgroup  label="${cat.tituloAcao}">
								<c:forEach items="${acoesEAtendentes.get(cat)}" var="tarefa">
									<option value="${tarefa.acao.idAcao}" ${solicitacao.acao.idAcao.equals(tarefa.acao.idAcao) ? 'selected' : ''}> ${tarefa.acao.tituloAcao}</option>
								</c:forEach>					 
							</optgroup>
						</c:forEach>
					</select>
					<br/><span id="acaoNaoInformada" style="color: red; display: none;">Ação não informada</span>
				</div>
			</c:if>
			<div id="divAtributos" depende="solicitacao.acao">	
				<sigasr:atributo atributoSolicitacaoMap="${atributoSolicitacaoMap}" 
					atributoAssociados="${atributoAssociados}"
					entidade="solicitacao" />
			</div>
		</div>
	</div>
	<div class="gt-form-row">
		<input type="button" value="Gravar" class="gt-btn-medium gt-btn-left" onclick="gravar()"/>
	</div>
</form>
</div>

<script>
$(document).ready(function($) {
	//inicializa valores default para serem usados na function valorInputMudou()
	window.item_default = $("#formulario_solicitacaoitemConfiguracao_id").val();
});
// param_1: id do input que deseja verificar se mudou do valor default
// param_2: tipo de input (solicitante, item...)
function valorInputMudou(id, tipo){
	var input = $("#"+ id);
	if (input.val() != window[tipo + '_default']) {
		window[tipo + '_default'] = input.val(); 
		return true;
	}
	return false;
}

function postbackURL(){
	return '${linkTo[SolicitacaoController].reclassificar}?solicitacao.codigo='+$("#sigla").val()
		+'&solicitacao.itemConfiguracao.id='+$("#formulario_solicitacaoitemConfiguracao_id").val();
}
function validarReclassificacao() {
	$("#itemNaoInformado").hide();
	$("#erroJustificativa").hide();

	if (!$("#formulario_solicitacaoitemConfiguracao_id").val()){
		$("#itemNaoInformado").show();
		return false;
	}
	if (!$("#selectAcao").val()) {
		$("#acaoNaoInformada").show();
		return false;
	}
	return true;
}

function carregarAcao() {
	sbmt('solicitacao.acao', postbackURL()+'&solicitacao.acao.id='+$("#selectAcao").val(), true);
}

function dispararFuncoesOnBlurItem() {
	if (valorInputMudou('formulario_solicitacaoitemConfiguracao_id', 'item')) {
		$('#itemNaoInformado').hide();
		sbmt('solicitacao.itemConfiguracao', null, false, carregarAcao);
	}	
}

function gravar() {
	if (!validarReclassificacao()) 
		return false; 
	jQuery.blockUI(objBlock);
	$.ajax({
    	type: "POST",
    	url: "${linkTo[SolicitacaoController].reclassificarGravar}",
    	data: $('#frm').serialize(),
    	dataType: "text",
    	"beforeSend": function () {
    		jQuery.blockUI(objBlock);
    	},
		"complete": function () {
			jQuery.unblockUI();
		},
    	success: function(response) {
			window.location.href = "${linTo[SolicitacaoController].exibir}?sigla=${siglaCompacta}";
    	},
    	error: function(response) {
        	var responseHtml = $(response.responseText).find("#reclassificacao").html();
        	$("#reclassificacao").replaceWith(responseHtml);
    	}
   	});
}
</script>