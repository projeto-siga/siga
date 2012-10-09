<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Assinatura em Lote">

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
        <h2>Assinatura em Lote</h2>
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
		    <c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC;ASS;EXT')}">		    
		    <c:set var="jspServer" value="${request.scheme}://${request.serverName}:${request.localPort}/${request.contextPath}/expediente/mov/assinar_gravar.action" />
   	 	    <c:set var="nextURL" value="${request.scheme}://${request.serverName}:${request.localPort}/siga/principal.action"  />
    	    <c:set var="urlPath" value="/${request.contextPath}/expediente" />    	   
   		    <c:set var="botao" value=""/>
		    <c:set var="lote" value="true"/>	
		    ${f:obterExtensaoAssinador(lotaTitular.orgaoUsuario,request.scheme,request.serverName,request.localPort,urlPath,jspServer,nextURL,botao,lote)}	
	         </c:if>
	        </td> 				
			</tr>
		</table>	
		</div>			
		<br />
		<c:if test="${(not empty itensSolicitados)}">			
		    <h2>Documentos pendentes de assinatura: Como Subscritor</h2>			
			<div  class="gt-content-box gt-for-table">
		    <table class="gt-table">			    
			    <tr>
			        <th width="3%"></th>
			        <th width="13%" align="left">Número</th>	
			        <th  width="5%"></th>		       	        
			        <th width="15%" colspan="2" align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cadastrante</th>	
			        <th width="15%"></th>	 <th width="49%"></th>			       
			    </tr>
			    <tr>
			        <th width="3%" align="right"><input type="checkbox" name="checkall"
			    					onclick="checkUncheckAll(this)" /></th>	
			     	<th width="13%"></th>													
			        <th width="5%" align="center">Data</th>
			        <th width="10%" align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Lotacao</th>
			        <th width="5%" align="left">Pessoa</th>			        
			        <th width="15%" align="left">Tipo</th>
			        <th width="49%" align="left">Descrição</th>				 
			    </tr>	   		   
			    <c:forEach var="doc" items="${itensSolicitados}">
			        <c:set var="x" scope="request">chk_${doc.idDoc}</c:set>
				    <c:remove var="x_checked" scope="request" />
				    <c:if test="${param[x] == 'true'}">
				       <c:set var="x_checked" scope="request">checked</c:set>
				    </c:if>
			        <tr class="even">
				        <td width="3%"align="center"><input type="checkbox" name="${x}"
				           value="true" ${x_checked} /></td>		       
     			        <td width="13%"align="left">
	    		            <ww:url id="url" action="exibir" namespace="/expediente/doc">
		    		            <ww:param name="sigla">${doc.sigla}</ww:param>
			    		    </ww:url>
				            <ww:a href="%{url}">${doc.codigo}</ww:a>
			            </td>
			            <td width="5%" align="center">${doc.dtDocDDMMYY}</td>
			            <td width="10%" align="center">${doc.lotaCadastrante.siglaLotacao}</td>
			            <td width="5%" align="left">${doc.cadastrante.sigla}</td>			            
			            <td width="15%" align="left">${doc.descrFormaDoc}</td>
			            <td width="49%"align="left">${doc.descrDocumento}</td>			            				    
			        </tr>			         		         
			        <ww:hidden name="pdf${x}" value="${doc.sigla}" />
				    <ww:hidden name="url${x}" value="doc/${doc.codigoCompacto}.pdf"/>
			    </c:forEach>   
			 </table>
	         </div>
	      </c:if>       		    
	  </ww:form>
	</div></div>	
</siga:pagina>
