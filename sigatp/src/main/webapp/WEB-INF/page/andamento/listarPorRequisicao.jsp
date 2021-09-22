<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<siga:pagina titulo="SIGA - Transporte" > 
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<c:choose>
				<c:when test="${andamentos.size() > 0}">
					<h2>Rela&ccedil;&atilde;o de Andamentos da requisi&ccedil;&atilde;o ${andamentos.get(0).requisicaoTransporte.buscarSequence()} </h2>
					<jsp:include page="../requisicao/menu.jsp" ></jsp:include>
					<div class="gt-content-box gt-for-table">     
					 	<table id="htmlgrid" class="gt-table tablesorter" >
					 	<thead>
					    	<tr style="font-weight: bold;">
					    		<th width="10%">Data do Andamento</th>
					    	    <th width="10%">Estado</th>
					    		<th width="30%">Respons&aacute;vel</th>
						   		<th width="20%">Descri&ccedil;&atilde;o</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${andamentos}" var="andamento" >
							   	<tr id ="row_${andamento.requisicaoTransporte.id}">	
						    	    <td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${andamento.dataAndamento.time}" /></td>
						    		<td style="white-space: pre;">${andamento.estadoRequisicao}</td>
						    	    <td style="white-space: pre;">${andamento.responsavel != null ? andamento.responsavel.siglaCompleta : ""} - ${andamento.responsavel != null ? andamento.responsavel.nomePessoaAI : ""}</td>
						    	    <c:choose>
								        <c:when test="${andamento.missao != null}">
									        <c:set var="linkMissaoMontado" value="<nobr><a href=\"${linkTo[MissaoController].buscarPelaSequence(false,andamento.missao.sequence)}\">${andamento.missao.sequence}</a> <a href=\"#\" onclick=\"javascript:window.open('${linkTo[MissaoController].buscarPelaSequence(true,andamento.missao.sequence)}');\"><img src=\"/sigatp/public/images/linknovajanelaicon.png\" alt=\"Abrir em uma nova janela\" title=\"Abrir em uma nova janela\"></a></nobr>" />
									        <c:choose>
									        	<c:when test="${fn:contains(andamento.descricao, andamento.missao.sequence)}">
									        		<c:set var="descricaoMontada" value="${fn:replace(andamento.descricao, andamento.missao.sequence, linkMissaoMontado)}" />
								        		</c:when>
									        	<c:otherwise>
									        		<c:set var="descricaoMontada" value="${andamento.descricao} - Miss&atilde;o ${linkMissaoMontado}" />
									        	</c:otherwise>
								        	</c:choose>
								        </c:when>
										<c:otherwise>
											<c:set var="descricaoMontada" value="${andamento.descricao}" />
										</c:otherwise>
									</c:choose>
						    		<td style="white-space: pre;">${descricaoMontada}</td>
								</tr>
							</c:forEach>
						</tbody>
					     </table>  
					</div>
					<sigatp:paginacao/> 
					
				</c:when>
				<c:otherwise>
					<h2>Rela&ccedil;&atilde;o de Andamentos</h2>
					<jsp:include page="../requisicao/menu.jsp" ></jsp:include>
				</c:otherwise>
			</c:choose>
	   </div>
	</div>
</siga:pagina>