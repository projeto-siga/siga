<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>




<siga:pagina titulo="TransferÃªncia">

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");</script>
</c:if>


	<script type="text/javascript" language="Javascript1.1">
<ww:url id="url" action="transferir" namespace="/expediente/mov">
</ww:url>
<ww:url id="urlEditar" action="editar" namespace="/expediente/doc"> 
</ww:url>

function tamanho() {
	var i = tamanho2();
	if (i<0) {i=0};
	document.getElementById("Qtd").innerText = 'Restam ' + i + ' Caracteres';
}

function tamanho2() {
	nota= new String();
	nota = this.frm.descrMov.value;
	var i = 400 - nota.length;
	return i;
}
function corrige() {
	if (tamanho2()<0) {
		alert('DescriÃ§Ã£o com mais de 400 caracteres');
		nota = new String();
		nota = document.getElementById("descrMov").value;
		document.getElementById("descrMov").value = nota.substring(0,400);
	}
}


function sbmt() {
	<%--ExMovimentacaoForm.page.value='';
	ExMovimentacaoForm.acao.value='aTransferir';
	ExMovimentacaoForm.submit();--%>

	if(document.getElementById('transferir_gravar_idTpDespacho').value == -2)
		{
			document.getElementById('transferir_gravar_sigla').value= '';
			document.getElementById('transferir_gravar_pai').value= '${mob.sigla}';
			document.getElementById('transferir_gravar_despachando').value= 'true';
			frm.postback.value=0;
			frm.action='<ww:property value="%{urlEditar}"/>';
  		}	
  		else {
			document.getElementById('transferir_gravar_sigla').value= '${mob.sigla}';
			document.getElementById('transferir_gravar_pai').value= '';
			document.getElementById('transferir_gravar_despachando').value= 'false';
			frm.action='<ww:property value="%{url}"/>';
		}
		
	frm.submit();
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
		
			<h2>Despacho / Transferencia - ${mob.siglaEDescricaoCompleta}</h2>
			
			<div class="gt-content-box gt-for-table">
			
			<ww:form name="frm" action="transferir_gravar"
				namespace="/expediente/mov" theme="simple" method="GET">
				<ww:hidden name="postback" value="1" />
				<ww:hidden name="docFilho" value="true" />
				<ww:hidden name="sigla" value="${sigla}"
					id="transferir_gravar_sigla" />
				<ww:hidden name="mobilPaiSel.sigla" value=""
					id="transferir_gravar_pai" />
				<ww:hidden name="despachando" value="" id="transferir_gravar_despachando" />
				<%-- ww:hidden name="acao" value="aTransferirGravar" />
=======
<table width="100%">
	<tr>
		<td><ww:form name="frm" action="transferir_gravar"
			namespace="/expediente/mov" theme="simple" method="POST">
			<ww:token />
			<ww:hidden name="postback" value="1" />
			<ww:hidden name="idDoc" />
			<ww:hidden name="numVia" />
			<%-- ww:hidden name="acao" value="aTransferirGravar" />
>>>>>>> 1.7
			<ww:hidden name="page" value="1" /--%>

				<%-- html:form action="/expediente/mov"> 
			<input type="hidden" name="acao" value="aTransferirGravar" />
			<input type="hidden" name="postback" value="1" /> 
			<input type="hidden" name="page" value="1" /> 
			<html:hidden property="idDoc" />
			<html:hidden property="numVia" /> --%>

				<table class="gt-form-table">
					<tr class="header">
						<td colspan="2">Despacho</td>
					</tr>
					<tr>
						<td>Data:</td>
						<td><ww:textfield name="dtMovString"
							onblur="javascript:verifica_data(this,0);" /></td>
					</tr>
					<tr>
						<td>Subscritor:</td>
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
						<td>FunÃ§Ã£o;LotaÃ§Ã£o;Localidade:</td>
						<td colspan="3"><input type="hidden" name="campos"
							value="nmFuncaoSubscritor" /> <ww:textfield
							name="nmFuncaoSubscritor" size="50" maxLength="128" />
						(Opcionalmente informe a funÃ§Ã£o e a lotaÃ§Ã£o na forma:
						FunÃ§Ã£o;LotaÃ§Ã£o;Localidade)</td>
					</tr>

					<tr>
						<td>Despacho</td>
						<td><ww:select name="idTpDespacho" list="tiposDespacho"
							listKey="idTpDespacho" listValue="descTpDespacho"
							onchange="javascript:sbmt();" /></td>
					</tr>
					<!--Orlando: Substitui o textfield pelo textarea no choose, abaixo, para atender aos usuÃ¡rios, que disseram terem dificuldade
					ao escrever, porque nÃ£o viam o inÃ­cio da digitaÃ§Ã£o na caixa de texto. TambÃ©m incluÃ­ funÃ§Ãµes e um div para limitar o nÃºmero de caracteres,
					como ocorre na antoÃ§Ã£o (anotaÃ§Ã£o JSP)-->
					<c:choose>
						<c:when test="${idTpDespacho == -1}">
							<tr>
								<td>Texto</td>
                            <!--<td><ww:textfield name="descrMov"  maxlength="400" size="80"   /></td>-->
                                <td><ww:textarea  rows="3" cols="50"  name="descrMov"  onkeyup="corrige();tamanho();" onblur="tamanho();"
						onclick="tamanho();"/> <div id="Qtd">Restam&nbsp;400&nbsp;Caracteres</div></td>
							</tr>
						</c:when>
					</c:choose>

					<%-- 		</table>
=======
			<table class="form" width="100%">
				<tr class="header">
					<td colspan="2">Despacho</td>
				</tr>
				<tr>
					<td>Data:</td>
					<td><ww:textfield name="dtMovString"
						onblur="javascript:verifica_data(this,0);" /></td>
				</tr>
				<tr>
					<td>Subscritor:</td>
					<td><siga:selecao tema="simple" propriedade="subscritor" />
					&nbsp;&nbsp;<ww:checkbox theme="simple" name="substituicao"
						onclick="javascript:displayTitular(this);" />Substituto</td>
				</tr>
				<c:set var="style" value="" />
				<c:if test="${!substituicao}">
					<c:set var="style" value="display:none" />
				</c:if>
				<tr id="tr_titular" style="${style}">
					<td>Titular:</td>
					<input type="hidden" name="campos" value="titularSel.id" />
					<td><siga:selecao propriedade="titular" tema="simple" /></td>
				</tr>
				<tr>
					<td>FunÃ§Ã£o;LotaÃ§Ã£o;Localidade:</td>
					<td colspan="3"><input type="hidden" name="campos"
						value="nmFuncaoSubscritor" /> <ww:textfield
						name="nmFuncaoSubscritor" size="50" maxLength="128" /> (Opcionalmente informe a funÃ§Ã£o e a lotaÃ§Ã£o na forma: FunÃ§Ã£o;LotaÃ§Ã£o;Localidade)</td>
				</tr>
				<tr>
					<td>Despacho</td>
					<td>
						 <ww:select name="idTpDespacho" list="tiposDespacho"
						listKey="idTpDespacho" listValue="descTpDespacho"
						onchange="javascript:sbmt();" /></td>
				</tr>
				<c:choose>
					<c:when test="${idTpDespacho == -1}">
						<tr>
							<td>Texto</td>
							<td><ww:textfield name="descrMov" maxlength="255" size="80" /></td>
						</tr>
					</c:when>
					<c:when test="${idTpDespacho == -2}">
						<tr>
							<td valign="top">Texto</td>
							<td>--%>
					<!-- Pedro : 20070313  -->
					<%--<FCK:editor id="Conteudo" basePath="/fckeditor/"
							     height="300" toolbarSet="DefaultSemSave">	
							       ${Conteudo}
							 </FCK:editor>								 
							</td>
						</tr>
					</c:when>
				</c:choose>--%>
					<%-- 		</table>
>>>>>>> 1.7
		<br />
		<table class="form" width="100%">
 --%>
					<tr class="header">
						<td colspan="2">TransferÃªncia</td>
					</tr>

					<c:if test="${not doc.eletronico}">
						<tr>
							<td><span style="color: red"><b>AtenÃ§Ã£o:</b></span></td>
							<td><span style="color: red"><b>Este documento
							nÃ£o Ã© digital, portanto, alÃ©m de transferi-lo pelo sistema,<br />
							Ã© necessÃ¡rio imprimi-lo e enviÃ¡-lo por papel.</b></span></td>
						</tr>
					</c:if>
					<tr>
						<td>Atendente:</td>

						<td><ww:select name="tipoResponsavel" list="listaTipoResp"
							onchange="javascript:sbmt();" /> <c:choose>
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
					<tr>
					<td>Data da devoluÃ§Ã£o:</td>
						<td><ww:textfield name="dtDevolucaoMovString"
							onblur="javascript:verifica_data(this,0);" /> (Opcional)</td>
					</tr>
					<tr>
						<td colspan=2><input type="checkbox" name="protocolo"
							value="mostrar" />&nbsp;Mostrar protocolo ao concluir a transferÃªncia</td>
					</tr>

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
							<td>ObservaÃ§Ã£o</td>
							<td><ww:textfield size="30" name="obsOrgao" /></td>
						</tr>
					</c:if>
					<tr>
						<td colspan="2"><input type="submit" value="Ok" class="gt-btn-medium gt-btn-left once" /> <input type="button"
							value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left"/>
							<input type="button" name="ver_doc"	value="Visualizar o despacho" class="gt-btn-large gt-btn-left" onclick="javascript: popitup_movimentacao();"/></td>
					</tr>
				</table>
			</ww:form>
	</div></div></div>
</siga:pagina>
