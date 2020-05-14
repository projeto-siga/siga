<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="Movimentação" desabilitarmenu="sim"
	onLoad="try{var num = document.getElementById('id_number');if (num.value == ''){num.focus();num.select();}else{var cap = document.getElementById('id_captcha');cap.focus();cap.select();}}catch(e){};">
	<script src='https://www.google.com/recaptcha/api.js'></script>
	<div class="container-fluid">
		<div class="row">
			<div class="col col-12 col-sm-4">
				<div class="card bg-light mb-3" >
					<div class="card-header">
						<h5>
							Autenticação de Documentos
						</h5>
					</div>
					<div class="card-body">
		
						<form action="${request.contextPath}/public/app/autenticar"	method="get" class="form">
							<div class="row">
								<div class="col">
									<div class="form-group">
										<label>Número de referência</label> 
										<input type="text" id="id_number" name="n" class="form-control" value="${n}"/>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col">
									<div class="form-group">
										<label>Verificação</label> 
										<div class="g-recaptcha" data-sitekey="${recaptchaSiteKey}"></div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col">
									<button type="submit" class="btn btn-lg btn-primary btn-block"><i class="fas fa-stamp"></i> Autenticar</button>
								</div>
							</div>
						</form>
					</div>	
				</div>
			</div>
			<div class="col">
			
				<div class="card mb-3" >
				  <div class="card-header">
				    <h5>Informações Gerais</h5>
				  </div>
				  <div class="card-body">
				    <p>Para utilizar a Confirmação da Autenticidade do Documento é
					obrigatório informar o número do documento que se encontra no rodapé
					do documento a ser consultado.</p>
					<p>
						<u>Preenchimento do campo Número do Documento</u>
					</p>
					<ul>
						<li>O campo deve ser preenchido com todos os números e traços
							('-').</li>
						<li>Exemplo de preenchimento: 123456-1011</li>
					</ul>
				  </div>
				</div>
			</div>
		</div>
		
	</div>
	</div>
	</div>

</siga:pagina>