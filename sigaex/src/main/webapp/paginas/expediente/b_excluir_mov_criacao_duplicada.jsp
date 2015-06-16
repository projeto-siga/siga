<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<siga:pagina titulo="Corrigir Data de Fim de Movimentação">

<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='aReferenciar';
	ExMovimentacaoForm.submit();
}

</script>

<table width="100%">
	<tr>
		<td><ww:form action="b_excluir_mov_criacao_duplicada_gravar"
			namespace="/expediente/mov"
			cssClass="form" method="POST">
			<input type="hidden" name="postback" value="1" />
			<ww:token />

			<h1>Excluir Movimentacao De Criação Duplicada
			</h1>
			<table class="form" width="100%">
				<tr class="header">
					<td colspan="2">Dados</td>
				</tr>
				<ww:textfield label="Movimentação" name="id" />
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
