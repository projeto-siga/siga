<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="nome" required="false"%>
<%@ attribute name="nomeSelPessoa" required="false"%>
<%@ attribute name="nomeSelLotacao" required="false"%>
<%@ attribute name="valuePessoa" required="false"%>
<%@ attribute name="valueLotacao" required="false"%>
<%@ attribute name="disabled" required="false"%>
<%@ attribute name="requiredValue" required="false"%>

<%
	String _nomeSelPessoaClean = ((String)jspContext.getAttribute("nomeSelPessoa")).replaceAll("\\.","");
	String _nomeSelLotacaoClean = nomeSelLotacao.replaceAll("\\.","");
	
	request.setAttribute("_nomeSelPessoaClean", _nomeSelPessoaClean);
	request.setAttribute("_nomeSelLotacaoClean", _nomeSelLotacaoClean);
%>
<c:if test="${disabled == 'sim'}">
	<c:set var="pessoaLotaSelecaoDisabled" value="disabled" scope="request"/>
</c:if>

<select id="${requestScope._nomeSelPessoaClean}${requestScope._nomeSelLotacaoClean}" onchange="" ${pessoaLotaSelecaoDisabled} >
  <option value="1">Pessoa</option>
  <option value="2">Lotação</option>
</select>

<span id="spanPessoa${requestScope._nomeSelPessoaClean}">
	<siga:selecao tipo="pessoa" propriedade="pessoa" tema="simple" modulo="siga" inputName="${nomeSelPessoa}" 
		 urlAcao="buscar" desativar="${disabled}" siglaInicial="${valuePessoa}" idInicial="${valuePessoa.id}" 
		 descricaoInicial="${valuePessoa.descricao}"/>
</span>

<span id="spanLotacao${requestScope._nomeSelLotacaoClean}">
	<siga:selecao tipo="lotacao" propriedade="lotacao" tema="simple" modulo="siga" inputName="${nomeSelLotacao}" 
		 urlAcao="buscar" desativar="${disabled}" siglaInicial="${valueLotacao}" idInicial="${valuevalueLotacaoPessoa.id}" 
		 descricaoInicial="${valueLotacao.descricao}"/>
</span>
<script language="javascript">
var select = document.getElementById('${requestScope._nomeSelPessoaClean}${requestScope._nomeSelLotacaoClean}');
if (document.getElementById('${requestScope._nomeSelPessoaClean}').value)
	select.value = 1;
else select.value= 2;
// O onchange tem de ser definido da forma abaixo porque, quando esta tag está dentro de um código
// carregado por ajax, não funciona o tratamento do modo tradicional (onchange="", etc)
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