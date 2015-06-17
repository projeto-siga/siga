<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<siga:pagina titulo="Corrigir Data de Fim de MovimentaÃ§Ã£o">

<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='aReferenciar';
	ExMovimentacaoForm.submit();
}

</script>

<table width="100%">
	<tr>
		<td><ww:form action="b_corrigir_data_fim_mov_gravar"
			namespace="/expediente/mov"
			cssClass="form" method="POST">
			<input type="hidden" name="postback" value="1" />
			<ww:hidden name="idDoc" />
			<ww:hidden name="numVia" />
			<ww:token />

			<h1>Corrigir documento - ${doc.codigo}<c:if 
			test="${numVia != null && numVia != 0}">-${viaChar}</c:if>
			</h1>
			<table class="form" width="100%">
				<tr class="header">
					<td colspan="2">Dados</td>
				</tr>

				<siga:selecao titulo="Documento:" propriedade="documentoRef" modulo="sigaex" />
				<tr class="button">
					<td></td>
					<td><input type="submit" value="Ok" /> <input type="button"
						value="Cancela" onclick="javascript:history.back();" />
				</tr>
			</table>
		</ww:form></td>
	</tr>
</table>

</siga:pagina>
