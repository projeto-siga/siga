<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="Lista Feriados">
	<!-- main content -->
	
	<script type="text/javascript">
	function validar() {
		var descricao = $('#dscFeriado').val();		
		if (descricao==null || descricao=="") {			
			alert("Preencha a descricao do feriado");
			document.getElementById('dscFeriado').focus();		
		}else 
			frm.submit();					
	}
	</script>
	
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
				
			<h2>Cadastro de Feriados</h2>
				<div class="gt-content-box gt-for-table" style="width: 80% !important;">
					<form method="post" name="frm" action="/siga/app/feriado/salvar">
						<input type="hidden" name="id" /> 
						<input type="hidden" name="postback" value="1" />
						<table class="gt-form-table">
							<tr>
								<td width="4%" align="right">Feriado:</td>
								<td><input type="text" name="dscFeriado" id="dscFeriado" value="${dscFeriado}" maxlength="60" size="60" /></td>
							</tr>
							<tr>	
								<td colspan="2"><input type="button" value="Salvar" onclick="javascript: validar();"
										class="gt-btn-medium gt-btn-left" />	
									<input  type="button" value="Excluir" onclick="javascript:if (confirm('Deseja excluir o feriado?')) location.href='/siga/app/feriado/excluir?id=${id}';"
									       class="gt-btn-medium gt-btn-left" />											
									<input  type="button" value="Voltar" onclick="javascript:location.href='/siga';"
									       class="gt-btn-medium gt-btn-left" />	
								</td>
							</tr>
						</table>
					</form>
				</div>
				
				
			<h2 class="gt-table-head">Feriados cadastrados</h2>
			<div class="gt-content-box gt-for-table" style="width: 80% !important;">
				<table class="gt-table">
					<thead>
						<tr>							
							<th align="left" width="30%">Descrição</th>	
							<th align="right" width="20%">Incluir ocorrência</th>
							<th align="right" width="15%">Data Inicio</th>
							<th align="right" width="15%">Data Fim</th>														
							<th colspan="2" align="right" width="20%">Opções de ocorrência</th>					
						</tr>
					</thead>
					
					<tbody>
						<c:forEach var="feriado" items="${itens}">
							<tr>							
								<c:url var="url" value="/app/feriado/listar">
									<c:param name="id">${feriado.id}</c:param>
								</c:url>	
								<c:url var="urlI" value="/app/feriado/editar-ocorrencia">
									<c:param name="id">${feriado.id}</c:param>	
								</c:url>														
								<td align="left" rowspan="${feriado.quantidadeOcorrencias+1}">
									<a href="${url}">${feriado.dscFeriado}</a>
								</td>
								<td align="center" rowspan="${feriado.quantidadeOcorrencias+1}">
									<siga:links inline="${true}" separator="${false}">
										<siga:link title="Incluir" url="${urlI}" />
									</siga:links>
								</td>
								<c:choose>									
									<c:when test="${(not empty feriado.cpOcorrenciaFeriadoSet)}">
										<c:forEach var="ocorrencia" items="${feriado.cpOcorrenciaFeriadoSet}">	
										<tr>											
											<td align="left">${ocorrencia.dtRegIniDDMMYYYY}</td>
											<td align="left">${ocorrencia.dtRegFimDDMMYYYY}</td>
											<td align="left">
												<c:url var="url" value="/app/feriado/editar-ocorrencia">
													<c:param name="idOcorrencia">${ocorrencia.idOcorrencia}</c:param>
													<c:param name="id">${feriado.id}</c:param>
												</c:url>
												<siga:links inline="${true}" separator="${false}">
													<siga:link title="Alterar" url="${url}" />
												</siga:links>					
											</td>
											<td align="center" width="10%">									
		 			 							<a href="javascript:if (confirm('Deseja excluir a ocorrência do feriado?')) location.href='/siga/app/feriado/excluir-ocorrencia?idOcorrencia=${ocorrencia.idOcorrencia}';">
													<img style="display: inline;"
													src="/siga/css/famfamfam/icons/cancel_gray.png" title="Excluir feriado"							
													onmouseover="this.src='/siga/css/famfamfam/icons/cancel.png';" 
													onmouseout="this.src='/siga/css/famfamfam/icons/cancel_gray.png';"/>
												</a>															
											</td>
										</tr>																	
										</c:forEach>										
									</c:when>
									<c:otherwise>										
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>										
									</c:otherwise>	
								</c:choose>													 							
							</tr>
						</c:forEach>
					</tbody>
				</table>				
			</div>				
		</div>	
	</div>		
</siga:pagina>
