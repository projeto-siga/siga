<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script>
$(document).ready(function() {
    notificarCampoMudou('#selectAcao', 'Ação', 'solicitacao.acao');
    removeSelectedDuplicado();
});

function carregarAtributos(){
    jQuery.blockUI(objBlock);
    var frm = document.getElementById('formSolicitacao'),
        params = '';
    carregarLotacaoDaAcao();
    
    for (i = 0; i < frm.length; i++){
        if (frm[i].name && frm[i].value)
            params = params + frm[i].name + '=' + escape(frm[i].value) + '&';
    }

    var url = '${linkTo[SolicitacaoController].exibirAtributos}?' + params;
    Siga.ajax(url, null, "GET", function(response){
        carregouAtributos(response);
    });
}

function carregouAtributos(response){
    var div = document.getElementById('divAtributos');
    div.innerHTML = response;
    var scripts = div.getElementsByTagName("script");
    for(var i=0;i<scripts.length;i++)  
       eval(scripts[i].text);  

    // Para disparar o onchange e preencher os filtros
    $("#atributos").find('input,select').each(function() {
        var me = $(this);
        if(me.val()) {
            var label = $.trim(me.prev('label').html()),
                innerHTML = label + ' - ' + me.val(),
                divFiltro = $('#filtro'),
                selector = "." + me.attr('class'),
                optionVl = me.attr('name');
            
            addFiltro(divFiltro, selector, innerHTML, optionVl)
        }
    });
    //jQuery.unblockUI();
}

function removeSelectedDuplicado() {
    //solucao de contorno temporaria para op??es no select com mesmo value.
    var primeiro = $("#selectAcao option:eq(0)");
    var segundo = $("#selectAcao option:eq(1)");
    if (primeiro.val() == segundo.val()) {
        segundo.prop("selected", false);
        primeiro.prop("selected", true);
    }
}

function carregarLotacaoDaAcao(){
    //preenche o campo atendente com a lotacao designada a cada alteracao da acao 
    var opcaoSelecionada = $("#selectAcao option:selected");
    var idAcao = opcaoSelecionada.val();
    try{
        var siglaLotacao = opcaoSelecionada.html().split("(")[1].slice(0,-2);

        var spanLotacao = $(".lotacao-" + idAcao + ":contains(" + siglaLotacao + ")");
        var descLotacao = spanLotacao.html();
        var idDesignacaoDaAcao = spanLotacao.prev().html();

        $("#solicitacaoDesignacao").val(idDesignacaoDaAcao);
        $("#atendentePadrao").html(descLotacao);
    } catch(err) {
        $("#solicitacaoDesignacao").val('');
        $("#atendentePadrao").html('');
    }
}
</script>
<c:if test="${solicitacao.itemConfiguracao != null && podeUtilizarServicoSigaGC}">
    <div style="display: inline-block" >
        <div id="gc-ancora-item"></div>
    </div>
    <!-- CONHECIMENTOS RELACIONADOS -->
    <script type="text/javascript">
    var url = "/../sigagc/app/knowledge?tags=${solicitacao.itemConfiguracao.gcTagAbertura}&estilo=inplace&testarAcesso=true&popup=true&podeCriar=${exibirMenuConhecimentos}&msgvazio=&titulo=${solicitacao.itemConfiguracao.tituloItemConfiguracao}";
    Siga.ajax(url, null, "GET", function(response){
        $("#gc-ancora-item").html(response);
    });
    </script>
</c:if>

<c:if test="${not empty solicitacao.itemConfiguracao && not empty acoesEAtendentes}" > 
    <div class="gt-form-row gt-width-66">
    	<label>A&ccedil;&atilde;o</label>   
        <select name="solicitacao.acao.id" id="selectAcao" onchange="carregarAtributos();notificarCampoMudou('#selectAcao', 'A&ccedil;&atilde;o', 'solicitacao.acao');">
            <option value="0"></option>
            <c:forEach items="${acoesEAtendentes.keySet()}" var="cat">
                <optgroup label="${cat.tituloAcao}">
                    <c:forEach items="${acoesEAtendentes.get(cat)}" var="tarefa">
                        <option value="${tarefa.acao.idAcao}" ${solicitacao.acao.idAcao.equals(tarefa.acao.idAcao) ? 'selected' : ''}>${tarefa.acao.tituloAcao} (${tarefa.conf.atendente.siglaCompleta})</option>
                    </c:forEach>
                </optgroup>                  
            </c:forEach>
        </select> 
        <siga:error name="solicitacao.acao"/>
    </div>
    <div>
        <!-- Necessario listar novamente a lista "acoesEAtendentes" para ter a lotacao designada da cada acao
                ja que acima no select nao tem como "esconder" essa informacao -->
        <c:forEach items="${acoesEAtendentes.keySet()}" var="cat">
            <c:forEach items="${acoesEAtendentes.get(cat)}" var="t">
	            <span class="idDesignacao-${t.acao.idAcao}" style="display:none;">${t.conf.idConfiguracao}</span>
	            <span class="lotacao-${t.acao.idAcao}" style="display:none;">${t.conf.atendente.siglaCompleta} - ${t.conf.atendente.descricao}</span>
	            
	            <c:if test="${cat_isFirst && cat_isLast && t_isFirst && t_isLast}">
	               <c:set var="lotacaoDesignada" value="${t.conf.atendente.siglaCompleta + ' - ' + t.conf.atendente.descricao}"/>
	               <c:set var="idDesignacao" value="${t.conf.idConfiguracao}"/>
	            </c:if>
	        </c:forEach>
        </c:forEach>
        <label>Atendente</label>
        <span id="atendentePadrao" style="display:block;">
            ${lotacaoDesignada}
        </span>
        <input type="hidden" id="solicitacaoDesignacao" name="solicitacao.designacao.id" value="${idDesignacao}" />
    </div>
</c:if>

<div id="divAtributos">
    <jsp:include page="exibirAtributos.jsp"></jsp:include>
</div>
