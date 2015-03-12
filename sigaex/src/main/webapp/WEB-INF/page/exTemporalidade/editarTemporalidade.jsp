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

	<div class="gt-bd clearfix">
		<div class="gt-content">
			<h2 class="gt-form-head">${titulo_pagina}</h2>

			<div class="gt-form gt-content-box">
				<div id="divTemporalidade" class="clearfix">
					<!-- left column -->
					<form id="frmTemporalidade" action="gravar" method="post">
						<input type="hidden" id="acao" name="acao" value="${acao}" />
						<input type="hidden" id="idTemporalidade" name="idTemporalidade" value="${exTemporal.idTemporalidade}" />
						<div class="gt-left-col">

							<!-- form row -->
							<div class="gt-form-row gt-width-66">
								<label>Descrição</label> <input id="descTemporalidade" name="descTemporalidade" type="text" value="${exTemporal.descTemporalidade}" size="50"/>
							</div>
							<!-- /form row -->
	
							<!-- form row -->
							<div class="gt-form-row gt-width-66">
								<label>Valor</label>
								<select  id="valorTemporalidade" name="valorTemporalidade">
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
							<!-- /form row -->
							
							<!-- form row -->
							<div class="gt-form-row gt-width-66">

								<label>Unidade de Medida</label>
								<select id="idCpUnidade" name="idCpUnidade"> 
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
							<!-- form row -->
							
						</div>
					</form>
				</div>
				<div class="botoesTemporalidade">
					<!-- form row -->
					<div class="gt-form-row">
						<a id="btGravarTemporalidade" class="gt-btn-large gt-btn-left" style="cursor: pointer;" onclick="javascript:gravarTemporalidade()">Gravar</a>
							<p class="gt-cancel">
							<c:choose>
								<c:when test="${acao == 'editar_temporalidade'}">
										ou <a href="excluir?idTemporalidade=${exTemporal.idTemporalidade}">excluir</a>
								</c:when>
								<c:otherwise>
										ou <a href="${pageContext.request.contextPath}/app/expediente/temporalidade/listar">cancelar</a>
								</c:otherwise>
							</c:choose>
							</p>
						
					</div>
				</div>
			</div>
		</div>
	</div>
	
</siga:pagina>
