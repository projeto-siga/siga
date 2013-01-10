<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<%@ taglib prefix="ww" uri="/webwork"%>
<siga:pagina titulo="Lista Configurações">

<script type="text/javascript">

function sbmt(id) {
	
	var frm = document.getElementById('frm');	
	
	if (id == null || IsRunningAjaxRequest()) {
		frm.action='<ww:property value="%{url}"/>';
		frm.submit();
	} else {
		ReplaceInnerHTMLFromAjaxResponse('<ww:property value="%{url}"/>', frm, id);
	}
	return;
}

function montaTableCadastradas(idTpConfiguracao){	
	    document.write("Aqui");
		$('#tableCadastradas').html('Carregando...');			
		$.ajax({				     				  
			  url:'/sigaex/expediente/configuracao/listar_cadastradas.action?idTpConfiguracao=${idTpConfiguracao}',					    					   					 
			  success: function(data) {
		    	$('#tableCadastradas').html(data);				    
		 	 }
		});	
}		

</script>


	<h1>Configurações cadastradas (listagem por tipo):</h1>
	<br />
	<ww:url id="url" action="editar" namespace="/expediente/configuracao">
	</ww:url>
	<input type="button" value="Incluir Configuração"
		onclick="javascript:window.location.href='${url}'">
	<br>	
	<br>
	
	<b>Tipo de Configuração</b>
		<ww:select name="idTpConfiguracao"
				   list="listaTiposConfiguracao" listKey="idTpConfiguracao" id="idTpConfiguracao"
				   listValue="dscTpConfiguracao" onchange="javascript:montaTableCadastradas(idTpConfiguracao);"  theme="simple"
				   headerValue="[Indefinido]" headerKey="0" />
			
	
	
<%--	Tipos de Configuração: <ww:select name="idTpConfiguracao"	   
	   list="descrTiposConf" listKey="idTpConfiguracao" listValue="dscTpConfiguracao" /> 
 --%>
 
    <div class="gt-content clearfix">	  
		<div id="tableCadastradas"></div>		
	 </div>    	
	 
	

</siga:pagina>
