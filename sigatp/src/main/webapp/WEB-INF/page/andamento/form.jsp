<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>

<form id="formAndamentos" action="${linkTo[AndamentoController].salvar}" method="post" enctype="multipart/form-data" >
		<input type="hidden" name="andamento.requisicaoTransporte" value="${andamento.requisicaoTransporte.id}" />
		<input type="hidden" name="andamento.estadoRequisicao" value="${andamento.estadoRequisicao}" />
	<h3> A requisi&ccedil;&atilde;o ${andamento.requisicaoTransporte.buscarSequence()} ser&aacute; ${andamento.estadoRequisicao}</h3>
	<sigatp:erros />
	<div class="gt-content-box gt-form clearfix">
	    <c:choose>
	        <c:when test="${acao.equals('Autorizar')}">	
		       	<label for="andamento.descricao">Motivo</label>
			</c:when>
			<c:otherwise>
		       	<label for="andamento.descricao" class= "obrigatorio">Motivo</label>
			</c:otherwise>
		</c:choose>

		<sigatp:controleCaracteresTextArea nomeTextArea="andamento.descricao" rows="5" cols="80" valorTextArea="${andamento.descricao}" />
	</div>
	<br/>
    <c:if test="${!acao.equals('Autorizar')}">
    	<span class="alerta menor"><fmt:message key="views.erro.preenchimentoObrigatorio"/></span>
	</c:if>
	
	<div class="gt-table-buttons">
		<input type="submit" value="${acao}" class="gt-btn-medium gt-btn-left" />
		<c:choose>
			<c:when test="${acao.equals('Cancelar')}">
				<input type="button" value="<fmt:message key="views.botoes.voltar" />" onClick="javascript:location.href='${linkTo[RequisicaoController].buscarPelaSequence(popUp,andamento.requisicaoTransporte.buscarSequence())}'" class="gt-btn-medium gt-btn-left" />
			</c:when> 
			<c:otherwise> 
				<input type="button" value="<fmt:message key="views.botoes.voltar" />" onClick="javascript:location.href='${linkTo[RequisicaoController].listarPAprovar}'" class="gt-btn-medium gt-btn-left" />		
			</c:otherwise>
		</c:choose>
	</div>
</form>	
