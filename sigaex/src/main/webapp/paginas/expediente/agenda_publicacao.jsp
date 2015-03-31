<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

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
	function contaLetras() {
		var i = 256 - tamanho();
		document.getElementById("Qtd").innerText = 'Restam ' + i + ' caracteres';
	}

	function tamanho() {
		nota= new String();
		nota = this.frm.descrPublicacao.value;
		return nota.length;		
	}

	function validar() {
		var data = document.getElementsByName('dtDispon')[0].value;
		var i = tamanho();
		if (data==null || data=="") {			
			alert("Preencha a data para disponibilização.");
			document.getElementById('dt_dispon').focus();		
		}else {
			if (i>256) {
				alert('Descrição com mais de 256 caracteres');
				document.getElementById('descrPublicacao').focus();	
			}else {
				if (i<=0) {
					alert('Descrição deve ser preenchida');
					document.getElementById('descrPublicacao').focus();	
				}else	
					frm.submit();
			}	
		}
	}

	function buscaNomeLota(){
		var siglaLota = $('#idLotPublicacao :selected').text();	
			$.ajax({				     				  
				  url:'/siga/lotacao/selecionar.action?sigla=' + siglaLota ,					    					   					 
				  success: function(data) {
					 var parts = data.split(';');					   
			    	$('#nomeLota').html(parts[3]);				    
			 	 }
			});			
	}		
	
	
</script>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">		
			<h2>Agendamento de Publicação - ${mob.siglaEDescricaoCompleta}</h2>
			<div class="gt-content-box gt-for-table">			
		<form name="frm" action="agendar_publicacao_gravar.action"
namespace="/expediente/mov" cssClass="form" method="GET">
			<ww:token/>
			<input type="hidden" name="postback" value="1" />
			<ww:hidden name="sigla" value="%{sigla}"/>
			<ww:if test="${not empty mensagem}">
				<c:set var="disabled" value="disabled" />				
			</ww:if>
			<ww:else>
				<c:set var="disabled" value="" />
			</ww:else>
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
				<tr>
					<td>Próxima data para disponibilização:</td>
					<td>${proximaDataDisponivelStr}</td>
				</tr>
				<ww:textfield name="dtDispon" id="dt_dispon"
					onblur="javascript:verifica_data(this,true);prever_data();"
					label="Data para disponibilização" />
				<tr>
					<td>Data de publicação:</td>
					<td><div id="dt_publ" /></td>
				</tr>				
				<tr>									
					<td>Lotação de Publicação:</td>
						<ww:if test="${podeAtenderPedidoPubl}">
							<td><siga:selecao tema="simple" propriedade="lotaSubscritor" modulo="siga" /></td>
						</ww:if>
						<ww:else>						
							<td><ww:select name="idLotPublicacao" list="listaLotPubl" listKey="idLotacao"
						                  listValue="siglaLotacao" value="${idLotDefault}" onchange="javascript:buscaNomeLota();" theme="simple"  />
								&nbsp;&nbsp;&nbsp;&nbsp;<span id="nomeLota"></span></td>							
						</ww:else>									
				</tr>	
				<ww:textarea name="descrPublicacao" cols="80" id="descrPublicacao"
							rows="5" cssClass="gt-form-textarea" label="Descrição do documento"
							onkeyup="contaLetras();" />	
				<tr><td></td><td><div id="Qtd">Restam&nbsp;${tamMaxDescr}&nbsp;caracteres</div></td></tr>						
				<tr>
					<td colspan="2"><input type="button" value="Ok" onclick="javascript: validar();" class="gt-btn-medium gt-btn-left" ${disabled}/> <input type="button"
						value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left" />
						<a href="/sigaex/arquivo/download.action?arquivo=${mob.referenciaRTF}" class="gt-btn-large gt-btn-left">Visualizar Publicação</a>
				</tr>
			</table>
			</form>	
			<span style="font-weight:bold; color: red">${mensagem}</span>			
			</div>
			
			
			<br/>
			<span style="margin-left: 0.5cm;color: red;"><b>Atenção:</b></span>
			<ul>
				<li><span style="font-weight:bold">Data para
				Disponibilização</span> - data em que a matéria efetivamente aparece no
				site</li>
				<li><span style="font-weight:bold">Data de Publicação</span> -
				a Data de Disponibilização + 1, conforme prevê art. 4º, parágrafo 3º
				da Lei 11419 / 2006</li>
				<li><span style="font-weight:bold">Visualizar Publicação</span> -
				Permite visualizar o documento antes de ser enviado para o DJE.</li>
			</ul>
</div></div>
<script type="text/javascript">
	buscaNomeLota();
</script>
</siga:pagina>
