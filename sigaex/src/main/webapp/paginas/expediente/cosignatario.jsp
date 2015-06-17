<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<script type="text/javascript" language="Javascript1.1">
	function sbmt() {
		ExMovimentacaoForm.page.value = '';
		ExMovimentacaoForm.acao.value = 'aJuntar';
		ExMovimentacaoForm.submit();
	}
</script>

<siga:pagina titulo="MovimentaÃ§Ã£o">

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");</script>
</c:if>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">

			<h2>
				InclusÃ£o de CossignatÃ¡rio- ${mob.siglaEDescricaoCompleta}</h2>

			<div class="gt-content-box gt-for-table">

				<ww:form action="incluir_cosignatario_gravar"
					namespace="/expediente/mov" cssClass="form" method="POST">
					<input type="hidden" name="postback" value="1" />
					<ww:hidden name="sigla" value="%{sigla}" />
					<table class="gt-form-table">
						<tr class="header">
							<td colspan="2">Dados do CossignatÃ¡rio</td>
						</tr>
						<%--<ww:textfield name="dtMovString" label="Data"
					onblur="javascript:verifica_data(this);" />--%>

						<siga:selecao titulo="CossignatÃ¡rio:" propriedade="cosignatario" modulo="siga"/>
						<ww:textfield name="funcaoCosignatario"
							label="FunÃ§Ã£o;LotaÃ§Ã£o;Localidade" size="50" maxLength="128" />

						<tr class="button">
							<td colspan="2"><input type="submit" value="Ok"
								class="gt-btn-small gt-btn-left" /> <input type="button"
								value="Cancela" onclick="javascript:history.back();"
								class="gt-btn-small gt-btn-left" />
						</tr>
					</table>
				</ww:form>
			</div>
		</div>
	</div>
</siga:pagina>
