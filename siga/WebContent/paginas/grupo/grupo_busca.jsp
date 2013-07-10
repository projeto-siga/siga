<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<siga:pagina titulo="Busca de Grupo" popup="true">

<script type="text/javascript" language="Javascript1.1">
function sbmt(offset) {
	if (offset==null) {
		offset=0;
	}
	frm.elements['p.offset'].value=offset;
	frm.submit();
}
</script>

<ww:form name="frm" action="buscar" cssClass="form" method="POST">
	<table class="form" width="100%">
		<input type="hidden" name="buscarFechadas" value="${param['buscarFechadas']}" />
		<input type="hidden" name="propriedade" value="${param.propriedade}" />
		<input type="hidden" name="postback" value="1" />
		<input type="hidden" name="p.offset" value="0" />
		<tr class="header">
			<td align="center" valign="top" colspan="4">Dados do Grupo</td>
		</tr>
		<ww:textfield label="Nome ou Sigla" name="nome" />
		<%--<ww:hidden name="postback" value="true" />--%>
		<%--<ww:select name="orgaoUsu" list="orgaosUsu" listKey="idOrgaoUsu"
			listValue="nmOrgaoUsu" label="Órgão" headerKey="0"
			headerValue="[Todos]" />--%>
		<ww:submit value="Pesquisar" />
	</table>
</ww:form>

<br>

<table class="list" width="100%">
	<tr class="header">
		<td align="center">Sigla</td>
		<td align="left">Nome</td>
		<td>Fim de Vigência</td>
	</tr>
	<siga:paginador maxItens="10" maxIndices="10" totalItens="${tamanho}"
		itens="${itens}" var="item">
		<tr class="${evenorodd}">
			<td width="10%" align="center"><a
				href="javascript: opener.retorna_${param.propriedade}('${item.id}','${item.sigla}','${item.descricao}');">${item.sigla}</a></td>
			<td width="70%" align="left">${item.descricao}</td>
			<td align="left" width="20%">${item.hisDtFim}</td>
		</tr>
	</siga:paginador>
</table>

</siga:pagina>