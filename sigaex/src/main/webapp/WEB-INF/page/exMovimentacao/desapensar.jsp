<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='aDesapensar';
	ExMovimentacaoForm.submit();
}
</script>

<siga:pagina titulo="Movimentação">

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");$("fisico").addClass("fisico");</script>
</c:if>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
			<h2>Desapensamento de Documento - ${mob.siglaEDescricaoCompleta}</h2>

			<div class="gt-content-box gt-for-table">

				<form action="desapensar_gravar" enctype="multipart/form-data" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="sigla" value="${sigla}" />
	
					<table class="gt-form-table">
						<tr class="header">
							<td colspan="2">Dados do desapensamento</td>
						</tr>
						<tr>
							<td>
								<label>Data:</label>
							</td>
							<td>
								<input type="text" name="dtMovString"/>
							</td>
						</tr>					
						<tr>
							<td>Responsável:</td>
							<td><siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>&nbsp;
							<input type="checkbox" theme="simple" name="substituicao"
								onclick="javascript:displayTitular(this);" />&nbsp;Substituto</td>
						</tr>
						<c:set var="style" value="" />
						<c:if test="${!substituicao}">
							<c:set var="style" value="display:none" />
						</c:if>
						<tr id="tr_titular" style="">
							<td>Titular:</td>
							<input type="hidden" name="campos" value="titularSel.id" />
							<td><siga:selecao propriedade="titular" tema="simple" modulo="siga"/></td>
						</tr>
						<tr class="button">
							<td colspan="2"><input type="submit" value="Ok" class="gt-btn-small gt-btn-left" /> <input type="button"
								value="Cancela" onclick="javascript:history.back();" class="gt-btn-small gt-btn-left" /></td>
						</tr>
					</table>
				</form>
		
			</div>
		</div>
	</div>
</siga:pagina>
