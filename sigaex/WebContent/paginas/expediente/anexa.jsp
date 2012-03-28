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

	<table width="100%">
		<tr>
			<td><ww:form action="anexar_gravar" namespace="/expediente/mov"
					method="POST" enctype="multipart/form-data" cssClass="form">

					<input type="hidden" name="postback" value="1" />
					<ww:hidden name="sigla" value="%{sigla}" />

					<h1>
						Anexação de Arquivo - ${doc.codigo}
						<c:if test="${numVia != null && numVia != 0}">
			- ${numVia}&ordf; Via
			</c:if>
					</h1>

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

		<ww:textfield name="descrMov" label="Motivo" maxlength="80" size="80" />

		<ww:file name="arquivo" label="Arquivo" accept="application/pdf"
			onchange="testpdf(this.form)" />

		<!-- ww:datepicker tooltip="Select Your Birthday" label="Birthday"
				name="birthday" / -->
		<ww:submit value="Ok" cssClass="button" align="center" />
		<!--  			<tr class="button"> teste
				<td><ww:submit type="input" value="Cancela" theme="simple"
					onclick="javascript:history.back(-1);" /></td>
			</tr> 
-->
		</ww:form>
		</td>
		</tr>
	</table>

</siga:pagina>