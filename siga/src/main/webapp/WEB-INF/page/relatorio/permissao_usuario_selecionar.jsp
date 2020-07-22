<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<siga:pagina titulo="Relatório de Permissão de Usuários">
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Relatório de Permissão de Usuários</h5>
			</div>
			<div class="card-body">
			<form method="get" action="javascript:submeter()">
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
							<label>Matrícula</label>
							<siga:selecao tipo="pessoa" tema="simple" propriedade="pessoa" modulo="siga"/>
						</div>
					</div>				
				</div>
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<button type="submit" class="btn btn-primary">Gerar...</button>
						</div>
					</div>				
				</div>
			</form>
			</div>
		</div>		
	</div>
</siga:pagina>
<script type="text/javascript">
	function submeter() {
		var t_strIdPessoa = document.getElementsByName("pessoa_pessoaSel.id")[0];
		if (t_strIdPessoa) {
			if (t_strIdPessoa.value) {
				location.href = 'emitir_permissao_usuario?idPessoa=' + t_strIdPessoa.value;
			} else {
				sigaModal.alerta("Por favor, é necessário preencher o campo pessoa!");
			}
		}			
	}	
</script>
