<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<c:if test="${not empty solicitacao.solicitante}">
<script>

$(document).ready(function() {
    notificarCampoMudou('#solicitacaoitemConfiguracao', 'Item', 'solicitacao.itemConfiguracao');
});
function dispararFuncoesOnBlurItem() {
	if (valorInputMudou('formulario_solicitacaoitemConfiguracao_id', 'item')) {
		carregarAcao();
		notificarCampoMudou('#solicitacaoitemConfiguracao', 'Item', 'solicitacao.itemConfiguracao');
	}	
}

function carregarAcao(){
    jQuery.blockUI(objBlock);
    frm = document.getElementById('formSolicitacao');
    params = '';
    for (i = 0; i < frm.length; i++){
        if (frm[i].name && frm[i].value)
            params = params + frm[i].name + '=' + encodeURIComponent(frm[i].value) + '&';
    }
    Siga.ajax('${linkTo[SolicitacaoController].exibirAcao}?' + params, null, "GET", function(response){		
    	carregouAcao(response);
	});	
    //PassAjaxResponseToFunction('${linkTo[SolicitacaoController].exibirAcao}?' + params, 'carregouAcao', null, false, null);
}

function carregouAcao(response, param){
    var div = document.getElementById('divAcao');
    div.innerHTML = response;
    var scripts = div.getElementsByTagName("script");
    for(var i=0;i<scripts.length;i++)  
       eval(scripts[i].text);
    jQuery.unblockUI();
}
</script>

<div class="gt-form-row gt-width-66" >
    <label>Produto, Servi&ccedil;o ou Sistema relacionado à Solicita&ccedil;&atilde;o</label>
<%--     <input type="hidden" name="solicitacao.acao" value="${solicitacao.acao.idAcao}" id="idAcaoTeste"> --%>
    <siga:selecao2 propriedade="solicitacao.itemConfiguracao" 
                   tipo="itemConfiguracao" 
                   tema="simple" 
                   modulo="sigasr" 
                   tamanho="grande"
                   onchange="dispararFuncoesOnBlurItem();"
                   paramList="sol.solicitante.id=${solicitacao.solicitante.idPessoa};sol.local.id=${solicitacao.local.idComplexo};sol.titular.id=${titular.idPessoa};sol.lotaTitular.id=${lotaTitular.idLotacao}"/>
    <siga:error name="solicitacao.itemConfiguracao"/>
</div>

<div id="divAcao">
    <jsp:include page="exibirAcao.jsp"></jsp:include>
</div>


</c:if>