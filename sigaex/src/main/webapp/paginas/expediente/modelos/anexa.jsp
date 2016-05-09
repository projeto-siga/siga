<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='aAnexar';
	ExMovimentacaoForm.submit();
}
</script>

<c:set var="titulo_pagina" scope="request">Movimentação</c:set>
<c:import url="/cabecalho.jf" />
<table width="100%">
	<tr>
		<td><html:form action="/expediente/mov" enctype="multipart/form-data">
			<input type="hidden" name="acao" value="aAnexarGravar" />
			<input type="hidden" name="postback" value="1" />
			<input type="hidden" name="page" value="1" />
			<html:hidden property="idDoc" />
			<html:hidden property="numVia" />

			<h1>Anexação de Arquivo
			<c:if test="${numVia != null}">
			- ${ExMovimentacaoForm.numVia}&ordf; Via
			</c:if>
			</h1>
			<table class="form" width="100%">
				<tr class="header">
					<td colspan="2">Dados do Arquivo</td>
				</tr>
				<tr>
					<td>Data:</td>
					<td><html:text property="dtMovString" /></td>
				</tr>
				<tr>
					<td>Subscritor:</td>
					<td><siga:selecao propriedade="subscritor" modulo="siga"/></td>
				</tr>
				<tr>
					<td>Data:</td>
					<td><html:file property="arquivo" /></td>
				</tr>
				<tr class="button">
					<td></td>
					<td><input type="submit" value="Ok" /> <input type="submit"
						value="Cancela" onclick="javascript:history.back();" />
				</tr>
			</table>
		</html:form></td>
	</tr>
</table>

<!--  tabela do rodapé -->
<c:import url="/rodape.jf" />
