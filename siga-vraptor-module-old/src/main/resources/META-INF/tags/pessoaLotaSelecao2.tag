<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<%@ attribute name="propriedadePessoa" required="true"%>
<%@ attribute name="propriedadeLotacao" required="true"%>
<%@ attribute name="propriedadeEmail" required="false"%>
<%@ attribute name="disabled" required="false"%>

<c:set var="propriedadePessoaClean"
	value="${fn:replace(propriedadePessoa,'.','')}" />
<c:set var="propriedadeLotacaoClean"
	value="${fn:replace(propriedadeLotacao,'.','')}" />
<c:set var="propriedadeEmailClean"
	value="${fn:replace(propriedadeEmail,'.','')}" />

<c:set var="desativar" value="nao"></c:set>
<c:if test="${disabled == 'sim'}">
	<c:set var="pessoaLotaSelecaoDisabled" value="disabled='disabled'"
		scope="request" />
	<c:set var="desativar" value="sim"></c:set>
</c:if>

<select id="${propriedadePessoaClean}${propriedadeLotacaoClean}"
	onchange="" ${pessoaLotaSelecaoDisabled}>
	<option value="1">Pessoa</option>
	<option value="2">Lotação</option>
	<c:if test="${not empty propriedadeEmail}">
		<option value="3">E-mail</option>
	</c:if>
</select>

<span id="spanPessoa${propriedadePessoaClean}"> <siga:selecao2
		tipo="pessoa" propriedade="${propriedadePessoa}" tema="simple"
		modulo="siga" desativar="${desativar}" />
</span>

<span id="spanLotacao${propriedadeLotacaoClean}"> <siga:selecao2
		tipo="lotacao" propriedade="${propriedadeLotacao}" tema="simple"
		modulo="siga" desativar="${desativar}" />
</span>

<c:if test="${not empty propriedadeEmail}">
	<c:set var="inputNameEmail" value="${propriedadeEmail}" />
</c:if>
<span style="display: none;" id="spanEmail${propriedadeEmailClean}">
	<input type='text' name='${inputNameEmail}' size="70"
	id="formulario_${propriedadeEmailClean}" /> Obs.: Ao informar
	v&aacute;rios, separar por espa&ccedil;o.
</span>


<script language="javascript">
	var select = document
			.getElementById('${propriedadePessoaClean}${propriedadeLotacaoClean}');

	if (document.getElementById('formulario_${propriedadePessoaClean}_id').value)
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
			document
					.getElementById('formulario_${propriedadeLotacaoClean}_sigla').value = '';
			document
					.getElementById('formulario_${propriedadeLotacaoClean}_descricao').value = '';

			document.getElementById('${propriedadeLotacaoClean}Span').innerHTML = '';

			document.getElementById('spanEmail${propriedadeEmailClean}').style.display = 'none';
			document.getElementById('formulario_${propriedadeEmailClean}').value = '';
		} else if (select.value == '2') {
			document.getElementById('spanPessoa${propriedadePessoaClean}').style.display = 'none';
			document.getElementById('spanLotacao${propriedadeLotacaoClean}').style.display = 'inline';
			document.getElementById('formulario_${propriedadePessoaClean}_id').value = '';
			document
					.getElementById('formulario_${propriedadePessoaClean}_sigla').value = '';
			document
					.getElementById('formulario_${propriedadePessoaClean}_descricao').value = '';
			document.getElementById('${propriedadePessoaClean}Span').innerHTML = '';

			document.getElementById('spanEmail${propriedadeEmailClean}').style.display = 'none';
			document.getElementById('formulario_${propriedadeEmailClean}').value = '';
		} else if (select.value == '3') {
			document.getElementById('spanLotacao${propriedadeLotacaoClean}').style.display = 'none';
			document.getElementById('formulario_${propriedadeLotacaoClean}_id').value = '';
			document
					.getElementById('formulario_${propriedadeLotacaoClean}_sigla').value = '';
			document
					.getElementById('formulario_${propriedadeLotacaoClean}_descricao').value = '';
			document.getElementById('${propriedadeLotacaoClean}Span').innerHTML = '';

			document.getElementById('spanPessoa${propriedadePessoaClean}').style.display = 'none';
			document.getElementById('formulario_${propriedadePessoaClean}_id').value = '';
			document
					.getElementById('formulario_${propriedadePessoaClean}_sigla').value = '';
			document
					.getElementById('formulario_${propriedadePessoaClean}_descricao').value = '';

			document.getElementById('${propriedadePessoaClean}Span').innerHTML = '';

			document.getElementById('spanEmail${propriedadeEmailClean}').style.display = 'inline';
		}
	}
	select.onchange();
</script>