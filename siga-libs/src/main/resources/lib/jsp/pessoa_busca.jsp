<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<siga:pagina titulo="Buscar Pessoa" popup="true">

<script type="text/javascript" language="Javascript1.1">
function sbmt(offset) {
	if (offset==null) {
		offset=0;
	}
	frm.elements['p.offset'].value=offset;
	frm.submit();
}
</script>

<ww:form name="frm" action="buscar" namespace="/pessoa" cssClass="form" method="POST">
	<input type="hidden" name="propriedade" value="${param.propriedade}" />
	<input type="hidden" name="postback" value="1" />
	<input type="hidden" name="p.offset" value="0" />
	<input type="hidden" name="buscarFechadas" value="${param['buscarFechadas']}" />
	<table class="form" width="100%">
		<tr class="header">
			<td align="center" valign="top" colspan="4">Dados do Usuário</td>
		</tr>
		<ww:textfield label="Nome ou Matrícula" name="nome" value="${param.sigla}"/>
		<siga:selecao titulo="Lotação" propriedade="lotacao" modulo="siga"/>
		<ww:select name="orgaoUsu" list="orgaosUsu" listKey="idOrgaoUsu"
			listValue="nmOrgaoUsu" label="Órgão" headerKey="0"
			headerValue="[Todos]" />
		<ww:submit value="Pesquisar" />
</ww:form>

<br>

<table class="list" width="100%">
	<tr class="header">
		<td align="center">Matrícula</td>
		<td align="left">Nome</td>
		<td align="center">Lotação</td>
		<td align="center">Função</td>
		<td>Fim de Vigência</td>
	</tr>
	<%--<ww:property value='itemPagina'/> --%>
	<siga:paginador maxItens="10" maxIndices="10" totalItens="${tamanho}"
		itens="${pessoas}" var="pessoa">
		<tr class="${evenorodd}">
			<td align="center"><a
				href="javascript: opener.retorna_${param.propriedade}('${pessoa.id}','${pessoa.sigla}','${pessoa.descricao}','${pessoa.funcaoConfianca}');">${pessoa.sigla}</a></td>
			<td align="left">${pessoa.descricao}</td>
			<td align="center">${pessoa.lotacao.sigla}</td>
			<td align="center">${pessoa.funcaoConfianca.nomeFuncao}${buscarFechadas}</td>
			<td align="left">${pessoa.dataFimPessoa}</td>
		</tr>
	</siga:paginador>
</table>

</siga:pagina>
