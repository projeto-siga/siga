<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="32kb"%>
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

	<div class="container-fluid">
		<div class="card bg-light mb-3" >

			<div class="card-header"><h5>Configurações Cadastradas</h5></div>

			<div class="card-body">
				<input type="hidden" name="postback" value="1" />
				
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label>Tipo de Configuração</label> 
								<siga:select name="idTpConfiguracao"
									list="listaTiposConfiguracao" listKey="idTpConfiguracao"
									id="idTpConfiguracao" headerValue="[Indefinido]" headerKey="0"
									listValue="dscTpConfiguracao" theme="simple" />
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label>Órgão</label> 
								<siga:select
									name="idOrgaoUsu" id="idOrgaoUsu" list="orgaosUsu" listKey="idOrgaoUsu"
									listValue="nmOrgaoUsu" theme="simple" headerValue="[Todos]"
									headerKey="0" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<input type="button" value="Pesquisar" class="btn btn-primary" onclick="javascript:montaTableCadastradas();" />
							<input type="button" value="Incluir Configuração" class="btn btn-primary" onclick="javascript:window.location.href='editar'"/>
						</div>
					</div>
				
					
			</div>
			
		</div>
		<div id="tableCadastradas"></div>	
		
	</div>
	
</siga:pagina>
