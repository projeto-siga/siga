<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Cadastro de configuração">	
<style>	
	.titulo-card {		
		margin-top: .5rem;
	}

	.botao-chaveador-accordion {
		width: 100%;    
    	text-align: left;
	}
	
	.btn-pesquisa-configuracoes, .btn-nova-configuracao {
		max-width: 100%;
	}
	
	@media screen and (min-width: 575px) and (max-width: 945px) {
		.btn-nova-configuracao {
			margin-top: 10px;
		}
	}	
</style>
 	
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<div class="row">
					<div class="col-sm-6">
						<h5 class="titulo-card">Dicionário de Tipos de Configurações</h5>
					</div>
					<div class="col-sm-6  text-right">						
						<a class="btn btn-outline-primary  btn-pesquisa-configuracoes" href="../pesquisa" title="Pesquisa de Configurações"><i class="fas fa-search"></i> Pesquisa de Configurações</a>
						<a class="btn btn-primary  btn-nova-configuracao" href="../nova" title="Nova Configuração"><i class="fas fa-plus"></i> Nova Configuração</a>																
					</div>
				</div>
			</div>
												
			<div class="card-body">
				<div id="accordion">				
					<c:forEach items="${tiposConfiguracao}" var="tipoConfiguracao" varStatus="loop">															
						<div class="card">
						  <div class="card-header" id="heading_${loop.count}">
						    <h5 class="mb-0">
						      <button class="btn btn-link${loop.count != 1 ? '  collapsed' : ''} botao-chaveador-accordion" data-toggle="collapse" 
						      	data-target="#collapse_${loop.count}" aria-expanded="${loop.count == 1 ? true : false}" aria-controls="collapse_${loop.count}">
						        ${tipoConfiguracao.dscTpConfiguracao}
						      </button>						                                       
						    </h5>
						  </div>						
						  <div id="collapse_${loop.count}" class="collapse ${loop.count == 1 ? 'show' : ''}" aria-labelledby="heading_${loop.count}" 
						  	data-parent="#accordion">
						    <div class="card-body">
							    <c:set var="conteudo" value="${tipoConfiguracao.dicionario.conteudo}" />
							    <c:choose>
							    	<c:when test="${empty conteudo}">
							    		Nenhum conteúdo encontrado.							    		
							    	</c:when>
							    	<c:otherwise>
							    		${conteudo}						
							    	</c:otherwise>
							    </c:choose>									    							                                     											    						   						      	     
						    </div>
						  </div>
						</div>						
				  	</c:forEach>					  				  				  				 	
				</div>
			</div>			
		</div>
	</div>		
</siga:pagina>
