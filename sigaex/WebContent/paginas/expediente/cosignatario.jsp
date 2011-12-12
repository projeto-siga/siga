<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://fckeditor.net/tags-fckeditor" prefix="FCK"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='aJuntar';
	ExMovimentacaoForm.submit();
}

</script>

<siga:pagina titulo="Movimentação">
<table width="100%">
	<tr>
		<td><ww:form action="incluir_cosignatario_gravar"
			namespace="/expediente/mov"
			cssClass="form" method="POST">
			<input type="hidden" name="postback" value="1" />
			<ww:hidden name="sigla" value="%{sigla}"/>

			<h1>Inclusão de Co-signatário - ${doc.codigo} <c:if
				test="${numVia != null && numVia != 0}">
			- ${numVia}&ordf; Via
			</c:if></h1>
			<table class="form" width="100%">
				<tr class="header">
					<td colspan="2">Dados do Co-signatário</td>
				</tr>
				<%--<ww:textfield name="dtMovString" label="Data"
					onblur="javascript:verifica_data(this);" />--%>
					
				<siga:selecao titulo="Co-signatário:" propriedade="cosignatario" />
				<ww:textfield name="funcaoCosignatario" label="Função;Lotação;Localidade" size="50" maxLength="128" />
				
				<tr class="button">
					<td></td>
					<td><input type="submit" value="Ok" /> <input type="button"
						value="Cancela" onclick="javascript:history.back();" />
				</tr>
			</table>
		</ww:form></td>
	</tr>
</table>
</siga:pagina>