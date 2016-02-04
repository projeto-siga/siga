<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Movimentação">

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");$("body").addClass("fisico");</script>
</c:if>

<script type="text/javascript" language="Javascript1.1">
function sbmt() {
	frm.action='${pageContext.request.contextPath}/app/expediente/mov/vincularPapel';
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

function alteraResponsavel()
{
	var objSelecionado = document.getElementById('tipoResponsavel');
	
	switch (parseInt(objSelecionado.value))
	{
		case 1:
			document.getElementById('selecaoResponsavel').style.display = '';
			document.getElementById('selecaoLotaResponsavel').style.display = 'none';
			break;
		case 2:
			document.getElementById('selecaoResponsavel').style.display = 'none';
			document.getElementById('selecaoLotaResponsavel').style.display = '';
			break;
	}
}

</script>
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>
				Definição de Perfil - ${mob.siglaEDescricaoCompleta}
			</h2>
			<div class="gt-content-box gt-for-table">
				<form name="frm" action="vincularPapel_gravar" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="sigla" value="${sigla}"/>
					<table class="gt-form-table">
						<tr class="header">
							<td colspan="2">
								Vinculação
							</td>
						</tr>
						<tr>
							<td>
								Data:
							</td>
							<td>
								<input type="text" name="dtMovString" value="${dtMovString}" onblur="javascript:verifica_data(this,0);" />
							</td>
						</tr>
						<tr>
						    <td>
						    	Responsável:
						    </td>
							<td>
			
								<select id="tipoResponsavel"  name="tipoResponsavel" onchange="javascript:alteraResponsavel();">
									<c:forEach items="${listaTipoRespPerfil}" var="item">
										<option value="${item.key}" ${item.key == tipoResponsavel ? 'selected' : ''}>
											${item.value}
										</option>  
									</c:forEach>
								</select>		
								<span id="selecaoResponsavel">
									<siga:selecao propriedade="responsavel" tema="simple" modulo="siga"/>
								</span>
								<span id="selecaoLotaResponsavel">
									<siga:selecao propriedade="lotaResponsavel" tema="simple" modulo="siga"/>
								</span>		
								<script>alteraResponsavel();</script>			  
							</td>
					    </tr>
						<tr>
							<td>
								Perfil
							</td>
							<td>
								<select  name="idPapel">
									<c:forEach items="${listaExPapel}" var="item">
										<option value="${item.idPapel}" ${item.idPapel == idPapel ? 'selected' : ''}>
											${item.descPapel}
										</option>  
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr class="button">
							<td colspan="2">
								<input type="submit" value="Ok" class="gt-btn-medium gt-btn-left"/>
								<input type="button" value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left"/>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
