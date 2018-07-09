<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>
<jsp:include page="../tags/calendario.jsp" />
<sigatp:decimal/>

<siga:pagina titulo="SIGA:Transporte">
    <c:set var="mostrarBotoesEditar" value="true"></c:set>
	<c:set var="mostrarDadosProgramada" value="true"></c:set>
	<c:if test="#{servico.situacaoServico.equals('INICIADO')}">
		<c:set var="mostrarDadosIniciada" value="true"></c:set> 
	</c:if>
	
	<c:if test="${servico.situacaoServico.equals('REALIZADO')}">
		<c:set var="mostrarDadosIniciada" value="true"></c:set> 
		<c:set var="mostrarDadosFinalizada" value="true"></c:set> 
	</c:if>
	
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Editar Servi&ccedil;os de Ve&iacute;culos: ${servico.sequence}</h2>
			<jsp:include page="form.jsp"></jsp:include>
		</div>
	</div>
</siga:pagina>