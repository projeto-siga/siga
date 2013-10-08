<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>


<siga:pagina titulo="Lista Orgão Externo">
	<!-- main content -->
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">		
			<h2 class="gt-table-head">Orgãos Externos cadastrados</h2>
			<div class="gt-content-box gt-for-table">
				<table border="0" class="gt-table">
					<thead>
						<tr>
							<th align="left">Nome</th>
							<th align="center">Sigla</th>
							<th align="left">Orgão Solicitante</th>
							<th align="right">Ativo</th>		
							<th colspan="2" align="center">Opções</th>					
						</tr>
					</thead>
					
					<tbody>
						<c:forEach var="orgao" items="${itens}">
							<tr>
								<td align="left">${orgao.descricao}</td>
								<td align="left">${orgao.sigla}</td>
								<td align="left">${orgao.orgaoUsuario.descricao}</td>
								<ww:if test="${(orgao.ativo == 'S')}">			
									<td align="left">Sim</td>
								</ww:if>
								<ww:else>			
									<td align="left">Não</td>
								</ww:else>	
								<td align="left">
									<ww:url id="url" action="editar" namespace="/orgao">
										<ww:param name="id">${orgao.idOrgao}</ww:param>
									</ww:url>
									<siga:link title="Alterar" url="${url}" />					
								</td>
							<%--	<td align="left">									
	 			 					<a href="javascript:if (confirm('Deseja excluir o orgão?')) location.href='/siga/orgao/excluir.action?id=${orgao.idOrgao}';">
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
					<ww:url id="url" action="editar" namespace="/orgao"></ww:url>
					<input type="button" value="Incluir"
						onclick="javascript:window.location.href='${url}'"
						class="gt-btn-medium gt-btn-left">
				</div>				
		</div>			
	</div>
</siga:pagina>
