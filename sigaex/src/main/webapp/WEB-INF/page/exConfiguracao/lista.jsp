<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Lista Configurações">

<script type="text/javascript">
	function sbmt(id, action) {
		var frm = document.getElementById(id);
		frm=action;
		frm.submit();
		return;
	}

	function montaTableCadastradas(){	
   		var idTpConf = $('#idTpConfiguracao').val();
    	var idOrgao = $('#idOrgaoUsu').val();
		$('#tableCadastradas').html('Carregando...');			
		$.ajax({				     				  
			url:'/sigaex/app/expediente/configuracao/listar_cadastradas',
			type: "GET",
			data: {idTpConfiguracao : idTpConf, idOrgaoUsu : idOrgao},					    					   					 
			success: function(data) {
	    		$('#tableCadastradas').html(data);				    
	 		}
		});	
	}		
</script>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">

			<h2>Configurações Cadastradas</h2>

			<div class="gt-content-box gt-for-table">
				<input type="hidden" name="postback" value="1" />
				<table class="gt-form-table">
					<tr class="header">
						<td colspan="2">Dados da Pesquisa</td>
					</tr>
					<tr>
						<td><b>Tipo de Configuração</b> 
								<siga:select name="idTpConfiguracao"
									list="listaTiposConfiguracao" listKey="idTpConfiguracao"
									id="idTpConfiguracao" headerValue="[Indefinido]" headerKey="0"
									listValue="dscTpConfiguracao" theme="simple" /> <b>Órgão</b> 
								<siga:select
									name="idOrgaoUsu" list="orgaosUsu" listKey="idOrgaoUsu"
									listValue="nmOrgaoUsu" theme="simple" headerValue="[Todos]"
									headerKey="0" /></td>
					</tr>
					<tr>
						<td><input type="button" value="Pesquisar"
							class="gt-btn-medium gt-btn-left"
							onclick="javascript:montaTableCadastradas();" /> <input
							type="button" value="Incluir Configuração"
							class="gt-btn-large gt-btn-left"
							onclick="javascript:window.location.href='editar'"></td>
					</tr>
				</table>
			</div>
			<div class="gt-content clearfix">
				<div id="tableCadastradas"></div>
			</div>
		</div>
	</div>
</siga:pagina>
