<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> --%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<%@ attribute name="nomeSelPessoa" required="false"%>
<%@ attribute name="nomeSelLotacao" required="false"%>
<%@ attribute name="nomeSelFuncao" required="false"%>
<%@ attribute name="nomeSelCargo" required="false"%>
<%@ attribute name="nomeSelGrupo" required="false"%>

<%@ attribute name="valuePessoa" required="false"%>
<%@ attribute name="valueLotacao" required="false"%>
<%@ attribute name="valueFuncao" required="false"%>
<%@ attribute name="valueCargo" required="false"%>
<%@ attribute name="valueGrupo" required="false"%>

<%@ attribute name="disabled" required="false"%>
<%@ attribute name="onchange" required="false"%>
<%@ attribute name="cssClass" required="false"%>
<%@ attribute name="id" required="false"%>

<c:set var="desativar" value="nao"></c:set>
<c:if test="${disabled == 'true'}">
	<c:set var="pessoaLotaFuncCargoSelecaoDisabled" value="disabled='disabled'"></c:set>
	<c:set var="desativar" value="sim"></c:set>
</c:if>

<select id="${nomeSelPessoa}${nomeSelLotacao}${nomeSelFuncao}${nomeSelCargo}${nomeSelGrupo}" onchange="" ${pessoaLotaFuncCargoSelecaoDisabled} class="form-control mb-1" >
  <c:if test="${not empty nomeSelPessoa}"><option value="1">Pessoa</option></c:if>
  <c:if test="${not empty nomeSelLotacao}"><option value="2">Lotação</option></c:if>
  <c:if test="${not empty nomeSelFuncao}"><option value="3">Função</option></c:if>
  <c:if test="${not empty nomeSelCargo}"><option value="4">Cargo</option></c:if>
  <c:if test="${not empty nomeSelGrupo}"><option value="5">Grupo</option></c:if>
</select>

<c:if test="${not empty nomeSelPessoa}">
<span id="spanPessoa${nomeSelPessoa}">
	<input type="hidden" name="${nomeSelPessoa}" id="${nomeSelPessoa}" class="pessoaLotaFuncCargoSelecao">
	<sigasr:selecao propriedade="${nomeSelPessoa}" tema="simple" modulo="siga"
		 urlAcao="buscar" desativar="${desativar}" siglaInicial="${valuePessoa}"/>
</span>
</c:if>

<c:if test="${not empty nomeSelLotacao}">
<span id="spanLotacao${nomeSelLotacao}">
	<input type="hidden" name="${nomeSelLotacao}" id="${nomeSelLotacao}" class="pessoaLotaFuncCargoSelecao">
	<sigasr:selecao propriedade="${nomeSelLotacao}" tema="simple" modulo="siga"
		 urlAcao="buscar" desativar="${desativar}" siglaInicial="${valueLotacao}"/>
</span>
</c:if>

<c:if test="${not empty nomeSelFuncao}">
<span id="spanFuncao${nomeSelFuncao}">
	<input type="hidden" name="${nomeSelFuncao}" id="${nomeSelFuncao}" class="pessoaLotaFuncCargoSelecao">
	<sigasr:selecao propriedade="${nomeSelFuncao}" tema="simple" modulo="siga"
		 urlAcao="buscar" desativar="${desativar}" siglaInicial="${valueFuncao}"/>
</span>
</c:if>

<c:if test="${not empty nomeSelCargo}">
<span id="spanCargo${nomeSelCargo}">
	<input type="hidden" name="${nomeSelCargo}" id="${nomeSelCargo}" class="pessoaLotaFuncCargoSelecao">
	<sigasr:selecao propriedade="${nomeSelCargo}" tema="simple" modulo="siga"
		 urlAcao="buscar" desativar="${desativar}" siglaInicial="${valueCargo}"/>
</span>
</c:if>

<c:if test="${not empty nomeSelGrupo}">
<span id="spanGrupo${nomeSelGrupo}">
	<input type="hidden" name="${nomeSelGrupo}" id="${nomeSelGrupo}" class="pessoaLotaFuncCargoSelecao">
	<sigasr:selecao propriedade="${nomeSelGrupo}" tema="simple" modulo="siga"
		 urlAcao="buscar" desativar="${desativar}" siglaInicial="${valueGrupo}"/>
</span>
</c:if>

<script language="javascript">

var select = document.getElementById('${nomeSelPessoa}${nomeSelLotacao}${nomeSelFuncao}${nomeSelCargo}${nomeSelGrupo}');

// O onchange tem de ser definido da forma abaixo porque, quando esta tag estÃ¡ dentro de um cÃ³digo
// carregado por ajax, nÃ£o funciona o tratamento do modo tradicional (onchange="", etc)
// http://stackoverflow.com/questions/8893786/uncaught-referenceerror-x-is-not-defined
function limparPessoa() {
	<c:if test="${not empty nomeSelPessoa}">
	document.getElementById('spanPessoa${nomeSelPessoa}').style.display = 'none';
	document.getElementById('formulario_${nomeSelPessoa}Sel_id').value='';
	document.getElementById('formulario_${nomeSelPessoa}Sel_sigla').value='';
	document.getElementById('formulario_${nomeSelPessoa}Sel_descricao').value='';
	document.getElementById('${nomeSelPessoa}SelSpan').innerHTML='';
	</c:if>
}

function limparLotacao() { 
	<c:if test="${not empty nomeSelLotacao}">
	document.getElementById('spanLotacao${nomeSelLotacao}').style.display = 'none';
	document.getElementById('formulario_${nomeSelLotacao}Sel_id').value='';
	document.getElementById('formulario_${nomeSelLotacao}Sel_sigla').value='';
	document.getElementById('formulario_${nomeSelLotacao}Sel_descricao').value='';
	document.getElementById('${nomeSelLotacao}SelSpan').innerHTML='';
	</c:if>
}

function limparFuncao() {
	<c:if test="${not empty nomeSelFuncao}">
	document.getElementById('spanFuncao${nomeSelFuncao}').style.display = 'none';
	document.getElementById('formulario_${nomeSelFuncao}Sel_id').value='';
	document.getElementById('formulario_${nomeSelFuncao}Sel_sigla').value='';
	document.getElementById('formulario_${nomeSelFuncao}Sel_descricao').value='';
	document.getElementById('${nomeSelFuncao}SelSpan').innerHTML='';
	</c:if>
}

function limparCargo() {
	<c:if test="${not empty nomeSelCargo}">
	document.getElementById('spanCargo${nomeSelCargo}').style.display = 'none';
	document.getElementById('formulario_${nomeSelCargo}Sel_id').value='';
	document.getElementById('formulario_${nomeSelCargo}Sel_sigla').value='';
	document.getElementById('formulario_${nomeSelCargo}Sel_descricao').value='';
	document.getElementById('${nomeSelCargo}SelSpan').innerHTML='';
	</c:if>
}

function limparGrupo() {
	<c:if test="${not empty nomeSelGrupo}">
	document.getElementById('spanGrupo${nomeSelGrupo}').style.display = 'none';
	document.getElementById('formulario_${nomeSelGrupo}Sel_id').value='';
	document.getElementById('formulario_${nomeSelGrupo}Sel_sigla').value='';
	document.getElementById('formulario_${nomeSelGrupo}Sel_descricao').value='';
	document.getElementById('${nomeSelGrupo}SelSpan').innerHTML='';
	</c:if>
}

select.onchange = function(){
	var select = document.getElementById('${nomeSelPessoa}${nomeSelLotacao}${nomeSelFuncao}${nomeSelCargo}${nomeSelGrupo}');

	if (select.value == '1'){
		document.getElementById('spanPessoa${nomeSelPessoa}').style.display = 'inline';
		limparLotacao();
		limparFuncao();
		limparCargo();
		limparGrupo();
	} else if (select.value == '2'){
		document.getElementById('spanLotacao${nomeSelLotacao}').style.display = 'inline';
		limparPessoa();
		limparFuncao();
		limparCargo();
		limparGrupo();
	} else if (select.value == '3'){
		document.getElementById('spanFuncao${nomeSelFuncao}').style.display = 'inline';
		limparPessoa();
		limparLotacao();
		limparCargo();
		limparGrupo();
	} else if (select.value == '4'){
		document.getElementById('spanCargo${nomeSelCargo}').style.display = 'inline';
		limparPessoa();
		limparLotacao();
		limparFuncao();
		limparGrupo();
	} else if (select.value == '5'){
		document.getElementById('spanGrupo${nomeSelGrupo}').style.display = 'inline';
		limparPessoa();
		limparLotacao();
		limparFuncao();
		limparCargo();
	}
}

select.clearAll = function() {
	limparPessoa();
	limparLotacao();
    limparFuncao();
    limparCargo();
    limparGrupo();
}

select.changeValue = function(newValue) {
	select.value = newValue;
	select.onchange();
}
select.onchange();
</script>