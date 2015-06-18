<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<siga:pagina titulo="Arquivamento IntermediÃ¡rio">

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");</script>
</c:if>

<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='aArquivarIntermediario';
	ExMovimentacaoForm.submit();
}

</script>

<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
			<h2>Arquivamento IntermediÃ¡rio - ${mob.siglaEDescricaoCompleta}</h2>

			<div class="gt-content-box gt-for-table">

			<ww:form action="arquivar_intermediario_gravar"
			enctype="multipart/form-data" namespace="/expediente/mov"
			cssClass="form" method="POST">
			<input type="hidden" name="postback" value="1" />
			<ww:hidden name="sigla" value="${sigla}"/>
			<ww:token />
			
			<table class="gt-form-table">
				<tr class="header">
					<td colspan="2">Dados do arquivamento</td>
				</tr>
				
				<!-- Bernardo Inicio -->
				<!-- Checa se o documento Ã© eletronico ou nÃ£o. Caso seja, seu valor default para Data Ã© o atual e o ResponsÃ¡vel Ã© quem fez o Login. -->
				<c:choose>
					<c:when test="${!doc.eletronico}"> <!-- Documento Eletronico -->

						<ww:textfield name="dtMovString" label="Data"
							onblur="javascript:verifica_data(this, true);" />
						<tr>
						<td>ResponsÃ¡vel:</td>
						<td><siga:selecao tema="simple" propriedade="subscritor" modulo="siga" />
						&nbsp;&nbsp;<ww:checkbox theme="simple" name="substituicao"
							onclick="javascript:displayTitular(this);" />Substituto</td>
					</c:when>
				</c:choose>
				<!-- Bernardo Fim -->
				
				<c:choose>
					<c:when test="${!substituicao}">
						<tr id="tr_titular" style="display: none">
					</c:when>
					<c:otherwise>
						<tr id="tr_titular" style="">
					</c:otherwise>
				</c:choose>

				<td>Titular:</td>
					<input type="hidden" name="campos" value="titularSel.id" />
				<td colspan="3"><siga:selecao propriedade="titular"
							tema="simple"  modulo="siga" /></td>
				</tr>
				<tr>
					<ww:textfield name="descrMov" label="LocalizaÃ§Ã£o" maxlength="80"
						size="80" />
				</tr>
				
				<tr class="button">
					<td colspan="2"><input type="submit" value="Ok" class="gt-btn-medium gt-btn-left"/> <input type="button"
						value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left"/>
				</tr>
			</table>
		</ww:form>
	</div></div></div>
</siga:pagina>
