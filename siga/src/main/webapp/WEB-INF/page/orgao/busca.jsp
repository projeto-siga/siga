<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="propriedadeClean"
	value="${fn:replace(param.propriedade,'.','')}" />

<siga:pagina titulo="Busca de Órgão Externo" popup="true">

<script type="text/javascript" language="Javascript1.1">
function sbmt(offset) {
	if (offset==null) {
		offset=0;
	}
	frm.elements['offset'].value=offset;
	frm.submit();
}
</script>

<form name="frm" action="${request.contextPath}/app/orgao/buscar" cssClass="form" method="POST">
	<input type="hidden" name="propriedade" value="${param.propriedade}" />
	<input type="hidden" name="postback" value="1" />
	<input type="hidden" name="offset" value="0" />

	<table class="form" width="100%">
		<tr class="header">
			<td align="center" valign="top" colspan="4">Dados do Órgão Externo</td>
		</tr>
		<tr>
			<td>
				<label>Nome ou Sigla:</label>
			</td>
			<td>
				<input type="text" name="sigla" value="${sigla}"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div align="right">
					<input type="submit" value="Pesquisar" />
				</div>
			</td>
		</tr>
	</table>
</form>

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
				href="javascript: opener.retorna_${propriedadeClean}('${item.id}','${item.sigla}','${item.descricao}');">${item.sigla}</a></td>
			<td width="90%" align="left">${item.descricao}</td>
		</tr>
	</siga:paginador>
</table>

</siga:pagina>