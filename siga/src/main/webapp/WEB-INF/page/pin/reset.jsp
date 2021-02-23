<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Redefinição do PIN">	

	<link rel="stylesheet" href="/siga/css/siga.multiploselect.css" type="text/css" media="screen, projection"/>
	<link rel="stylesheet" href="/siga/css/siga-pin.css" type="text/css" media="screen, projection"/>
 	
 	<div class="container-fluid">
 		<c:if test="${baseTeste}">
			<div class="alert alert-warning" role="alert">
			  ATENÇÃO: Esta é uma versão de testes. Para sua segurança, não utilize a mesmo PIN do Ambiente Oficial.
			</div>
		</c:if>
		<div class="card bg-light mb-3">
				
			<div class="card-header">
				<div class="row">
					<div class="col-sm-9">
						<h5 class="titulo-principal-etapa" id="tituloPrincipalEtapa">Novo PIN</h5>
					</div>
				</div>
			</div>
							
			<div class="card-body lead">
				<form name="formularioCadastro">				
					<div id="apresentacaoPin" class="etapa  js-etapa">
						<div class="row">
							<div class="col-md-12 col-lg-12">
								<div class="py-5 text-center">
								    <img class="d-block mx-auto mb-4" src="/siga/imagens/siga-pin.png" alt="" width="72" height="72">
									<h2 class="mt-5">Esqueceu seu PIN?</h2>
						            <p>Não se preocupe! Um código para definir um novo PIN será enviado para seu e-mail cadastrado:</p
						            
									<c:if test="${not empty cadastrante}">
								 		<p><span class="font-weight-bold"> ${cadastrante.emailPessoaAtualParcialmenteOculto}</span></p>
								 	</c:if>	
								 		
								    <button type="button" id="btnEnviarCodigo" class="btn btn-primary btn-lg mt-2">Enviar Código <i class="fa fa-paper-plane" aria-hidden="true"></i></button>
								
								</div>
							</div>
						</div>
					</div>
					
					<div id="cadastroPinEtapa" class="etapa  js-etapa">
						<div class="container">
							<h4 class="text-center p-4">
								<label for="cadastroPinEtapa">Redefinição de PIN</label>							
							</h4>
							
							<form class="needs-validation" novalidate>							
								<div class="row was-validated">
									<div class="col-0 col-lg-3 col-sm-0"></div>
									<div class="col col-lg-6 col-sm-12"> 
	
										<label for="pinUserCurrent">Informe o código enviado</label>
										<div class="input-group input-group-lg">
				   						  <input type="text" id="tokenPin" class="form-control " style="text-align: center;" aria-describedby="passwordHelp" minlength="8" maxlength="8" size="8" autocomplete="new-password" autofocus inputmode="numeric" required  />
										</div>
										<div class="input-group input-group-lg">
										  <small class="form-text text-muted">Caso não tenha recebido o e-mail com o código, <button type="button" id="btnReenviarCodigo" class="btn btn-link p-0 border-0">clique aqui</button> para reenviar.</small>
										</div>
										<hr />
										<label for="pinUser">Novo PIN</label>
										<div class="input-group input-group-lg">
										  <div class="input-group-prepend">
										    <span class="input-group-text" id="inputGroup-sizing-lg"><i class="fa fa-key"></i></span>
										  </div>
										  <input type="password" id="pinUser" class="form-control " style="text-align: center;" aria-describedby="passwordHelp" minlength="8" maxlength="8" size="8" autocomplete="new-password" autofocus inputmode="numeric" required  />
										  <div class="invalid-feedback">
										  	Seu novo PIN deve conter apenas números e deve ter 8 dígitos.
										  </div>
										</div>
							
		
										<label for="pinUserConfirm">Confirme PIN</label> 									
										<div class="input-group input-group-lg">
										  <div class="input-group-prepend">
										    <span class="input-group-text" id="inputGroup-sizing-lg"><i class="fa fa-key"></i></span>
										  </div>
										  <input type="password" id="pinUserConfirm" class="form-control" style="text-align: center;" aria-describedby="passwordHelp" minlength="8" maxlength="8" size="8" autocomplete="new-password" autofocus inputmode="numeric" required/>
										  <div class="invalid-feedback">
										  	Por favor, insira novamente o PIN para confirmação.
										  </div>								
										
										</div>
	
									</div>
								</div>
							</form>
		

						
						</div>

					</div>
					
					
					
					
					<div id="salvando" class="etapa etapa--final  js-etapa-final">
						<h1 class="text-center display-4">
							Salvando seu PIN...													
						</h1>											
											
						<div class="row">
							<div class="col-sm-12 text-center">
					            <span class="spinner spinner--salvando js-spinner--salvando"></span>
					            <span class="icone-salvo-sucesso"><i class="fas fa-check-circle"></i></span>
					            <a id="btnGoToMesa" class="btn btn-primary text-center" href="/siga/app/principal" title="Cadastrar Nova Configuração" style="margin-top:50px;">Ir para Mesa Virtual</a>
					            				            					            					           
							</div>						
						</div>
					</div>
										
					<div class="row mt-5">						
					  	<div class="col-sm-12 text-right">					  	  
							<button type="button" class="btn  btn-secondary btn-lg  btn-anterior  js-btn-anterior"><i class="fas fa-long-arrow-alt-left"></i> Anterior</button>	
						  	<button type="button" class="btn  btn-primary btn-lg  btn-proximo  js-btn-proximo">Próximo <i class="fas fa-arrow-right"></i></button>					    					  
					  	</div>
					</div>									
														
				</form>
			</div>			
		</div>
	</div>
	
	<siga:siga-modal id="erroModal" exibirRodape="false" tituloADireita="Erro">
		<div class="modal-body">
	   	</div>
	   	<div class="modal-footer">
	   		<button type="button" class="btn btn-danger btn-erro-modal" data-dismiss="modal">Voltar</button>		        
		</div>
	</siga:siga-modal>	
				
	<script type="text/javascript" src="/siga/javascript/siga.multiploselect.js"></script>	
	<script type="text/javascript" src="/siga/javascript/siga-pin-reset.js"></script>	
</siga:pagina>
