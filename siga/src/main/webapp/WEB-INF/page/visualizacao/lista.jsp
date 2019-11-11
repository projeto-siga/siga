<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%-- pageContext.setAttribute("sysdate", new java.util.Date()); --%>
<siga:pagina titulo="Lista Delegação">
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Delegar visualização</h5>
			</div>
			<div class="card-body">
				<div class="row">
					<div class="col-sm">
						<h6>Delegações cadastradas</h5>
						<table border="0" class="gt-table table table-sm table-hover">
							<thead class="thead-dark">
								<tr>
									<th align="left">Titular Delegante</th>
									<th align="left">Delegado</th>
									<th align="center">Data inicial</th>
									<th align="center">Data final</th>
									<th align="center">Opções</th>
								</tr>
							</thead>
							<tbody class="table-bordered">
								<c:forEach var="visualizacao" items="${itens}">
									<tr>
										<td align="left">
											${visualizacao.titular.nomePessoa}
										</td>
										<td align="left">
											${visualizacao.delegado.nomePessoa}
										</td>
										<td align="center">${visualizacao.dtIniDelegDDMMYY}</td>
										<td align="center">${visualizacao.dtFimDelegDDMMYY}</td>
										<td align="center">
											<siga:link title="Alterar" url="editar?id=${visualizacao.idVisualizacao}" />
																			
											<siga:link title="Excluir" url="exclui?id=${visualizacao.idVisualizacao}" 
												popup="excluir" confirm="Deseja excluir delegação?" />									
																		
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				<c:choose>
					<c:when test="${(isSubstituicao == 'true')}">			
						<div class="row">
							<div class="col-sm">
								<h6>Substituições cadastradas para o Titular</h6>
								<table border="0" class="gt-table table table-sm table-hover">
									<thead class="thead-dark">

										<tr>
											<th align="left">Titular</th>
											<th align="left">Substituto</th>
											<th align="center">Data inicial</th>
											<th align="center">Data final</th>
											<th align="center">Opções</th>
										</tr>
									</thead>
									<tbody  class="table-bordered">
										<c:forEach var="visTitular" items="${itensTitular}">
											<tr>
												<td align="left">
													${visTitular.titular.nomePessoa}
												</td>
												<td align="left">
													${visTitular.delegado.nomePessoa}
												</td>
												<td align="center">${visTitular.dtIniDelegDDMMYY}</td>
												<td align="center">${visTitular.dtFimDelegDDMMYY}</td>
												<td align="center">
													<siga:link title="Alterar" url="editar?id=${visTitular.idVisualizacao}" />
																					
													<siga:link title="Excluir" url="exclui?id=${visTitular.idVisualizacao}" 
														popup="excluir" confirm="Deseja excluir configuração?" />										
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>		
					</c:when>
					<c:otherwise>
						<div class="row">
							<div class="col-sm-2">
								<button type="button"  onclick="javascript:window.location.href='editar'" class="btn btn-primary">Incluir</button>
							</div>						
						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>	
</siga:pagina>