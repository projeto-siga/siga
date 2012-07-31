<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://fckeditor.net/tags-fckeditor" prefix="FCK"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<siga:pagina titulo="Movimentação">

	<script type="text/javascript" language="Javascript1.1">
		function sbmt() {
			ExMovimentacaoForm.page.value = '';
			ExMovimentacaoForm.acao.value = 'aAnexar';
			ExMovimentacaoForm.submit();
		}

		function testpdf(x) {
			padrao = /\.pdf/;
			a = x.arquivo.value;
			OK = padrao.exec(a);
			if (a != '' && !OK) {
				window.alert("Somente é permitido anexar arquivo PDF!");
				x.arquivo.value = '';
				x.arquivo.focus();
			}
		}
	</script>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
			<h2>Anexação de Arquivo - ${mob.siglaEDescricaoCompleta}</h2>

			<div class="gt-content-box gt-for-table">
			
		<ww:form action="anexar_gravar" namespace="/expediente/mov"
					method="POST" enctype="multipart/form-data" cssClass="form">
			<input type="hidden" name="postback" value="1" />
			<ww:hidden name="sigla" value="%{sigla}"/>
			
			<table class="gt-form-table">


					<tr class="header">
						<td colspan="2">Dados do Arquivo</td>
					</tr>
					<ww:textfield name="dtMovString" label="Data"
						onblur="javascript:verifica_data(this, true);" />

					<tr>
						<td>Responsável:</td>
						<td><siga:selecao tema="simple" propriedade="subscritor" />
							&nbsp;&nbsp;<ww:checkbox theme="simple" name="substituicao"
								onclick="javascript:displayTitular(this);" />Substituto</td>
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
							tema="simple" />
					</td>
		</tr>

		<ww:textfield name="descrMov" label="Descrição" maxlength="80" size="80" />

		<ww:file name="arquivo" label="Arquivo" accept="application/pdf"
			onchange="testpdf(this.form)" />

				<tr class="button">
					<td colspan="2"><input type="submit" value="Ok" class="gt-btn-small gt-btn-left" /> <input type="button"
						value="Cancela" onclick="javascript:history.back();" class="gt-btn-small gt-btn-left" /></td>
				</tr>
			</table>
		</ww:form>
		
		</div></div></div>
</siga:pagina>