<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='aReverterIndicacaoPermanente';
	ExMovimentacaoForm.submit();
}
</script>

<siga:pagina titulo="MovimentaÃ§Ã£o">

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");</script>
</c:if>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
			<h2>ReversÃ£o de IndicaÃ§Ã£o para Guarda Permanente - ${mob.siglaEDescricaoCompleta}</h2>

			<div class="gt-content-box gt-for-table">

			<ww:form action="reverter_indicacao_permanente_gravar"
				enctype="multipart/form-data" namespace="/expediente/mov"
				method="post">
				<input type="hidden" name="postback" value="1" />
				<ww:hidden name="sigla" value="%{sigla}" />

				<table class="gt-form-table">
					<tr class="header">
						<td colspan="2">Dados da reversÃ£o</td>
					</tr>
					<ww:textfield name="dtMovString" label="Data" />
					<tr>
						<td>ResponsÃ¡vel:</td>
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
					<tr>
						<ww:textfield name="descrMov" label="Motivo" maxlength="80"
									          size="80" />
					</tr>
					<tr class="button">
					<td colspan="2"><input type="submit" value="Ok" class="gt-btn-small gt-btn-left" /> <input type="button"
						value="Cancela" onclick="javascript:history.back();" class="gt-btn-small gt-btn-left" /></td>
				</tr>
			</table>
		</ww:form>
		
		</div></div></div>
</siga:pagina>
