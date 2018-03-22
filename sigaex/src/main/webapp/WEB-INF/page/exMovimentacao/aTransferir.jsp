<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>

<siga:pagina titulo="Tramitar">

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");$("body").addClass("fisico");</script>
</c:if>


<script type="text/javascript" language="Javascript1.1">
	
function tamanho() {
	var i = tamanho2();
	if (i<0) {i=0};
	$("#Qtd").html('Restam ' + i + ' Caracteres');
}

function tamanho2() {
	nota= new String();
	nota = this.frm.descrMov.value;
	var i = 400 - nota.length;
	return i;
}
function corrige() {
	if (tamanho2()<0) {
		alert('Descrição com mais de 400 caracteres');
		nota = new String();
		nota = document.getElementById("descrMov").value;
		document.getElementById("descrMov").value = nota.substring(0,400);
	}
}

function sbmt() {
	document.getElementById('transferir_gravar_sigla').value= '${mob.sigla}';
	document.getElementById('transferir_gravar_pai').value= '';
	frm.action='transferir?sigla=VALOR_SIGLA&popup=true'
			.replace('VALOR_SIGLA', document.getElementById('transferir_gravar_sigla').value);
	frm.submit();
}

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
	frm.action='${pageContext.request.contextPath}/app/expediente/mov/prever?id=${mov.idMov}';
	frm.submit();
	frm.target = t; 
	frm.action = a;
	
	if (window.focus) {
		newwindow.focus()
	}
	return false;
}	

function submeter() {
	document.getElementById('frm').submit();
}

$(function(){
    $("#formulario_lotaResponsavelSel_sigla").focus();
});

</script>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
			<h2>Tramitar - ${mob.siglaEDescricaoCompleta}</h2>
			
			<div class="gt-content-box gt-for-table">
			
			<form name="frm" id="frm" action="transferir_gravar" method="post">
				<input type="hidden" name="id" value="${id}" />
				<input type="hidden" name="postback" value="1" />
				<input type="hidden" name="docFilho" value="true" />
				<input type="hidden" name="sigla" value="${sigla}" id="transferir_gravar_sigla" />
				<input type="hidden" name="mobilPaiSel.sigla" value="" id="transferir_gravar_pai" />
				<input type="hidden" name="despachando" value="" id="transferir_gravar_despachando" />
				<table class="gt-form-table">
					<c:if test="${not doc.eletronico}">
						<tr>
							<td><span style="color: red"><b>Atenção:</b></span></td>
							<td><span style="color: red"><b>Este documento
							não é digital, portanto, além de transferi-lo pelo sistema,<br />
							é necessário imprimi-lo e enviá-lo por papel.</b></span></td>
						</tr>
					</c:if>
					<tr>
						<td>Destinatário:</td>
						<td>
						<select name="tipoResponsavel" onchange="javascript:sbmt();" >
							<c:forEach items="${listaTipoResp}" var="item">
								<option value="${item.key}" ${item.key == tipoResponsavel ? 'selected' : ''}>
									${item.value}
								</option>  
							</c:forEach>
						</select> 
						<c:choose>
							<c:when test="${tipoResponsavel == 1}">
								<siga:selecao propriedade="lotaResponsavel" tema="simple" modulo="siga"/>
							</c:when>
							<c:when test="${tipoResponsavel == 2}">
								<siga:selecao propriedade="responsavel" tema="simple" modulo="siga"/>
							</c:when>
							<c:when test="${tipoResponsavel == 3}">
								<siga:selecao propriedade="cpOrgao" tema="simple" modulo="siga"/>
							</c:when>
						</c:choose></td>
					</tr>
					<c:if test="${tipoResponsavel == 3}">
					<tr>
						<td></td>
						<td style="color: red; font-size: 11px">Atenção: O trâmite para órgão externo não acarreta o envio digital do documento. Portanto, além de fazer esta operação, será necessário imprimir o documento e remetê-lo fisicamente ou realizar o trâmite por algum outro sistema em uso pelo órgão destinatário.</td>
					</tr>
					</c:if>
					<tr>
					<td>Data da devolução:</td>
						<td>
							<input type="text" name="dtDevolucaoMovString"onblur="javascript:verifica_data(this,0);" 
								value="${dtDevolucaoMovString}"/> 
							(Opcional)
						</td>
					</tr>
					<tr>
						<td colspan=2>
							<input type="checkbox" name="protocolo" value="mostrar" <c:if test="${protocolo}">checked</c:if>/>
							&nbsp;Mostrar protocolo ao concluir o trâmite
						</td>
					</tr>
					<c:if test="${tipoResponsavel == 3}">
						<tr>
							<td>Observação</td>
							<td>
								<input type="text" size="30" name="obsOrgao" value="${obsOrgao}" />
							</td>
						</tr>
					</c:if>
					<tr>
						<td colspan="2">
							<a accesskey="o" id="button_ok" onclick="javascript:submeter();" class="gt-btn-medium gt-btn-left once"><u>O</u>k</a>
							<input type="button" value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left"/>
						</td>
					</tr>
				</table>
			</form>
	</div></div></div>
</siga:pagina>
