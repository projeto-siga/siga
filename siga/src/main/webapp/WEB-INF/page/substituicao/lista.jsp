<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%-- pageContext.setAttribute("sysdate", new java.util.Date()); --%>
<siga:pagina titulo="Lista Substituições">
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Gerenciar possíveis substitutos</h5>
			</div>
			<div class="card-body">
				<div class="row">
					<div class="col-sm">
						<h6>Substituições cadastradas</h5>
						<table border="0" class="gt-table table table-sm table-hover">
							<thead class="${thead_color}">
								<tr>
									<th align="left">Titular</th>
									<th align="left">Substituto</th>
									<th align="center">Data inicial</th>
									<th align="center">Data final</th>
									<th align="center">Opções</th>
								</tr>
							</thead>
							<tbody class="table-bordered">
								<c:forEach var="substituicao" items="${itens}">
									<tr>
										<td align="left">
											<c:choose>
												<c:when test="${not empty substituicao.titular}">
													${substituicao.titular.nomePessoa}
												</c:when>
												<c:otherwise>
													${substituicao.lotaTitular.nomeLotacao}
												</c:otherwise>
											</c:choose>
										</td>
										<td align="left">
											<c:choose>
												<c:when test="${not empty substituicao.substituto}">
													${substituicao.substituto.nomePessoa}
												</c:when>
												<c:otherwise>
													${substituicao.lotaSubstituto.nomeLotacao}
												</c:otherwise>
											</c:choose>
										</td>
										<td align="center">${substituicao.dtIniSubstDDMMYY}</td>
										<td align="center">${substituicao.dtFimSubstDDMMYY}</td>
										<td align="center">
											<siga:link title="Alterar" url="editar?id=${substituicao.idSubstituicao}" />
																			
											<siga:link title="Excluir" url="exclui?id=${substituicao.idSubstituicao}" 
												popup="excluir" confirm="Deseja excluir substituição?" />									
																		
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
									<thead class="${thead_color}">

										<tr>
											<th align="left">Titular</th>
											<th align="left">Substituto</th>
											<th align="center">Data inicial</th>
											<th align="center">Data final</th>
											<th align="center">Opções</th>
										</tr>
									</thead>
									<tbody  class="table-bordered">
										<c:forEach var="substTitular" items="${itensTitular}">
											<tr>
												<td align="left">
													<c:choose>
														<c:when test="${not empty substTitular.titular}">
																${substTitular.titular.nomePessoa}
														</c:when>
														<c:otherwise>
																${substTitular.lotaTitular.nomeLotacao}
														</c:otherwise>	
													</c:choose>
												</td>
												<td align="left">
													<c:choose>
														<c:when test="${not empty substTitular.substituto}">
															${substTitular.substituto.nomePessoa}
														</c:when>
														<c:otherwise>
															${substTitular.lotaSubstituto.nomeLotacao}
														</c:otherwise>
													</c:choose>
												</td>
												<td align="center">${substTitular.dtIniSubstDDMMYY}</td>
												<td align="center">${substTitular.dtFimSubstDDMMYY}</td>
												<td align="center">
													<siga:link title="Alterar" url="editar?id=${substTitular.idSubstituicao}" />
																					
													<siga:link title="Excluir" url="exclui?id=${substTitular.idSubstituicao}" 
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
