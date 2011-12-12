<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<siga:pagina titulo="Cadastro de configuração">


<ww:url id="url" action="editar" namespace="/configuracao">
</ww:url>

<script type="text/javascript" language="Javascript1.1">

function sbmt() {
	editar_gravar.action='<ww:property value="%{url}"/>';
	editar_gravar.submit();
}

</script>
<body onload="aviso()">
<table width="100%">
	<tr>
		<td>
		<form action="editar_gravar.action">
		<input type="hidden" name="postback" value="1" /> <ww:hidden
			name="id" /> <c:set var="dataFim" value="" />
		<h1>Cadastro de configuração <c:if
			test="${not empty configuracao}">
			para ${configuracao.cpTipoConfiguracao.dscTpConfiguracao}
		</c:if></h1>
		<table class="form" width="100%">
			<tr class="header">
				<td colspan="2">Dados da configuração</td>
			</tr>
			<tr>
				<td><b>Tipo de Configuração</b></td>
				<td><ww:select name="idTpConfiguracao"
					list="listaTiposConfiguracao" listKey="idTpConfiguracao"
					listValue="dscTpConfiguracao" theme="simple"
					headerValue="[Indefinido]" headerKey="0" /></td>
			</tr>
			<tr>
				<td>Nível de acesso</td>
				<td><ww:select name="idNivelAcesso" list="listaNivelAcesso"
					theme="simple" listKey="idNivelAcesso" listValue="nmNivelAcesso"
					headerValue="[Indefinido]" headerKey="0" /></td>
			</tr>
			<tr>
				<td>Pessoa</td>
				<td><siga:selecao propriedade="pessoa" tema="simple" /></td>
			</tr>
			<tr>
				<td>Lotação</td>
				<td><siga:selecao propriedade="lotacao" tema="simple" /></td>
			</tr>
			<tr>
				<td>Função de Confiança</td>
				<td><siga:selecao propriedade="funcao" tema="simple" /></td>
			</tr>
			<tr>
				<td>Órgão</td>
				<td><ww:select name="idOrgaoUsu" list="orgaosUsu"
					listKey="idOrgaoUsu" listValue="nmOrgaoUsu" theme="simple"
					headerValue="[Indefinido]" headerKey="0" /></td>
			</tr>
			<%--<tr>
				<td>Cargo</td>
				<td><siga:selecao propriedade="cargo" tema="simple" /></td>
			</tr>--%>
			<tr>
				<td>Tipo de Movimentação</td>
				<td><ww:select name="idTpMov" list="listaTiposMovimentacao"
					listKey="idTpMov" listValue="descrTipoMovimentacao" theme="simple"
					headerValue="[Indefinido]" headerKey="0" /></td>
			</tr>
			<%--<tr>
				<td>Via</td>
				<td><ww:select name="idVia" list="listaVias" listKey="idVia"
					listValue="destinacao" theme="simple" headerValue="[Indefinido]"
					headerKey="0" /></td>
			</tr> --%>
			<tr>
				<td>Modelo</td>
				<td><ww:select name="idMod" list="listaModelos" listKey="idMod"
					listValue="nmMod" theme="simple" headerValue="[Indefinido]"
					headerKey="0" /></td>
			</tr>
			<tr>
				<td>Classificação</td>
				<td><siga:selecao propriedade="classificacao" tema="simple" /></td>
			</tr>
			<tr>
				<td>Forma de documento</td>
				<td><ww:select name="idFormaDoc" list="listaFormas"
					listKey="idFormaDoc" listValue="descrFormaDoc" theme="simple"
					headerValue="[Indefinido]" headerKey="0" /></td>
			</tr>
			<tr>
				<td>Tipo de Documento</td>
				<td><ww:select name="idTpDoc" list="listaTiposDocumento"
					listKey="idTpDoc" listValue="descrTipoDocumento" theme="simple"
					headerValue="[Indefinido]" headerKey="0" /></td>
			</tr>
			<tr>
				<td><b>Situação</b></td>
				<td><ww:select name="idSituacao" list="listaSituacao"
					listKey="idSitConfiguracao" listValue="dscSitConfiguracao"
					theme="simple" headerValue="[Indefinido]" headerKey="0" /></td>
			</tr>
			<tr>
			</tr>
			<tr class="button">
				<td></td>
				<td><input type="submit" value="Ok" /> <input type="button"
					value="Cancela" onclick="javascript:history.back();" />
			</tr>
		</table>
		</form>
		</td>
	</tr>
</table>
<body>
<br />

</siga:pagina>