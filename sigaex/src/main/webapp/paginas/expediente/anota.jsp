<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<siga:pagina titulo="Movimentação">

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");</script>
</c:if>

<script type="text/javascript" language="Javascript1.1">
<ww:url id="url" action="anotar" namespace="/expediente/mov">
	<ww:param name="id">${doc.idDoc}</ww:param>
</ww:url>
function sbmt() {
	<%--ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='aTransferir';
	ExMovimentacaoForm.submit();--%>
	
	frm.action='<ww:property value="%{url}"/>';
	frm.submit();
}
function tamanho() {
	var i = tamanho2();
	if (i<0) {i=0};
	document.getElementById("Qtd").innerText = 'Restam ' + i + ' Caracteres';
}

function tamanho2() {
	nota= new String();
	nota = this.frm.descrMov.value;
	var i = 255 - nota.length;
	return i;
}
function corrige() {
	if (tamanho2()<0) {
		alert('Descrição com mais de 255 caracteres');
		nota = new String();
		nota = document.getElementById("descrMov").value;
		document.getElementById("descrMov").value = nota.substring(0,255);
	}
}

<ww:url id="url" action="prever" namespace="/expediente/mov">
	<ww:param name="id">${mov.idMov}</ww:param>
</ww:url>
var newwindow = '';
function popitup_movimentacao() {
	if (!newwindow.closed && newwindow.location) {
	} else {
		var popW = 600;
		var popH = 400;
		var winleft = (screen.width - popW) / 2;
		var winUp = (screen.height - popH) / 2;
		winProp = 'width='+popW+',height='+popH+',left='+winleft+',top='+winUp+',scrollbars=yes,resizable'
		newwindow=window.open('','${propriedade}',winProp);
		newwindow.name='mov';
	}
	
	newwindow.opener = self;
	t = frm.target; 
	a = frm.action;
	frm.target = newwindow.name;
	frm.action='<ww:property value="%{url}"/>';
	frm.submit();
	frm.target = t; 
	frm.action = a;
	
	if (window.focus) {
		newwindow.focus()
	}
	return false;
}			
</script>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
			<h2>Anotação - ${mob.siglaEDescricaoCompleta}</h2>

			<div class="gt-content-box gt-for-table">

		<ww:form name="frm" action="anotar_gravar"
			namespace="/expediente/mov" theme="simple" method="POST">
			<ww:token/>
			<ww:hidden name="postback" value="1" />
			<ww:hidden name="sigla" value="%{sigla}"/>
			<%-- ww:hidden name="acao" value="aTransferirGravar" />
			<ww:hidden name="page" value="1" /--%>

			<%-- html:form action="/expediente/mov"> 
			<input type="hidden" name="acao" value="aTransferirGravar" />
			<input type="hidden" name="postback" value="1" /> 
			<input type="hidden" name="page" value="1" /> 
			<html:hidden property="idDoc" />
			<html:hidden property="numVia" /> --%>

			<table class="gt-form-table">
				<tr class="header">
					<td colspan="2">Anotação</td>
				</tr>
				<tr>
					<td>Data:</td>
					<td><ww:textfield name="dtMovString"
						onblur="javascript:verifica_data(this,0);" /></td>
				</tr>
				<tr>
					<td>Responsável:</td>
					<td><siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>
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
							tema="simple" modulo="siga"/></td>
				</tr>
				<tr>
					<td>Função do Responsável:</td>
					<td><input type="hidden" name="campos"
						value="nmFuncaoSubscritor" /> <ww:textfield
						name="nmFuncaoSubscritor" size="50" maxLength="128" theme="simple"/> (opcional)</td>
				</tr>
				<tr>
					<td>Nota</td>
					<td><ww:textarea name="descrMov" cols="60" rows="5"
						onkeyup="corrige();tamanho();" onblur="tamanho();"
						onclick="tamanho();" />
					<div id="Qtd">Restam&nbsp;255&nbsp;Caracteres</div>
					</td>
				</tr>

				<!-- 		</table>
		<br />
		<table class="form" width="100%">
 -->


				<%--<c:if test="${tipoResponsavel != 3}">
					<tr>
						<td>Destino final (opcional)</td>
						<td><ww:select name="tipoDestinoFinal" list="listaTipoDestinoFinal"
							onchange="javascript:sbmt();" /> <c:choose>
							<c:when test="${tipoDestinoFinal == 1}">
								<siga:selecao propriedade="lotaDestinoFinal" tema="simple" />
							</c:when>
							<c:when test="${tipoDestinoFinal == 2}">
								<siga:selecao propriedade="destinoFinal" tema="simple" />
							</c:when>
						</c:choose></td>
					</tr>
				</c:if>--%>
				<c:if test="${tipoResponsavel == 3}">
					<tr>
						<td>Observação</td>
						<td><ww:textfield size="30" name="obsOrgao" /></td>
					</tr>
				</c:if>

				<tr class="button">
					<td colspan="2"><input type="submit" value="Ok" class="gt-btn-small gt-btn-left" /> <input type="button"
						value="Cancela" onclick="javascript:history.back();" class="gt-btn-small gt-btn-left" /></td>
				</tr>
			</table>

		</ww:form>
		
	</div></div></div>
</siga:pagina>
