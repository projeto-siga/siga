<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%-- pageContext.setAttribute("sysdate", new java.util.Date()); --%>
<siga:pagina titulo="Lista Substituições">
	<!-- main content -->
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">		
			<h2 class="gt-table-head">Substituições cadastradas</h2>
			<div class="gt-content-box gt-for-table">
				<table border="0" class="gt-table">
					<thead>
						<tr>
							<th align="left">Titular</th>
							<th align="left">Substituto</th>
							<th align="center">Data inicial</th>
							<th align="center">Data final</th>
							<th align="center">Opções</th>
						</tr>
					</thead>
					
					<tbody>
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
	
			<br/>    
			
			<c:choose>
				<c:when test="${(isSubstituicao == 'true')}">			
					<h2 class="gt-table-head">Substituições cadastradas para o Titular</h2>
					<div class="gt-content-box gt-for-table">
						<table border="0" class="gt-table">
							<thead>
								<tr>
									<th align="left">Titular</th>
									<th align="left">Substituto</th>
									<th align="center">Data inicial</th>
									<th align="center">Data final</th>
									<th align="center">Opções</th>
								</tr>
							</thead>
	
							<tbody>
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
				</c:when>
				<c:otherwise>
					<div class="gt-table-buttons">
						<input type="button" value="Incluir"
							onclick="javascript:window.location.href='editar'"		
						    class="gt-btn-medium gt-btn-left">
					</div>			
				</c:otherwise>
			</c:choose>
		</div>			
	</div>
</siga:pagina>
