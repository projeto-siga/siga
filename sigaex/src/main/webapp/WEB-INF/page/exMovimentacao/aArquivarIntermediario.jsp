<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Arquivamento Intermediário">

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");$("body").addClass("fisico");</script>
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
		
			<h2>Arquivamento Intermediário - ${mob.siglaEDescricaoCompleta}</h2>

			<div class="gt-content-box gt-for-table">

			<form action="arquivar_intermediario_gravar"
			enctype="multipart/form-data"
			class="form" method="POST">
			<input type="hidden" name="postback" value="1" />
			<input type="hidden" name="sigla" value="${sigla}"/>
			
			<table class="gt-form-table">
				<tr class="header">
					<td colspan="2">Dados do arquivamento</td>
				</tr>
				
				<!-- Bernardo Inicio -->
				<!-- Checa se o documento é eletronico ou não. Caso seja, seu valor default para Data é o atual e o Responsável é quem fez o Login. -->
				<c:choose>
					<c:when test="${!doc.eletronico}"> <!-- Documento Eletronico -->
						<tr>
							<td>Data:</td>
							<td>
								<input type="text" name="dtMovString" onblur="javascript:verifica_data(this, true);" />
							</td>
						</tr>
						<tr>
						<td>Responsável:</td>
						<td><siga:selecao tema="simple" propriedade="subscritor" modulo="siga" />
						&nbsp;&nbsp;<input type="checkbox" name="substituicao"
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
					<td>Localização:</td>
					<td>
						<input type="text" name="descrMov" maxlength="80" size="80" />
					</td>
				</tr>
				
				<tr class="button">
					<td colspan="2"><input type="submit" value="Ok" class="gt-btn-medium gt-btn-left"/> <input type="button"
						value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left"/>
				</tr>
			</table>
		</form>
	</div></div></div>
</siga:pagina>
