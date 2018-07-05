<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<siga:pagina titulo="Transportes">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>${condutor.dadosParaExibicao}</h2>
			<h3>Escalas de Trabalho</h3>
			<jsp:include page="../condutor/menu.jsp"></jsp:include>
			<sigatp:erros/>
			<br/>
			<jsp:include page="form.jsp" />
			<c:choose>
				<c:when test="${escalas.size() > 0}">
					<div class="gt-content-box gt-for-table">    
					    <h3>&nbsp;&nbsp;Hist&oacute;rico das Escalas de Trabalho</h3> 
					 	<table id="htmlgrid" class="gt-table tablesorter">
					 		<thead>
						    	<tr style="font-weight: bold;">
						    	    <th>In&iacute;cio Vig&ecirc;ncia</th>
							   		<th>Fim Vig&ecirc;ncia</th>
							   		<th>Escala</th>
								</tr>
					 		</thead>
					 		<tbody>
								<c:forEach items="${escalas}" var="escala">
								   	<tr>
							    	    <td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${escala.dataVigenciaInicio.time}" /></td>
							    	    <td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${escala.dataVigenciaFim.time}" /></td>
							    		<td>${escala.escalaParaExibicao}</td>
									</tr>
								</c:forEach>
					 		</tbody>
					     </table>  
					</div>
				     <sigatp:paginacao/> 
				</c:when>
				<c:otherwise>
					<br/>
					<h3>N&atilde;o existem escalas cadastradas para este condutor.</h3>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</siga:pagina>