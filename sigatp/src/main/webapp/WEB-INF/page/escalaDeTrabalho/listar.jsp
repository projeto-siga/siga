<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<siga:pagina titulo="Transportes">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Escalas de Trabalho</h2>
			<c:choose>
				<c:when test="${escalas.size() > 0}">
					<div class="gt-content-box gt-for-table">
					 	<table id="htmlgrid" class="gt-table tablesorter" >
					 		<thead>
						    	<tr style="font-weight: bold;">
						    	    <th width="15%">In&iacute;cio</th>
							   		<th width="15%">Fim</th>
							   		<th width="14%">Segunda</th>
							   		<th width="14%">Ter&ccedil;a</th>
							   		<th width="14%">Quarta</th>
							   		<th width="14%">Quinta</th>
							   		<th width="14%">Sexta</th>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${escalas}" var="item">
							   	<tr>
						    	    <td><fmt:formatDate pattern="dd/MM/yyyy" value="${item.dataVigenciaInicio.time}" /></td>
						    		<td><fmt:formatDate pattern="dd/MM/yyyy" value="${item.dataVigenciaFim.time}" /></td>
						    		<td>
						    			${item.condutor.telefoneInstitucional}
						    			<br/>
						    			${item.condutor.celularInstitucional}
						    		</td>
						    		<td>
						    			${item.condutor.telefonePessoal}
						    			<br/>
						    			${item.condutor.celularPessoal}
						    		</td>
						    		<td>Cat. ${item.condutor.categoriaCNH.toString()}
							    		<br/>
							    		<c:if test="${item.condutor.vencimentoCNHExpirado}">
							    			<span style="color: red; font-weight: bolder;">
							    		</c:if>
							    		<c:if test="${null != item.condutor.dataVencimentoCNH}">
							    			<fmt:formatDate pattern="dd/MM/yyyy" value="${item.condutor.dataVencimentoCNH.time}" />
							    		</c:if>
							    		<c:if test="${item.condutor.vencimentoCNHExpirado}">
							    			</span>
							    		</c:if>
						    		</td>
						    		<%-- 
						    			Os links abaixo foram modificados de Condutor para EscalaDeTrabalhoController para fazer sentido nessa tela
						    			Se fosse feita a transcrição do Play o método estaria tentando editar/excluir um condutor pelo id da escala de trabalho.
						    		 --%>
						    		<td><a href="${linkTo[EscalaDeTrabalhoController].editar(item.id)}"><fmt:message key="views.botoes.editar" /></a></td>
						    		<td><a href="${linkTo[EscalaDeTrabalhoController].excluir(item.id)}" onclick="javascript:return confirm('Tem certeza de que deseja excluir os dados desta escala de trabalho?');"><fmt:message key="views.botoes.excluir" /></a></td>
								</tr>
							</c:forEach>
							</tbody>
					     </table>
					     <sigatp:paginacao />
					</div>
				</c:when>
				<c:otherwise>
					<br/>
					<h3>N&atilde;o existem escalas de trabalho cadastradas para este condutor.</h3>
				</c:otherwise>
			</c:choose>
			<%-- Esse trecho está comentado pois para incluir uma nova escala de trabalho é necessário o id de um condutor. Foi apenas transcrito do Play.
 			<div class="gt-table-buttons">
				<a href="${linkTo[EscalaDeTrabalho].incluir(0)}" class="gt-btn-medium gt-btn-left"><fmt:message key="views.botoes.incluir" /></a>
			</div>
			--%>
		</div>
	</div>
</siga:pagina>