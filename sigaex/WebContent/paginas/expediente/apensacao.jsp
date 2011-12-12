<!DOCTYPE1 HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
	ExMovimentacaoForm.acao.value='aApensar';
	ExMovimentacaoForm.submit();
}
</script>

<siga:pagina titulo="Apensar Documento">

	<table width="100%">
		<tr>
			<td><ww:form action="apensar_gravar"
				enctype="multipart/form-data" namespace="/expediente/mov"
				cssClass="form" method="POST">
				<input type="hidden" name="postback" value="1" />
				<ww:hidden name="sigla" value="%{sigla}" />

				<h1>Apensação de Documento - ${mob.siglaEDescricaoCompleta}</h1>

				<table class="form" width="100%">
					<tr class="header">
						<td colspan="2">Dados da apensação</td>
					</tr>

					<!-- Checa se o documento é eletronico ou não. Caso seja, seu valor default para Data é o atual e o Responsável é quem fez o Login. -->
					<c:choose>
						<c:when test="${!doc.eletronico}">
							<!-- Documento Eletronico -->

							<ww:textfield name="dtMovString" label="Data"
								onblur="javascript:verifica_data(this, true);" />
							<tr>
								<td>Responsável:</td>
								<td><siga:selecao tema="simple" propriedade="subscritor" />
								&nbsp;&nbsp;<ww:checkbox theme="simple" name="substituicao"
									onclick="javascript:displayTitular(this);" />Substituto</td>
						</c:when>
					</c:choose>

					</tr>
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
						tema="simple" /></td>
					</tr>
					<tr>
						<td>Documento Mestre:</td>
						<td><siga:selecao tema='simple' titulo="Documento Mestre:"
							propriedade="documentoRef" /></td>
					</tr>
					<tr class="button">
						<td></td>
						<td><input type='submit' value="Ok" /> <input type="button"
							value="Cancela" onclick="javascript:history.back();" />
					</tr>
				</table>
			</ww:form></td>
		</tr>
	</table>

</siga:pagina>
