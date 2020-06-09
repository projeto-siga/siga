<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="idModal"%>
<%@ attribute name="exibirCabecalho"%>
<%@ attribute name="texto"%>
<%@ attribute name="descricaoBotaoQueFechaModal"%>
<%@ attribute name="exibirBotaoDeAcao"%>
<%@ attribute name="descricaoBotaoDeAcao"%>
<%@ attribute name="urlBotaoDeAcao"%>

<c:if test="${not empty texto}">
	<div class="modal fade" id="${idModal}" tabindex="-1" role="dialog" aria-labelledby="confirmacao" aria-hidden="true">
	  <div class="modal-dialog text-center" role="document">
	    <div class="modal-content">
	    	<c:if test="${exibirCabecalho eq 'true'}">
		      <div class="modal-header">
		        <div class="col-12" style="margin: 0 auto;">
		        	<i class="fas fa-exclamation-circle" style="font-size: 3em; color: #ffc107;"></i>
		        	<h5 class="modal-title" id="confirmacao" style="font-size: 1.3em; font-weight: bold">Atenção</h5>
		        </div>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close" style="margin-left: -40px">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
	      </c:if>
	      <div class="modal-body text-center">${texto}</div>
	      <div class="modal-footer text-center">
	      	<div class="row" style="margin: 0 auto;">
		        <button type="button" class="btn btn-secondary" data-dismiss="modal">${descricaoBotaoQueFechaModal}</button>
		        <c:if test="${exibirBotaoDeAcao eq 'true'}">		        
		        	<a href="${urlBotaoDeAcao}" class="btn btn-primary btn-acao" role="button" aria-pressed="true" style="margin-left: .5rem;">
		        		${descricaoBotaoDeAcao}
		        	</a>
		        </c:if>
		    </div>    
	      </div>
	    </div>
	  </div>
	</div>
</c:if>