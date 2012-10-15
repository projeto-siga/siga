<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://fckeditor.net/tags-fckeditor" prefix="FCK"%>
<%--<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>--%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<siga:pagina titulo="Movimentação">

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");</script>
</c:if>

<ww:url id="url" action="prever_data" namespace="/expediente/mov">
</ww:url>
<script type="text/javascript">
	function prever_data() {
		var dtPublDiv = document.getElementById('dt_publ');
		ReplaceInnerHTMLFromAjaxResponse('<ww:property value="%{url}"/>'+'?data='+document.getElementById('dt_dispon').value, null, dtPublDiv);
	}
	
	function sbmt() {
		ExMovimentacaoForm.page.value='';
		ExMovimentacaoForm.acao.value='aAgendarPublicacao';
		ExMovimentacaoForm.submit();
}
</script>

<!-- A linha abaixo é temporária, pois está presente num dos cabeçalhos  -->
<div id="carregando" style="position:absolute;top:0px;right:0px;background-color:red;font-weight:bold;padding:4px;color:white;display:none">Carregando...</div>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
			<h2>Agendamento de Publicação - ${mob.siglaEDescricaoCompleta}</h2>

			<div class="gt-content-box gt-for-table">

		<form action="agendar_publicacao_gravar.action"
			namespace="/expediente/mov" cssClass="form" method="GET">
			<ww:token/>
			<input type="hidden" name="postback" value="1" />
			<ww:hidden name="sigla" value="%{sigla}"/>

			<table class="gt-form-table">
				<colgroup>
				<col  style="width:30%"/>
				<col  style="width:70%"/>
				</colgroup>
				<tr class="header">
					<td colspan="2">Dados do Agendamento</td>
				</tr>
				<c:choose>
					<c:when test="${cadernoDJEObrigatorio}">
						<c:set var="disabledTpMat">true</c:set> 
						<input type="hidden" name="tipoMateria" value="${tipoMateria}" />
						<tr>
							<td>Tipo de Matéria:</td>
							<td>
								<c:choose>
									<c:when test="${tipoMateria eq 'A'}">
										Administrativa 
									</c:when>
									<c:otherwise>
										Judicial
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<ww:radio list="#{'J':'Judicial', 'A':'Administrativa'}" name="tipoMateria" id="tm" label="Tipo de Matéria"  value="${tipoMateria}" disabled="${disabledTpMat}" />
					</c:otherwise>
				</c:choose>
				<ww:textfield name="dtDispon" id="dt_dispon"
					onblur="javascript:verifica_data(this,true);prever_data();"
					label="Data para disponibilização" />
				<tr>
					<td>Data de publicação:</td>
					<td><div id="dt_publ" /></td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="Ok" class="gt-btn-medium gt-btn-left" /> <input type="button"
						value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left" />
				</tr>
			</table>
			</form>
			</div>
			
			<br/>
			<h3>Atenção:</h3>
			<ul>
				<li><span style="font-weight:bold">Data para
				Disponibilização</span> - data em que a matéria efetivamente aparece no
				site</li>
				<li><span style="font-weight:bold">Data de Publicação</span> -
				a Data de Disponibilização + 1, conforme prevê art. 4º, parágrafo 3º
				da Lei 11419 / 2006</li>
			</ul>
</div></div>
</siga:pagina>