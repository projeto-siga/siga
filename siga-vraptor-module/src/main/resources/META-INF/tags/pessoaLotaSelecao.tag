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

<c:set var="desativar" value="nao"></c:set>
<c:if test="${disabled == 'sim'}">
	<c:set var="pessoaLotaSelecaoDisabled" value="disabled='disabled'" scope="request"/>
	<c:set var="desativar" value="sim"></c:set>
</c:if>

<select id="${nomeSelPessoa}${nomeSelLotacao}" onchange="" ${pessoaLotaSelecaoDisabled} >
  <option value="1">Pessoa</option>
  <option value="2">Lota&ccedil;&atilde;o</option>
</select>

<span id="spanPessoa${nomeSelPessoa}">
	<input type="hidden" name="${nomeSelPessoa}" id="${nomeSelPessoa}" class="pessoaLotaSelecao">
	<siga:selecao tipo="pessoa" propriedade="pessoa" tema="simple" modulo="siga" inputName="${nomeSelPessoa}" 
		 urlAcao="buscar" desativar="${desativar}" siglaInicial="${valuePessoa}"/>
</span>

<span id="spanLotacao${nomeSelLotacao}">
	<input type="hidden" name="${nomeSelLotacao}" id="${nomeSelLotacao}" class="pessoaLotaSelecao">
	<siga:selecao tipo="lotacao" propriedade="lotacao" tema="simple" modulo="siga" inputName="${nomeSelLotacao}" 
		 urlAcao="buscar" desativar="${desativar}" siglaInicial="${valueLotacao}"/>
</span>

<script language="javascript">
var select = document.getElementById('${nomeSelPessoa}${nomeSelLotacao}');
if (document.getElementById('${nomeSelPessoa}').value)
	select.value = 1;
else select.value= 2;
// O onchange tem de ser definido da forma abaixo porque, quando esta tag está dentro de um código
// carregado por ajax, não funciona o tratamento do modo tradicional (onchange="", etc)
// http://stackoverflow.com/questions/8893786/uncaught-referenceerror-x-is-not-defined
select.onchange = function(){
	var select = document.getElementById('${nomeSelPessoa}${nomeSelLotacao}');

	if (select.value == '1'){
		document.getElementById('spanLotacao${nomeSelLotacao}').style.display = 'none';
		document.getElementById('spanPessoa${nomeSelPessoa}').style.display = 'inline';
		document.getElementById('${nomeSelLotacao}').value='';
		document.getElementById('${nomeSelLotacao}_sigla').value='';
		document.getElementById('${nomeSelLotacao}_descricao').value='';
		document.getElementById('${nomeSelLotacao}Span').innerHTML='';
	} else if (select.value == '2'){
		document.getElementById('spanPessoa${nomeSelPessoa}').style.display = 'none';
		document.getElementById('spanLotacao${nomeSelLotacao}').style.display = 'inline';
		document.getElementById('${nomeSelPessoa}').value='';
		document.getElementById('${nomeSelPessoa}_sigla').value='';
		document.getElementById('${nomeSelPessoa}_descricao').value='';
		document.getElementById('${nomeSelPessoa}Span').innerHTML='';
	}
}
select.onchange();
</script>