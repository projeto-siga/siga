<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<siga:pagina titulo="Transportes">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>${veiculo.dadosParaExibicao}</h2>
			<h3><fmt:message key="avarias" /></h3>
			<jsp:include page="../veiculo/menu.jsp" />
			<c:choose>
				<c:when test="${avarias.size() > 0}">
					<div class="gt-content-box gt-for-table">     
					 	<table id="htmlgrid" class="gt-table tablesorter" >
					 		<thead>
						    	<tr style="font-weight: bold;">
							    	<th width="1%" class="noSort"></th>
									<th width="1%" class="noSort"></th>
							   		<th width="10%">Data Registro</th>
							   		<th width="10%">Data Solu&ccedil;&atilde;o</th>
							   		<th>Descri&ccedil;&atilde;o</th>
								</tr>
					 		</thead>
					 		<tbody>
								<c:forEach items="${avarias}" var="avaria">
								   	<tr>
								   		<td>
											<sigatp:formatarColuna operacao="editar" href="${linkTo[AvariaController].editar(veiculo.id,avaria.id,true)}" titulo="avaria" />
										</td>
										<td>
											<sigatp:formatarColuna operacao="excluir" href="${linkTo[AvariaController].excluir(avaria.id,true)}" titulo="avaria"
											onclick="javascript:return confirm('Tem certeza de que deseja excluir esta avaria?');" />
										</td>
							    		<td><fmt:formatDate pattern="dd/MM/yyyy" value="${avaria.dataDeRegistro.time}"/></td>
							    		<td><fmt:formatDate pattern="dd/MM/yyyy" value="${avaria.dataDeSolucao.time}"/></td>
							    		<td style="white-space: pre-line;">${avaria.descricao}</td>
									</tr>
								</c:forEach>
					 		</tbody>
					     </table>
					</div>
	  		        <sigatp:paginacao />
				</c:when>
				<c:otherwise>
					<br/>
					<h3>N&atilde;o existem avarias cadastradas para este ve&iacute;culo.</h3>
				</c:otherwise>
			</c:choose>
			<div class="gt-table-buttons">
				<a href="${linkTo[AvariaController].incluir(veiculo.id)}" class="gt-btn-medium gt-btn-left"><fmt:message key="views.botoes.incluir" /></a>
			</div>
		</div>
	</div>
</siga:pagina>