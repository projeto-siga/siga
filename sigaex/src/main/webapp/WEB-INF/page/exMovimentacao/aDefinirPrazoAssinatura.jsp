<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Atribuir Prazo para Assinatura">
	<div class="container-fluid content" id="page">

		<c:if test="${not mob.doc.eletronico}">
			<script type="text/javascript">
				$("html").addClass("fisico");
				$("body").addClass("fisico");
			</script>
		</c:if>

		<script type="text/javascript" language="Javascript1.1">
			function sbmt() {
				sigaSpinner.mostrar();
		        document.getElementById('btnSubmit').disabled = true;
		        sigladoc = "${sigla}".replace('-', '');
                frm.action = 'definir_prazo_assinatura_gravar?sigla=' + sigladoc.replace('/', '') + '&dtPrazo=' + $('#dtPrazo').val() + '&hrPrazo=' + $('#hrPrazo').val();
	            frm.method = 'POST';
	            frm.submit();
	        }
		</script>

	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>
					Atribuir Prazo para Assinatura - ${mob.siglaEDescricaoCompleta}
				</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="definir_prazo_assinatura_gravar" method="post">
					<input type="hidden" name="postback" value="1" /> 
					<div class="row">
						<div class="col-6 col-md-4">
							<div class="form-group">
								<label for="dtPrazoString">Prazo Final:</label> 
								<input type="text" id="dtPrazoString" name="dtPrazoString" onblur="javascript:verifica_data(this,0);" 
									value="${dtPrazoString}" class="form-control campoData" placeholder="dd/mm/aaaa" autocomplete="off"/>
							</div>
						</div>
						<div class="col-6 col-md-3">
							<div class="form-group">
								<label for="hrPrazoString">Hora Final:</label> 
								<input id="hrPrazoString" name="hrPrazoString" class="form-control"
									onblur="verifica_hora(this,0);" maxlength="5" placeholder="hh:mm" value="${hrPrazoString}" /></input>
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
</siga:pagina>