<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Registro de Migração para o SEI">
	<div class="container-fluid content" id="page">



		<script type="text/javascript" language="Javascript1.1">
			function sbmt() {
				document.getElementById("btnOk").disabled = true;
				sigaSpinner.mostrar();
				frm.submit();
			}


			jQuery(function($){
			//  console.log('hi');
			//   $("#date").mask("99/99/9999",{placeholder:"mm/dd/yyyy"});
			//   $("#phone").mask("(999) 999-9999");
			//   $("#tin").mask("99-9999999");
			//   $("#ssn").mask("999-99-9999");
			  $('.sei').mask("9999999-99.9999.04.2.8009");
			});
			
		</script>	
	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5 class="mb-0">
					Registrar Migração para o SEI - ${mob.siglaEDescricaoCompleta}
				</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="migrarSEIGravar" method="post">
					<input type="hidden" name="postback" value="1" /> 
					<input type="hidden" name="sigla" value="${sigla}" />
					<div class="row">
						<div class="col-sm">
							<div class="form-group">
								<label for="descrMov">Número do Processo SEI: </label>
								<input type="text"  class="form-control sei" name="descrMov" required min=28 max=28 placeholder="9999999-99.9999.4.02.8000"  pattern="[0-9]{7}\-[0-9]{2}\.[0-9]{4}\.4\.02\.8000"
									title="Número do processo do SEI inválido.">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm">
							<p class="text-justify" style="color:red;"><b>ATENÇÃO: antes de prosseguir com esta operação verifique: 1) Se não há processo(s) apensado(s) a este. Se for o caso, é necessário realizar a DESAPENSAÇÃO de todos primeiramente. 
							                                              2) Se está anexada neste processo informação com o novo número do processo no SEI em que o seu dossiê integral em .pdf foi incluído para prosseguimento. Após você confirmá-la, este processo SIGA-DOC permanecerá disponível somente para consulta e nenhuma outra operação poderá ser realizada. O cancelamento dessa situação poderá ser realizado somente mediante abertura de chamado pelo gestor da unidade justificadamente. Deseja prosseguir?</b></p>
							
							
						</div>
					</div>
					<div class="row">
						<div class="col-sm">
							<input type="button" id="btnOk" value="Ok" class="btn btn-primary" onclick="sbmt();"/>
							<input type="button" value="Cancela" onclick="javascript:history.back();" class="btn btn-cancel ml-2" />
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>