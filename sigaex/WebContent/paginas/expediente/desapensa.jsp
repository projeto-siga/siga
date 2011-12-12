<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://fckeditor.net/tags-fckeditor" prefix="FCK"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='aDesapensar';
	ExMovimentacaoForm.submit();
}
</script>

<siga:pagina titulo="Movimentação">
	<table width="100%">
		<tr>
			<td><ww:form action="desapensar_gravar"
				enctype="multipart/form-data" namespace="/expediente/mov"
				method="post">
				<input type="hidden" name="postback" value="1" />
				<ww:hidden name="sigla" value="%{sigla}" />
				<!-- Pedro : 20070326 -->
				<h1>Desapensamento de Documento -
				${mob.siglaEDescricaoCompleta}</h1>

				<table class="form" width="100%">
					<tr class="header">
						<td colspan="2">Dados do desapensamento</td>
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