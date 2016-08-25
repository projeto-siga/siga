<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<%@ attribute name="metodo" required="true"%>

<div id="${metodo}" class="gt-form-row">
	<label>Produto, Servi&ccedil;o ou Sistema relacionado &agrave; Solicita&ccedil;&atilde;o</label>
	<siga:selecao2 propriedade="solicitacao.itemConfiguracao" 
		tipo="itemConfiguracao" 
		tema="simple" 
		modulo="sigasr" 
		tamanho="grande"
		onchange="dispararFuncoesOnBlurItem();"
		checarInput="true"
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
<script>
function validarCampos() {
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
	$('#itemNaoInformado').hide();
	sbmt('solicitacao.itemConfiguracao', null, false, carregarAcao);
}

function gravar() {
	if (!validarCampos()) 
		return false; 
	$.ajax({
    	type: "POST",
    	url: submitURL(),
    	data: $("form").serialize(),
    	dataType: "text",
    	"beforeSend": function () {
    		jQuery.blockUI(objBlock);
    	},
		"complete": function () {
			jQuery.unblockUI();
		},
    	success: function(response) {
        	${metodo}_fechar();
        	window.location.href = "${linkTo[SolicitacaoController].exibir}${siglaCompacta}";
    	},
    	error: function(response) {
        	var responseHtml = $(response.responseText).find("#${metodo}").html();
        	$("#${metodo}").replaceWith(responseHtml);
    	}
   	});
}
</script>