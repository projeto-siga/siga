<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Redefinição de Senha">	

	<link rel="stylesheet" href="/siga/css/siga.multiploselect.css" type="text/css" media="screen, projection"/>
	<link rel="stylesheet" href="/siga/css/siga-pin.css" type="text/css" media="screen, projection"/>
	<script src='https://www.google.com/recaptcha/api.js'></script>
 	
 	<div class="container-fluid">
 		<c:if test="${baseTeste}">
			<div class="alert alert-warning" role="alert">
			  ATENÇÃO: Esta é uma versão de testes. Para sua segurança, não utilize a mesma SENHA do Ambiente Oficial.
			</div>
		</c:if>
		<div class="card bg-light mb-3">
				
			<div class="card-header">
				<div class="row">
					<div class="col-sm-9">
						<h5 class="titulo-principal-etapa" id="tituloPrincipalEtapa">Senha</h5>
					</div>
				</div>
			</div>
							
			<div class="card-body lead">
				<form name="formularioReset">			

					<div id="cpfResetSenha" class="etapa js-etapa">
						<div class="row">
							<div class="col-md-12 col-lg-12">
								<div class="py-1 text-center">
									<h2 class="mt-5">Esqueceu sua Senha?</h2>
						            <p>Não se preocupe! Um código de segurança para definir uma nova SENHA será enviado para seu e-mail cadastrado:</p>
						            
									<div class="row text-left">
										<div class="col-md-4 col-lg-4"></div>
										<div class="col-md-4 col-lg-4">
											<label for="cpfUser">Informe seu CPF</label>
											<div class="input-group input-group-lg">
											  <div class="input-group-prepend">
											    <span class="input-group-text" id="inputGroup-sizing-lg"><i class="fa fa-user"></i></span>
											  </div>
											  <input type="text" id="cpfUser" class="form-control " style="text-align: center;" minlength="14" maxlength="14" size="11" autofocus inputmode="numeric" required value="76633006020" />
											</div>
											<div class="row mt-4">
												<div class="col">
													<div class="form-group"> 
														<div class="g-recaptcha" data-sitekey="${recaptchaSiteKey}"></div>
													</div>
												</div>
											</div>
										</div>
										<div class="col-md-4 col-lg-4"></div>
									</div>
								</div>
							</div>
						</div>
					</div>
					
					<div id="emailResetSenha" class="etapa js-etapa">
						<div class="row">
							<div class="col-md-12 col-lg-12">
								<div class="py-1 text-center">
									<h2 class="mt-5">Enviar código de Recuperação</h2>
						            <p>Um código de segurança para definir uma nova SENHA será enviado para seu e-mail cadastrado:</p>
						            
									<div class="row text-center">
										<div class="col-md-4 col-lg-4"></div>
										<div class="col-md-4 col-lg-4">
											

										 	<div id="emailListContainer" class="font-weight-bold"></div>

										    <button type="button" id="btnEnviarCodigo" class="btn btn-primary btn-lg mt-2">Enviar Código <i class="fa fa-paper-plane" aria-hidden="true"></i></button>
										
										</div>
										<div class="col-md-4 col-lg-4"></div>
									</div>
								</div>
							</div>
						</div>
					</div>
					
					<div id="salvando" class="etapa etapa--final  js-etapa-final">
						<h1 class="text-center display-4">
							Salvando sua Senha...													
						</h1>											
											
						<div class="row">
							<div class="col-sm-12 text-center">
					            <span class="spinner spinner--salvando js-spinner--salvando"></span>
					            <span class="icone-salvo-sucesso"><i class="fas fa-check-circle"></i></span>
					            <a id="btnGoToLogin" class="btn btn-primary text-center" href="/siga/public/app/login" title="Ir para Login" style="margin-top:50px;">Ir para Login</a>
					            				            					            					           
							</div>						
						</div>
					</div>
										
					<div class="row mt-5">						
					  	<div class="col-sm-12 text-right">					  	  
							<button type="button" class="btn  btn-secondary btn-lg  btn-anterior  js-btn-cancelar">Cancelar</button>
						  	<button type="button" class="btn  btn-primary btn-lg  btn-proximo  js-btn-proximo">Próximo <i class="fas fa-arrow-right"></i></button>					    					  
					  	</div>
					</div>									
					<div class="text-center  pt-4">
						<span class="indicador-etapa  js-indicador-etapa"></span>
						<span class="indicador-etapa  js-indicador-etapa"></span>
					</div>									
				</form>
			</div>			
		</div>
	</div>
					
	<script type="text/javascript" src="/siga/javascript/siga.multiploselect.js"></script>	
	<script type="text/javascript" src="/siga/javascript/siga-senha-reset.js?v=1614289085"></script>
	<script type="text/javascript" src="http://andti.com.br/bootstrap-strong-password/js/bootstrap-strong-password.js"></script>	

</siga:pagina>
