<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="SigaLink" desabilitarmenu="sim">
	<script src='https://www.google.com/recaptcha/api.js'></script>
	<div class="container content pt-2">
		<div class="row justify-content-center">
			<div class="col col-sm-12 col-md-5">
					<div class="jumbotron">
						<form action="${request.contextPath}/public/app/sigalink/${tipoLink}/${token}" method="get" class="form" onsubmit="sigaSpinner.mostrar();">
				
							<input type="hidden" id="tipoLink" name="tipoLink" class="form-control" value="${tipoLink}"/>
							<input type="hidden" id="token" name="token" class="form-control" value="${token}"/>

							<div class="row">
								<div class="col">
									<div class="form-group">
										<h3 class="display-4">SigaLink</h3>
										<p class="lead">Por favor, responda a questão abaixo para continuar o processo.</p>
										<div class="g-recaptcha" data-sitekey="${recaptchaSiteKey}"></div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col">
									<button type="submit" class="btn btn-lg btn-primary btn-block" >Clique aqui para prosseguir</button>
								</div>
							</div>
						</form>
					</div>	


			</div>
		
		
		
			
			
	</div>
	</div>
	</div>

</siga:pagina>