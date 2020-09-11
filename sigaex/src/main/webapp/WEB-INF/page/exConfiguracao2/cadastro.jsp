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
	.js-etapa {
		display: none;
	}
	
	.js-indicador-etapa {
	  height: 15px;
	  width: 15px;
	  margin: 0 2px;
	  background-color: #007bff;
	  border: none;
	  border-radius: 50%;
	  display: inline-block;
	  opacity: 0.5;
	}
	
	.js-indicador-etapa.active {
  		opacity: 1;	
	}
	
	.js-indicador-etapa.finish {
  		background-color: #4CAF50;
	}
	
	.dropdown-menu.show {
		min-width: 100%!important;
		max-width: 100%!important;
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
			
					<div class="js-etapa">
						<h3 class="text-center p-4">
							<label for="modelo">Qual ou quais modelos você deseja configurar?</label>
						</h3>
					
						<div class="row">
							<div class="col-sm-12">
								<div class="form-group">																	
									<input type="hidden" name="idModeloPesquisa" value="${idModeloPesquisa}" id="inputHiddenModelosSelecionados" />																																		
				                    <select id="modelo" class="form-control siga-multiploselect  js-siga-multiploselect--modelo" data-quantidade-modelos="${quantidadeModelos}">				                    		
				                    	<c:forEach items="${listaModelos}" var="modelo">
				                    		<option value="${modelo.id}">${modelo.nmMod}</option>
				                    	</c:forEach>				                        			                            				                        				                       
				                    </select>						                    						                   
				                    <div class="invalid-feedback">
								     	Favor, selecione um ou mais modelos
								    </div>					            																															
								</div>
							</div>						
						</div>
					</div>
					
					<div class="js-etapa">
						<h3 class="text-center p-4">
							<label for="tipoConfiguracao">Qual configuração deseja aplicar?</label>
						</h3>
					
						<div class="row">
							<div class="col-sm-12">
								<div class="form-group">									
									<siga:select name="idTpConfiguracao"
										list="listaTiposConfiguracao" listKey="idTpConfiguracao"
										id="tipoConfiguracao" headerValue="[Indefinido]" headerKey="0"
										listValue="dscTpConfiguracao" theme="simple" />																																				
				                    <div class="invalid-feedback">
								     	Favor, selecione um ou mais modelos
								    </div>					            																															
								</div>
							</div>						
						</div>
					</div>
					
					<div class="js-etapa">
						<h3 class="text-center p-4">
							<label for="nivelAcesso">Escolha o nível de acesso</label>
						</h3>
					
						<div class="row">
							<div class="col-sm-12">
								<div class="form-group">
									<siga:select id="nivelAcesso" name="idNivelAcesso"
										list="listaNivelAcesso" theme="simple" listKey="idNivelAcesso"
										listValue="nmNivelAcesso" headerValue="[Indefinido]"
										headerKey="0" value="${idNivelAcesso}" />			            																															
								</div>
							</div>						
						</div>
					</div>
					
					<div class="js-etapa">
						<h3 class="text-center p-4">
							<label for="unidade">E por fim, selecione as unidades</label>
						</h3>
					
						<div class="form-group">										
							<input type="hidden" name="idModeloPesquisa" value="${idModeloPesquisa}" id="inputHiddenModelosSelecionados" />																			
		                    <select id="unidade" class="form-control siga-multiploselect  js-siga-multiploselect--modelo">
		                        <optgroup label="Secretaria de Governo">
		                            <option>Modelo 1</option>                  
		                            <option>Modelo 2</option>
		                            <option>Modelo 3</option>
		                            <option>Modelo 4</option>
		                        </optgroup>				                        
		                    </select>						                    						                   
		                    <div class="invalid-feedback">
						     	Favor, selecione uma ou mais unidades
						    </div>					            																															
						</div>
					</div>
					
					<div style="overflow:auto;">
					  <div style="float:right;">
						  <button type="button" class="btn  btn-secondary  js-btn-anterior">Anterior</button>	
						  <button type="button" class="btn  btn-primary  js-btn-proximo">Próximo</button>					    					  
					  </div>
					</div>
					
					<div class="text-center  pt-4">
						<a href="#" class="js-link-indicador-etapa" data-indicador-etapa="1"><span class="js-indicador-etapa"></span></a>
						<a href="#" class="js-link-indicador-etapa" data-indicador-etapa="2"><span class="js-indicador-etapa"></span></a>
						<a href="#" class="js-link-indicador-etapa" data-indicador-etapa="3"><span class="js-indicador-etapa"></span></a>
						<a href="#" class="js-link-indicador-etapa" data-indicador-etapa="4"><span class="js-indicador-etapa"></span></a>
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
<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>
<script type="text/javascript" src="../../../javascript/exconfiguracao.js"></script>			
</siga:pagina>
