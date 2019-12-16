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
	
	<div class="container-fluid">
		<div class="card bg-light mb-3" >		
			<div class="card-header"><h5>Cadastro de Feriados</h5></div>
				<div class="card-body">
					<form method="post" name="frm" action="/siga/app/feriado/salvar">
						<input type="hidden" name="id" /> 
						<input type="hidden" name="postback" value="1" />
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label>Feriado</label> 
									<input type="text" name="dscFeriado" id="dscFeriado" value="${dscFeriado}" maxlength="60" size="60" class="form-control"/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12">
								<div class="form-group">
									<input type="button" value="Salvar" onclick="javascript: validar();"
										class="btn btn-primary" />	
									<input  type="button" value="Excluir" onclick="javascript:if (confirm('Deseja excluir o feriado?')) location.href='/siga/app/feriado/excluir?id=${id}';"
									       class="btn btn-primary" />											
									<input  type="button" value="Voltar" onclick="javascript:location.href='/siga';"
									       class="btn btn-primary" />
								</div>	
							</div>
						</div>
					</form>
				</div>
			</div>
			<h5>Feriados cadastrados</h5>
				<table class="table table-sm table-striped">
					<thead class="${thead_color}">
						<tr>							
							<th align="left" width="30%">Descrição</th>	
							<th align="right" width="20%">Incluir ocorrência</th>
							<th align="right" width="15%">Data Inicio</th>
							<th align="right" width="15%">Data Fim</th>														
							<th colspan="2" align="right" width="20%">Opções de ocorrência</th>					
						</tr>
					</thead>
					<tbody class="table-bordered">
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
									<input type="button" value="Incluir"
										class="btn btn-primary" onclick="javascript:location.href='${urlI}'"/>	
								</td>
								<c:choose>									
									<c:when test="${(not empty feriado.cpOcorrenciaFeriadoSet)}">
										<c:forEach var="ocorrencia" items="${feriado.cpOcorrenciaFeriadoSet}">	
																					
											<td align="left">${ocorrencia.dtRegIniDDMMYYYY}</td>
											<td align="left">${ocorrencia.dtRegFimDDMMYYYY}</td>
											<td align="left">
												<c:url var="url" value="/app/feriado/editar-ocorrencia">
													<c:param name="idOcorrencia">${ocorrencia.idOcorrencia}</c:param>
													<c:param name="id">${feriado.id}</c:param>
												</c:url>
												<input type="button" value="Alterar"
													class="btn btn-primary" onclick="javascript:location.href='${url}'"/>					
											</td>
											<td align="center" width="10%">									
		 			 							<a href="javascript:if (confirm('Deseja excluir a ocorrência do feriado?')) location.href='/siga/app/feriado/excluir-ocorrencia?idOcorrencia=${ocorrencia.idOcorrencia}';">
													<img style="display: inline;"
													src="/siga/css/famfamfam/icons/cancel_gray.png" title="Excluir feriado"							
													onmouseover="this.src='/siga/css/famfamfam/icons/cancel.png';" 
													onmouseout="this.src='/siga/css/famfamfam/icons/cancel_gray.png';"/>
												</a>															
											</td>
																											
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
</siga:pagina>
