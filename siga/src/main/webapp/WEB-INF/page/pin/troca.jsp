<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Alteração do PIN">	

	<link rel="stylesheet" href="/siga/css/siga.multiploselect.css" type="text/css" media="screen, projection"/>
	<link rel="stylesheet" href="/siga/css/siga-steps.css" type="text/css" media="screen, projection"/>
 	
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
						<h5 class="titulo-principal-etapa" id="tituloPrincipalEtapa">SIGA PIN</h5>
					</div>
				</div>
			</div>
							
			<div class="card-body lead">
				<form name="formularioCadastro">								
					<div id="trocaPinEtapa" class="etapa  js-etapa">
						<div class="container">						
							<form class="needs-validation" novalidate>							
								<div class="row">
									<div class="col-0 col-lg-3 col-sm-0"></div>
									<div class="col col-lg-6 col-sm-12"> 
										<c:if test="${not empty cadastrante}">
										  <div class="form-group row mb-0">
										    <label for="staticNome" class="col-sm-2 col-form-label">Nome</label>
										    <div class="col-sm-10">
										      <input type="text" readonly class="form-control-plaintext" id="staticNome" value="${cadastrante.nomePessoa}">
										    </div>
										  </div>
										  <div class="form-group row">
										    <label for="staticCpf" class="col-sm-2 col-form-label">CPF</label>
										    <div class="col-sm-10">
										      <input type="text" readonly class="form-control-plaintext" id="staticCpf" value="${cadastrante.cpfFormatado}">
										    </div>
										  </div>
										</c:if>
										
										<label for="pinUserCurrent">Informe seu PIN atual</label>
										<div class="input-group input-group-lg">
										  <div class="input-group-prepend">
										    <span class="input-group-text" id="inputGroup-sizing-lg"><i class="fa fa-key"></i></span>
										  </div>
										  <input type="password" id="pinUserCurrent" class="form-control " style="text-align: center;" aria-describedby="passwordHelp" minlength="8" maxlength="8" size="8" autocomplete="new-password" autofocus inputmode="numeric" required  />
										</div>
										<hr />
										
										<label for="pinUser" id="lblPinUser">Novo PIN</label>
										<div class="input-group input-group-lg">
										  <div class="input-group-prepend">
										    <span class="input-group-text" id="inputGroup-sizing-lg"><i class="fa fa-key"></i></span>
										  </div>
										  <input type="password" id="pinUser" name="pinUser" class="form-control " style="text-align: center;" aria-describedby="pinUserHelp" minlength="8" maxlength="8" size="8" autocomplete="off" inputmode="numeric" required  />
										</div>
										<small id="pinUserHelp" class="form-text text-muted" style="font-size: 70% !important;">
											Seu PIN para Assinatura de Documentos com Senha deve conter apenas números e deve ter 8 dígitos.
										</small>
		
										<label for="pinUserConfirm" id="lblPinUserConfirm" class="mt-2">Confirme seu PIN</label> 									
										<div class="input-group input-group-lg">
										  <div class="input-group-prepend">
										    <span class="input-group-text" id="inputGroup-sizing-lg"><i class="fa fa-key"></i></span>
										  </div>
										  <input type="password" id="pinUserConfirm" name="pinUserConfirm"  class="form-control" style="text-align: center;" aria-describedby="lblPinUserConfirm" minlength="8" maxlength="8" size="8" autocomplete="off" inputmode="numeric" required/>								
										</div>
										<small id="pinUserHelp" class="form-text text-muted" style="font-size: 70% !important;">
											Por favor, insira novamente o PIN para confirmação.
										</small>	
	
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
					            <a id="btnGoToMesa" class="btn btn-primary text-center" href="/siga/app/principal" title="Ir para Mesa Virtual" style="margin-top:50px;">Ir para Mesa Virtual</a>
					            				            					            					           
							</div>						
						</div>
					</div>
										
					<div class="row mt-5">						
					  	<div class="col-sm-12 text-right">					  	  
							<button type="button" class="btn  btn-secondary btn-lg  btn-anterior  js-btn-cancelar">Cancelar</button>	
						  	<button type="button" class="btn  btn-primary btn-lg  btn-proximo  js-btn-proximo">Próximo <i class="fas fa-arrow-right"></i></button>					    					  
					  	</div>
					</div>									
														
				</form>
			</div>			
		</div>
	</div>
				
	<script type="text/javascript" src="/siga/javascript/siga.multiploselect.js"></script>	
	<script type="text/javascript" src="/siga/javascript/siga-pin-troca.js?v=1617652107"></script>	
</siga:pagina>
