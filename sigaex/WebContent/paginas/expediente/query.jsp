<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://fckeditor.net/tags-fckeditor" prefix="FCK"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='aJuntar';
	ExMovimentacaoForm.submit();
}

</script>

<siga:pagina titulo="Executar">
<table width="100%">
	<tr>
		<td><ww:form action="executar_gravar"
			namespace="/expediente/configuracao"
			cssClass="form" method="POST">
			<input type="hidden" name="postback" value="1" />

			<ww:textarea name="q" label="Executar" />
			</table>
		</ww:form></td>
	</tr>
</table>
</siga:pagina>