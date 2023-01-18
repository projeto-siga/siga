<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script type="text/javascript">
	function validar(orgao) {
		if(document.getElementById("marco").value.trim() == "") {
			sigaModal.alerta("Preencha o Marco Regulatório.");
			return;
		}
		if(document.getElementById("dataAlteracao").value == "") {
			sigaModal.alerta("Preencha a Data de Alteração.");
			return;
		}
		
		sigaModal.enviarHTMLEAbrir('confirmacaoModal', 'Órgão: ' +orgao+ '<br>Quantidade de usuários ativos: 0<br>Quantidade de unidades ativas: 0');
	}
	
	function sbmt() {
		frm.submit();
	}
</script>

<siga:pagina titulo="">
<form name="frm" action="ativarInativarGravar" class="form" method="POST">
	<input type="hidden" name="id" value="${orgaoUsuario.id}" />
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header"><h5>${orgaoUsuario.hisDtFim == null || orgaoUsuario.hisDtFim == "" ? 'Inativar' : 'Ativar'}  ${orgaoUsuario.sigla}</h5></div>
			<div class="card-body">
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
							<label>Dados da ${orgaoUsuario.hisDtFim == null || orgaoUsuario.hisDtFim == "" ? 'Inativação' : 'Ativação'}</label>
						</div>						
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
							<label>Marco Regulatório</label>
							<textarea class="form-control" id="marco" name="marco" maxlength="500" rows="3"></textarea>
						</div>						
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
							<label>Data da Alteração</label>
							<input type="text" id="dataAlteracao" name="dataAlteracao" class="form-control" onblur="javascript:verifica_data(this,0);"/>
						</div>						
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<input type="button" value="Ok" onclick="validar('${orgaoUsuario.descricao}');" class="btn btn-primary"/>
						<input type="button" value="Cancela" class="btn btn-primary" onclick="javascript:history.back();"/>
					</div>
				</div>
				
			</div>
		</div>
	</div>
</form>
<siga:siga-modal id="confirmacaoModal" exibirRodape="false" tituloADireita="Confirma&ccedil;&atilde;o">
	<div class="modal-body">
     		
   	</div>
   	<div class="modal-footer">Deseja Ativar / Inativar Órgão? &nbsp;&nbsp;
    	<button type="button" class="btn btn-danger" data-dismiss="modal">Não</button>		        
    	<a href="javascript:sbmt();" class="btn btn-success btn-confirmacao-inativacao-cadastro" role="button" aria-pressed="true">Sim</a>
	</div>
</siga:siga-modal>
</siga:pagina>
