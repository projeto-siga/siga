<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ attribute name="id" required="true" 
	description="Identificador único do modal"%>
<%@ attribute name="centralizar"
	description="Quando true, o modal será exibido no centro da tela. Quando false ou não informado, o modal será exibido centralizado 
		no topo da tela"%>		
<%@ attribute name="tamanhoGrande"
	description="Quando true, o modal será aberto ocupando um espaço grande na tela. Quando false ou não informado, o modal será aberto no tamanho normal"%>
<%@ attribute name="abrirAoCarregarPagina" 
	description="Quando true, modal será aberto automaticamente após carregamento da página. Quando false ou não informado, modal não será aberto automaticamente"%>
<%@ attribute name="botaoFecharNoCabecalho" 
	description="Quando true ou não informado, exibirá botão 'X' no cabeçalho para fechar modal. Quando false, não exibirá o botão"%>
<%@ attribute name="tituloADireita"
	description="Texto que será exibido no cabeçalho alinhado à direita"%>
<%@ attribute name="exibirRodape"
	description="Quando true, será exibido o rodapé padrão. Quando false ou não informado, não exibirá o rodapé padrão"%>
<%@ attribute name="descricaoBotaoFechaModalDoRodape"
	description="Texto que será exibido no botão do rodapé para fechar modal, caso não informado, será exibido por padrão o texto 'Fechar'. O botão somente
		será exibido se modal estiver para exibir o rodapé padrão"%>
<%@ attribute name="linkBotaoDeAcao"
	description="Quando informado um texto como link, exibirá um botão extra. Somente será exibido se modal estiver para exibir o rodapé padrão. Por padrão, a descrição
		do botão será 'Ok', caso deseje uma descrição diferente, basta informar através do atributo 'descricaoBotaoDeAcao'"%>
<%@ attribute name="descricaoBotaoDeAcao"
	description="Texto para o botão de ação, caso não informado, será exibido por padrão o texto 'Ok' Somente será exibido se modal tiver para exibir o rodapé 
		padrão e também tiver sido adicionado um link através do atributo 'linkBotaoDeAcao'"%>							

<div class="modal  fade" id="${id}" tabindex="-1" role="dialog">
 	<div class="modal-dialog${centralizar ? '  modal-dialog-centered' : ''}${tamanhoGrande ? '  modal-lg' : ''}" role="document">
   		<div class="modal-content">
   			<div class="modal-header">  
   				<div class="col-6  p-0"> 	   							   	
   					<img src="${uri_logo_siga_pequeno}" class="siga-modal__logo" alt="logo siga">
   				</div>   						
   				<c:if test="${not empty tituloADireita}">
   					<div class="col-6  p-0">
   						<h4 class="modal-title  siga-modal__titulo  siga-modal__titulo--direita">
   							${tituloADireita}
   							<c:if test="${empty botaoFecharNoCabecalho or not botaoFecharNoCabecalho eq 'false'}">
   								&nbsp;&nbsp;&nbsp;
   							</c:if>
   						</h4>   						
   					</div>
   				</c:if>   				
   				<c:if test="${empty botaoFecharNoCabecalho or not botaoFecharNoCabecalho eq 'false'}">
					<button type="button" class="close  p-0  m-0  siga-modal__btn-close" data-dismiss="modal" aria-label="Close">
		          		<span aria-hidden="true">&times;</span>
		        	</button>   				    					
	        	</c:if>			  				       			     
      		</div>
      		<jsp:doBody />      		   				     
     		<c:if test="${exibirRodape}">
	     		<div class="modal-footer">			      	
			        <button type="button" class="btn btn-secondary  siga-modal__btn-fechar-rodape" data-dismiss="modal">${empty descricaoBotaoFechaModalDoRodape ? 'Fechar' : descricaoBotaoFechaModalDoRodape}</button>
			        <c:if test="${not empty linkBotaoDeAcao}">		        
			        	<a href="${linkBotaoDeAcao}" class="btn btn-primary  siga-modal__btn-acao" role="button" aria-pressed="true">
			        		${empty descricaoBotaoDeAcao ? 'Ok' : descricaoBotaoDeAcao}
			        	</a>
			        </c:if>				        
		      </div>
			</c:if>      
   		</div>
 	</div> 	 	
</div>

<c:if test="${abrirAoCarregarPagina}">
	<script>
		$(function() {			
			sigaModal.abrir('${id}');
		});		
	</script>
</c:if>