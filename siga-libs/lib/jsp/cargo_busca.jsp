<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<siga:pagina titulo="Buscar Cargo" popup="true">

<script type="text/javascript" language="Javascript1.1">
function sbmt(offset) {
	if (offset==null) {
		offset=0;
	}
	frm.elements['p.offset'].value=offset;
	frm.submit();
}
</script>

<ww:form name="frm" action="buscar" namespace="/cargo" cssClass="form">
	<input type="hidden" name="propriedade" value="${param.propriedade}" />
	<input type="hidden" name="postback" value="1" />
	<input type="hidden" name="p.offset" value="0" />

	<table class="form" width="100%">
		<tr class="header">
			<td align="center" valign="top" colspan="4">Dados do Cargo</td>
		</tr>
		<ww:textfield label="Nome" name="nome" />
		<ww:select name="orgaoUsu" list="orgaosUsu" listKey="idOrgaoUsu"
			listValue="nmOrgaoUsu" label="Órgão" />
		<ww:submit value="Pesquisar" />
		</ww:form>

		<br>

		<table class="list" width="100%">
			<tr class="header">
				<td align="center">Sigla</td>
				<td align="left">Nome</td>
			</tr>
			<siga:paginador maxItens="10" maxIndices="10" totalItens="${tamanho}"
				itens="${itens}" var="item">
				<tr class="${evenorodd}">
					<td width="10%" align="center"><a
						href="javascript: opener.retorna_${param.propriedade}('${item.id}','${item.sigla}','${item.descricao}');">${item.sigla}</a></td>
					<td width="90%" align="left">${item.descricao}</td>
				</tr>
			</siga:paginador>
		</table>

</siga:pagina>
