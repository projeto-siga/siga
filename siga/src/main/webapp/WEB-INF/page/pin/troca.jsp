<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Cadastro de Chave PIN">	

	<link rel="stylesheet" href="/siga/css/siga.multiploselect.css" type="text/css" media="screen, projection"/>
	<link rel="stylesheet" href="/siga/css/siga-pin.css" type="text/css" media="screen, projection"/>
 	
 	<div class="container-fluid">
		<div class="card bg-light mb-3">
				
			<div class="card-header">
				<div class="row">
					<div class="col-sm-9">
						<h5 class="titulo-principal-etapa" id="tituloPrincipalEtapa">Troca de chave PIN</h5>
					</div>
				</div>
			</div>
							
			<div class="card-body lead">
				<form name="formularioCadastro">				
					<div id="apresentacaoPin" class="etapa  js-etapa">
						<div class="row">
													<h4 class="text-center p-4">
								<label for="tipoConfiguracao">Defina uma chave numérica de 8 dígitos</label>							
							</h4>
			
						</div>
					</div>
					
					<div id="cadastroPinEtapa" class="etapa  js-etapa">
						<div class="container">
							<h4 class="text-center p-4">
								<label for="cadastroPinEtapa">Troca de chave PIN</label>							
							</h4>
							
							<form class="needs-validation" novalidate>							
								<div class="row was-validated">
									<div class="col-0 col-lg-3 col-sm-0"></div>
									<div class="col col-lg-6 col-sm-12"> 
										<c:if test="${not empty cadastrante}">
										  <div class="form-group row">
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
										
										<label for="pinUserCurrent">Informe sua chave PIN atual</label>
										<div class="input-group input-group-lg">
										  <div class="input-group-prepend">
										    <span class="input-group-text" id="inputGroup-sizing-lg"><i class="fa fa-key"></i></span>
										  </div>
										  <input type="password" id="pinUserCurrent" class="form-control " style="text-align: center;" aria-describedby="passwordHelp" minlength="8" maxlength="8" size="8" autocomplete="new-password" autofocus inputmode="numeric" required  />
										</div>
										
										<label for="pinUser">Nova chave PIN</label>
										<div class="input-group input-group-lg">
										  <div class="input-group-prepend">
										    <span class="input-group-text" id="inputGroup-sizing-lg"><i class="fa fa-key"></i></span>
										  </div>
										  <input type="password" id="pinUser" class="form-control " style="text-align: center;" aria-describedby="passwordHelp" minlength="8" maxlength="8" size="8" autocomplete="new-password" autofocus inputmode="numeric" required  />
										  <div class="invalid-feedback">
										  	Sua nova chave PIN deve conter apenas números e deve ter 8 dígitos.
										  </div>
										</div>
		
										<label for="pinUserConfirm">Confirme chave PIN</label> 									
										<div class="input-group input-group-lg">
										  <div class="input-group-prepend">
										    <span class="input-group-text" id="inputGroup-sizing-lg"><i class="fa fa-key"></i></span>
										  </div>
										  <input type="password" id="pinUserConfirm" class="form-control" style="text-align: center;" aria-describedby="passwordHelp" minlength="8" maxlength="8" size="8" autocomplete="new-password" autofocus inputmode="numeric" required/>
										  <div class="invalid-feedback">
										  	Por favor, insira novamente a chave PIN para confirmação.
										  </div>								
										
										</div>
	
									</div>
								</div>
							</form>
		

						
						</div>

					</div>
					
					
					
					
					<div id="salvando" class="etapa etapa--final  js-etapa-final">
						<h1 class="text-center display-4">
							Salvando sua chave PIN...													
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
					
					<div class="text-center  pt-4">
						<span class="indicador-etapa  js-indicador-etapa"></span>
						<span class="indicador-etapa  js-indicador-etapa"></span>
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
	<script type="text/javascript" src="/siga/javascript/siga-pin.js"></script>	
</siga:pagina>
