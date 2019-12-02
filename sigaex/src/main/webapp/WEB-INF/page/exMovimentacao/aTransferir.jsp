<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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

	document.getElementById("button_ok").onclick = function(){console.log("Aguarde requisição")};	
	document.getElementById('frm').submit();
}

$(function(){
    $("#formulario_lotaResponsavelSel_sigla").focus();
});

</script>
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Tramitar - ${mob.siglaEDescricaoCompleta}</h5>
			</div>
			<div class="card-body">
			<form name="frm" id="frm" action="transferir_gravar" method="post">
				<input type="hidden" name="id" value="${id}" />
				<input type="hidden" name="postback" value="1" />
				<input type="hidden" name="docFilho" value="true" />
				<input type="hidden" name="sigla" value="${sigla}" id="transferir_gravar_sigla" />
				<input type="hidden" name="mobilPaiSel.sigla" value="" id="transferir_gravar_pai" />
				<input type="hidden" name="despachando" value="" id="transferir_gravar_despachando" />

				<c:if test="${not doc.eletronico}">
				<div class="row">
					<div class="col-sm">
						<div class="form-group">
							<span style="color: red"><b>Atenção:</b></span>
							<span style="color: red"><b>Este documento não é digital, portanto, além de transferi-lo pelo sistema, é necessário imprimi-lo e enviá-lo por papel.</b></span>
						</div>
					</div>
				</div>
				</c:if>
				<c:if test="${tipoResponsavel == 3}">
				<div class="row">
					<div class="col-sm">
						<div class="form-group">
							<span style="color: red"><fmt:message key="tela.tramitar.atencao"/></span>
						</div>
					</div>
				</div>
				</c:if>
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<label>Destinatário</label> 
							<select name="tipoResponsavel" onchange="javascript:sbmt();" class="form-control" >
								<c:forEach items="${listaTipoResp}" var="item">
									<option value="${item.key}" ${item.key == tipoResponsavel ? 'selected' : ''}>
										${item.value}
									</option>  
								</c:forEach>
							</select> 
						</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
							<label>&nbsp;&nbsp;&nbsp;</label> 
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
						</div>
					</div>
				</div>				
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<label>Data da devolução</label> 
							<input type="text" name="dtDevolucaoMovString"onblur="javascript:verifica_data(this,0);" value="${dtDevolucaoMovString}" class="form-control"/>					 
							<small class="form-text text-muted">Atenção: somente preencher a data de devolução se a intenção for, realmente, que o documento seja devolvido até esta data.</small>
						</div>
					</div>
					<c:if test="${tipoResponsavel == 3}">
					<div class="col-sm-3">
						<div class="form-group">
							<div class="form-check form-check-inline mt-4">
							  <input class="form-check-input" type="checkbox" name="protocolo" id="protocolo" value="mostrar" <c:if test="${protocolo}">checked</c:if>/>
							  <label class="form-check-label" for="protocolo"><fmt:message key="tela.tramitar.checkbox"/></label>
							</div>
						</div>
					</div>
					<div class="col-sm-4">
						<div class="form-group">
							<label>Observação</label> 
							<input type="text" size="30" name="obsOrgao" value="${obsOrgao}" class="form-control"/>			 
						</div>
					</div>
					</c:if>
				</div>
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<a accesskey="o" id="button_ok" onclick="javascript:submeter();" class="btn btn-primary"><u>O</u>k</a>
							<button type="button"  onclick="javascript:history.back();" class="btn btn-primary">Cancela</button>
						</div>
					</div>
				</div>				
			</form>
			</div>
		</div>
	</div>
</siga:pagina>
