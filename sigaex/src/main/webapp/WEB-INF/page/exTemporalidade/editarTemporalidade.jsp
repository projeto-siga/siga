<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Temporalidade Documental">

<script type="text/javascript">
	//funções de dados
	function gravarTemporalidade(){
		$('#frmTemporalidade').submit();
	}
	
	function ocultarBotoesTemporalidade(){
		$('.botoesTemporalidade').hide(delay);
		$('#divTemporalidade :input').attr("disabled", true);
	}
	function exibirBotoesTemporalidade(){
		$('.botoesTemporalidade').show(delay);
		$('#divTemporalidade :input').attr("disabled", false);
	}
</script>

<c:choose>
	<c:when test="${acao == 'editar_temporalidade'}">
		<c:set var="titulo_pagina" value="Editar Temporalidade - ${exTemporal.descTemporalidade}"></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="titulo_pagina" value="Nova Temporalidade"></c:set>
	</c:otherwise>
</c:choose>

<div class="container-fluid">
	<div class="card bg-light mb-3" >
		<div class="card-header"><h5>${titulo_pagina}</h5></div>

			<div class="card-body">
				<div id="divTemporalidade" class="clearfix">
					<!-- left column -->
					<form id="frmTemporalidade" action="gravar" method="post">
						<input type="hidden" id="acao" name="acao" value="${acao}" />
						<input type="hidden" id="idTemporalidade" name="idTemporalidade" value="${exTemporal.idTemporalidade}" />
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label>Descrição</label>
									<input id="descTemporalidade" name="descTemporalidade" type="text" value="${exTemporal.descTemporalidade}" size="50" class="form-control"/>
								</div>
							</div>
							<div class="col-sm-3">
								<div class="form-group">
									<label>Valor</label>
									<select  id="valorTemporalidade" name="valorTemporalidade" class="form-control">
										<option value="-1">[Sem valor]</option>								
										<c:forEach begin="1" end="365" var="item">
											<c:choose>
												<c:when test="${exTemporal.valorTemporalidade==item}">
													<option value="${item}" selected="selected">${item}</option>
												</c:when>
												<c:otherwise>
													<option value="${item}">${item}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="col-sm-3">
								<div class="form-group">
									<label>Unidade de Medida</label>
									<select id="idCpUnidade" name="idCpUnidade" class="form-control"> 
										<option value="-1">[Sem valor]</option>
										<c:forEach items="${listaCpUnidade}" var="itemLista">
											<c:choose>
												<c:when test="${itemLista.idUnidadeMedida == exTemporal.cpUnidadeMedida.idUnidadeMedida}">
													<option value="${itemLista.idUnidadeMedida}" selected="selected">${itemLista.descricao}</option>
												</c:when>
												<c:otherwise>
													<option value="${itemLista.idUnidadeMedida}">${itemLista.descricao}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="botoesTemporalidade">
					<!-- form row -->
					<div class="gt-form-row">
						<input type="button" value="Gravar" onclick="javascript:gravarTemporalidade()" class="btn btn-primary" />
						<c:choose>
							<c:when test="${acao == 'editar_temporalidade'}">
								<input type="button" value="Excluir" onclick="javascript:location.href='${pageContext.request.contextPath}/app/expediente/temporalidade/excluir?idTemporalidade=${exTemporal.idTemporalidade}';" class="btn btn-primary" />
							</c:when>
							<c:otherwise>
								<input type="button" value="Cancelar" onclick="javascript:location.href='${pageContext.request.contextPath}/app/expediente/temporalidade/listar';" class="btn btn-primary" />
							</c:otherwise>
						</c:choose>
						
					</div>
				</div>
			</div>
		</div>
	</div>
	
</siga:pagina>
