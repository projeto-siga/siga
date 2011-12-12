<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=iso-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://fckeditor.net/tags-fckeditor" prefix="FCK"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='aCancelar';
	ExMovimentacaoForm.submit();
}
</script>

<siga:pagina titulo="Movimentação">
	<table width="100%">
		<tr>
			<td><ww:form action="cancelar_movimentacao_gravar"
				enctype="multipart/form-data" namespace="/expediente/mov"
				method="post">
				<input type="hidden" name="postback" value="1" />
				<input type="hidden" name="id" value="${id}" />
				<ww:hidden name="sigla" value="%{sigla}" />
				<!-- Pedro : 20070326 -->
				<h1>Cancelamento de Movimentação - ${doc.codigo} <c:if
					test="${numVia != null && numVia != 0}">-${viaChar}</c:if></h1>

				<table class="form" width="100%">
					<tr class="header">
						<td colspan="2">Dados do cancelamento de movimentacao</td>
					</tr>
					<ww:textfield name="dtMovString" label="Data" />
					<tr>
						<td>Responsável:</td>
						<td><siga:selecao tema="simple" propriedade="subscritor" />
						&nbsp;&nbsp;<ww:checkbox theme="simple" name="substituicao"
							onclick="javascript:displayTitular(this);" />Substituto</td>
					</tr>
					<c:set var="style" value="" />
					<c:if test="${!substituicao}">
						<c:set var="style" value="display:none" />
					</c:if>
					<tr id="tr_titular" style="">
						<td>Titular:</td>
						<input type="hidden" name="campos" value="titularSel.id" />
						<td><siga:selecao propriedade="titular" tema="simple" /></td>
					</tr>

					<ww:textfield name="descrMov" label="Motivo" maxlength="80"
						size="80" />

					<tr class="button">
						<td></td>
						<td><input type="submit" value="Ok" /> <input type="button"
							value="Cancela" onclick="javascript:history.back();" /></td>
					</tr>
				</table>
			</ww:form></td>
		</tr>
	</table>
</siga:pagina>