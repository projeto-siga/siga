<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags/mensagem" prefix="siga-mensagem"%>

<script type="text/javascript" language="Javascript1.1">
	function validarEnviar() {
		var usuarioPubNet = document.getElementById('usuarioPubNet').value;	
		if (usuarioPubNet==null || usuarioPubNet=="") {									
			sigaModal.alerta('Preencha o campo Usuário PubNet');
			document.getElementById('usuarioPubNet').focus();
			return
		}
		
		if(usuarioPubNet.length > 20) {
			sigaModal.alerta('Tamanho máximo do Usuário Pubnet é de 20 caracteres');
			document.getElementById('usuarioPubNet').focus();
			return
		}
		sigaModal.alterarLinkBotaoDeAcao('confirmacaoModal', 'javascript:enviar()');
		sigaModal.enviarHTMLEAbrir('confirmacaoModal', 'Confirma a inclusão do usuário PubNet?');	
	}
	
	function removerEspaco() {
		document.getElementById('usuarioPubNet').value = document.getElementById('usuarioPubNet').value.replaceAll(" ", "");	
	}
	
	function enviar() {
		frm.submit();
	}
</script>
<siga:pagina popup="false" titulo="Associar Login de Publicação no DOE">
	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Associar Login de Publicação no DOE</h5>
			</div>

			<div class="card-body">
				<form name="frm" action="/siga/app/usuario/associar_login_gravar" method="post">
					<input type="hidden" name="page" value="1" />
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label>Usuário PubNet</label>
								<input type="text" name="usuarioPubNet" id="usuarioPubNet" value="${usuarioPubNet}" class="form-control" maxlength="20" onkeyup="javascript:removerEspaco();"/>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm">
							<button type="button" class="btn btn-primary" onclick="javascript:validarEnviar();" >Gravar</button>
							<button type="button" onclick="location.href='/siga/app/principal'" class="btn btn-cancel btn-light" >Cancela</button>
						</div>
					</div>
				</form>
				<br />				
			</div>
		</div>		
	</div>
	<siga:siga-modal id="confirmacaoModal" exibirRodape="true" tituloADireita="Confirmação" descricaoBotaoFechaModalDoRodape="Cancelar" linkBotaoDeAcao="#">
		<div class="modal-body">	
		</div>
	</siga:siga-modal>	
</siga:pagina>

