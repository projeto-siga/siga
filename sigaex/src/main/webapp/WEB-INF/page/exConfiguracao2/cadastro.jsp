<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Cadastro de configuração">	
<link rel="stylesheet" href="/siga/css/selectpicker/bootstrap-select.min.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="/siga/css/siga.multiploselect.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css" media="screen, projection" />

<style>
	.etapa {
		display: none;
	}
	
	.indicador-etapa {
	  height: 15px;
	  width: 15px;
	  margin: 0 2px;
	  background-color: #007bff;
	  border: none;
	  border-radius: 50%;
	  display: inline-block;
	  opacity: 0.5;
	}
	
	.indicador-etapa.active {
  		opacity: 1;	
	}
	
	.indicador-etapa.finish {
  		background-color: #4CAF50;
	}
	
	.btn-proximo[disabled] {
		cursor: not-allowed;
	}
		
	div.dropdown-menu.show {
		min-width: 100%!important;
		max-width: 100%!important;
	}
	
	ul.dropdown-menu.inner.show {
		margin-bottom: 0!important;
	}
	
	.btn.dropdown-toggle {
		background-color: #FFF;
	}
	
	.select2-container--bootstrap {
		width: 100%!important; 
	}
	
</style>

 	
 	<div class="container-fluid">
		<div class="card bg-light mb-3" >
				
			<div class="card-header">
				<div class="row">
					<div class="col-sm-8">
						<h5>Nova Configuração - Modelos</h5>
					</div>
					<div class="col-sm-4">
						<a class="btn btn-primary  float-right" href="listar">Pesquisa de Configurações</a>
					</div>
				</div>
			</div>
							
			<div class="card-body">
				<form action="nova">	
			
					<div id="selecaoModelos" class="etapa  js-etapa">
						<h3 class="text-center p-4">
							<label for="modelo">Qual ou quais modelos você deseja configurar?</label>
						</h3>
					
						<div class="row">
							<div class="col-sm-12">
								<div class="form-group">																																																											
									<div style="display: flex; width: 100%">																												
					                    <select id="modelo" class="form-control siga-multiploselect  js-siga-multiploselect--modelo">				                    		
					                    	<option>Modelo 1</option>			                        			                            				                        				                       
					                    </select>						                    						                   
					                    <!--  <div class="invalid-feedback">
									     	Favor, selecione um ou mais modelos
									    </div>-->					            																															
									     <div>
					                    	<span class="spinner js-spinner--modelo"></span>
					                    </div>
					                  </div>
								</div>
							</div>						
						</div>
					</div>
					
					<div id="selecaoConfiguracao" class="etapa  js-etapa">
						<h3 class="text-center p-4">
							<label for="tipoConfiguracao">Qual configuração deseja aplicar?</label>
						</h3>
					
						<div class="row">
							<div class="col-sm-12">
								<div class="form-group">																
									<siga:select name="idTpConfiguracao"
										list="listaTiposConfiguracao" listKey="idTpConfiguracao"
										id="tipoConfiguracao" headerValue="[Selecione a configuração a ser aplicada]" headerKey="0"
										listValue="dscTpConfiguracao" theme="simple" />																																								                   			            																														
								</div>
							</div>						
						</div>
					</div>
					
					<div id="selecaoNivelAcesso" class="etapa  js-etapa">
						<h3 class="text-center p-4">
							<label for="nivelAcesso">Para quem se destina essa configuração?</label>
						</h3>
					
						<div class="row">
							<div class="col-sm-12">
								<div class="form-group">																												
									<select id="nivelAcesso" name="nivelAcesso" class="form-control  js-nivel-acesso">
										<option value="0">[Selecione como deverá ser o acesso a essa configuração]</option>									
										<option value="1">Órgãos</option>
										<option value="2">Unidades</option>
										<option value="3">Cargos</option>
										<option value="4">Usuários</option>
									</select>									            																														
								</div>
							</div>						
						</div>
					</div>
					
					<div id="selecaoOrgaosEUnidades" class="etapa  js-etapa">
						<h3 class="text-center p-4">
							<label for="idOrgaoUsu">Selecione os órgãos e por fim, selecione as unidades desejadas</label>
						</h3>
					
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">									
									<div style="display: flex; width: 100%">																												
										<select name="idOrgaoUsu" id="idOrgaoUsu" class="form-control  siga-multiploselect  js-siga-multiploselect--orgao">								
											<option>Unidade 1</option>
										</select>										
										<div>
					                    	<span class="spinner js-spinner--orgao"></span>
					                    </div>					          																														
									</div>
								</div>
							</div>
							
							<div class="col-sm-6">
								<div class="form-group">
									<div style="display: flex; width: 100%">																												
					                    <select id="unidade" class="form-control siga-multiploselect  js-siga-multiploselect--unidade">				               			  
				                        	<optgroup label="Prodesp">
					                            <option>Unidade 1</option>                  
					                            <option>Unidade 2</option>
					                            <option>Unidade 3</option>
					                            <option>Unidade 4</option>
					                        </optgroup>
					                    </select>						                    						                   					                    
					                    <div>
					                    	<span class="spinner js-spinner--unidade"></span>
					                    </div>
				            		</div>		   	            																													
								</div>
							</div>
						</div>														
					</div>
					
					<div style="overflow:auto;">
					  <div style="float:right;">
						  <button type="button" class="btn  btn-secondary  btn-anterior  js-btn-anterior"><i class="fas fa-long-arrow-alt-left"></i> Anterior</button>	
						  <button type="button" class="btn  btn-primary  btn-proximo  js-btn-proximo">Próximo <i class="fas fa-arrow-right"></i></button>					    					  
					  </div>
					</div>
					
					<div class="text-center  pt-4">
						<span class="indicador-etapa  js-indicador-etapa"></span>
						<span class="indicador-etapa  js-indicador-etapa"></span>
						<span class="indicador-etapa  js-indicador-etapa"></span>
						<span class="indicador-etapa  js-indicador-etapa"></span>
					</div>
								
				<!-- <div class="row">
					<div class="col-sm-12">
						<div class="form-group mb-0">
							<input type="submit" value="Ok"	class="btn btn-primary" /> 
							<input type="button" value="Cancelar" onclick="javascript:history.back();" class="btn btn-primary" />
						</div>
					</div>
				</div>-->
				
			</form>
		</div>			
		</div>
	</div>
	
<siga:siga-modal id="confirmacaoModal" exibirRodape="false" tituloADireita="Confirma&ccedil;&atilde;o">
	<div class="modal-body">
      		Você selecionou todos os modelos, tem certeza que deseja aplicar configuração em todos?
    	</div>
    	<div class="modal-footer">
      		<button type="button" class="btn btn-success" data-dismiss="modal">Voltar</button>		        
      		<a href="#" class="btn btn-danger btn-confirmacao-modal" role="button" aria-pressed="true">Continuar</a>
	</div>
</siga:siga-modal>				
<script type="text/javascript" src="/siga/javascript/selectpicker/bootstrap-select.min.js"></script>
<script type="text/javascript" src="/siga/javascript/siga.multiploselect.js"></script>
<script type="text/javascript" src="/siga/javascript/select2/select2.min.js"></script>
<script type="text/javascript" src="/siga/javascript/select2/i18n/pt-BR.js"></script>
<script type="text/javascript" src="../../../javascript/exconfiguracao.js"></script>
<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>			
</siga:pagina>
