<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://fckeditor.net/tags-fckeditor" prefix="FCK"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<siga:pagina titulo="Registro de Assinatura">

	<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='aRegistrarAssinatura';
	ExMovimentacaoForm.submit();
}
</script>

	<table width="100%">
		<tr>
			<td><ww:form action="registrar_assinatura_gravar"
				enctype="multipart/form-data" namespace="/expediente/mov"
				cssClass="form" method="POST">
				<input type="hidden" name="postback" value="1" />
				<ww:hidden name="sigla" value="${sigla}"/>

				<h1>Registro de Assinatura de Documento - ${doc.codigo} <c:if
					test="${numVia != null && numVia != 0}">
			- ${numVia}&ordf; Via
			</c:if></h1>
				<table class="form" width="100%">
					<tr class="header">
						<td colspan="2">Dados da assinatura</td>
					</tr>

					<ww:textfield name="dtMovString" label="Data"
						onblur="javascript:verifica_data(this, true);" />
					<tr>
						<td>Subscritor:</td>
						<td><siga:selecao tema="simple" propriedade="subscritor" />
						&nbsp;&nbsp;<ww:checkbox theme="simple" name="substituicao"
							onclick="javascript:displayTitular(this);" />Substituto</td>
					</tr>
					<c:set var="style" value="" />
					<c:if test="${!substituicao}">
						<c:set var="style" value="display:none" />
					</c:if>
					<tr id="tr_titular" style="${style}">
						<td>Titular:</td>
						<input type="hidden" name="campos" value="titularSel.id" />
						<td><siga:selecao propriedade="titular" tema="simple" /></td>
					</tr>
					<tr>
						<td><span style="font-weight: bold">Atenção:</span></td>
						<td>
						<table width="50%">
							<tr>

								<td><span>Após o registro da assinatura não é mais possível editar ou excluir o documento.</span> <br>
								<span style="font-weight: bold">Deseja registrar a assinatura ?</span></td>
							</tr>
						</table>
						</td>
					</tr>
					<tr class="button">
						<td></td>
						<td><input type="submit" value="Sim" /> <input type="button"
							value="Não" onclick="javascript:history.back();" /></td>
					</tr>
				</table>
			</ww:form></td>
		</tr>
	</table>

</siga:pagina>