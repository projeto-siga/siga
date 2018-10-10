<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Lista Orgão Usuário">
	<!-- main content -->
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">		
			<h2 class="gt-table-head">Org&atilde;os cadastrados</h2>
			<div class="gt-content-box gt-for-table">
				<table border="0" class="gt-table">
					<thead>
						<tr>
							<th algin="center">ID</th>
							<th align="left">Nome</th>
							<th align="center">Sigla</th>
							<th colspan="2" align="center">Op&ccedil;&oacute;es</th>					
						</tr>
					</thead>
					
					<tbody>
						<c:forEach var="orgaoUsuario" items="${itens}">
							<tr>
								<td align="left">${orgaoUsuario.id}</td>
								<td align="left">${orgaoUsuario.descricao}</td>
								<td align="left">${orgaoUsuario.sigla}</td>
								<td align="left">
									<c:url var="url" value="/app/orgaoUsuario/editar">
										<c:param name="id" value="${orgaoUsuario.id}"></c:param>
									</c:url>
									<siga:link title="Alterar" url="${url}" />					
								</td>
							<%--	<td align="left">									
	 			 					<a href="javascript:if (confirm('Deseja excluir o orgão?')) location.href='/siga/app/orgao/excluir?id=${orgao.idOrgao}';">
										<img style="display: inline;"
										src="/siga/css/famfamfam/icons/cancel_gray.png" title="Excluir orgão"							
										onmouseover="this.src='/siga/css/famfamfam/icons/cancel.png';" 
										onmouseout="this.src='/siga/css/famfamfam/icons/cancel_gray.png';"/>
									</a>															
								</td>
							 --%>							
							</tr>
						</c:forEach>
					</tbody>
				</table>				
			</div>	
			<div class="gt-table-buttons">
					<c:url var="url" value="/app/orgaoUsuario/editar"></c:url>
					<input type="button" value="Incluir"
						onclick="javascript:window.location.href='${url}'"
						class="gt-btn-medium gt-btn-left">
				</div>				
		</div>			
	</div>
</siga:pagina>