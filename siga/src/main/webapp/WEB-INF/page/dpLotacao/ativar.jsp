<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<siga:pagina titulo="Ativar">

	<script type="text/javascript">
		function sbmt() {
			document.getElementById("btnOk").disabled = true;
			sigaSpinner.mostrar();
			frm.submit();
		}
		function tamanho() {
			var i = tamanho2();
			if (i < 0) {
				i = 0
			}
			;
			document.getElementById("Qtd").innerText = 'Restam ' + i + ' caracteres';
		}
	
		function tamanho2() {
			nota = new String();
			nota = this.frm.motivo.value;
			var i = 500 - nota.length;
			return i;
		}
		function corrige() {
			if (tamanho2() < 0) {
				sigaModal.alerta('Descrição com mais de 500 caracteres');
				nota = new String();
				nota = document.getElementById("motivo").value;
				document.getElementById("motivo").value = nota.substring(
						0, 500);
			}
		}
	</script>
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Ativar <fmt:message key="usuario.lotacao"/> - ${lotacao.sigla}</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="${request.contextPath}/app/lotacao/ativar_gravar" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="id" value="${id}" />
					<div class="form-group">
						<div class="form-group">
							<p><strong>Informações de ativação</strong></p>
							<label for="motivo">Motivo</label>
							<textarea placeholder="Preencher o campo com o motivo da ativação"
								class="form-control" name="motivo" id="motivo" cols="60" rows="5" onkeydown="corrige();tamanho();" maxlength="500"onblur="tamanho();" onclick="tamanho();"></textarea>
							<small class="form-text text-muted float-right" id="Qtd">Restam 500 Caracteres</small>
						</div>
					</div>
					<div class="row">
						<div class="col-sm">
							<input type="button" id="btnOk" value="Ok" class="btn btn-primary" onclick="sbmt();"/>
							<input type="button" value="Cancela" onclick="javascript:history.back();" class="btn btn-link ml-2" />
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
