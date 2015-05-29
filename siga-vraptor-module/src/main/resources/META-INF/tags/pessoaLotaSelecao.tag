<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<%@ attribute name="nomeSelPessoa" required="false"%>
<%@ attribute name="nomeSelLotacao" required="false"%>
<%@ attribute name="valuePessoa" required="false"%>
<%@ attribute name="valueLotacao" required="false"%>
<%@ attribute name="disabled" required="false"%>
<%@ attribute name="requiredValue" required="false"%>

<c:set var="nomeSelPessoaClean" value="${fn:replace(nomeSelPessoa,'.','')}" />
<c:set var="nomeSelLotacaoClean" value="${fn:replace(nomeSelLotacao,'.','')}" />

<c:set var="desativar" value="nao"></c:set>
<c:if test="${disabled == 'sim'}">
	<c:set var="pessoaLotaSelecaoDisabled" value="disabled='disabled'" scope="request"/>
	<c:set var="desativar" value="sim"></c:set>
</c:if>

<select id="${requestScope._nomeSelPessoaClean}${requestScope._nomeSelLotacaoClean}" onchange="" ${pessoaLotaSelecaoDisabled} >
  <option value="1">Pessoa</option>
  <option value="2">LotaÃ§Ã£o</option>
</select>

<span id="spanPessoa${requestScope._nomeSelPessoaClean}">
	<siga:selecao tipo="pessoa" propriedade="pessoa" tema="simple" modulo="siga" inputName="${nomeSelPessoaClean}" 
		 urlAcao="buscar" desativar="${desativar}" siglaInicial="${valuePessoa}"/>
</span>

<span id="spanLotacao${requestScope._nomeSelLotacaoClean}">
	<siga:selecao tipo="lotacao" propriedade="lotacao" tema="simple" modulo="siga" inputName="${nomeSelLotacaoClean}" 
		 urlAcao="buscar" desativar="${desativar}" siglaInicial="${valueLotacao}"/>
</span>
<script language="javascript">
var select = document.getElementById('${requestScope._nomeSelPessoaClean}${requestScope._nomeSelLotacaoClean}');
if (document.getElementById('${requestScope._nomeSelPessoaClean}').value)
	select.value = 1;
else select.value= 2;
// O onchange tem de ser definido da forma abaixo porque, quando esta tag esta dentro de um codigo
// carregado por ajax, nao funciona o tratamento do modo tradicional (onchange="", etc)
// http://stackoverflow.com/questions/8893786/uncaught-referenceerror-x-is-not-defined
select.onchange = function(){
	var select = document.getElementById('${requestScope._nomeSelPessoaClean}${requestScope._nomeSelLotacaoClean}');

	if (select.value == '1'){
		document.getElementById('spanLotacao${requestScope._nomeSelLotacaoClean}').style.display = 'none';
		document.getElementById('spanPessoa${requestScope._nomeSelPessoaClean}').style.display = 'inline';
		document.getElementById('${requestScope._nomeSelLotacaoClean}').value='';
		document.getElementById('${requestScope._nomeSelLotacaoClean}_sigla').value='';
		document.getElementById('${requestScope._nomeSelLotacaoClean}_descricao').value='';
		document.getElementById('${requestScope._nomeSelLotacaoClean}Span').innerHTML='';
	} else if (select.value == '2'){
		document.getElementById('spanPessoa${requestScope._nomeSelPessoaClean}').style.display = 'none';
		document.getElementById('spanLotacao${requestScope._nomeSelLotacaoClean}').style.display = 'inline';
		document.getElementById('${requestScope._nomeSelPessoaClean}').value='';
		document.getElementById('${requestScope._nomeSelPessoaClean}_sigla').value='';
		document.getElementById('${requestScope._nomeSelPessoaClean}_descricao').value='';
		document.getElementById('${requestScope._nomeSelPessoaClean}Span').innerHTML='';
	}
}
select.onchange();
</script>