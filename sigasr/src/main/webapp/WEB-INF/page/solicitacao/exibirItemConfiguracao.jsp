<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<c:if test="${not empty solicitacao.solicitante}">
<script>

$(document).ready(function() {
    notificarCampoMudou('#solicitacaoitemConfiguracao', 'Item', 'solicitacao.itemConfiguracao');
});

function carregarAcao(){
    jQuery.blockUI(objBlock);
    frm = document.getElementById('formSolicitacao');
    params = '';
    for (i = 0; i < frm.length; i++){
        if (frm[i].name && frm[i].value)
            params = params + frm[i].name + '=' + escape(frm[i].value) + '&';
    }
    PassAjaxResponseToFunction('${linkTo[SolicitacaoController].exibirAcao}?' + params, 'carregouAcao', null, false, null);
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
    <label>Produto, Serviço ou Sistema relacionado à Solicitação</label>
    <siga:selecao2 propriedade="solicitacao.itemConfiguracao" 
                   tipo="itemConfiguracao" 
                   tema="simple" 
                   modulo="sigasr" 
                   onchange="carregarAcao();notificarCampoMudou('#solicitacaoitemConfiguracao', 'Item', 'solicitacao.itemConfiguracao')"
                   paramList="sol.solicitante=${solicitacao.solicitante.idPessoa};sol.local=${solicitacao.local.idComplexo};sol.titular=${titular.idPessoa};sol.lotaTitular=${lotaTitular.idLotacao}"/>
    <siga:error name="solicitacao.itemConfiguracao"/>
</div>

<div id="divAcao">
    <jsp:include page="exibirAcao.jsp"></jsp:include>
</div>


</c:if>