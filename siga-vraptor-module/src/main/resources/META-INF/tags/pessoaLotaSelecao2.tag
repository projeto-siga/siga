<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<%@ attribute name="propriedadePessoa" required="true"%>
<%@ attribute name="propriedadeLotacao" required="true"%>
<%@ attribute name="disabled" required="false"%>

<c:set var="propriedadePessoaClean"
	value="${fn:replace(propriedadePessoa,'.','')}" />
<c:set var="propriedadeLotacaoClean"
	value="${fn:replace(propriedadeLotacao,'.','')}" />

<c:set var="desativar" value="nao"></c:set>
<c:if test="${disabled == 'sim'}">
	<c:set var="pessoaLotaSelecaoDisabled" value="disabled='disabled'"
		scope="request" />
	<c:set var="desativar" value="sim"></c:set>
</c:if>

<select id="${propriedadePessoaClean}${propriedadeLotacaoClean}" onchange=""
	${pessoaLotaSelecaoDisabled}>
	<option value="1">Pessoa</option>
	<option value="2">Lotação</option>
</select>

<span id="spanPessoa${propriedadePessoaClean}"> <siga:selecao2
		tipo="pessoa" propriedade="${propriedadePessoa}" tema="simple" modulo="siga"
		desativar="${desativar}" />
</span>

<span id="spanLotacao${propriedadeLotacaoClean}"> <siga:selecao2
		tipo="lotacao" propriedade="${propriedadeLotacao}" tema="simple" modulo="siga"
		desativar="${desativar}" />
</span>
<script language="javascript">
	var select = document
			.getElementById('${propriedadePessoaClean}${propriedadeLotacaoClean}');
	if (document
			.getElementById('formulario_${propriedadePessoaClean}_id').value)
		select.value = 1;
	else
		select.value = 2;
	// O onchange tem de ser definido da forma abaixo porque, quando esta tag está dentro de um código
	// carregado por ajax, não funciona o tratamento do modo tradicional (onchange="", etc)
	// http://stackoverflow.com/questions/8893786/uncaught-referenceerror-x-is-not-defined
	select.onchange = function() {
		var select = document
				.getElementById('${propriedadePessoaClean}${propriedadeLotacaoClean}');

		if (select.value == '1') {
			document.getElementById('spanLotacao${propriedadeLotacaoClean}').style.display = 'none';
			document.getElementById('spanPessoa${propriedadePessoaClean}').style.display = 'inline';
			document.getElementById('formulario_${propriedadeLotacaoClean}_id').value = '';
			document.getElementById('formulario_${propriedadeLotacaoClean}_sigla').value = '';
			document.getElementById('formulario_${propriedadeLotacaoClean}_descricao').value = '';
			document.getElementById('${propriedadeLotacaoClean}Span').innerHTML = '';
		} else if (select.value == '2') {
			document.getElementById('spanPessoa${propriedadePessoaClean}').style.display = 'none';
			document.getElementById('spanLotacao${propriedadeLotacaoClean}').style.display = 'inline';
			document.getElementById('formulario_${propriedadePessoaClean}_id').value = '';
			document.getElementById('formulario_${propriedadePessoaClean}_sigla').value = '';
			document.getElementById('formulario_${propriedadePessoaClean}_descricao').value = '';
			document.getElementById('${propriedadePessoaClean}Span').innerHTML = '';
		}
	}
	select.onchange();
</script>