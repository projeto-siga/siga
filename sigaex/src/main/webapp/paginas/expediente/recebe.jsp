<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<siga:pagina titulo="Recebimento">

<c:if test="${not doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");</script>
</c:if>

<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='aReceber';
	ExMovimentacaoForm.submit();
}
</script>

<table width="100%">
	<tr>
		<td><ww:form action="receber_gravar"
			enctype="multipart/form-data" namespace="/expediente/mov">
			<input type="hidden" name="postback" value="1" />
			<ww:hidden name="sigla" value="${sigla}"/>

			<h1>Recebimento de Documento - ${doc.codigo} <c:if
				test="${numVia != null && numVia != 0}">
			- ${numVia}&ordf; Via
			</c:if></h1>
			<table class="form" width="100%">
				<tr class="header">
					<td colspan="2">Dados do recebimento</td>
				</tr>
				<c:forEach var="mov" items="${doc.exMovimentacaoSet}">
					<c:if test="${(empty mov.dtFimMov) && (mov.numVia==param.via)}">
						<c:set var="ultMov" value="${mov}" />
					</c:if>
				</c:forEach>
				<ww:select name="idResp" label="Novo ResponsÃ¡vel"
					list="listaRespRecebimento" />
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
