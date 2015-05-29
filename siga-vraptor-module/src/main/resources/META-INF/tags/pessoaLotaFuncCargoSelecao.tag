<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

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


<c:set var="nomeSelPessoaClean" value="${fn:replace(nomeSelPessoa,'.','')}" />
<c:set var="nomeSelLotacaoClean" value="${fn:replace(nomeSelLotacao,'.','')}" />
<c:set var="nomeSelFuncaoClean" value="${fn:replace(nomeSelFuncao,'.','')}" />
<c:set var="nomeSelCargoClean" value="${fn:replace(nomeSelCargo,'.','')}" />
<c:set var="nomeSelGrupoClean" value="${fn:replace(nomeSelGrupo,'.','')}" />

<c:set var="desativar" value="nao"></c:set>
<c:if test="${disabled == 'true'}">
	<c:set var="pessoaLotaFuncCargoSelecaoDisabled" value="disabled='disabled'"></c:set>
	<c:set var="desativar" value="sim"></c:set>
</c:if>

<select id="${nomeSelPessoaClean}${nomeSelLotacaoClean}${nomeSelFuncaoClean}${nomeSelCargoClean}${nomeSelGrupoClean}" onchange="" ${pessoaLotaFuncCargoSelecaoDisabled} >
  <option value="1">Pessoa</option>
  <option value="2">Lotação</option>
  <option value="3">Função</option>
  <option value="4">Cargo</option>
  <option value="5">Grupo</option>
</select>

<span id="spanPessoa${nomeSelPessoaClean}">
	<siga:selecao tipo="pessoa" propriedade="pessoa" tema="simple" modulo="siga" inputName="${nomeSelPessoaClean}" 
		 urlAcao="buscar" desativar="${desativar}" siglaInicial="${valuePessoa}"/>
</span>

<span id="spanLotacao${nomeSelLotacaoClean}">
	<siga:selecao tipo="lotacao" propriedade="lotacao" tema="simple" modulo="siga" inputName="${nomeSelLotacaoClean}" 
		 urlAcao="buscar" desativar="${desativar}" siglaInicial="${valueLotacao}"/>
</span>

<span id="spanFuncao${nomeSelFuncaoClean}">
	<siga:selecao tipo="funcao" propriedade="funcao" tema="simple" modulo="siga" inputName="${nomeSelFuncaoClean}" 
		 urlAcao="buscar" desativar="${desativar}" siglaInicial="${valueFuncao}"/>
</span>

<span id="spanCargo${nomeSelCargoClean}">
	<siga:selecao tipo="cargo" propriedade="cargo" tema="simple" modulo="siga" inputName="${nomeSelCargoClean}" 
		 urlAcao="buscar" desativar="${desativar}" siglaInicial="${valueCargo}"/>
</span>

<span id="spanGrupo${nomeSelGrupoClean}">
	<siga:selecao tipo="perfil" propriedade="perfil" tema="simple" modulo="siga" prefix="gi" inputName="${nomeSelGrupoClean}" 
		 urlAcao="buscar" desativar="${desativar}" siglaInicial="${valueGrupo}"/>
</span>


<script language="javascript">

var select = document.getElementById('${nomeSelPessoaClean}${nomeSelLotacaoClean}${nomeSelFuncaoClean}${nomeSelCargoClean}${nomeSelGrupoClean}');

// O onchange tem de ser definido da forma abaixo porque, quando esta tag estÃ¡ dentro de um cÃ³digo
// carregado por ajax, nÃ£o funciona o tratamento do modo tradicional (onchange="", etc)
// http://stackoverflow.com/questions/8893786/uncaught-referenceerror-x-is-not-defined
function limparPessoa() {
	document.getElementById('spanPessoa${nomeSelPessoaClean}').style.display = 'none';
	document.getElementById('formulario_${nomeSelPessoaClean}_pessoaSel_sigla').value='';
	document.getElementById('formulario_${nomeSelPessoaClean}_pessoaSel_descricao').value='';
	document.getElementById('pessoa_pessoaSelSpan').innerHTML='';
}

function limparLotacao() {
	document.getElementById('spanLotacao${nomeSelLotacaoClean}').style.display = 'none';
	document.getElementById('formulario_${nomeSelLotacaoClean}_lotacaoSel_sigla').value='';
	document.getElementById('formulario_${nomeSelLotacaoClean}_lotacaoSel_descricao').value='';
	document.getElementById('lotacao_lotacaoSelSpan').innerHTML='';
}

function limparFuncao() {
	document.getElementById('spanFuncao${nomeSelFuncaoClean}').style.display = 'none';
	document.getElementById('formulario_${nomeSelFuncaoClean}_funcaoSel_sigla').value='';
	document.getElementById('formulario_${nomeSelFuncaoClean}_funcaoSel_descricao').value='';
	document.getElementById('funcao_funcaoSelSpan').innerHTML='';
}

function limparCargo() {
	document.getElementById('spanCargo${nomeSelCargoClean}').style.display = 'none';
	document.getElementById('formulario_${nomeSelCargoClean}_cargoSel_sigla').value='';
	document.getElementById('formulario_${nomeSelCargoClean}_cargoSel_descricao').value='';
	document.getElementById('cargo_cargoSelSpan').innerHTML='';
}

function limparGrupo() {
	document.getElementById('spanGrupo${nomeSelGrupoClean}').style.display = 'none';
	document.getElementById('formulario_${nomeSelGrupoClean}_perfilSel_sigla').value='';
	document.getElementById('formulario_${nomeSelGrupoClean}_perfilSel_descricao').value='';
	document.getElementById('perfil_perfilSelSpan').innerHTML='';
}

select.onchange = function(){
	var select = document.getElementById('${nomeSelPessoaClean}${nomeSelLotacaoClean}${nomeSelFuncaoClean}${nomeSelCargoClean}${nomeSelGrupoClean}');

	if (select.value == '1'){
		document.getElementById('spanPessoa${nomeSelPessoaClean}').style.display = 'inline';
		limparLotacao();
		limparFuncao();
		limparCargo();
		limparGrupo();
	} else if (select.value == '2'){
		document.getElementById('spanLotacao${nomeSelLotacaoClean}').style.display = 'inline';
		limparPessoa();
		limparFuncao();
		limparCargo();
		limparGrupo();
	} else if (select.value == '3'){
		document.getElementById('spanFuncao${nomeSelFuncaoClean}').style.display = 'inline';
		limparPessoa();
		limparLotacao();
		limparCargo();
		limparGrupo();
	} else if (select.value == '4'){
		document.getElementById('spanCargo${nomeSelCargoClean}').style.display = 'inline';
		limparPessoa();
		limparLotacao();
		limparFuncao();
		limparGrupo();
		
	} else if (select.value == '5'){
		document.getElementById('spanGrupo${nomeSelGrupoClean}').style.display = 'inline';
		limparPessoa();
		limparLotacao();
		limparFuncao();
		limparCargo();
	}
}

select.onchange();
</script>