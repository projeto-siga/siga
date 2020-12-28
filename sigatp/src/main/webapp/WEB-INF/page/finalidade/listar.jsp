<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<siga:pagina titulo="Transportes">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<sigatp:erros/>
			<h2><fmt:message key="finalidades" /></h2>
			<jsp:include page="menu.jsp"></jsp:include>
			<c:choose>
				<c:when test="${finalidades.size() > 0}">
					<div class="gt-content-box gt-for-table">
						<table id="htmlgrid" class="gt-table tablesorter" >
					    	<thead>
					    		<tr>
					    			<th width="1%" class="noSort"></th>
									<th width="1%" class="noSort"></th>
					    	    	<th width="90%">Descri&ccedil;&atilde;o</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${finalidades}" var="item">
							   		<tr>
										<td>
											<sigatp:formatarColuna operacao="editar" href="${linkTo[FinalidadeController].editar(item.id)}" titulo="finalidade" />
										</td>
										<td>
											<sigatp:formatarColuna operacao="excluir" href="${linkTo[FinalidadeController].excluir(item.id)}" titulo="finalidade"
											onclick="javascript:return confirm('Tem certeza de que deseja excluir os dados desta finalidade?');" />
										</td>
							   		
						    	    	<td>${item.descricao}</td>
									</tr>
								</c:forEach>
							</tbody>
					    </table>
					</div>
					<sigatp:paginacao />
				</c:when>
				<c:otherwise>
					<br/>
					<h3>N&atilde;o existem finalidades cadastradas.</h3>
				</c:otherwise>
			</c:choose>
			<div class="gt-table-buttons">
				<a href="${linkTo[FinalidadeController].incluir}" id="botaoIncluirFinalidade" class="gt-btn-medium gt-btn-left"><fmt:message key="views.botoes.incluir" /></a>
			</div>
		</div>
	</div>
</siga:pagina>