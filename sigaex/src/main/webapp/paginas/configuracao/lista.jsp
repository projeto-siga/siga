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

function montaTableCadastradas(){	
	    
//	    alert($('#idTpConfiguracao').val());
//	    alert($('#idOrgaoUsu').val());
	    var idTpConf = $('#idTpConfiguracao').val();
	    var idOrgao = $('#idOrgaoUsu').val();
		$('#tableCadastradas').html('Carregando...');			
		$.ajax({				     				  
			  url:'/sigaex/expediente/configuracao/listar_cadastradas.action?idTpConfiguracao='+idTpConf+'&idOrgaoUsu='+idOrgao,					    					   					 
			  success: function(data) {
		    	$('#tableCadastradas').html(data);				    
		 	 }
		});	
}		

</script>

<div class="gt-bd clearfix">
	<div class="gt-content clearfix">		

	<h2>Configurações Cadastradas</h2>	
	
	<ww:url id="urlIncluir" action="editar" namespace="/expediente/configuracao">
	</ww:url>
	<div class="gt-content-box gt-for-table">	
		<input type="hidden" name="postback" value="1" />
		<table class="gt-form-table">
			<tr class="header">
				<td colspan="2">Dados da Pesquisa</td>
			</tr>
			<tr>
				<td><b>Tipo de Configuração</b>
				<ww:select name="idTpConfiguracao"
				   list="listaTiposConfiguracao" listKey="idTpConfiguracao" id="idTpConfiguracao"
				   headerValue="[Indefinido]" headerKey="0" listValue="dscTpConfiguracao"  theme="simple" />
				  
			
	
				<b>Órgão</b>
				<ww:select name="idOrgaoUsu" list="orgaosUsu"
					listKey="idOrgaoUsu" listValue="nmOrgaoUsu" theme="simple"
					headerValue="[Todos]" headerKey="0" />
				</td>	
			</tr>
			<tr>
				<td><input type="button" value="Pesquisar" class="gt-btn-medium gt-btn-left" onclick="javascript:montaTableCadastradas();" />
					
					<input type="button" value="Incluir Configuração" class="gt-btn-large gt-btn-left"
		                   onclick="javascript:window.location.href='${urlIncluir}'">		
							
				</td>
			</tr>	
		</table>
	</div> 
    <div class="gt-content clearfix">	  
		<div id="tableCadastradas"></div>		
	 </div>    	
	 
</div></div>
	

</siga:pagina>
