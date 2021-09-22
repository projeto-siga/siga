<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Ciência">
	<div class="container-fluid content" id="page">

		<c:if test="${not mob.doc.eletronico}">
			<script type="text/javascript">
				$("html").addClass("fisico");
				$("body").addClass("fisico");
			</script>
		</c:if>

		<script type="text/javascript" language="Javascript1.1">
			function tamanho() {
				var i = tamanho2();
				if (i < 0) {
					i = 0
				}
				;
				document.getElementById("Qtd").innerText = 'Restam ' + i
						+ ' Caracteres';
			}

			function tamanho2() {
				nota = new String();
				nota = this.frm.descrMov.value;
				var i = 255 - nota.length;
				return i;
			}
			function corrige() {
				if (tamanho2() < 0) {
					sigaModal.alerta('Descrição com mais de 255 caracteres');					
					nota = new String();
					nota = document.getElementById("descrMov").value;
					document.getElementById("descrMov").value = nota.substring(
							0, 255);
				}
			}	
			function sbmt() {
				sigaSpinner.mostrar();
		        document.getElementById('btnSubmit').disabled = true;
	            if ($('#descrMov').val() == null || $('#descrMov').val() == '') {
	            	frm.action = 'ciencia_gravar?sigla=${sigla}&postback=1';
	            } else {
	                frm.action = 'ciencia_gravar?sigla=${sigla}&postback=1&descrMov=' + $('#descrMov').val();
	            }
	            frm.submit();
	        }
		</script>

	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>
					Ciência - ${mob.siglaEDescricaoCompleta}
				</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="ciencia_gravar" method="post">
					<input type="hidden" name="postback" value="1" /> 
					<input type="hidden" name="sigla" value="${sigla}" />
					<div class="row">
						<div class="col-sm">
							<div class="form-group">
								<textarea id="descrMov" class="form-control" name="descrMov" cols="60"
									rows="5" onkeydown="corrige();tamanho();" maxlength="255"
									onblur="tamanho();" onclick="tamanho();">${descrMov}</textarea>
								<small class="form-text text-muted" id="Qtd">Restam&nbsp;255&nbsp;Caracteres</small>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm">
							<input id="btnSubmit" type="button" value="Ok" class="btn btn-primary" onclick="sbmt();" />							
							<a href="${linkTo[ExDocumentoController].exibe()}?sigla=${sigla}" class="btn btn-cancel ml-2">Cancela</a>														 												
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
<script>
	$(function(){
		if ('${descrMov}'.length > 0) {
			corrige();
			tamanho();		
		}
	});
</script>	
</siga:pagina>