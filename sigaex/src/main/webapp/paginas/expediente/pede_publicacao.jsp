]<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<%--<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>--%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<siga:pagina titulo="MovimentaÃ§Ã£o">

<c:if test="${not doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");</script>
</c:if>

<ww:url id="url" action="prever_data" namespace="/expediente/mov">
</ww:url>
<script type="text/javascript">
	function prever_data() {
		var dtPublDiv = document.getElementById('dt_publ');
		ReplaceInnerHTMLFromAjaxResponse('<ww:property value="%{url}"/>'+'?data='+document.getElementById('dt_dispon').value+'apenasSolicitacao=true', null, dtPublDiv);
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
			alert("Preencha a data para disponibilizaÃ§Ã£o.");
			document.getElementById('dt_dispon').focus();		
		}else {
			if (i>256) {
				alert('DescriÃ§Ã£o com mais de 256 caracteres');
				document.getElementById('descrPublicacao').focus();	
			}else {
				if (i<=0) {
					alert('DescriÃ§Ã£o deve ser preenchida');
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

<%--<c:set var="titulo_pagina" scope="request">MovimentaÃ§Ã£o</c:set>
<c:import context="/siga" url="/paginas/cabecalho.jsp" />--%>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>SolicitaÃ§Ã£o de PublicaÃ§Ã£o - ${doc.codigo}</h2>
			<div class="gt-content-box gt-for-table">			
			<form name="frm" action="pedir_publicacao_gravar.action"
					namespace="/expediente/mov" cssClass="form" method="GET">
			<input type="hidden" name="postback" value="1" />
			<ww:hidden name="sigla" value="%{sigla}"/>	
			<ww:if test="${not empty mensagem}">
				<c:set var="disabled" value="disabled" />				
			</ww:if>		
			<table class="gt-form-table">
				<tr class="header">
					<td colspan="2">Dados da SolicitaÃ§Ã£o</td>
				</tr>
				<tr>					
					<c:choose>
						<c:when test="${cadernoDJEObrigatorio}">
						    <c:set var="disabledTpMat">true</c:set> 
						    <input type="hidden" name="tipoMateria" value="${tipoMateria}" />	
							<td>Tipo de MatÃ©ria:</td>												
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
						</c:when>
						<c:otherwise>
							<td colspan="2">
								<ww:radio list="#{'J':'Judicial', 'A':'Administrativa'}" name="tipoMateria" id="tm" label="Tipo de MatÃ©ria"  value="${tipoMateria}"  disabled="${disabledTpMat}" />
							</td>
						</c:otherwise>	
					</c:choose>
				</tr>
				<tr>
					<td>PrÃ³xima data para disponibilizaÃ§Ã£o:</td>
					<td>${proximaDataDisponivelStr}</td>
				</tr>
				<tr>					
					<td colspan="2"><ww:textfield name="dtDispon" id="dt_dispon"
					onblur="javascript:verifica_data(this,true);prever_data();"
					label="Data para disponibilizaÃ§Ã£o" /> 
					</td>
				</tr>						
				<tr>
					<td>Data de publicaÃ§Ã£o:</td>
					<td><div id="dt_publ"></div></td>
				</tr>				
				<tr>
					<td>LotaÃ§Ã£o de publicaÃ§Ã£o:</td>
					<td><ww:select name="idLotPublicacao" list="listaLotPubl" listKey="idLotacao"
						        listValue="siglaLotacao" value="${idLotDefault}" onchange="javascript: buscaNomeLota();" theme="simple"  />
								&nbsp;&nbsp;&nbsp;&nbsp;<span id="nomeLota"></span></td>		
				</tr>				
					
				<ww:textarea name="descrPublicacao" cols="80" id="descrPublicacao"
							rows="5" cssClass="gt-form-textarea" label="DescriÃ§Ã£o do documento"
							onkeyup="contaLetras();" />
				<tr><td></td><td><div id="Qtd">Restam&nbsp;${tamMaxDescr}&nbsp;caracteres</div></td></tr>
								
				<tr class="button">
					<td colspan="2"><input type="button" onclick="javascript: validar();" value="Ok" class="gt-btn-medium gt-btn-left"  ${disabled}/> <input type="button"
						value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left" />
						<a href="/sigaex/arquivo/download.action?arquivo=${mob.referenciaRTF}" class="gt-btn-large gt-btn-left">Visualizar PublicaÃ§Ã£o</a></td>					
				</tr>
			</table>
		</form>
		<span style="font-weight:bold; color: red">${mensagem}</span>	
	</div>	
	<span style="margin-left: 0.5cm;color: red;"><b>AtenÃ§Ã£o:</b></span>
	<ul>
	<li><span style="font-weight:bold">Data para
	DisponibilizaÃ§Ã£o</span> - data em que a matÃ©ria efetivamente aparece no
	site</li>
	<li><span style="font-weight:bold">Data de PublicaÃ§Ã£o</span> -
	a Data de DisponibilizaÃ§Ã£o + 1, conforme prevÃª art. 4Âº, parÃ¡grafo 3Âº
	da Lei 11419 / 2006</li>
	<li><span style="font-weight:bold">Visualizar PublicaÃ§Ã£o</span> -
	Permite visualizar o documento antes de ser enviado para o DJE.</li>
	</ul>		
	</div>	
</div>
<script type="text/javascript">
	buscaNomeLota();
</script>


<!--  tabela do rodapÃ© -->
<%--<c:import context="/siga" url="/paginas/rodape.jsp" />--%>
</siga:pagina>
