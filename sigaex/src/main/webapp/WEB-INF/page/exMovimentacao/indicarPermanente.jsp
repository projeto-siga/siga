<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='indicarPermanente';
	ExMovimentacaoForm.submit();
}
</script>

<siga:pagina titulo="Movimentação">

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");$("body").addClass("fisico");</script>
</c:if>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
			<h2>Indicação para Guarda Permanente - ${mob.siglaEDescricaoCompleta}</h2>

			<div class="gt-content-box gt-for-table">

			<form action="${request.contextPath}/app/expediente/mov/indicar_permanente_gravar" enctype="multipart/form-data" method="post">
				<input type="hidden" name="postback" value="1" />
				<input type="hidden" name="sigla" value="${sigla}" />

				<table class="gt-form-table">
					<tr class="header">
						<td colspan="2">Dados da indicação</td>
					</tr>
					<tr>
						<td>Data:</td>
						<td>
							<input type="text" name="dtMovString"/>
						</td>
					</tr>
					
					<tr>
						<td>Responsável:</td>
						<td>
							<siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>&nbsp;
							<input type="checkbox" theme="simple" name="substituicao" onclick="javascript:displayTitular(this);"/>&nbsp;Substituto
						</td>
					</tr>
					<c:set var="style" value="" />
					<c:if test="${!substituicao}">
						<c:set var="style" value="display:none" />
					</c:if>
					<tr id="tr_titular" style="">
						<td>Titular:</td>
							<input type="hidden" name="campos" value="titularSel.id" />
						<td>
							<siga:selecao propriedade="titular" tema="simple" modulo="siga"/>
						</td>
					</tr>
					<tr>
						<td>
							<label>Motivo:</label>
						</td>
						<td>
							<input type="text" name="descrMov" maxlength="80"size="80" />
						</td>
					</tr>
					<tr class="button">
					<td colspan="2">
						<input type="submit" value="Ok" class="gt-btn-small gt-btn-left" /> 
						<input type="button" value="Cancela" onclick="javascript:history.back();" class="gt-btn-small gt-btn-left" />
					</td>
				</tr>
			</table>
		</form>
		
		</div></div></div>
</siga:pagina>
