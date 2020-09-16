<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Cadastro de configuração">	
<link rel="stylesheet" href="/siga/css/selectpicker/bootstrap-select.min.css" type="text/css" media="screen, projection"/>
<link rel="stylesheet" href="/siga/css/siga.multiploselect.css" type="text/css" media="screen, projection"/>
<link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection"/>
<link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css" media="screen, projection"/>
<link rel="stylesheet" href="../../../stylesheet/exconfiguracao.css"/>
 	
 	<div class="container-fluid">
		<div class="card bg-light mb-3">
				
			<div class="card-header">
				<div class="row">
					<div class="col-sm-9">
						<h5 class="titulo-nova-configuracao  js-titulo-nova-configuracao">Nova Configuração</h5>
					</div>
					<div class="col-sm-3  text-right">
						<a class="btn btn-outline-primary  btn-pesquisa-configuracoes  js-btn-pesquisa-configuracoes" href="#" title="Pesquisa de Configurações"><i class="fas fa-search"></i> Pesquisa</a>
						<a class="btn btn-outline-primary  btn-dicionario" href="tipos/dicionario" target="_blank" title="Dicionário de Tipos de Configurações"><i class="fas fa-book-reader"></i> Dicionário</a>						
					</div>
				</div>
			</div>
							
			<div class="card-body">
				<form name="formularioCadastro">				
					<div id="selecaoModelos" class="etapa  js-etapa">
						<h1 class="text-center display-4 p-4">
							<label for="modelo">Qual ou quais modelos você deseja configurar?</label>
						</h1>
					
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
						<h3 class="text-center display-4 p-4">
							<label for="tipoConfiguracao">Qual configuração deseja aplicar?</label>
						</h3>
					
						<div class="row">
							<div class="col-sm-12">
								<div class="form-group">																																																	
									<select id="tipoConfiguracao" name="tipoConfiguracao" class="form-control  js-tipo-configuracao siga-select2" 
										data-siga-select2-placeholder="Selecione a configuração a ser aplicada">
										<option value="">Selecione a configuração a ser aplicada</option>																											
										<c:forEach items="${tiposConfiguracao}" var="tipoConfiguracao">
											<option value="${tipoConfiguracao.idTpConfiguracao}">${tipoConfiguracao.dscTpConfiguracao}</option>
										</c:forEach>																				
									</select>																																										                   			            																														
								</div>
							</div>						
						</div>
					</div>
					
					<div id="selecaoNivelAcesso" class="etapa  js-etapa">
						<h3 class="text-center display-4 p-4">							
							<label for="nivelAcesso">Para quem se destina essa configuração?</label>							  
						</h3>
					
						<div class="row">
							<div class="col-sm-12">
								<div class="form-group">																												
									<select id="nivelAcesso" name="nivelAcesso" class="form-control  js-nivel-acesso siga-select2" 
										data-siga-select2-placeholder="Selecione como deverá ser o acesso a essa configuração">
										<option value="">Selecione como deverá ser o acesso a essa configuração</option>																											
										<c:forEach items="${niveisAcesso}" var="acesso">
											<option value="id_${acesso}">${acesso.descricao}</option>
										</c:forEach>																				
									</select>																            																													
								</div>
							</div>						
						</div>
					</div>
					
					<div id="selecaoOrgaosEUnidades" class="etapa  js-etapa">
						<h3 class="text-center display-4 p-4">
							<label for="idOrgaoUsu">Selecione os órgãos as unidades desejadas</label>
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
										
					<div class="row">						
					  	<div class="col-sm-12 text-right">					  	  
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
