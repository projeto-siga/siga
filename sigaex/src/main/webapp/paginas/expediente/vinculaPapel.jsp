<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<siga:pagina titulo="MovimentaÃ§Ã£o">

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");</script>
</c:if>

<script type="text/javascript" language="Javascript1.1">
<ww:url id="url" action="vincularPapel" namespace="/expediente/mov" />
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
		alert('DescriÃ§Ã£o com mais de 255 caracteres');
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
			<h2>DefiniÃ§Ã£o de Perfil - ${mob.siglaEDescricaoCompleta}</h2>
			<div class="gt-content-box gt-for-table">
			
			<ww:form name="frm" action="vincularPapel_gravar"
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
					<td colspan="2">VinculaÃ§Ã£o</td>
				</tr>
				<tr>
					<td>Data:</td>
					<td><ww:textfield name="dtMovString"
						onblur="javascript:verifica_data(this,0);" /></td>
				</tr>
				
				
				<tr>
				    <td>ResponsÃ¡vel:</td>

					<td><ww:select name="tipoResponsavel" list="listaTipoRespPerfil"
						onchange="javascript:sbmt();" /> 
     					<c:choose>							  
							  <c:when test="${tipoResponsavel == 1}">
								<siga:selecao propriedade="responsavel" tema="simple" modulo="siga"/>
							  </c:when>
							  <c:when test="${tipoResponsavel == 2}">
								<siga:selecao propriedade="lotaResponsavel" tema="simple" modulo="siga"/>
							  </c:when>							  
						</c:choose>
					</td>
			    </tr>
				

				<tr>
					<td>Perfil</td>
					<td>
						<ww:select name="idPapel" list="listaExPapel" listValue="descPapel" listKey="idPapel"/>
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
				</c:if>
				<c:if test="${tipoResponsavel == 3}">
					<tr>
						<td>ObservaÃ§Ã£o</td>
						<td><ww:textfield size="30" name="obsOrgao" /></td>
					</tr>
				</c:if>--%>

				<tr class="button">
					<td colspan="2"><input type="submit" value="Ok" class="gt-btn-medium gt-btn-left"/> <input type="button"
						value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left"/> <%--input
						type="button" name="ver_doc"
						value="Visualizar o modelo preenchido"
						onclick="javascript: popitup_movimentacao();" --%></td>
				</tr>
			</table>

		</ww:form>
	</div></div></div>
</siga:pagina>
