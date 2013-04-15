<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>


<siga:pagina titulo="Classificação Documental">
<script type="text/javascript">
	function gravarClassificacao(){
		$('#frmClassificacao').submit();
	}
	function adicionarVia(){
		$('#divBotoesClassificacao').hide(400);
		$('#divClassificacao :input').attr("disabled", true);
		exibirNovaVia();
	}

	function exibirNovaVia(){
		$('#divVias').show()
	}
</script>
<c:choose>
	<c:when test="${acao == 'editar_classificacao'}">
		<c:set var="titulo_pagina" value="Editar Classificação - ${codificacao}"></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="titulo_pagina" value="Nova Classificação"></c:set>
	</c:otherwise>
</c:choose>

	<h2 class="gt-table-head">Classificação Documental</h2>
	<div class="gt-bd clearfix">
		<div class="gt-content">
			<h2 class="gt-form-head">${titulo_pagina}</h2>

			<div class="gt-form gt-content-box">
				<div id="divClassificacao" class="clearfix">
					<!-- left column -->
					<ww:form id="frmClassificacao" action="gravar" method="aGravar">
						<div class="gt-left-col">
							<!-- form row -->
							<div class="gt-form-row gt-width-66">
								<label>Codificação</label><input id="codificacao" name="codificacao" type="text" class="gt-form-text" value="${exClass.codificacao}"/>
								<input type="hidden" id="codificacaoAntiga" name="codificacaoAntiga" value="${exClass.codificacao}"/>
							</div>
							<!-- /form row -->
	
							<!-- form row -->
							<div class="gt-form-row gt-width-66">
								<label>Descrição</label> <input id="descrClassificacao" name="descrClassificacao" type="text" class="gt-form-text" value="${exClass.descrClassificacao}"/>
							</div>
							<!-- /form row -->
	
							<!-- form row -->
							<div class="gt-form-row gt-width-66">
								<label>Observação</label> <input id="obs" name="obs" type="text" class="gt-form-text" value="${exClass.obs}"/>
							</div>
							<!-- /form row -->
						</div>
					</ww:form>
				</div>
				<div id="divBotoesClassificacao">
					<!-- form row -->
					<div class="gt-form-row">
						<a id="btGravarClassificacao" class="gt-btn-large gt-btn-left" style="cursor: pointer;" onclick="javascript:gravarClassificacao()">Gravar</a>
							<p class="gt-cancel">
							<c:choose>
								<c:when test="${acao == 'editar_classificacao'}">
										ou <ww:a href="excluir.action?codificacao=${exClass.codificacao}">excluir</ww:a>
								</c:when>
								<c:otherwise>
										ou <ww:a href="listar.action">cancelar</ww:a>
								</c:otherwise>
							</c:choose>
							</p>
						
					</div>
					<!-- /form row -->
					<c:if test="${acao == 'editar_classificacao'}">
						<!-- form row -->
						<div class="gt-form-row">
							<a id="btAddVia" class="gt-btn-large gt-btn-left" style="cursor: pointer;" onclick="javascript:adicionarVia()">Adicionar Via</a>
						</div>
						<!-- /form row -->
					</c:if>
				</div>
			</div>
		</div>
	</div>
	
	<c:if test="${acao == 'editar_classificacao'}">
		<c:if test="${numeroDeVias > 0}">
			<h2 class="gt-form-head">Vias</h2>
		</c:if>
		
		<c:forEach items="${exClass.exViaSet}" var="via">
			
		<div id="divVias">
			<div class="gt-bd clearfix">
				<div class="gt-content">
		
					<div class="gt-form gt-content-box">
						<div class="clearfix">
							<!-- left column -->
								<div class="gt-left-col">
									<!-- form row -->
									<div class="gt-form-row gt-width-66">
										<label>Via ${via.codVia}</label>
									</div>
									<!-- /form row -->
									<!-- form row -->
									<div class="gt-form-row gt-width-66">
										<label>Destino</label>${via.exTipoDestinacao.descrTipoDestinacao}
									</div>
									<!-- /form row -->
									<!-- form row -->
									<div class="gt-form-row gt-width-66">
										<label>Arquivo Corrente</label>${via.temporalidadeCorrente.descTemporalidade}
									</div>
									<!-- /form row -->
									<!-- form row -->
									<div class="gt-form-row gt-width-66">
										<label>Arquivo Intermediário</label>${via.temporalidadeIntermediario.descTemporalidade}
									</div>
									<!-- /form row -->
									<!-- form row -->
									<div class="gt-form-row gt-width-66">
										<label>Destinação Final</label>${via.exDestinacaoFinal.descrTipoDestinacao}
									</div>
									<!-- /form row -->
									<!-- form row -->
									<div class="gt-form-row gt-width-66">
										<label>Observação</label>
										<c:choose>
											<c:when test="${empty via.obs or via.obs==''}">
												Sem observações definidas
											</c:when>
											<c:otherwise>
												${via.obs}
											</c:otherwise>
										</c:choose>
									</div>
									<!-- /form row -->
								</div>
						</div>
						<!-- form row -->
						<div class="gt-form-row">
							<a id="btEditarVia" class="gt-btn-large gt-btn-left" style="cursor: pointer;" onclick="javascript:editarVia()">Editar Via</a>
							<p class="gt-cancel">
								ou <ww:a href="excluir.action?codificacao=${exClass.codificacao}">excluir via</ww:a>
							</p>
						</div>
						<!-- /form row -->
					</div>
				</div>
			</div>
		</div>	



		</c:forEach>

	</c:if>
</siga:pagina>
