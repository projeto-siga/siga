<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=iso-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

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

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");</script>
</c:if>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
			<h2>Cancelamento de Movimentação - ${mob.siglaEDescricaoCompleta}</h2>

			<div class="gt-content-box gt-for-table">
			
		<ww:form action="cancelar_movimentacao_gravar"
				enctype="multipart/form-data" namespace="/expediente/mov"
				method="post">
			<input type="hidden" name="postback" value="1" />
				<input type="hidden" name="id" value="${id}" />
			<ww:hidden name="sigla" value="%{sigla}"/>
			
			<table class="gt-form-table">
					<tr class="header">
						<td colspan="2">Dados do cancelamento de movimentacao</td>
					</tr>
					<ww:textfield name="dtMovString" label="Data (Opcional)" />
					<tr>
						<td>Responsável (Opcional):</td>
						<td><siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>
						&nbsp;&nbsp;<ww:checkbox theme="simple" name="substituicao"
							onclick="javascript:displayTitular(this);" />Substituto</td>
					</tr>
					<tr id="tr_titular" style="display:none">
						<td>Titular:</td>
						<input type="hidden" name="campos" value="titularSel.id" />
						<td><siga:selecao propriedade="titular" tema="simple" modulo="siga"/></td>
					</tr>

					<ww:textfield name="descrMov" label="Motivo (Opcional)" maxlength="80"
						size="80" />

				<tr class="button">
					<td colspan="2"><input type="submit" value="Ok" class="gt-btn-small gt-btn-left" /> <input type="button"
						value="Cancela" onclick="javascript:history.back();" class="gt-btn-small gt-btn-left" /></td>
				</tr>
			</table>
		</ww:form>
		
		</div></div></div>
</siga:pagina>
