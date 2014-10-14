<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Assinatura de Despacho em Lote" onLoad="javascript: TestarAssinaturaDigital()">

	<script type="text/javascript" language="Javascript1.1"
		src="<c:url value="/staticJavascript.action"/>"></script>

	<script type="text/javascript" language="Javascript1.1">
		
	function checkUncheckAll(theElement) {
		var theForm = theElement.form, z = 0;
		for(z=0; z<theForm.length;z++) {
	    	if(theForm[z].type == 'checkbox' && theForm[z].name != 'checkall') {
				theForm[z].checked = !(theElement.checked);
				theForm[z].click();
			}
		}
	}
	
	function displaySel(chk, el) {
		document.getElementById('div_' + el).style.display=chk.checked ? '' : 'none';
		if (chk.checked == -2) 
			document.getElementById(el).focus();
	}
	
	function displayTxt(sel, el) {					
		document.getElementById('div_' + el).style.display=sel.value == -1 ? '' : 'none';
		document.getElementById(el).focus();
	}
	
	
</script>

	<div  class="gt-bd clearfix">
		<div class="gt-content clearfix">
        <h2>Assinatura de Despacho em Lote</h2>
        <ww:form name="frm" id="frm" cssClass="form" theme="simple">
		<ww:token />
		<ww:hidden name="postback" value="1" />
		<div class="gt-content-box gt-for-table">	
		<table class="gt-form-table">
			<tr class="header">
				<td>Assinatura</td>
			</tr>
			<tr class="button">
			<td>
				<div id="dados-assinatura" style="visible: hidden">
				    <c:set var="jspServer" value="${request.scheme}://${request.serverName}:${request.localPort}/${request.contextPath}/expediente/mov/assinar_mov_gravar.action" />
		   	 	    <c:set var="nextURL" value="${request.scheme}://${request.serverName}:${request.localPort}/siga/principal.action"  />
		    	    <c:set var="urlPath" value="${request.contextPath}" />
		
					<ww:hidden id="jspserver" name="jspserver" value="${jspServer}" />
					<ww:hidden id="nexturl" name="nextUrl" value="${nextURL}" />
					<ww:hidden id="urlpath" name="urlpath" value="${urlPath}" />
					<c:set var="urlBase"
						value="${request.scheme}://${request.serverName}:${request.serverPort}" />
					<ww:hidden id="urlbase" name="urlbase" value="${urlBase}" />   		    
		
					<c:set var="botao" value=""/>
				    <c:set var="lote" value="true"/>	
				</div>			
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;VBS:VBScript e CAPICOM')}">
					<c:import url="/paginas/expediente/inc_assina_js.jsp" />
						<div id="capicom-div">
							<a id="bot-assinar" href="#" onclick="javascript: AssinarDocumentos('false', this);" class="gt-btn-alternate-large gt-btn-left">Assinar em Lote</a>
						</div> 
					<p id="ie-missing" style="display: none;">A assinatura digital utilizando padrão do SIGA-DOC só poderá ser realizada no Internet Explorer. No navegador atual, apenas a assinatura com <i>Applet Java</i> é permitida.</p>
					<p id="capicom-missing" style="display: none;">Não foi possível localizar o componente <i>CAPICOM.DLL</i>. Para realizar assinaturas digitais utilizando o método padrão do SIGA-DOC, será necessário instalar este componente. O <i>download</i> pode ser realizado clicando <a href="https://code.google.com/p/projeto-siga/downloads/detail?name=Capicom.zip&can=2&q=#makechanges"><u>aqui</u></a>. Será necessário expandir o <i>ZIP</i> e depois executar o arquivo de instalação.</p>
				<script type="text/javascript">
					 if (window.navigator.userAgent.indexOf("MSIE ") > 0 || window.navigator.userAgent.indexOf(" rv:11.0") > 0) {
						 document.getElementById("capicom-div").style.display = "block";
						 document.getElementById("ie-missing").style.display = "none";
					} else {
						 document.getElementById("capicom-div").style.display = "none";
						 document.getElementById("ie-missing").style.display = "block";
					}
				 </script>
		    
				</c:if>
			
				<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">		    
			   		${f:obterExtensaoAssinador(lotaTitular.orgaoUsuario,request.scheme,request.serverName,request.localPort,urlPath,jspServer,nextURL,botao,lote)}	
	         	</c:if>
	        </td> 				
			</tr>
		</table>	
		</div>			
		<br />
		<c:if test="${(not empty itens)}">			
		    <h2>Despachos pendentes de assinatura: Como Subscritor</h2>			
			<div  class="gt-content-box gt-for-table">
		    <table class="gt-table">			    
			    <tr>
			        <th width="3%"></th>
			        <th width="17%" align="left">Número</th>	
			        <th  width="5%"></th>		       	        
			        <th width="15%" colspan="2" align="left">Cadastrante</th>	
			        <th width="15%"></th>	 <th width="49%"></th>			       
			    </tr>
			    <tr>
			        <th width="3%" align="right"><input type="checkbox" name="checkall"
			    					onclick="checkUncheckAll(this)" /></th>	
			     	<th width="13%"></th>													
			        <th width="7%" align="left">Data</th>
			        <th width="8%" align="left">Lotação</th>
			        <th width="7%" align="left">Pessoa</th>			        
			        <th width="18%" align="left">Tipo</th>
			        <th width="40%" align="left">Descrição</th>				 
			    </tr>	   		   
			    <c:forEach var="mov" items="${itens}">
			       <%--  <c:set var="x" scope="request">chk_${mov.exMobil.id}</c:set> --%>
			        <c:set var="x" scope="request">chk_${mov.idMov}</c:set>
				    <c:remove var="x_checked" scope="request" />
				    <c:if test="${param[x] == 'true'}">
				       <c:set var="x_checked" scope="request">checked</c:set>
				    </c:if>
			        <tr class="even">
				        <td width="3%"align="center"><input type="checkbox" name="${x}"
				           value="true" ${x_checked} /></td>		       
     			        <td width="17%"align="left">
	    		            <ww:url id="url" action="exibir" namespace="/expediente/doc">
		    		            <ww:param name="sigla">${mov.exMobil.sigla}</ww:param>
			    		    </ww:url>
				            <ww:a href="%{url}">${mov.exMobil.codigo}</ww:a>
			            </td>
			            <td width="7%" align="left">${mov.dtRegMovDDMMYY}</td>
			            <td width="8%" align="left">${mov.lotaCadastrante.sigla}</td>
			            <td width="7%" align="left">${mov.cadastrante.sigla}</td>			            
			            <td width="18%" align="left">${mov.exTipoMovimentacao.sigla}</td>
			            <td width="40%"align="left">${mov.obs}</td> 	            				    
			        </tr>			         		         
					<ww:hidden name="pdf${x}" value="${mov.referencia}" />
					<ww:hidden name="url${x}" value="/arquivo/exibir.action?arquivo=${mov.nmPdf}" />
				</c:forEach>   
			 </table>
	         </div>
	      </c:if>       		    
	  </ww:form>
	</div></div>	
</siga:pagina>
