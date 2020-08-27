<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="SigaLink" desabilitarmenu="sim">
	<style>
		.switch{font-size:1rem;position:relative}.switch input{position:absolute;height:1px;width:1px;background:0 0;border:0;clip:rect(0 0 0 0);clip-path:inset(50%);overflow:hidden;padding:0}.switch input+label{position:relative;min-width:calc(calc(2.375rem * .8) * 2);border-radius:calc(2.375rem * .8);height:calc(2.375rem * .8);line-height:calc(2.375rem * .8);display:inline-block;cursor:pointer;outline:0;user-select:none;vertical-align:middle;text-indent:calc(calc(calc(2.375rem * .8) * 2) + .5rem)}.switch input+label::after,.switch input+label::before{content:"";position:absolute;top:0;left:0;width:calc(calc(2.375rem * .8) * 2);bottom:0;display:block}.switch input+label::before{right:0;background-color:#dee2e6;border-radius:calc(2.375rem * .8);transition:.2s all}.switch input+label::after{top:2px;left:2px;width:calc(calc(2.375rem * .8) - calc(2px * 2));height:calc(calc(2.375rem * .8) - calc(2px * 2));border-radius:50%;background-color:#fff;transition:.2s all}.switch input:checked+label::before{background-color:#08d}.switch input:checked+label::after{margin-left:calc(2.375rem * .8)}.switch input:focus+label::before{outline:0;box-shadow:0 0 0 .2rem rgba(0,136,221,.25)}.switch input:disabled+label{color:#333;cursor: not-allowed;}.switch input:disabled+label::before{background-color:#333}.switch.switch-sm{font-size:.875rem}.switch.switch-sm input+label{min-width:calc(calc(1.9375rem * .8) * 2);height:calc(1.9375rem * .8);line-height:calc(1.9375rem * .8);text-indent:calc(calc(calc(1.9375rem * .8) * 2) + .5rem)}.switch.switch-sm input+label::before{width:calc(calc(1.9375rem * .8) * 2)}.switch.switch-sm input+label::after{width:calc(calc(1.9375rem * .8) - calc(2px * 2));height:calc(calc(1.9375rem * .8) - calc(2px * 2))}.switch.switch-sm input:checked+label::after{margin-left:calc(1.9375rem * .8)}.switch+.switch{margin-left:1rem}
	</style>
	<script>
		$(document).ready(function() {
			sigaSpinner.mostrar();
		});
		
		function carregarCompleto() {
			$("#completo").attr("disabled", true);
			sigaSpinner.mostrar();
		    $('#framePdf').attr('src', '/siga/public/app/sigalinkStream/${jwt}?completo=1&volumes=1');
		    return false;
		}

		
	</script>

	<div id="pdfContainer" class="container content pt-2 pb-2">
		<div class="row justify-content-center">
			<div class="col col-sm-12 col-md-8">
				<div class="card">
				  <div class="card-header bg-danger text-white">
				  	<div class="row">
				  		<div class="col">
				  			<h5><i class="fa fa-file-pdf" aria-hidden="true"></i> ${sigla}</h5>
				  		</div>
				  		<div class="col text-right">
						  	<div class="form-group m-0">
							  <span class="switch switch-sm">
							    <input type="checkbox" class="switch" id="completo" name="completo" onchange="javascript:carregarCompleto();">
							    <label for="completo">Completo</label>
							  </span>
							</div>
						</div>	

								  	
				  	</div>
				  	

				  </div>
				  <div class="card-body bg-secondary text-white p-0">
					  <c:url var='pdf' value='/public/app/sigalinkStream/${jwt}' />
					  <iframe id="framePdf" src="${pdf}" width="100%" height="500" align="center" style="border: none;" onload='sigaSpinner.ocultar();'  allowfullscreen>
					  	<h5>Carregando PDF...</h5>
					  </iframe>
					  </div>
				  </div>
				</div>
			</div>
		</div>
	</div>
</siga:pagina>