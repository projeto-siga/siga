<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>


<siga:pagina titulo="Lista Feriados">
	<!-- main content -->
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">		
			<h2 class="gt-table-head">Feriados cadastrados</h2>
			<div class="gt-content-box gt-for-table" style="width: 80% !important;">
				<table border="0" class="gt-table">
					<thead>
						<tr>							
							<th align="left" width="50%">Descrição</th>	
							<th align="right" width="15%">Data Inicio</th>
							<th align="right" width="15%">Data Fim</th>														
							<th colspan="2" align="right" width="20%">Opções</th>					
						</tr>
					</thead>
					
					<tbody>
						<c:forEach var="feriado" items="${itens}">
							<tr>								
								<td align="left" rowspan="${feriado.quantidadeOcorrencias}">${feriado.dscFeriado}</td>
								<ww:if test="${(not empty feriado.cpOcorrenciaFeriadoSet)}">
									<c:forEach var="ocorrencia" items="${feriado.cpOcorrenciaFeriadoSet}">			
										<td align="left">${ocorrencia.dtRegIniDDMMYY}</td>
										<td align="left">${ocorrencia.dtRegFimDDMMYY}</td>
										<td align="left"><ww:url id="url" action="editar_ocorrencia" namespace="/feriado">
															<ww:param name="id">${ocorrencia.idOcorrencia}</ww:param>
														</ww:url>
													<siga:link title="Alterar" url="${url}" />					
										</td>
										<td align="center" width="10%">									
	 			 							<a href="javascript:if (confirm('Deseja excluir o feriado?')) location.href='/siga/feriado/excluir_ocorrencia.action?id=${ocorrencia.idOcorrencia}';">
												<img style="display: inline;"
												src="/siga/css/famfamfam/icons/cancel_gray.png" title="Excluir feriado"							
												onmouseover="this.src='/siga/css/famfamfam/icons/cancel.png';" 
												onmouseout="this.src='/siga/css/famfamfam/icons/cancel_gray.png';"/>
											</a>															
										</td>							
									</c:forEach>										
								</ww:if>
								<ww:else>			
									<td></td><td></td><td></td><td></td><td></td>
								</ww:else>							 							
							</tr>
						</c:forEach>
					</tbody>
				</table>				
			</div>	
			<div class="gt-table-buttons">
					<ww:url id="url" action="editar" namespace="/feriado"></ww:url>
					<input type="button" value="Incluir"
						onclick="javascript:window.location.href='${url}'"
						class="gt-btn-medium gt-btn-left">
				</div>				
		</div>			
	</div>
</siga:pagina>
