<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<siga:pagina titulo="Inativar">

	<script type="text/javascript">
		function sbmt() {
			document.getElementById("btnOk").disabled = true;
			if (validaMotivo()) {
				sigaModal.abrir('confirmacaoModal');		
			}
		}
		
        function confirmar() {
            sigaSpinner.mostrar();
            sigaModal.fechar('confirmacaoModal');
            document.frm.submit();
        }
        
        function cancelar() {
        	document.getElementById("btnOk").disabled = false;
            sigaModal.fechar('confirmacaoModal');
            window.location.href = '/siga/app/lotacao/listar?idOrgaoUsu='+ ${lotacao.orgaoUsuario.idOrgaoUsu};
        }

		function validaMotivo() {
			var motivo = document.getElementById("motivo").value;
			if (motivo.length > 500) {
				document.getElementById("btnOk").disabled = false;
				sigaModal.alerta('Motivo com mais de 500 caracteres');
				
				return false;
			}
			return true;
		}
	</script>
	
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Inativar <fmt:message key="usuario.lotacao"/> - ${lotacao.sigla}</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="/siga/app/lotacao/inativar_gravar" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="id" value="${lotacao.id}" />
					<div class="form-group row">
						<div class="col-12 col-md-6">
							<p><strong>Informações de Inativação</strong></p>
							<label for="motivo">Motivo</label>
							<textarea placeholder="Preencher o campo com o motivo da Inativação" class="form-control" name="motivo" id="motivo" cols="60" rows="2"></textarea>
						</div>
					</div>
					<div class="form-group row">
						<div class="col-12 col-md-6">
							<input type="button" id="btnOk" value="Ok" class="btn btn-primary" onclick="sbmt();"/>
							<input type="button" value="Cancela" onclick="cancelar();" class="btn btn-link ml-2" />
						</div>
					</div>

					
					<siga:siga-modal id="confirmacaoModal" exibirRodape="false" tituloADireita="Confirmação">
						<div id="msg" class="modal-body">
				       		Deseja inativar cadastro selecionado?
				     	</div>
				     	<div class="modal-footer">
				       		<button type="button" class="btn btn-danger" data-dismiss="modal" onclick="cancelar();">Não</button>		        
				       		<a href="#" class="btn btn-success btn-confirmacao" role="button" aria-pressed="true" onclick="confirmar();">Sim</a>
						</div>
					</siga:siga-modal>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
