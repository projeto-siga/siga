<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<siga:pagina titulo="Registro de Assinatura">

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");</script>
</c:if>

	<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='aRegistrarAssinatura';
	ExMovimentacaoForm.submit();
}
</script>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
			<h2>Registro de Assinatura de Documento - ${mob.siglaEDescricaoCompleta}</h2>

			<div class="gt-content-box gt-for-table">


			<ww:form action="registrar_assinatura_gravar"
				enctype="multipart/form-data" namespace="/expediente/mov"
				cssClass="form" method="POST">
				<input type="hidden" name="postback" value="1" />
				<ww:hidden name="sigla" value="${sigla}"/>

				<table class="gt-form-table">
					<tr class="header">
						<td colspan="2">Dados da assinatura</td>
					</tr>

					<ww:textfield name="dtMovString" label="Data"
						onblur="javascript:verifica_data(this, true);" />
					<tr>
						<td>Subscritor:</td>
						<td><siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>
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
						<td><siga:selecao propriedade="titular" tema="simple" modulo="siga"/></td>
					</tr>
					<tr>
						<td><span style="font-weight: bold">Atenção:</span></td>
						<td>
							<span>Após o registro da assinatura não é mais possível editar ou excluir o documento.</span> <br>
							<span style="font-weight: bold">Deseja registrar a assinatura?</span>
						</td>
					</tr>
					<tr>
						<td colspan="2"><input type="submit" value="Sim" class="gt-btn-medium gt-btn-left"/> <input type="button"
							value="Não" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left"/></td>
					</tr>
				</table>
			</ww:form>
	</div></div></div>
</siga:pagina>
